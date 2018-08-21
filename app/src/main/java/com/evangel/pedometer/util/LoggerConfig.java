package com.evangel.pedometer.util;

import com.evangel.pedometer.BuildConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Logger
 */
public class LoggerConfig {
	public static void installLogger() {
		// FormatStrategy - 更改全局属性
		PrettyFormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(false) // 是否显示线程信息。 默认为true
				.methodCount(1) // 显示方法的行数。 默认2
				// .methodOffset(7) // 隐藏内部方法调用到偏移量。 默认5
				// .tag("TAG") // 全局Tag标签。 默认PRETTY_LOGGER
				.build();
		Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
			/**
			 * Loggable - 控制是否输出Log
			 * 
			 * @param priority
			 * @param tag
			 * @return 返回true表示输出Log，false表示不输出Log
			 */
			@Override
			public boolean isLoggable(int priority, String tag) {
				return BuildConfig.DEBUG;
			}
		});
	}
}
