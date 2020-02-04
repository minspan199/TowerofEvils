package com.michael.pan.eviltower.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.activities.StartGameActivity;
import com.michael.pan.eviltower.utilities.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.warrior;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_MAP_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_TREASURES_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_UPDATE_TIME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USERDATA_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.HAVE_SENT_NOTIFICATION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_LEVEL_LIVEDATA;

public class DataSaving {

	public static String convertDate(String dateInMilliseconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US);
		return formatter.format(Long.parseLong(dateInMilliseconds));
	}

	public static String getCurrentDate() {
		String pattern = "HH:mm:ss MM-dd-yyyy";
		return new SimpleDateFormat(pattern, Locale.US).format(new Date());
	}

	static ContentValues getContentValuesFromCurrentData(Context context) throws JSONException {
		ContentValues values = new ContentValues();
		ArrayList<Integer> gameDataList = gameLiveData.getGameDataList();
		JSONObject userDataJSON = JSONUtil.getUserDataJson(gameDataList);
		JSONObject userTreasureJSON = JSONUtil.getTreasuresJSON(context, gameLiveData.getTreasureJson(), gameDataList);
		JSONObject mapJSON = JSONUtil.getMapJSON(StartGameActivity.mapDataInArrays);
		values.put(COLUMN_USERDATA_JSON, userDataJSON.toString());
		values.put(COLUMN_TREASURES_JSON, userTreasureJSON.toString());
		values.put(COLUMN_MAP_JSON, mapJSON.toString());
		values.put(COLUMN_LEVEL, gameDataList.get(INDEX_LEVEL_LIVEDATA).toString());
		values.put(COLUMN_UPDATE_TIME, String.valueOf(System.currentTimeMillis()));
		return values;
	}

	public static void saveBonusPreferences(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String s = preferences.getString(context.getString(R.string.pref_user_bonus_key), "[]");
		JSONArray jsonArray = new JSONArray();
		if (!s.equals("[]")) {
			try {
				jsonArray = new JSONArray(s);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					String id = json.getString(COLUMN_ID);
					boolean sent = json.getBoolean(HAVE_SENT_NOTIFICATION);
					if (id.equals(warrior.userId) && !sent) {
//						System.out.println(warrior.userId + ":okoooo");
						json.put(HAVE_SENT_NOTIFICATION, true);
						jsonArray.put(i, json);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(context.getString(R.string.pref_user_bonus_key), jsonArray.toString()).apply();
		editor.clear();
	}

}
