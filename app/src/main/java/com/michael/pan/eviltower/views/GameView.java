package com.michael.pan.eviltower.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.activities.MainThread;
import com.michael.pan.eviltower.data.FloorData;
import com.michael.pan.eviltower.data.FlyWingData;
import com.michael.pan.eviltower.utilities.AlgorithmUtil;
import com.michael.pan.eviltower.utilities.ImageUtil;
import com.michael.pan.eviltower.utilities.Node;

import org.json.JSONException;

import java.util.ArrayList;

import static com.michael.pan.eviltower.activities.StartGameActivity.door;
import static com.michael.pan.eviltower.activities.StartGameActivity.flyWingData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.mapData;
import static com.michael.pan.eviltower.activities.StartGameActivity.mapDataInArrays;
import static com.michael.pan.eviltower.activities.StartGameActivity.warrior;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_DOOR_EVENT;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_DOWNSTAIRS;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_ENEMY_EVENT;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_NO_PATH;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_NPC_EVENT;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_TREASURE_EVENT;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATES_UPSTAIRS;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.STATUS_LOADING;
import static com.michael.pan.eviltower.utilities.AlgorithmUtil.canReach;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "GameView: ";
	public int FPS;
	public int[][] layer00, layer01, layer02, layer03, layerMerged;
	private static int index = 0, counter = 0;
	public boolean handlingStoreViewWindow = false;
	public boolean handlingDialogEvents = false;
	public boolean handlingNotePadWindow = false;
	public boolean handlingEnemyListWindow = false;
	public boolean handlingBattleWindow = false;
	public boolean handlingTransitionView = false;
	public boolean doorAnimating = false;
	private String direction;
	public int floor;
	public float screenWidth, screenHeight, xBlockSize, yBlockSize;
	private int xTouch;
	private int yTouch;
	public int xCount;
	public int yCount;
	public FloorData currentFloorData;
	public MainThread gameThread;
	public Bitmap[][] background, npcs, enemies, treasures, background2;
	public boolean drawMap = true;

	public GameView(Context context) {
		super(context);
	}

	public void update() {
		loadMapData(floor);
	}

	public interface updateStates {
		void updateStatesA(String s, int floor);
		void updateStatesB(String s, int xTouch, int yTouch, int matrixValue, int floor);
	}

	private updateStates statesUpdate;

	public GameView(Context context, updateStates states) {
		super(context);
		this.statesUpdate = states;
		getHolder().addCallback(this);

		background = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.terrains), 1, 45);
		npcs = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.npcs), 2, 28);
		enemies = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemys), 2, 60);
		treasures = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.treasures), 4, 16);
		background2 = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.animates), 4, 27);
		layer00 = null;layer01 = null; layer02 = null; layer03 = null;

		setFocusable(true);

	}

	public void setFloor(int f) {
		floor = f;
		loadMapData(floor);
		drawMap = false;
		gameLiveData.setFloor(floor);// set the floor to the LiveData for UI updates
		statesUpdate.updateStatesA(STATUS_LOADING, floor);//send to the activity to show loading page.
	}

	private void loadMapData(int floor) {
//		Log.i(TAG, "loading map data");
		currentFloorData = mapDataInArrays.get(floor);
		layer00 = currentFloorData.getLayer00();
		layer01 = currentFloorData.getLayer01();
		layer02 = currentFloorData.getLayer02();				//enemies encoding
		layer03 = currentFloorData.getLayer03();
		layerMerged = currentFloorData.getMergedLayer();

		//Log.i(TAG, "Loaded floor data layers, layer00:" + Arrays.deepToString(layer00) + "; layer01:" + Arrays.deepToString(layer01) +
		//	"; layer02:" + Arrays.deepToString(layer02) + "; layer03:" + Arrays.deepToString(layer03));
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (counter <= 3){
			counter ++;
		}else {
			counter = 0;
			if (index > 2) index = 0;
			else index++;
		}
		if(drawMap) {
			drawGameViews(canvas);
			if (door != null && door.canDraw()) {
				door.draw(canvas, xBlockSize, yBlockSize);
			}
		}
	}

	public void drawGameViews(Canvas canvas) {
		if (layer00 != null) drawLayer0(canvas);
		if (layer01 != null) drawLayer1(canvas);
		if (layer02 != null) drawLayer2(canvas);
		if (layer03 != null) drawLayer3(canvas);
		warrior.draw(canvas, xBlockSize, yBlockSize);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN && !handlingTransitionView && !handlingBattleWindow && !handlingNotePadWindow && !handlingEnemyListWindow && !handlingStoreViewWindow && !handlingDialogEvents && !warrior.getMoving()) {
			//calculate the touch event coordinates.
			xTouch = (int) (event.getX()/xBlockSize);
			yTouch = (int) (event.getY()/yBlockSize);
			//if (xTouch >= yCount || yTouch >= xCount) return true;
//			System.out.println("Clicked on position x: " + xTouch + " and y: " + yTouch + " the destination is: " + layerMerged[yTouch][xTouch]);
//			System.out.println("Clicked on position x: " + xTouch + " and y: " + yTouch + " the background is: " + layer00[yTouch][xTouch]);
//			if (layer02 != null)System.out.println("Clicked on position x: " + xTouch + " and y: " + yTouch + " the enemy is: " + layer02[yTouch][xTouch]);
//			System.out.println("floor:" + floor);

			if (xTouch != warrior.getX() || yTouch != warrior.getY()){//detected warrior position needs to change
				ArrayList<Node> nodes = null;
				if (layerMerged[yTouch][xTouch] == 0 || layerMerged[yTouch][xTouch] == 1) {
					nodes = AlgorithmUtil.getPath(layerMerged, warrior.getY(), warrior.getX(), yTouch, xTouch);
					//interchange the coordinates due to the index of the 2D array
					//the getPath method doesn't modify the 2D array object so it can be passed here.
				}else if (layerMerged[yTouch][xTouch] == 2 || layerMerged[yTouch][xTouch] == 100 || layerMerged[yTouch][xTouch] == 200 || layerMerged[yTouch][xTouch] == 300 ){

					int[] neighbor = getReachableNeighbor(xTouch, yTouch);
					if (neighbor != null){
//						Log.i(TAG, "Destination is NPC, door, or enemy, finding neighboring position, X:" + neighbor[0] + ", Y:"+ neighbor[1] );
						nodes = AlgorithmUtil.getPath(layerMerged, warrior.getY(), warrior.getX(), neighbor[1], neighbor[0]);
						//interchange the coordinates due to the index of the 2D array
					}
				}

				if (nodes != null){
					warrior.updateLocation(nodes);
//					Log.i(TAG, "Find a route: "+ nodes.toString());
					while (true){
						try {
							Thread.sleep((long) (2000/gameThread.getFPS()));
							if (!warrior.getMoving()){
								handleEncounterEvents(xTouch, yTouch);
								break;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} else {
					statesUpdate.updateStatesA(STATES_NO_PATH, floor);
//					Log.i(TAG, "No route to get there.");
					direction = AlgorithmUtil.getDirection(xTouch, yTouch, warrior.getX(), warrior.getY());
					warrior.setStatus(direction, false);
				}
			}
			else if (layer00[yTouch][xTouch] == 24 ||layer00[yTouch][xTouch] == 25) handleSpecialBg();
		}
		return super.onTouchEvent(event);
	}

	private void handleEncounterEvents(int xTouch, int yTouch) {
//		Log.i(TAG, "Handling encounter events at X: " + xTouch + ", Y: " + yTouch);
		switch (layerMerged[yTouch][xTouch]){
			case 0:
			case 1:
			case 2:
				handleSpecialBg();
				break;
			case 100:
				handleNPCEvents();
				break;
			case 200:
				handleEnemyEvents();
				break;
			case 300:
				handleTreasuresEvents();
				break;
			default:
//				Log.i(TAG, "Encounter Events Not Handled!");
				break;
		}
	}

	private void handleSpecialBg() {
		switch(layer00[yTouch][xTouch]){
			case 24:
				rememberWarriorPosition();
				switch (floor) {
					case 0:
						statesUpdate.updateStatesA(STATES_DOWNSTAIRS, 43);
						break;
					case 37:
						statesUpdate.updateStatesA(STATES_DOWNSTAIRS, floor - 3);
						break;
					case 32:
					case 39:
						statesUpdate.updateStatesA(STATES_DOWNSTAIRS, floor - 2);
						break;
					default:
						statesUpdate.updateStatesA(STATES_DOWNSTAIRS, floor - 1);
						break;
				}
				break;
			case 25:
				rememberWarriorPosition();
				switch (floor) {
					case 34:
						statesUpdate.updateStatesA(STATES_UPSTAIRS, floor + 3);
						break;
					case 30:
					case 37:
						statesUpdate.updateStatesA(STATES_UPSTAIRS, floor + 2);
						break;
					case 43:
						statesUpdate.updateStatesA(STATES_UPSTAIRS, 0);
						break;
					default:
						statesUpdate.updateStatesA(STATES_UPSTAIRS, floor + 1);
						break;
				}
				break;
			case 26:
			case 27:
			case 28:
			case 29:
			case 30:
			case 31:
				statesUpdate.updateStatesB(STATES_DOOR_EVENT, xTouch, yTouch, layer00[yTouch][xTouch], floor);
				break;
			default:
				break;
		}
	}

	private void rememberWarriorPosition() {
		FlyWingData flyWing = new FlyWingData(floor, xTouch, yTouch);
		try {
			flyWingData.put(floor, flyWing.flyWing);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void handleTreasuresEvents() {
		statesUpdate.updateStatesB(STATES_TREASURE_EVENT, xTouch, yTouch, layer03[yTouch][xTouch], floor);
	}

	private void handleEnemyEvents() {
		statesUpdate.updateStatesB(STATES_ENEMY_EVENT, xTouch, yTouch, layer02[yTouch][xTouch], floor);
//		Log.i(TAG, "Handling enemy events with " + layer02[yTouch][xTouch]);
	}

	private void handleNPCEvents() {
		statesUpdate.updateStatesB(STATES_NPC_EVENT, xTouch, yTouch, layer01[yTouch][xTouch], floor);
	}

	private int[] getReachableNeighbor(int xTouch, int yTouch) {
		// If the target position is the npc, enemy, or treasures, get the reachable neighbor of the target position.
		if (((xTouch - 1) == warrior.getX() && (yTouch) == warrior.getY()) || canReach(layerMerged , xTouch - 1, yTouch, warrior.getX(), warrior.getY())){
			return new int[]{xTouch - 1, yTouch};
		}else if ((xTouch == warrior.getX() && (yTouch - 1) == warrior.getY()) || canReach(layerMerged, xTouch, yTouch - 1, warrior.getX(), warrior.getY())){
			return new int[]{xTouch, yTouch - 1};
		}else if ((xTouch == warrior.getX() && (yTouch + 1) == warrior.getY()) || canReach(layerMerged, xTouch, yTouch + 1, warrior.getX(), warrior.getY())){
			return new int[]{xTouch, yTouch + 1};
		}else if (((xTouch + 1) == warrior.getX() && yTouch == warrior.getY()) || canReach(layerMerged, xTouch + 1, yTouch, warrior.getX(), warrior.getY())){
			return new int[]{xTouch + 1, yTouch};
		}
		return null;

	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		gameThread = new MainThread(getHolder(),this);
		if (gameThread.getState() == Thread.State.NEW){
			gameThread.setRunning(true);
			gameThread.start();
		}
		gameThread.setFPS(FPS);
		screenWidth = getWidth();
		screenHeight = getHeight();
		getPixelSize();
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
		screenWidth = i1;
		screenHeight = i2;
		getPixelSize();
	}

	private void getPixelSize() {
		xCount = mapData.getxCount(); //15 horizontal
		yCount = mapData.getyCount();//12 vertical
		xBlockSize = screenWidth / (xCount);
		yBlockSize = screenHeight / (yCount);// This is because the screen orientation is landscape
//		Log.i(TAG, "The view width is: " + screenWidth + "and the height is: " + screenHeight +
//			". The pixels of the game is set to X " + xCount + "by Y: " + yCount +
//			". The x block size is: " + xBlockSize + ", and the y block size is:" + yBlockSize);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		boolean retry = true;
		while(retry)
		{
			try{
				gameThread.setRunning(false);
				gameThread.join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			retry = false;
		}
	}

	private void drawLayer0(Canvas canvas) {
		for (int y = 0; y < yCount; y++){
			for (int x = 0; x < xCount; x++){
				if (layer00[y][x] != 0 && layer00[y][x] <= 45) {
					canvas.drawBitmap(background[0][layer00[y][x] - 1], null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
				}else{
					if (layer00[y][x] != 0){
						int ints = (layer00[y][x] - 286)/4;
						canvas.drawBitmap(background2[index % 4][ints], null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
					}
				}
			}
		}
	}

	private void drawLayer1(Canvas canvas) {
		for (int y = 0; y < yCount; y++){
			for (int x = 0; x < xCount; x++){
				if (layer01[y][x] != 0){
					if (layer01[y][x] >= 338 && layer01[y][x] <= 393){
						int ints = (layer01[y][x] - 286)/4;
						canvas.drawBitmap(background2[index % 4][ints], null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
					} else canvas.drawBitmap(npcs[index % 2][(layer01[y][x] - 1 - 45)/2], null,  new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
				}
			}
		}
	}

	private void drawLayer2(Canvas canvas) {
		for (int y = 0; y < yCount; y++){
			for (int x = 0; x < xCount; x++){
				if (layer02[y][x] != 0){
					canvas.drawBitmap(enemies[index % 2][(layer02[y][x] - 1 - 101)/2], null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
				}
			}
		}
	}

	private void drawLayer3(Canvas canvas) {
		for (int y = 0; y < yCount; y++){
			for (int x = 0; x < xCount; x++){
				if (layer03[y][x] != 0){
					int ints = layer03[y][x] - 222;
					canvas.drawBitmap(treasures[(ints%4)][(ints/4)],null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
				}
			}
		}
	}

	public int getStatesBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public void setDrawMap(boolean drawMap) {
		this.drawMap = drawMap;
	}
}
