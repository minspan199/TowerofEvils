package com.michael.pan.eviltower.entities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.fragments.BattleFragment;
import com.michael.pan.eviltower.fragments.DialogViewFragment;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ENERGY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_NPC_TYPE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.TAG_ENEMY_BEFORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_CANCEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ENEMY_AFTER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_LOSE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_OK;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_WIN;

public class Enemy implements BattleFragment.OnBattleFinishedListener, DialogViewFragment.OnDialogViewClickedListener {

//	Green Slime（绿色史莱姆） 35 18 1 1
//	Red Slime（红色史莱姆） 45 20 2 2
//	Priest（初级法师） 60 32 8 5
//	Bat（小蝙蝠） 35 38 3 3
//	Skeleton C（骷髅人） 50 42 6 6
//	Big Slime（大史莱姆） 130 60 3 8
//	Skeleton B（骷髅士兵） 55 52 12 8
//	Gate-Keeper C（初级卫兵） 50 48 22 12
//	Skeleton A（骷髅队长） 100 65 15 30
//	Zombie （兽人） 260 85 5 18
//	Big Bat （大蝙蝠） 60 100 8 12
//	Superior Priest（高级法师） 100 95 30 22
//	Zombie Knight（兽人武士） 320 120 15 30
//	Slime Man（史莱姆人） 320 140 20 30
//	Rock （石头人） 20 100 68 28
//	Golden Knight （金骑士） 120 150 50 100
//	Giant Octopus （大乌贼）1200 180 20 100
//	Ghost Soldier （鬼战士）220 180 30 35
//	Soldier （战士）210 200 65 45
//	Vampire （吸血鬼）444 199 66 144
//	Gate-Keeper B （中级卫兵）100 180 110 100
//	SlimeLord （史莱姆王）360 310 20 40
//	Knight （骑士） 160 230 105 65
//	Vampire Bat（吸血蝙蝠） 200 390 90 50
//	Magician B （初级巫师）220 370 110 80
//	Magician A （高级巫师）200 380 130 90
//	The Magic Sergeant （魔法警卫）230 450 100 100
//	The Magic Sergeant Zeno （魔王齐诺）800(0) 500(0) 100(0) 500
//	Dark Knight （黑暗骑士） 180 430 210 120
//	Swordsman （双手剑士）100 680 50 55
//	Gate-Keeper A （高级卫兵）180 460 360 200
//	Dragon （魔龙）1500 600 250 800
//	Great Magic Master （大法师） 4500 560 310 1000
//	魔王 5000 1580 190 500

	public int type, floor;
	private FragmentManager fragmentManager;
	private Bundle extras;
	private EnemyEntry enemyEntry;

	public Enemy(Context context, Bundle extras, TextView popMessage, FragmentManager fragmentManager) {

		popMessage.setVisibility(View.GONE);
		type = extras.getInt(context.getString(R.string.matrix_value));
		floor = extras.getInt(context.getString(R.string.floor));
		this.fragmentManager = fragmentManager;
		this.extras = extras;
		enemyEntry = new EnemyEntry(context, type, floor);
		Fragment fragment;
		extras.putString(EXTRA_NPC_TYPE, TAG_ENEMY_BEFORE);
		if (enemyEntry.npc) {
			fragment = new DialogViewFragment(context, this);
			fragment.setArguments(extras);
		} else {
			fragment = new BattleFragment(this, enemyEntry);
			fragment.setArguments(extras);
		}
		fragmentManager.beginTransaction().replace(R.id.message_view_holder, fragment, EvilTowerContract.TAG_BATTLE_VIEW).commit();
	}

	@Override
	public void onBattleFinished(Context context, String s, Bundle bundle) {
		int xTouch = 0, yTouch = 0, type = 0;
		if (bundle.containsKey(EvilTowerContract.KEY_X_TOUCH)) {
			xTouch = bundle.getInt(EvilTowerContract.KEY_X_TOUCH);
		}
		if (bundle.containsKey(EvilTowerContract.KEY_Y_TOUCH)) {
			yTouch = bundle.getInt(EvilTowerContract.KEY_Y_TOUCH);
		}
		if (bundle.containsKey(EvilTowerContract.KEY_MATRIX_VALUE)) {
			type = bundle.getInt(EvilTowerContract.KEY_MATRIX_VALUE);
		}
		bundle.putString(EXTRA_NPC_TYPE, TAG_ENEMY_AFTER);

		if (s.equals(TAG_WIN)) {
			gameLiveData.setCoins(gameLiveData.getCoins() + enemyEntry.coin);// add coins/scores
			int exp = ((int) (enemyEntry.coin * 0.05f));
			gameLiveData.setExperience(gameLiveData.getExperience() + (exp == 0 ? 1 : exp));//add experience
			gameLiveData.setEnergy(bundle.getInt(COLUMN_ENERGY));
			if (type == -100) gameView.layer01[yTouch][xTouch] = 0;
			else gameView.layer02[yTouch][xTouch] = 0;//remove the enemy if win
			gameView.layerMerged = gameView.currentFloorData.getMergedLayer();
			refreshFragment(EvilTowerContract.TAG_BATTLE_VIEW);
			if (enemyEntry.npc) {
				Fragment fragment = new DialogViewFragment(context, this);
				fragment.setArguments(bundle);
				fragmentManager.beginTransaction().replace(R.id.message_view_holder, fragment, refreshFragment(EvilTowerContract.TAG_BATTLE_VIEW)).commit();
			}
		} else if (s.equals(TAG_LOSE)) {
			gameLiveData.setCoins((int) (gameLiveData.getCoins() * 0.9f));// add coins/scores
			gameLiveData.setEnergy((int) (gameLiveData.getEnergy() * 0.9f));
			refreshFragment(EvilTowerContract.TAG_BATTLE_VIEW);
		}
		gameView.handlingBattleWindow = false;
	}

	@Override
	public void onDialogViewClicked(Context context, String s) {
		Fragment fragment;
		switch (s) {
			case TAG_OK:
				fragment = new BattleFragment(this, enemyEntry);
				fragment.setArguments(extras);
				fragmentManager.beginTransaction().replace(R.id.message_view_holder, fragment, refreshFragment(EvilTowerContract.TAG_BATTLE_VIEW)).commit();
				break;
			case TAG_CANCEL:
				refreshFragment(EvilTowerContract.TAG_BATTLE_VIEW);
				break;
		}
		gameView.handlingDialogEvents = false;
	}

	private String refreshFragment(String tag) {
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		if (fragment != null) {
			fragmentManager.beginTransaction().remove(fragment).commit();
//			gameView.handlingEvents = false;
		}
		return tag;
	}

	public static class EnemyEntry {

		public int energy, attack, defense, coin, type;
		boolean npc = false;
		public boolean vampire = false;
		public String name;

		public EnemyEntry(Context context, int enemyType, int floor) {
			this.type = enemyType;
			switch (enemyType) {
				case -100:
					name = context.getString(R.string.wizard_blue_robe);
					energy = 10200;
					attack = 10000;
					defense = 7000;
					coin = 1000;
					vampire = true;
					break;
				case 102:
				case 103:
					name = context.getString(R.string.enemy_green_slime);
					energy = 30;
					attack = 18;
					defense = 1;
					coin = 1;
					break;
				case 104:
				case 105:
					name = context.getString(R.string.enemy_red_slime);
					energy = 45;
					attack = 20;
					defense = 2;
					coin = 2;
					break;
				case 106:
				case 107:
					name = context.getString(R.string.enemy_black_slime);
					energy = 60;
					attack = 22;
					defense = 3;
					coin = 3;
					break;
				//	SlimeLord （史莱姆王）360 310 20 40
				case 108:
				case 109:
					name = context.getString(R.string.enemy_slime_lord);
					energy = 360;
					attack = 310;
					defense = 20;
					coin = 40;
					break;
				case 110:
				case 111:
					name = context.getString(R.string.enemy_bat);
					energy = 35;
					attack = 38;
					defense = 3;
					coin = 3;
					break;
				case 112:
				case 113:
					name = context.getString(R.string.enemy_big_bat);
					energy = 60;
					attack = 100;
					defense = 8;
					coin = 2;
					break;
				case 114:
				case 115:
					name = context.getString(R.string.enemy_vampire_bat);
					energy = 200;
					attack = 390;
					defense = 90;
					coin = 50;
					vampire = true;
					break;
				case 116:
				case 117:
					if (floor == 42) {
						name = context.getString(R.string.enemy_evil_spirit_iii);
						energy = 16500;
						attack = (int) (gameLiveData.getDefense() * 1.25f);
						defense = (int) (gameLiveData.getAttack() * 0.84f);
						coin = 200;
						vampire = true;
					} else if (floor == 41) {
						name = context.getString(R.string.enemy_evil_spirit_iv);
						energy = 50000;
						attack = gameLiveData.getDefense() + 250;
						defense = 4300;
						coin = 300;
						vampire = true;
					} else if (floor >= 25) {
						name = context.getString(R.string.enemy_spirit_ii);
						energy = 2000;
						attack = 2200;
						defense = 560;
						coin = 100;
					} else {
						name = context.getString(R.string.enemy_spirit_i);
						energy = 500;
						attack = 1200;
						defense = 256;
						coin = 100;
						vampire = true;
					}
					break;
				case 118:
				case 119:
					name = context.getString(R.string.enemy_skeleton_c);
					energy = 50;
					attack = 42;
					defense = 6;
					coin = 6;
					break;
				case 120:
				case 121:
					name = context.getString(R.string.enemy_skeleton_b);
					energy = 55;
					attack = 52;
					defense = 12;
					coin = 8;
					break;
				case 122:
				case 123:
					name = context.getString(R.string.enemy_skeleton_a);
					energy = 100;
					attack = 65;
					defense = 15;
					coin = 30;
					break;
				//	Ghost Soldier （鬼战士）220 180 30 35
				case 124:
				case 125:
					if (floor <= 20) {
						name = context.getString(R.string.enemy_ghost_soldier);
						energy = 220;
						attack = 180;
						defense = 30;
						coin = 35;
					} else {
						name = context.getString(R.string.enemy_ghost_soldier_ii);
						energy = 2121;
						attack = 1885;
						defense = 986;
						coin = 80;
					}
					break;
				case 126:
				case 127:
					if (floor <= 22) {
						name = context.getString(R.string.enemy_zombie);
						energy = 260;
						attack = 85;
						defense = 5;
						coin = 18;
					} else {
						name = context.getString(R.string.enemy_zonbie_ii);
						energy = 2200;
						attack = 1800;
						defense = 300;
						coin = 100;
					}
					break;
				//	Zombie Knight（兽人武士） 320 120 15 30
				case 128:
				case 129:
					if (floor <= 22) {
						name = context.getString(R.string.enemy_zombie_knight);
						energy = 320;
						attack = 120;
						defense = 20;
						coin = 30;
					} else {
						name = context.getString(R.string.enemy_zombie_knight_ii);
						energy = 3200;
						attack = 2000;
						defense = 1000;
						coin = 90;
					}
					break;
				case 130:
				case 131:
					name = context.getString(R.string.enemy_rock);
					energy = (int) (gameLiveData.getEnergy() * 0.5f);
					attack = gameLiveData.getAttack();
					defense = (int) (gameLiveData.getDefense() * 0.8f);
					coin = 28;
					break;
				case 132:
				case 133:
					name = context.getString(R.string.enemy_ghost);
					energy = 320;
					attack = 140;
					defense = 20;
					coin = 30;
					break;
				case 134:
				case 135:
					name = context.getString(R.string.enemy_priest);
					energy = 60;
					attack = 32;
					defense = 8;
					coin = 5;
					break;
				case 136:
				case 137:
					name = context.getString(R.string.enemy_superior_priest);
					energy = 100;
					attack = 95;
					defense = 30;
					coin = 22;
					break;
				case 138:
				case 139:
					name = context.getString(R.string.enemy_magician_b);
					energy = 220;
					attack = 370;
					defense = 110;
					coin = 80;
					break;
				case 140:
				case 141:
					name = context.getString(R.string.enemy_magician_a);
					energy = 220;
					attack = 380;
					defense = 130;
					coin = 90;
					break;
				//	Gate-Keeper C（初级卫兵） 50 48 22 12
				case 142:
				case 143:
					name = context.getString(R.string.enemy_gate_keeper_c);
					energy = 50;
					attack = 48;
					defense = 22;
					coin = 12;
					break;
				//	Gate-Keeper B （中级卫兵）100 180 110 100
				case 144:
				case 145:
					name = context.getString(R.string.enemy_gate_keeper_b);
					energy = 100;
					attack = 180;
					defense = 110;
					coin = 100;
					break;
				//	Gate-Keeper A （高级卫兵）180 460 360 200
				case 146:
				case 147:
					name = context.getString(R.string.enemy_gate_keeper_a);
					energy = 180;
					attack = 460;
					defense = 360;
					coin = 200;
					break;
				//	Swordsman （双手剑士）100 680 50 55
				case 148:
				case 149:
					name = context.getString(R.string.enemy_swordsman);
					energy = 100;
					attack = 680;
					defense = 50;
					coin = 55;
					break;
				case 150:
				case 151:
					name = context.getString(R.string.enemy_soldier);
					energy = 210;
					attack = 200;
					defense = 65;
					coin = 45;
					break;
				//	Golden Knight （金骑士） 120 150 50 100
				case 152:
				case 153:
					name = context.getString(R.string.enemy_golden_knight);
					energy = 120;
					attack = 150;
					defense = 50;
					coin = 100;
					break;
				//	Knight （骑士） 160 230 105 65
				case 154:
				case 155:
					if (floor <= 22) {
						name = context.getString(R.string.enemy_knight);
						energy = 160;
						attack = 230;
						defense = 105;
						coin = 65;
					} else {
						name = context.getString(R.string.enemy_knight_ii);
						energy = 2120;
						attack = 2300;
						defense = 1005;
						coin = 135;
					}
					break;
				//	Dark Knight （黑暗骑士） 180 430 210 120
				case 156:
				case 157:
					name = context.getString(R.string.enemy_dark_knight);
					energy = 180;
					attack = 430;
					defense = 210;
					coin = 120;
					break;
				case 158:
					if (floor <= 20) {
						name = context.getString(R.string.enemy_black_wizard_i);
						energy = 300;
						attack = 600;
						defense = 600;
						coin = 100;
						break;
					} else if (floor <= 33) {
						name = context.getString(R.string.enemy_black_wizard_ii);
						energy = gameLiveData.getEnergy();
						attack = 6400;
						defense = 1600;
						coin = 120;
					} else {
						name = context.getString(R.string.enemy_black_wizard_iii);
						energy = (int) (gameLiveData.getEnergy() * 2f);
						attack = (gameLiveData.getDefense() * 1.1f) > 6500f ? (int) (gameLiveData.getDefense() * 1.1f) : 6500;
						defense = (int) (gameLiveData.getAttack() * 0.75f);
						coin = 250;
					}
					break;
				case 160:
					if (floor <= 20) {
						name = context.getString(R.string.enemy_yellow_wizard_i);
						energy = 800;
						attack = 700;
						defense = 700;
						coin = 123;
					} else {
						name = context.getString(R.string.enemy_yellow_wizard_ii);
						energy = 2400;
						attack = (int) (gameLiveData.getDefense() * 1.15f);
						defense = 1700;
						coin = 234;
					}
					break;
				case 162:
				case 163:
					if (floor <= 20) {
						name = context.getString(R.string.enemy_green_wizard_i);
						energy = 1200;
						attack = 1500;
						defense = 300;
						coin = 80;
					} else {
						name = context.getString(R.string.enemy_green_wizard_ii);
						energy = 5600;
						attack = (int) (gameLiveData.getDefense() * 1.1f);
						defense = 2120;
						coin = 120;
					}
					if (floor == 15) npc = true;
					break;
				//	The Magic Sergeant （魔法警卫）230 450 100 100
				case 164:
					name = context.getString(R.string.enemy_blue_knight);
					energy = 2000;
					attack = (int) (gameLiveData.getDefense() * 1.15f);
					defense = 555;
					coin = 115;
					break;
				case 166:
					if (floor < 21) {
						name = context.getString(R.string.enemy_red_eye_slime);
						energy = 1500;
						attack = 855;
						defense = 300;
						coin = 85;
					} else {
						name = context.getString(R.string.enemy_red_eye_slime_ii);
						energy = 3500;
						attack = (int) (gameLiveData.getDefense() * 1.2f);
						defense = 1300;
						coin = 155;
						vampire = true;
					}
					break;
				case 168:
					name = context.getString(R.string.enemy_pink_skeleton);
					energy = 1100;
					attack = (int) (gameLiveData.getDefense() * 1.1f);
					defense = 800;
					coin = 150;
					vampire = true;
					break;
				case 170:
				case 171:
					if (floor < 24) {
						name = context.getString(R.string.enemy_pink_bat);
						energy = 300;
						attack = 1000;
						defense = 400;
						coin = 100;
						vampire = true;
						break;
					} else if (floor < 40) {
						name = context.getString(R.string.enemy_pink_bat_ii);
						energy = 3000;
						attack = 2345;
						defense = 1001;
						coin = 150;
						vampire = true;
						break;
					} else {
						name = context.getString(R.string.enemy_pink_bat_iii);
						energy = 12000;
						attack = (int) (gameLiveData.getDefense() * 1.1f);
						defense = 3021;
						coin = 200;
						vampire = true;
					}
					break;
				case 172:
					name = context.getString(R.string.enemy_iron_man);
					energy = 2600;
					attack = gameLiveData.getAttack() + 100;
					defense = (int) (gameLiveData.getDefense() * 0.5f);
					coin = 100;
					break;
				case 174:
					name = context.getString(R.string.enemy_zombie_skeleton);
					energy = 4000;
					attack = 4000;
					defense = 1000;
					coin = 100;
					break;
				case 176:
					if (floor < 25) {
						name = context.getString(R.string.enemy_marshall_i);
						energy = 5000;
						attack = 2000;
						defense = 1200;
						coin = 500;
						break;
					} else if (floor <= 36) {
						name = context.getString(R.string.enemy_marshall_ii);
						energy = 8000;
						attack = 3800;
						defense = 1500;
						coin = 500;
						vampire = true;
						break;
					} else {
						name = context.getString(R.string.enemy_marshall_iii);
						energy = (int) (gameLiveData.getEnergy() * 1.7f);
						attack = (int) (gameLiveData.getDefense() * 1.2f);
						defense = (int) (gameLiveData.getAttack() * 0.5f);
						coin = 500;
						break;
					}
				case 178:
					name = context.getString(R.string.enemy_beast_guard);
					energy = 1000;
					attack = 6000;
					defense = 3000;
					coin = 50;
					break;
				case 180:
					name = context.getString(R.string.enemy_butcher);
					energy = 8720;
					attack = 5600;
					defense = 2100;
					coin = 165;
					break;
				case 182:
					name = context.getString(R.string.enemy_evil_head);
					energy = 50000;
					attack = (int) (gameLiveData.getDefense() * 1.5f);
					defense = (int) (gameLiveData.getAttack() * 0.8f);
					coin = 10000;
					npc = true;
					vampire = true;
					break;
				case 184:
					if (floor < 40) {
						name = context.getString(R.string.enemy_robber);
						energy = 4000;
						attack = 3500;
						defense = 4000;
						coin = 100;
					} else {
						name = context.getString(R.string.enemy_robber);
						energy = (int) (gameLiveData.getEnergy() * 1.2f);
						attack = (int) (gameLiveData.getDefense() * 1.2f);
						defense = (int) (gameLiveData.getAttack() * 0.6f);
						coin = 300;
					}
					break;
				case 186:
					name = context.getString(R.string.enemy_monkey_king);
					energy = (int) (floor * 200f);
					attack = (int) (floor * 10f + gameLiveData.getDefense() * 1.2f);
					defense = (int) (gameLiveData.getAttack() * 0.75f);
					coin = 800;
					break;
				case 188:
					name = context.getString(R.string.enemy_wild_cat);
					energy = 20000;
					attack = 7000;
					defense = 5000;
					coin = 150;
					break;
				case 190:
					name = context.getString(R.string.enemy_red_wizard);
					energy = 10000;
					attack = (int) (gameLiveData.getAttack() * 1.1f);
					defense = (int) (gameLiveData.getDefense() * 0.5f);
					coin = 160;
					break;
				case 192:
				case 193:
					name = context.getString(R.string.enemy_magic_sergeant);
					energy = 230;
					attack = 450;
					defense = 100;
					coin = 100;
					break;
				case 194:
				case 195:
					if (floor < 39) {
						name = context.getString(R.string.enemy_archmage);
						energy = 4500;
						attack = 560;
						defense = 310;
						coin = 1000;
						vampire = true;
					} else {
						name = context.getString(R.string.enemy_archmage_i);
						energy = 12500;
						attack = (int) (gameLiveData.getDefense() * 1.23f);
						defense = (int) (gameLiveData.getAttack() * 0.82f);
						coin = 1000;
						vampire = true;
					}
					break;
				case 196:
					name = context.getString(R.string.enemy_grey_slime);
					energy = 3000;
					attack = 3000;
					defense = 3560;
					coin = 123;
					break;
				case 198:
				case 199:
					if (floor > 40) {
						name = context.getString(R.string.enemy_blood_knight);
						energy = 6000;
						attack = (int) (gameLiveData.getDefense() * (1 + floor / 300f));
						defense = 2000;
						coin = 300;
						vampire = true;
					} else {
						name = context.getString(R.string.enemy_blood_knight_ii);
						energy = gameLiveData.getEnergy();
						attack = (int) (gameLiveData.getDefense() * (1.2f));
						defense = (int) (gameLiveData.getAttack() * 0.8f);
						coin = 300;
						vampire = true;
					}
					if (floor == 29) npc = true;
					break;
				case 200:
					if (floor < 40) {
						name = context.getString(R.string.enemy_skeleton_monster);
						energy = 5000;
						attack = 3500;
						defense = 4000;
						coin = 234;
					} else {
						name = context.getString(R.string.enemy_skeleton_master);
						energy = 12345;
						attack = (int) (gameLiveData.getDefense() * 1.2f);
						defense = (int) (gameLiveData.getAttack() * 0.75f);
						coin = 345;
					}
					break;
				case 202:
					name = context.getString(R.string.enemy_fake_princess);
					energy = 8000;
					attack = 4000;
					defense = 3000;
					coin = 300;
					if (floor == 37) npc = true;
					vampire = true;
					break;
				case 204:
					name = context.getString(R.string.enemy_bad_angels);
					energy = 4500;
					attack = (gameLiveData.getDefense() * 1.1f) > 6500f ? (int) (gameLiveData.getDefense() * 1.1f) : 6500;
					defense = (int) (gameLiveData.getAttack() * 0.75f);
					coin = 200;
					break;
				case 206:
					name = context.getString(R.string.enemy_ghost_wizard);
					energy = 5800;
					attack = 2500;
					defense = 500;
					coin = 100;
					break;
				case 208:
					name = context.getString(R.string.enemy_warrior_ninja);
					energy = 2000;
					attack = (int) (2210 * (1 + floor * 0.015f));
					defense = 1220;
					coin = 80;
					break;
				case 210:
					name = context.getString(R.string.enemy_invisible_man);
					energy = 6500;
					attack = gameLiveData.getEnergy();
					defense = gameLiveData.getExperience();
					coin = 200;
					vampire = true;
					break;
				case 212:
					if (floor < 40) {
						name = context.getString(R.string.enemy_green_zombie);
						energy = 2000;
						attack = 3400;
						defense = 2000;
					} else {
						name = context.getString(R.string.enemy_green_zombie_ii);
						energy = 20000;
						attack = (int) (gameLiveData.getDefense() * 1.22f);
						defense = (int) (gameLiveData.getAttack() * 0.78f);
					}
					coin = 1;
					break;
				case 214:
					name = context.getString(R.string.enemy_red_dragon);
					energy = 10000;
					attack = 6000;
					defense = 2200;
					npc = true;
					vampire = true;
					break;
				case 216:
					name = context.getString(R.string.enemy_giant_spider);
					energy = 20000;
					int att = 100 * Math.abs(gameLiveData.getDefense() - gameLiveData.getAttack());
					attack = att > 7600 ? att : 7600;
					defense = (int) (gameLiveData.getAttack() * 0.8f);
					vampire = true;
					npc = true;
					break;
				case 220:
					if (floor >= 40) {
						name = context.getString(R.string.enemy_green_soldier_iii);
						energy = (int) (gameLiveData.getEnergy() * 2f);
						defense = (int) (gameLiveData.getAttack() * 0.5f);
						attack = (int) (gameLiveData.getDefense() * 1.2f);
						vampire = false;
					} else if (floor <= 30) {
						name = context.getString(R.string.enemy_green_soldier_i);
						energy = 1000;
						defense = 2000;
						attack = 2000;
					} else {
						name = context.getString(R.string.enemy_green_soldier_ii);
						energy = gameLiveData.getEnergy();
						defense = (int) (gameLiveData.getDefense() * 0.7f);
						attack = (int) (gameLiveData.getAttack() * 1.1f);
						vampire = true;
					}
					coin = 200;
					break;
				default:
					name = context.getString(R.string.enemgy_unknown);
					energy = 1000;
					attack = (int) (gameLiveData.getDefense() * 1.1f);
					defense = (int) (gameLiveData.getAttack() * 0.2f);
					coin = 10;
			}
		}
	}
}
