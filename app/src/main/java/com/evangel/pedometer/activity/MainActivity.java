package com.evangel.pedometer.activity;

import com.evangel.pedometer.R;
import com.evangel.pedometer.util.Globals;
import com.evangel.pedometer.util.NumAnim;
import com.evangel.pedometer.util.SharedPreferencesUtil;
import com.evangel.pedometer.util.StepChartUtil;
import com.evangel.pedometer.util.ValueTouchListener;
import com.evangel.pedometer.util.WeakRefHandler;
import com.evangel.pedometer.view.StepArcView;
import com.evangel.pedometerlib.DateUtils;
import com.evangel.pedometerlib.ISportStepInterface;
import com.evangel.pedometerlib.SportStepJsonUtils;
import com.evangel.pedometerlib.TodayStepManager;
import com.evangel.pedometerlib.TodayStepService;
import com.orhanobut.logger.Logger;

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
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import lecho.lib.hellocharts.view.ColumnChartView;

public class MainActivity extends AppCompatActivity
		implements View.OnClickListener {
	private static final int REFRESH_STEP_WHAT = 0;
	/**
	 * 循环取当前时刻的步数中间的间隔时间
	 */
	private long TIME_INTERVAL_REFRESH = 500;
	private TodayStepCounterCall todayStepCounterCall = new TodayStepCounterCall();
	private Handler mDelayHandler = new WeakRefHandler(todayStepCounterCall);
	private int mStepSum;
	private ISportStepInterface iSportStepInterface;
	private TextView tv_data;
	private StepArcView sav_step;
	private TextView tv_set;
	private SharedPreferencesUtil sp;
	private TextView tv_step;
	private TextView tv_km;
	private TextView tv_calorie;
	/**
	 * 柱状图的自定义View
	 */
	private ColumnChartView chart;

	private void assignViews() {
		tv_data = findViewById(R.id.tv_data);
		sav_step = findViewById(R.id.sav_step);
		tv_set = findViewById(R.id.tv_set);
		tv_step = findViewById(R.id.tv_step);
		tv_km = findViewById(R.id.tv_km);
		tv_calorie = findViewById(R.id.tv_calorie);
		chart = findViewById(R.id.column_chart);
	}

	private void addListener() {
		tv_set.setOnClickListener(this);
		tv_data.setOnClickListener(this);
		chart.setOnValueTouchListener(new ValueTouchListener());
	}

	private void initData() {
		sp = new SharedPreferencesUtil(this);
		// 设置当前步数为0
		sav_step.setCurrentCount(Globals.getPlanWalk(sp), 0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		Globals.setContentViewAndStatusBar(this, R.layout.activity_main);
		assignViews();
		addListener();
		initData();
		// 初始化计步模块
		TodayStepManager.init(getApplication());
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
		Logger.i("updateStepCount : " + mStepSum);
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
				StepChartUtil.drawColumnChart(chart, stepArray);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 返回桌面，不退出应用
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
