package com.feicui.base;

import com.feicui.activity.AboutUsActivity;
import com.feicui.activity.R;
import com.feicui.activity.SettingActivity;
import com.feicui.utils.Toastutil;
import com.feicui.view.TitleView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class BaseActivity extends Activity {

	String tag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		tag = this.getClass().getSimpleName();

		Log.e(tag, "onCreate");
	}

	/**
	 * 用来界面跳转不結束当前界面
	 * 
	 * @param mClass
	 *            目标activity
	 */
	protected void startActivity(Class<?> mClass) {

		Intent intent = new Intent(this, mClass);
		startActivity(intent);

	}
	/**
	 * 用来界面跳转不結束当前界面,并且传值
	 * 
	 * @param mClass
	 *            目标activity
	 */
	protected void startActivity(Class<?> mClass,Bundle bundle) {

		Intent intent = new Intent(this, mClass);
		if(bundle!=null){
			intent.putExtras(bundle);
		}
		startActivity(intent);

	}
	/**
	 * 用来界面跳转，并且结束当前界面
	 * 
	 * @param mClass
	 *            目标activity
	 */
	protected void startActivityFinish(Class<?> mClass) {

		Intent intent = new Intent(this, mClass);
		startActivity(intent);
		this.finish();
	}

	/**
	 * 设置只有返回键界面Title的方法
	 * @param title  要显示的标题
	 */
	protected TitleView setTitle(String title) {
	  OnClickListener left_click = new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseActivity.this.finish();
			}
		};
		
		TitleView titleView = (TitleView) findViewById(R.id.titleView1);
		titleView.setTitleView(title, R.drawable.btn_homeasup_default, 0,
				left_click, null);
		return titleView;
	}
	
	/**
	 * 设置主界面Title的方法
	 * 
	 */
	protected void setTitle() {
		 OnClickListener left_click = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用自己中的跳转方法
				startActivity(AboutUsActivity.class);
			}
		};
		 OnClickListener right_click = new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(SettingActivity.class);

			}
		};
		TitleView titleView = (TitleView) findViewById(R.id.titleView1);
		titleView.setTitleView("手机管家", R.drawable.ic_launcher,
				R.drawable.ic_child_configs, left_click, right_click);
		titleView.setTextColor(Color.WHITE);
	}

	
	protected void shortToast(String text){
		Toastutil.shortToast(this, text);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e(tag, "onStart");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.e(tag, "onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(tag, "onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e(tag, "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e(tag, "onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e(tag, "onDestroy");
	}

	
	
}
