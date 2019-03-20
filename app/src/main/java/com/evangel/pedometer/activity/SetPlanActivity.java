package com.evangel.pedometer.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.evangel.pedometer.R;
import com.evangel.pedometer.util.Globals;
import com.evangel.pedometer.util.LeakUtil;
import com.evangel.pedometer.util.SharedPreferencesUtil;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * 设置锻炼计划
 */
public class SetPlanActivity extends AppCompatActivity
		implements View.OnClickListener {
	private SharedPreferencesUtil sp;
	private ImageView iv_left;
	private ImageView iv_right;
	private EditText tv_step_number;
	private CheckBox cb_remind;
	private TextView tv_remind_time;
	private Button btn_save;
	private Button btn_bmi;
	private EditText tv_height;
	private EditText tv_weight;
	private TextView tv_copyright;
	private TextView tv_copyright_en;

	private void assignViews() {
		iv_left = findViewById(R.id.iv_left);
		iv_right = findViewById(R.id.iv_right);
		tv_step_number = findViewById(R.id.tv_step_number);
		cb_remind = findViewById(R.id.cb_remind);
		tv_remind_time = findViewById(R.id.tv_remind_time);
		btn_save = findViewById(R.id.btn_save);
		btn_bmi = findViewById(R.id.btn_bmi);
		tv_height = findViewById(R.id.tv_height);
		tv_weight = findViewById(R.id.tv_weight);
		tv_copyright = findViewById(R.id.tv_copyright);
		tv_copyright_en = findViewById(R.id.tv_copyright_en);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_exercise_plan);
		assignViews();
		initData();
		addListener();
	}

	public void initData() {
		sp = new SharedPreferencesUtil(this);
		String planWalk_QTY = (String) sp.getParam(Globals.PLAN_WALK_KEY,
				Globals.PLAN_WALK_QTY);
		String remind = (String) sp.getParam("remind", "1");
		String achieveTime = (String) sp.getParam("achieveTime", "20:00");
		if (!planWalk_QTY.isEmpty()) {
			if ("0".equals(planWalk_QTY)) {
				tv_step_number.setText(Globals.PLAN_WALK_QTY);
			} else {
				tv_step_number.setText(planWalk_QTY);
			}
		}
		if (!remind.isEmpty()) {
			if ("0".equals(remind)) {
				cb_remind.setChecked(false);
			} else if ("1".equals(remind)) {
				cb_remind.setChecked(true);
			}
		}
		if (!achieveTime.isEmpty()) {
			tv_remind_time.setText(achieveTime);
		}
		String height = (String) sp.getParam(Globals.HEIGHT_KEY,
				Globals.HEIGHT_VALUE);
		String weight = (String) sp.getParam(Globals.WEIGHT_KEY,
				Globals.WEIGHT_VALUE);
		if (!height.isEmpty()) {
			if ("0".equals(height)) {
				tv_height.setText(Globals.HEIGHT_VALUE);
			} else {
				tv_height.setText(height);
			}
		}
		if (!weight.isEmpty()) {
			if ("0".equals(weight)) {
				tv_weight.setText(Globals.WEIGHT_VALUE);
			} else {
				tv_weight.setText(weight);
			}
		}
	}

	public void addListener() {
		iv_left.setOnClickListener(this);
		iv_right.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		btn_bmi.setOnClickListener(this);
		tv_remind_time.setOnClickListener(this);
		tv_copyright.setOnClickListener(this);
		tv_copyright_en.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.btn_save:
			save();
			Globals.showToast(this, "保存成功");
			break;
		case R.id.tv_remind_time:
			showTimeDialog();
			break;
		case R.id.btn_bmi:
			float bmi = Globals.getBmi(tv_height.getText().toString(),
					tv_weight.getText().toString());
			Intent intent = new Intent(this, BmiActivity.class);
			intent.putExtra("bmi", bmi);
			startActivity(intent);
			break;
		case R.id.tv_copyright:
			startActivity(new Intent(this, CopyrightActivity.class));
			break;
		case R.id.tv_copyright_en:
			startActivity(new Intent(this, CopyrightActivity.class));
			break;
		}
	}

	private void save() {
		String walk_qty = tv_step_number.getText().toString().trim();
		String remind;
		if (cb_remind.isChecked()) {
			remind = "1";
		} else {
			remind = "0";
		}
		String achieveTime = tv_remind_time.getText().toString().trim();
		if (walk_qty.isEmpty() || "0".equals(walk_qty)) {
			sp.setParam(Globals.PLAN_WALK_KEY, Globals.PLAN_WALK_QTY);
		} else {
			sp.setParam(Globals.PLAN_WALK_KEY, walk_qty);
		}
		sp.setParam("remind", remind);
		if (achieveTime.isEmpty()) {
			sp.setParam("achieveTime", "21:00");
			achieveTime = "21:00";
		} else {
			sp.setParam("achieveTime", achieveTime);
		}
		String height = tv_height.getText().toString().trim();
		String weight = tv_weight.getText().toString().trim();
		if (height.isEmpty() || "0".equals(height)) {
			sp.setParam(Globals.HEIGHT_KEY, Globals.HEIGHT_VALUE);
		} else {
			sp.setParam(Globals.HEIGHT_KEY, height);
		}
		if (weight.isEmpty() || "0".equals(weight)) {
			sp.setParam(Globals.WEIGHT_KEY, Globals.WEIGHT_VALUE);
		} else {
			sp.setParam(Globals.WEIGHT_KEY, weight);
		}
		finish();
	}

	private void showTimeDialog() {
		final Calendar calendar = Calendar.getInstance(Locale.CHINA);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		final DateFormat df = new SimpleDateFormat("HH:mm");
		new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				String remaintime = calendar.get(Calendar.HOUR_OF_DAY) + ":"
						+ calendar.get(Calendar.MINUTE);
				Date date = null;
				try {
					date = df.parse(remaintime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (null != date) {
					calendar.setTime(date);
				}
				tv_remind_time.setText(df.format(date));
			}
		}, hour, minute, true).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LeakUtil.clearTextLineCache();
	}
}
