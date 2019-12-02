package com.michael.pan.eviltower.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.databinding.GameEndFragmentsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_SCORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ATTACK;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DEFENSE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DIFFICULTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ENERGY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_EASY;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_HARD;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_DIFFICULTY_MEDIUM;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_UNKNOWN;

public class SlideFragment extends Fragment {

	@StringRes
	private static final int[] PAGE_TITLES = new int[]{ R.string.congratulations, R.string.about_game, R.string.about_autor};
	private static final int[] PAGE_SUBTITLES1 = new int[]{ R.string.task_finished, R.string.statistics, R.string.contact_author};
	private static final int[] PAGE_CONTENT_TITLE1 = new int[]{ R.string.attack_text, R.string.total_floors, R.string.webpage_design};
	private static final int[] PAGE_CONTENT_TITLE2 = new int[]{ R.string.defense_text, R.string.label_difficulty, R.string.map_design};
	private static final int[] PAGE_CONTENT_TITLE3 = new int[]{ R.string.energy_text, R.string.label_saving_slot, R.string.blank};
	private static final int[] PAGE_IMAGE_INDICATOR = new int[]{R.drawable.crowns, R.drawable.dark_background, R.drawable.rabbit};
	private String[] PAGE_CONTENT_VALUE1, PAGE_CONTENT_VALUE2, PAGE_CONTENT_VALUE3;
	private String[] PAGE_SUBTITLES2;
	private String extras;
	private int  attack, defense, energy, level, score, index = 0;
	private String userName, userId, difficulty = TAG_UNKNOWN;
	private JSONObject jsonExtras;

	public SlideFragment(int position, String extras) {
		this.index = position;
		this.extras = extras;
	}

	@Override
	public View onCreateView(
		@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		GameEndFragmentsBinding binding = DataBindingUtil.inflate(inflater, R.layout.game_end_fragments, container, false);
		View root = binding.getRoot();

		PAGE_CONTENT_VALUE1 = new String[]{getString(R.string.dummy), getString(R.string.total_floor_no), getString(R.string.webdesigner_value)};
		PAGE_CONTENT_VALUE2 = new String[]{getString(R.string.dummy), getString(R.string.difficulty_info), getString(R.string.info_map_editor)};
		PAGE_CONTENT_VALUE3 = new String[]{getString(R.string.dummy), getString(R.string.total_slot_no), getString(R.string.blank)};
		PAGE_SUBTITLES2 = new String[]{getString(R.string.dummy), getString(R.string.blank), getString(R.string.author_email)};
		jsonExtras = new JSONObject();
		if (extras != null){
//			index = extras.getInt(ARG_SECTION_NUMBER); ??Strange extra did not a good job passing the index
			try {
				jsonExtras = new JSONObject(extras);
				attack = jsonExtras.getInt(COLUMN_ATTACK);
				defense = jsonExtras.getInt(COLUMN_DEFENSE);
				energy = jsonExtras.getInt(COLUMN_ENERGY);
				level = jsonExtras.getInt(COLUMN_LEVEL);
				score = jsonExtras.getInt(EXTRA_SCORE);
				userId = jsonExtras.getString(COLUMN_ID);
				userName = jsonExtras.getString(COLUMN_NAME);
				difficulty = jsonExtras.getString(COLUMN_DIFFICULTY);
			} catch (JSONException e) {
				e.printStackTrace();
			}
//			if (gameLiveData != null){
//				userName = gameLiveData.userName;
//				userId = gameLiveData.userId;
//				difficulty = gameLiveData.difficulty;
//			}
			PAGE_CONTENT_VALUE1[0] = String.valueOf(attack);
			PAGE_CONTENT_VALUE2[0] = String.valueOf(defense);
			PAGE_CONTENT_VALUE3[0] = String.valueOf(energy);
			PAGE_SUBTITLES2[0] = getString(R.string.total_score) + score;
		}
		binding.toolbarText.setText(String.format("%s:%s", userName, getString(R.string.task_finished)));
		binding.viewPagerTitle.setText(SlideFragment.this.getString(PAGE_TITLES[index]));
		binding.viewPagerSubtitle1.setText(SlideFragment.this.getString(PAGE_SUBTITLES1[index]));
		binding.viewPagerContent11.setText(SlideFragment.this.getString(PAGE_CONTENT_TITLE1[index]));
		binding.viewPagerContent21.setText(SlideFragment.this.getString(PAGE_CONTENT_TITLE2[index]));
		binding.viewPagerContent31.setText(SlideFragment.this.getString(PAGE_CONTENT_TITLE3[index]));
		binding.viewPagerContent12.setText(PAGE_CONTENT_VALUE1[index]);
		binding.viewPagerContent22.setText(PAGE_CONTENT_VALUE2[index]);
		binding.viewPagerContent32.setText(PAGE_CONTENT_VALUE3[index]);
		binding.viewPagerSubtitle2.setText(PAGE_SUBTITLES2[index]);
		Glide.with(this).load(PAGE_IMAGE_INDICATOR[index]).into(binding.viewPagerIcon);
//		binding.viewPagerIcon.setImageDrawable(getResources().getDrawable(PAGE_IMAGE_INDICATOR[index]));
		if (index == 2) {
			binding.column3.setVisibility(View.GONE);
			binding.columnDivider2.setVisibility(View.GONE);
		}
		if (index == 1){
			switch (difficulty){
				case TAG_DIFFICULTY_EASY:
					binding.viewPagerContent22.setText(getString(R.string.tag_easy));
					binding.viewPagerContent22.setTextColor(getResources().getColor(R.color.material_green_700));
					break;
				case TAG_DIFFICULTY_MEDIUM:
					binding.viewPagerContent22.setText(getString(R.string.tag_medium));
					binding.viewPagerContent22.setTextColor(getResources().getColor(R.color.material_yellow_700));
					break;
				case TAG_DIFFICULTY_HARD:
					binding.viewPagerContent22.setText(getString(R.string.tag_hard));
					binding.viewPagerContent22.setTextColor(getResources().getColor(R.color.material_red_700));
					break;
				default:
					binding.viewPagerContent22.setText(TAG_UNKNOWN);
					break;
			}
		}

	return root;
	}
}