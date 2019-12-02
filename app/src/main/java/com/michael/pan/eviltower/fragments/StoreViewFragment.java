package com.michael.pan.eviltower.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.michael.pan.eviltower.R;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_GIANT_STORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_LARGE_STORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_STORE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_SUPER_STORE;

public class StoreViewFragment extends Fragment implements View.OnClickListener {

	private Button buyEnergy, buyExperience, buyAttack, buyDefense, storeClose;
	TextView storeInfo;
	private String type;
	private Context context;
	private CloseStoreWindow closeStoreWindow;
	public StoreViewFragment(Context context, String type, CloseStoreWindow closeStoreWindow) {
		this.context = context;
		this.closeStoreWindow = closeStoreWindow;
		this.type = type;
		gameView.handlingStoreViewWindow = true;
	}

	public interface CloseStoreWindow{
		void closeStore();
	}
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.store_view, container, false);
		buyEnergy = rootView.findViewById(R.id.trade_energy);
		buyExperience = rootView.findViewById(R.id.trade_experience);
		buyAttack = rootView.findViewById(R.id.trade_attack);
		buyDefense = rootView.findViewById(R.id.trade_defense);
		storeClose = rootView.findViewById(R.id.store_close_button);
		storeInfo = rootView.findViewById(R.id.store_info);

		switch (type) {
			case TAG_STORE:
				storeInfo.setText(context.getString(R.string.small_store_dialogue));
				buyEnergy.setText(context.getString(R.string.get_energy_800));
				buyAttack.setText(context.getString(R.string.get_3_points_attack));
				buyDefense.setText(context.getString(R.string.get_3_points_defense));
				buyExperience.setText(context.getString(R.string.get_50_points_experience));
				break;
			case TAG_LARGE_STORE:
				storeInfo.setText(context.getString(R.string.large_store_dialogue));
				buyEnergy.setText(context.getString(R.string.get_energy_2400));
				buyAttack.setText(context.getString(R.string.get_10_points_attack));
				buyDefense.setText(context.getString(R.string.get_10_points_defense));
				buyExperience.setText(context.getString(R.string.get_150_points_experience));
				break;
			case TAG_SUPER_STORE:
				storeInfo.setText(R.string.super_store_dialogue);
				buyEnergy.setText(R.string.get_energy_5500);
				buyAttack.setText(R.string.get_25_points_attack);
				buyDefense.setText(R.string.get_25_points_defense);
				buyExperience.setText(R.string.get_325_points_experience);
				break;
			case TAG_GIANT_STORE:
				storeInfo.setText(R.string.giant_store_diaglogue);
				buyEnergy.setText(R.string.get_energy_12000);
				buyAttack.setText(R.string.get_60_points_attack);
				buyDefense.setText(R.string.get_60_points_defense);
				buyExperience.setText(R.string.get_750_points_experience);
				break;
		}
		buyAttack.setOnClickListener(this);
		buyDefense.setOnClickListener(this);
		buyEnergy.setOnClickListener(this);
		buyExperience.setOnClickListener(this);
		storeClose.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.store_close_button) {
			closeStoreWindow.closeStore();
			return;
		}
		switch (type) {
			case TAG_STORE:
				if (gameLiveData.getCoins() >= 50) {
					gameLiveData.setCoins(gameLiveData.getCoins() - 50);
					switch (view.getId()) {
						case R.id.trade_energy:
							gameLiveData.setEnergy(gameLiveData.getEnergy() + 800);
							break;
						case R.id.trade_experience:
							gameLiveData.setExperience(gameLiveData.getExperience() + 50);
							break;
						case R.id.trade_attack:
							gameLiveData.setAttack(gameLiveData.getAttack() + 3);
							break;
						case R.id.trade_defense:
							gameLiveData.setDefense(gameLiveData.getDefense() + 3);
							break;
					}
				} else storeInfo.setText(context.getString(R.string.small_store_cannot_afford));
				break;
			case TAG_LARGE_STORE:
				if (gameLiveData.getCoins() >= 125) {
					gameLiveData.setCoins(gameLiveData.getCoins() - 125);
					switch (view.getId()) {
						case R.id.trade_energy:
							gameLiveData.setEnergy(gameLiveData.getEnergy() + 2400);
							break;
						case R.id.trade_experience:
							gameLiveData.setExperience(gameLiveData.getExperience() + 150);
							break;
						case R.id.trade_attack:
							gameLiveData.setAttack(gameLiveData.getAttack() + 10);
							break;
						case R.id.trade_defense:
							gameLiveData.setDefense(gameLiveData.getDefense() + 10);
							break;
					}
				} else storeInfo.setText(context.getString(R.string.large_store_cannot_afford));
				break;
			case TAG_SUPER_STORE:
				if (gameLiveData.getCoins() >= 250) {
					gameLiveData.setCoins(gameLiveData.getCoins() - 250);
					switch (view.getId()) {
						case R.id.trade_energy:
							gameLiveData.setEnergy(gameLiveData.getEnergy() + 5500);
							break;
						case R.id.trade_experience:
							gameLiveData.setExperience(gameLiveData.getExperience() + 325);
							break;
						case R.id.trade_attack:
							gameLiveData.setAttack(gameLiveData.getAttack() + 25);
							break;
						case R.id.trade_defense:
							gameLiveData.setDefense(gameLiveData.getDefense() + 25);
							break;
					}
				} else storeInfo.setText(context.getString(R.string.super_store_cannot_afford));
				break;
			case TAG_GIANT_STORE:
				if (gameLiveData.getCoins() >= 500){
					gameLiveData.setCoins(gameLiveData.getCoins() - 500);
					switch (view.getId()){
						case R.id.trade_energy:
							gameLiveData.setEnergy(gameLiveData.getEnergy() + 12000);
							break;
						case R.id.trade_experience:
							gameLiveData.setExperience(gameLiveData.getExperience() + 750);
							break;
						case R.id.trade_attack:
							gameLiveData.setAttack(gameLiveData.getAttack() + 60);
							break;
						case R.id.trade_defense:
							gameLiveData.setDefense(gameLiveData.getDefense() + 60);
							break;
					}
				} else storeInfo.setText(R.string.giant_store_info_cannot_afford);
				break;
		}
	}
}
