package com.evangel.pedometerlib;

import java.util.Calendar;

import android.content.Context;
import android.os.PowerManager;

/**
 */
class WakeLockUtils {
	private static PowerManager.WakeLock mWakeLock;

	synchronized static PowerManager.WakeLock getLock(Context context) {
		if (mWakeLock != null) {
			if (mWakeLock.isHeld())
				mWakeLock.release();
			mWakeLock = null;
		}
		if (mWakeLock == null) {
			PowerManager mgr = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					TodayStepService.class.getName());
			mWakeLock.setReferenceCounted(true);
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			mWakeLock.acquire();
		}
		return (mWakeLock);
	}
}
