package com.michael.pan.eviltower.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.utilities.ImageUtil;
import com.michael.pan.eviltower.utilities.Node;

import java.util.ArrayList;

public class Warrior{

	public String userName;
	// position
	private int x, y, index = 0, targetX, targetY;
	private float xShift = 0, yShift = 0;
	private int[] pathX = null, pathY = null;
	// ifOpen: left, right, up, down, moving
	private String status = "down";
	private Boolean moving = false;
	public boolean isFirstTime = true;
	private static int graphIndex = 0;
	private Bitmap[][] goingUp, goingDown, goingLeft, goingRight;
	private int pathStep = 0;
	public String userId = "";


	public Warrior(Context context) {
		userName = context.getString(R.string.warrior);
		goingDown = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.warrior_down), 4, 1);
		goingUp = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.warrior_up), 4, 1);
		goingLeft = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.warrior_left), 4, 1);
		goingRight = ImageUtil.splitBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.warrior_right), 4, 1);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status, Boolean moving) {
		this.status = status;
		this.moving = moving;
	}

	public Boolean getMoving() {
		return moving;
	}

	private void setMoving(Boolean moving) {
		this.moving = moving;
	}

	private Bitmap getGraph(){
		if (moving) {
			graphIndex = (graphIndex + 1) % 4;
		} else graphIndex = 0;
		switch (status){
			case "up": return goingUp[graphIndex][0];
			case "down": return goingDown[graphIndex][0];
			case "left": return goingLeft[graphIndex][0];
			case "right": return goingRight[graphIndex][0];
			default:return goingDown[0][0];
		}
	}

	public void draw(Canvas canvas, float xBlockSize, float yBlockSize) {
		if (index>=2)	index = 0;
		else {
			index++;
		}
		if (pathX!=null&&pathY!=null){// if the path has been planned and the warrior position needs updates
			if (pathStep < pathX.length - 1) {
				if (pathX[pathStep + 1]>pathX[pathStep]){
					this.setStatus("right", true);
					this.setX(pathX[pathStep + 1]);
//					xShift += xBlockSize/2.0;
				}else if (pathY[pathStep + 1]>pathY[pathStep]){
					this.setStatus("up", true);
					this.setY(pathY[pathStep + 1]);
//					yShift += yBlockSize/2.0;
				}else if (pathX[pathStep + 1]<pathX[pathStep]){
					this.setStatus("left", true);
					this.setX(pathX[pathStep + 1]);
//					xShift -= xBlockSize/ 2.0;
				}else if (pathY[pathStep+1]<pathY[pathStep]){
					this.setStatus("down", true);
					this.setY(pathY[pathStep + 1]);
//					yShift -= yBlockSize/2.0;
				}
//				pathStep ++;
				if (index == 0){
					pathStep++;// set the updateStatesA frequencies of pathStep and pathShift to different values.
				}
			}
			else {
				pathStep = 0;
				this.setX(pathX[pathX.length - 1]);
				this.setY(pathY[pathY.length - 1]);
				xShift = 0;
				yShift = 0;
				pathY = null;
				pathX = null;
				this.setMoving(false);
			}
		}
		//System.out.println("index:"+index);
		canvas.drawBitmap(this.getGraph(), null, new RectF(x*xBlockSize, y*yBlockSize, (x+1)*xBlockSize, (y+1)*yBlockSize), null);
	}

	public void updateLocation(ArrayList<Node> nodes) {
		int l = nodes.size();
		int[] pathX = new int[l], pathY = new int[l];
		Node node;
		for (int i = l - 1; i >= 0; i--){
			node = nodes.get(l - 1 - i);
			pathY[i] = node.x;
			pathX[i] = node.y;
		}
		this.targetX = pathX[l - 1];
		this.targetY = pathY[l - 1];
		this.pathX = pathX;
		this.pathY = pathY;
	}

}
