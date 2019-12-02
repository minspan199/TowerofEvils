package com.michael.pan.eviltower.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.entities.Door;
import com.michael.pan.eviltower.utilities.GameHelperUtil;
import com.michael.pan.eviltower.utilities.ImageUtil;

import java.util.Locale;
import java.util.Objects;

import static com.michael.pan.eviltower.activities.StartGameActivity.door;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.data.EvilTowerContract.ERROR_MAXIMUM_LEVEL_FIVE;
import static com.michael.pan.eviltower.data.EvilTowerContract.ERROR_MAXIMUM_LEVEL_ONE;
import static com.michael.pan.eviltower.data.EvilTowerContract.ERROR_MAXIMUM_LEVEL_SEVEN;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.ERROR_LOW_COIN;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.ERROR_LOW_EXPERIENCE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.ERROR_NO_BLUE_KEY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_NPC_TYPE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.TAG_SOLD_BLUE_KEY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.TAG_ENEMY_BEFORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ANGEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_CANCEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ENEMY_AFTER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_EXPERIENCE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GAME_OVER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GIANT_SPIDER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GOT_BLUE_KEY;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GOT_KEY_BOX;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GOT_RED_KEY;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GOT_YELLOW_KEY;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_KEY_MERCHANT;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_LEVELED_UP_FIVE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_LEVELED_UP_ONE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_LEVELED_UP_SEVEN;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_LEVELED_UP_TEN;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_OK;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_PRISONER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_REAL_PRINCESS;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_RED_DRAGON;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_SECRET_MERCHANT;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_TECHNICIAN;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_WIZARD_BLUE;
import static com.michael.pan.eviltower.utilities.GameHelperUtil.getNPCName;

public class DialogViewFragment extends Fragment {

	private OnDialogViewClickedListener mDialogCallback;
	private Context context;
	private static int index = 0;
	private String[] dialogContents;
	private String TAG = "DialogViewFragment: ";

	public interface OnDialogViewClickedListener{
		void onDialogViewClicked(Context context, String s);
	}

	public DialogViewFragment(Context context, OnDialogViewClickedListener onDialogViewClickedListener) {
		this.mDialogCallback = onDialogViewClickedListener;
		this.context = context;
		gameView.handlingDialogEvents = true;
	}

	@Override
	public void onAttach(@NonNull Context context) {
		// To check if the mDialogCallback has been implemented in the activity class
		super.onAttach(context);
		try{
			mDialogCallback = (OnDialogViewClickedListener) context;
//			Log.i(TAG, "implemented OnDialogViewClickedListener");
		}catch (ClassCastException e){
//			Log.i(TAG, Objects.requireNonNull(new ClassCastException(context.toString() + "must implement OnDialogViewClickedListener").getMessage()));
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.dialog_view, container, false);
		ImageView npcIcon = rootView.findViewById(R.id.npc_icon);
		TextView dialogContent = rootView.findViewById(R.id.dialog_content);

		Button dialog_ok = rootView.findViewById(R.id.dialog_ok);
		Button dialog_cancel = rootView.findViewById(R.id.dialog_cancel);

		Bitmap npcImage, userIcon;
		Bundle bundle = this.getArguments();
		assert bundle != null;
		String npcType = bundle.getString(EXTRA_NPC_TYPE, "");
		int xTouch = bundle.getInt(context.getString(R.string.xTouch));
		int yTouch = bundle.getInt(context.getString(R.string.yTouch));
		int floor = bundle.getInt(context.getString(R.string.floor));
//		Log.i(TAG, " Beginning dialog with " + npcType);
		UIPopulation(npcType, dialog_ok, dialog_cancel);

		npcImage = ImageUtil.getNpcIcon(getActivity(), npcType, floor);
//		npcIcon.setImageBitmap(npcImage);
		userIcon = ImageUtil.getUserIcon(context, gameLiveData.getIcon());
		dialogContents = GameHelperUtil.getDialogContent(getContext(), npcType, floor);
		dialogContent.setText(dialogContents[0]);
		if (ifUserIcon(floor, npcType)){
			dialogContent.setText(String.format("%s%s", context.getString(R.string.me_dialog), dialogContents[0]));
			npcIcon.setImageBitmap(userIcon);
		} else {
			//getContext here can make the content translated!!
			dialogContent.setText(String.format("%s%s", getNPCName(getContext(), npcType), dialogContents[0]));
			npcIcon.setImageBitmap(npcImage);
		}
		View.OnClickListener listener = view -> {
			int id = view.getId();
//			Log.i(TAG, "Handling click on DialogView with NPC: " + npcType);
			if (id == R.id.dialog_ok || id == R.id.dialog_content) {
				if (index < dialogContents.length - 1) {
					index++;
					if (ifUserIcon(floor, npcType)){
						dialogContent.setText(String.format("%s%s", context.getString(R.string.me_dialog), dialogContents[index]));
						npcIcon.setImageBitmap(userIcon);
					} else {
						dialogContent.setText(String.format("%s%s", getNPCName(getContext(), npcType), dialogContents[index]));
						npcIcon.setImageBitmap(npcImage);
					}
				} else {// else there is no more dialog contents
					index = 0;
					switch (npcType) {
						case TAG_ANGEL: //angel opens door on the ground floor.
							switch (floor) {
								case 0:
									Bundle extras = new Bundle();
									extras.putInt(context.getString(R.string.xTouch), xTouch);
									extras.putInt(context.getString(R.string.yTouch), yTouch);
									extras.putInt(context.getString(R.string.matrix_value), 31);//steel door
									extras.putInt(context.getString(R.string.floor), floor);
									door = new Door(context, extras, null, true);//null textField means no text need to be sent and the door open directly.
									break;
								case 3:
								case 9:
								case 16:
								case 32:
								case 36:
								case 38:
									gameView.layer01[yTouch][xTouch] = 0;
									break;
							}
							mDialogCallback.onDialogViewClicked(getContext(), TAG_OK);
							break;
						case TAG_ENEMY_BEFORE:
//							if (floor == 15 || floor == 29 || floor == 32 || floor == 37 || floor == 40)
							mDialogCallback.onDialogViewClicked(getContext(), TAG_OK);
							break;
						case TAG_ENEMY_AFTER:
							switch (floor) {
								case 15:
									gameView.layer02[4][3] = 0;
									gameView.layer02[2][3] = 0;
									gameView.layer01[2][5] = 0;
									Bundle extras = new Bundle();
									extras.putInt(context.getString(R.string.xTouch), 5);
									extras.putInt(context.getString(R.string.yTouch), 2);
									extras.putInt(context.getString(R.string.floor), floor);
									extras.putInt(context.getString(R.string.matrix_value), -1);//floor collapsing
									gameView.layer03[2][5] = 241;
									door = new Door(context, extras, null, true);
									gameView.update();
									break;
								case 29:
									gameView.layer03[yTouch][xTouch] = 253;
									gameView.layer03[yTouch + 1][xTouch] = 281;
									gameView.layer03[yTouch + 2][xTouch] = 240;
									gameView.update();
									break;
								case 32:
									gameView.layer01[3][9] = 0;
									gameView.layer01[3][10] = 0;
									gameView.layer01[3][11] = 0;
									gameView.layer01[4][9] = 0;
									gameView.layer01[4][10] = 0;
									gameView.layer01[4][11] = 0;
									gameView.layer01[5][9] = 0;
									gameView.layer01[5][10] = 0;
									gameView.layer01[5][11] = 0;
									gameView.layer03[4][10] = 241;
									gameView.layer01[1][10] = 0;
									gameView.update();
									break;
								case 37:
									gameView.layer02[6][12] = 0;
									gameView.layer03[6][12] = 248;
									gameView.update();
									break;
								case 40:
									gameView.layer01[2][6] = 0;
									gameView.layer01[2][7] = 0;
									gameView.layer01[2][8] = 0;
									gameView.layer01[3][6] = 0;
									gameView.layer01[3][7] = 0;
									gameView.layer01[3][8] = 0;
									gameView.layer01[4][6] = 0;
									gameView.layer01[4][7] = 0;
									gameView.layer02[4][7] = 0;
									gameView.layer01[4][8] = 0;
									gameView.layer03[2][5] = 256;
									gameView.layer03[4][7] = 253;
									gameView.layer03[2][10] = 247;
									gameView.update();
									break;
								case 41:
									gameView.layer00[4][5] = 298;
									gameView.layer00[3][5] = 298;
									gameView.layer00[5][5] = 298;
									gameView.update();
									break;
							}
							mDialogCallback.onDialogViewClicked(getContext(), TAG_CANCEL);
							break;
						case TAG_PRISONER:
							if (floor == 12){
								gameView.layer01[2][4] = 0;
								gameView.layer03[1][4] = 261;
								gameView.layer03[2][4] = 242;
								gameView.update();
								mDialogCallback.onDialogViewClicked(getContext(), TAG_CANCEL);
							} else if (floor == 26){
								gameView.layer01[1][4] = 0;
								gameView.layer03[1][4] = 253;//smile coin
								gameView.update();
								mDialogCallback.onDialogViewClicked(getContext(), TAG_CANCEL);
							}
							break;
						case TAG_TECHNICIAN:
							if (floor == 34) gameView.layer01[8][12] = 0;
							else if (floor == 9) gameView.layer01[yTouch][xTouch] = 0;
						case TAG_RED_DRAGON:
						case TAG_GIANT_SPIDER:
							mDialogCallback.onDialogViewClicked(getContext(), TAG_CANCEL);
							break;
						case TAG_EXPERIENCE:
							switch (floor){
								case 0:
									mDialogCallback.onDialogViewClicked(getContext(), TAG_CANCEL);
									break;
								case 10:
									if (gameLiveData.getExperience() >= 380) {
										if (gameLiveData.getLevel() + 5 > 128){
											mDialogCallback.onDialogViewClicked(getContext(), ERROR_MAXIMUM_LEVEL_FIVE);
											dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), context.getString(R.string.error_maximum_level_reached_five)));
											break;
										}
										if (gameLiveData.setLevel(gameLiveData.getLevel() + 5)){
											gameLiveData.setExperience(gameLiveData.getExperience() - 380);
											commitLevelUpdate(5, gameLiveData.difficulty);
											mDialogCallback.onDialogViewClicked(getContext(), TAG_LEVELED_UP_FIVE);
											dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), getString(R.string.diag_level_5)));
										}
									} else {
										mDialogCallback.onDialogViewClicked(getContext(), ERROR_LOW_EXPERIENCE);
										dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), getString(R.string.dialog_insufficient_experience_380)));
									}
									break;
								case 28:
									if (gameLiveData.getExperience() >= 500) {
										if (gameLiveData.getLevel() + 7 > 256){
											mDialogCallback.onDialogViewClicked(getContext(), ERROR_MAXIMUM_LEVEL_SEVEN);
											dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), context.getString(R.string.error_maximum_level_reached_seven)));
											break;
										}
										if (gameLiveData.setLevel(gameLiveData.getLevel() + 7)){
											gameLiveData.setExperience(gameLiveData.getExperience() - 500);
											commitLevelUpdate(7, gameLiveData.difficulty);
											mDialogCallback.onDialogViewClicked(getContext(), TAG_LEVELED_UP_SEVEN);
											dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), getString(R.string.diag_level_7)));
										}
									} else {
										mDialogCallback.onDialogViewClicked(getContext(), ERROR_LOW_EXPERIENCE);
										dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), getString(R.string.dialog_insufficient_experience_500)));
									}
									break;
								case 35:
									if (gameLiveData.getExperience() >= 500) {
										if (gameLiveData.getLevel() + 7 > 512){
											mDialogCallback.onDialogViewClicked(getContext(), ERROR_MAXIMUM_LEVEL_SEVEN);
											dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), context.getString(R.string.error_maximum_level_reached_512)));
											break;
										}
										if (gameLiveData.setLevel(gameLiveData.getLevel() + 7)){
											gameLiveData.setExperience(gameLiveData.getExperience() - 500);
											commitLevelUpdate(7, gameLiveData.difficulty);
											mDialogCallback.onDialogViewClicked(getContext(), TAG_LEVELED_UP_SEVEN);
											dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), getString(R.string.diag_level_7)));
										}
									} else {
										mDialogCallback.onDialogViewClicked(getContext(), ERROR_LOW_EXPERIENCE);
										dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), getString(R.string.dialog_insufficient_experience_500)));
									}
									break;
								case 39:
								case 42:
									if (gameLiveData.getExperience() > 700 && gameLiveData.setLevel(gameLiveData.getLevel() + 10)){
										gameLiveData.setExperience(gameLiveData.getExperience() - 700);
										commitLevelUpdate(10, gameLiveData.difficulty);
										mDialogCallback.onDialogViewClicked(getContext(), TAG_LEVELED_UP_TEN);
										dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), getString(R.string.dialog_10_levels)));
									} else {
										mDialogCallback.onDialogViewClicked(getContext(), ERROR_LOW_EXPERIENCE);
										dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), getString(R.string.dialog_insufficient_experience_700)));
									}
									break;
								default:
									if (gameLiveData.getExperience() >= 100) {
										if (gameLiveData.getLevel() >= 30){
											mDialogCallback.onDialogViewClicked(getContext(), ERROR_MAXIMUM_LEVEL_ONE);
											dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), context.getString(R.string.error_maximum_level_reached_one)));
											break;
										}
										if (gameLiveData.setLevel(gameLiveData.getLevel() + 1)){
											gameLiveData.setExperience(gameLiveData.getExperience() - 100);
											commitLevelUpdate(1, gameLiveData.difficulty);
											mDialogCallback.onDialogViewClicked(getContext(), TAG_LEVELED_UP_ONE);
											dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), context.getString(R.string.diag_level_up_1)));
										}
									} else {
										mDialogCallback.onDialogViewClicked(getContext(), ERROR_LOW_EXPERIENCE);
										dialogContent.setText(String.format(Locale.US, "%s%s", getNPCName(getContext(), npcType), getString(R.string.error_insufficient_experience_100)));
									}
									break;
							}
							break;
						case TAG_SECRET_MERCHANT:
							if (gameLiveData.getCoins() >= 2000){
								gameLiveData.setCoins(gameLiveData.getCoins() - 2000);
								gameLiveData.setrKey(gameLiveData.getrKey() + 1);
								gameView.layer01[yTouch][xTouch] = 0;
								gameView.update();
								mDialogCallback.onDialogViewClicked(getContext(), TAG_CANCEL);
							} else mDialogCallback.onDialogViewClicked(getContext(),ERROR_LOW_COIN);
							break;
						case TAG_KEY_MERCHANT:
							switch (floor){
								case 0:
									mDialogCallback.onDialogViewClicked(getContext(), TAG_CANCEL);
									break;
								case 2:
								case 6:
									if (gameLiveData.getCoins() > 200){
										gameLiveData.setCoins(gameLiveData.getCoins() - 200);
										gameLiveData.setyKey(gameLiveData.getyKey() + 1);
										mDialogCallback.onDialogViewClicked(getContext(), TAG_GOT_YELLOW_KEY);
									} else mDialogCallback.onDialogViewClicked(getContext(), ERROR_LOW_COIN);
									break;
								case 10:
								case 28:
									if (gameLiveData.getCoins() > 500){
										gameLiveData.setCoins(gameLiveData.getCoins() - 500);
										gameLiveData.setbKey(gameLiveData.getbKey() + 1);
										mDialogCallback.onDialogViewClicked(getContext(),TAG_GOT_BLUE_KEY);
									} else mDialogCallback.onDialogViewClicked(getContext(), ERROR_LOW_COIN);
									break;
								case 35:
									if (gameLiveData.getCoins() > 4000){
										gameLiveData.setCoins(gameLiveData.getCoins() - 4000);
										gameLiveData.setrKey(gameLiveData.getrKey() + 1);
										mDialogCallback.onDialogViewClicked(getContext(), TAG_GOT_RED_KEY);
									} else mDialogCallback.onDialogViewClicked(getContext(), ERROR_LOW_COIN);
									break;
								case 39:
									if (gameLiveData.getbKey() > 0){
										gameLiveData.setbKey(gameLiveData.getbKey() - 1);
										gameLiveData.setCoins(gameLiveData.getCoins() + 150);
										mDialogCallback.onDialogViewClicked(getContext(), TAG_SOLD_BLUE_KEY);
									} else mDialogCallback.onDialogViewClicked(getContext(), ERROR_NO_BLUE_KEY);
									break;
								case 42:
									if (gameLiveData.getCoins() > 4200){
										gameLiveData.setCoins(gameLiveData.getCoins() - 4200);
										gameLiveData.setrKey(gameLiveData.getrKey() + 1);
										gameLiveData.setyKey(gameLiveData.getyKey() + 1);
										gameLiveData.setbKey(gameLiveData.getbKey() + 1);
										mDialogCallback.onDialogViewClicked(getContext(), TAG_GOT_KEY_BOX);
									} else mDialogCallback.onDialogViewClicked(getContext(), ERROR_LOW_COIN);
							}
							break;
						case TAG_WIZARD_BLUE:
							mDialogCallback.onDialogViewClicked(getContext(), TAG_WIZARD_BLUE);
							break;
						case TAG_REAL_PRINCESS:
							switch (floor){
								case 41:
									mDialogCallback.onDialogViewClicked(getContext(), TAG_REAL_PRINCESS);
									break;
								case 0:
									mDialogCallback.onDialogViewClicked(getContext(), TAG_GAME_OVER);
									break;
							}

					}
				}
			} else if (id == R.id.dialog_cancel) {
				index = 0;
				mDialogCallback.onDialogViewClicked(getContext(),TAG_CANCEL);
			}
		};

		dialog_ok.setOnClickListener(listener);
		dialog_cancel.setOnClickListener(listener);
//		dialogContent.setOnClickListener(listener);
		return rootView;
	}

	private void commitLevelUpdate(int level, String difficulty) {
		double factor = 0;
		switch (difficulty){
			case EvilTowerContract.TAG_DIFFICULTY_EASY: factor = 1.5;
			break;
			case EvilTowerContract.TAG_DIFFICULTY_MEDIUM: factor = 1.25;
			break;
			case EvilTowerContract.TAG_DIFFICULTY_HARD: factor = 1.0;
			break;
			default: factor = 8;
			break;
		}
		gameLiveData.setEnergy((int) (gameLiveData.getEnergy() + level*1200*factor));
		gameLiveData.setAttack((int) (gameLiveData.getAttack() + 10*level*factor));
		gameLiveData.setDefense((int) (gameLiveData.getDefense() + 10*level*factor));
	}

	private void UIPopulation(String npcType, Button dialog_ok, Button dialog_cancel) {
		switch (npcType) {
			case EvilTowerContract.TAG_ENEMY_AFTER:
				dialog_cancel.setVisibility(View.GONE);
				break;
			case TAG_EXPERIENCE:
				dialog_ok.setText(context.getString(R.string.note_yes));
				dialog_cancel.setText(context.getString(R.string.note_no));
				break;
			case TAG_TECHNICIAN:
				dialog_cancel.setVisibility(View.GONE);
				dialog_ok.setText(context.getString(R.string.note_yes));
				break;
			case TAG_SECRET_MERCHANT:
				dialog_cancel.setVisibility(View.VISIBLE);
				dialog_ok.setText(context.getString(R.string.note_yes));
				dialog_cancel.setText(context.getString(R.string.note_no));
				break;
			case TAG_WIZARD_BLUE:
				dialog_cancel.setVisibility(View.VISIBLE);
				dialog_cancel.setText(getString(R.string.quit));
				dialog_ok.setVisibility(View.VISIBLE);
				dialog_ok.setText(getString(R.string.fight));
				break;
		}
	}

	private boolean ifUserIcon(int floor, String npcType) {

		switch (floor){
			case 0:
				if (npcType.equals(TAG_REAL_PRINCESS)) return index == 0;
				else return false;
			case 12:
				return index == 1 || index == 3;
			case 11:
				return index == 0 || index == 3;
			case 29:
				return index == 0 && npcType.equals(TAG_ENEMY_BEFORE);
			case 37:
				if ((index == 0 || index == 2 || index == 3 || index == 5 || index== 7) && npcType.equals(TAG_ENEMY_BEFORE)) return true;
				return (index == 3 || index == 5) && npcType.equals(EvilTowerContract.TAG_ENEMY_AFTER);
			case 40:
				return (index == 0 || index == 3) && npcType.equals(TAG_ENEMY_BEFORE);
			case 41:
				if ((index == 1 || index == 3) && npcType.equals(TAG_ENEMY_BEFORE)) return true;
				if (index == 1 && npcType.equals(TAG_ENEMY_AFTER)) return true;
				return index == 1 && npcType.equals(TAG_REAL_PRINCESS);
		}
		return false;
	}
}
