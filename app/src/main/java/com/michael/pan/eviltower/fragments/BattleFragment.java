package com.michael.pan.eviltower.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.data.EvilTowerContract;
import com.michael.pan.eviltower.entities.Enemy;
import com.michael.pan.eviltower.views.BattleView;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ENERGY;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_USER_ICON;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRAS_EVENT_TYPE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_COIN;

public class BattleFragment extends Fragment implements BattleView.onBattleFinishing, View.OnClickListener{

	OnBattleFinishedListener onBattleFinishedCallback;
	private String TAG = BattleFragment.class.getSimpleName();

	private BattleView battleField = null;
	private Bundle bundle;
	private int enemyType;
	private Button quitButton, skipButton, dialog_ok, dialog_cancel;
	private TextView gameViewInfo;
	private FragmentManager fragmentManager;
	private Enemy.EnemyEntry enemyEntry;

	@Override
	public void BattleFinishing(String s) {

		switch (s){
			case EvilTowerContract.TAG_LOSE:
				new Handler(Looper.getMainLooper()).post(() -> {
					quitButton.setVisibility(View.VISIBLE);
					skipButton.setVisibility(View.GONE);
					gameViewInfo.setVisibility(View.VISIBLE);
					gameViewInfo.setText(getString(R.string.lose_instruction));
				});
				break;
			case EvilTowerContract.TAG_WIN:
				getActivity().runOnUiThread(() -> {
					bundle.putInt(COLUMN_ENERGY, battleField.warriorEnergy);
					onBattleFinishedCallback.onBattleFinished(getContext(), EvilTowerContract.TAG_WIN, bundle);
				});
				break;
		}
//		System.out.println("name:" + s);
	}

	public interface OnBattleFinishedListener {
		void onBattleFinished(Context context, String s, Bundle bundle);
	}

	public BattleFragment(OnBattleFinishedListener onBattleFinished, Enemy.EnemyEntry enemyEntry) {
		this.onBattleFinishedCallback = onBattleFinished;
		this.enemyEntry = enemyEntry;
		gameView.handlingBattleWindow = true;

	}

	public BattleFragment(){}

	@Override
	public void onAttach(@NonNull Context context) {
		// To check if the mCallback has been implemented in the activity class
		super.onAttach(context);
		try{
			onBattleFinishedCallback = (OnBattleFinishedListener) context;
//			Log.i(TAG, "implemented OnDialogViewClickedListener");
		}catch (ClassCastException e){
//			Log.i(TAG, new ClassCastException(context.toString() + "must implement onBattleFinishedCallback").getMessage());
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		bundle = getArguments();
		assert bundle != null;
		String type = bundle.getString(EXTRAS_EVENT_TYPE);
		enemyType = bundle.getInt(getString(R.string.matrix_value));
		int floor = bundle.getInt(getString(R.string.floor));
		String[] dialogContents;
		View rootView;
		rootView = inflater.inflate(R.layout.battle_view, container, false);
		battleField = rootView.findViewById(R.id.battle_field);
		gameViewInfo = rootView.findViewById(R.id.game_view_info);
		skipButton = rootView.findViewById(R.id.skip_game_button);
		quitButton = rootView.findViewById(R.id.quit_game_button);
		skipButton.setOnClickListener(this);
		quitButton.setOnClickListener(this);
		LoadBattleField(bundle, enemyType);
		//battleField.setRunning();
		return rootView;
	}

	private void LoadBattleField(Bundle bundle, int enemyIcon) {
		battleField.setEnemyAttack(enemyEntry.attack)
			.setEnemyDefense(enemyEntry.defense)
			.setEnemyEnergy(enemyEntry.energy)
			.setWarriorAttack(gameLiveData.getAttack())
			.setWarriorDefense(gameLiveData.getDefense())
			.setWarriorEnergy(gameLiveData.getEnergy())
			.setUserIcon(bundle.getInt(COLUMN_USER_ICON))
			.setEnemyName(enemyEntry.name)
			.setFinishing(this)
			.setEnemyIcon(enemyIcon)
			.setVampire(enemyEntry.vampire)
			.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.skip_game_button) {
			BattleFragment.this.LoadBattleField(bundle, enemyType);
			battleField.gamerFirst = true;
			battleField.stopRunning();
			if (battleField.skipAnimation()) {
				bundle.putInt(COLUMN_ENERGY, battleField.warriorEnergy);
				bundle.putInt(EXTRA_COIN, enemyEntry.coin);
				onBattleFinishedCallback.onBattleFinished(getContext(), EvilTowerContract.TAG_WIN, bundle);
			} else {
				skipButton.setVisibility(View.GONE);
				quitButton.setVisibility(View.VISIBLE);
				gameViewInfo.setVisibility(View.VISIBLE);
				gameViewInfo.setText(getContext().getString(R.string.lose_instruction));
			}
		} else if (view.getId() == R.id.quit_game_button){
			bundle.putInt(EXTRA_COIN, 0);
			onBattleFinishedCallback.onBattleFinished(getContext(), EvilTowerContract.TAG_LOSE, bundle);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		battleField.stopRunning();
	}

}
