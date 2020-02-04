package com.michael.pan.eviltower.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.utilities.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.utilities.ImageUtil.splitBitmap;
import static com.michael.pan.eviltower.utilities.JSONUtil.getTreasuresFromJSON;

public class UserTreasureGridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context mContext;
	private Bitmap[][] bitmaps;
	public static JSONObject treasures;

	public UserTreasureGridViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		bitmaps = splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.treasures), 4, 16);
	}

	public void setTreasures(String data) {
		if (data == null) treasures = null;
		else {
			try {
				UserTreasureGridViewAdapter.treasures = new JSONObject(getTreasuresFromJSON(mContext, "TREASURES", data));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (treasures == null) return 0;
		else return treasures.names().length();
	}

	@Override
	public String getItem(int i) {
		try {
			if (treasures != null) return treasures.names().getString(i);
		} catch (JSONException e) {
			e.printStackTrace();
//			System.out.println(e.getMessage());
		}
		return EvilTowerContract.TAG_NONAME;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if (view == null) view = inflater.inflate(R.layout.gridview_item, viewGroup, false);
		ImageView icon = view.findViewById(R.id.gridview_image_cell);
		try {
			String key = treasures.names().getString(i);
			icon.setImageBitmap(ImageUtil.getTreasureBitmapByKey(mContext, key, bitmaps));
//			icon.setImageResource(R.drawable.blue_diamond);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return view;
	}
}
