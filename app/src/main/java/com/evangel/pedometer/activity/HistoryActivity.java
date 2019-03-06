package com.evangel.pedometer.activity;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import java.io.File;
import java.util.List;

import com.evangel.pedometer.R;
import com.evangel.pedometer.adapter.CommonAdapter;
import com.evangel.pedometer.adapter.CommonViewHolder;
import com.evangel.pedometer.app.RestartAppTool;
import com.evangel.pedometer.bean.StepData;
import com.evangel.pedometer.util.FileUtil;
import com.evangel.pedometer.util.Globals;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 查看历史步数
 */
public class HistoryActivity extends AppCompatActivity {
	private ImageView iv_left;
	private ListView lv;
	private Button btn_backup;
	private Button btn_restore;
	private static final String DATA_FILE_NAME = "TodayStepDB.db";

	private void assignViews() {
		iv_left = findViewById(R.id.iv_left);
		lv = findViewById(R.id.lv);
		btn_backup = findViewById(R.id.btn_backup);
		btn_restore = findViewById(R.id.btn_restore);
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
		btn_backup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				backup();
			}
		});
		btn_restore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showNormalDialog();
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

	private void backup() {
		File downloadFile = Environment
				.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
		String oldPath = getDatabasePath(DATA_FILE_NAME).getAbsolutePath();
		String newPath = downloadFile.toString() + File.separator
				+ DATA_FILE_NAME;
		FileUtil.copyFile(oldPath, newPath);
		Globals.showToast(this, "备份成功！路径：" + downloadFile.getName()
				+ File.separator + DATA_FILE_NAME);
	}

	private void restore() {
		File downloadFile = Environment
				.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
		String oldPath = downloadFile.toString() + File.separator
				+ DATA_FILE_NAME;
		String newPath = getDatabasePath(DATA_FILE_NAME).getAbsolutePath();
		File oldfile = new File(oldPath);
		if (oldfile.exists()) {
			FileUtil.copyFile(oldPath, newPath);
			Globals.showToast(this, "还原成功！正在重启...");
			RestartAppTool.restartApp(this);
		} else {
			Globals.showToast(this, "请确认还原文件：" + downloadFile.getName()
					+ File.separator + DATA_FILE_NAME);
		}
	}

	private void showNormalDialog() {
		/*
		 * @setIcon 设置对话框图标
		 * 
		 * @setTitle 设置对话框标题
		 * 
		 * @setMessage 设置对话框消息提示 setXXX方法返回Dialog对象，因此可以链式设置属性
		 */
		final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
		normalDialog.setTitle("还原提示");
		normalDialog.setMessage("请谨慎操作，确定还原吗？");
		normalDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						restore();
					}
				});
		normalDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		// 显示
		normalDialog.show();
	}
}
