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
		//ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		tag = this.getClass().getSimpleName();

		Log.e(tag, "onCreate");
	}

	/**
	 * ����������ת���Y����ǰ����
	 * 
	 * @param mClass
	 *            Ŀ��activity
	 */
	protected void startActivity(Class<?> mClass) {

		Intent intent = new Intent(this, mClass);
		startActivity(intent);

	}
	/**
	 * ����������ת���Y����ǰ����,���Ҵ�ֵ
	 * 
	 * @param mClass
	 *            Ŀ��activity
	 */
	protected void startActivity(Class<?> mClass,Bundle bundle) {

		Intent intent = new Intent(this, mClass);
		if(bundle!=null){
			intent.putExtras(bundle);
		}
		startActivity(intent);

	}
	/**
	 * ����������ת�����ҽ�����ǰ����
	 * 
	 * @param mClass
	 *            Ŀ��activity
	 */
	protected void startActivityFinish(Class<?> mClass) {

		Intent intent = new Intent(this, mClass);
		startActivity(intent);
		this.finish();
	}

	/**
	 * ����ֻ�з��ؼ�����Title�ķ���
	 * @param title  Ҫ��ʾ�ı���
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
	 * ����������Title�ķ���
	 * 
	 */
	protected void setTitle() {
		 OnClickListener left_click = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �����Լ��е���ת����
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
		titleView.setTitleView("�ֻ��ܼ�", R.drawable.ic_launcher,
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
