package com.evangel.pedometer.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.evangel.pedometer.bean.StepData;
import com.evangel.pedometerlib.DateUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

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
	 * X轴，24小时
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
	 * Y轴最大高度
	 */
	public static Long TOP_MAX = 0L;

	/**
	 * 用于今日步数柱状图
	 */
	public static int pickColor() {
		return COLORS[(int) Math.round(Math.random() * (COLORS.length - 1))];
	}

	/**
	 * 用于今日步数柱状图，返回每小时总步数
	 * 
	 * @param stepArray
	 * @param numColumns
	 * @return
	 */
	public static List<Long> getHourList(String stepArray, int numColumns) {
		Gson gson = new Gson();
		List<StepData> stepDataList = gson.fromJson(stepArray,
				new TypeToken<List<StepData>>() {
				}.getType());
		// 用于记录各个小时对应的总步数
		List<Long> hourList = new ArrayList<>();
		// 用于记录上一小时
		int olderHour = 0;
		// 用于记录上一次大于0的总步数，hourList之和
		long olderStepNum = 0L;
		for (int i = 0; i < numColumns; i++) {
			hourList.add(0L);
			for (int j = 0; j < stepDataList.size(); j++) {
				StepData stepData = stepDataList.get(j);
				long stepNum = stepData.getStepNum();
				long sportDate = stepData.getSportDate();
				Date date = new Date(sportDate);
				String hour = DateUtils.getHour(date);
				int hourInt = Integer.valueOf(hour);
				if (i > 0 && i != olderHour) {
					if (hourList.get(i - 1) > 0) {
						olderStepNum = olderStepNum + hourList.get(i - 1);
					}
					olderHour = i;
				}
				if (i == hourInt && (stepNum - olderStepNum >= 0)) {
					hourList.set(i, stepNum - olderStepNum);
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
		// "[{\"sportDate\":1533856573082,\"stepNum\":100},{\"sportDate\":1533856573083,\"stepNum\":200},{\"sportDate\":1533866573083,\"stepNum\":300},{\"sportDate\":1533884436964,\"stepNum\":1350}]";
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
					if (TOP_MAX < step) {
						TOP_MAX = step;
					}
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
			// 初始化横坐标，去除放大时的小数
			List<AxisValue> axisValueList = new ArrayList<>();
			for (int i = 0; i < NUM_COLUMNS; i++) {
				AxisValue axisValue = new AxisValue(i);
				axisValueList.add(axisValue);
			}
			axisX.setValues(axisValueList);
			axisY.setName(" ");// 必须设置空格，要显示1000才可以显示完整
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}
		return data;
	}

	/**
	 * TODO 尽量处理Y轴标签上部分被切断问题，增加一定的偏移量
	 * https://github.com/lecho/hellocharts-android/issues/243
	 * https://github.com/lecho/hellocharts-android/issues/184
	 * 
	 * @return
	 */
	public static float formatTopMax() {
		float result = TOP_MAX / 100f;
		result = (result + 1) * 100;
		return result;
	}

	/**
	 * 绘制今日步数柱状图
	 * 
	 * @param chart
	 * @param stepArray
	 */
	public static void drawColumnChart(ColumnChartView chart,
			String stepArray) {
		ColumnChartData data = getColumnChartData(stepArray);
		chart.setColumnChartData(data);
		float topMax = formatTopMax();
		if (topMax > 0) {
			// set chart data to initialize viewport, otherwise it will
			// be[0,0;0,0]
			// get initialized viewport and change if ranges according to your
			// needs.
			final Viewport v = new Viewport(chart.getMaximumViewport());
			v.top = topMax; // example max value
			v.bottom = 0; // example min value
			chart.setMaximumViewport(v);
			chart.setCurrentViewport(v);
			// Optional step: disable viewport recalculations, thanks to this
			// animations will not change viewport automatically.
			chart.setViewportCalculationEnabled(false);
		}
	}
}
