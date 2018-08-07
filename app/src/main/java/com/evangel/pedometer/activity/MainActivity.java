package com.evangel.pedometer.activity;

import com.evangel.pedometer.R;
import com.evangel.pedometer.app.TSApplication;
import com.evangel.pedometer.step.utils.Globals;
import com.evangel.pedometer.step.utils.SharedPreferencesUtils;
import com.evangel.pedometer.view.StepArcView;
import com.evangel.pedometerlib.ISportStepInterface;
import com.evangel.pedometerlib.TodayStepManager;
import com.evangel.pedometerlib.TodayStepService;
import com.evangel.pedometerlib.utils.LibGlobals;

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

public class MainActivity extends AppCompatActivity
		implements View.OnClickListener {
	private static String TAG = "MainActivity";
	private static final int REFRESH_STEP_WHAT = 0;
	// 循环取当前时刻的步数中间的间隔时间
	private long TIME_INTERVAL_REFRESH = 500;
	private Handler mDelayHandler = new Handler(new TodayStepCounterCall());
	private int mStepSum;
	private ISportStepInterface iSportStepInterface;
	private TextView mStepArrayTextView;
	private TSApplication tsApplication;
	private TextView tv_data;
	private StepArcView cc;
	private TextView tv_set;
	private SharedPreferencesUtils sp;
	private TextView stepArrayView;

	private void assignViews() {
		tv_data = (TextView) findViewById(R.id.tv_data);
		cc = (StepArcView) findViewById(R.id.cc);
		tv_set = (TextView) findViewById(R.id.tv_set);
		stepArrayView = (TextView) findViewById(R.id.stepArrayView);
	}

	private void addListener() {
		tv_set.setOnClickListener(this);
		tv_data.setOnClickListener(this);
		stepArrayView.setOnClickListener(this);
	}

	private void initData() {
		sp = new SharedPreferencesUtils(this);
		// 获取用户设置的计划锻炼步数，没有设置过的话默认
		String planWalk_QTY = (String) sp.getParam(Globals.PLAN_WALK_KEY,
				Globals.PLAN_WALK_QTY);
		// 设置当前步数为0
		cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
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
		mStepArrayTextView = (TextView) findViewById(R.id.stepArrayTextView);
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
					updateStepCount();
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
						updateStepCount();
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

	private void updateStepCount() {
		Log.e(TAG, "updateStepCount : " + mStepSum);
		TextView stepTextView = (TextView) findViewById(R.id.stepTextView);
		stepTextView.setText(LibGlobals.getKmCalorieByStep(mStepSum));
		String planWalk_QTY = (String) sp.getParam(Globals.PLAN_WALK_KEY,
				Globals.PLAN_WALK_QTY);
		cc.setCurrentCount(Integer.parseInt(planWalk_QTY), mStepSum);
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
		case R.id.stepArrayView: {
			// 获取所有步数列表
			if (null != iSportStepInterface) {
				try {
					String stepArray = iSportStepInterface
							.getTodaySportStepArray();
					mStepArrayTextView.setText(stepArray);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			break;
		}
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
}
