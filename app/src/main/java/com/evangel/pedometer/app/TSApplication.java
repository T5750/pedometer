package com.evangel.pedometer.app;

import com.evangel.pedometer.util.LoggerConfig;
import com.squareup.leakcanary.LeakCanary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class TSApplication extends Application {
	private static TSApplication sApplication;
	private int appCount = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		sApplication = this;
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity,
					Bundle savedInstanceState) {
			}

			@Override
			public void onActivityStarted(Activity activity) {
				appCount++;
			}

			@Override
			public void onActivityResumed(Activity activity) {
			}

			@Override
			public void onActivityPaused(Activity activity) {
			}

			@Override
			public void onActivityStopped(Activity activity) {
				appCount--;
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity,
					Bundle outState) {
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
			}
		});
		LoggerConfig.installLogger();
	}

	/**
	 * app是否在前台
	 * 
	 * @return true前台，false后台
	 */
	public boolean isForeground() {
		return appCount > 0;
	}

	public static TSApplication getApplication() {
		return sApplication;
	}
}
