package com.michael.pan.eviltower.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.bumptech.glide.Glide;
import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.adapters.UserTreasureGridViewAdapter;
import com.michael.pan.eviltower.data.DataSaving;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.databinding.ActivityUserDetailsBinding;
import com.michael.pan.eviltower.services.DatabaseUpdateTask;
import com.michael.pan.eviltower.services.LocaleManager;
import com.michael.pan.eviltower.utilities.AnimUtil;
import com.michael.pan.eviltower.utilities.GameHelperUtil;
import com.michael.pan.eviltower.utilities.ImageUtil;
import com.michael.pan.eviltower.utilities.JSONUtil;
import com.michael.pan.eviltower.views.LoadingScreenView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.adapters.UserTreasureGridViewAdapter.treasures;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_DIFFICULTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_SAVING_SLOT;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ATTACK;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DEFENSE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DIFFICULTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ENERGY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_EXPERIENCE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_FLOOR;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_UPDATE_TIME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USER_ICON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_MAP_JSON_STRING;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_TREASURE_JSON_STRING;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_USER_DATA_JSON_STRING;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.ID_USER_DETAIL_LOADER;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_RECORD_TIME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_MAP;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_TREASURES;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.LOCATION_PRIMARY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.LOCATION_SECONDARY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.LOCATION_THIRD;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.USER_LOADER_PROJECTION;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_EASY;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_HARD;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_MEDIUM;

public class UserDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, DatabaseUpdateTask.onTaskExecuted {
	private Uri uriForUserClicked;
	private String TAG = "UserDetailActivity: ";
	private JSONObject treasureJson = null, userDataJson = null;
	private String treasureList = null;
	private UserTreasureGridViewAdapter adapter;

	private String userName, userEnergy, userId, userExperience, userFloor, userAttack, userDefense, entryRecordTime, userLevel,
		userPositionX, userPositionY, userTreasuresJsonString, userDataJsonString, mapDaraJsonString, userIcon, difficulty, savingSlot;
	private Boolean startGame = false, ifNew = false;
	private DatabaseUpdateTask databaseUpdateAsyncTask;
	ActivityUserDetailsBinding activityUserDetailsBinding;
	private LoadingScreenView loadingScreenView = null;
	private int numOfColGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_details);
		activityUserDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_details);
		loadingScreenView = activityUserDetailsBinding.loadingScreenView;//Connect the instance to the xml definition.
		//initialize the text fields in the user detail UI.
		//get the user database idOfCOLUMN_ID for loading
		int id = getIntent().getIntExtra(COLUMN_ID, -1);
		numOfColGridView = getResources().getInteger(R.integer.grid_column_count);
		startGame = getIntent().getBooleanExtra(EvilTowerContract.TAG_START_GAME, false);
		ifNew = getIntent().getBooleanExtra(EvilTowerContract.IF_NEW_USER, false);
		if (startGame) LoadingScreen();// if starting the game, then show the loading screen directly!
		else LoadingDetails();

		if (getIntent().hasExtra(EXTRA_DIFFICULTY)) difficulty = getIntent().getStringExtra(EXTRA_DIFFICULTY);
		if (difficulty == null) difficulty = TAG_DIFFICULTY_MEDIUM;
		System.out.println("difficulty: "+difficulty);
		System.out.println("getIntent().hasExtra(EXTRA_DIFFICULTY):"+getIntent().hasExtra(EXTRA_DIFFICULTY));
		if (ifNew) FancyToast.makeText(this, getString(R.string.user_setup_ready), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false);

		if (getIntent().hasExtra(EXTRA_SAVING_SLOT)) savingSlot = getIntent().getStringExtra(EXTRA_SAVING_SLOT);
		else savingSlot = LOCATION_PRIMARY;

		switch (savingSlot){
				case LOCATION_SECONDARY:
					uriForUserClicked = EvilTowerContract.EvilTowerEntry.buildUriByUserId(String.valueOf(id), 1);
					break;
				case LOCATION_THIRD:
					uriForUserClicked = EvilTowerContract.EvilTowerEntry.buildUriByUserId(String.valueOf(id), 2);
					break;
				default:
					uriForUserClicked = EvilTowerContract.EvilTowerEntry.buildUriByUserId(String.valueOf(id), 0);
					break;
		}

		adapter = new UserTreasureGridViewAdapter(this);
		activityUserDetailsBinding.userDetailTreasureLayout.treasureGridView.setNumColumns(numOfColGridView);
		activityUserDetailsBinding.userDetailTreasureLayout.treasureGridView.setAdapter(adapter);
		activityUserDetailsBinding.userDetailTreasureLayout.treasureGridView.setOnItemClickListener((adapterView, view, position, id1) -> {
			String key = adapter.getItem(position);
			String value = "0";
			try {
				 value = treasures.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			AnimUtil.sendFlyMessage(activityUserDetailsBinding.popupDetails, GameHelperUtil.getHelpStrings(this, key, value), Color.BLACK);
		});
		LoaderManager.getInstance(this).initLoader(ID_USER_DETAIL_LOADER, null, this);
	}

	private void LoadingDetails() {
		activityUserDetailsBinding.userDetailViewHolder.setVisibility(View.VISIBLE);
		loadingScreenView.setVisibility(View.GONE);
		activityUserDetailsBinding.userNameDetailInfo.setVisibility(View.VISIBLE);
	}

	private void LoadingScreen() {
		activityUserDetailsBinding.userDetailViewHolder.setVisibility(View.GONE);
		activityUserDetailsBinding.userNameDetailInfo.setVisibility(View.GONE);
		loadingScreenView.setRunning();
		loadingScreenView.setVisibility(View.VISIBLE);

	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(LocaleManager.setLocale(newBase));
	}

	@NonNull
	@Override
	public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
		if (id == ID_USER_DETAIL_LOADER) {
			//extraUri is in the form as content://com.example.MagicTowerGame/magic_tower/#
			return new CursorLoader(this, uriForUserClicked, USER_LOADER_PROJECTION, null, null, null);
		}
		throw new RuntimeException("Loader Not Implemented For ID: " + id);
	}

	@Override
	public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
		if (data.getCount() != 0 && !data.isClosed()) {
			data.moveToFirst();//don't forget to move to the first item;
//			Log.i(TAG, "Number of items has been loaded: " + data.getCount());
			resolveData(data);
		}
//		else Log.i(TAG, "Number of items has been loaded: " + data.getCount());
		if (startGame){//a branch that start the game activity right after loading.
			startGame = false;
//			Log.i(TAG, "Loading finished, starting new game now.");
//			Log.i(TAG, "User items loaded: " + treasureList);
			new Handler().postDelayed(() -> {
				Intent intent = new Intent(UserDetailActivity.this, StartGameActivity.class);
				intent.putExtras(getBundleData());
				intent.putExtra(EXTRA_DIFFICULTY, difficulty);
				startActivity(intent);
				loadingScreenView.stopRunning();
				UserDetailActivity.this.finish();
			}, 500);
		} else updateUI();
	}

	private void updateUI() {
		activityUserDetailsBinding.userNameDetailInfo.setText(userName);
		activityUserDetailsBinding.userDetailInfoEnergy.setText(userEnergy);
		activityUserDetailsBinding.userDetailInfoExperience.setText(userExperience);
		activityUserDetailsBinding.userDetailInfoFloor.setText(userFloor);
		activityUserDetailsBinding.userDetailInfoAttack.setText(userAttack);
		activityUserDetailsBinding.userDetailInfoDefense.setText(userDefense);
		switch (difficulty) {
			case TAG_DIFFICULTY_EASY:
				activityUserDetailsBinding.difficultyTagDetail.setText(String.format("%s%s", getString(R.string.difficulty), getString(R.string.tag_easy)));
				activityUserDetailsBinding.difficultyTagDetail.setTextColor(getResources().getColor(R.color.material_light_green_accent_700));
				break;
			case TAG_DIFFICULTY_MEDIUM:
				activityUserDetailsBinding.difficultyTagDetail.setText(String.format("%s%s", getString(R.string.difficulty), getString(R.string.tag_medium)));
				activityUserDetailsBinding.difficultyTagDetail.setTextColor(getResources().getColor(R.color.material_yellow_accent_700));
				break;
			case TAG_DIFFICULTY_HARD:
				activityUserDetailsBinding.difficultyTagDetail.setText(String.format("%s%s", getString(R.string.difficulty), getString(R.string.tag_hard)));
				activityUserDetailsBinding.difficultyTagDetail.setTextColor(getResources().getColor(R.color.material_red_accent_700));
				break;
		}
		activityUserDetailsBinding.userDetailInfoRecordTime.setText(String.format("%s%s", this.getString(R.string.record_time_is), DataSaving.convertDate(entryRecordTime)));
		TypedArray userIconDrawables = getResources().obtainTypedArray(R.array.user_icon_drawables);
		Glide.with(this).load(userIconDrawables.getResourceId(Integer.parseInt(userIcon), 0)).into(activityUserDetailsBinding.userPortrait);
		userIconDrawables.recycle();
//		activityUserDetailsBinding.userPortrait.setImageBitmap(ImageUtil.getUserIcon(this, Integer.parseInt(userIcon)));
	}

	private void resolveData(Cursor data) {
		userName = data.getString(INDEX_USER_NAME);
		userDataJsonString = data.getString(INDEX_USER_DATA);
		userTreasuresJsonString = data.getString(INDEX_USER_TREASURES);
		mapDaraJsonString = data.getString(INDEX_USER_MAP);
		userId = data.getString(INDEX_USER_ID);
		entryRecordTime = data.getString(INDEX_RECORD_TIME);
		//System.out.println(userTreasuresJsonString);
		try {
			treasureJson = new JSONObject(userTreasuresJsonString);
			userDataJson = new JSONObject(userDataJsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (treasureJson != null) {
			try {
				activityUserDetailsBinding.userDetailInfoDeposit.setText(treasureJson.getString(this.getString(R.string.coins)));
			} catch (JSONException e) {
				e.printStackTrace();
//				System.out.println(e.getMessage());
			}
			try {
				treasureList = JSONUtil.getTreasuresFromJSON(this, EvilTowerContract.TAG_NONZERO, treasureJson.toString());
			} catch (JSONException e) {
				e.printStackTrace();
//				System.out.println(e.getMessage());
			}
			adapter.setTreasures(treasureList);//send data to adapter;
			activityUserDetailsBinding.userDetailTreasureLayout.treasureGridView.setAdapter(adapter);
		}

		if (userDataJson != null){
			try {
				userEnergy = userDataJson.getString(COLUMN_ENERGY);
				userAttack = userDataJson.getString(COLUMN_ATTACK);
				userFloor = userDataJson.getString(COLUMN_FLOOR);
				userLevel = userDataJson.getString(COLUMN_LEVEL);
				userDefense = userDataJson.getString(COLUMN_DEFENSE);
				userExperience = userDataJson.getString(COLUMN_EXPERIENCE);
				userIcon = userDataJson.getString(COLUMN_USER_ICON);
				if (userDataJson.has(COLUMN_DIFFICULTY)) difficulty = userDataJson.getString(COLUMN_DIFFICULTY);
				//overwrite difficulty from the extras.
			} catch (JSONException e) {
				e.printStackTrace();
			}
			System.out.println("Loaded difficulty:" + difficulty);
		}
	}

	@Override
	public void onLoaderReset(@NonNull Loader<Cursor> loader) {
		adapter.setTreasures(null);//send data to adapter;
		adapter.notifyDataSetChanged();
		activityUserDetailsBinding.userDetailTreasureLayout.treasureGridView.setAdapter(null);
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putString(EXTRA_TREASURE_JSON_STRING, userTreasuresJsonString);
		outState.putString(EXTRA_MAP_JSON_STRING, mapDaraJsonString);
		outState.putString(EXTRA_USER_DATA_JSON_STRING, userDataJsonString);
		outState.putString(EvilTowerContract.EXTRA_TREASURE_LIST, treasureList);
		outState.putString(EvilTowerContract.EXTRA_USER_DATA_JSON, userDataJson.toString());
		outState.putString(COLUMN_ID, userId);
		outState.putString(COLUMN_NAME, userName);
		outState.putString(COLUMN_EXPERIENCE, userExperience);
		outState.putString(COLUMN_FLOOR, userFloor);
		outState.putString(COLUMN_ATTACK, userAttack);
		outState.putString(COLUMN_DEFENSE, userDefense);
		outState.putString(COLUMN_ENERGY, userEnergy);
		outState.putString(COLUMN_UPDATE_TIME, entryRecordTime);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		userTreasuresJsonString = savedInstanceState.getString(EXTRA_TREASURE_JSON_STRING);
		mapDaraJsonString = savedInstanceState.getString(EXTRA_MAP_JSON_STRING);
		userDataJsonString = savedInstanceState.getString(EXTRA_USER_DATA_JSON_STRING);
		userId = savedInstanceState.getString(COLUMN_ID);
		userName = savedInstanceState.getString(COLUMN_NAME);
		userExperience = savedInstanceState.getString(COLUMN_EXPERIENCE);
		userFloor = savedInstanceState.getString(COLUMN_FLOOR);
		userAttack = savedInstanceState.getString(COLUMN_ATTACK);
		userDefense = savedInstanceState.getString(COLUMN_DEFENSE);
		userEnergy = savedInstanceState.getString(COLUMN_ENERGY);
		entryRecordTime = savedInstanceState.getString(COLUMN_UPDATE_TIME);
		String treasure = savedInstanceState.getString(EvilTowerContract.EXTRA_TREASURE_LIST);
		String userData = savedInstanceState.getString(EvilTowerContract.EXTRA_USER_DATA_JSON);
		try {
			if (treasure != null) treasureList = treasure;
			if (userData != null) userDataJson = new JSONObject(userData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		updateUI();
	}

//	public void RuntimePermissionForUser() {
//
//		Intent PermissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//		startActivityForResult(PermissionIntent, 0);
//
//	}

	public void startNewGame(View view) {
//		Log.i(TAG, "Starting over data for new game: ");
		databaseUpdateAsyncTask = new DatabaseUpdateTask(this, this);
		databaseUpdateAsyncTask.execute(userId, EvilTowerContract.TAG_ROLLBACK, LOCATION_PRIMARY);
	}

	public void resumeOldGame(View view) {
		Intent intent = new Intent(this, StartGameActivity.class);
		//Log.i(TAG, "Resuming game: " + treasureList.toString());
		intent.putExtras(getBundleData());
		startActivity(intent);
		this.finish();
	}

	private Bundle getBundleData() {//prepare the data to start game for user with ID
		Bundle bundle = new Bundle();
		bundle.putString(COLUMN_NAME, userName);
		bundle.putString(COLUMN_ID, userId);
		bundle.putString(COLUMN_ENERGY, userEnergy);
		bundle.putString(COLUMN_EXPERIENCE, userExperience);
		bundle.putString(COLUMN_FLOOR, userFloor);
		bundle.putString(COLUMN_ATTACK, userAttack);
		bundle.putString(COLUMN_DEFENSE, userDefense);
		bundle.putString(COLUMN_UPDATE_TIME, entryRecordTime);
		bundle.putString(COLUMN_LEVEL, userLevel);
		bundle.putString(EXTRA_TREASURE_JSON_STRING, treasureList);
		bundle.putString(EXTRA_USER_DATA_JSON_STRING, userDataJson.toString());
		bundle.putString(EXTRA_MAP_JSON_STRING, mapDaraJsonString);
		return bundle;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (LoaderManager.getInstance(this).hasRunningLoaders()) LoaderManager.getInstance(this).destroyLoader(ID_USER_DETAIL_LOADER);
		if (databaseUpdateAsyncTask != null) databaseUpdateAsyncTask.cancel(true);
	}

	@Override
	public void onDatabaseUpdated() {
		LoaderManager.getInstance(this).restartLoader(ID_USER_DETAIL_LOADER, null, UserDetailActivity.this);
		startGame = true;
	}

}
