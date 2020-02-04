package com.michael.pan.eviltower.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.michael.pan.eviltower.data.EvilTowerContract.CONTENT_AUTHORITY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.PRIMARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.SECONDARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.THIRD_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.PATH;
import static com.michael.pan.eviltower.data.EvilTowerContract.PATH_PRIMARY;
import static com.michael.pan.eviltower.data.EvilTowerContract.PATH_SECONDARY;
import static com.michael.pan.eviltower.data.EvilTowerContract.PATH_THIRD;

public class EvilTowerProvider extends ContentProvider {
	private static final int CODE_USER_PRIMARY = 123;
	private static final int CODE_USER_SECONDARY = 124;
	private static final int CODE_USER_THIRD = 125;
	private static final int CODE_USER_ALL_LOCATIONS = 126;
	private GameDbHelper dbHelper;
	private static final int CODE_USER_DETAILS_WITH_ID = 743;
	private static final int CODE_ALL_USER_LIST = 287;
	private static final UriMatcher matcher = buildUriMatcher();
	private final String TAG = "EvilTowerProvider: ";

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(CONTENT_AUTHORITY, PATH + "/" + PATH_PRIMARY + "/#", CODE_USER_PRIMARY);
		matcher.addURI(CONTENT_AUTHORITY, PATH + "/" + PATH_SECONDARY + "/#", CODE_USER_SECONDARY);
		matcher.addURI(CONTENT_AUTHORITY, PATH + "/" + PATH_THIRD + "/#", CODE_USER_THIRD);
		matcher.addURI(CONTENT_AUTHORITY, PATH + "/" + PATH, CODE_ALL_USER_LIST);
		matcher.addURI(CONTENT_AUTHORITY, PATH + "/" + PATH + "/#", CODE_USER_ALL_LOCATIONS);
		return matcher;
	}

	@Override
	public boolean onCreate() {
		dbHelper = new GameDbHelper(getContext());
		return false;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

		Cursor cursor;
		String[] selectionArguments = {uri.getLastPathSegment()};
//		System.out.println("Querying database: "+uri.toString());
		switch (matcher.match(uri)) {
			case CODE_USER_PRIMARY:
				cursor = dbHelper.getReadableDatabase().query(PRIMARY_TABLE_NAME, projection, COLUMN_ID + " = ? ", selectionArguments, null, null, sortOrder);
//				Log.i(TAG, "a. The Uri is querying first item :" + uri.toString());
				break;
			case CODE_USER_SECONDARY:
				cursor = dbHelper.getReadableDatabase().query(SECONDARY_TABLE_NAME, projection, COLUMN_ID + " = ? ", selectionArguments, null, null, sortOrder);
//				Log.i(TAG, "b. The Uri is querying second item :" + uri.toString());
				break;
			case CODE_USER_THIRD:
				cursor = dbHelper.getReadableDatabase().query(THIRD_TABLE_NAME, projection, COLUMN_ID + " = ? ", selectionArguments, null, null, sortOrder);
//				Log.i(TAG, "c. The Uri is querying third item :" + uri.toString());
				break;
			case CODE_ALL_USER_LIST:
				cursor = dbHelper.getReadableDatabase().query(PRIMARY_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
//				Log.i(TAG, "d. The Uri is querying all data :" + uri.toString());
				break;
			case CODE_USER_ALL_LOCATIONS:
				String selectQuery = "SELECT  * FROM " + PRIMARY_TABLE_NAME + " pn, "
					+ SECONDARY_TABLE_NAME + " sn, " + THIRD_TABLE_NAME + " tn WHERE pn."
					+ COLUMN_ID + " = " + selectionArguments[0] + " OR sn." + COLUMN_ID
					+ " = " + selectionArguments[0] + " OR tn." + COLUMN_ID + " = "
					+ selectionArguments[0];
				cursor = dbHelper.getReadableDatabase().rawQuery(selectQuery, null);
//				Log.i(TAG, "e. The Uri is querying all data :" + uri.toString());
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
//		System.out.println("Inserting database: "+uri.toString());
		db.beginTransaction();
		int rowsInserted = 0;
		switch (matcher.match(uri)) {
			case CODE_USER_SECONDARY:
				try {
					long _id = db.insert(SECONDARY_TABLE_NAME, null, values);
					if (_id != -1) {
						rowsInserted++;
					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				if (rowsInserted > 0) {
					getContext().getContentResolver().notifyChange(uri, null);
//					Log.i(getContext().getPackageName(), "Inserted Uri: " + uri.toString());
					return uri;
				}
				break;
			case CODE_USER_THIRD:
				try {
					long _id = db.insert(THIRD_TABLE_NAME, null, values);
					if (_id != -1) {
						rowsInserted++;
					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				if (rowsInserted > 0) {
					getContext().getContentResolver().notifyChange(uri, null);
//					Log.i(getContext().getPackageName(), "Inserted Uri: " + uri.toString());
					return uri;
				}
				break;
			case CODE_USER_PRIMARY:
				try {
					long _id = db.insert(PRIMARY_TABLE_NAME, null, values);
					if (_id != -1) {
						rowsInserted++;
					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				if (rowsInserted > 0) {
					getContext().getContentResolver().notifyChange(uri, null);
//					Log.i(getContext().getPackageName(), "Inserted Uri: " + uri.toString());
					return uri;
				}
				break;
			default:
//				Log.i(getContext().getPackageName(), "Unhandled Uri: " + uri.toString());
		}
		return null;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] selectionArguments = {uri.getLastPathSegment()};
		int count = 0;
		switch (matcher.match(uri)) {
			case CODE_USER_PRIMARY:
				count = db.delete(PRIMARY_TABLE_NAME, COLUMN_ID + " = ?", selectionArguments);
				getContext().getContentResolver().notifyChange(uri, null);
				break;
			case CODE_USER_SECONDARY:
				count = db.delete(SECONDARY_TABLE_NAME, COLUMN_ID + " = ?", selectionArguments);
				getContext().getContentResolver().notifyChange(uri, null);
				break;
			case CODE_USER_THIRD:
				count = db.delete(THIRD_TABLE_NAME, COLUMN_ID + " = ?", selectionArguments);
				getContext().getContentResolver().notifyChange(uri, null);
				break;
			case CODE_USER_ALL_LOCATIONS:
				db.delete(PRIMARY_TABLE_NAME, COLUMN_ID + " = ?", selectionArguments);
				db.delete(SECONDARY_TABLE_NAME, COLUMN_ID + " = ?", selectionArguments);
				db.delete(THIRD_TABLE_NAME, COLUMN_ID + " = ?", selectionArguments);
				break;
		}
		return count;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
//		System.out.println("Updating database: "+uri.toString());
		int rowsUpdated = 0;
		switch (matcher.match(uri)) {
			case CODE_USER_PRIMARY:
				rowsUpdated = db.update(PRIMARY_TABLE_NAME, values, selection, selectionArgs);
//				Log.i(getContext().getPackageName(), "Updated Uri: " + uri.toString());
				break;
			case CODE_USER_SECONDARY:
				rowsUpdated = db.update(SECONDARY_TABLE_NAME, values, selection, selectionArgs);
//				Log.i(getContext().getPackageName(), "Updated Uri: " + uri.toString());
				break;
			case CODE_USER_THIRD:
				rowsUpdated = db.update(THIRD_TABLE_NAME, values, selection, selectionArgs);
//				Log.i(getContext().getPackageName(), "Updated Uri: " + uri.toString());
				break;
			case CODE_USER_ALL_LOCATIONS:
				db.update(PRIMARY_TABLE_NAME, values, selection, selectionArgs);
				db.update(SECONDARY_TABLE_NAME, values, selection, selectionArgs);
				db.update(THIRD_TABLE_NAME, values, selection, selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unhandled URI: " + uri);
		}
		return rowsUpdated;
	}
}
