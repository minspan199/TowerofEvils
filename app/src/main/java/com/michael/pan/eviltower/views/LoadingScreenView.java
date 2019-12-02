package com.michael.pan.eviltower.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.michael.pan.eviltower.R;

public class LoadingScreenView extends View implements Runnable{

	private int index = 0;
	private boolean running = false;
	Thread thread;
	private Paint mPaint;
	private int mWidth, mHeight;
	private float wBlockSize, hBlockSize;
	private int blockNumber = 30;
	private float x;
	private float y;
	private String TAG = "LoadingScreenView: ";
	private Rect r = new Rect();

	public LoadingScreenView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		thread = new Thread(this);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(getResources().getColor(R.color.white));
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(80);
		mPaint.setAlpha(0);

	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		wBlockSize = w/blockNumber;
		hBlockSize = h/blockNumber;
//		Log.i(TAG, "LoadingView size changed: " + mWidth + " by " + mHeight);
	}

	void update(){
		if (mPaint.getAlpha() < 255) mPaint.setAlpha(8 * index);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//canvas.getClipBounds(r);
		mPaint.getTextBounds(getContext().getString(R.string.loading_game), 0, getContext().getString(R.string.loading_game).length(), r);
		x = mWidth / 2f - r.width() / 2f - r.left;
		y = mHeight / 2f + r.height() / 2f - r.bottom;
		canvas.drawText(getContext().getString(R.string.loading_game), x, y, mPaint);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.stopRunning();
	}

	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(1000/(blockNumber));
				index ++;
				this.update();
				this.invalidate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setRunning() {
		this.running = true;
		thread.start();
	}

	public void stopRunning(){
		this.running = false;
	}
}
