package com.michael.pan.eviltower.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.michael.pan.eviltower.data.EvilTowerContract;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.Date;

public class SnapshotTask extends AsyncTask<String, Void, String> implements ActivityCompat.OnRequestPermissionsResultCallback {

	private static int REQUEST_PERMISSION = 786;
	private onTaskExecuted onTaskExecuted;
	private WeakReference<Context> activityReference;

	public interface onTaskExecuted{
		void snapshotTaken();
		void snapshotTakenAtEnd(Context context);
	}

	public SnapshotTask(Context context, SnapshotTask.onTaskExecuted onTaskExecuted) {
		activityReference = new WeakReference<>(context);
		this.onTaskExecuted = onTaskExecuted;
	}

	private void takeScreenshot(Context context) {
		Date now = new Date();
		DateFormat.getDateFormat(context).format(now);

		Activity activity = (Activity) context;

		try {
			// image naming and path  to include sd card  appending name you choose for file
			String mPath = context.getFilesDir().toString() + "/" + now + ".png";

			// create bitmap screen capture
			View v1 = activity.getWindow().getDecorView().getRootView();
			v1.setDrawingCacheEnabled(true);
			Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
			v1.setDrawingCacheEnabled(false);

			File imageFile = new File(mPath);
			if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)&&
				(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
					// Permission is not granted
			} else {
				FileOutputStream outputStream = new FileOutputStream(imageFile);
				int quality = 100;
				bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
				outputStream.flush();
				outputStream.close();
				MediaStore.Images.Media.insertImage(context.getContentResolver(),imageFile.getAbsolutePath(),imageFile.getName(),imageFile.getName());
				//openScreenshot(context, imageFile);
			}
		} catch (Exception e) {
			// Several error may come out with file handling or DOM
			e.printStackTrace();
		}
	}

	private static void openScreenshot(Context context, File imageFile) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(imageFile);
		intent.setDataAndType(uri, "image/*");
		context.startActivity(intent);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {}

	@Override
	protected String doInBackground(String... strings) {
		takeScreenshot(activityReference.get());
		return strings[0];
	}

	@Override
	protected void onPostExecute(String string) {
		super.onPostExecute(string);
		switch (string){
			case EvilTowerContract.TAG_SNAPSHOT_IN_THE_GAME:
				onTaskExecuted.snapshotTaken();
				break;
			case EvilTowerContract.TAG_AFTER_THE_GAME:
				onTaskExecuted.snapshotTakenAtEnd(activityReference.get());
				break;
		}
	}
}
