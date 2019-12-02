package com.michael.pan.eviltower.activities;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import androidx.lifecycle.MutableLiveData;

import com.michael.pan.eviltower.views.GameView;

public class MainThread extends Thread
{
	public int getFPS() {
		return FPS;
	}

	public void setFPS(int FPS) {
		this.FPS = FPS;
	}

	private int FPS = 10;
	private final SurfaceHolder surfaceHolder;
	private GameView gamePanel;
	private boolean canRun;
	private MutableLiveData<String[]> currentName = new MutableLiveData<>();

	public MainThread(SurfaceHolder surfaceHolder, GameView gameView)
	{
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gameView;
	}
	@Override
	public synchronized void run()
	{
		long startTime, timeMillis, waitTime, totalTime = 0;
		int frameCount = 0;
		long targetTime = 1000/FPS;
		Canvas canvas = null;

		while(canRun) {
			startTime = System.currentTimeMillis();
			//try locking the canvas for pixel editing
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					this.gamePanel.draw(canvas);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				if(canvas !=null)
				{try {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
				catch(Exception e){e.printStackTrace();}
				}
			}
			timeMillis = System.currentTimeMillis() - startTime;
			waitTime = targetTime-timeMillis;
			try{
				sleep(waitTime);
			}catch(Exception ignored){}
			totalTime += System.currentTimeMillis()-startTime;
			frameCount++;
			if(frameCount == FPS)
			{
				double averageFPS = 1000 / (totalTime / frameCount);
				frameCount =0;
				totalTime = 0;
				//System.out.println(averageFPS);
			}
		}
	}
	public void setRunning(boolean b)
	{
		canRun = b;
	}
}