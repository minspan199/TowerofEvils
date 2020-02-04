package com.michael.pan.eviltower.entities;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.BindingAdapter;

import java.io.Serializable;

/**
 * Created by lgvalle on 04/09/15.
 */
public class Sample implements Serializable {

	final int color;
	private final String name;

	public Sample(@ColorRes int color, String name) {
		this.color = color;
		this.name = name;
	}

	@SuppressLint("ResourceAsColor")
	@BindingAdapter("bind:colorTint")
	public static void setColorTint(ImageView view, @ColorRes int color) {
		DrawableCompat.setTint(view.getDrawable(), color);
		//view.setColorFilter(color, PorterDuff.Mode.SRC_IN);
	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}


}
