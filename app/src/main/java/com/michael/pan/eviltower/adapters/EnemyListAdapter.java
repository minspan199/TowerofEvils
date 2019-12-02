package com.michael.pan.eviltower.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.entities.Enemy;

import java.util.ArrayList;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameLiveData;
import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;

public class EnemyListAdapter extends RecyclerView.Adapter<EnemyListAdapter.EnemyListViewHolder>{

	private Context context;
	private ArrayList<Enemy.EnemyEntry> enemyList;
	private int index;
	public EnemyListAdapter(Context context) {
		this.context = context;
//		this.run();
	}

	@NonNull
	@Override
	public EnemyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.enemy_list_item, parent, false);
		view.setFocusable(true);
		return new EnemyListViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull EnemyListViewHolder holder, int position) {
		Enemy.EnemyEntry enemy = enemyList.get(position);
		holder.name.setText(enemy.name);
		holder.defense.setText(String.valueOf(enemy.defense));
		holder.attack.setText(String.valueOf(enemy.attack));
		holder.energy.setText(String.valueOf(enemy.energy));
		holder.icon.setImageBitmap(gameView.enemies[index % 2][(enemy.type - 1 - 101)/2]);
		int cost = getEnergyCost(enemy);
		if (enemy.vampire) holder.bloodSucking.setVisibility(View.VISIBLE);
		else holder.bloodSucking.setVisibility(View.GONE);
		if (cost == -1){
			holder.description.setTextColor(context.getResources().getColor(R.color.orange_light));
			holder.description.setText(context.getString(R.string.cannot_defeat));
		}else {
			holder.description.setTextColor(context.getResources().getColor(R.color.design_default_color_primary));
			if (enemy.vampire) holder.description.setText(String.format(context.getString(R.string.energy_cost1), cost));
			else holder.description.setText(String.format(context.getString(R.string.energy_cost), cost));
		}
	}

	private int getEnergyCost(Enemy.EnemyEntry enemy) {
		boolean inBattle = true, gamerFirst = true;
		int enemyEnergy = enemy.energy;
		int enemyDefense = enemy.defense;
		int enemyAttack = enemy.attack;
		int warriorAttack = gameLiveData.getAttack();
		int warriorEnergy = gameLiveData.getEnergy();
		int warriorDefense = gameLiveData.getDefense();
		while(inBattle){
			if (gamerFirst){
				enemyEnergy -= (warriorAttack > enemyDefense) ? (warriorAttack - enemyDefense) : 1;
				gamerFirst = false;
			}else {
				warriorEnergy -= (enemyAttack > warriorDefense) ? (enemyAttack - warriorDefense) : 1;
				gamerFirst = true;
			}
			inBattle = warriorEnergy > 0 && enemyEnergy > 0;
		}
		if (warriorEnergy < 0) return -1;
		else return gameLiveData.getEnergy() - warriorEnergy;
	}

	@Override
	public int getItemCount() {
		if (enemyList == null)return 0;
		else return enemyList.size();
	}

	public void setEnemyList(ArrayList<Enemy.EnemyEntry> enemyList) {
		this.enemyList = enemyList;
		notifyDataSetChanged();
	}

//	@Override
//	public void run() {
//		try {
//			Thread.sleep(1000/5);
//			if (index == 0) index = 1;
//			else index = 0;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	class EnemyListViewHolder extends RecyclerView.ViewHolder{

		TextView name, energy, attack, defense, description, bloodSucking;
		ImageView icon;

		EnemyListViewHolder(@NonNull View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.list_enemy_name);
			energy = itemView.findViewById(R.id.list_enemy_energy);
			attack = itemView.findViewById(R.id.list_enemy_attack);
			defense = itemView.findViewById(R.id.list_enemy_defense);
			description = itemView.findViewById(R.id.list_enemy_description);
			icon = itemView.findViewById(R.id.list_enemy_icon);
			bloodSucking = itemView.findViewById(R.id.blood_sucking_text);
		}
	}
}
