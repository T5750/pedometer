package com.evangel.pedometerlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 关机监听
 */
public class TodayStepShutdownReceiver extends BroadcastReceiver {
	private static final String TAG = "TodayStepShutdownReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
			Logger.e(TAG, "TodayStepShutdownReceiver");
			PreferencesHelper.setShutdown(context, true);
		}
	}
}
