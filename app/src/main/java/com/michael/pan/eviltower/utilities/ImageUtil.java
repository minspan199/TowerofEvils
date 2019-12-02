package com.michael.pan.eviltower.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.michael.pan.eviltower.R;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.TAG_ENEMY_BEFORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ANGEL;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ANGEL_VISITED;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_ENEMY_AFTER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_EXPERIENCE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_EXPERIENCE_VISITED;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_KEY_MERCHANT;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_KEY_MERCHANT_VISITED;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_PRISONER;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_REAL_PRINCESS;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_RED_DRAGON;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_SECRET_MERCHANT;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_TECHNICIAN;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_WIZARD_BLUE;

public class ImageUtil {

//	public static Bitmap[] getTreasureBitmapArray(Context context) {
//		JSONObject json = getDefaultJSON(context);
//		JSONArray names = json.names();
//		Bitmap[] treasuresArray = new Bitmap[names.length()];
//		try {
//			for (int i = 0; i < names.length(); i++) {
//				treasuresArray[i] = getTreasureBitmapByKey(context, names.getString(i), getBitmapArray(context));
//				//System.out.println("Image stored in array at position :" + i + " for " + names.get(i));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return treasuresArray;
//	}
//
//	public static Bitmap[][] getBitmapArray(Context context) {
//
//		return splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.treasures), 4, 16);
//
//	}

	public static Bitmap getTreasureBitmapByKey(Context context, String key, Bitmap[][] bitmaps) {
		Bitmap treasureIcon;

		if (context.getString(R.string.r_key).equals(key)) treasureIcon = bitmaps[2][4];
		else if (context.getString(R.string.b_key).equals(key)) treasureIcon = bitmaps[1][4];
		else if (context.getString(R.string.y_key).equals(key)) treasureIcon = bitmaps[0][4];
		else if (context.getString(R.string.g_key).equals(key)) treasureIcon = bitmaps[3][4];
		else if (context.getString(R.string.r_diamond).equals(key)) treasureIcon = bitmaps[1][3];
		else if (context.getString(R.string.b_diamond).equals(key)) treasureIcon = bitmaps[1][0];
		else if (context.getString(R.string.g_diamond).equals(key)) treasureIcon = bitmaps[0][2];
		else if (context.getString(R.string.y_diamond).equals(key)) treasureIcon = bitmaps[0][3];
		else if (context.getString(R.string.key_box).equals(key)) treasureIcon = bitmaps[2][5];
		else if (context.getString(R.string.book_of_wisdom).equals(key)) treasureIcon = bitmaps[0][7];
		else if (context.getString(R.string.notebook).equals(key)) treasureIcon = bitmaps[1][7];
		else if (context.getString(R.string.pot_of_wisdom).equals(key)) treasureIcon = bitmaps[3][2];
		else if (context.getString(R.string.large_pot_of_wisdom).equals(key)) treasureIcon = bitmaps[3][3];
		else if (context.getString(R.string.smile_coin).equals(key)) treasureIcon = bitmaps[3][7];
		else if (context.getString(R.string.fly_wing).equals(key)) treasureIcon = bitmaps[0][9];
		else if (context.getString(R.string.large_smile_coin).equals(key)) treasureIcon = bitmaps[3][7];
		else if (context.getString(R.string.cross).equals(key)) treasureIcon = bitmaps[3][9];
		else if (context.getString(R.string.blessing_gift).equals(key)) treasureIcon = bitmaps[1][11];
		else if (context.getString(R.string.wooden_sword).equals(key)) treasureIcon = bitmaps[0][12];
		else if (context.getString(R.string.iron_sword).equals(key)) treasureIcon = bitmaps[1][12];
		else if (context.getString(R.string.large_wooden_sword).equals(key)) treasureIcon = bitmaps[2][12];
		else if (context.getString(R.string.large_iron_sword).equals(key)) treasureIcon = bitmaps[3][12];
		else if (context.getString(R.string.chaos_sword).equals(key)) treasureIcon = bitmaps[0][13];
		else if (context.getString(R.string.skin_shield).equals(key)) treasureIcon = bitmaps[0][14];
		else if (context.getString(R.string.wooden_shield).equals(key)) treasureIcon = bitmaps[1][14];
		else if (context.getString(R.string.iron_shield).equals(key)) treasureIcon = bitmaps[2][14];
		else if (context.getString(R.string.diamond_shield).equals(key)) treasureIcon = bitmaps[3][14];
		else if (context.getString(R.string.divine_shield).equals(key)) treasureIcon = bitmaps[0][15];
		else if (context.getString(R.string.coins).equals(key)) treasureIcon = bitmaps[3][7];
		else if (context.getString(R.string.detonator).equals(key)) treasureIcon = bitmaps[0][10];
		else if (context.getString(R.string.pink_energy_pot).equals(key)) treasureIcon = bitmaps[1][2];
		else if (context.getString(R.string.green_pot).equals(key)) treasureIcon = bitmaps[0][2];
		else if (context.getString(R.string.upstairs_wing).equals(key)) treasureIcon = bitmaps[2][9];
		else if (context.getString(R.string.downstairs_wing).equals(key)) treasureIcon = bitmaps[1][9];
		else if (context.getString(R.string.old_scroll).equals(key)) treasureIcon = bitmaps[3][8];
		else if (context.getString(R.string.g_key_box).equals(key)) treasureIcon = bitmaps[0][5];
		else if (context.getString(R.string.cross).equals(key)) treasureIcon = bitmaps[3][9];
		else if (context.getString(R.string.b_key_box).equals(key)) treasureIcon = bitmaps[1][5];
		else if (context.getString(R.string.y_key_box).equals(key)) treasureIcon = bitmaps[2][5];
		else if (context.getString(R.string.cyan_attack_pot).equals(key)) treasureIcon = bitmaps[3][2];
		else if (context.getString(R.string.blue_defense_pot).equals(key)) treasureIcon = bitmaps[2][2];
		else if (context.getString(R.string.ice_plate).equals(key)) treasureIcon = bitmaps[2][8];
		else if (context.getString(R.string.g_basement_key).equals(key)) treasureIcon = bitmaps[3][6];
		else if (context.getString(R.string.r_basement_key).equals(key)) treasureIcon = bitmaps[2][6];
		else if (context.getString(R.string.b_basement_key).equals(key)) treasureIcon = bitmaps[1][6];
		else if (context.getString(R.string.y_basement_key).equals(key)) treasureIcon = bitmaps[0][6];
		else if (context.getString(R.string.w_basement_key).equals(key)) treasureIcon = bitmaps[3][5];
		else if (context.getString(R.string.pickax).equals(key)) treasureIcon = bitmaps[0][8];
		else if (context.getString(R.string.book_of_exp).equals(key)) treasureIcon = bitmaps[2][11];
		else if (context.getString(R.string.spring_shoes).equals(key)) treasureIcon = bitmaps[0][11];
		else if (context.getString(R.string.warrior_axe).equals(key)) treasureIcon = bitmaps[2][10];
		else treasureIcon = bitmaps[3][2];
		return treasureIcon;
	}

	public static Bitmap[][] splitBitmap(Bitmap bitmap, int xCount, int yCount) {
		// Allocate a two dimensional array to hold the individual images.
		Bitmap[][] bitmaps = new Bitmap[xCount][yCount];
		int width, height;
		// Divide the original bitmap width by the desired vertical column count
		width = bitmap.getWidth() / xCount;
		// Divide the original bitmap height by the desired horizontal row count
		height = bitmap.getHeight() / yCount;
		// Loop the array and create bitmaps for each coordinate
//		System.out.println("width:" + width + ";height:" + height);
		for (int x = 0; x < xCount; ++x) {
			for (int y = 0; y < yCount; ++y) {
				// Create the sliced bitmap
				bitmaps[x][y] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);
			}
		}
		// Return the array
		return bitmaps;
	}

	public static Bitmap getNpcIcon(Context context, String npcType, int floor) {

			switch (npcType) {
				case TAG_ANGEL:
				case TAG_ANGEL_VISITED:
					return BitmapFactory.decodeResource(context.getResources(), R.drawable.angel);
				case TAG_EXPERIENCE:
				case TAG_EXPERIENCE_VISITED:
					return BitmapFactory.decodeResource(context.getResources(), R.drawable.experience_merchant);
				case TAG_KEY_MERCHANT:
				case TAG_KEY_MERCHANT_VISITED:
					return BitmapFactory.decodeResource(context.getResources(), R.drawable.key_merchant);
				case TAG_TECHNICIAN:
					return BitmapFactory.decodeResource(context.getResources(), R.drawable.technician);
				case TAG_ENEMY_AFTER:
					if (floor == 36) return BitmapFactory.decodeResource(context.getResources(), R.drawable.fake_princess);
					else return BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_defeated);
				case TAG_ENEMY_BEFORE:
					if (floor == 36) return BitmapFactory.decodeResource(context.getResources(), R.drawable.fake_princess);
					else return BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_challenge);
				case TAG_PRISONER:
					return BitmapFactory.decodeResource(context.getResources(), R.drawable.prisoner_warrior);
				case TAG_RED_DRAGON:
					return BitmapFactory.decodeResource(context.getResources(), R.drawable.red_dragon);
				case TAG_SECRET_MERCHANT:
					return BitmapFactory.decodeResource(context.getResources(), R.drawable.secret_merchant);
				case TAG_WIZARD_BLUE:
					return BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_robe_wizard);
				case TAG_REAL_PRINCESS:
					return BitmapFactory.decodeResource(context.getResources(), R.drawable.real_princess);
			}
		return null;
	}

	public static Bitmap getUserIcon(Context context, int id){
		switch (id){
			case 8:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.road);
			case 1:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit);
			case 2:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.castle);
			case 3:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.haski);
			case 4:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.key_merchant);
			case 5:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.warrior);
			case 6:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.crowns);
			case 7:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.lightening);
			case 9:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.beach);
			case 10:
			default:
				return BitmapFactory.decodeResource(context.getResources(), R.drawable.beef_icon);
		}
	}
}
