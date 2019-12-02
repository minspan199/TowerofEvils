package com.michael.pan.eviltower.utilities;

import android.content.Context;

import com.michael.pan.eviltower.R;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.TAG_ENEMY_BEFORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ANGEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ANGEL_VISITED;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ENEMY_AFTER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_EXPERIENCE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_EXPERIENCE_VISITED;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GIANT_SPIDER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_KEY_MERCHANT;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_KEY_MERCHANT_VISITED;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_PRISONER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_REAL_PRINCESS;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_RED_DRAGON;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_SECRET_MERCHANT;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_TECHNICIAN;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_TORCH_LIGHT;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_WIZARD_BLUE;

public class GameHelperUtil {

	public static String getHelpStrings(Context context, String name, String value) {
		String s;

		if (context.getString(R.string.coins).equals(name)) {
			s = context.getString(R.string.coins_content) + value;
		} else if (context.getString(R.string.r_key).equals(name)) {
			s = context.getString(R.string.red_key_content1) + value + context.getString(R.string.red_key_content2);
		} else if (context.getString(R.string.b_key).equals(name)) {
			s = context.getString(R.string.blue_key_content1) + value + context.getString(R.string.blue_key_content2);
		} else if (context.getString(R.string.y_key).equals(name)) {
			s = context.getString(R.string.yellow_key_content1) + value + context.getString(R.string.yellow_key_content2);
		} else if (context.getString(R.string.r_diamond).equals(name)) {
			s = context.getString(R.string.red_diamond_content1) + value + context.getString(R.string.red_diamond_content2);
		} else if (context.getString(R.string.b_diamond).equals(name)) {
			s = context.getString(R.string.blue_diamond_content1) + value + context.getString(R.string.blue_diamond_content2);
		} else if (context.getString(R.string.g_diamond).equals(name)) {
			s = value + context.getString(R.string.green_diamond_content2);
		} else if (context.getString(R.string.y_diamond).equals(name)) {
			s = value + context.getString(R.string.yellow_diamond_content2);
		} else if (context.getString(R.string.key_box).equals(name)) {
			s = value + " " + context.getString(R.string.key_box_content2);
		} else if (context.getString(R.string.book_of_wisdom).equals(name)) {
			s = context.getString(R.string.book_of_wisdom_content2);
		} else if (context.getString(R.string.pot_of_wisdom).equals(name)) {
			s = value + " " + context.getString(R.string.pot_of_wisdom_content2);
		} else if (context.getString(R.string.large_pot_of_wisdom).equals(name)) {
			s = value + " " + context.getString(R.string.large_pot_of_wisdom_content2);
		} else if (context.getString(R.string.smile_coin).equals(name)) {
			s = value + " " + context.getString(R.string.smile_coin_content2);
		} else if (context.getString(R.string.fly_wing).equals(name)) {
			s = context.getString(R.string.fly_wing_content);
		} else if (context.getString(R.string.large_smile_coin).equals(name)) {
			s = value + " " + context.getString(R.string.large_smiley_coin_content2);
		} else if (context.getString(R.string.cross).equals(name)) {
			s = context.getString(R.string.info_cross);
		} else if (context.getString(R.string.blessing_gift).equals(name)) {
			s = context.getString(R.string.intro_new_year_blessing);
		} else if (context.getString(R.string.wooden_sword).equals(name)) {
			s = context.getString(R.string.intro_bokken);
		} else if (context.getString(R.string.iron_sword).equals(name)) {
			s = context.getString(R.string.info_get_iron_sword);
		} else if (context.getString(R.string.large_wooden_sword).equals(name)) {
			s = context.getString(R.string.info_get_large_wooden_sword);
		} else if (context.getString(R.string.large_iron_sword).equals(name)) {
			s = context.getString(R.string.info_get_large_iron_sword);
		} else if (context.getString(R.string.chaos_sword).equals(name)) {
			s = context.getString(R.string.info_get_chaos_sword);
		} else if (context.getString(R.string.skin_shield).equals(name)) {
			s = context.getString(R.string.info_get_skin_shield);
		} else if (context.getString(R.string.iron_shield).equals(name)) {
			s = context.getString(R.string.info_get_iron_shield);
		} else if (context.getString(R.string.diamond_shield).equals(name)) {
			s = context.getString(R.string.info_get_diamond_shield);
		} else if (context.getString(R.string.divine_shield).equals(name)) {
			s = context.getString(R.string.info_get_divine_shield);
		} else if (context.getString(R.string.cyan_attack_pot).equals(name)){
			s = context.getString(R.string.info_attack_pot);
		} else if (context.getString(R.string.blue_defense_pot).equals(name)){
			s = context.getString(R.string.info_defense_pot);
		} else if (context.getString(R.string.pink_energy_pot).equals(name)){
			s = context.getString(R.string.info_p_pot_energy);
		} else if (context.getString(R.string.green_pot).equals(name)){
			s = context.getString(R.string.info_g_pot_exp);
		} else if (context.getString(R.string.pickax).equals(name)){
			s = context.getString(R.string.intro_pickax);
		} else if (context.getString(R.string.r_basement_key).equals(name)){
			s = context.getString(R.string.info_r_basement_key);
		} else if (context.getString(R.string.y_basement_key).equals(name)){
			s = context.getString(R.string.info_y_basement_key);
		} else if (context.getString(R.string.b_basement_key).equals(name)){
			s = context.getString(R.string.info_b_basement_key);
		} else if (context.getString(R.string.w_basement_key).equals(name)){
			s = context.getString(R.string.info_w_basement_key);
		} else if (context.getString(R.string.g_basement_key).equals(name)){
			s = context.getString(R.string.info_g_basement_key);
		} else if (context.getString(R.string.ice_plate).equals(name)){
			s = context.getString(R.string.info_ice_plate);
		} else if (context.getString(R.string.upstairs_wing).equals(name)){
			s = context.getString(R.string.info_upstairs_wing);
		} else if (context.getString(R.string.downstairs_wing).equals(name)){
			s = context.getString(R.string.info_downstairs_wing);
		} else if (context.getString(R.string.old_scroll).equals(name)){
			s = context.getString(R.string.info_old_scroll);
		} else if (context.getString(R.string.notebook).equals(name)){
			s = context.getString(R.string.info_get_notebook);
		} else if (context.getString(R.string.detonator).equals(name)){
			s = context.getString(R.string.info_get_detonator);
		} else if (context.getString(R.string.book_of_exp).equals(name)){
			s = context.getString(R.string.info_book_exp);
		} else if (context.getString(R.string.spring_shoes).equals(name)){
			s = context.getString(R.string.info_spring_shoe);
		} else if (context.getString(R.string.warrior_axe).equals(name)){
			s = context.getString(R.string.info_warrior_axe);
		}
		else {
			s = context.getString(R.string.unknow_object);
		}
//		System.out.println(s);
		return s;
	}

	public static String getNPCName(Context context, String npcType){
		switch (npcType){
			case TAG_ANGEL:
			case TAG_ANGEL_VISITED:
				return context.getString(R.string.angel_diag);
			case TAG_GIANT_SPIDER:
				return context.getString(R.string.enemy_giant_spider_diag);
			case TAG_EXPERIENCE:
			case TAG_EXPERIENCE_VISITED:
				return context.getString(R.string.experience_diag);
			case TAG_KEY_MERCHANT:
			case TAG_KEY_MERCHANT_VISITED:
				return context.getString(R.string.merchant_diag);
			case TAG_REAL_PRINCESS:
				return context.getString(R.string.princess_diag);
			case TAG_PRISONER:
				return context.getString(R.string.prisoner_diag);
			case TAG_RED_DRAGON:
				return context.getString(R.string.red_dragon_diag);
			case TAG_ENEMY_BEFORE:
			case TAG_ENEMY_AFTER:
				return context.getString(R.string.boss_diag);
			case TAG_SECRET_MERCHANT:
				return context.getString(R.string.secret_merchant_diag);
			case TAG_TECHNICIAN:
				return context.getString(R.string.technician_diag);
			case TAG_WIZARD_BLUE:
				return context.getString(R.string.wizard_blue_robe_diag);
			case TAG_TORCH_LIGHT:
				return context.getString(R.string.torch_diag);
			default:
				return context.getString(R.string.npc_diag);
		}
	}

	public static String[] getDialogContent(Context context, String npcType, int floor) {

		switch (floor) {
			case 0:
				switch (npcType) {
					case TAG_ANGEL:
						return new String[]{
							context.getString(R.string.angel_floor0_content1),
							context.getString(R.string.angel_floor0_content2),
							context.getString(R.string.angel_floor0_content3),
							context.getString(R.string.angel_floor0_content4),
							context.getString(R.string.good_luck)
						};
					case TAG_EXPERIENCE:
						return new String[]{
							context.getString(R.string.experience_floor0_content1),
							context.getString(R.string.experience_floor0_content2),
							context.getString(R.string.experience_floor0_content3),
							context.getString(R.string.good_luck)
						};
					case TAG_KEY_MERCHANT:
						return new String[]{
							context.getString(R.string.key_merchant_floor0_content)
						};
					case TAG_WIZARD_BLUE:
						return new String[]{
							context.getString(R.string.blue_wizard_floor0_content)
						};
					case TAG_REAL_PRINCESS:
						return new String[]{
							context.getString(R.string.rp_0),
							context.getString(R.string.rp_1)
						};
					case TAG_TECHNICIAN:
						return new String[]{
							context.getString(R.string.tech_0_1)
						};
					default:
						return new String[]{context.getString(R.string.good_luck)
						};
				}
			case 1:
				if (npcType.equals(TAG_TECHNICIAN)) return new String[]{
					context.getString(R.string.techinician_1)
				};
				break;
			case 3:
				switch (npcType){
					case TAG_TECHNICIAN: return new String[]{
						context.getString(R.string.tech_3_1),
						context.getString(R.string.tech_3_2)
					};
					case TAG_ANGEL:
						return new String[]{
							context.getString(R.string.angel_3_1),
							context.getString(R.string.angel_3_2)
						};
					case TAG_EXPERIENCE:
						return new String[]{
							context.getString(R.string.exp_3_1),
							context.getString(R.string.exp_3_2)
						};
					default:
						break;
				}
				break;
			case 2:
			case 6:
				switch (npcType) {
					case TAG_TECHNICIAN:
						return new String[]{
							context.getString(R.string.technician_6)
						};
					case TAG_KEY_MERCHANT:
						return new String[]{
							context.getString(R.string.key_merchant_6)
						};
					case TAG_EXPERIENCE:
						return new String[]{
							context.getString(R.string.experience_6)
						};
				}
				break;
			case 9:
				switch (npcType){
					case TAG_ANGEL:
						return new String[]{
							context.getString(R.string.angel_9_1),
							context.getString(R.string.angel_9_2)
						};
					case TAG_TECHNICIAN:
						return new String[]{
							context.getString(R.string.technician_9)
						};
				}
				break;
			case 10:
				switch (npcType) {
					case TAG_EXPERIENCE:
						return new String[]{
							context.getString(R.string.experience_10)
						};
					case TAG_KEY_MERCHANT:
						return new String[]{
							context.getString(R.string.key_merchant_10)
						};
				}
				break;
			case 11:
				if (npcType.equals(TAG_ANGEL)) return new String[]{
					context.getString(R.string.angel_11_1),
					context.getString(R.string.angel_11_2),
					context.getString(R.string.angel_11_3),
					context.getString(R.string.angel_11_4)
				};
				break;
			case 12:
				if (npcType.equals(TAG_PRISONER)) return new String[]{
					context.getString(R.string.prisoner_12_1),
					context.getString(R.string.prisoner_12_2),
					context.getString(R.string.prisoner_12_3),
					context.getString(R.string.prisoner_12_4),
					context.getString(R.string.prisoner_12_5),
					context.getString(R.string.prisoner_12_6),
					context.getString(R.string.prisoner_12_7)
				};
				break;
			case 15:
				switch (npcType){
					case TAG_ENEMY_AFTER: return new String[]{
						context.getString(R.string.enemy_floor15_content)
					};
					case TAG_ENEMY_BEFORE: return new String[]{
						context.getString(R.string.enemy_15_1)
					};
				}
				break;
			case 16:
				if (npcType.equals(TAG_ANGEL)) return new String[]{
					context.getString(R.string.angel_16_1),
					context.getString(R.string.angel_16_2)
				};
				break;
			case 19:
				if (npcType.equals(TAG_TECHNICIAN)) return new String[]{
					context.getString(R.string.technician_19_1),
					context.getString(R.string.technician_19_2)
				};
				break;
			case 22:
				if (npcType.equals(TAG_TECHNICIAN)) return new String[]{
					context.getString(R.string.technician_21)
				};
				break;
			case 26:
				if (npcType.equals(TAG_SECRET_MERCHANT)) return new String[]{
					context.getString(R.string.sec_merchant_26_1)
				};else if (npcType.equals(TAG_PRISONER)) return new String[]{
					context.getString(R.string.prisoner_26_1)
				};
				break;
			case 28:
				switch (npcType) {
					case TAG_EXPERIENCE:
						return new String[]{
							context.getString(R.string.experience_7_500)
						};
					case TAG_KEY_MERCHANT:
						return new String[]{
							context.getString(R.string.key_merchant_10)
						};
				}
				break;
			case 29:
				if (npcType.equals(TAG_ENEMY_BEFORE)) return new String[]{
					context.getString(R.string.eb_29_1),
					context.getString(R.string.eb_29_2)
				};
				if (npcType.equals(TAG_ENEMY_AFTER)) return new String[]{
					context.getString(R.string.ef_29_1)
				};
				break;
			case 32:
				switch (npcType) {
					case TAG_RED_DRAGON:
						return new String[]{
							context.getString(R.string.red_32_1)
						};
					case TAG_ENEMY_BEFORE:
						return new String[]{
							context.getString(R.string.eb_32_1)
						};
					case TAG_ENEMY_AFTER:
						return new String[]{
							context.getString(R.string.ea_32_1)
						};
					case TAG_ANGEL:
						return new String[]{
							context.getString(R.string.angel_32_1),
							context.getString(R.string.angel_32_2)
						};
				}
				break;
			case 34:
				if (TAG_TECHNICIAN.equals(npcType)) {
					return new String[]{
						context.getString(R.string.technician_34_1),
						context.getString(R.string.technician_34_2),
						context.getString(R.string.technician_34_3)
					};
				}
				break;
			case 35:
				switch (npcType){
					case TAG_EXPERIENCE:
						return new String[]{
							context.getString(R.string.experience_7_500)
						};
					case TAG_KEY_MERCHANT:
						return new String[]{
							context.getString(R.string.buy_r_key_4000)
						};
				}
				break;
			case 36:
				if (TAG_ANGEL.equals(npcType)){
					return new String[]{
						context.getString(R.string.angel_36_1)
					};
				}
				break;
			case 37:
				switch (npcType) {
					case TAG_ENEMY_BEFORE:
						return new String[]{
							context.getString(R.string.eb_37_1),
							context.getString(R.string.eb_37_2),
							context.getString(R.string.eb_37_3),
							context.getString(R.string.eb_37_4),
							context.getString(R.string.eb_37_5),
							context.getString(R.string.eb_37_6),
							context.getString(R.string.eb_37_7),
							context.getString(R.string.eb_37_8),
							context.getString(R.string.eb_37_9)
						};
					case TAG_ENEMY_AFTER:
						return new String[]{
							context.getString(R.string.ea_37_1),
							context.getString(R.string.ea_37_2),
							context.getString(R.string.ea_37_3),
							context.getString(R.string.ea_37_4),
							context.getString(R.string.ea_37_5),
							context.getString(R.string.ea_37_6)
						};
				}
				break;
			case 38:
				if (TAG_ANGEL.equals(npcType)) return new String[]{
					context.getString(R.string.abgel_38_1)
				};
				break;
			case 39:
				switch (npcType){
					case TAG_EXPERIENCE:
						return new String[]{
							context.getString(R.string.experience_10_700)
						};
					case TAG_KEY_MERCHANT:
						return new String[]{
							context.getString(R.string.key_merchant_39)
						};
				}
				break;
			case 40:
				switch (npcType) {
					case TAG_GIANT_SPIDER:
						return new String[]{
							context.getString(R.string.spider_40_1)
						};
					case TAG_ENEMY_BEFORE:
						return new String[]{
							context.getString(R.string.eb_40_1),
							context.getString(R.string.eb_40_2),
							context.getString(R.string.eb_40_3),
							context.getString(R.string.eb_40_4)
						};
					case TAG_ENEMY_AFTER:
						return new String[]{
							context.getString(R.string.ef_40_1),
							context.getString(R.string.ef_40_2)
						};
				}
				break;
			case 41:
				switch (npcType){
					case TAG_ENEMY_BEFORE:
						return new String[]{
							context.getString(R.string.eb_41_1),
							context.getString(R.string.eb_41_2),
							context.getString(R.string.eb_41_3),
							context.getString(R.string.eb_41_4)
						};
					case TAG_ENEMY_AFTER:
						return new String[]{
							context.getString(R.string.eb_41_5),
							context.getString(R.string.eb_41_6)
						};
					case TAG_REAL_PRINCESS:
						return new String[]{
							context.getString(R.string.rp_41_1),
							context.getString(R.string.rp_41_2),
							context.getString(R.string.rp_41_3)
						};
				}
				break;
			case 42:
				switch (npcType){
					case TAG_KEY_MERCHANT:
						return new String[]{
							context.getString(R.string.key_merchant_key_box_42_1),
							context.getString(R.string.key_merchant_key_box_42_2)
						};
					case TAG_EXPERIENCE:
						return new String[]{
							context.getString(R.string.experience_10_700)
						};
				}
		}
		return new String[]{context.getString(R.string.good_luck)};
	}

	public static String getOldScrollInfo(Context context, int floor) {
		switch (floor){
			case 0:
				return context.getString(R.string.scroll_info_0);
			case 12:
				return context.getString(R.string.scroll_info_12);
			case 19:
				return context.getString(R.string.scroll_info_19);
			case 20:
				return context.getString(R.string.scroll_info_20);
			case 21:
				return context.getString(R.string.sroll_info_21);
			case 25:
				return context.getString(R.string.scroll_info_25);
			case 27:
				return context.getString(R.string.scroll_info_27);
			case 37:
				return context.getString(R.string.scroll_info_37);
			case 40:
				return context.getString(R.string.scroll_info_40);
			case 41:
				return context.getString(R.string.scroll_info_41);
		}
		return "NONE";
	}
}
