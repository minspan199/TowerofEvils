package com.michael.pan.eviltower.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.utilities.ImageUtil;

import java.util.ArrayList;

public class IconGridAdapter extends BaseAdapter {

	private static ArrayList<Integer> dataList;
	private LayoutInflater inflater;
	private Context mContext;
	private String TAG = "UserIconAdapter:";
	private TypedArray userIconDrawables;
	onIconClicked onIconClicked;

	public interface onIconClicked {
		void iconClicked(int userId);
	}

	public IconGridAdapter(Context context, onIconClicked mIconClicked, TypedArray userIconDrawables) {
		onIconClicked = mIconClicked;
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.userIconDrawables = userIconDrawables;
	}

	@Override
	public int getCount() {
		if (dataList == null) return 0;
		return dataList.size();
	}

	@Override
	public Integer getItem(int i) {
		return dataList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if (view == null) view = inflater.inflate(R.layout.gridview_item, viewGroup, false);
		ImageView icon = view.findViewById(R.id.gridview_image_cell);
		icon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200));
		Glide.with(mContext).load(userIconDrawables.getResourceId(i, 0)).into(icon);
//		icon.setImageBitmap(ImageUtil.getUserIcon(mContext, i));
		icon.setOnClickListener(view1 -> onIconClicked.iconClicked(i));
		return view;
	}

	public void swapData(ArrayList<Integer> data) {
		dataList = data;
		notifyDataSetChanged();
	}
}
