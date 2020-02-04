package com.michael.pan.eviltower.activities;


import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.preference.PreferenceManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.services.BonusWorkViewModel;
import com.michael.pan.eviltower.services.LocaleManager;

import static androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_HIGH_BOUNCY;
import static androidx.dynamicanimation.animation.SpringForce.STIFFNESS_LOW;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_BOOL_NEW_GAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_CLICKED_LOAD_AT_MAIN;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_CLICKED_NEW_AT_MAIN;

public class MainActivity extends AppCompatActivity {

	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;
	private Button start_button, load_button, quit_button;
	SharedPreferences sharedPreferences;
	private SpringAnimation springAnimation;
	private YoYo.YoYoString control, settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//turn title off
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//set to full screen
		ImageView mota_welcome = findViewById(R.id.mota_welcome_image);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		mota_welcome.setAnimation(animation);
		ButtonInitialization();
		AnimationMake();
		RequestPermission();
//        startActivity(new Intent(this, GameOverActivity.class));
		//sharedPreferences.registerOnSharedPreferenceChangeListener(this);
//        bonusWorkViewModel = ViewModelProviders.of(this).get(BonusWorkViewModel.class);
//        if (sharedPreferences.getBoolean(getString(R.string.pref_bonus_coin_key), false)) {
//            bonusWorkViewModel.applyBonusNotify();
//
////            bonusWorkViewModel.mSavedWorkInfo.observe(this, workInfo -> {
////                // Note that these next few lines grab a single WorkInfo if it exists
////                // This code could be in a Transformation in the ViewModel; they are included here
////                // so that the entire process of displaying a WorkInfo is in one location.
////                // If there are no matching work info, do nothing
////                if (workInfo != null) Log.i(TAG, "Work status:" + workInfo.getState().toString());
////            });
//        }else {
//            bonusWorkViewModel.CancelCoinBonus();
//        }
	}

	private void RequestPermission() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
			} else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

				// MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		} else {
			// Permission has already been granted
		}
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		//this will get called before onCreate
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase);
		super.attachBaseContext(LocaleManager.updateResources(newBase, getLanguagePreference(newBase)));
	}

	private String getLanguagePreference(Context context) {
		return sharedPreferences.getString(context.getString(R.string.pref_lan_key), context.getString(R.string.pref_lan_english_value));
		//fontSizePreference = sharedPreferences.getString(context.getString(R.string.pref_size_key), context.getString(R.string.pref_size_default));
	}

	private void AnimationMake() {
//        mShowButton = AnimationUtils.loadAnimation(this,R.anim.button_animation);
//	    start_button.setAnimation(mShowButton);
//        load_button.setAnimation(mShowButton);
//        quit_button.setAnimation(mShowButton);
		springAnimation = new SpringAnimation(findViewById(R.id.game_title), DynamicAnimation.TRANSLATION_Y, 10).setStartVelocity(100).setStartValue(-200);
		springAnimation.getSpring().setStiffness(STIFFNESS_LOW).setDampingRatio(DAMPING_RATIO_HIGH_BOUNCY);
		springAnimation.start();
	}

	private void ButtonInitialization() {
		start_button = findViewById(R.id.new_button);
		load_button = findViewById(R.id.load_button);
		quit_button = findViewById(R.id.quit_button);
		View.OnClickListener listener = v -> {
			Intent intent = new Intent(MainActivity.this, UserListActivity.class);
			control = YoYo.with(Techniques.ZoomOutRight)
				.duration(500)
				.repeat(YoYo.ONETIME)
				.pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
				.interpolate(new AccelerateDecelerateInterpolator())
				.withListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
					}

					@Override
					public void onAnimationEnd(Animator animator) {
						switch (v.getId()) {
							case R.id.new_button:
								intent.putExtra(EXTRA_BOOL_NEW_GAME, EXTRA_CLICKED_NEW_AT_MAIN);
								startActivity(intent);
								break;
							case R.id.load_button:
								intent.putExtra(EXTRA_BOOL_NEW_GAME, EXTRA_CLICKED_LOAD_AT_MAIN);
								startActivity(intent);
								break;
							case R.id.quit_button:
								finish();
								break;
							default:
								break;
						}
						new Handler().postDelayed(() -> control.stop(true), 500);
					}

					@Override
					public void onAnimationCancel(Animator animator) {
					}

					@Override
					public void onAnimationRepeat(Animator animator) {
					}
				})
				.playOn(v);

		};
		start_button.setOnClickListener(listener);
		load_button.setOnClickListener(listener);
		quit_button.setOnClickListener(listener);
	}

	public void settings(View view) {
		settings = YoYo.with(Techniques.Hinge)
			.duration(500)
			.repeat(YoYo.ONETIME)
			.pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
			.interpolate(new AccelerateDecelerateInterpolator())
			.withListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animator) {
				}

				@Override
				public void onAnimationEnd(Animator animator) {
					Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
					startActivityForResult(intent, 0);
				}

				@Override
				public void onAnimationCancel(Animator animator) {
				}

				@Override
				public void onAnimationRepeat(Animator animator) {
				}
			})
			.playOn(view);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LocaleManager.updateResources(this, getLanguagePreference(this));
		startActivity(this.getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));// to clear old activity and start a brand new activity
	}

	private static void setLanguagePref(Context context, String localeKey) {
		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		mPreferences.edit().putString(context.getString(R.string.pref_lan_key), localeKey).apply();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void about_info(View view) {
		Intent intent = new Intent(MainActivity.this, AboutActivity.class);
		startActivity(intent);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission was granted, yay! Do the
					// contacts-related task you need to do.
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request.
		}
	}

}
