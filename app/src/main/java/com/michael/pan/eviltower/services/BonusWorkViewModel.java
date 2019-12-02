package com.michael.pan.eviltower.services;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import static com.michael.pan.eviltower.data.EvilTowerContract.EvilTowerEntry.SCHEDULE_COIN_BONUS_WORK_NAME;

public class BonusWorkViewModel extends AndroidViewModel {

	private static final String TAG_DATA_BASE_QUERY = "query-the-database";
	private static final String TAG_NOTIFICATION_WORK = "schedule-notification";
	private WorkManager mWorkManager;
	public LiveData<WorkInfo> mSavedWorkInfo;
	private OneTimeWorkRequest databaseQuery;


	public BonusWorkViewModel(@NonNull Application application) {

		super(application);

		databaseQuery = new OneTimeWorkRequest.Builder(DatabaseQueryWork.class)
			.setInitialDelay(1, TimeUnit.SECONDS)
			.addTag(TAG_DATA_BASE_QUERY)
			.build();
		mWorkManager = WorkManager.getInstance(application);
		mSavedWorkInfo = mWorkManager.getWorkInfoByIdLiveData(databaseQuery.getId());

	}


	public void CancelCoinBonus() {
		mWorkManager.cancelUniqueWork(TAG_DATA_BASE_QUERY);
	}

	public void applyBonusNotify() {

		Constraints constraints = new Constraints.Builder()
			.setRequiresCharging(true)
			.build();

		OneTimeWorkRequest.Builder notifyBuilder = new OneTimeWorkRequest.Builder(NotificationWork.class)
			.setInitialDelay(1, TimeUnit.SECONDS)
			.setConstraints(constraints)
			.addTag(TAG_NOTIFICATION_WORK);
		WorkContinuation continuation = mWorkManager.beginUniqueWork(SCHEDULE_COIN_BONUS_WORK_NAME,
			ExistingWorkPolicy.REPLACE, databaseQuery);
		continuation = continuation.then(notifyBuilder.build());
		continuation.enqueue();
	}
}