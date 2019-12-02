package com.michael.pan.eviltower.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ComponentActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.adapters.ListPopupWindowAdapter;
import com.michael.pan.eviltower.adapters.UserTreasureGridViewAdapter;
import com.michael.pan.eviltower.data.FloorData;
import com.michael.pan.eviltower.data.GameLiveDataViewModel;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.data.MapData;
import com.michael.pan.eviltower.databinding.ActivityStartGameBinding;
import com.michael.pan.eviltower.entities.Door;
import com.michael.pan.eviltower.entities.Enemy;
import com.michael.pan.eviltower.entities.Equipment;
import com.michael.pan.eviltower.entities.NPC;
import com.michael.pan.eviltower.entities.Treasure;
import com.michael.pan.eviltower.entities.Warrior;
import com.michael.pan.eviltower.fragments.TransitionViewFragment;
import com.michael.pan.eviltower.services.DatabaseQueryTask;
import com.michael.pan.eviltower.services.DatabaseUpdateTask;
import com.michael.pan.eviltower.services.LocaleManager;
import com.michael.pan.eviltower.services.SnapshotTask;
import com.michael.pan.eviltower.services.YoYoAnim;
import com.michael.pan.eviltower.utilities.AnimUtil;
import com.michael.pan.eviltower.utilities.GameHelperUtil;
import com.michael.pan.eviltower.utilities.ImageUtil;
import com.michael.pan.eviltower.views.GameView;
import com.michael.pan.eviltower.views.SnapshotView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.michael.pan.eviltower.adapters.UserTreasureGridViewAdapter.treasures;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_DIFFICULTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_GAME_FINISHED;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_LAN_SETTINGS;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_SCORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ATTACK;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DEFENSE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DIFFICULTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ENERGY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_FLOOR;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_FLY_WING_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USER_ICON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRAS_EVENT_TYPE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_MAP_JSON_STRING;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_TREASURE_JSON_STRING;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_URI;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_USER_DATA_JSON_STRING;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_ATTACK_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_B_KEY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_COINS_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_DEFENSE_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_ENERGY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_EXPERIENCE_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_FLOOR_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_ICON_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_LEVEL_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_R_KEY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_Y_KEY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.LOCATION_PRIMARY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.LOCATION_SECONDARY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.LOCATION_THIRD;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.PRIMARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.SECONDARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_DOOR_EVENT;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_DOWNSTAIRS;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_ENEMY_EVENT;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_NO_PATH;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_NPC_EVENT;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_TREASURE_EVENT;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_UPSTAIRS;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATUS_LOADING;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.THIRD_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.floorName;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.floorNameZhCn;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.floorNameZhHk;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_EASY;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_HARD;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_MEDIUM;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_SAVE_USER_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_RELOAD_GAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_SAVE_GAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_SNAPSHOT_IN_THE_GAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_UPDATE;


public class StartGameActivity extends AppCompatActivity implements GameView.updateStates, TransitionViewFragment.OnTransitionViewClickedListener,
	DatabaseUpdateTask.onTaskExecuted, SnapshotTask.onTaskExecuted, View.OnClickListener {

	public String userName, userId, userTreasures, userData, userMapData, URIStringForCurrentUser, difficultyTag, fap, userIcon;
	Bundle extras;
	public static JSONObject treasuresJSON;
	private JSONObject userDataJson;
//	private static final String TAG = "StartGameActivity";
	private UserTreasureGridViewAdapter mAdapter;
	public static GameView gameView;
	SnapshotView snapshotView;
	public static ArrayList<FloorData> mapDataInArrays;
	private static String[] dataSummary = new String[3];
	public static MapData mapData;
	public static Warrior warrior;
	public static Door door = null;
	public String lanPref;
	public static JSONArray flyWingData;
	private SnapshotTask snapshotTask;
	private FragmentManager fragmentManager;
	int[][] arr00, arr01, arr02, arr03;
	ActivityStartGameBinding activityStartGameBinding;
	private int currentFloor;
	public static GameLiveDataViewModel gameLiveData;
	private String fpsPreference, gameViewPreference;
	public static boolean isFabOpen = false;
//	private static boolean isActivityAlive = false;
	private SharedPreferences preferences;
	private TypedArray userIconDrawables;
	private int numOfColGridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start_game);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			setupWindowAnimations();
//		}
		numOfColGridView = getResources().getInteger(R.integer.grid_column_count);
		mapDataInArrays = new ArrayList<>();

		activityStartGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_start_game);
		fragmentManager = getSupportFragmentManager();

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		fpsPreference = preferences.getString(this.getString(R.string.pref_fps_key), this.getString(R.string.pref_fps_default));
		gameViewPreference = preferences.getString(getString(R.string.pref_game_panel_key), getString(R.string.pref_game_panel_default));
		lanPref = preferences.getString(getString(R.string.pref_lan_key), getString(R.string.pref_lan_english_value));

		if (gameViewPreference.equals(getString(R.string.pref_landscape_value))) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		else if (gameViewPreference.equals(getString(R.string.pref_portrait_value))) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		else if (gameViewPreference.equals(getString(R.string.pref_sensor_value))) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

		if (!preferences.getBoolean(getString(R.string.pref_snapshot_key), true)) activityStartGameBinding.userInfoLayout.actionScreenshot.hide();

		extras = getIntent().getExtras();
		resolveExtraDataSets();
		initializeInstances();

		if (warrior.isFirstTime) showGameIntro();
		else showGameViewPanel();

		setUserInfo();

		@SuppressLint("SetTextI18n") Observer<List<Integer>> gameDataObserver = o -> {
			activityStartGameBinding.userInfoLayout.attackTextView.setText(o.get(INDEX_ATTACK_LIVEDATA).toString());
			activityStartGameBinding.userInfoLayout.defenseTextView.setText(o.get(INDEX_DEFENSE_LIVEDATA).toString());
			activityStartGameBinding.userInfoLayout.energyTextView.setText(o.get(INDEX_ENERGY_LIVEDATA).toString());
			activityStartGameBinding.userInfoLayout.coinTextView.setText(o.get(INDEX_COINS_LIVEDATA).toString());
			activityStartGameBinding.userInfoLayout.rKeyCounts.setText(o.get(INDEX_R_KEY_LIVEDATA).toString());
			activityStartGameBinding.userInfoLayout.bKeyCounts.setText(o.get(INDEX_B_KEY_LIVEDATA).toString());
			activityStartGameBinding.userInfoLayout.yKeyCounts.setText(o.get(INDEX_Y_KEY_LIVEDATA).toString());
			activityStartGameBinding.userInfoLayout.levelTextView.setText(o.get(INDEX_LEVEL_LIVEDATA).toString() + "/999");
			activityStartGameBinding.userInfoLayout.experienceTextView.setText(o.get(INDEX_EXPERIENCE_LIVEDATA).toString());
			if (lanPref.equals(getString(R.string.pref_lan_chinese_simplified_value))) activityStartGameBinding.floorTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.floor_is), floorNameZhCn[o.get(INDEX_FLOOR_LIVEDATA)]));
			else if (lanPref.equals(getString(R.string.pref_lan_chinese_traditional_value))) activityStartGameBinding.floorTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.floor_is), floorNameZhHk[o.get(INDEX_FLOOR_LIVEDATA)]));
			else activityStartGameBinding.floorTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.floor_is), floorName[o.get(INDEX_FLOOR_LIVEDATA)]));

//			System.out.println(lanPref);
//			if (o.get(INDEX_FLOOR_LIVEDATA) > gameLiveData.getMaxFloor()) gameLiveData.setMaxFloor(o.get(INDEX_FLOOR_LIVEDATA));
//			Log.i(TAG, "UI updated due to changes observed by LiveData View Model.");
		};
		gameLiveData.getGameLiveData().observe(this, gameDataObserver);
		Observer<JSONObject> gameTreasureJsonObserver = jsonObject -> mAdapter.setTreasures(jsonObject.toString());
		gameLiveData.treasureJsonLiveData.observe(this, gameTreasureJsonObserver);
		gameLiveData.userId = userId;
		gameLiveData.userName = userName;
		gameLiveData.difficulty = difficultyTag;
//		isActivityAlive = true;
		refreshDatabaseSummary();

//		try {
//			startGameFinishedActivity(this);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}

	private void refreshDatabaseSummary() {
		new LoadSavedDataSummary(this, PRIMARY_TABLE_NAME).execute(userId, PRIMARY_TABLE_NAME);
		new LoadSavedDataSummary(this, SECONDARY_TABLE_NAME).execute(userId, SECONDARY_TABLE_NAME);
		new LoadSavedDataSummary(this, THIRD_TABLE_NAME).execute(userId, THIRD_TABLE_NAME);
	}

	private void setUserInfo() {
		activityStartGameBinding.userNameInGame.setText(String.format("%s(%s)", userName, difficultyTag));
		switch (difficultyTag){
			case TAG_DIFFICULTY_EASY:
				activityStartGameBinding.userNameInGame.setTextColor(getResources().getColor(R.color.material_light_green_accent_700));
				break;
			case TAG_DIFFICULTY_MEDIUM:
				activityStartGameBinding.userNameInGame.setTextColor(getResources().getColor(R.color.material_yellow_accent_700));
				break;
			case TAG_DIFFICULTY_HARD:
				activityStartGameBinding.userNameInGame.setTextColor(getResources().getColor(R.color.material_red_accent_700));
				break;
		}
		TypedArray userIconDrawables = getResources().obtainTypedArray(R.array.user_icon_drawables);
		Glide.with(this).load(userIconDrawables.getResourceId(gameLiveData.getIcon(), 0)).into(activityStartGameBinding.userInfoLayout.userPortraitIcon);
//		activityStartGameBinding.userInfoLayout.userPortraitIcon.setImageBitmap(ImageUtil.getUserIcon(this, o.get(INDEX_ICON_LIVEDATA)));
		userIconDrawables.recycle();
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void setupWindowAnimations() {
		// Re-enter transition is executed when returning to this activity
		Slide slideTransition = new Slide();
		slideTransition.setSlideEdge(Gravity.START);
		slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
		getWindow().setReenterTransition(slideTransition);
		getWindow().setExitTransition(slideTransition);
	}

	private void showGameViewPanel() {
		activityStartGameBinding.gameInfoViewHolder.setVisibility(View.GONE);
		activityStartGameBinding.gameViewPanel.setVisibility(View.VISIBLE);
//		String s = preferences.getString(getApplicationContext().getString(R.string.pref_user_bonus_key), "[]");
//		if (!s.equals("[]")){
//			try {
//				JSONArray jsonArray = new JSONArray(s);
//				for (int i = 0; i < jsonArray.length(); i++) {
//					JSONObject json = jsonArray.getJSONObject(i);
//					String id = json.getString(COLUMN_ID);
//					boolean sent = json.getBoolean(HAVE_SENT_NOTIFICATION);
//					if (id.equals(userId) && !sent) AnimUtil.sendFlyMessageByType(this, activityStartGameBinding.bonusRemind, FLY_MESSAGE_FLY_IN);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
	}

	private void showGameIntro() {
		activityStartGameBinding.gameViewPanel.setVisibility(View.GONE);
		activityStartGameBinding.gameInfoViewHolder.setVisibility(View.VISIBLE);
		new DatabaseUpdateTask(this, this).execute(userId, TAG_UPDATE, LOCATION_PRIMARY);
	}

	@Override
	protected void attachBaseContext(Context newBase) {//language settings here
		super.attachBaseContext(LocaleManager.setLocale(newBase));
	}

	private void resolveExtraDataSets() {
		//Loading JSON data from extras below		bundle.putString(EXTRA_MAP_JSON_STRING, mapDaraJsonString);
		userName = extras.getString(COLUMN_NAME);
		userId = extras.getString(COLUMN_ID);
		if (extras.containsKey(EXTRA_DIFFICULTY)) difficultyTag = extras.getString(EXTRA_DIFFICULTY);
		else difficultyTag = TAG_DIFFICULTY_MEDIUM;
		userTreasures = extras.getString(EXTRA_TREASURE_JSON_STRING);
		userData = extras.getString(EXTRA_USER_DATA_JSON_STRING);
		userMapData = extras.getString(EXTRA_MAP_JSON_STRING);
		URIStringForCurrentUser = extras.getString(EXTRA_URI);
		try {
			treasuresJSON = new JSONObject(userTreasures);
			userDataJson = new JSONObject(userData);
//			JSONObject userMapDataJSON = new JSONObject(userMapData);
			if (userDataJson.getString(COLUMN_FLY_WING_DATA).equals("EMPTY")) flyWingData = new JSONArray();
			else flyWingData = new JSONArray(userDataJson.getString(COLUMN_FLY_WING_DATA));
//			System.out.println(userDataJson.toString()+">>>>>>>>>>>>>>>>>>>>>");
			if (userDataJson.has(COLUMN_DIFFICULTY)) difficultyTag = userDataJson.getString(COLUMN_DIFFICULTY);
			// if there is definition of difficulty in the database, overwrite difficulty!
		} catch (JSONException e) {
			e.printStackTrace();
//			Log.i(TAG, "Cannot resolve the bundle extras from JSON: " + e.getMessage());
		}
	}

	private void initializeInstances() {

		warrior = new Warrior(this);
		warrior.userId = userId;
//		mapData = new MapData(this, null);
		mapData = new MapData(this, userMapData);
		mapDataInArrays = mapData.getDataInArrays();
//		Log.i(TAG, "Number of floors loaded: " + mapDataInArrays.size());
		//Initialization of View Model
		gameLiveData = ViewModelProviders.of(this).get(GameLiveDataViewModel.class);
		gameLiveData.initialization(this, userDataJson, treasuresJSON);//including warrior set first time Flag.
		gameLiveData.difficulty = difficultyTag;//set the difficulty for the game livedata model.
		gameView = new GameView(this, this);
		//gameView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		gameView.setFloor(gameLiveData.getFloor());
//		gameView.setFloor(26);
		gameView.FPS = Integer.parseInt(fpsPreference);
		activityStartGameBinding.gameViewHolder.addView(gameView);
		updateFloorData();
		warrior.setX(gameLiveData.getPosition_X());
		warrior.setY(gameLiveData.getPosition_Y());

		mAdapter = new UserTreasureGridViewAdapter(this);
		mAdapter.setTreasures(treasuresJSON.toString());
		activityStartGameBinding.userTreasureLayout.treasureGridView.setNumColumns(numOfColGridView);
		activityStartGameBinding.userTreasureLayout.treasureGridView.setAdapter(mAdapter);
		activityStartGameBinding.userTreasureLayout.treasureGridView.setOnItemClickListener((adapterView, view, position, id) -> {
			String name = mAdapter.getItem(position);
			new Equipment(this, fragmentManager, activityStartGameBinding.popupMessage, name);
		});
		activityStartGameBinding.userTreasureLayout.treasureGridView.setOnItemLongClickListener((parent, view, position, id) -> {
			String name = mAdapter.getItem(position);
			String value = "0";
			try {
				value = treasures.getString(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			AnimUtil.sendFlyMessage(activityStartGameBinding.popupMessage, GameHelperUtil.getHelpStrings(getApplicationContext(), name, value), Color.YELLOW, 1);
			return true;
		});
		activityStartGameBinding.userInfoLayout.actionOpenList.setOnClickListener(this);
		activityStartGameBinding.userInfoLayout.actionSave.setOnClickListener(this);
		activityStartGameBinding.userInfoLayout.actionScreenshot.setOnClickListener(this);
		activityStartGameBinding.userInfoLayout.actionReload.setOnClickListener(this);
	}

	@Override
	public void updateStatesA(String s, int floor) {
		Bundle extras = new Bundle();
		extras.putString(EXTRAS_EVENT_TYPE, s);
		extras.putInt(EvilTowerContract.TAG_FLOOR, floor);
		extras.putString(EXTRA_LAN_SETTINGS, lanPref);
		switch (s){
			case STATUS_LOADING:
			case STATES_UPSTAIRS:
			case STATES_DOWNSTAIRS:
				TransitionViewFragment transitionFragment = new TransitionViewFragment(this);
				transitionFragment.setArguments(extras);
				fragmentManager.beginTransaction().replace(R.id.transition_view_holder, transitionFragment, getString(R.string.transition_view_tag)).commit();
				gameView.setDrawMap(false);
				activityStartGameBinding.gameViewHolder.setVisibility(View.GONE);
				if (floor != currentFloor){
					currentFloor = floor;
					gameView.setFloor(currentFloor);
					updateFloorData();
					//activityStartGameBinding.floorTextView.setText("FLOOR " + currentFloorData);
//					Log.i(TAG, "Updating floor data to Floor: " + currentFloor);
				}
				activityStartGameBinding.transitionViewHolder.setVisibility(View.VISIBLE);
				break;
			case STATES_NO_PATH:
				AnimUtil.sendFlyMessage(activityStartGameBinding.popupMessage, getString(R.string.no_path_to_go));
				break;
			default:
				AnimUtil.sendFlyMessage(activityStartGameBinding.popupMessage, "Unhandled status updateStatesA interface:" + s);
				break;
		}
	}

	private void updateFloorData() {
		arr00 = gameView.currentFloorData.getLayer00();
		arr01 = gameView.currentFloorData.getLayer01();
		arr02 = gameView.currentFloorData.getLayer02();
		arr03 = gameView.currentFloorData.getLayer03();
	}

	@Override
	public void updateStatesB(String s, int xTouch, int yTouch, int matrixValue, int floor) {
		Bundle extras = new Bundle();
		extras.putString(EXTRAS_EVENT_TYPE, s);
		extras.putInt(this.getString(R.string.floor), floor);
		extras.putInt(this.getString(R.string.xTouch), xTouch);
		extras.putInt(this.getString(R.string.yTouch), yTouch);
		extras.putInt(getString(R.string.matrix_value), matrixValue);
		extras.putInt(COLUMN_USER_ICON, gameLiveData.getIcon());
//		System.out.println(extras.getInt(getString(R.string.matrix_value)));
		switch (s){
			case STATES_NPC_EVENT:
				new NPC(getApplicationContext(), fragmentManager, activityStartGameBinding, extras);
				activityStartGameBinding.messageViewHolder.setVisibility(View.VISIBLE);
				break;
			case STATES_ENEMY_EVENT:
				new Enemy(this, extras, activityStartGameBinding.popupMessage, fragmentManager);
				break;
			case STATES_DOOR_EVENT:
				door = new Door(this, extras, activityStartGameBinding.popupMessage, true);
				break;
			case STATES_TREASURE_EVENT:
				new Treasure(this, this.activityStartGameBinding.popupMessage, extras);
				break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
//		isActivityAlive = true;
	}

	@Override
	protected void onStop() {
		//new DatabaseUpdateTask(this, this).execute(userId, "update");
				//gameView.gameThread.setRunning(false);
				//gameView.gameThread.interrupt();
//		isActivityAlive = false;
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		treasuresJSON = null;
		gameLiveData = null;
		gameView = null;
		mapData = null;
		door = null;
		flyWingData = null;
		mapDataInArrays = null;
	}

	@Override
	public void OnTransitionViewClicked() {
		activityStartGameBinding.transitionViewHolder.setVisibility(View.GONE);
		activityStartGameBinding.gameViewHolder.setVisibility(View.VISIBLE);
		clearFragment(getString(R.string.transition_view_tag));
		gameView.setDrawMap(true);
		gameView.handlingTransitionView = false;
	}

	public void clearFragment(String tag) {
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		if (fragment != null) fragmentManager.beginTransaction().remove(fragment).commit();
	}

	@Override
	public void onDatabaseUpdated() {
//		new Handler().postDelayed(() -> activityStartGameBinding.userInfoLayout.actionSave.show(), 100);
		AnimUtil.sendFlyMessage(activityStartGameBinding.popupMessage, getString(R.string.game_saved));
		refreshDatabaseSummary();
//		Log.i(TAG, "Database updated!");
	}

	public void takeSnapshot(View view) {//onclick
		snapshotView = new SnapshotView(this);
		gameView.setVisibility(View.GONE);
		activityStartGameBinding.gameViewHolder.addView(snapshotView);
		activityStartGameBinding.userInfoLayout.actionScreenshot.hide();
		activityStartGameBinding.userInfoLayout.actionOpenList.hide();
		activityStartGameBinding.userInfoLayout.actionSave.hide();
		activityStartGameBinding.userInfoLayout.actionReload.hide();
		snapshotTask = new SnapshotTask(this, this);
		new Handler().postDelayed(() -> snapshotTask.execute(TAG_SNAPSHOT_IN_THE_GAME), 500);
	}

	@Override
	public void snapshotTaken() {
		activityStartGameBinding.gameViewHolder.removeView(snapshotView);
		gameView.setVisibility(View.VISIBLE);
		activityStartGameBinding.userInfoLayout.actionOpenList.show();
		activityStartGameBinding.userInfoLayout.actionScreenshot.show();
		activityStartGameBinding.userInfoLayout.actionSave.show();
		activityStartGameBinding.userInfoLayout.actionReload.show();
		AnimUtil.sendFlyMessage(activityStartGameBinding.popupMessage, getString(R.string.snapshot_taken));
	}

	@Override
	public void snapshotTakenAtEnd(Context context) {

	}

	public void toggleFabList(View view) {//onclick
		if (isFabOpen) closeFabMenu();
		else showFabMenu();
	}

	private void showFabMenu(){//onclick
		AnimUtil.fabOpeningAnimation(this, activityStartGameBinding.userInfoLayout);
	}

	private void closeFabMenu(){//onclick
		AnimUtil.fabClosingAnimation(this, activityStartGameBinding.userInfoLayout);
	}

	public void saveGame(View view) {//onclick
		fap = EvilTowerContract.TAG_SAVE_GAME;
		initializePopupWindow(view);
	}

	public void skipGameIntro(View view) {//onclick
		new YoYoAnim(Techniques.ZoomOut, activityStartGameBinding.gameInfoViewHolder, 2000, this::showGameViewPanel).start();
		warrior.isFirstTime = false;
	}

	public void reloadGame(View view) {//onclick
		fap = EvilTowerContract.TAG_RELOAD_GAME;
		initializePopupWindow(view);
	}

	private void initializePopupWindow(View anchorView) {
		final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
		listPopupWindow.setWidth(600);
		listPopupWindow.setAnchorView(anchorView);
		ListPopupWindowAdapter listPopupWindowAdapter = new ListPopupWindowAdapter(this, dataSummary, position -> {
			listPopupWindow.dismiss();
			if (fap.equals(TAG_SAVE_GAME)){
				switch (position){
					case 0:
						if (!dataSummary[0].equals("No Data")) new DatabaseUpdateTask(StartGameActivity.this, StartGameActivity.this).execute(userId, TAG_UPDATE, LOCATION_PRIMARY);
						else new DatabaseUpdateTask(StartGameActivity.this, StartGameActivity.this).execute(userId, TAG_SAVE_USER_DATA, LOCATION_PRIMARY, userName);
						break;
					case 1:
						if (!dataSummary[1].equals("No Data")) new DatabaseUpdateTask(StartGameActivity.this, StartGameActivity.this).execute(userId, TAG_UPDATE, LOCATION_SECONDARY);
						else new DatabaseUpdateTask(StartGameActivity.this, StartGameActivity.this).execute(userId, TAG_SAVE_USER_DATA, LOCATION_SECONDARY, userName);
						break;
					case 2:
						if (!dataSummary[2].equals("No Data")) new DatabaseUpdateTask(StartGameActivity.this, StartGameActivity.this).execute(userId, TAG_UPDATE, LOCATION_THIRD);
						else new DatabaseUpdateTask(StartGameActivity.this, StartGameActivity.this).execute(userId, TAG_SAVE_USER_DATA, LOCATION_THIRD, userName);
						break;
					default:
						break;
				}
			} else if (fap.equals(TAG_RELOAD_GAME)){
				Intent intent = new Intent(StartGameActivity.this, UserDetailActivity.class);
				intent.putExtra(EvilTowerContract.TAG_START_GAME, true);
				intent.putExtra(COLUMN_ID, Integer.parseInt(userId));
				switch (position){
					case 0:
						intent.putExtra(EvilTowerContract.EXTRA_SAVING_SLOT, LOCATION_PRIMARY);
						break;
					case 1:
						intent.putExtra(EvilTowerContract.EXTRA_SAVING_SLOT, LOCATION_SECONDARY);
						break;
					case 2:
						intent.putExtra(EvilTowerContract.EXTRA_SAVING_SLOT, LOCATION_THIRD);
						break;
					default:
						break;
				}
				startActivity(intent);
				finish();
			}
		});
		listPopupWindowAdapter.tag = fap;
		listPopupWindow.setAdapter(listPopupWindowAdapter);
		listPopupWindow.show();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == activityStartGameBinding.userInfoLayout.actionOpenList.getId()) toggleFabList(v);
		else if (v.getId() == activityStartGameBinding.userInfoLayout.actionSave.getId()) saveGame(v);
		else if (v.getId() == activityStartGameBinding.userInfoLayout.actionReload.getId()) reloadGame(v);
		else if (v.getId() == activityStartGameBinding.userInfoLayout.actionScreenshot.getId()) takeSnapshot(v);
	}

	static class LoadSavedDataSummary extends DatabaseQueryTask{

		public int count = 0;
		String userDataJsonString, userFloor, userLevel;
		String savingSlot;
		JSONObject json;
		LoadSavedDataSummary(Context context, String savingSlot) {
			super(context);
			this.savingSlot = savingSlot;
		}

		@Override
		protected void onPostExecute(Cursor cursor) {
			super.onPostExecute(cursor);
			switch (savingSlot){
				case PRIMARY_TABLE_NAME:
					dataSummary[0] = "No Data";
					if (cursor != null && cursor.getCount() != 0) {
//				count = cursor.getCount();
						if (cursor.moveToFirst()) {
							userDataJsonString = cursor.getString(INDEX_USER_DATA);
							try {
								json = new JSONObject(userDataJsonString);
								userFloor = json.getString(COLUMN_FLOOR);
								userLevel = json.getString(COLUMN_LEVEL);
							} catch (JSONException e) {
								e.printStackTrace();
							} finally {
								cursor.close();
								this.cursor.close();
								this.dbHelper.close();
							}
							dataSummary[0] = "Level: " + userLevel + "; Floor: " + userFloor;
						}
					}
					break;
				case SECONDARY_TABLE_NAME:
					dataSummary[1] = "No Data";
					if (cursor != null && cursor.getCount() != 0) {
//				count = cursor.getCount();
						if (cursor.moveToFirst()) {
							userDataJsonString = cursor.getString(INDEX_USER_DATA);
							try {
								json = new JSONObject(userDataJsonString);
								userFloor = json.getString(COLUMN_FLOOR);
								userLevel = json.getString(COLUMN_LEVEL);
							} catch (JSONException e) {
								e.printStackTrace();
							} finally {
								cursor.close();
								this.cursor.close();
								this.dbHelper.close();
							}
							dataSummary[1] = "Level: " + userLevel + "; Floor: " + userFloor;
						}
					}
					break;
				case THIRD_TABLE_NAME:
					dataSummary[2] = "No Data";
					if (cursor != null && cursor.getCount() != 0) {
//				count = cursor.getCount();
						if (cursor.moveToFirst()) {
							userDataJsonString = cursor.getString(INDEX_USER_DATA);
							try {
								json = new JSONObject(userDataJsonString);
								userFloor = json.getString(COLUMN_FLOOR);
								userLevel = json.getString(COLUMN_LEVEL);
							} catch (JSONException e) {
								e.printStackTrace();
							} finally {
								cursor.close();
								this.cursor.close();
								this.dbHelper.close();
							}
							dataSummary[2] = "Level: " + userLevel + "; Floor: " + userFloor;
						}
					}
					break;
			}
			if (cursor != null) {
				cursor.close();
				this.cursor.close();
			}
			this.dbHelper.close();
		}
	}

	//for test use only
	private void startGameFinishedActivity(Context context) throws JSONException {
//		Sample sample = new Sample(R.color.sample_red, "Transitions");
//		final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants((Activity) context, false);
		Intent intent = new Intent(context, GameOverActivity.class);

		JSONObject jsonExtras = new JSONObject();
		jsonExtras.put(COLUMN_ID, gameLiveData.userId);
		jsonExtras.put(COLUMN_NAME, gameLiveData.userName);
		jsonExtras.put(COLUMN_ATTACK, gameLiveData.getAttack());
		jsonExtras.put(COLUMN_DEFENSE, gameLiveData.getDefense());
		jsonExtras.put(COLUMN_ENERGY, gameLiveData.getEnergy());
		jsonExtras.put(COLUMN_LEVEL, gameLiveData.getLevel());
		jsonExtras.put(COLUMN_DIFFICULTY, gameLiveData.difficulty);
		int score = (int) ((gameLiveData.getAttack()*1.2f + gameLiveData.getEnergy()*0.5f + gameLiveData.getDefense()*1f)*gameLiveData.getLevel()/100f);
		jsonExtras.put(EXTRA_SCORE, score);
		intent.putExtra(EXTRA_GAME_FINISHED, jsonExtras.toString());
//		ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pairs);
//		intent.putExtra("sample", sample);
//		context.startActivity(intent, transitionActivityOptions.toBundle());
		context.startActivity(intent);
		Activity activity = (Activity) context;
		activity.finish();
	}
}
