package com.evangel.pedometer.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.evangel.pedometer.R;
import com.evangel.pedometer.app.TSApplication;
import com.evangel.pedometer.step.bean.StepData;
import com.evangel.pedometer.step.utils.Globals;
import com.evangel.pedometer.step.utils.NumAnim;
import com.evangel.pedometer.step.utils.SharedPreferencesUtils;
import com.evangel.pedometer.view.StepArcView;
import com.evangel.pedometerlib.DateUtils;
import com.evangel.pedometerlib.ISportStepInterface;
import com.evangel.pedometerlib.SportStepJsonUtils;
import com.evangel.pedometerlib.TodayStepManager;
import com.evangel.pedometerlib.TodayStepService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class MainActivity extends AppCompatActivity
		implements View.OnClickListener {
	private static String TAG = "MainActivity";
	private static final int REFRESH_STEP_WHAT = 0;
	// 循环取当前时刻的步数中间的间隔时间
	private long TIME_INTERVAL_REFRESH = 500;
	private Handler mDelayHandler = new Handler(new TodayStepCounterCall());
	private int mStepSum;
	private ISportStepInterface iSportStepInterface;
	// private TextView mStepArrayTextView;
	private TSApplication tsApplication;
	private TextView tv_data;
	private StepArcView sav_step;
	private TextView tv_set;
	private SharedPreferencesUtils sp;
	// private TextView stepArrayView;
	private TextView tv_step;
	private TextView tv_km;
	private TextView tv_calorie;
	private ColumnChartView chart; // 柱状图的自定义View

	private void assignViews() {
		tv_data = (TextView) findViewById(R.id.tv_data);
		sav_step = (StepArcView) findViewById(R.id.sav_step);
		tv_set = (TextView) findViewById(R.id.tv_set);
		// stepArrayView = (TextView) findViewById(R.id.stepArrayView);
		tv_step = (TextView) findViewById(R.id.tv_step);
		tv_km = (TextView) findViewById(R.id.tv_km);
		tv_calorie = (TextView) findViewById(R.id.tv_calorie);
		chart = (ColumnChartView) findViewById(R.id.column_chart);
	}

	private void addListener() {
		tv_set.setOnClickListener(this);
		tv_data.setOnClickListener(this);
		// stepArrayView.setOnClickListener(this);
		chart.setOnValueTouchListener(new ValueTouchListener());
	}

	private void initData() {
		sp = new SharedPreferencesUtils(this);
		// 设置当前步数为0
		sav_step.setCurrentCount(Globals.getPlanWalk(sp), 0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		assignViews();
		addListener();
		initData();
		tsApplication = (TSApplication) getApplication();
		// 初始化计步模块
		TodayStepManager.init(getApplication());
		// mStepArrayTextView = (TextView) findViewById(R.id.stepArrayTextView);
		// 开启计步Service，同时绑定Activity进行aidl通信
		Intent intent = new Intent(this, TodayStepService.class);
		startService(intent);
		bindService(intent, new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name,
					IBinder service) {
				// Activity和Service通过aidl进行通信
				iSportStepInterface = ISportStepInterface.Stub
						.asInterface(service);
				try {
					mStepSum = iSportStepInterface.getCurrentTimeSportStep();
					updateStepCount(true);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT,
						TIME_INTERVAL_REFRESH);
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
			}
		}, Context.BIND_AUTO_CREATE);
	}

	class TodayStepCounterCall implements Handler.Callback {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_STEP_WHAT: {
				// 每隔500毫秒获取一次计步数据刷新UI
				if (null != iSportStepInterface) {
					int step = 0;
					try {
						step = iSportStepInterface.getCurrentTimeSportStep();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					if (mStepSum != step) {
						mStepSum = step;
						updateStepCount(false);
					}
				}
				mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT,
						TIME_INTERVAL_REFRESH);
				break;
			}
			}
			return false;
		}
	}

	/**
	 * 每隔500毫秒获取一次计步数据刷新UI
	 *
	 * @param flagNumAnim
	 *            是否开启步数累加效果
	 */
	private void updateStepCount(boolean flagNumAnim) {
		Log.e(TAG, "updateStepCount : " + mStepSum);
		tv_km.setText(SportStepJsonUtils.getDistanceByStep(mStepSum));
		tv_calorie.setText(SportStepJsonUtils.getCalorieByStep(mStepSum));
		sav_step.setCurrentCount(Globals.getPlanWalk(sp), mStepSum);
		if (flagNumAnim) {
			NumAnim.startAnim(tv_step, mStepSum);
			generateChartData();
		} else {
			tv_step.setText(String.valueOf(mStepSum));
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_set: {
			startActivity(new Intent(this, SetPlanActivity.class));
			break;
		}
		case R.id.tv_data: {
			Intent intent = new Intent(this, HistoryActivity.class);
			try {
				String stepArray = iSportStepInterface.getTodaySportStepArray();
				intent.putExtra("stepArray", stepArray);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			startActivity(intent);
			break;
		}
		// case R.id.stepArrayView: {
		// // 获取今日步数json
		// if (null != iSportStepInterface) {
		// try {
		// String stepArray = iSportStepInterface
		// .getTodaySportStepArrayByDate(
		// DateUtils.getTodayDate());
		// mStepArrayTextView.setText(stepArray);
		// } catch (RemoteException e) {
		// e.printStackTrace();
		// }
		// }
		// break;
		// }
		// case R.id.stepArrayButton: {
		// // 获取所有步数列表
		// if (null != iSportStepInterface) {
		// try {
		// String stepArray = iSportStepInterface
		// .getTodaySportStepArray();
		// mStepArrayTextView.setText(stepArray);
		// } catch (RemoteException e) {
		// e.printStackTrace();
		// }
		// }
		// break;
		// }
		// case R.id.stepArrayButton1: {
		// // 根据时间来获取步数列表
		// if (null != iSportStepInterface) {
		// try {
		// String stepArray = iSportStepInterface
		// .getTodaySportStepArrayByDate("2018-01-19");
		// mStepArrayTextView.setText(stepArray);
		// } catch (RemoteException e) {
		// e.printStackTrace();
		// }
		// }
		// break;
		// }
		// case R.id.stepArrayButton2: {
		// // 获取多天步数列表
		// if (null != iSportStepInterface) {
		// try {
		// String stepArray = iSportStepInterface
		// .getTodaySportStepArrayByStartDateAndDays(
		// "2018-01-20", 6);
		// mStepArrayTextView.setText(stepArray);
		// } catch (RemoteException e) {
		// e.printStackTrace();
		// }
		// }
		// break;
		// }
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first
		if (mStepSum > 0) {
			// sav_step.setCurrentCount(Globals.getPlanWalk(sp), 0);
			updateStepCount(true);
		}
	}

	/**
	 * 显示柱状图的数据
	 */
	private void generateChartData() {
		if (null != iSportStepInterface) {
			try {
				String stepArray = iSportStepInterface
						.getTodaySportStepArrayByDate(DateUtils.getTodayDate());
				// stepArray =
				// "[{\"sportDate\":1533856573082,\"stepNum\":100},{\"sportDate\":1533856573082,\"stepNum\":100},{\"sportDate\":1533856573082,\"stepNum\":100},{\"sportDate\":1533856573082,\"stepNum\":100},{\"sportDate\":1533884436964,\"stepNum\":900}]";
				Gson gson = new Gson();
				List<StepData> stepDataList = gson.fromJson(stepArray,
						new TypeToken<List<StepData>>() {
						}.getType());
				List<Long> hourList = new ArrayList<>();
				for (int i = 0; i < 24; i++) {
					hourList.add(0L);
					for (int j = 0; j < stepDataList.size(); j++) {
						StepData stepData = stepDataList.get(j);
						long sportDate = stepData.getSportDate();
						Date date = new Date(sportDate);
						String hour = DateUtils.getHour(date);
						int hourInt = Integer.valueOf(hour);
						if (i == hourInt) {
							hourList.set(i,
									hourList.get(i) + stepData.getStepNum());
						}
					}
				}
				int numSubcolumns = 1;
				int numColumns = 24;
				// Column can have many subcolumns, here by default I use 1
				// subcolumn in
				// each of 8 columns.
				List<Column> columns = new ArrayList<Column>();
				List<SubcolumnValue> values;
				for (int i = 0; i < numColumns; ++i) {
					values = new ArrayList<SubcolumnValue>();
					for (int j = 0; j < numSubcolumns; ++j) {
						Long step = hourList.get(i);
						if (step > 0) {
							values.add(new SubcolumnValue(Float.valueOf(step),
									Globals.pickColor()));
						}
					}
					Column column = new Column(values);
					boolean hasLabels = false; // 柱子上是否显示标识文字
					column.setHasLabels(hasLabels);
					boolean hasLabelForSelected = true; // 柱子被点击时，是否显示标识的文字
					column.setHasLabelsOnlyForSelected(hasLabelForSelected);
					columns.add(column);
				}
				// 存放柱状图数据的对象
				ColumnChartData data = new ColumnChartData(columns);
				boolean hasAxes = true; // 是否有坐标轴
				if (hasAxes) {
					Axis axisX = new Axis();
					Axis axisY = new Axis().setHasLines(true);
					boolean hasAxesNames = false; // 是否有坐标轴的名字
					if (hasAxesNames) {
						axisX.setName("Axis X");
						axisY.setName("Axis Y");
					}
					data.setAxisXBottom(axisX);
					data.setAxisYLeft(axisY);
				} else {
					data.setAxisXBottom(null);
					data.setAxisYLeft(null);
				}
				chart.setColumnChartData(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 图像的监听
	 */
	private class ValueTouchListener
			implements ColumnChartOnValueSelectListener {
		@Override
		public void onValueSelected(int columnIndex, int subcolumnIndex,
				SubcolumnValue value) {
			// showToast("Selected: " + value);
		}

		@Override
		public void onValueDeselected() {
		}
	}
}
