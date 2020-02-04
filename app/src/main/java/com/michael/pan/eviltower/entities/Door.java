package com.michael.pan.eviltower.entities;

//Create a door entity for validation of door opening conditions, door opening animation, popup instruction for user interaction.

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.TextView;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.utilities.AnimUtil;
import com.michael.pan.eviltower.utilities.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;

public class Door {

	private final int matrixValue;
	private int type, x, y, floor;
	private boolean ifOpen;
	private boolean canOpen;
	private boolean specialDoor;
	private int anInt = 7;
	private Bitmap[][] bitmap;

	public Door(Context context, Bundle extras, TextView textField, boolean ifOpen) {
		x = extras.getInt(context.getString(R.string.xTouch));
		y = extras.getInt(context.getString(R.string.yTouch));
		floor = extras.getInt(context.getString(R.string.floor));
		matrixValue = extras.getInt(context.getString(R.string.matrix_value));
		this.ifOpen = ifOpen;
		gameView.doorAnimating = true;
		bitmap = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.animates), 4, 27);
		switch (this.matrixValue) {
			case 26:
				type = 5;// yellow door
				canOpen = gameLiveData.getyKey() > 0;
				specialDoor = false;
				break;
			case 27:
				type = 6;// blue door
				specialDoor = false;
				canOpen = gameLiveData.getbKey() > 0;
				break;
			case 28:
				type = 7;// red door
				specialDoor = false;
				canOpen = gameLiveData.getrKey() > 0;
				break;
			case 29:
				type = 8;// green door
				specialDoor = false;
				canOpen = gameLiveData.getgKey() > 0;
				break;
			case 30:
				type = 9;//crystal door
				specialDoor = true;
				if (canOpen(context) || textField == null) trigger(context);
				else sendErrorMes(context, textField);
				break;
			case 31:
				type = 10;// steel door
				specialDoor = true;
				if (canOpen(context) || textField == null) trigger(context);
				else sendErrorMes(context, textField);
				break;
			case -1:
				type = 10;// floor collapsing
				specialDoor = true;
				trigger(context);
				break;
		}
		if (!specialDoor) {//if the door is an ordinary door that may need to fire fly message to the game screen.
			if (canOpen && Open(context)) {
				anInt = 0;//set a new start point for animation.'
				gameView.layer00[y][x] = getBackgroundCode(floor);
				gameView.update();
			} else {
				AnimUtil.sendFlyMessage(textField, showHelp(context));//show text that keys are needed to open the door
				gameView.doorAnimating = false;
			}
		}
	}

	private void sendErrorMes(Context context, TextView view) {
		switch (floor) {
			case 0:
				if (x == 6 && y == 6)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.error_enter_game));
				else if (x == 10 && y == 3)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.error_basement_key));
				break;
			case 6:
			case 12:
			case 15:
			case 18:
			case 20:
			case 21:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_safeguards));
				break;
			case 11:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.defeat_five_knights));
				break;
			case 17:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_all_enemies));
				break;
			case 19:
				if ((x == 10 && y == 3) || (x == 4 && y == 6))
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_safeguards));
				else if ((x == 11 || x == 12) && y == 5)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_all_enemies));
				else
					AnimUtil.sendFlyMessage(view, context.getString(R.string.warn_balcony_barrier));
				break;
			case 23:
				if ((x == 10 && y == 5) || (x == 10 && y == 4))
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_safeguards));
				else
					AnimUtil.sendFlyMessage(view, context.getString(R.string.warn_balcony_barrier));
				break;
			case 24:
				if (x == 8 && y == 7)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_safeguards));
				else if (x == 11 && y == 6)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.defeat_4_pinkbats));
				else
					AnimUtil.sendFlyMessage(view, context.getString(R.string.warn_balcony_barrier));
				break;
			case 26:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.defeat_3_gw));
				break;
			case 27:
				if (gameView.layer02[3][5] == 0 || gameView.layer02[3][10] == 0)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.cannot_open));
				else if (x == 7 && y == 3)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_4_greenwidzard));
				else if ((x == 10 && y == 7) || (x == 11 && y == 7))
					AnimUtil.sendFlyMessage(view, context.getString(R.string.defeat_4_skeleton));
				break;
			case 29:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.defeat_the_bosses));
				break;
			case 30:
				if ((x == 2 || x == 3 || x == 4 || x == 5) && y == 8)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.defeat_below_safe));
				else if (x == 1 && (y == 10 || y == 11))
					AnimUtil.sendFlyMessage(view, context.getString(R.string.defeat_the_guards));
				break;
			case 31:
				if ((x == 3 && (y == 4 || y == 5 || y == 6)) || (y == 4 && (x == 4 || x == 5)))
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_all_enemies));
				break;
			case 32:
				if (x == 7 && y == 1)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_red_dragon));
				else AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_dragon_n_safe));
				break;
			case 33:
				if (x == 3 && y == 5)
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_safeguards));
				else if ((x == 9 && y == 1) || (x == 9 && y == 2) || (x == 9 && y == 3))
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_5_wizards));
				else AnimUtil.sendFlyMessage(view, context.getString(R.string.cannot_open));
				break;
			case 34:
				if ((x == 11 && y == 10) || (x == 12 && y == 9) || (x == 11 && y == 8))
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_safeguards));
				break;
			case 36:
				if ((x == 1 && y == 3) || (x == 6 && y == 9))
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_safeguards));
				break;
			case 37:
				if ((x == 11 && y == 10) || (x == 11 && y == 8) || (x == 1 && y == 11))
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_all_enemies));
				break;
			case 40:
				if (((x == 5 || x == 6 || x == 7 || x == 8 || x == 9 || x == 10 || x == 11) && y == 5) || ((x == 5 || x == 11) && (y == 6 || y == 7 || y == 8))) {
					AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_safeguards_floor));
				} else AnimUtil.sendFlyMessage(view, context.getString(R.string.cannot_open));
				break;
			case 41:
				AnimUtil.sendFlyMessage(view, context.getString(R.string.clear_safeguards_floor));
				break;
		}
	}

	private boolean canOpen(Context context) {
//		System.out.println("floor"+floor+"x"+x+"y"+y);
		switch (floor) {
			case 0:
				if (x == 7 && y == 7) return gameView.layer01[7][7] == 53;
				else if (x == 10 && y == 3) return checkBasementKeys(context);
				break;
			case 6:
				if (x == 1 && y == 2)
					return (gameView.layer02[y + 1][x - 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);//enemy
				else if (x == 3 && y == 9)
					return (gameView.layer02[y - 1][x + 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				break;
			case 11:
				if (x == 2 && y == 3)
					return (gameView.layer02[y][x + 1] == 0 && gameView.layer02[3][5] == 0 && gameView.layer02[5][5] == 0 && gameView.layer02[5][3] == 0 && gameView.layer02[4][4] == 0);
				break;
			case 12:
				if (x == 7 && y == 2)
					return (gameView.layer02[y - 1][x + 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				else if (x == 5 && y == 2)
					return (gameView.layer02[y - 1][x + 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				else if (x == 7 && y == 9)
					return (gameView.layer02[y - 1][x - 1] == 0 && gameView.layer02[y + 1][x - 1] == 0);
				break;
			case 15:
				if (x == 5 && y == 6)
					return (gameView.layer02[y - 1][x - 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				else return (gameView.layer02[2][3] == 0 && gameView.layer02[4][3] == 0);
			case 17:
				if (x == 11 && y == 3 || x == 12 && y == 3) return ifAllZero(gameView.layer02);
				else return true;
			case 18:
				if (x == 10 && y == 3)
					return (gameView.layer02[y - 1][x + 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				else if (x == 8 && y == 3)
					return (gameView.layer02[y - 1][x - 1] == 0 && gameView.layer02[y + 1][x - 1] == 0);
				else if (x == 3 && y == 6)
					return (gameView.layer02[y + 1][x - 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				else if (x == 3 && y == 4)
					return (gameView.layer02[y + 1][x - 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				else if (x == 3 && y == 2)
					return (gameView.layer02[y + 1][x - 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				else return true;
			case 19:
				if (x == 10 && y == 3)
					return (gameView.layer02[y - 1][x + 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				else if (x == 4 && y == 6)
					return (gameView.layer02[y + 1][x - 1] == 0 && gameView.layer02[y + 1][x + 1] == 0);
				else if ((x == 11 || x == 12) && y == 5) return ifAllZero(gameView.layer02);
				break;
			case 20:
				if (x == 10 && y == 4)
					return (gameView.layer02[y - 1][x + 1] == 0 && gameView.layer02[y - 1][x - 1] == 0);
				else if (x == 4 && y == 4)
					return (gameView.layer02[y - 1][x + 1] == 0 && gameView.layer02[y - 1][x - 1] == 0);
				break;
			case 21:
				if (x == 10 && y == 9)
					return (gameView.layer02[y + 1][x - 1] == 0 && gameView.layer02[y - 1][x - 1] == 0);
				else if (x == 7 && y == 2)
					return (gameView.layer02[1][6] == 0 && gameView.layer02[3][6] == 0);
				break;
			case 23:
				if (x == 10 && y == 4)
					return (gameView.layer02[y][x - 1] == 0 && gameView.layer02[y - 1][x - 1] == 0);
				else if (x == 10 && y == 5)
					return (gameView.layer02[y + 1][x - 1] == 0 && gameView.layer02[y][x - 1] == 0);
				break;
			case 24:
				if (x == 8 && y == 7)
					return (gameView.layer02[y - 1][x - 1] == 0 && gameView.layer02[y - 1][x + 1] == 0);
				else if (x == 11 && y == 6)
					return (gameView.layer02[5][12] == 0 && gameView.layer02[5][13] == 0 && gameView.layer02[7][12] == 0 && gameView.layer02[7][13] == 0);
				break;
			case 26:
				return (gameView.layer02[5][6] == 0 && gameView.layer02[3][6] == 0 && gameView.layer02[1][6] == 0);
			case 27:
				if (x == 7 && y == 3)
					return (gameView.layer02[3][5] != 0 && gameView.layer02[3][4] == 0 && gameView.layer02[3][6] == 0 && gameView.layer02[2][5] == 0 && gameView.layer02[4][5] == 0);
				else if (x == 10 && y == 7 || x == 11 && y == 7)
					return (gameView.layer02[3][10] != 0 && gameView.layer02[3][9] == 0 && gameView.layer02[3][11] == 0 && gameView.layer02[2][10] == 0 && gameView.layer02[4][10] == 0);
				break;
			case 29:
				if (x == 1 && y == 1 || x == 3 && y == 5)
					return gameView.layer02[8][4] == 0 && gameView.layer02[6][10] == 0 && gameView.layer02[8][10] == 0 && gameView.layer02[10][10] == 0;
				else if (x == 2 & y == 10)
					return gameView.layer02[9][1] == 0 && gameView.layer02[11][1] == 0;
				break;
			case 30:
				if (((x == 2 || x == 3 || x == 4 || x == 5) && y == 8) || (x == 1 && (y == 10 || y == 11)))
					return gameView.layer02[9][2] == 0 && gameView.layer02[9][3] == 0 && gameView.layer02[9][4] == 0 && gameView.layer02[9][5] == 0;
				break;
			case 31:
				if (x == 3 && (y == 4 || y == 5 || y == 6) || y == 4 && (x == 4 || x == 5))
					return ifAllZero(gameView.layer02);
				break;
			case 32:
				if (x == 7 && y == 1) return gameView.layer02[5][10] == 0;
				else if (x == 4 && y == 4)
					return gameView.layer02[5][10] == 0 && gameView.layer02[4][5] == 0 && gameView.layer02[5][5] == 0 && gameView.layer02[6][5] == 0;
				break;
			case 33:
				if (x == 3 && y == 5)
					return gameView.layer02[4][4] == 0 && gameView.layer02[4][2] == 0;
				else if ((x == 9 && y == 1) || (x == 9 && y == 2) || (x == 9 && y == 3))
					return gameView.layer02[1][10] == 0 && gameView.layer02[3][10] == 0 && gameView.layer02[1][12] == 0 && gameView.layer02[3][12] == 0 && gameView.layer02[2][11] == 0;
				break;
			case 34:
				if ((x == 11 && y == 10) || (x == 12 && y == 9) || (x == 11 && y == 8))
					return gameView.layer02[10][10] == 0 && gameView.layer02[9][10] == 0 && gameView.layer02[8][10] == 0;
				break;
			case 36:
				if (x == 1 && y == 3)
					return gameView.layer02[4][2] == 0 && gameView.layer02[3][2] == 0 && gameView.layer02[2][2] == 0;
				else if (x == 6 && y == 9)
					return gameView.layer02[8][6] == 0 && gameView.layer02[10][6] == 0;
				break;
			case 37:
				if ((x == 11 && y == 10) || (x == 11 && y == 8) || (x == 1 && y == 11))
					return ifAllZero(gameView.layer02, 12, 6);
				break;
			case 40:
				if (((x == 5 || x == 6 || x == 7 || x == 8 || x == 9 || x == 10 || x == 11) && y == 5) || ((x == 5 || x == 11) && (y == 6 || y == 7 || y == 8)))
					return ifAllZero(gameView.layer02, 7, 4);
				break;
			case 41:
				if (x == 5 && y == 3)
					return ifAllZero(gameView.layer02, 6, 3);//if it is the crystal door
				else
					return ifAllZeroExcept(gameView.layer02, new Integer[]{170, 182});//170 the pink bat
		}
		return false;
	}

	private boolean ifAllZeroExcept(int[][] arr, Integer[] args) {
		int N = arr[0].length;//15
		int M = arr.length;
		List<Integer> exception = Arrays.asList(args);
		boolean b = true;
		for (int[] ints : arr) {
			for (int j = 0; j < N; j++) {
//				System.out.println("x:"+i+"y:"+j+"arr"+arr[i][j]);
				if (ints[j] != 0 && !exception.contains(ints[j])) {//the or here is to judge the coordinates!!!
					b = false;
					break;
				}
			}
		}
		return b;
	}

	private boolean checkBasementKeys(Context context) {
		int count = 0;
		JSONObject json = gameLiveData.getTreasureJson();
		if (!json.isNull(context.getString(R.string.r_basement_key))) {
			try {
				count = Integer.parseInt(json.getString(context.getString(R.string.r_basement_key)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (count <= 0) return false;
		} else return false;//else if the key is not even stored in the JSON
		if (!json.isNull(context.getString(R.string.w_basement_key))) {
			try {
				count = Integer.parseInt(json.getString(context.getString(R.string.w_basement_key)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (count <= 0) return false;
		} else return false;
		if (!json.isNull(context.getString(R.string.y_basement_key))) {
			try {
				count = Integer.parseInt(json.getString(context.getString(R.string.y_basement_key)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (count <= 0) return false;
		} else return false;
		if (!json.isNull(context.getString(R.string.b_basement_key))) {
			try {
				count = Integer.parseInt(json.getString(context.getString(R.string.b_basement_key)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (count <= 0) return false;
		} else return false;
		if (!json.isNull(context.getString(R.string.g_basement_key))) {
			try {
				count = Integer.parseInt(json.getString(context.getString(R.string.g_basement_key)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return count > 0;
		} else return false;
	}

	private boolean ifAllZero(int[][] arr, int x, int y) {
		int N = arr[0].length;//15
		int M = arr.length;
		boolean b = true;
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
//				System.out.println("x:"+i+"y:"+j+"arr"+arr[i][j]);
				if ((i != y || j != x) && arr[i][j] != 0) {//the or here is to judge the coordinates!!!
					b = false;
//					System.out.println(arr[i][j] +"is not 0");
					break;
				}
			}
		}
		return b;
	}

	private boolean ifAllZero(int[][] arr) {

		int N = arr[0].length;//15
		boolean b = true;
		for (int[] ints : arr) {
			for (int j = 0; j < N; j++) {
				if (ints[j] != 0) {
					b = false;
					break;
				}
			}
		}
		return b;
	}

	private void trigger(Context context) {
		anInt = 0;
		if (floor != 0) gameView.layer00[y][x] = getBackgroundCode(floor);
		else {
			if (x == 7 && y == 7 && gameView.layer01[y][x] == 52) {
				gameView.layer00[6][6] = 9; // replace with indoor ground.
				gameView.layer01[y][x] = 53;// set the angel to be visited.
				y = 6;
				x = 6;
			} else if (x == 10 && y == 3) {
				updateBKeyJson(context);
				gameView.layer00[3][10] = getBackgroundCode(floor);
			}
		}
		gameView.update();
	}

	private void updateBKeyJson(Context context) {
		JSONObject json = gameLiveData.getTreasureJson();
		json.remove(context.getString(R.string.r_basement_key));
		json.remove(context.getString(R.string.b_basement_key));
		json.remove(context.getString(R.string.w_basement_key));
		json.remove(context.getString(R.string.g_basement_key));
		json.remove(context.getString(R.string.y_basement_key));
		gameLiveData.setTreasureJson(json);
	}

	public boolean canDraw() {
		if (anInt >= 7) {
			gameView.doorAnimating = false;
			return false;
		} else {
			anInt++;
			return true;
		}
	}

	public void draw(Canvas canvas, float xBlockSize, float yBlockSize) {
		canvas.drawBitmap(this.getGraph(anInt / 2), null, new RectF(x * xBlockSize, y * yBlockSize, (x + 1) * xBlockSize, (y + 1) * yBlockSize), null);
	}

	private Bitmap getGraph(int index) {
		if (ifOpen) {
			return bitmap[index][type - 1];
		} else return bitmap[3 - index][type - 1];
	}

	private boolean Open(Context context) {
		if (canOpen) {
			switch (matrixValue) {
				case 26:// yellow door
					gameLiveData.setyKey(gameLiveData.getyKey() - 1);
					break;
				case 27:// blue door
					gameLiveData.setbKey(gameLiveData.getbKey() - 1);
					break;
				case 28:// red door
					gameLiveData.setrKey(gameLiveData.getrKey() - 1);
					break;
				case 29://green door
					gameLiveData.setgKey(gameLiveData.getgKey() - 1);
					int count = 0;
					JSONObject json = gameLiveData.getTreasureJson();
					if (!json.isNull(context.getString(R.string.g_key))) {
						try {
							count = Integer.parseInt(json.getString(context.getString(R.string.g_key)));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					try {
						gameLiveData.setTreasureJson(gameLiveData.getTreasureJson().put(context.getString(R.string.g_key), String.valueOf(count - 1)));
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
			return true;
		} else {
			return false;
		}
	}

	private String showHelp(Context context) {
		switch (type) {
			case 5:
				return context.getString(R.string.error_no_yellow_key);
			case 6:
				return context.getString(R.string.error_no_blue_key);
			case 7:
				return context.getString(R.string.error_no_red_key);
			case 8:
				return context.getString(R.string.error_no_green_key);
		}
		return context.getString(R.string.error_key);
	}

	static int getBackgroundCode(int floor) {
		switch (floor) {
			case 0:
			case 1:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				return 9;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				return 4;
			case 30:
			case 31:
			case 32:
			case 33:
			case 34:
			case 35:
			case 36:
			case 37:
			case 38:
			case 39:
			case 40:
				return 5;
			case 2:
			default:
				return 1;
		}
	}

}
