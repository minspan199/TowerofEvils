package com.michael.pan.eviltower.data;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.michael.pan.eviltower.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.michael.pan.eviltower.activities.StartGameActivity.warrior;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ATTACK;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DEFENSE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DIFFICULTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ENERGY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_EXPERIENCE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_FLOOR;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_IF_PROLOGUE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_X;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_Y;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USER_ICON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_ATTACK_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_B_KEY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_COINS_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_DEFENSE_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_DIFFICULTY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_ENERGY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_EXPERIENCE_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_FLOOR_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_ICON_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_LEVEL_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_POSITION_X_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_POSITION_Y_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_R_KEY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_Y_KEY_LIVEDATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_G_KEY_LIVEDATA;

public class GameLiveDataViewModel extends ViewModel {

	private JSONObject jsonData, jsonTreasure;
	private Integer attack, level, defense, floor, energy, experience, position_X, position_Y, rKey, yKey, bKey, gKey, coins, icon;
	private ArrayList<Integer> gameDataList;
	private MutableLiveData<List<Integer>> gameLiveData;
	public MutableLiveData<JSONObject> treasureJsonLiveData;
	public String userName, userId;
	public String difficulty;

	public void initialization(Context context, JSONObject userDataJson, JSONObject treasuresJSON) {

		jsonData = userDataJson;
		jsonTreasure = treasuresJSON;
		gameDataList = new ArrayList<>(13);//a list of variables for live observing
		gameLiveData = new MutableLiveData<>();
		treasureJsonLiveData = new MutableLiveData<>();
		setTreasureJson(treasuresJSON);
		try {
			attack = Integer.parseInt(jsonData.getString(COLUMN_ATTACK));
			level = Integer.parseInt(jsonData.getString(COLUMN_LEVEL));
			defense = Integer.parseInt(jsonData.getString(COLUMN_DEFENSE));
			floor = Integer.parseInt(jsonData.getString(COLUMN_FLOOR));
			energy = Integer.parseInt(jsonData.getString(COLUMN_ENERGY));
			experience = Integer.parseInt(jsonData.getString(COLUMN_EXPERIENCE));
			position_X = Integer.parseInt(jsonData.getString(COLUMN_POSITION_X));
			position_Y = Integer.parseInt(jsonData.getString(COLUMN_POSITION_Y));
			icon = Integer.parseInt(jsonData.getString(COLUMN_USER_ICON));
			warrior.isFirstTime = jsonData.getBoolean(COLUMN_IF_PROLOGUE);
			if (jsonData.has(COLUMN_DIFFICULTY)) difficulty = jsonData.getString(COLUMN_DIFFICULTY);

			if (jsonTreasure.isNull(context.getString(R.string.r_key))) rKey = 0;
			else rKey = Integer.parseInt(jsonTreasure.getString(context.getString(R.string.r_key)));
			if (jsonTreasure.isNull(context.getString(R.string.b_key))) bKey = 0;
			else bKey = Integer.parseInt(jsonTreasure.getString(context.getString(R.string.b_key)));
			if (jsonTreasure.isNull(context.getString(R.string.y_key))) yKey = 0;
			else yKey = Integer.parseInt(jsonTreasure.getString(context.getString(R.string.y_key)));
			if (jsonTreasure.isNull(context.getString(R.string.g_key))) gKey = 0;
			else gKey = Integer.parseInt(jsonTreasure.getString(context.getString(R.string.g_key)));
			if (jsonTreasure.isNull(context.getString(R.string.coins))) coins = 0;
			else
				coins = Integer.parseInt(jsonTreasure.getString(context.getString(R.string.coins)));
			//the order below cannot tbe changed!!!
			gameDataList.add(INDEX_ATTACK_LIVEDATA, attack);
			gameDataList.add(INDEX_DEFENSE_LIVEDATA, defense);
			gameDataList.add(INDEX_FLOOR_LIVEDATA, floor);
			gameDataList.add(INDEX_ENERGY_LIVEDATA, energy);
			gameDataList.add(INDEX_EXPERIENCE_LIVEDATA, experience);

			gameDataList.add(INDEX_POSITION_X_LIVEDATA, position_X);
			gameDataList.add(INDEX_POSITION_Y_LIVEDATA, position_Y);
			gameDataList.add(INDEX_LEVEL_LIVEDATA, level);
			gameDataList.add(INDEX_R_KEY_LIVEDATA, rKey);
			gameDataList.add(INDEX_B_KEY_LIVEDATA, bKey);

			gameDataList.add(INDEX_Y_KEY_LIVEDATA, yKey);
			gameDataList.add(INDEX_G_KEY_LIVEDATA, gKey);
			gameDataList.add(INDEX_COINS_LIVEDATA, coins);
			gameDataList.add(INDEX_ICON_LIVEDATA, icon);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public int getAttack() {
		return gameDataList.get(INDEX_ATTACK_LIVEDATA);
	}

	public void setAttack(int attack) {
		this.gameDataList.set(INDEX_ATTACK_LIVEDATA, attack);
		gameLiveData.setValue(gameDataList);
	}

	public void setTreasureJson(JSONObject json) {
		this.treasureJsonLiveData.setValue(json);
	}

	public JSONObject getTreasureJson() {
		return treasureJsonLiveData.getValue();
	}

	public int getLevel() {
		return gameDataList.get(INDEX_LEVEL_LIVEDATA);
	}

	public boolean setLevel(int level) {
		if (level <= 999) {
			this.gameDataList.set(INDEX_LEVEL_LIVEDATA, level);
			gameLiveData.setValue(gameDataList);
			return true;
		} else return false;
	}

	public int getDefense() {
		return gameDataList.get(INDEX_DEFENSE_LIVEDATA);
	}

	public void setDefense(int defense) {
		this.gameDataList.set(INDEX_DEFENSE_LIVEDATA, defense);
		gameLiveData.setValue(gameDataList);
	}

	public int getFloor() {
		return gameDataList.get(INDEX_FLOOR_LIVEDATA);
	}

	public void setFloor(int floor) {
		this.gameDataList.set(INDEX_FLOOR_LIVEDATA, floor);
		gameLiveData.setValue(gameDataList);
	}

	public int getEnergy() {
		return gameDataList.get(INDEX_ENERGY_LIVEDATA);
	}

	public void setEnergy(int energy) {
		this.gameDataList.set(INDEX_ENERGY_LIVEDATA, energy);
		gameLiveData.setValue(gameDataList);
	}

	public int getExperience() {
		return gameDataList.get(INDEX_EXPERIENCE_LIVEDATA);
	}

	public void setExperience(int experience) {
		this.gameDataList.set(INDEX_EXPERIENCE_LIVEDATA, experience);
		gameLiveData.setValue(gameDataList);
	}

	public int getPosition_X() {
		return gameDataList.get(INDEX_POSITION_X_LIVEDATA);
	}

	public void setPosition_X(int position_X) {
		this.gameDataList.set(INDEX_POSITION_X_LIVEDATA, position_X);
		gameLiveData.setValue(gameDataList);
	}

	public int getPosition_Y() {
		return gameDataList.get(INDEX_POSITION_Y_LIVEDATA);
	}

	public void setPosition_Y(int position_Y) {
		this.gameDataList.set(INDEX_POSITION_Y_LIVEDATA, position_Y);
		gameLiveData.setValue(gameDataList);
	}

	public int getrKey() {
		return gameDataList.get(INDEX_R_KEY_LIVEDATA);
	}

	public void setrKey(int rKey) {
		this.gameDataList.set(INDEX_R_KEY_LIVEDATA, rKey);
		gameLiveData.setValue(gameDataList);
	}

	public int getyKey() {
		return this.gameDataList.get(INDEX_Y_KEY_LIVEDATA);
	}

	public void setyKey(int yKey) {
		this.gameDataList.set(INDEX_Y_KEY_LIVEDATA, yKey);
		gameLiveData.setValue(gameDataList);
	}

	public int getbKey() {
		return gameDataList.get(INDEX_B_KEY_LIVEDATA);
	}

	public void setbKey(int bKey) {
		this.gameDataList.set(INDEX_B_KEY_LIVEDATA, bKey);
		gameLiveData.setValue(gameDataList);
	}

	public int getgKey() {
		return gameDataList.get(INDEX_G_KEY_LIVEDATA);
	}

	public void setgKey(int gKey) {
		this.gameDataList.set(INDEX_G_KEY_LIVEDATA, gKey);
		gameLiveData.setValue(gameDataList);
	}

	public int getCoins() {
		return gameDataList.get(INDEX_COINS_LIVEDATA);
	}

	public void setCoins(int coins) {
		this.gameDataList.set(INDEX_COINS_LIVEDATA, coins);
		gameLiveData.setValue(gameDataList);
	}

	public Integer getIcon() {
		return icon;
	}

	public MutableLiveData<List<Integer>> getGameLiveData() {
		gameLiveData.setValue(gameDataList);
		return gameLiveData;
	}

	public ArrayList<Integer> getGameDataList() {
		return gameDataList;
	}

}
