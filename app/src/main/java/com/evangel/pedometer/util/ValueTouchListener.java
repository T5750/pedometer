package com.evangel.pedometer.util;

import android.content.Context;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * 图像的监听
 */
public class ValueTouchListener implements ColumnChartOnValueSelectListener {
	private Context context;

	public ValueTouchListener(Context context) {
		this.context = context;
	}

	@Override
	public void onValueSelected(int columnIndex, int subcolumnIndex,
			SubcolumnValue value) {
		// TODO How to modify the size/style of points labels
		// https://github.com/lecho/hellocharts-android/issues/432
		if (columnIndex > 0 && columnIndex < StepChartUtil.NUM_COLUMNS - 1) {
		} else {
			Globals.showToast(context,
					String.valueOf(Math.round(value.getValue())));
		}
	}

	@Override
	public void onValueDeselected() {
	}
}