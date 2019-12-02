package com.michael.pan.eviltower.services;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.michael.pan.eviltower.adapters.UserListAdapter;

import java.util.Collections;
import java.util.List;

public class UserListTouchHelper extends ItemTouchHelper.SimpleCallback {

	private List userIdList;
	private UserListAdapter adapter;
	/**
	 * Creates a Callback for the given drag and swipe allowance. These values serve as
	 * defaults
	 * and if you want to customize behavior per ViewHolder, you can override
	 * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
	 * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
	 *
	 * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
	 *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
	 *                  #END},
	 *                  {@link #UP} and {@link #DOWN}.
	 * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
	 *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
	 *                  #END},
	 *                  {@link #UP} and {@link #DOWN}.
	 */
	public UserListTouchHelper(int dragDirs, int swipeDirs, List userList, UserListAdapter adapter) {
		super(dragDirs, swipeDirs);
		userIdList = userList;
		this.adapter = adapter;
	}

	@Override
	public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
		// Get the from and to positions.
		int from = viewHolder.getAdapterPosition();
		int to = target.getAdapterPosition();
		// Swap the items and notify the adapter.
		Collections.swap(userIdList, from, to);
		adapter.notifyItemMoved(from, to);
		return true;
	}

	@Override
	public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
		// Remove the item from the dataset.
		userIdList.remove(viewHolder.getAdapterPosition());
		// Notify the adapter.
		adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
	}
}
