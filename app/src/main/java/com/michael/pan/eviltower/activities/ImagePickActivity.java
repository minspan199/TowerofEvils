package com.michael.pan.eviltower.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.adapters.IconGridAdapter;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.services.DatabaseUpdateTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_USER_POSITION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USERDATA_JSON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USER_ICON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_USER_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_USER_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_USER_NAME;

public class ImagePickActivity extends AppCompatActivity implements IconGridAdapter.onIconClicked, DatabaseUpdateTask.onTaskExecuted{

	private IconGridAdapter iconAdapter;
	private GridView gridView;
	private TextView userInfo;
	private ArrayList<Integer> data;
	private String userId, userData, userName;
	private int userPosition;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pref_user_icon_gridview);
		userId = getIntent().getStringExtra(EXTRA_USER_ID);
		userName = getIntent().getStringExtra(EXTRA_USER_NAME);
		userData = getIntent().getStringExtra(EXTRA_USER_DATA);
		userPosition = getIntent().getIntExtra(EXTRA_USER_POSITION, 0);
		TypedArray userIconDrawables = getResources().obtainTypedArray(R.array.user_icon_drawables);
		iconAdapter = new IconGridAdapter(this, this, userIconDrawables);
		data = new ArrayList<>();
		for (int i = 0; i< 11; i++) data.add(i);
		iconAdapter.swapData(data);
		gridView = findViewById(R.id.user_icon_grid_view);
		gridView.setAdapter(iconAdapter);
		gridView.setVisibility(View.VISIBLE);
		userInfo = findViewById(R.id.user_icon_text);
		userInfo.setText(String.format("%s: %s", getString(R.string.user), userName));
	}

	@Override
	public void iconClicked(int id) {
		JSONObject json = new JSONObject();
		try {
			json = new JSONObject(userData);
			json.put(COLUMN_USER_ICON, String.valueOf(id));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_USERDATA_JSON, json.toString());
		new DatabaseUpdateTask(this, cv, this).execute(userId, EvilTowerContract.TAG_USER_ROW_UPDATE);
		//when clicked to select an icon, the database is updated..
	}

	@Override
	public void onDatabaseUpdated() {
		setResult(Activity.RESULT_OK);
		finish();
		//after updating the database, go back to the original fragment.
	}
}
