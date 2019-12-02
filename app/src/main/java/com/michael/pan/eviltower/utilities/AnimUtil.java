package com.michael.pan.eviltower.utilities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.activities.StartGameActivity;
import com.michael.pan.eviltower.data.DataSaving;
import com.michael.pan.eviltower.databinding.UserInfoLayoutBinding;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;

public class AnimUtil {

	public static Animation getTransitionAnim(Context context, String animType){

		Animation aniFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
		Animation aniFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
		switch (animType){
			case "fadingIn":
				return aniFadeIn;
			case "fadingOut":
				return aniFadeOut;

		}
		return aniFadeIn;
	}

	public static void sendFlyMessageByType(@Nullable Context context, View view, int type) {

		if (type == 0){
			ObjectAnimator animAlphaOut = ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(3000);
			ObjectAnimator animTransOut = ObjectAnimator.ofFloat(view, "translationY", 0f, -50f).setDuration(2000);
			final AnimatorSet setIn = new AnimatorSet();
			setIn.play(animAlphaOut).with(animTransOut);
			view.setOnClickListener(view1 -> {
				setIn.cancel();
				view1.clearAnimation();
				view1.setVisibility(View.GONE);
				gameLiveData.setCoins(gameLiveData.getCoins() + 50);
				DataSaving.saveBonusPreferences(context);
			});
			setIn.start();
			new Handler().postDelayed(() -> view.setVisibility(View.GONE), 10000);
		} else if (type == 1){
			ObjectAnimator animTransOut = ObjectAnimator.ofFloat(view, "translationY", 0f, -50f).setDuration(1000);
			final AnimatorSet setIn = new AnimatorSet();
			setIn.play(animTransOut);
			view.setOnClickListener(view1 -> {
				setIn.cancel();
				view1.clearAnimation();
				view1.setVisibility(View.GONE);
			});
			setIn.start();
		}
		view.setVisibility(View.VISIBLE);
	}

	public static void sendFlyMessage(View view, String text) {
		TextView v = (TextView) view;
		v.setText(text);
		sendFlyMessage(v);
	}

	public static void sendFlyMessage(View view, String text, int color) {
		TextView v = (TextView) view;
		v.setTextColor(color);
		v.setText(text);
		sendFlyMessage(v);
	}

	public static void sendFlyMessage(View view, String text, int color, int type) {
		TextView v = (TextView) view;
		v.setTextColor(color);
		v.setText(text);
		sendFlyMessageByType(null, v, type);
	}

	private static void sendFlyMessage(View view){

		ObjectAnimator animAlphaIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1).setDuration(800);
		ObjectAnimator animTransIn = ObjectAnimator.ofFloat(view, "translationY", 50f, 0f).setDuration(700);

		ObjectAnimator animAlphaOut = ObjectAnimator.ofFloat(view, "alpha", 1, 1f).setDuration(3000);
		ObjectAnimator animTransOut = ObjectAnimator.ofFloat(view, "translationY", 0f, 150f).setDuration(2000);

		final AnimatorSet setIn = new AnimatorSet();
		setIn.play(animAlphaIn).with(animTransIn);

		AnimatorSet setOut = new AnimatorSet();
		setOut.play(animAlphaOut).with(animTransOut);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(setIn).before(setOut);
		animatorSet.start();
		view.setVisibility(View.VISIBLE);
//		anim.setAnimationListener(new Animation.AnimationListener() {
//			@Override
//			public void onAnimationStart(Animation animation) {}
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				view.setAnimation(animSlideOut);
//			}
//			@Override
//			public void onAnimationRepeat(Animation animation) {}
//		});
//		animSlideOut.setAnimationListener(new Animation.AnimationListener() {
//			@Override
//			public void onAnimationStart(Animation animation) {}
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				view.setVisibility(View.GONE);
//			}
//			@Override
//			public void onAnimationRepeat(Animation animation) {}
//		});new Handler(Looper.getMainLooper()).post(() ->
		view.setOnClickListener(view1 ->  {
			animatorSet.cancel();
			view.clearAnimation();
			view.setVisibility(View.GONE);
		});

	}

	public static void fabClosingAnimation(Context context, UserInfoLayoutBinding userInfoLayout) {
		Animation animClose = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fab_close);
		StartGameActivity.isFabOpen = false;
		userInfoLayout.actionScreenshot.animate().translationY(context.getResources().getDimension(R.dimen.standard_0));
		userInfoLayout.actionScreenshot.animate().translationX(context.getResources().getDimension(R.dimen.standard_0));
		userInfoLayout.actionSave.animate().translationY(context.getResources().getDimension(R.dimen.standard_0));
		userInfoLayout.actionReload.animate().translationX(context.getResources().getDimension(R.dimen.standard_0));
		userInfoLayout.actionOpenList.animate().rotation(0);
//		if (android.os.Build.VERSION.SDK_INT >= 15) {
		if (userInfoLayout.actionScreenshot.getVisibility() == View.VISIBLE) userInfoLayout.actionScreenshot.startAnimation(animClose);

		userInfoLayout.actionSave.startAnimation(animClose);
		userInfoLayout.actionReload.startAnimation(animClose);
//		}
//		else {
//			userInfoLayout.actionReload.animate().setDuration(200).alpha(0);
//			userInfoLayout.actionScreenshot.animate().setDuration(200).alpha(0);
//			userInfoLayout.actionSave.animate().setDuration(200).alpha(0);
//			userInfoLayout.actionReload.animate().scaleX(0);
//			userInfoLayout.actionScreenshot.animate().scaleX(0);
//			userInfoLayout.actionSave.animate().scaleX(0);
//			userInfoLayout.actionReload.animate().scaleY(0);
//			userInfoLayout.actionScreenshot.animate().scaleY(0);
//			userInfoLayout.actionSave.animate().scaleY(0);
//		}
	}

	public static void fabOpeningAnimation(Context context, UserInfoLayoutBinding userInfoLayout) {
		Animation animOpen = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fab_open);
		StartGameActivity.isFabOpen = true;
		userInfoLayout.actionReload.animate().setDuration(400).translationX(context.getResources().getDimension(R.dimen.standard_60));
		userInfoLayout.actionScreenshot.animate().setDuration(600).translationX(context.getResources().getDimension(R.dimen.standard_60));
		userInfoLayout.actionScreenshot.animate().setDuration(800).translationY(context.getResources().getDimension(R.dimen.standard_60));
		userInfoLayout.actionSave.animate().setDuration(1000).translationY(context.getResources().getDimension(R.dimen.standard_60));
		userInfoLayout.actionOpenList.animate().rotation(-45);
//		if (android.os.Build.VERSION.SDK_INT >= 15){
		if (userInfoLayout.actionScreenshot.getVisibility() == View.VISIBLE) userInfoLayout.actionScreenshot.startAnimation(animOpen);

		userInfoLayout.actionSave.startAnimation(animOpen);
		userInfoLayout.actionReload.startAnimation(animOpen);
			// Do something for lollipop and above versions
//		} else {
//			userInfoLayout.actionScreenshot.animate().setDuration(200).alpha(1);
//			userInfoLayout.actionReload.animate().setDuration(200).alpha(1);
//			userInfoLayout.actionSave.animate().setDuration(200).alpha(1);
//			userInfoLayout.actionReload.animate().scaleX(1);
//			userInfoLayout.actionScreenshot.animate().scaleX(1);
//			userInfoLayout.actionSave.animate().scaleX(1);
//			userInfoLayout.actionReload.animate().scaleY(1);
//			userInfoLayout.actionScreenshot.animate().scaleY(1);
//			userInfoLayout.actionSave.animate().scaleY(1);
//		}
	}
}
