package com.michael.pan.eviltower.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.tabs.TabLayout;
import com.michael.pan.autoscrollpagerlib.AutoScrollViewPager;
import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.adapters.AutoScrollPagerAdapter;
import com.michael.pan.eviltower.services.DatabaseUpdateTask;
import com.michael.pan.eviltower.services.LocaleManager;

import org.json.JSONException;
import org.json.JSONObject;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.flyWingData;
import static com.michael.pan.eviltower.activities.StartGameActivity.mapData;
import static com.michael.pan.eviltower.activities.StartGameActivity.mapDataInArrays;
import static com.michael.pan.eviltower.activities.StartGameActivity.door;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.activities.StartGameActivity.treasuresJSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_GAME_FINISHED;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_STATISTICS;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_COMPLETION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USERDATA_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GAME_COMPLETING;
import static com.michael.pan.eviltower.utilities.JSONUtil.getUserDataJson;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameOverActivity extends FragmentActivity implements DatabaseUpdateTask.onTaskExecuted {

	AutoScrollViewPager viewPager;
//	private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 1000;
	private boolean revisit = false;
	String extras;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		viewPager = findViewById(R.id.game_finishing_pager);
		if (getIntent().hasExtra(EXTRA_GAME_FINISHED)) {
			extras = getIntent().getStringExtra(EXTRA_GAME_FINISHED);
			revisit = false;
		}
		if (getIntent().hasExtra(EXTRA_STATISTICS)) {
			extras = getIntent().getStringExtra(EXTRA_STATISTICS);
			revisit = true;
		}
		AutoScrollPagerAdapter adapter = new AutoScrollPagerAdapter(getSupportFragmentManager(), 0, extras);
		viewPager.setAdapter(adapter);
		TabLayout tabs = findViewById(R.id.tabs);
		tabs.setupWithViewPager(viewPager, true);
		// start auto scroll
//		viewPager.startAutoScroll();
		// set auto scroll time in mili
//		viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
		// enable recycling using true
		viewPager.setCycle(false);
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		//this will get called before onCreate
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase);
		String lanPref = sharedPreferences.getString(newBase.getString(R.string.pref_lan_key), newBase.getString(R.string.pref_lan_english_value));
		super.attachBaseContext(LocaleManager.updateResources(newBase, lanPref));
	}

	@Override
	public void onBackPressed() {}

	public void quitGame(View view) {//onClick
		if (revisit) {
			quit();
			return;
		}
		ContentValues cv = new ContentValues();
		JSONObject json = getUserDataJson(gameLiveData.getGameDataList());
		try {
			json.put(COLUMN_COMPLETION, extras);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		cv.put(COLUMN_USERDATA_JSON, json.toString());
//		System.out.println(json.toString());
		new DatabaseUpdateTask(this, cv, this).execute(gameLiveData.userId, TAG_GAME_COMPLETING);
	}

	private void quit() {
		finish();
		treasuresJSON = null;
		gameLiveData = null;
		gameView = null;
		mapData = null;
		door = null;
		flyWingData = null;
		mapDataInArrays = null;
		startActivity(new Intent(this, MainActivity.class));
	}

	@Override
	public void onDatabaseUpdated() {
		quit();
	}

}
