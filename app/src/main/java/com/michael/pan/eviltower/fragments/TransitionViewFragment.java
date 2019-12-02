package com.michael.pan.eviltower.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.michael.pan.eviltower.R;

import java.util.Locale;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.data.EvilTowerContract.EXTRA_LAN_SETTINGS;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_FLOOR;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRAS_EVENT_TYPE;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.floorName;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.floorNameZhCn;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.floorNameZhHk;

public class TransitionViewFragment extends Fragment {

	private OnTransitionViewClickedListener mCallback;
	private String TAG = "Transition View Fragment: ";
	public interface OnTransitionViewClickedListener{
		void OnTransitionViewClicked();
	}
	public TransitionViewFragment(){

	}

	public TransitionViewFragment(OnTransitionViewClickedListener listener) {
		this.mCallback = listener;
		gameView.handlingTransitionView = true;
	}

	@Override
	public void onAttach(@NonNull Context context) {
		// To check if the mCallback has been implemented in the activity class
		super.onAttach(context);
		try{
			mCallback = (TransitionViewFragment.OnTransitionViewClickedListener) context;
//			Log.i(TAG, "implemented OnDialogViewClickedListener");
		}catch (ClassCastException e){
//			Log.i(TAG, new ClassCastException(context.toString() + "must implement OnDialogViewClickedListener").getMessage());
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.transition_view, container, false);
		TextView layerInfo = rootView.findViewById(R.id.layer_info);

		Bundle bundle = this.getArguments();
		assert bundle != null;
		String npcType = bundle.getString(EXTRAS_EVENT_TYPE);
		int floor = bundle.getInt(COLUMN_FLOOR);
		String lanPref = bundle.getString(EXTRA_LAN_SETTINGS,"en");
//		Log.i(TAG, " Beginning dialog with " + npcType);

		switch (lanPref) {
			case "en":
				layerInfo.setText(String.format(Locale.US, "%s%s", getString(R.string.floor_is), floorName[floor]));
				break;
			case "zh":
				layerInfo.setText(String.format(Locale.US, "%s%s", getString(R.string.floor_is), floorNameZhCn[floor]));
				break;
			case "hk":
				layerInfo.setText(String.format(Locale.US, "%s%s", getString(R.string.floor_is), floorNameZhHk[floor]));
				break;
		}
		View.OnClickListener listener = view -> {
//			Log.i(TAG, "Handling click on DialogView for transition view");
			mCallback.OnTransitionViewClicked();
		};

		rootView.setOnClickListener(listener);

		Animation aniFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
		Animation aniFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
		aniFadeIn.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				rootView.setAnimation(aniFadeOut);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
		});
		aniFadeOut.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				mCallback.OnTransitionViewClicked();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
		});

		rootView.setAnimation(aniFadeIn);

		return rootView;
	}

}
