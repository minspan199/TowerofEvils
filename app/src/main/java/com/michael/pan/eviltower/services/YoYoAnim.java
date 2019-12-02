package com.michael.pan.eviltower.services;

import android.animation.Animator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class YoYoAnim {

	private YoYo.AnimationComposer anim;
	private View view;

	public YoYoAnim(Techniques techniques, View view, long durationMills, Runnable callbacks) {

		this.view = view;
		anim = YoYo.with(techniques)
			.duration(durationMills)
			.repeat(YoYo.ONETIME)
			.pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
			.interpolate(new AccelerateDecelerateInterpolator())
			.withListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animator) {}
				@Override
				public void onAnimationEnd(Animator animator) {
					callbacks.run();
				}
				@Override
				public void onAnimationCancel(Animator animator) {}
				@Override
				public void onAnimationRepeat(Animator animator) {}
			});
	}

	public void start(){
		anim.playOn(view);
	}
}
