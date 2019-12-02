package com.michael.pan.eviltower.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.activities.MainActivity;
import com.michael.pan.eviltower.data.GameDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_TREASURES_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_UPDATE_TIME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_USER_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.HAVE_SENT_NOTIFICATION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.ID_NOTIFICATION_CHANNEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_TREASURES;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.KEY_USER_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.KEY_USER_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.PRIMARY_TABLE_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.USER_LOADER_PROJECTION;

public class NotificationWork extends Worker {

	private static final int notificationId = 598;
	private static final String TAG_NOTIFICATION_CHANNEL = "name-of-notification-channel";
	private static final String ACTION_GET_BONUS = "com.michael.pan.eviltower.services.NotificationWork.ACTION_GET_BONUS";


	public NotificationWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	@NonNull
	@Override
	public Result doWork() {

		String userName = getInputData().getString(KEY_USER_NAME);
		String userId = getInputData().getString(KEY_USER_ID);
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

		NotificationChannel notificationChannel;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			notificationChannel = new NotificationChannel(ID_NOTIFICATION_CHANNEL, TAG_NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
			notificationChannel.enableLights(true);
			notificationChannel.setLightColor(Color.RED);
			notificationChannel.enableVibration(true);
			notificationChannel.setVibrationPattern(new long[]{0});
			notificationManager.createNotificationChannel(notificationChannel);
		}

		Intent getBonusIntent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
		getBonusIntent.setAction(ACTION_GET_BONUS);
		getBonusIntent.putExtra(EXTRA_USER_ID, userId);
		PendingIntent getBonusPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, getBonusIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//		A PendingIntent itself is simply a reference to a token maintained by the system describing the original data used to retrieve it. ...
//		If the creating application later re-retrieves the same kind of PendingIntent (same operation, same Intent action, data, categories, and components, and same flags),
//		it will receive a PendingIntent representing the same token
//		Try adding FLAG_UPDATE_CURRENT to your PendingIntent when you create it via getBroadcast().

		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), ID_NOTIFICATION_CHANNEL)
			.setSmallIcon(R.drawable.magic_tower_small_icon)
			.setContentTitle(getApplicationContext().getString(R.string.notification_title))
			.setVibrate(new long[]{0})
			.setStyle(new NotificationCompat.BigTextStyle().bigText(getApplicationContext().getString(R.string.hello) + userName + getApplicationContext().getString(R.string.bonus_invitation)))
			.setContentIntent(pendingIntent)
			.addAction(R.drawable.ic_attach_money_yellow_a700_48dp, getApplicationContext().getString(R.string.get_bonus), getBonusPendingIntent).setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setColorized(true)
			.setAutoCancel(true);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			builder.setColor(getApplicationContext().getColor(R.color.orange_light));
		}

		// notificationId is a unique int for each notification that you must define
		notificationManager.notify(notificationId, builder.build());

		return Result.success();
	}

	public static class MyBroadcastReceiver extends BroadcastReceiver implements DatabaseUpdateTask.onTaskExecuted{
		//Making the class public to expose to manifest file!!!
		String action = "", extraId = "";
		String userName, userTreasuresJsonString, userCoin;
		JSONObject treasureJson = new JSONObject();
		private SharedPreferences preferences;

		@Override
		public void onReceive(Context context, Intent intent) {
			NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
			notificationManagerCompat.cancel(notificationId);
			Cursor cursor = null; // to properly CloseButtonClicked the cursor, should define the scope exactly to the CloseButtonClicked position.
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
			action = intent.getAction();
			extraId = intent.getStringExtra(EXTRA_USER_ID);
			String[] selectionArguments = {extraId};
//			Log.i("MyBroadcastReceiver: ", "User: "+ extraId);

			if (action.equals(ACTION_GET_BONUS)){
				GameDbHelper dbHelper = new GameDbHelper(context);
				try{
					cursor = dbHelper.getReadableDatabase().query(PRIMARY_TABLE_NAME, USER_LOADER_PROJECTION, COLUMN_ID + " = ? ", selectionArguments, null, null, null);
					if (cursor != null && cursor.getCount() != 0 && cursor.moveToFirst()){
						ContentValues cv = getUpdatedJsonCV(resolveData(context, cursor));
						if (cv != null){
							new DatabaseUpdateTask(context, cv, this).execute(extraId, "row-userdata");
						}
//						else {
////							Log.i("MyBroadcastReceiver: ", "to user: "+ extraId +", you already got the bonus for today!");
//						}
					}
				}finally {
					if (cursor != null) {
						cursor.close();
						dbHelper.close();
					}
				}
			}
		}

		public JSONObject resolveData(Context context, Cursor cursor) {
			userName = cursor.getString(INDEX_USER_NAME);
			userTreasuresJsonString = cursor.getString(INDEX_USER_TREASURES);
			SharedPreferences.Editor editor = preferences.edit();
			try {
				treasureJson = new JSONObject(userTreasuresJsonString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (treasureJson != null){
				try {
					userCoin = treasureJson.getString(context.getString(R.string.coins));
					int coin = 50 + Integer.parseInt(userCoin);
					String notifyPreferences = preferences.getString(context.getString(R.string.pref_user_bonus_key), "");

					if (!notifyPreferences.equals("")){
						try {
							JSONObject json;
							JSONArray jsonArray = new JSONArray(notifyPreferences);
							JSONArray jsonArray1 = new JSONArray();
							for (int i = 0; i < jsonArray.length(); i++) {
								json = jsonArray.getJSONObject(i);
								String id = json.getString(COLUMN_ID);
								String updateTime = json.getString(COLUMN_UPDATE_TIME);
								boolean sent = json.getBoolean(HAVE_SENT_NOTIFICATION);
								if (id.equals(extraId)) {
									if (sent) return null;
									else json.put(HAVE_SENT_NOTIFICATION, true);
									// defaulted if the date is not the same as recorded.
								}
//								Log.i("user:", id +"and sent"+sent + updateTime);
								jsonArray1.put(i, json);
							}
							editor.putString(context.getString(R.string.pref_user_bonus_key), jsonArray1.toString()).apply();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					treasureJson.put(context.getString(R.string.coins), String.valueOf(coin));
//					Log.i("MyBroadcastReceiver: ", "Coin increased from " + userCoin + " to " + coin);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return treasureJson;
		}

		private ContentValues getUpdatedJsonCV(JSONObject jsonObject) {
			if (jsonObject != null){
				ContentValues cv = new ContentValues();
				cv.put(COLUMN_TREASURES_JSON, jsonObject.toString());
				cv.put(COLUMN_UPDATE_TIME, String.valueOf(System.currentTimeMillis()));
				return cv;
			}else return null;

		}

		@Override
		public void onDatabaseUpdated() {
//			Log.i("MyBroadcastReceiver", "got bonus successfully!");
		}
	}
}
