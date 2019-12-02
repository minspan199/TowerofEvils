package com.michael.pan.eviltower.entities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.utilities.AnimUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;

public class Treasure {

	private String type;

	public Treasure(Context context, TextView textField, Bundle extras){

//		gameView.handlingTreasureEvents = true;
		textField.setVisibility(View.VISIBLE);
		int xTouch = extras.getInt(context.getString(R.string.xTouch));
		int yTouch = extras.getInt(context.getString(R.string.yTouch));
		int matrixValue = extras.getInt(context.getString(R.string.matrix_value));
		gameView.layer03[yTouch][xTouch] = 0;
		gameView.update();
		switch (matrixValue) {
			case 223:
				type = context.getString(R.string.b_diamond);
				gameLiveData.setDefense(gameLiveData.getDefense() + 3);
				break;
			case 224:
				type = context.getString(R.string.g_diamond);
				gameLiveData.setDefense(gameLiveData.getDefense() + 5);
				break;
			case 225:
				type = context.getString(R.string.y_diamond);
				gameLiveData.setAttack(gameLiveData.getAttack() + 5);
				break;
			case 226:
				type = context.getString(R.string.r_energy);
				gameLiveData.setEnergy(gameLiveData.getEnergy() + 200);
				break;
			case 227:
				type = context.getString(R.string.b_energy);
				gameLiveData.setEnergy(gameLiveData.getEnergy() + 500);
				break;
			case 228:
				type = context.getString(R.string.g_energy);
				gameLiveData.setEnergy(gameLiveData.getEnergy() + 1000);
				break;
			case 229:
				type = context.getString(R.string.y_energy);
				gameLiveData.setEnergy(gameLiveData.getEnergy() + 2000);
				break;
			case 230:
				type = context.getString(R.string.green_pot);
				UpdateTreasureJson();
				break;
			case 231:
				type = context.getString(R.string.pink_energy_pot);
				UpdateTreasureJson();
				break;
			case 232:
				type = context.getString(R.string.blue_defense_pot);
				UpdateTreasureJson();
				break;
			case 233:
				type = context.getString(R.string.cyan_attack_pot);
				UpdateTreasureJson();
				break;
			case 235:
				type = context.getString(R.string.r_diamond);
				gameLiveData.setAttack(gameLiveData.getAttack() + 3);
				break;
			case 238:
				type = context.getString(R.string.y_key);
				gameLiveData.setyKey(gameLiveData.getyKey() + 1);
				break;
			case 239:
				type = context.getString(R.string.b_key);
				gameLiveData.setbKey(gameLiveData.getbKey() + 1);
				break;
			case 240:
				type = context.getString(R.string.r_key);
				gameLiveData.setrKey(gameLiveData.getrKey() + 1);
				break;
			case 241:
				type = context.getString(R.string.g_key);
				gameLiveData.setgKey(gameLiveData.getgKey() + 1);
				UpdateTreasureJson();
				break;
			case 242:
				type = context.getString(R.string.g_key_box);
				UpdateTreasureJson();
				break;
			case 243:
				type = context.getString(R.string.b_key_box);
				UpdateTreasureJson();
				break;
			case 244:
				type = context.getString(R.string.y_key_box);
				UpdateTreasureJson();
				break;
			case 245:
				type = context.getString(R.string.w_basement_key);
				UpdateTreasureJson();
				break;
			case 246:
				type = context.getString(R.string.y_basement_key);
				UpdateTreasureJson();
				break;
			case 247:
				type = context.getString(R.string.b_basement_key);
				UpdateTreasureJson();
				break;
			case 248:
				type = context.getString(R.string.r_basement_key);
				UpdateTreasureJson();
				break;
			case 249:
				type = context.getString(R.string.g_basement_key);
				UpdateTreasureJson();
				break;
			case 250:
				type = context.getString(R.string.book_of_wisdom);
				UpdateTreasureJson();
				break;
			case 251:
				type = context.getString(R.string.notebook);
				UpdateTreasureJson();
				break;
			case 253:
				type = context.getString(R.string.smile_coin);
				UpdateTreasureJson();
				break;
			case 254:
			case 255:
				type = context.getString(R.string.pickax);
				UpdateTreasureJson();
				break;
			case 256:
				type = context.getString(R.string.ice_plate);
				UpdateTreasureJson();
				break;
			case 257:
				type = context.getString(R.string.old_scroll);
				UpdateTreasureJson();
				break;
			case 258:
				type = context.getString(R.string.fly_wing);
				UpdateTreasureJson();
				break;
			case 259:
				type = context.getString(R.string.downstairs_wing);
				UpdateTreasureJson();
				break;
			case 260:
				type = context.getString(R.string.upstairs_wing);
				UpdateTreasureJson();
				break;
			case 261:
				type = context.getString(R.string.cross);
				UpdateTreasureJson();
				break;
			case 262:
				type = context.getString(R.string.detonator);
				UpdateTreasureJson();
				break;
			case 264:
				type = context.getString(R.string.warrior_axe);
				UpdateTreasureJson();
				break;
			case 266:
				type = context.getString(R.string.spring_shoes);
				UpdateTreasureJson();
				break;
			case 267:
				type = context.getString(R.string.blessing_gift);
				UpdateTreasureJson();
				break;
			case 268:
				type = context.getString(R.string.book_of_exp);
				UpdateTreasureJson();
				break;
			case 270:
				type = context.getString(R.string.wooden_sword);
				gameLiveData.setAttack(gameLiveData.getAttack() + 20);
				UpdateTreasureJson();
				break;
			case 271:
				type = context.getString(R.string.iron_sword);
				gameLiveData.setAttack(gameLiveData.getAttack() + 40);
				UpdateTreasureJson();
				break;
			case 272:
				type = context.getString(R.string.large_wooden_sword);
				gameLiveData.setAttack(gameLiveData.getAttack() + 70);
				UpdateTreasureJson();
				break;
			case 273:
				type = context.getString(R.string.large_iron_sword);
				gameLiveData.setAttack(gameLiveData.getAttack() + 100);
				UpdateTreasureJson();
				break;
			case 274:
				type = context.getString(R.string.chaos_sword);
				gameLiveData.setAttack(gameLiveData.getAttack() + 200);
				UpdateTreasureJson();
				break;
			case 278:
				type = context.getString(R.string.skin_shield);
				gameLiveData.setDefense(gameLiveData.getDefense() + 20);
				UpdateTreasureJson();
				break;
			case 279:
				type = context.getString(R.string.wooden_shield);
				gameLiveData.setDefense(gameLiveData.getDefense() + 40);
				UpdateTreasureJson();
				break;
			case 280:
				type = context.getString(R.string.iron_shield);
				gameLiveData.setDefense(gameLiveData.getDefense() + 70);
				UpdateTreasureJson();
				break;
			case 281:
				type = context.getString(R.string.diamond_shield);
				gameLiveData.setDefense(gameLiveData.getDefense() + 100);
				UpdateTreasureJson();
				break;
			case 282:
				type = context.getString(R.string.divine_shield);
				gameLiveData.setDefense(gameLiveData.getDefense() + 200);
				UpdateTreasureJson();
				break;
		}
		AnimUtil.sendFlyMessage(textField, showHelp(context));
	}

	private void UpdateTreasureJson(){
		int count = 0;
		JSONObject json = gameLiveData.getTreasureJson();
		if (!json.isNull(type)) {
			try {
			count = Integer.parseInt(json.getString(type));
		} catch (JSONException e) {
			e.printStackTrace();
			}
		}
		try {
			gameLiveData.setTreasureJson(gameLiveData.getTreasureJson().put(type, String.valueOf(count + 1)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//System.out.println("json updated:"+json.toString());
	}

	private String showHelp(Context context) {
		if (type.equals(context.getString(R.string.y_key))) return context.getString(R.string.info_get_yellow_key);
		else if (type.equals(context.getString(R.string.b_key))) return context.getString(R.string.info_get_blue_key);
		else if (type.equals(context.getString(R.string.r_key))) return context.getString(R.string.info_get_red_key);
		else if (type.equals(context.getString(R.string.g_key))) return context.getString(R.string.info_get_green_key);
		else if (type.equals(context.getString(R.string.book_of_wisdom))) return context.getString(R.string.info_get_book_of_wisdom);
		else if (type.equals(context.getString(R.string.notebook))) return context.getString(R.string.info_get_notebook);
		else if (type.equals(context.getString(R.string.smile_coin))) return context.getString(R.string.info_get_smile_coin);
		else if (type.equals(context.getString(R.string.fly_wing))) return context.getString(R.string.info_get_fly_wing);
		else if (type.equals(context.getString(R.string.wooden_sword))) return context.getString(R.string.info_get_wooden_sword);
		else if (type.equals(context.getString(R.string.iron_sword))) return context.getString(R.string.info_get_iron_sword);
		else if (type.equals(context.getString(R.string.large_wooden_sword))) return context.getString(R.string.info_get_large_wooden_sword);
		else if (type.equals(context.getString(R.string.large_iron_sword))) return context.getString(R.string.info_get_large_iron_sword);
		else if (type.equals(context.getString(R.string.chaos_sword))) return context.getString(R.string.info_get_chaos_sword);
		else if (type.equals(context.getString(R.string.skin_shield))) return context.getString(R.string.info_get_skin_shield);
		else if (type.equals(context.getString(R.string.wooden_shield))) return context.getString(R.string.info_get_wooden_shield);
		else if (type.equals(context.getString(R.string.pink_energy_pot))) return context.getString(R.string.info_p_pot_energy);
		else if (type.equals(context.getString(R.string.iron_shield))) return context.getString(R.string.info_get_iron_shield);
		else if (type.equals(context.getString(R.string.diamond_shield))) return context.getString(R.string.info_get_diamond_shield);
		else if (type.equals(context.getString(R.string.divine_shield))) return context.getString(R.string.info_get_divine_shield);
		else if (type.equals(context.getString(R.string.b_diamond))) return context.getString(R.string.info_get_blue_diamond);
		else if (type.equals(context.getString(R.string.pickax))) return "You got a pickax!";
		else if (type.equals(context.getString(R.string.r_diamond))) return context.getString(R.string.info_get_red_diamond);
		else if (type.equals(context.getString(R.string.g_diamond))) return context.getString(R.string.info_get_green_diamond);
		else if (type.equals(context.getString(R.string.y_diamond))) return context.getString(R.string.info_get_yellow_diamond);
		else if (type.equals(context.getString(R.string.r_energy))) return context.getString(R.string.info_get_red_energy);
		else if (type.equals(context.getString(R.string.b_energy))) return context.getString(R.string.info_get_blue_energy);
		else if (type.equals(context.getString(R.string.g_energy))) return context.getString(R.string.info_get_green_energy);
		else if (type.equals(context.getString(R.string.y_energy))) return context.getString(R.string.info_get_yellow_energy);
		else if (type.equals(context.getString(R.string.detonator))) return context.getString(R.string.info_explosive);
		else if (type.equals(context.getString(R.string.green_pot)))return context.getString(R.string.info_g_pot_exp);
		else if (type.equals(context.getString(R.string.blessing_gift))) return context.getString(R.string.gift_popup);
		else if (type.equals(context.getString(R.string.upstairs_wing))) return context.getString(R.string.info_upstairs_wing);
		else if (type.equals(context.getString(R.string.downstairs_wing))) return context.getString(R.string.info_downstairs_wing);
		else if (type.equals(context.getString(R.string.old_scroll))) return context.getString(R.string.info_old_scroll);
		else if (type.equals(context.getString(R.string.g_key_box))) return context.getString(R.string.info_g_key_box);
		else if (type.equals(context.getString(R.string.cross))) return context.getString(R.string.info_cross);
		else if (type.equals(context.getString(R.string.b_key_box))) return context.getString(R.string.info_b_key_box);
		else if (type.equals(context.getString(R.string.y_key_box))) return context.getString(R.string.info_y_key_box);
		else if (type.equals(context.getString(R.string.cyan_attack_pot))) return context.getString(R.string.info_attack_pot);
		else if (type.equals(context.getString(R.string.blue_defense_pot))) return context.getString(R.string.info_defense_pot);
		else if (type.equals(context.getString(R.string.b_basement_key))) return context.getString(R.string.info_b_basement_key);
		else if (type.equals(context.getString(R.string.y_basement_key))) return context.getString(R.string.info_y_basement_key);
		else if (type.equals(context.getString(R.string.g_basement_key))) return context.getString(R.string.info_g_basement_key);
		else if (type.equals(context.getString(R.string.r_basement_key))) return context.getString(R.string.info_r_basement_key);
		else if (type.equals(context.getString(R.string.ice_plate))) return context.getString(R.string.info_ice_plate);
		else if (type.equals(context.getString(R.string.w_basement_key))) return context.getString(R.string.info_w_basement_key);
		else if (type.equals(context.getString(R.string.book_of_exp))) return context.getString(R.string.info_book_exp);
		else if (type.equals(context.getString(R.string.spring_shoes))) return context.getString(R.string.info_spring_shoe);
		else if (type.equals(context.getString(R.string.warrior_axe))) return context.getString(R.string.info_warrior_axe);
		return context.getString(R.string.error_key);
	}
}
