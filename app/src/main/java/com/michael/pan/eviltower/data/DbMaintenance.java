package com.michael.pan.eviltower.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.michael.pan.eviltower.activities.UserListActivity;

import org.json.JSONException;

import static com.michael.pan.eviltower.data.DataSaving.getContentValuesFromCurrentData;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_MAP_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_TREASURES_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_UPDATE_TIME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USERDATA_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.LOCATION_PRIMARY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.LOCATION_SECONDARY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.LOCATION_THIRD;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.buildUriByUserId;

public class DbMaintenance {

	public static void updateRowOfDatabase(Context context, String userId, ContentValues cv) {

		Uri uri = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 3);
		context.getContentResolver().update(uri, cv, COLUMN_ID + " = " + userId, null);
//		Log.i("DbMaintenance: ", "Data updated for user ID:" + userId);
	}

	public static void gameCompleting(Context context, String userId, ContentValues cv) {
		Uri uriPrimary = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 0);
		Uri uriSecondary = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 1);
		Uri uriThird = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 2);
		context.getContentResolver().update(uriPrimary, cv, COLUMN_ID + " = " + userId, null);
//		Log.i("DbMaintenance: ", "Data updated for user ID:" + userId);
	}


	public static void deleteUser(Context context, String userId) {
		Uri uriPrimary = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 0);
		Uri uriSecondary = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 1);
		Uri uriThird = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 2);
		context.getContentResolver().delete(uriPrimary, null, null);
		context.getContentResolver().delete(uriSecondary, null, null);
		context.getContentResolver().delete(uriThird, null, null);
//		Log.i("DbMaintenance: ", "Data deleted for user ID:" + userId);
	}

	public static void updateDatabase(Context context, String[] args, final boolean rollingBack) {

		String userId = args[0], updatingSlot = args[2];
		ContentValues values = null;
		Uri uriPrimary = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 0);
		Uri uriSecondary = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 1);
		Uri uriThird = EvilTowerContract.EvilTowerEntry.buildUriByUserId(userId, 2);
		if (rollingBack) {
			values = getDefaultContentValues(context);
//			Log.i("DnMaintenance: ", "rolling back to original state!");
			context.getContentResolver().delete(uriSecondary, COLUMN_ID + " = " + userId, null);
			context.getContentResolver().delete(uriThird, COLUMN_ID + " = " + userId, null);
		} else {
			try {
				values = getContentValuesFromCurrentData(context);
				//System.out.println("this received is :"+values.get(COLUMN_TREASURES_JSON));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		//new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_LEVEL}, COLUMN_STATUS);
		switch (updatingSlot) {
			case LOCATION_PRIMARY:
				context.getContentResolver().update(uriPrimary, values, COLUMN_ID + " = " + userId, null);
				break;
			case LOCATION_SECONDARY:
				context.getContentResolver().update(uriSecondary, values, COLUMN_ID + " = " + userId, null);
				break;
			case LOCATION_THIRD:
				context.getContentResolver().update(uriThird, values, COLUMN_ID + " = " + userId, null);
				break;
		}
//		Log.i("DbMaintenance: ", "Data updated for user ID:" + userId);
	}


	public static void rollBackDatabase(Context context, String[] args) {
		args[2] = LOCATION_PRIMARY;
		updateDatabase(context, args, true);
	}

	private static ContentValues getDefaultContentValues(Context context) {
		//Insert Property Step 5
		ContentValues values = new ContentValues();
		values.put(COLUMN_USERDATA_JSON, EvilTowerContract.EvilTowerEntry.getDefaultValues(context, COLUMN_USERDATA_JSON));
		values.put(COLUMN_TREASURES_JSON, EvilTowerContract.EvilTowerEntry.getDefaultValues(context, COLUMN_TREASURES_JSON));
		values.put(COLUMN_MAP_JSON, EvilTowerContract.EvilTowerEntry.getDefaultValues(context, COLUMN_MAP_JSON));
		values.put(COLUMN_LEVEL, EvilTowerContract.EvilTowerEntry.getDefaultValues(context, COLUMN_LEVEL));
		values.put(COLUMN_UPDATE_TIME, EvilTowerContract.EvilTowerEntry.getDefaultValues(context, COLUMN_UPDATE_TIME));
		return values;
	}


	public static ContentValues getDefaultContentValues(Context context, int newId, String newUserName) {
		//COLUMN_ID, COLUMN_NAME, COLUMN_LEVEL}, COLUMN_STATUS
		//COLUMN_STATUS = {"attack", "defense", "life", "layer", "positionX", "positionY"};
//		{_ID, COLUMN_NAME, COLUMN_UPDATE_TIME, COLUMN_ID, COLUMN_LEVEL,
//			COLUMN_ATTACK, COLUMN_DEFENSE, COLUMN_ENERGY, COLUMN_FLOOR, COLUMN_POSITION_X, COLUMN_POSITION_Y};

		//Insert Property Step 6
		ContentValues values = getDefaultContentValues(context);
		values.put(COLUMN_NAME, newUserName);
		values.put(COLUMN_ID, newId);
		return values;
	}

	public static void insertData(Context context, String[] strings) {
		Uri uriPrimary = EvilTowerContract.EvilTowerEntry.buildUriByUserId(strings[0], 0);
		Uri uriSecondary = EvilTowerContract.EvilTowerEntry.buildUriByUserId(strings[0], 1);
		Uri uriThird = EvilTowerContract.EvilTowerEntry.buildUriByUserId(strings[0], 2);
		ContentValues values = new ContentValues();
		try {
			values = getContentValuesFromCurrentData(context);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		values.put(COLUMN_NAME, strings[3]);
		values.put(COLUMN_ID, strings[0]);
		switch (strings[2]) {
			case LOCATION_PRIMARY:
				context.getContentResolver().insert(uriPrimary, values);
				break;
			case LOCATION_SECONDARY:
				context.getContentResolver().insert(uriSecondary, values);
				break;
			case LOCATION_THIRD:
				context.getContentResolver().insert(uriThird, values);
				break;
		}
	}

	public static void insertNewUser(Context context, String id, ContentValues contentValues) {
		context.getContentResolver().insert(buildUriByUserId(id, 0), contentValues);
	}
}
