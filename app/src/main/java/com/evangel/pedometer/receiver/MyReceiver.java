package com.evangel.pedometer.receiver;

import com.evangel.pedometer.activity.MainActivity;
import com.evangel.pedometer.app.TSApplication;
import com.evangel.pedometerlib.BaseClickBroadcast;

import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BaseClickBroadcast {
	@Override
	public void onReceive(Context context, Intent intent) {
		TSApplication tsApplication = (TSApplication) context
				.getApplicationContext();
		if (!tsApplication.isForeground()) {
			Intent mainIntent = new Intent(context, MainActivity.class);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mainIntent);
		} else {
		}
	}
}
