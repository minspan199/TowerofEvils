package com.michael.pan.eviltower.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.data.DataSaving;
import com.michael.pan.eviltower.data.GameDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_UPDATE_TIME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.HAVE_SENT_NOTIFICATION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.KEY_USER_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.KEY_USER_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.PRIMARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.USER_LOADER_PROJECTION;

public class DatabaseQueryWork extends Worker {


	private GameDbHelper dbHelper;
	private int userCount = 0;
	private String userName, userId;
	private String TAG = "DatabaseQueryWork:";
	private SharedPreferences sharedPreferences;
	public DatabaseQueryWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	@NonNull
	@Override
	public Result doWork() {

		Cursor cursor;
		String sortOrder = COLUMN_UPDATE_TIME + " ASC";
		dbHelper = new GameDbHelper(getApplicationContext());
		int count = 0;
		cursor = dbHelper.getReadableDatabase().query(PRIMARY_TABLE_NAME, USER_LOADER_PROJECTION, null, null, null, null, sortOrder);
		if (cursor != null && cursor.moveToLast()){
			userName = cursor.getString(INDEX_USER_NAME);
			userId = cursor.getString(INDEX_USER_ID);
			count = cursor.getCount();
		}else {
//			Log.i(TAG, "Work failed due to empty loaded database.");
			return Result.failure();
		}

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		JSONObject json;
		String s = sharedPreferences.getString(getApplicationContext().getString(R.string.pref_user_bonus_key), "[]");
//		Log.i("DatabseQueryWork: (by " + userId, s);
		if (s.equals("[]")) setDefaultUserBonusPreferences(count, cursor);
		try {
			JSONArray jsonArray = new JSONArray(s);
			for (int i = 0; i < jsonArray.length(); i++) {
				json = jsonArray.getJSONObject(i);
				String id = json.getString(COLUMN_ID);
				String updateTime = json.getString(COLUMN_UPDATE_TIME);
				boolean sent = json.getBoolean(HAVE_SENT_NOTIFICATION);
//				Log.i("user:", id +"and sent"+sent + updateTime);
				if (id.equals(userId) && !updateTime.equals(DataSaving.getCurrentDate())) {
					// defaulted if the date is not the same as recorded.
					setDefaultUserBonusPreferences(count, cursor);
//					Log.i("User Json: ", "Default values loaded for shared preferences");
				}
				if (id.equals(userId) && sent) return Result.failure();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			cursor.close();
			dbHelper.close();
		}

		Data output = new Data.Builder()
			.putString(KEY_USER_NAME, userName)
			.putString(KEY_USER_ID, userId)
			.build();
		return Result.success(output);
	}

	private void setDefaultUserBonusPreferences(int count, Cursor cursor) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		JSONArray jsonArray = new JSONArray();
		try{
			for (int i = 0; i < count;i++){
				cursor.moveToPosition(i);
				JSONObject json = new JSONObject();
				json.put(COLUMN_NAME, cursor.getString(INDEX_USER_NAME));
				json.put(COLUMN_ID, cursor.getString(INDEX_USER_ID));
				json.put(COLUMN_UPDATE_TIME, DataSaving.getCurrentDate());
				json.put(HAVE_SENT_NOTIFICATION, false);
				jsonArray.put(i, json);
			}
		}catch (JSONException e){
			e.printStackTrace();
		}
		editor.putString(getApplicationContext().getString(R.string.pref_user_bonus_key), jsonArray.toString()).apply();
		editor.clear();
	}
}
