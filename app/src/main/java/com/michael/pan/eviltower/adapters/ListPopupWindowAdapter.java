package com.michael.pan.eviltower.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.michael.pan.eviltower.R;

import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_RELOAD_GAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_SAVE_GAME;

public class ListPopupWindowAdapter extends BaseAdapter {
	private Activity mActivity;
	private String[] mDataSource;
	private LayoutInflater layoutInflater;
	private OnClickButtonListener clickButtonListener;
	public String tag;
	private String[] savingSlots = new String[]{"Saving Slot 1", "Saving Slot 2", "Saving Slot 3"};

	public ListPopupWindowAdapter(Activity activity, String[] dataSource, @NonNull OnClickButtonListener clickButtonListener) {
		this.mActivity = activity;
		this.mDataSource = dataSource;
		layoutInflater = mActivity.getLayoutInflater();
		this.clickButtonListener = clickButtonListener;
		savingSlots = new String[]{mActivity.getString(R.string.saving_slot_one), mActivity.getString(R.string.saving_slot_two), mActivity.getString(R.string.saving_slot_three)};
	}

	@Override
	public int getCount() {
		return mDataSource.length;
	}

	@Override
	public String getItem(int position) {
		return mDataSource[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = layoutInflater.inflate(R.layout.item_list_popup_window, null);
			holder.tvTitle = view.findViewById(R.id.popup_menu_title);
			holder.tvContent = view.findViewById(R.id.popup_menu_content);
			holder.icon = view.findViewById(R.id.popup_menu_icon);
			holder.icon.setOnClickListener(v -> clickButtonListener.onClickButton(position));
			if (tag.equals(TAG_SAVE_GAME)) {
				if (!getItem(position).equals("No Data"))
					holder.icon.setImageResource(R.drawable.ic_save_blue_700_48dp);
				else holder.icon.setImageResource(R.drawable.ic_save_grey_400_48dp);
			} else if (tag.equals(TAG_RELOAD_GAME)) {
				if (!getItem(position).equals("No Data")) {
					holder.icon.setClickable(true);
					holder.icon.setImageResource(R.drawable.ic_cloud_download_green_700_48dp);
				} else {
					holder.icon.setClickable(false);
					holder.icon.setImageResource(R.drawable.ic_cloud_download_grey_400_48dp);
				}
			}
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// bind data
		holder.tvContent.setText(getItem(position));
		holder.tvContent.setTextColor(mActivity.getResources().getColor(R.color.material_blue_700));
		holder.tvTitle.setText(savingSlots[position]);

		return view;
	}

	private class ViewHolder {
		private TextView tvTitle, tvContent;
		private ImageView icon;
	}

	// interface to return callback to activity
	public interface OnClickButtonListener {
		void onClickButton(int position);
	}
}
