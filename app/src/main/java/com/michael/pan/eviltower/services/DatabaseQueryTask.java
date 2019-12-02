package com.michael.pan.eviltower.services;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.michael.pan.eviltower.data.GameDbHelper;

import java.lang.ref.WeakReference;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.PRIMARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.SECONDARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.THIRD_TABLE_NAME;

public class DatabaseQueryTask extends AsyncTask<String, Void, Cursor> {

	private WeakReference<Context> activityReference;
	public Cursor cursor;
	public DatabaseQueryTask(Context context) {
		activityReference = new WeakReference<>(context);
	}
	protected GameDbHelper dbHelper;
	@Override
	protected Cursor doInBackground(String... strings) {
		dbHelper = new GameDbHelper(activityReference.get());
//		String selectQuery = "SELECT * FROM " + PRIMARY_TABLE_NAME + " pn, "
//			+ SECONDARY_TABLE_NAME + " sn, " + THIRD_TABLE_NAME + " tn WHERE pn."
//			+ COLUMN_ID + " = " + strings[0] + " OR sn." + COLUMN_ID
//			+ " = " + strings[0] + " OR tn." + COLUMN_ID + " = "
//			+ strings[0];
//		String selection = "SELECT primary.*, secondary.*, third.* FROM " + PRIMARY_TABLE_NAME + " primary, " + SECONDARY_TABLE_NAME + " secondary, " +
//			THIRD_TABLE_NAME + " third WHERE " + COLUMN_ID + " = " + strings[0];
//		String selectQuery1 = "SELECT * FROM " + PRIMARY_TABLE_NAME + " WHERE " + COLUMN_ID + " = " + strings[0];
//		String selectQuery2 = "SELECT * FROM " + SECONDARY_TABLE_NAME + " WHERE " + COLUMN_ID + " = " + strings[0];
//		String selectQuery3 = "SELECT * FROM " + THIRD_TABLE_NAME + " WHERE " + COLUMN_ID + " = " + strings[0];
		String query = "SELECT * FROM " + strings[1] + " WHERE " + COLUMN_ID + " = " + strings[0];
//		cursor = dbHelper.getReadableDatabase().rawQuery(selectQuery1, null);
//		cursor = dbHelper.getReadableDatabase().rawQuery(selectQuery2, null);
//		cursor = dbHelper.getReadableDatabase().rawQuery(selectQuery3, null);
		cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
//		dbHelper.close();
		return cursor;
	}
}
