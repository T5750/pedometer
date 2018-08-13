package com.evangel.pedometer.step.bean;

public class StepData {
	/**
	 * 当天时间，只显示到天 yyyy-MM-dd
	 */
	private String today;
	/**
	 * 步数时间，显示到毫秒
	 */
	private long sportDate;
	/**
	 * 对应date时间的步数
	 */
	private long stepNum;

	public StepData() {
	}

	public long getSportDate() {
		return sportDate;
	}

	public void setSportDate(long sportDate) {
		this.sportDate = sportDate;
	}

	public long getStepNum() {
		return stepNum;
	}

	public void setStepNum(long stepNum) {
		this.stepNum = stepNum;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	@Override
	public String toString() {
		return "TodayStepData{" + ", today=" + today + ", sportDate="
				+ sportDate + ", stepNum=" + stepNum + '}';
	}
}
