package com.michael.pan.eviltower.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.utilities.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USER_ICON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_NAME;

public class UserIconAdapter extends BaseAdapter {

	private static Cursor mCursor;
	private LayoutInflater inflater;
	private Context mContext;
	private String TAG = "UserIconAdapter:";
	private onIconClicked onIconClicked;
	private TypedArray userIconDrawables;

	public interface onIconClicked{
		void clicked(String[] strings);
	}

	public UserIconAdapter(Context context, onIconClicked mIconClicked, TypedArray userIconDrawables) {
		onIconClicked = mIconClicked;
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.userIconDrawables = userIconDrawables;
	}

	@Override
	public int getCount() {
		if (mCursor == null) return 0;
		return mCursor.getCount();
	}

	@Override
	public Object getItem(int i) {
		mCursor.moveToPosition(i);
		return mCursor;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		mCursor.moveToPosition(i);
		if (view == null) view = inflater.inflate(R.layout.gridview_item, viewGroup, false);
		ImageView icon = view.findViewById(R.id.gridview_image_cell);
		TextView name = view.findViewById(R.id.gridview_text_cell);
		icon.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
		String userData = mCursor.getString(INDEX_USER_DATA);
		name.setVisibility(View.VISIBLE);
		String userName = mCursor.getString(INDEX_USER_NAME);
		String iconId = "0";
		try {
			JSONObject jsonObject = new JSONObject(userData);
			iconId = jsonObject.getString(COLUMN_USER_ICON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("position:"+i+"icon id:"+iconId);
		Glide.with(mContext).load(userIconDrawables.getResourceId(Integer.parseInt(iconId), 0)).into(icon);
//		icon.setImageBitmap(ImageUtil.getUserIcon(mContext, Integer.parseInt(iconId)));
		name.setText(userName);
		String[] s = new String[]{mCursor.getString(INDEX_USER_ID), userData, userName};
		icon.setOnClickListener(view1 -> onIconClicked.clicked(s));
//		Log.i(TAG, "Gridview set for user: " + userName);
		return view;
	}

	public void swapCursor(Cursor data) {
		mCursor = data;
		notifyDataSetChanged();
	}
}
