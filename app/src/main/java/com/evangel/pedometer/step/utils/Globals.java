package com.evangel.pedometer.step.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.widget.Toast;

import lecho.lib.hellocharts.util.ChartUtils;

public class Globals {
	/**
	 * 每日锻炼步数key
	 */
	public static final String PLAN_WALK_KEY = "planWalk_QTY";
	/**
	 * 每日锻炼步数
	 */
	public static final String PLAN_WALK_QTY = "5000";
	/**
	 * 身高key
	 */
	public static final String HEIGHT_KEY = "height";
	/**
	 * 身高默认值
	 */
	public static final String HEIGHT_VALUE = "180";
	/**
	 * 体重key
	 */
	public static final String WEIGHT_KEY = "weight";
	/**
	 * 体重默认值
	 */
	public static final String WEIGHT_VALUE = "70";
	public static final int[] COLORS = new int[] { ChartUtils.COLOR_BLUE,
			ChartUtils.COLOR_VIOLET, ChartUtils.COLOR_GREEN };

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

	/**
	 * cm2上标，仅使用TextView时有效
	 * 
	 * @param text
	 * @return
	 */
	public static SpannableStringBuilder unitCm2(String text) {
		SpannableString cm2 = new SpannableString("cm2");
		cm2.setSpan(new RelativeSizeSpan(0.5f), 2, 3,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 一半大小
		cm2.setSpan(new SuperscriptSpan(), 2, 3,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 上标
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
				text);
		spannableStringBuilder.append(cm2);
		return spannableStringBuilder;
	}

	/**
	 * 用于首页chart
	 * 
	 * @return
	 */
	public static final int pickColor() {
		return COLORS[(int) Math.round(Math.random() * (COLORS.length - 1))];
	}

	public static void showToast(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
