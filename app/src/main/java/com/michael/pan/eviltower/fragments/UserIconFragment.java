package com.michael.pan.eviltower.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.activities.ImagePickActivity;
import com.michael.pan.eviltower.adapters.UserIconAdapter;

import java.util.Objects;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.COLUMN_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_USER_DATA;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_USER_ID;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.EXTRA_USER_NAME;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.USER_LOADER_PROJECTION;
import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.buildUriByUserId;

public class UserIconFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, UserIconAdapter.onIconClicked {

	private UserIconAdapter mAdapter;
	private GridView gridView;
	private static final int ID_CURSOR_LOADER = 62;
	private static final int ID_REQUEST_CODE = 413;
	private final String TAG = "UserIconFragment: ";
	int iconId;
	private String id;

	public UserIconFragment() {
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		LoaderManager.getInstance(getActivity()).initLoader(ID_CURSOR_LOADER, null, this);
		//getActivity will be instantiated on calling onAttach, so the Loader is employed here.
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.pref_user_icon_gridview, container, false);
		Bundle bundle = getArguments();
		assert bundle != null;
		TypedArray userIconDrawables = getResources().obtainTypedArray(R.array.user_icon_drawables);
		mAdapter = new UserIconAdapter(getContext(), this, userIconDrawables);
		gridView = rootView.findViewById(R.id.user_icon_grid_view);
		return rootView;
	}

	@NonNull
	@Override
	public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
		String sortOrder = COLUMN_ID + " ASC";
//				content://com.michael.pan.MagicTower/magic_tower
		return new CursorLoader(Objects.requireNonNull(this.getActivity()), buildUriByUserId(null, -1), USER_LOADER_PROJECTION, null,null, sortOrder);
	}

	@Override
	public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
		if (data != null && data.getCount() != 0) {
			/*If the onLoadFinished() method passes you a null cursor,
			then that means that the ContentProvider's query() method has returned null.
			You need to fix your query() method so that it doesn't return null in this case.*/
			mAdapter.swapCursor(data); // push the cursor to the RecyclerView Adapter
			gridView.setVisibility(View.VISIBLE);
			gridView.setAdapter(mAdapter);// Do not forget to set the adapter to link its holder, the Fragment/Activity
//			Log.i(TAG, "loaded groups of data: " + data.getCount());
			//emptyDatabaseMessage.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLoaderReset(@NonNull Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LoaderManager.getInstance(getActivity()).destroyLoader(ID_CURSOR_LOADER);
	}

	@Override
	public void clicked(String[] strings) {//strings = {userId, userData, userName}
		Intent intent = new Intent(getActivity(), ImagePickActivity.class);
		intent.putExtra(EXTRA_USER_ID, strings[0]);
		intent.putExtra(EXTRA_USER_DATA, strings[1]);
		intent.putExtra(EXTRA_USER_NAME, strings[2]);
		startActivityForResult(intent, ID_REQUEST_CODE);//do not use getActivity since it will reference not to this fragment.
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//		System.out.println("okokok");
		if (requestCode == ID_REQUEST_CODE){
			if (resultCode == Activity.RESULT_OK){
				LoaderManager.getInstance(getActivity()).restartLoader(ID_CURSOR_LOADER, null, this);
			}
		}
	}
}
