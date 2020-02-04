package com.michael.pan.eviltower.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.michael.pan.eviltower.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.floorName;

public class FloorSelectorAdapter extends BaseAdapter {

	public JSONArray flyWingData;
	private LayoutInflater inflater;
	private JSONObject jsonObject;

	public FloorSelectorAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return flyWingData.length();
	}

	@Override
	public Object getItem(int i) {
		return floorName[i];
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if (view == null) view = inflater.inflate(R.layout.gridview_item, viewGroup, false);
		try {
			jsonObject = flyWingData.getJSONObject(i);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		view.findViewById(R.id.gridview_image_cell).setVisibility(View.GONE);
		TextView tv = view.findViewById(R.id.gridview_text_cell);
		tv.setVisibility(View.VISIBLE);
		try {
			tv.setText(floorName[jsonObject.getInt(COLUMN_NAME)]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return view;
	}

	public void setFlyWingData(JSONArray jsonArray) {
		flyWingData = new JSONArray();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				if (jsonArray.getJSONObject(i) != null) {
					flyWingData.put(jsonArray.getJSONObject(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		notifyDataSetChanged();
	}
}
