package com.michael.pan.eviltower.entities;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.fragments.EnemyListFragment;
import com.michael.pan.eviltower.fragments.NotePadFragment;
import com.michael.pan.eviltower.utilities.AnimUtil;
import com.michael.pan.eviltower.utilities.GameHelperUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.michael.pan.eviltower.activities.StartGameActivity.flyWingData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.activities.StartGameActivity.treasuresJSON;
import static com.michael.pan.eviltower.activities.StartGameActivity.warrior;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_X;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_Y;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.buildUriByUserId;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.scrollInfo4Floors;

public class Equipment implements EnemyListFragment.onClickEnemyListClose, NotePadFragment.NotebookClose {

	private FragmentManager fragmentManager;
	Integer[] walls = new Integer[]{26, 22, 15, 14, 13};
	List<Integer> wallList = Arrays.asList(walls);
	private TextView message;

	public Equipment(Context context, FragmentManager fragmentManager, TextView popupMessage, String name) {
		message = popupMessage;
		this.fragmentManager = fragmentManager;
		String s = null;
		try {
			s = GameHelperUtil.getHelpStrings(context, name, treasuresJSON.getString(name));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (name.equals(context.getString(R.string.notebook))) {
			NotePadFragment notePadFragment = new NotePadFragment(context, this, context.getString(R.string.notebook));
			fragmentManager.beginTransaction().replace(R.id.notebook_view_holder, notePadFragment, renewFragment(fragmentManager, context.getString(R.string.notebook))).commit();
		} else if (name.equals(context.getString(R.string.smile_coin))) {
			NotePadFragment notePadFragment = new NotePadFragment(context, this, context.getString(R.string.smile_coin));
			fragmentManager.beginTransaction().replace(R.id.notebook_view_holder, notePadFragment, renewFragment(fragmentManager, context.getString(R.string.smile_coin))).commit();
		} else if (name.equals(context.getString(R.string.fly_wing))) {
			NotePadFragment notePadFragment = new NotePadFragment(context, this, context.getString(R.string.fly_wing));
			fragmentManager.beginTransaction().replace(R.id.notebook_view_holder, notePadFragment, renewFragment(fragmentManager, context.getString(R.string.fly_wing))).commit();
		} else if (name.equals(context.getString(R.string.upstairs_wing))) {
			TryFlyTo(context, gameView.floor + 1);
		} else if (name.equals(context.getString(R.string.downstairs_wing))) {
			TryFlyTo(context, gameView.floor - 1);
		} else if (name.equals(context.getString(R.string.book_of_wisdom))) {
			EnemyListFragment enemyListFragment = new EnemyListFragment(this);
			fragmentManager.beginTransaction().replace(R.id.enemy_list_view_holder, enemyListFragment, renewFragment(fragmentManager, context.getString(R.string.enemy_list))).commit();
		} else if (name.equals(context.getString(R.string.blessing_gift))) {
			NotePadFragment notePadFragment = new NotePadFragment(context, this, context.getString(R.string.blessing_gift));
			fragmentManager.beginTransaction().replace(R.id.notebook_view_holder, notePadFragment, renewFragment(fragmentManager, context.getString(R.string.blessing_gift))).commit();
		} else if (name.equals(context.getString(R.string.detonator))) {
			if (UpdateJsonSub(context.getString(R.string.detonator))) exploding(context);
			gameView.update();
		} else if (name.equals(context.getString(R.string.pink_energy_pot))) {
			if (UpdateJsonSub(context.getString(R.string.pink_energy_pot))) {
				gameLiveData.setEnergy((int) (gameLiveData.getEnergy() * 1.1f));
				AnimUtil.sendFlyMessage(message, context.getString(R.string.energy_increase_10per));
			}
		} else if (name.equals(context.getString(R.string.y_key_box))) {
			if (UpdateJsonSub(context.getString(R.string.y_key_box))) {
				gameLiveData.setyKey(gameLiveData.getyKey() + 3);
				AnimUtil.sendFlyMessage(message, context.getString(R.string.info_got_3_ykeys));
			}
		} else if (name.equals(context.getString(R.string.b_key_box))) {
			if (UpdateJsonSub(context.getString(R.string.b_key_box))) {
				gameLiveData.setbKey(gameLiveData.getbKey() + 3);
				AnimUtil.sendFlyMessage(message, context.getString(R.string.info_got_3_bkeys));
			}
		} else if (name.equals(context.getString(R.string.g_key_box))) {
			if (UpdateJsonSub(context.getString(R.string.g_key_box))) {
				gameLiveData.setyKey(gameLiveData.getyKey() + 1);
				gameLiveData.setbKey(gameLiveData.getbKey() + 1);
				gameLiveData.setrKey(gameLiveData.getrKey() + 1);
				AnimUtil.sendFlyMessage(message, context.getString(R.string.info_got_ybr_keys));
			}
		} else if (name.equals(context.getString(R.string.green_pot))) {
			if (UpdateJsonSub(context.getString(R.string.green_pot))) {
				gameLiveData.setExperience((int) (gameLiveData.getExperience() * 1.1f));
				AnimUtil.sendFlyMessage(message, context.getString(R.string.experience_incerease_10per));
			}
		} else if (name.equals(context.getString(R.string.blue_defense_pot))) {
			if (UpdateJsonSub(context.getString(R.string.blue_defense_pot))) {
				gameLiveData.setDefense((int) (gameLiveData.getDefense() * 1.1f));
				AnimUtil.sendFlyMessage(message, context.getString(R.string.defense_increase_10per));
			}
		} else if (name.equals(context.getString(R.string.cyan_attack_pot))) {
			if (UpdateJsonSub(context.getString(R.string.cyan_attack_pot))) {
				gameLiveData.setAttack((int) (gameLiveData.getAttack() * 1.1f));
				AnimUtil.sendFlyMessage(message, context.getString(R.string.attack_increase_10per));
			}
		} else if (name.equals(context.getString(R.string.ice_plate))) {
			extinguishing(context);
			gameView.update();
		} else if (name.equals(context.getString(R.string.old_scroll))) {
			sendScrollInfo(context);
		} else if (name.equals(context.getString(R.string.pickax))) {
			if (UpdateJsonSub(context.getString(R.string.pickax))) {
				int x = warrior.getX();
				int y = warrior.getY();
				System.out.println(warrior.getStatus());
				switch (warrior.getStatus()) {
					case "up":
						CheckPickaxThrough(context, x, y + 1);
						break;
					case "down":
						CheckPickaxThrough(context, x, y - 1);
						break;
					case "left":
						CheckPickaxThrough(context, x - 1, y);
						break;
					case "right":
						CheckPickaxThrough(context, x + 1, y);
						break;
				}
			}
		} else if (name.equals(context.getString(R.string.cross))) {
			if (UpdateJsonSub(context.getString(R.string.cross))) {
				gameLiveData.setDefense((int) (gameLiveData.getDefense() * 1.1f));
				AnimUtil.sendFlyMessage(message, context.getString(R.string.defense_increase_10per));
			}
		} else if (name.equals(context.getString(R.string.book_of_exp))) {
			if (gameLiveData.getEnergy() / 2 > 1000) {
				int energy = gameLiveData.getEnergy() / 2;
				gameLiveData.setEnergy(gameLiveData.getEnergy() - (energy / 1000) * 1000);
				gameLiveData.setExperience(gameLiveData.getExperience() + 10 * (energy / 1000));
				AnimUtil.sendFlyMessage(message, context.getString(R.string.info_conversion_ene2exp));
			} else if (gameLiveData.getEnergy() > 1000) {
				gameLiveData.setEnergy(gameLiveData.getEnergy() - 1000);
				gameLiveData.setExperience(gameLiveData.getExperience() + 10);
			} else
				AnimUtil.sendFlyMessage(message, context.getString(R.string.error_conversion_ene2exp));
		} else if (name.equals(context.getString(R.string.spring_shoes))) {
			switch (gameView.floor) {
				case 25:
					if (warrior.getX() >= 8) {
						warrior.setX(5);
						warrior.setY(5);
					} else {
						warrior.setX(8);
						warrior.setY(5);
					}
					break;
				default:
					AnimUtil.sendFlyMessage(message, context.getString(R.string.error_spring_shoe), Color.RED);
			}
		} else if (name.equals(context.getString(R.string.warrior_axe))) {
			if (UpdateJsonSub(context.getString(R.string.warrior_axe)))
				gameLiveData.setAttack((int) (gameLiveData.getAttack() * 1.2f));
			AnimUtil.sendFlyMessage(message, context.getString(R.string.info_warrior_ax_equip));
		}
//		Log.i(TAG, "The description for item iconClicked " + mAdapter.getItem(position) + " is : " + s);


	}

	private void sendScrollInfo(Context context) {
		String info = GameHelperUtil.getOldScrollInfo(context, gameView.floor);
		if (!info.equals("NONE")) AnimUtil.sendFlyMessage(message, info, Color.BLACK, 1);
		else AnimUtil.sendFlyMessage(message, context.getString(R.string.error_scroll));
	}

	private void CheckPickaxThrough(Context context, int x, int y) {
		if (gameView.layer00[y][x] >= 26 && gameView.layer00[y][x] <= 31) {
			AnimUtil.sendFlyMessage(message, context.getString(R.string.error_pickax));
			UpdateJsonReturn(context.getString(R.string.pickax));
		} else {
			gameView.layer00[y][x] = 41;
			gameView.update();
			AnimUtil.sendFlyMessage(message, context.getString(R.string.success_pickax));
		}
	}

	private void extinguishing(Context context) {
		int x = warrior.getX();
		int y = warrior.getY();
		if (x == 0 || y == 0 || x == gameView.xCount - 1 || y == gameView.yCount - 1) {
			AnimUtil.sendFlyMessage(message, context.getString(R.string.error_extinguishing));
			return;
		}
		if (gameView.layer00[y - 1][x - 1] == 290) gameView.layer00[y - 1][x - 1] = 362;
		if (gameView.layer00[y - 1][x] == 290) gameView.layer00[y - 1][x] = 362;
		if (gameView.layer00[y - 1][x + 1] == 290) gameView.layer00[y - 1][x + 1] = 362;
		if (gameView.layer00[y][x - 1] == 290) gameView.layer00[y][x - 1] = 362;
		if (gameView.layer00[y][x + 1] == 290) gameView.layer00[y][x + 1] = 362;
		if (gameView.layer00[y + 1][x - 1] == 290) gameView.layer00[y + 1][x - 1] = 362;
		if (gameView.layer00[y + 1][x] == 290) gameView.layer00[y + 1][x] = 362;
		if (gameView.layer00[y + 1][x + 1] == 290) gameView.layer00[y + 1][x + 1] = 362;
	}

	private void exploding(Context context) {

		int x = warrior.getX();
		int y = warrior.getY();
		int background = Door.getBackgroundCode(gameView.floor);
		if (x == 0 || y == 0 || x == gameView.xCount - 1 || y == gameView.yCount - 1) {
			AnimUtil.sendFlyMessage(message, context.getString(R.string.error_explosive));
			UpdateJsonReturn(context.getString(R.string.detonator));
			return;
		}
		if (wallList.contains(gameView.layer00[y - 1][x - 1]))
			gameView.layer00[y - 1][x - 1] = background;
		if (wallList.contains(gameView.layer00[y - 1][x])) gameView.layer00[y - 1][x] = background;
		if (wallList.contains(gameView.layer00[y - 1][x + 1]))
			gameView.layer00[y - 1][x + 1] = background;
		if (wallList.contains(gameView.layer00[y][x - 1])) gameView.layer00[y][x - 1] = background;
		if (wallList.contains(gameView.layer00[y][x + 1])) gameView.layer00[y][x + 1] = background;
		if (wallList.contains(gameView.layer00[y + 1][x - 1]))
			gameView.layer00[y + 1][x - 1] = background;
		if (wallList.contains(gameView.layer00[y + 1][x])) gameView.layer00[y + 1][x] = background;
		if (wallList.contains(gameView.layer00[y + 1][x + 1]))
			gameView.layer00[y + 1][x + 1] = background;

	}

	private void UpdateJsonReturn(String type) {//inquire the number of treasure, update the json, return if the number is larger than 0.
		int count = 0;
		JSONObject json = gameLiveData.getTreasureJson();
		if (!json.isNull(type)) {
			try {
				count = Integer.parseInt(json.getString(type));
				gameLiveData.setTreasureJson(gameLiveData.getTreasureJson().put(type, String.valueOf(count + 1)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean UpdateJsonSub(String type) {//inquire the number of treasure, update the json, return if the number is larger than 0.
		int count = 0;
		JSONObject json = gameLiveData.getTreasureJson();
		if (!json.isNull(type)) {
			try {
				count = Integer.parseInt(json.getString(type));
				if (count-- > 0)
					gameLiveData.setTreasureJson(gameLiveData.getTreasureJson().put(type, String.valueOf(count)));
				else
					gameLiveData.setTreasureJson(gameLiveData.getTreasureJson().put(type, String.valueOf(0)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return count >= 0;
		}
		return false;
	}

	private void TryFlyTo(Context context, int floor) {
		if (floor >= 40 || floor < 0) {
			AnimUtil.sendFlyMessage(message, context.getString(R.string.error_flywing));
			return;
		}
		int x, y;
		JSONObject jsonObject;
		try {
			jsonObject = flyWingData.getJSONObject(floor);
			if (jsonObject != null) {
				gameView.setFloor(floor);
				x = jsonObject.getInt(COLUMN_POSITION_X);
				y = jsonObject.getInt(COLUMN_POSITION_Y);
				warrior.setX(x);
				warrior.setY(y);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			AnimUtil.sendFlyMessage(message, "Cannot fly to an unexplored floor!");
		}
	}

	private String renewFragment(FragmentManager fragmentManager, String tag) {
		if (tag.equals("")) {
			for (Fragment fragment : fragmentManager.getFragments())
				fragmentManager.beginTransaction().remove(fragment).commit();
		} else {
			Fragment fragment = fragmentManager.findFragmentByTag(tag);
			if (fragment != null) {
				fragmentManager.beginTransaction().remove(fragment).commit();
			}
		}
		return tag;
	}

	@Override
	public void closeEnemyList() {
		renewFragment(fragmentManager, EvilTowerContract.FRAGMENT_TAG_ENEMY_LIST);
		gameView.handlingEnemyListWindow = false;
	}

	@Override
	public void CloseButtonClicked(String type) {
		renewFragment(fragmentManager, type);
		gameView.handlingNotePadWindow = false;
	}

}
