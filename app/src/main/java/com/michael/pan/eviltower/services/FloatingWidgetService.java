package com.michael.pan.eviltower.services;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.michael.pan.eviltower.R;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_POSITION_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_TREASURE_JSON_STRING;

public class FloatingWidgetService extends Service implements Runnable{


	private WindowManager windowManager;
	private View floatingView;
	private WindowManager.LayoutParams params;
	private TextView floatingMessage;
	private String extraJSONString;
	private int position;
	private Handler mHandler = new Handler();
	private Runnable mRunnable;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		extraJSONString = intent.getStringExtra(EXTRA_TREASURE_JSON_STRING);
		position = intent.getIntExtra(EXTRA_POSITION_ID, -1);
		setText();//setText after parameter initialization
		return START_NOT_STICKY;
	}


	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public void onCreate() {
		super.onCreate();

		floatingView = LayoutInflater.from(this).inflate(R.layout.floating_window_service, null);
		params = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			PixelFormat.TRANSLUCENT);

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		assert windowManager != null;
		windowManager.addView(floatingView, params);

		floatingMessage = floatingView.findViewById(R.id.floating_message);

	}

	private void setText() {
		floatingView.setVisibility(View.VISIBLE);
		floatingMessage.setText(extraJSONString +"       "+ position);
		floatingMessage.setOnTouchListener(new View.OnTouchListener() {
			int X_Axis, Y_Axis;
			float TouchX, TouchY;
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				switch (motionEvent.getAction()) {
					case MotionEvent.ACTION_MOVE:
						params.x = X_Axis + (int) (motionEvent.getRawX() - TouchX);
						params.y = Y_Axis + (int) (motionEvent.getRawY() - TouchY);
						windowManager.updateViewLayout(floatingView, params);
						return true;
				}
				return false;
			}
		});

		mRunnable = new Runnable() {
			@Override
			public void run() {
				if (floatingView != null){
					floatingView.setVisibility(View.GONE);
					stopSelf();
				}
			}
		};//after a time, dismiss the floating window
		mHandler.removeCallbacksAndMessages(null);
		mHandler.postDelayed(mRunnable, 8000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (floatingView != null) windowManager.removeView(floatingView);
	}

	@Override
	public void run() {

	}
}
