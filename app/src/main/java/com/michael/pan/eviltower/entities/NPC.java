package com.michael.pan.eviltower.entities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.activities.GameOverActivity;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.databinding.ActivityStartGameBinding;
import com.michael.pan.eviltower.fragments.DialogViewFragment;
import com.michael.pan.eviltower.fragments.StoreViewFragment;
import com.michael.pan.eviltower.services.SnapshotTask;
import com.michael.pan.eviltower.services.TransitionHelper;
import com.michael.pan.eviltower.utilities.AnimUtil;
import com.michael.pan.eviltower.views.SnapshotView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.activities.StartGameActivity.warrior;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_GAME_FINISHED;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_SCORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ATTACK;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DEFENSE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_DIFFICULTY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ENERGY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_LEVEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.ERROR_LOW_COIN;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.ERROR_LOW_EXPERIENCE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.ERROR_NO_BLUE_KEY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_NPC_TYPE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.TAG_SOLD_BLUE_KEY;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_AFTER_THE_GAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_CANCEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_CRYSTAL_EXIT;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GAME_OVER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_REAL_PRINCESS;
import static com.michael.pan.eviltower.utilities.GameUtil.refreshFragment;

public class NPC implements DialogViewFragment.OnDialogViewClickedListener, StoreViewFragment.CloseStoreWindow, SnapshotTask.onTaskExecuted {

	private FragmentManager fragmentManager;
	private TextView view;
	private Bundle extras;
	private ActivityStartGameBinding binding;
	private int xTouch, yTouch, floor;

	public NPC(Context context, FragmentManager fragmentManager, ActivityStartGameBinding binding, Bundle extras) {
		this.binding = binding;
		view = binding.popupMessage;
		this.fragmentManager = fragmentManager;
		this.extras = extras;
		int matrixValue = extras.getInt(context.getString(R.string.matrix_value));
		floor = extras.getInt(context.getString(R.string.floor));
		xTouch = extras.getInt(context.getString(R.string.xTouch));
		yTouch = extras.getInt(context.getString(R.string.yTouch));
		String type = getNpcType(matrixValue);
		extras.putString(EXTRA_NPC_TYPE, type);
		switch (type) {
			case EvilTowerContract.TAG_STORE:
			case EvilTowerContract.TAG_LARGE_STORE:
			case EvilTowerContract.TAG_SUPER_STORE:
			case EvilTowerContract.TAG_GIANT_STORE:
				StoreViewFragment storeViewFragment = new StoreViewFragment(context, type, this);
				storeViewFragment.setArguments(extras);
				fragmentManager.beginTransaction().replace(R.id.message_view_holder, storeViewFragment, refreshFragment(fragmentManager, EvilTowerContract.FRAGMENT_TAG_STORE_VIEW)).commit();
				break;
			case EvilTowerContract.TAG_GOING_RIGHT:
				if (floor == 34) {
					gameView.setFloor(floor + 1);
					warrior.setX(1);
					warrior.setY(5);
				} else if (floor == 37) {
					gameView.setFloor(floor - 1);
					warrior.setX(1);
					warrior.setY(3);
				}
				break;
			case EvilTowerContract.TAG_GOING_LEFT:
				if (floor == 36) {
					gameView.setFloor(floor + 1);
					warrior.setX(13);
					warrior.setY(3);
				} else if (floor == 35) {
					gameView.setFloor(floor - 1);
					warrior.setX(14);
					warrior.setY(5);
				}
				break;
			case EvilTowerContract.TAG_GOING_UP:
				switch (floor) {
					case 39:
						gameView.setFloor(floor + 1);
						warrior.setY(10);
						warrior.setX(10);
						break;
					case 31:
						gameView.setFloor(floor - 1);
						warrior.setX(0);
						warrior.setY(10);
						break;
					case 38:
						gameView.setFloor(floor - 2);
						warrior.setX(10);
						warrior.setY(10);
						break;
				}
				break;
			case EvilTowerContract.TAG_GOING_DOWN:
				switch (floor) {
					case 30:
						gameView.setFloor(floor + 1);
						warrior.setX(0);
						warrior.setY(1);
						break;
					case 36:
						gameView.setFloor(floor + 2);
						warrior.setX(10);
						warrior.setY(1);
						break;
					case 40:
						gameView.setFloor(floor - 1);
						warrior.setX(9);
						warrior.setY(0);
						break;
				}
				break;
			case EvilTowerContract.TAG_TORCH_LIGHT:
				gameView.layer01[yTouch][xTouch] = 0;
				gameLiveData.setEnergy(gameLiveData.getEnergy() + 100);
				AnimUtil.sendFlyMessage(view, context.getString(R.string.plus_100_energy));
				break;
			case TAG_CRYSTAL_EXIT:
				gameView.setFloor(0);
				gameView.layer01[11][11] = 68;
				warrior.setY(10);
				warrior.setX(12);
				gameView.update();
				break;
			default:
				DialogViewFragment dialogFragment = new DialogViewFragment(context, this);
				dialogFragment.setArguments(extras);
				this.fragmentManager.beginTransaction().replace(R.id.message_view_holder, dialogFragment, EvilTowerContract.FRAGMENT_TAG_DIALOG_VIEW).commit();
				break;
		}
	}

	private String getNpcType(int type) {

		switch (type) {
			case 46:
				return EvilTowerContract.TAG_EXPERIENCE;
			case 47:
				return EvilTowerContract.TAG_EXPERIENCE_VISITED;
			case 48:
				return EvilTowerContract.TAG_KEY_MERCHANT;
			case 49:
				return EvilTowerContract.TAG_KEY_MERCHANT_VISITED;
			case 50:
				return EvilTowerContract.TAG_TECHNICIAN;
			case 52:
				return EvilTowerContract.TAG_ANGEL;
			case 53:
				return EvilTowerContract.TAG_ANGEL_VISITED;
			case 54:
				return EvilTowerContract.TAG_WIZARD_BLUE;
			case 56:
			case 57:
				return EvilTowerContract.TAG_SECRET_MERCHANT;
			case 60:
				return EvilTowerContract.TAG_PRISONER;
			case 64:
			case 65:
				if (gameView.floor == 22 || gameView.floor == 35)
					return EvilTowerContract.TAG_SUPER_STORE;
				else if (gameView.floor == 32) return EvilTowerContract.TAG_GIANT_STORE;
				else return EvilTowerContract.TAG_LARGE_STORE;
			case 66:
			case 67:
				if (gameView.floor == 24) return EvilTowerContract.TAG_GIANT_STORE;
				else return EvilTowerContract.TAG_STORE;
			case 68:
				return TAG_REAL_PRINCESS;
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 91:
			case 92:
			case 96:
			case 98:
			case 100:
				return EvilTowerContract.TAG_RED_DRAGON;
			case 70:
			case 74:
			case 76:
			case 78:
			case 80:
			case 82:
			case 84:
				return EvilTowerContract.TAG_GIANT_SPIDER;
			case 338:
			case 342:
				return TAG_CRYSTAL_EXIT;
			case 350:
				return EvilTowerContract.TAG_TORCH_LIGHT;
			case 378:
				return EvilTowerContract.TAG_GOING_DOWN;
			case 386:
				return EvilTowerContract.TAG_GOING_RIGHT;
			case 382:
				return EvilTowerContract.TAG_GOING_LEFT;
			case 390:
				return EvilTowerContract.TAG_GOING_UP;
			default:
				return EvilTowerContract.TAG_UNKNOWN;
		}
	}

	@Override
	public void onDialogViewClicked(Context context, String s) {
//		gameView.handlingEvents = false;
		switch (s) {
			case EvilTowerContract.TAG_OK:
				gameView.update();
			case EvilTowerContract.TAG_CANCEL:
				Fragment fragment = fragmentManager.findFragmentByTag(EvilTowerContract.FRAGMENT_TAG_DIALOG_VIEW);
				if (fragment != null) {
					fragmentManager.beginTransaction().remove(fragment).commit();
					gameView.handlingDialogEvents = false;
				}
				break;
			case ERROR_LOW_EXPERIENCE:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.error_level_up));
//				gameView.handlingEvents = false;
				break;
			case ERROR_LOW_COIN:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.error_coin));
				break;
			case EvilTowerContract.ERROR_MAXIMUM_LEVEL_ONE:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.error_maximum_level_reached_one));
				break;
			case EvilTowerContract.TAG_GOT_BLUE_KEY:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.info_get_blue_key));
				break;
			case EvilTowerContract.TAG_GOT_YELLOW_KEY:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.info_get_yellow_key));
				break;
			case EvilTowerContract.TAG_LEVELED_UP_ONE:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.info_get_leveled_up_1));
				break;
			case EvilTowerContract.TAG_LEVELED_UP_SEVEN:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.info_get_leveled_up_7));
				break;
			case EvilTowerContract.TAG_LEVELED_UP_TEN:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.info_get_leveled_up_10));
				break;
			case EvilTowerContract.TAG_GOT_RED_KEY:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.info_get_red_key));
				break;
			case EvilTowerContract.TAG_GOT_KEY_BOX:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.info_got_key_box));
				if (floor == 42) {
					gameView.layer01[yTouch][xTouch] = 0;
					gameView.update();
				}
				break;
			case TAG_SOLD_BLUE_KEY:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.sold_one_b_key));
				break;
			case ERROR_NO_BLUE_KEY:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.error_no_b_keys));
				break;
			case EvilTowerContract.TAG_WIZARD_BLUE:// if challenge the blue wizard.
				extras.putInt(context.getString(R.string.matrix_value), -100);
				gameView.handlingDialogEvents = false;
				new Enemy(context, extras, view, fragmentManager);//point to battle with npc
				break;
			case TAG_REAL_PRINCESS:
				gameView.layer00[10][12] = 298;
				gameView.layer00[10][11] = 298;
				gameView.layer00[10][10] = 298;
				gameView.layer00[10][13] = 298;
				gameView.layer01[2][9] = 338;
				gameView.layer01[3][9] = 342;
				gameView.update();
				this.onDialogViewClicked(context, TAG_CANCEL);
				break;
			case TAG_GAME_OVER:
				this.onDialogViewClicked(context, TAG_CANCEL);
				binding.gameViewHolder.addView(new SnapshotView(context));
				SnapshotTask snapshotTask = new SnapshotTask(context, this);
				snapshotTask.execute(TAG_AFTER_THE_GAME);
				break;
		}
	}

	@Override
	public void closeStore() {
		gameView.handlingStoreViewWindow = false;
		refreshFragment(fragmentManager, EvilTowerContract.FRAGMENT_TAG_STORE_VIEW);
	}

	@Override
	public void snapshotTaken() {
		gameView.handlingDialogEvents = false;
	}

	@Override
	public void snapshotTakenAtEnd(Context context) {
		binding.gameInfoViewHolder.setVisibility(View.VISIBLE);
		binding.gameViewPanel.setVisibility(View.GONE);
		binding.textGameIntro.setText(R.string.game_end_intro);
		binding.textGameIntro.setGravity(View.TEXT_ALIGNMENT_CENTER);
		binding.textGameIntroSkip.setVisibility(View.GONE);
		binding.textGameIntro.setOnClickListener(v -> {
			try {
				startGameFinishedActivity(context);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		});
	}

	private void startGameFinishedActivity(Context context) throws JSONException {
//		Sample sample = new Sample(R.color.sample_red, "Transitions");
//		final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants((Activity) context, false);
		Intent intent = new Intent(context, GameOverActivity.class);

		JSONObject jsonExtras = new JSONObject();
		jsonExtras.put(COLUMN_ID, gameLiveData.userId);
		jsonExtras.put(COLUMN_NAME, gameLiveData.userName);
		jsonExtras.put(COLUMN_ATTACK, gameLiveData.getAttack());
		jsonExtras.put(COLUMN_DEFENSE, gameLiveData.getDefense());
		jsonExtras.put(COLUMN_ENERGY, gameLiveData.getEnergy());
		jsonExtras.put(COLUMN_LEVEL, gameLiveData.getLevel());
		jsonExtras.put(COLUMN_DIFFICULTY, gameLiveData.difficulty);
		int score = (int) ((gameLiveData.getAttack() * 1.2f + gameLiveData.getEnergy() * 0.5f + gameLiveData.getDefense() * 1f) * gameLiveData.getLevel() / 100f);
		jsonExtras.put(EXTRA_SCORE, score);
		intent.putExtra(EXTRA_GAME_FINISHED, jsonExtras.toString());
//		ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pairs);
//		intent.putExtra("sample", sample);
//		context.startActivity(intent, transitionActivityOptions.toBundle());
		context.startActivity(intent);
		Activity activity = (Activity) context;
		activity.finish();
	}
}
