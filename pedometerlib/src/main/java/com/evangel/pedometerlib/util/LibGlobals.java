package com.evangel.pedometerlib.util;

import static com.evangel.pedometerlib.SportStepJsonUtils.getCalorieByStep;
import static com.evangel.pedometerlib.SportStepJsonUtils.getDistanceByStep;

public class LibGlobals {
	/**
	 * 通知，通过步数，获取公里、千卡
	 */
	public static String getKmCalorieByStep(int mStepSum) {
		String km = getDistanceByStep(mStepSum);
		String calorie = getCalorieByStep(mStepSum);
		String result = km + " 公里  " + calorie + " 千卡";
		return result;
	}
}
