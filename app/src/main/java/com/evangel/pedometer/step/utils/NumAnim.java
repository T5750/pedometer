package com.evangel.pedometer.step.utils;

import java.util.LinkedList;
import java.util.Random;

import android.widget.TextView;

/**
 * 步数累加效果 for TextView
 */
public class NumAnim {
	// 每秒刷新多少次
	private static final int COUNTERS = 20;
	private static final int DECIMAL_COUNTERS = 0;

	public static void startAnim(TextView textV, float num) {
		startAnim(textV, num, 500);
	}

	public static void startAnim(TextView textV, float num, long time) {
		if (num == 0) {
			textV.setText(NumUtil.NumberFormat(num, DECIMAL_COUNTERS));
			return;
		}
		Float[] nums = splitnum(num, (int) ((time / 1000f) * COUNTERS));
		Counter counter = new Counter(textV, nums, time);
		textV.removeCallbacks(counter);
		textV.post(counter);
	}

	private static Float[] splitnum(float num, int count) {
		Random random = new Random();
		float numtemp = num;
		float sum = 0;
		LinkedList<Float> nums = new LinkedList<Float>();
		nums.add(0f);
		while (true) {
			float nextFloat = NumUtil.NumberFormatFloat(
					(random.nextFloat() * num * 2f) / (float) count,
					DECIMAL_COUNTERS);
			// System.out.println("next:" + nextFloat);
			if (numtemp - nextFloat >= 0) {
				sum = NumUtil.NumberFormatFloat(sum + nextFloat,
						DECIMAL_COUNTERS);
				nums.add(sum);
				numtemp -= nextFloat;
			} else {
				nums.add(num);
				return nums.toArray(new Float[0]);
			}
		}
	}

	static class Counter implements Runnable {
		private final TextView view;
		private Float[] nums;
		private long pertime;
		private int i = 0;

		Counter(TextView view, Float[] nums, long time) {
			this.view = view;
			this.nums = nums;
			this.pertime = time / nums.length;
		}

		@Override
		public void run() {
			if (i > nums.length - 1) {
				view.removeCallbacks(Counter.this);
				return;
			}
			view.setText(NumUtil.NumberFormat(nums[i++], DECIMAL_COUNTERS));
			view.removeCallbacks(Counter.this);
			view.postDelayed(Counter.this, pertime);
		}
	}
}