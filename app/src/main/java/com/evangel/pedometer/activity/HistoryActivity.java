package com.evangel.pedometer.activity;

import java.util.List;

import com.evangel.pedometer.R;
import com.evangel.pedometer.adapter.CommonAdapter;
import com.evangel.pedometer.adapter.CommonViewHolder;
import com.evangel.pedometer.bean.StepData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 查看历史步数
 */
public class HistoryActivity extends AppCompatActivity {
	private ImageView iv_left;
	private ListView lv;

	private void assignViews() {
		iv_left = (ImageView) findViewById(R.id.iv_left);
		lv = (ListView) findViewById(R.id.lv);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_history);
		assignViews();
		iv_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initData();
	}

	private void initData() {
		setEmptyView(lv);
		Intent intent = getIntent();
		String stepArray = intent.getStringExtra("stepArray");
		// Log.e(TAG, "stepArray : " + stepArray);
		Gson gson = new Gson();
		List<StepData> stepDataList = gson.fromJson(stepArray,
				new TypeToken<List<StepData>>() {
				}.getType());
		lv.setAdapter(
				new CommonAdapter<StepData>(this, stepDataList, R.layout.item) {
					@Override
					protected void convertView(View item, StepData stepData) {
						TextView tv_date = CommonViewHolder.get(item,
								R.id.tv_date);
						TextView tv_step = CommonViewHolder.get(item,
								R.id.tv_step);
						tv_date.setText(stepData.getToday());
						tv_step.setText(stepData.getStepNum() + "步");
					}
				});
	}

	protected <T extends View> T setEmptyView(ListView listView) {
		TextView emptyView = new TextView(this);
		emptyView.setLayoutParams(
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无数据！");
		emptyView.setGravity(Gravity.CENTER);
		emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
		return (T) emptyView;
	}
}
