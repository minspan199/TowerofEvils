package com.michael.pan.eviltower.services;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.michael.pan.eviltower.data.DbMaintenance;

import java.lang.ref.WeakReference;

import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DELETE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GAME_COMPLETING;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_INSERT_NEW_USER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_SAVE_USER_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ROLLBACK;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_UPDATE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_USER_ROW_UPDATE;

public class DatabaseUpdateTask extends AsyncTask<String, Void, Integer> {

	public interface onTaskExecuted{
		void onDatabaseUpdated();
	}
	public interface onNewUserCreated{
		void onUserCreated(Integer userId);
	}
	private WeakReference<Context> activityReference;
	private onTaskExecuted onTaskExecuted;
	private onNewUserCreated onNewUserCreated;
	private ContentValues cv;
		// only retain a weak reference to the activity
	public DatabaseUpdateTask(Context context, onTaskExecuted onTaskExecuted) {
		activityReference = new WeakReference<>(context);
		this.onTaskExecuted = onTaskExecuted;
	}

	public DatabaseUpdateTask(Context context, ContentValues cv, onTaskExecuted onTaskExecuted) {
		activityReference = new WeakReference<>(context);
		this.onTaskExecuted = onTaskExecuted;
		this.cv = cv;
	}

	public DatabaseUpdateTask(Context context, ContentValues cv, onNewUserCreated onNewUserCreated) {
		activityReference = new WeakReference<>(context);
		this.onNewUserCreated = onNewUserCreated;
		this.cv = cv;
	}

	@Override
	protected Integer doInBackground(String... strings) {
		switch (strings[1]) {
			case TAG_ROLLBACK:
				DbMaintenance.rollBackDatabase(activityReference.get(), strings);
				break;
			case TAG_UPDATE:
				DbMaintenance.updateDatabase(activityReference.get(), strings, false);
				break;
			case TAG_USER_ROW_UPDATE:
				DbMaintenance.updateRowOfDatabase(activityReference.get(), strings[0], cv);
				break;
			case TAG_SAVE_USER_DATA:
				DbMaintenance.insertData(activityReference.get(), strings);
				break;
			case TAG_GAME_COMPLETING:
				DbMaintenance.gameCompleting(activityReference.get(), strings[0], cv);
				break;
			case TAG_DELETE:
				DbMaintenance.deleteUser(activityReference.get(), strings[0]);
				break;
			case TAG_INSERT_NEW_USER:
				DbMaintenance.insertNewUser(activityReference.get(), strings[0], cv);
				return Integer.valueOf(strings[0]);
		}
		return -1;
	}

	@Override
	protected void onPostExecute(Integer integer) {
		super.onPostExecute(integer);
		if (integer.equals(-1))onTaskExecuted.onDatabaseUpdated();
		else onNewUserCreated.onUserCreated(integer);
	}

}
