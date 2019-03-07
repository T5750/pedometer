package com.evangel.pedometer.activity;

import com.evangel.pedometer.R;
import com.evangel.pedometer.util.NetworkUtil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Copyright<br/>
 * Android：最全面的Webview详解<br/>
 * https://blog.csdn.net/carson_ho/article/details/52693322<br/>
 * android中WebView加载网页设置进度条<br/>
 * https://blog.csdn.net/rocrocflying/article/details/49850095
 */
public class CopyrightActivity extends AppCompatActivity
		implements View.OnClickListener {
	private ImageView iv_left;
	private TextView tv_title;
	private WebView wv_copyright;
	private ProgressBar pb_loading;
	public static final String BLOG = "https://t5750.github.io";

	private void assignViews() {
		iv_left = findViewById(R.id.iv_left);
		tv_title = findViewById(R.id.tv_title);
		pb_loading = findViewById(R.id.pb_loading);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_copyright);
		assignViews();
		initData();
		addListener();
	}

	public void initData() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		wv_copyright = new WebView(getApplicationContext());
		wv_copyright.setLayoutParams(params);
		if (NetworkUtil.isNetworkConnected(getApplicationContext())) {
			wv_copyright.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
		} else {
			wv_copyright.getSettings()
					.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 没网，则从本地获取，即离线加载
		}
		wv_copyright.getSettings().setJavaScriptEnabled(true);
		wv_copyright.loadUrl(BLOG);
		wv_copyright.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		// 设置WebChromeClient类
		wv_copyright.setWebChromeClient(new WebChromeClient() {
			// 获取网站标题
			@Override
			public void onReceivedTitle(WebView view, String title) {
				tv_title.setText(title);
			}

			// 获取加载进度
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress < 100) {
					pb_loading.setVisibility(View.VISIBLE);// 开始加载网页时显示进度条
					pb_loading.setProgress(newProgress);// 设置进度值
				} else if (newProgress == 100) {
					pb_loading.setVisibility(View.GONE);// 加载完网页进度条消失
				}
			}
		});
		LinearLayout layout_copyright = findViewById(R.id.layout_copyright);
		layout_copyright.addView(wv_copyright);
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

	/**
	 * 点击返回上一页面而不是退出浏览器
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && wv_copyright.canGoBack()) {
			wv_copyright.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 销毁Webview
	 */
	@Override
	protected void onDestroy() {
		if (wv_copyright != null) {
			wv_copyright.loadDataWithBaseURL(null, "", "text/html", "utf-8",
					null);
			wv_copyright.clearHistory();
			((ViewGroup) wv_copyright.getParent()).removeView(wv_copyright);
			wv_copyright.destroy();
			wv_copyright = null;
		}
		super.onDestroy();
	}
}
