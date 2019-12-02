package com.michael.pan.eviltower.data;

import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_X;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_Y;

public class FlyWingData {

	public int name;
	private int positionX;
	private int positionY;
	public JSONObject flyWing = new JSONObject();

	public FlyWingData(int floor, int positionX, int positionY) {
		this.name = floor;
		this.positionX = positionX;
		this.positionY = positionY;
		try {
			flyWing.put(COLUMN_NAME, floor);
			flyWing.put(COLUMN_POSITION_X, positionX);
			flyWing.put(COLUMN_POSITION_Y, positionY);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
