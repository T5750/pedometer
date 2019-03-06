package com.evangel.pedometer.app;

import android.content.Context;
import android.content.Intent;

/**
 * Android比较便捷的重启APP的方法<br/>
 * https://www.jianshu.com/p/6321c7bfb443
 */
public class RestartAppTool {
	/**
	 * 重启整个APP
	 * 
	 * @param context
	 * @param delayed
	 *            延迟多少毫秒
	 */
	public static void restartApp(Context context, long delayed) {
		/** 开启一个新的服务，用来重启本APP */
		Intent intent = new Intent(context, KillSelfService.class);
		intent.putExtra("packageName", context.getPackageName());
		intent.putExtra("delayed", delayed);
		context.startService(intent);
		/** 杀死整个进程 **/
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/** 重启整个APP */
	public static void restartApp(Context context) {
		restartApp(context, 2000);
	}
}
