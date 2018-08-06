package com.evangel.pedometerlib;

import java.util.List;

/**
 */
interface ITodayStepDBHelper {
	void createTable();

	void deleteTable();

	void clearCapacity(String curDate, int limit);

	boolean isExist(TodayStepData todayStepData);

	void insert(TodayStepData todayStepData);

	List<TodayStepData> getQueryAll();

	List<TodayStepData> getStepListByDate(String dateString);

	List<TodayStepData> getStepListByStartDateAndDays(String startDate,
			int days);
}
