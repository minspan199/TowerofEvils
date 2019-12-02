package com.michael.pan.eviltower.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;

public class SnapshotView extends View {

	public SnapshotView(Context context) {
		super(context);

	}

	public SnapshotView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		gameView.drawGameViews(canvas);
	}

}
