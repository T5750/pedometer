package com.evangel.pedometer.step.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.evangel.pedometer.step.bean.StepData;
import com.evangel.pedometerlib.DateUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;

/**
 * HelloCharts步数柱状图相关
 */
public class StepChartUtil {
	/**
	 * 步数柱状图自定义颜色
	 */
	public static final int[] COLORS = new int[] { ChartUtils.COLOR_BLUE,
			ChartUtils.COLOR_VIOLET, ChartUtils.COLOR_GREEN };
	/**
	 * 横坐标，24小时
	 */
	public static final int NUM_COLUMNS = 24;
	/**
	 * 柱子上是否显示标识文字
	 */
	public static final boolean HAS_LABELS = false;
	/**
	 * 柱子被点击时，是否显示标识的文字
	 */
	public static final boolean HAS_LABEL_FOR_SELECTED = true;
	/**
	 * 是否有坐标轴
	 */
	public static final boolean HAS_AXES = true;
	/**
	 * 是否有坐标轴的名字
	 */
	public static final boolean HAS_AXES_NAMES = false;

	/**
	 * 用于今日步数柱状图
	 */
	public static final int pickColor() {
		return COLORS[(int) Math.round(Math.random() * (COLORS.length - 1))];
	}

	/**
	 * 用于今日步数柱状图，返回每小时总步数
	 * 
	 * @param stepArray
	 * @param numColumns
	 * @return
	 */
	public static List getHourList(String stepArray, int numColumns) {
		Gson gson = new Gson();
		List<StepData> stepDataList = gson.fromJson(stepArray,
				new TypeToken<List<StepData>>() {
				}.getType());
		// 用于记录各个小时对应的总步数
		List<Long> hourList = new ArrayList<>();
		// 用于记录上一小时
		int olderHour = 0;
		// 用于记录上一小时对应的总步数
		long olderStepNum = 0L;
		for (int i = 0; i < numColumns; i++) {
			hourList.add(0L);
			for (int j = 0; j < stepDataList.size(); j++) {
				StepData stepData = stepDataList.get(j);
				long sportDate = stepData.getSportDate();
				Date date = new Date(sportDate);
				String hour = DateUtils.getHour(date);
				int hourInt = Integer.valueOf(hour);
				if (i > 0 && i != olderHour) {
					olderStepNum = hourList.get(i - 1);
					olderHour = i;
				}
				if (i == hourInt) {
					hourList.set(i, stepData.getStepNum() - olderStepNum);
				}
			}
		}
		return hourList;
	}

	/**
	 * 用于今日步数柱状图，返回柱状图数据
	 * 
	 * @param stepArray
	 * @return
	 */
	public static ColumnChartData getColumnChartData(String stepArray) {
		// stepArray =
		// "[{\"sportDate\":1533856573082,\"stepNum\":100},{\"sportDate\":1533856573083,\"stepNum\":200},{\"sportDate\":1533884436964,\"stepNum\":900}]";
		List<Long> hourList = StepChartUtil.getHourList(stepArray, NUM_COLUMNS);
		int numSubcolumns = 1;
		// Column can have many subcolumns, here by default I use 1
		// subcolumn in
		// each of 8 columns.
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < NUM_COLUMNS; ++i) {
			values = new ArrayList<SubcolumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				Long step = hourList.get(i);
				if (step > 0) {
					values.add(new SubcolumnValue(Float.valueOf(step),
							pickColor()));
				}
			}
			Column column = new Column(values);
			column.setHasLabels(HAS_LABELS);
			column.setHasLabelsOnlyForSelected(HAS_LABEL_FOR_SELECTED);
			columns.add(column);
		}
		// 存放柱状图数据的对象
		ColumnChartData data = new ColumnChartData(columns);
		if (HAS_AXES) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			if (HAS_AXES_NAMES) {
				axisX.setName("小时");
				axisY.setName("步数");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}
		return data;
	}
}