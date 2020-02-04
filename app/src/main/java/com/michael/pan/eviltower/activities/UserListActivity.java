package com.michael.pan.eviltower.activities;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.adapters.UserListAdapter;
import com.michael.pan.eviltower.data.DbMaintenance;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.services.DatabaseUpdateTask;
import com.michael.pan.eviltower.services.LocaleManager;
import com.michael.pan.eviltower.services.UserListTouchHelper;
import com.michael.pan.eviltower.utilities.GameUtil;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.michael.pan.eviltower.data.EvilTowerContract.DIFFICULTY_TEXT_TRANSITION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_STATISTICS;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_BOOL_NEW_GAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_CLICKED_LOAD_AT_MAIN;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_CLICKED_NEW_AT_MAIN;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.USER_LOADER_PROJECTION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.buildUriByUserId;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_CROWN;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DELETE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DETAILS;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_EMPTY_LIST;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_INSERT_NEW_USER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_NEW_USER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_RESUME;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_START_GAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.USER_ICON_TRANSITION;
import static com.michael.pan.eviltower.data.EvilTowerContract.USER_NAME_TRANSITION;

public class UserListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, UserListAdapter.GameListOnClickListener, DatabaseUpdateTask.onTaskExecuted, DatabaseUpdateTask.onNewUserCreated {

	private UserListAdapter mAdapter;
	private RecyclerView mRecyclerView;
	private ProgressBar pb;
	private String[] mItemValues, mItemLabels;
	private LinearLayout emptyDatabaseMessage;
	private static final int ID_USER_LIST_LOADER = 100;
	private int mPosition = RecyclerView.NO_POSITION;
	private String TAG = "user-list-activity: ";
	private Boolean createNew = false;
	private TextView inputBoxMessage;
	public List<Integer> positionIdList = new ArrayList<>();//An index layer for swiping and removing of ItouchHelper.
	Spinner difficultySettings;//using findviewbyid will cause null context error??!!
	String difficultyTag;
	//	ArrayList<Integer> userIconDrawables;
	AsyncTask<String, Void, Integer> insertUser;
	private int numOfColumn;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().hasExtra(EXTRA_BOOL_NEW_GAME)) {
			String extra = getIntent().getStringExtra(EXTRA_BOOL_NEW_GAME);
			if (extra != null && extra.equals(EXTRA_CLICKED_NEW_AT_MAIN)) createNew = true;
			else if (extra != null && extra.equals(EXTRA_CLICKED_LOAD_AT_MAIN)) createNew = false;
			setContentView(R.layout.activity_user_list);
			inputBoxMessage = findViewById(R.id.error_message);
			pb = findViewById(R.id.user_list_pb);
			mRecyclerView = findViewById(R.id.game_list_view);
			emptyDatabaseMessage = findViewById(R.id.input_new_user_box);
			numOfColumn = getResources().getInteger(R.integer.user_list_column);
			GridLayoutManager layoutManager = new GridLayoutManager(this, numOfColumn);
			mRecyclerView.setLayoutManager(layoutManager);
			mRecyclerView.setHasFixedSize(true);
			// Create the ArrayList of Sports objects with the titles and
			// information about each sport
			TypedArray userIconDrawables = getResources().obtainTypedArray(R.array.user_icon_drawables);
			mAdapter = new UserListAdapter(this, this, userIconDrawables);
			mRecyclerView.setAdapter(mAdapter);
			LoaderManager.getInstance(this).initLoader(ID_USER_LIST_LOADER, null, this);
			difficultySettings = findViewById(R.id.difficulty_settings);
			mItemValues = getResources().getStringArray(R.array.difficulty_values);
			mItemLabels = getResources().getStringArray(R.array.difficulty_labels);
			ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, mItemLabels);
			difficultySettings.setAdapter(adapter);

//			userIconDrawables.clear();
			// Recycle the typed array.
//			userIconDrawables.recycle();
		}
	}

	@NonNull
	@Override
	public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
		switch (id) {
			case ID_USER_LIST_LOADER:
				/* Sort order: Ascending by ID */
				String sortOrder = COLUMN_ID + " ASC";
//				content://com.michael.pan.MagicTower/magic_tower
//				Log.i(TAG, CONTENT_URI.toString());
				return new CursorLoader(this, buildUriByUserId(null, -1), USER_LOADER_PROJECTION, null, null, sortOrder);
			default:
				throw new RuntimeException("Loader Not Implemented: " + id);
		}
	}

	@Override
	public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

		pb.setVisibility(View.GONE);
		if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
		mRecyclerView.smoothScrollToPosition(mPosition);
		if (data != null && data.getCount() != 0) {
			int count = data.getCount();
			/*If the onLoadFinished() method passes you a null cursor,
			then that means that the ContentProvider's query() method has returned null.
			You need to fix your query() method so that it doesn't return null in this case.*/
			if (createNew) showErrorMessage(TAG_NEW_USER);
			else {
				positionIdList = new ArrayList<>(count);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					positionIdList = IntStream.rangeClosed(0, count - 1).boxed().collect(Collectors.toList());
				} else {
					for (int i = 0; i < count; i++) {
						positionIdList.add(i);
					}
				}
//			System.out.println(positionIdList.toString());
				mAdapter.swapCursor(data, positionIdList); // push the cursor to the RecyclerView Adapter
				showUserLists();
				UserListTouchHelper touchHelper = new UserListTouchHelper(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
					ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
					positionIdList, mAdapter);
				new ItemTouchHelper(touchHelper).attachToRecyclerView(mRecyclerView);
//			Log.i(TAG, "Number of items has been loaded: " + data.getCount());
			}
		} else showErrorMessage(TAG_EMPTY_LIST);
//		else{
//			try {
////				Log.i(TAG, "Number of items has been loaded: " + data.getCount());
//			}catch (Exception e){
////				Log.i(TAG, "A Null cursor was returned: " + e.getMessage());
//			}
//			showErrorMessage("empty-list");
//		}
	}

	private void showUserLists() {
		mRecyclerView.setVisibility(View.VISIBLE);
		emptyDatabaseMessage.setVisibility(View.GONE);
	}

	private void showErrorMessage(String s) {
		mRecyclerView.setVisibility(View.INVISIBLE);
		emptyDatabaseMessage.setVisibility(View.VISIBLE);
		if (s.equals(TAG_EMPTY_LIST)) inputBoxMessage.setText(getString(R.string.error_empty));
		else if (s.equals(TAG_NEW_USER))
			inputBoxMessage.setText(getString(R.string.input_new_name_message));
	}

	@Override
	public void onLoaderReset(@NonNull Loader<Cursor> loader) {
		mAdapter.swapCursor(null, positionIdList);
	}

	@Override
	public Resources.Theme getTheme() {
		Resources.Theme theme = super.getTheme();
		if (createNew) theme.applyStyle(R.style.MyAlertDialogStyle, true);
		return theme;
	}

	@Override
	protected void attachBaseContext(Context newBase) {//language settings here
		super.attachBaseContext(LocaleManager.setLocale(newBase));
	}

	@Override
	public void onClick(int id, String data, String instruction) {//adapter call backs
		switch (instruction) {
			case TAG_RESUME:
				startGame(id, true);
				break;
			case TAG_DELETE:
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
					.setTitleText(getString(R.string.alert_delete_title))
					.setContentText(getString(R.string.alert_delete_content))
					.setCancelText(getString(R.string.alert_false))
					.setConfirmText(getString(R.string.alert_positive))
					.showCancelButton(true)
					.setCancelClickListener(sDialog -> {
						// reuse previous dialog instance, keep widget user state, reset them if you need
						sDialog.setTitleText(getString(R.string.alert_cancel_title))
							.setContentText(getString(R.string.alert_cancel_content))
							.setConfirmText(getString(R.string.alert_confirm_positive))
							.showCancelButton(false)
							.setCancelClickListener(null)
							.setConfirmClickListener(null)
							.changeAlertType(SweetAlertDialog.ERROR_TYPE);
					})
					.setConfirmClickListener(sDialog -> {
						new DatabaseUpdateTask(this, this).execute(String.valueOf(id), TAG_DELETE);
						sDialog.setTitleText(getString(R.string.alert_deleted_title))
							.setContentText(getString(R.string.alert_deleted_content))
							.setConfirmText(getString(R.string.alert_deleted_positive))
							.showCancelButton(false)
							.setCancelClickListener(null)
							.setConfirmClickListener(null)
							.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
					})
					.show();
				break;
			case TAG_DETAILS:
				startGame(id, false);
				break;
			case TAG_CROWN:
				Intent intent = new Intent(this, GameOverActivity.class);
				intent.putExtra(EXTRA_STATISTICS, data);
				startActivity(intent);
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + instruction);
		}
	}

	private void startGame(int userId, boolean isStartingGame) {
		Intent intent = new Intent(UserListActivity.this, UserDetailActivity.class);
		intent.putExtra(TAG_START_GAME, isStartingGame);
		intent.putExtra(COLUMN_ID, userId);
		intent.putExtra(EvilTowerContract.EXTRA_DIFFICULTY, difficultyTag);
		intent.putExtra(EvilTowerContract.IF_NEW_USER, true);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			if (!isStartingGame) {
				ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
					Pair.create(mAdapter.holder.userIcon, USER_ICON_TRANSITION),
					Pair.create(mAdapter.holder.userNameInList, USER_NAME_TRANSITION),
					Pair.create(mAdapter.holder.difficulty, DIFFICULTY_TEXT_TRANSITION));
				startActivity(intent, options.toBundle());
			} else startActivity(intent);
		} else startActivity(intent);
		finish();
	}

	public void createNewUser(View view) {//ok button onclick for creating new users

		TextView newUser = findViewById(R.id.text_input_nickname);
		int spinnerPosition = difficultySettings.getSelectedItemPosition();
		difficultyTag = mItemValues[spinnerPosition];
		final String newUserName = newUser.getText().toString();
//		System.out.println(newUserName);
		if (!newUserName.equals("")) {
			try {
				String newName = newUser.getText().toString();
				int newId = GameUtil.getIdfromName(newName);
				if (mAdapter.idList.contains(String.valueOf(newId))) {
					FancyToast.makeText(this, getString(R.string.error_existed_id), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
					return;
				}
				ContentValues values = DbMaintenance.getDefaultContentValues(this, newId, newUserName);
				insertUser = new DatabaseUpdateTask(this, values, (DatabaseUpdateTask.onNewUserCreated) this).execute(String.valueOf(newId), TAG_INSERT_NEW_USER);
//			Log.i(TAG, "New user ID:" + newId);
			} catch (Exception e) {
				FancyToast.makeText(this, getString(R.string.error_invalid_user_name) + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
			}
		} else {
			FancyToast.makeText(this, getString(R.string.input_a_name), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (insertUser != null) insertUser.cancel(true);
		LoaderManager.getInstance(this).destroyLoader(ID_USER_LIST_LOADER);
	}

	@Override
	public void onDatabaseUpdated() {
		LoaderManager.getInstance(this).restartLoader(ID_USER_LIST_LOADER, null, this);
	}

	@Override
	public void onUserCreated(Integer newId) {
		startGame(newId, true);
	}
}

