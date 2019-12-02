package com.michael.pan.eviltower.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.michael.pan.eviltower.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_COMPLETION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DIFFICULTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USER_ICON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.INDEX_USER_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_CROWN;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DELETE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DETAILS;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_EASY;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_HARD;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_MEDIUM;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_EMPTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_RESUME;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_UNKNOWN;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.GameViewHolder> {

	private Context mContext;
	private GameListOnClickListener mListClickListener;
	private Cursor mCursor;
	public ArrayList<String> idList = new ArrayList<>();
	private String difficultyTag, gameStatis = TAG_EMPTY;
	private TypedArray userIconDrawables;
	public GameViewHolder holder;
	private List<Integer> positionIdList;
	public interface GameListOnClickListener{
		void onClick(int id, String data, String instruction);
	}

	public UserListAdapter(Context context, GameListOnClickListener listener, TypedArray userIconDrawables){
		mContext = context;
		mListClickListener = listener;
		this.userIconDrawables = userIconDrawables;
	}

	@NonNull
	@Override
	public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.game_list_item_view, parent, false);
		view.setFocusable(true);
		holder = new GameViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
		mCursor.moveToPosition(positionIdList.get(position));

		//holder.userIdInList.setText(mCursor.getString(INDEX_USER_ID));
		holder.userLevelInList.setText(String.format("%s%s", mContext.getString(R.string.level_is), mCursor.getString(INDEX_USER_LEVEL)));
		holder.userNameInList.setText(mCursor.getString(INDEX_USER_NAME));
		//holder.entryRecordTime.setText(mCursor.getString(INDEX_RECORD_TIME));
		holder.idOfCOLUMN_ID = mCursor.getInt(INDEX_USER_ID);
		String userData = mCursor.getString(INDEX_USER_DATA);
		String userIcon = "";
		JSONObject userDataJson;
		try {
			userDataJson = new JSONObject(userData);
			userIcon = userDataJson.getString(COLUMN_USER_ICON);
			//in database the tag is COLUMN_DIFFICULTY, in the intent extras it is EXTRA_DIFFICULTY
			if (userDataJson.has(COLUMN_DIFFICULTY)) difficultyTag = userDataJson.getString(COLUMN_DIFFICULTY);
			else difficultyTag = TAG_UNKNOWN;
			if (!userDataJson.has(COLUMN_COMPLETION)) userDataJson.put(COLUMN_COMPLETION, TAG_EMPTY);
			if (!userDataJson.getString(COLUMN_COMPLETION).equals(TAG_EMPTY)) {
				holder.resume.setVisibility(View.GONE);
				holder.details.setVisibility(View.GONE);
				holder.crown.setVisibility(View.VISIBLE);
				gameStatis = userDataJson.getString(COLUMN_COMPLETION);
			} else {
				holder.resume.setVisibility(View.VISIBLE);
				holder.details.setVisibility(View.VISIBLE);
				holder.crown.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		System.out.println("difficulty"+difficultyTag+"@"+position);
		switch (difficultyTag) {
			case TAG_DIFFICULTY_EASY:
				holder.difficulty.setText(mContext.getString(R.string.tag_easy));
				holder.difficulty.setTextColor(mContext.getResources().getColor(R.color.material_light_green_accent_700));
				break;
			case TAG_DIFFICULTY_MEDIUM:
				holder.difficulty.setText(mContext.getString(R.string.tag_medium));
				holder.difficulty.setTextColor(mContext.getResources().getColor(R.color.material_yellow_accent_700));
				break;
			case TAG_DIFFICULTY_HARD:
				holder.difficulty.setText(mContext.getString(R.string.tag_hard));
				holder.difficulty.setTextColor(mContext.getResources().getColor(R.color.material_red_accent_700));
				break;
		}
		// Load the images into the ImageView using the Glide library.
		Glide.with(mContext).load(userIconDrawables.getResourceId(Integer.parseInt(userIcon), 0)).into(holder.userIcon);
//		holder.userIcon.setImageBitmap(ImageUtil.getUserIcon(mContext, Integer.parseInt(userIcon)));
	}

	@Override
	public int getItemCount() {
		if (mCursor == null) return 0;
		if (positionIdList != null) return positionIdList.size();
		return mCursor.getCount();
	}

	public void swapCursor(Cursor newCursor, List<Integer> positionIdList){
		mCursor = newCursor;
		int count = mCursor.getCount();
		this.positionIdList = positionIdList;
		setUserIdList(count);
		notifyDataSetChanged();
	}

	private void setUserIdList(int n) {
		idList = new ArrayList<>();
		for (int i = 0; i < n; i++){
			mCursor.moveToPosition(positionIdList.get(i));
			idList.add(String.valueOf(mCursor.getInt(INDEX_USER_ID)));
		}
	}

	public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

		public Button resume;
		Button delete;
		Button details;
		TextView userIdInList;
		public TextView userNameInList;
		TextView userLevelInList;
		TextView entryRecordTime;
		public TextView difficulty;
		public ImageView userIcon;
		ImageView crown;
		int idOfCOLUMN_ID;
		GameViewHolder(@NonNull View itemView) {
			super(itemView);
			userNameInList = itemView.findViewById(R.id.user_name_in_list);
			userLevelInList = itemView.findViewById(R.id.level_in_list);
			userIcon = itemView.findViewById(R.id.game_list_user_icon);
			resume = itemView.findViewById(R.id.game_list_resume_button);
			delete = itemView.findViewById(R.id.button_close);
			difficulty = itemView.findViewById(R.id.difficulty_in_list);
			details = itemView.findViewById(R.id.button_details);
			crown = itemView.findViewById(R.id.user_list_crown);
			resume.setOnClickListener(this);
			delete.setOnClickListener(this);
			details.setOnClickListener(this);
			crown.setOnClickListener(this);
			userIcon.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.game_list_resume_button:
					mListClickListener.onClick(idOfCOLUMN_ID, null, TAG_RESUME);
					break;
				case R.id.button_close:
					mListClickListener.onClick(idOfCOLUMN_ID, null, TAG_DELETE);
					break;
				case R.id.button_details:
				case R.id.game_list_user_icon:
					mListClickListener.onClick(idOfCOLUMN_ID, null, TAG_DETAILS);
					break;
				case R.id.user_list_crown:
					mListClickListener.onClick(idOfCOLUMN_ID, gameStatis, TAG_CROWN);
					break;
			}
		}
	}
}

