package com.michael.pan.eviltower.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.adapters.EnemyListAdapter;
import com.michael.pan.eviltower.entities.Enemy;

import java.util.ArrayList;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;

public class EnemyListFragment extends Fragment {

	public interface onClickEnemyListClose{
		void closeEnemyList();
	}
	private onClickEnemyListClose onClickEnemyListClose;

	public EnemyListFragment(EnemyListFragment.onClickEnemyListClose onClickEnemyListClose) {
		this.onClickEnemyListClose = onClickEnemyListClose;
		gameView.handlingEnemyListWindow = true;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.enemy_list_view, container, false);
		RecyclerView enemyListView = rootView.findViewById(R.id.enemy_list_view);

		EnemyListAdapter mAdapter = new EnemyListAdapter(getContext());
		mAdapter.setEnemyList(getEnemyList(getContext()));
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
		enemyListView.setLayoutManager(layoutManager);
		enemyListView.setAdapter(mAdapter);

		Button close = rootView.findViewById(R.id.enemy_list_close_button);
		close.setOnClickListener(view -> onClickEnemyListClose.closeEnemyList());
		return rootView;

	}

	private ArrayList<Enemy.EnemyEntry> getEnemyList(Context context) {

		if (gameView.layer02 == null) return new ArrayList<>();
		ArrayList<Integer> values = new ArrayList<>();
		for (int y = 0; y < gameView.yCount; y++){
			for (int x = 0; x < gameView.xCount; x++){
				int value = gameView.layer02[y][x];
				if (value!= 0 && !values.contains(value)) values.add(value);
			}
		}

		ArrayList<Enemy.EnemyEntry> enemyList = new ArrayList<>();
		for (int i = 0; i < values.size(); i ++)
			enemyList.add(new Enemy.EnemyEntry(context, values.get(i), gameView.floor));
		return enemyList;
	}
}
