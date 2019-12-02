package com.michael.pan.eviltower.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class MapData {

	private String json = null;

	private ArrayList<FloorData> dataInArrays;
	private int xCount, yCount, floor;
	private JSONArray jsonArray;
	JSONObject jsonObject;

	public ArrayList<FloorData> getDataInArrays() {
		return dataInArrays;
	}

	public MapData(Context context, String jsonString) {

		dataInArrays = new ArrayList<>();
		if (jsonString == null){
			try {
				InputStream is = context.getAssets().open("MagicTowerOriginal.json");
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				json = new String(buffer, StandardCharsets.UTF_8);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			json = jsonString;
		}

		getJsonList();
		loadMapData();
	}

	public int getxCount() {
		return xCount;
	}

	public int getyCount() {
		return yCount;
	}

	private void getJsonList() {
		try {
			jsonObject = new JSONObject(json);
			yCount = jsonObject.getInt("height"); // 12
			xCount = jsonObject.getInt("width"); // 15
			jsonArray = (JSONArray) jsonObject.get("layers");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private JSONObject getFloor(int floor) {
		JSONObject jsonObjectOnFloor = new JSONObject();
		try {
			jsonObjectOnFloor = jsonArray.getJSONObject(jsonArray.length() - floor - 1);
		} catch (JSONException e) {
			e.printStackTrace();
//			System.out.println(e.getMessage());
		}
		return jsonObjectOnFloor;
	}

	private void loadMapData() {
		int layerCount = jsonArray.length();
		for (int ind = 0;ind< layerCount;ind++){
			int[][] layer00 = null, layer01 = null, layer02 = null, layer03 = null;
			try {
				JSONObject layerData = this.getFloor(ind);
//				System.out.println(layerData.toString());
				JSONArray mapLayers = layerData.getJSONArray("layers");
				int i = 0;

				while (i < mapLayers.length()) {
					JSONObject json = mapLayers.getJSONObject(i);
					String name = json.getString("name");
					i++;
					switch (name) {
						case "background":
							layer00 = get2DArray(getIntFromJsonArray(json.getJSONArray("data")));
							break;
						case "npcs":
							layer01 = get2DArray(getIntFromJsonArray(json.getJSONArray("data")));
							break;
						case "enemies":
							layer02 = get2DArray(getIntFromJsonArray(json.getJSONArray("data")));
							break;
						case "treasures":
							layer03 = get2DArray(getIntFromJsonArray(json.getJSONArray("data")));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			FloorData node = new FloorData(layer00, layer01, layer02, layer03, this.getxCount(), this.getyCount());

			dataInArrays.add(node);
//			System.out.println("lay0:" + Arrays.deepToString(layer00));
//			System.out.println("lay1:" + Arrays.deepToString(layer01));
//			System.out.println("lay2:" + Arrays.deepToString(layer02));
//			System.out.println("lay3:" + Arrays.deepToString(layer03));
		}
	}

	private int[][] get2DArray(int[] data) {
		int [][] twoDArray = new int[yCount][xCount];
		for (int i = 0; i<yCount;i++) {
			if (xCount >= 0) System.arraycopy(data, i * xCount, twoDArray[i], 0, xCount);
//			for (int j = 0; j<xCount;j++){
//				twoDArray[i][j] = data[i*xCount+j];
//			}
		}
		return twoDArray;
	}

	private int[] getIntFromJsonArray(JSONArray data) {
		int[] intArray = new int[data.length()];
		try {
			for (int i = 0; i < data.length(); i++){
				intArray[i] = data.getInt(i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return intArray;
	}

}