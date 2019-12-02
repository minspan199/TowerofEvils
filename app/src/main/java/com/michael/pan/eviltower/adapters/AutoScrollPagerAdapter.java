package com.michael.pan.eviltower.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.michael.pan.eviltower.fragments.SlideFragment;


public class AutoScrollPagerAdapter extends FragmentPagerAdapter {
	private String extras;

	public AutoScrollPagerAdapter(@NonNull FragmentManager fm, int behavior, String extras) {
		super(fm, behavior);
		this.extras = extras;
	}

	@NonNull
	@Override
	public Fragment getItem(int position) {
		return new SlideFragment(position, extras);
	}

	@Override
	public int getCount() {
		return 3;
	}
}
