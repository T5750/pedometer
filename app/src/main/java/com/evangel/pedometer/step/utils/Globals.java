package com.evangel.pedometer.step.utils;

public class Globals {
	/**
	 * 每日锻炼步数key
	 */
	public static String PLAN_WALK_KEY = "planWalk_QTY";
	/**
	 * 每日锻炼步数
	 */
	public static String PLAN_WALK_QTY = "5000";

	/**
	 * 获取锻炼步数
	 * 
	 * @param sp
	 * @return
	 */
	public static Integer getPlanWalk(SharedPreferencesUtils sp) {
		// 获取用户设置的计划锻炼步数，没有设置过的话默认
		String planWalk_QTY = (String) sp.getParam(Globals.PLAN_WALK_KEY,
				Globals.PLAN_WALK_QTY);
		return Integer.parseInt(planWalk_QTY);
	}

	/**
	 * 获取BMI
	 * 
	 * @param heightStr
	 * @param weightStr
	 * @return
	 */
	public static float getBmi(String heightStr, String weightStr) {
		float bmi = 0f;
		try {
			float height = Float.valueOf(heightStr);
			float weight = Float.valueOf(weightStr);
			float heightM = height / 100;
			bmi = weight / (heightM * heightM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmi;
	}
}
