package com.evangel.pedometer;

import com.evangel.pedometer.activity.HistoryActivity;
import com.evangel.pedometer.view.StepArcView;
import com.evangel.pedometerlib.ISportStepInterface;
import com.evangel.pedometerlib.TodayStepManager;
import com.evangel.pedometerlib.TodayStepService;

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

	private void assignViews() {
		tv_data = (TextView) findViewById(R.id.tv_data);
		cc = (StepArcView) findViewById(R.id.cc);
		tv_set = (TextView) findViewById(R.id.tv_set);
	}

	private void addListener() {
		tv_set.setOnClickListener(this);
		tv_data.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		assignViews();
		addListener();
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
		stepTextView.setText(mStepSum + "步");
		cc.setCurrentCount(5000, mStepSum);
	}

	public void onClick(View view) {
		switch (view.getId()) {
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
		case R.id.stepArrayButton: {
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
		case R.id.stepArrayButton1: {
			// 根据时间来获取步数列表
			if (null != iSportStepInterface) {
				try {
					String stepArray = iSportStepInterface
							.getTodaySportStepArrayByDate("2018-01-19");
					mStepArrayTextView.setText(stepArray);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			break;
		}
		case R.id.stepArrayButton2: {
			// 获取多天步数列表
			if (null != iSportStepInterface) {
				try {
					String stepArray = iSportStepInterface
							.getTodaySportStepArrayByStartDateAndDays(
									"2018-01-20", 6);
					mStepArrayTextView.setText(stepArray);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			break;
		}
		default:
			break;
		}
	}
}
