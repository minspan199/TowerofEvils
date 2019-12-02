package com.michael.pan.eviltower.utilities;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.data.FloorData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.michael.pan.eviltower.activities.StartGameActivity.flyWingData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.warrior;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ATTACK;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_COMPLETION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DEFENSE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DIFFICULTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ENERGY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_EXPERIENCE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_FLOOR;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_FLY_WING_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_IF_PROLOGUE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_X;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_Y;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USER_ICON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_ATTACK_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_B_KEY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_COINS_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_DEFENSE_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_ENERGY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_EXPERIENCE_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_FLOOR_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_ICON_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_LEVEL_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_R_KEY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_Y_KEY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.TAG_ENEMY_BEFORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_EMPTY;

public class JSONUtil {


	private static JSONObject getDefaultJSON(Context context){
		JSONObject json = new JSONObject();// Create one Jsonobject
		try {
			json.put(context.getString(R.string.coins), "100");
			json.put(context.getString(R.string.r_key), "1");
			json.put(context.getString(R.string.b_key), "1");
			json.put(context.getString(R.string.y_key), "1");
			json.put(context.getString(R.string.r_diamond), "0");
			json.put(context.getString(R.string.b_diamond), "0");
			json.put(context.getString(R.string.g_diamond), "0");
			json.put(context.getString(R.string.y_diamond), "0");
			json.put(context.getString(R.string.key_box), "0");
			json.put(context.getString(R.string.book_of_wisdom), "0");
			json.put(context.getString(R.string.pot_of_wisdom), "0");
			json.put(context.getString(R.string.large_pot_of_wisdom), "0");
			json.put(context.getString(R.string.smile_coin), "0");
			json.put(context.getString(R.string.fly_wing), "0");
			json.put(context.getString(R.string.large_smile_coin), "0");
			json.put(context.getString(R.string.cross), "0");
			json.put(context.getString(R.string.blessing_gift), "0");
			json.put(context.getString(R.string.wooden_sword), "1");
			json.put(context.getString(R.string.iron_sword), "0");
			json.put(context.getString(R.string.large_wooden_sword), "0");
			json.put(context.getString(R.string.large_iron_sword), "0");
			json.put(context.getString(R.string.chaos_sword), "0");
			json.put(context.getString(R.string.skin_shield), "1");
			json.put(context.getString(R.string.wooden_shield), "0");
			json.put(context.getString(R.string.iron_shield), "0");
			json.put(context.getString(R.string.diamond_shield), "0");
			json.put(context.getString(R.string.divine_shield), "0");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String getDefaultTreasuresString(Context context){
		return getDefaultJSON(context).toString();//Convert json to string
	}

	public static String getDefaultUserDataString(Context context){
		return getDefaultUserDataJson(context).toString();
	}

	private static JSONObject getDefaultUserDataJson(Context context) {
		JSONObject json = new JSONObject();// Create one Jsonobject
		try {
			json.put(COLUMN_ATTACK, "20");
			json.put(COLUMN_LEVEL, "0");
			json.put(COLUMN_DEFENSE, "20");
			json.put(COLUMN_FLOOR, "0");
			json.put(COLUMN_ENERGY, "1000");
			json.put(COLUMN_EXPERIENCE, "0");
			json.put(COLUMN_POSITION_X, "7");
			json.put(COLUMN_POSITION_Y, "11");
			json.put(COLUMN_USER_ICON, "0");
			json.put(COLUMN_IF_PROLOGUE, true);//start the prologue when the game is first initialized.
			json.put(COLUMN_FLY_WING_DATA, TAG_EMPTY);
			json.put(COLUMN_COMPLETION, TAG_EMPTY);
//			if (gameLiveData != null) json.put(COLUMN_DIFFICULTY, gameLiveData.difficulty);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject getUserDataJson(ArrayList<Integer> gameData){
		JSONObject json = new JSONObject();// Create one Jsonobject
		try {
			json.put(COLUMN_ATTACK, gameData.get(INDEX_ATTACK_LIVEDATA).toString());
			json.put(COLUMN_LEVEL, gameData.get(INDEX_LEVEL_LIVEDATA).toString());
			json.put(COLUMN_DEFENSE, gameData.get(INDEX_DEFENSE_LIVEDATA).toString());
			json.put(COLUMN_FLOOR, gameData.get(INDEX_FLOOR_LIVEDATA).toString());
			json.put(COLUMN_ENERGY, gameData.get(INDEX_ENERGY_LIVEDATA).toString());
			json.put(COLUMN_EXPERIENCE, gameData.get(INDEX_EXPERIENCE_LIVEDATA).toString());
			json.put(COLUMN_POSITION_X, warrior.getX());
			json.put(COLUMN_POSITION_Y, warrior.getY());
			json.put(COLUMN_USER_ICON, gameData.get(INDEX_ICON_LIVEDATA).toString());
			json.put(COLUMN_IF_PROLOGUE, warrior.isFirstTime);
			json.put(COLUMN_FLY_WING_DATA, flyWingData.toString());
			json.put(COLUMN_COMPLETION, TAG_EMPTY);
			json.put(COLUMN_DIFFICULTY, gameLiveData.difficulty);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject getTreasuresJSON(Context context, JSONObject json, ArrayList<Integer> gameData) {
		try {
			json.put(context.getString(R.string.y_key), gameData.get(INDEX_Y_KEY_LIVEDATA).toString());
			json.put(context.getString(R.string.r_key), gameData.get(INDEX_R_KEY_LIVEDATA).toString());
			json.put(context.getString(R.string.b_key), gameData.get(INDEX_B_KEY_LIVEDATA).toString());
			json.put(context.getString(R.string.coins), gameData.get(INDEX_COINS_LIVEDATA).toString());
		} catch (JSONException e){
			e.printStackTrace();
		}
		return json;
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static JSONObject getMapJSON(ArrayList<FloorData> mapDataInArrays) throws JSONException {
		int floorCounts = mapDataInArrays.size();
		JSONObject jsonObject = new JSONObject();
		JSONArray floors = new JSONArray();
		jsonObject.put("height", 12);
		jsonObject.put("width", 15);

		for (int i = floorCounts - 1; i >= 0; i--){
			FloorData thisFloor = mapDataInArrays.get(i);
			JSONObject background = new JSONObject(), npc = new JSONObject(), enemy = new JSONObject(), treasure = new JSONObject();
			JSONArray floorData = new JSONArray();
			int[][] arr00 = thisFloor.getLayer00();
			int[][] arr01 = thisFloor.getLayer01();
			int[][] arr02 = thisFloor.getLayer02();
			int[][] arr03 = thisFloor.getLayer03();
			if (arr00 != null){
				int[] layer00 = convertToOneDArray(arr00);
				background.put(EvilTowerContract.JSON_TAG_DATA, new JSONArray(layer00));
				background.put(EvilTowerContract.JSON_TAG_NAME, EvilTowerContract.TAG_BACKGROUND);
				floorData.put(background);
			}
			if (arr01 != null){
				int[] layer01 = convertToOneDArray(arr01);
				npc.put(EvilTowerContract.JSON_TAG_DATA, new JSONArray(layer01));
				npc.put(EvilTowerContract.JSON_TAG_NAME, EvilTowerContract.TAG_NPCS);
				floorData.put(npc);
			}
			if (arr02 != null){
				int[] layer02 = convertToOneDArray(arr02);
				enemy.put(EvilTowerContract.JSON_TAG_DATA, new JSONArray(layer02));
				enemy.put(EvilTowerContract.JSON_TAG_NAME, EvilTowerContract.TAG_ENEMIES);
				floorData.put(enemy);
			}
			if (arr03 != null){
				int[] layer03 = convertToOneDArray(arr03);
				treasure.put(EvilTowerContract.JSON_TAG_DATA, new JSONArray(layer03));
				treasure.put(EvilTowerContract.JSON_TAG_NAME, EvilTowerContract.TAG_TREASURES);
				floorData.put(treasure);
			}
			JSONObject floorThis = new JSONObject();
			floorThis.put(EvilTowerContract.JSON_TAG_LAYERS, floorData);
			floors.put(floorThis);
		}
		jsonObject.put(EvilTowerContract.JSON_TAG_LAYERS, floors);
		return jsonObject;
	}

	private static int[] convertToOneDArray(int[][] arr) {

		int M = arr.length;
		int N = arr[0].length;
		int[] oneDArray = new int[M*N];
		for (int i = 0; i < M; i++){
			System.arraycopy(arr[i], 0, oneDArray, N * i, N);
		}
		return oneDArray;
	}

	//Suppose the JSON is single layer mapping structure, this method helps remove the zero value entries.

	public static String getTreasuresFromJSON(Context context, String instruction, String jsonString) throws JSONException {
		JSONObject json = new JSONObject(jsonString);
		JSONArray names = json.names();
//		System.out.println("instruction:" + instruction + "Array size: " + names);
		switch (instruction){
			case "TREASURES":
				if (names == null) break;
				json.remove(context.getString(R.string.y_key));
				json.remove(context.getString(R.string.b_key));
				json.remove(context.getString(R.string.r_key));
				json.remove(context.getString(R.string.coins));
			case "NONZERO":
				if (names == null) break;
				for(int i = 0; i < names.length(); i++){
					String key = names.getString(i);
					if (!json.isNull(key) && json.getString(key).equals("0")) json.remove(key);
				}
				break;
			default: break;
		}
		return json.toString();
	}




//	public static ArrayList<String> getJSONArrayList(){
//		JSONObject json = new JSONObject(stringreadfromsqlitetable);// Convert the String to JsonObject
//		ArrayList items = json.optJSONArray("uniqueArrays");// Conver the jsonObject to Arraylist
//	}

}
