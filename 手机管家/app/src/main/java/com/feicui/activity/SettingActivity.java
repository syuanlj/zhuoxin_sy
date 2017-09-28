package com.feicui.activity;

import com.feicui.base.BaseActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

public class SettingActivity extends BaseActivity implements OnCheckedChangeListener {

	RelativeLayout rl_setting_help;
	SharedPreferences sp;
	ToggleButton tb_toggle_notification;
	private Editor ed;
	private NotificationManager nm;
	private Editor edNotification;
	private SharedPreferences spNotification;
	
	
	private boolean isSN;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//Ϊ�˱��ִ���������Lead����
		sp=getSharedPreferences("isShowLead", MODE_PRIVATE);
		ed=sp.edit();
		//Ϊ�˱���notification״̬
		spNotification=getSharedPreferences("isShowNotification", 0);
		 edNotification=spNotification.edit();
		isSN=spNotification.getBoolean("isSN", false);
		setTitle("����");
		tb_toggle_notification=(ToggleButton) findViewById(R.id.tb_toggle_notification);
		rl_setting_help=(RelativeLayout) findViewById(R.id.rl_setting_help);
		rl_setting_help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ed.putBoolean("isFromSetting", true);
				ed.commit();
				startActivity(LeadActivity.class);
				
			}
		});
		tb_toggle_notification.setOnCheckedChangeListener(this);
		if(isSN){
			tb_toggle_notification.setChecked(true);
		}else {
			tb_toggle_notification.setChecked(false);
		}
		
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.tb_toggle_notification:
			if(isChecked){
				showNotification();
				 edNotification.putBoolean("isSN", true);
			}else{
				nm.cancel(1);
				edNotification.putBoolean("isSN", false);
			}
			edNotification.commit();
			break;

		default:
			break;
		}
		
	}
	/**
	 * ��ʾ֪ͨ
	 */
	private void showNotification() {
		//����֪ͨ��Ϣ����
		 nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent intent=new Intent(this, MainActivity.class);
		PendingIntent pintent=PendingIntent.getActivity(this, 0, intent, 0);
		//2�õ�notification����
		Notification notification=new Builder(this)
		.setContentTitle("֪ͨ����")
		.setContentText("֪ͨ��һ��")
		.setTicker("����Ϣ��")
		.setWhen(System.currentTimeMillis())
		.setContentIntent(pintent)
		.setSmallIcon(R.drawable.ic_launcher)
		.build();
		//3����֪ͨ
		
		nm.notify(1, notification);
		
	}
	

}
