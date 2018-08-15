package com.evangel.pedometer.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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

	/**
	 * 获取锻炼步数
	 * 
	 * @param sp
	 * @return
	 */
	public static Integer getPlanWalk(SharedPreferencesUtil sp) {
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

	public static void showToast(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * 设置透明状态栏
	 */
	public static void setContentViewAndStatusBar(AppCompatActivity activity,
			int resource) {
		// 隐藏标题栏
		// supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		View root = LayoutInflater.from(activity).inflate(resource, null);
		// 或在界面的根层加入android:fitsSystemWindows=”true” 这个属性，这样就可以让内容界面从状态栏下方开始
		ViewCompat.setFitsSystemWindows(root, true);
		activity.setContentView(root);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			// Android 5.0 以上 全透明
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView()
					.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(
					WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			// 状态栏（以上几行代码必须，参考setStatusBarColor|setNavigationBarColor方法源码）
			window.setStatusBarColor(Color.TRANSPARENT);
			// 虚拟导航键
			window.setNavigationBarColor(Color.TRANSPARENT);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// Android 4.4 以上 半透明
			Window window = activity.getWindow();
			// 状态栏
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 虚拟导航键
			window.addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}
}
