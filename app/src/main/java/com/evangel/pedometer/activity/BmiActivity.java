package com.evangel.pedometer.activity;

import com.evangel.pedometer.R;
import com.evangel.pedometer.view.RoundIndicatorView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * BMI
 */
public class BmiActivity extends AppCompatActivity
		implements View.OnClickListener {
	private ImageView iv_left;
	private RoundIndicatorView roundIndicatorView;

	private void assignViews() {
		iv_left = findViewById(R.id.iv_left);
		roundIndicatorView = findViewById(R.id.my_view);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_bmi);
		assignViews();
		initData();
		addListener();
	}

	public void initData() {
		Intent intent = getIntent();
		float bmi = intent.getFloatExtra("bmi", 0f);
		roundIndicatorView.setCurrentNumAnim(bmi);
	}

	public void addListener() {
		iv_left.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		}
	}
}
