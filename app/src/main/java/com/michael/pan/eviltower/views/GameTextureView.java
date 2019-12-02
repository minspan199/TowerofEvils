//package com.michael.pan.eviltower.views;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.RectF;
//import android.graphics.SurfaceTexture;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.TextureView;
//import android.view.View;
//
//import com.michael.pan.eviltower.activitities.MainThread;
//import com.michael.pan.eviltower.R;
//import com.michael.pan.eviltower.Utilities.AlgorithmUtil;
//import com.michael.pan.eviltower.Utilities.ImageUtil;
//import com.michael.pan.eviltower.Utilities.Node;
//import com.michael.pan.eviltower.data.FloorData;
//
//import java.util.ArrayList;
//
//import static com.michael.pan.eviltower.activitities.StartGameActivity.door;
//import static com.michael.pan.eviltower.activitities.StartGameActivity.mapData;
//import static com.michael.pan.eviltower.activitities.StartGameActivity.mapDataInArrays;
//import static com.michael.pan.eviltower.activitities.StartGameActivity.warrior;
//import static com.michael.pan.eviltower.Utilities.AlgorithmUtil.canReach;
//
//public class GameTextureView extends SurfaceTexture implements TextureView.SurfaceTextureListener, Runnable {
//
//	private static final String TAG = "GameView: ";
//	public int[][] layer00, layer01, layer02, layer03, layerMerged;
//
//
//	private static int index = 0, counter = 0;
//	private String direction;
//	private int floor;
//	private float screenWidth, screenHeight, xBlockSize, yBlockSize;
//	private int xTouch, yTouch, xCount, yCount;
//	public FloorData currentFloorData;
//	MainThreadTexture gameThread;
//	Bitmap[][] background, npcs, enemies, treasures, background2;
//	public boolean drawMap = true, drawLayerAnim = false, fadeOut = false;
//	private boolean isRunning = true;
//	private float fps = 5;
//
//	public void updateStatesA() {
//		loadMapData(floor);
//	}
//
//	public interface updateStatesA{
//		void updateStatesA(String s);
//	}
//
//	public GameTextureView(int texName, Context context, updateStatesA mUpdateStatusBar) {
//		super(texName);
//		this.mUpdateStatusBar = mUpdateStatusBar;
//		gameThread = new MainThreadTexture(this);
//		background = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.terrains), 1, 45);
//		npcs = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.npcs), 2, 28);
//		enemies = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemys), 2, 60);
//		treasures = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.treasures), 4, 16);
//		background2 = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.animates), 4, 27);
//		layer00 = null;layer01 = null; layer02 = null; layer03 = null;
//		SurfaceTexture surfaceTexture =
//	}
//
//	@Override
//	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
//		this.isRunning = true;
//		this.run();
//		screenWidth = i;
//		screenHeight = i1;
//		getPixelSize();
//	}
//
//	@Override
//	public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
//
//	}
//
//	@Override
//	public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//		return false;
//	}
//
//	@Override
//	public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//
//	}
//
//	@Override
//	public void run() {
//		Canvas canvas;
//
//		while (isRunning) {
//
//			long currentTimeMillis = System.currentTimeMillis();
//			long elapsedTimeMs = currentTimeMillis - previousTime;
//			long sleepTimeMs = (long) (1000f/ fps - elapsedTimeMs);
//
//			canvas = null;
//			try {
//
//				canvas = surfaceHolder.lockCanvas();
//
//				if (canvas == null) {
//					Thread.sleep(1);
//
//					continue;
//
//				}else if (sleepTimeMs > 0){
//
//					Thread.sleep(sleepTimeMs);
//
//				}
//
//				synchronized (surfaceHolder) {
//					sceneComposer.drawOn(canvas);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (canvas != null) {
//					surfaceHolder.unlockCanvasAndPost(canvas);
//					previousTime = System.currentTimeMillis();
//				}
//			}
//		}
//	}
//
//
//	private updateStatesA mUpdateStatusBar;
//
//
//
//	public void setFloor(int f) {
//		floor = f;
//		loadMapData(floor);
//	}
//
//
//	private void loadMapData(int floor) {
//		System.out.println("testB!!!!!");
//		currentFloorData = mapDataInArrays.get(floor);
//		layer00 = currentFloorData.getLayer00();
//		layer01 = currentFloorData.getLayer01();
//		layer02 = currentFloorData.getLayer02();
//		layer03 = currentFloorData.getLayer03();
//		layerMerged = currentFloorData.getMergedLayer();
//
//		//Log.i(TAG, "Loaded floor data layers, layer00:" + Arrays.deepToString(layer00) + "; layer01:" + Arrays.deepToString(layer01) +
//		//	"; layer02:" + Arrays.deepToString(layer02) + "; layer03:" + Arrays.deepToString(layer03));
//	}
//
//
//
//	@Override
//	public void draw(Canvas canvas) {
//		super.draw(canvas);
//		if (counter <= 3){
//			counter ++;
//		}else {
//			counter = 0;
//			if (index > 2) index = 0;
//			else index++;
//		}
//		if(drawMap) {
//			if (layer00 != null) drawLayer0(canvas);
//			if (layer01 != null) drawLayer1(canvas);
//			if (layer02 != null) drawLayer2(canvas);
//			if (layer03 != null) drawLayer3(canvas);
//			warrior.draw(canvas, xBlockSize, yBlockSize);
//			if (door != null && door.canDraw()) {
//				door.draw(canvas, xBlockSize, yBlockSize);
//			}
//		}
//
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			//calculate the touch event coordinates.
//			xTouch = (int) (event.getX()/xBlockSize);
//			yTouch = (int) (event.getY()/yBlockSize);
//			//if (xTouch >= yCount || yTouch >= xCount) return true;
//			System.out.println("Clicked on position x: " + xTouch + " and y: " + yTouch + " the destination is: " + layerMerged[yTouch][xTouch]);
//			//direction = AlgorithmUtil.getDirection(xTouch, yTouch, warrior.getX(), warrior.getY());
//			//warrior.setStatus(direction, true);
//			if (xTouch != warrior.getX() || yTouch != warrior.getY()){//detected warrior position needs to change
//				ArrayList<Node> nodes = null;
//				if (layerMerged[yTouch][xTouch] == 0 || layerMerged[yTouch][xTouch] == 1) {
//					nodes = AlgorithmUtil.getPath(layerMerged, warrior.getY(), warrior.getX(), yTouch, xTouch);
//					//interchange the coordinates due to the index of the 2D array
//					//the getPath method doesn't modify the 2D array object so it can be passed here.
//				}else if (layerMerged[yTouch][xTouch] == 100 || layerMerged[yTouch][xTouch] == 200 || layerMerged[yTouch][xTouch] == 300 ){
//					int[] neighbor = getReachableNeighbor(xTouch, yTouch);
//					if (neighbor != null){
//						Log.i(TAG, "Destination is NPC, finding neighboring position, X:" + neighbor[0] + ", Y:"+ neighbor[1] );
//						nodes = AlgorithmUtil.getPath(layerMerged, warrior.getY(), warrior.getX(), neighbor[1], neighbor[0]);
//						//interchange the coordinates due to the index of the 2D array
//					}
//				}
//
//				if (nodes != null){
//					warrior.updateLocation(nodes);
//					Log.i(TAG, "Find a route: "+ nodes.toString());
//					while (true){
//						try {
//							Thread.sleep((long) (1000/gameThread.getFPS()));
//							if (!warrior.getMoving()){
//								handleEncounterEvents(xTouch, yTouch);
//								break;
//							}
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				} else {
//					mUpdateStatusBar.updateStatesA("No path");
//					Log.i(TAG, "No route to get there.");
//				}
//			}
//		}
//		return super.onTouchEvent(event);
//	}
//
//	private void handleEncounterEvents(int xTouch, int yTouch) {
//		Log.i(TAG, "Handling encounter events at X: " + xTouch + ", Y: " + yTouch);
//		switch (layerMerged[yTouch][xTouch]){
//			case 100:
//				handleNPCEvents();
//				break;
//			case 200:
//				handleEnemyEvents();
//				break;
//			case 300:
//				handleTreasuresEvents();
//				break;
//		}
//	}
//
//	private void handleTreasuresEvents() {
//
//	}
//
//	private void handleEnemyEvents() {
//
//	}
//
//	private void handleNPCEvents() {
//		switch (layer01[yTouch][xTouch]) {
//			case 24:
//				mUpdateStatusBar.updateStatesA("downstairs");
//				break;
//			case 25:
//				mUpdateStatusBar.updateStatesA("upstairs");
//				break;
//			case 46:
//				mUpdateStatusBar.updateStatesA("experience" + floor);
//				break;
//			case 47:
//				mUpdateStatusBar.updateStatesA("experience-visited" + floor);
//				break;
//			case 48:
//				mUpdateStatusBar.updateStatesA("key-merchant" + floor);
//				break;
//			case 49:
//				mUpdateStatusBar.updateStatesA("key-merchant-visited" + floor);
//				break;
//			case 50:
//				mUpdateStatusBar.updateStatesA("technician" + floor);
//				break;
//			case 52:
//				mUpdateStatusBar.updateStatesA("angel" + floor);
//				break;
//			case 53:
//				mUpdateStatusBar.updateStatesA("angel-visited" + floor);
//				break;
//			case 54:
//				mUpdateStatusBar.updateStatesA("wizard-blue" + floor);
//				break;
//			case 64:
//				mUpdateStatusBar.updateStatesA("large-store" + floor);
//				break;
//			case 66:
//				mUpdateStatusBar.updateStatesA("large-store" + floor);
//				break;
//		}
//	}
//
//	private int[] getReachableNeighbor(int xTouch, int yTouch) {
//		// If the target position is the npc, enemy, or treasures, get the reachable neighbor of the target position.
//		if (canReach(layerMerged , xTouch - 1, yTouch, warrior.getX(), warrior.getY())){
//			return new int[]{xTouch - 1, yTouch};
//		}else if (canReach(layerMerged, xTouch, yTouch - 1, warrior.getX(), warrior.getY())){
//			return new int[]{xTouch, yTouch - 1};
//		}else if (canReach(layerMerged, xTouch, yTouch + 1, warrior.getX(), warrior.getY())){
//			return new int[]{xTouch, yTouch + 1};
//		}else if (canReach(layerMerged, xTouch + 1, yTouch, warrior.getX(), warrior.getY())){
//			return new int[]{xTouch + 1, yTouch};
//		}
//		return null;
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder surfaceHolder) {
//
//	}
//
//
//	@Override
//	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//		//screenWidth = i1;
//		//screenHeight = i2;
//		getPixelSize();
//	}
//
//	private void getPixelSize() {
//		xCount = mapData.getxCount(); //15 horizontal
//		yCount = mapData.getyCount();//12 vertical
//		xBlockSize = screenWidth / (xCount);
//		yBlockSize = screenHeight / (yCount);// This is because the screen orientation is landscape
//		Log.i(TAG, "The view width is: " + screenWidth + "and the height is: " + screenHeight +
//			". The pixels of the game is set to X " + xCount + "by Y: " + yCount +
//			". The x block size is: " + xBlockSize + ", and the y block size is:" + yBlockSize);
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//		boolean retry = true;
//		while(retry)
//		{
//			try{
//				gameThread.setRunning(false);
//				gameThread.join();
//			}catch(InterruptedException e){
//				e.printStackTrace();
//			}
//			retry = false;
//		}
//	}
//
//	private void drawLayer0(Canvas canvas) {
//
//		for (int y = 0; y < yCount; y++){
//			for (int x = 0; x < xCount; x++){
//				if (layer00[y][x] != 0 && layer00[y][x] <= 45) {
//					canvas.drawBitmap(background[0][layer00[y][x] - 1], null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
//				}else{
//					int ints = (layer00[y][x] - 286)/4;
//					canvas.drawBitmap(background2[index % 4][ints], null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
//				}
//			}
//		}
//	}
//
//	private void drawLayer1(Canvas canvas) {
//		for (int y = 0; y < yCount; y++){
//			for (int x = 0; x < xCount; x++){
//				if (layer01[y][x] != 0){
//					canvas.drawBitmap(npcs[index % 2][(layer01[y][x] - 1 - 45)/2], null,  new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
//				}
//			}
//		}
//	}
//
//	private void drawLayer2(Canvas canvas) {
//		for (int y = 0; y < yCount; y++){
//			for (int x = 0; x < xCount; x++){
//				if (layer02[y][x] != 0){
//					canvas.drawBitmap(enemies[index % 2][(layer02[y][x] - 1 - 101)/2], null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
//				}
//			}
//		}
//	}
//
//	private void drawLayer3(Canvas canvas) {
//		for (int y = 0; y < yCount; y++){
//			for (int x = 0; x < xCount; x++){
//				if (layer03[y][x] != 0){
//					int ints = layer03[y][x] - 222;
//					canvas.drawBitmap(treasures[(ints%4)][(ints/4)],null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
//				}
//			}
//		}
//	}
//
//	public int getStatesBarHeight() {
//		int result = 0;
//		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//		if (resourceId > 0) {
//			result = getResources().getDimensionPixelSize(resourceId);
//		}
//		return result;
//	}
//}
