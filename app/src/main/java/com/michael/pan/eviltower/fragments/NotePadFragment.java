package com.michael.pan.eviltower.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.adapters.FloorSelectorAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;

import static com.michael.pan.eviltower.activities.StartGameActivity.flyWingData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.activities.StartGameActivity.treasuresJSON;
import static com.michael.pan.eviltower.activities.StartGameActivity.warrior;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_X;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_POSITION_Y;

public class NotePadFragment extends Fragment {

	private Context context;
	private String type;
	private NotebookClose notebookClose;

	public NotePadFragment(Context context, NotebookClose windowCloseCallback, String type) {
		this.context = context;
		this.type = type;
		this.notebookClose = windowCloseCallback;
		gameView.handlingNotePadWindow = true;
	}

	public interface NotebookClose{
		void CloseButtonClicked(String s);
	}
	@SuppressLint("SetTextI18n")
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.notebook_view, container, false);
		TextView notes = rootView.findViewById(R.id.notebook_text_view);
		TextView title = rootView.findViewById(R.id.notebook_title);
		Bundle bundle = getArguments();
		Button buttonOK = rootView.findViewById(R.id.notebook_ok);
		GridView gridView = rootView.findViewById(R.id.notebook_grid_view);
		String noteContents = context.getString(R.string.notebook_content);
		if (bundle!=null){}

		if (type.equals(context.getString(R.string.notebook))){
			title.setText(type);
			buttonOK.setVisibility(View.GONE);
			gridView.setVisibility(View.GONE);
			notes.setText(Html.fromHtml(noteContents));
			notes.setMovementMethod(new ScrollingMovementMethod());//set the textview can be scrolled.
		} else if (type.equals(context.getString(R.string.smile_coin))){
			buttonOK.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
			int count = 0;
			title.setText(context.getString(R.string.smile_coin_text));
			try {
				count = Integer.parseInt(treasuresJSON.getString(context.getString(R.string.smile_coin)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			updateNotes(notes, count);
		} else if (type.equals(context.getString(R.string.fly_wing))){
			title.setText(context.getString(R.string.fly_wing_text));
			buttonOK.setText("Cancel");
			buttonOK.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.VISIBLE);
			notes.setVisibility(View.GONE);
			FloorSelectorAdapter adapter = new FloorSelectorAdapter(context);
			adapter.setFlyWingData(flyWingData);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener((adapterView, view, position, l) -> {
//				AnimUtil.sendFlyMessage(title, "position"+position+"  "+l);
				int x = 0, y = 0, floor = 0;
				try {
					JSONObject jsonObject = adapter.flyWingData.getJSONObject(position);
					x = jsonObject.getInt(COLUMN_POSITION_X);
					y = jsonObject.getInt(COLUMN_POSITION_Y);
					floor = jsonObject.getInt(COLUMN_NAME);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				gameView.setFloor(floor);
				warrior.setX(x);
				warrior.setY(y);
			});
		} else if (type.equals(context.getString(R.string.blessing_gift))){
			title.setText(context.getString(R.string.blessing_title));
			buttonOK.setText(getString(R.string.ok));
			int count = 0;
			try {
				count = Integer.parseInt(treasuresJSON.getString(context.getString(R.string.blessing_gift)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String blessText = "";

			if (count > 0){
				try {
					gameLiveData.setTreasureJson(gameLiveData.getTreasureJson().put(type, String.valueOf(count - 1)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				switch (new Random().nextInt(3)){
					case 0:
						blessText = getString(R.string.bless_100_energy);
						gameLiveData.setEnergy(gameLiveData.getEnergy() + 100);
						break;
					case 1:
						blessText = getString(R.string.bless_20_coins);
						gameLiveData.setCoins(gameLiveData.getCoins() + 20);
						break;
					case 2:
						blessText = getString(R.string.bless_3_attack);
						gameLiveData.setAttack(gameLiveData.getAttack() + 3);
						break;
					case 3:
						blessText = getString(R.string.bless_3_defense);
						gameLiveData.setDefense(gameLiveData.getDefense() + 3);
						break;
				}
			}
			notes.setText(blessText);
		}

		rootView.findViewById(R.id.notebook_close).setOnClickListener(view -> notebookClose.CloseButtonClicked(type));
		buttonOK.setOnClickListener(view -> {
			if (type.equals(context.getString(R.string.smile_coin))){
				try {
					UpdateTreasureJson(notes);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else notebookClose.CloseButtonClicked(type);
		});
		return rootView;
	}

	private void updateNotes(TextView notes, int count) {
		if (count > 0) notes.setText(String.format(Locale.US, "%s%d%s", context.getString(R.string.you_have), count, context.getString(R.string.use_smile_coin)));
		else notes.setText(context.getString(R.string.no_smile_coin));
	}

	private void UpdateTreasureJson(TextView notes) throws JSONException {
		int count = 0;
		JSONObject json = gameLiveData.getTreasureJson();
		if (!json.isNull(type)) {
			count = Integer.parseInt(json.getString(type));
			if (count > 0) {
				gameLiveData.setTreasureJson(gameLiveData.getTreasureJson().put(type, String.valueOf(count - 1)));
				gameLiveData.setCoins(gameLiveData.getCoins() + 100);
			} else gameLiveData.setTreasureJson(gameLiveData.getTreasureJson().put(type, String.valueOf(0)));
		}
		updateNotes(notes, count - 1);
		if (count == 0) notebookClose.CloseButtonClicked(type);
	}
}
