package com.michael.pan.eviltower.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static android.provider.BaseColumns._ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ATTACK;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DEFENSE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ENERGY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_MAP_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_SCORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_TREASURES_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_UPDATE_TIME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USERDATA_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COMPLETION_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.PRIMARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.SECONDARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.THIRD_TABLE_NAME;

public class GameDbHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "tower_of_evil.db";
	public static final int DATABASE_VERSION = 8;

	public GameDbHelper(@Nullable Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override

	public void onCreate(SQLiteDatabase db) {
		/* This String will contain a simple SQL statement that will create a table that will cache our game data. */

		//Insert Property Step 5

		final String SQL_CREATE_PRIMARY_TABLE = "CREATE TABLE " + PRIMARY_TABLE_NAME + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT NOT NULL, " +
			COLUMN_ID + " TEXT NOT NULL, " +
			COLUMN_LEVEL + " TEXT NOT NULL, " +
			COLUMN_UPDATE_TIME + " TEXT NOT NULL, " +
			COLUMN_USERDATA_JSON + " TEXT NOT NULL, " +
			COLUMN_TREASURES_JSON + " TEXT NOT NULL, " +
			COLUMN_MAP_JSON + " TEXT NOT NULL, " +
			" UNIQUE (" + COLUMN_ID + ") ON CONFLICT REPLACE);";

		final String SQL_CREATE_SECONDARY_TABLE = "CREATE TABLE " + SECONDARY_TABLE_NAME + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT NOT NULL, " +
			COLUMN_ID + " TEXT NOT NULL, " +
			COLUMN_LEVEL + " TEXT NOT NULL, " +
			COLUMN_UPDATE_TIME + " TEXT NOT NULL, " +
			COLUMN_USERDATA_JSON + " TEXT NOT NULL, " +
			COLUMN_TREASURES_JSON + " TEXT NOT NULL, " +
			COLUMN_MAP_JSON + " TEXT NOT NULL, " +
			" UNIQUE (" + COLUMN_ID + ") ON CONFLICT REPLACE);";

		final String SQL_CREATE_THIRD_TABLE = "CREATE TABLE " + THIRD_TABLE_NAME + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT NOT NULL, " +
			COLUMN_ID + " TEXT NOT NULL, " +
			COLUMN_LEVEL + " TEXT NOT NULL, " +
			COLUMN_UPDATE_TIME + " TEXT NOT NULL, " +
			COLUMN_USERDATA_JSON + " TEXT NOT NULL, " +
			COLUMN_TREASURES_JSON + " TEXT NOT NULL, " +
			COLUMN_MAP_JSON + " TEXT NOT NULL, " +
			" UNIQUE (" + COLUMN_ID + ") ON CONFLICT REPLACE);";

//		final String SQL_CREATE_COMPLETION_TABLE = "CREATE TABLE " + COMPLETION_TABLE_NAME + " (" +
//			_ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//			COLUMN_NAME     + " TEXT NOT NULL, "  +
//			COLUMN_ID + " TEXT NOT NULL, " +
//			COLUMN_LEVEL + " TEXT NOT NULL, " +
//			COLUMN_UPDATE_TIME + " TEXT NOT NULL, " +
//			COLUMN_ATTACK + " TEXT NOT NULL, " +
//			COLUMN_DEFENSE + " TEXT NOT NULL, " +
//			COLUMN_ENERGY + " TEXT NOT NULL, " +
//			COLUMN_LEVEL + " TEXT NOT NULL, " +
//			COLUMN_SCORE + " TEXT NOT NULL, " +
//			" UNIQUE (" + COLUMN_ID + ") ON CONFLICT REPLACE);";

		db.execSQL(SQL_CREATE_PRIMARY_TABLE);
		db.execSQL(SQL_CREATE_SECONDARY_TABLE);
		db.execSQL(SQL_CREATE_THIRD_TABLE);
//		db.execSQL(SQL_CREATE_COMPLETION_TABLE);

//		Log.i(PRIMARY_TABLE_NAME, SQL_CREATE_PRIMARY_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PRIMARY_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + SECONDARY_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + THIRD_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + COMPLETION_TABLE_NAME);
		onCreate(db);
	}

}
