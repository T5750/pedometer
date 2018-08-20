package com.evangel.pedometer.util;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * 图像的监听
 */
public class ValueTouchListener implements ColumnChartOnValueSelectListener {
	@Override
	public void onValueSelected(int columnIndex, int subcolumnIndex,
			SubcolumnValue value) {
		// showToast("Selected: " + value);
	}

	@Override
	public void onValueDeselected() {
	}
}