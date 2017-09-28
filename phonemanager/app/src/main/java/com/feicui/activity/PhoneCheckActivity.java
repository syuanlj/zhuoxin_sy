package com.feicui.activity;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.feicui.adapter.PhoneCheckAdapter;
import com.feicui.base.BaseActivity;
import com.feicui.bean.PhoneInfo;
import com.feicui.manager.MemoryManager;
import com.feicui.manager.PhoneManager;
import com.feicui.utils.CommonUtil;
import com.feicui.view.BatteryView;
import com.feicui.view.BatteryView.OnColorChangeListener;

public class PhoneCheckActivity extends BaseActivity implements OnClickListener {
	private BatteryReceiver receiver;
	private BatteryView bv_pc;
	private ProgressBar pb_loading;
	private ListView exListView; // ���ּ����Ϣ
	private LinearLayout ll_Parent;//�����֣�������̬���ý��䱳��
	private PhoneCheckAdapter adapter;
	//��ǰ��ص���
	private int current;
	//����¶�
	public int temperature;
	private boolean isUse=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_check);
		setTitle("�ֻ����").setTextColor(Color.WHITE);
		
		initView();
		initEvent();
		initReceiver();
		//
		exListView = (ListView) findViewById(R.id.listviewLoad);
		adapter = new PhoneCheckAdapter(this);
		exListView.setAdapter(adapter);
		// ��ʼ���ֻ������Ϣ
				new Thread(new Runnable() {
					@Override
					public void run() {
						initAdapterData();
					}
				}).start();
		
	}
	
	/**
	 * ��ȡ�ֻ�Ӳ����Ϣ������adapter�У���ListView����ʾ
	 */
	public void initAdapterData() {
		pb_loading.setVisibility(View.VISIBLE);
		exListView.setVisibility(View.INVISIBLE);
		PhoneManager manager = PhoneManager.getPhoneManage(this);
		String title;
		String text;
		Drawable icon;
		//
		title = "�豸����:" + manager.getPhoneName1();
		text = "ϵͳ�汾:" + manager.getPhoneSystemVersion();
		icon = getResources().getDrawable(R.drawable.setting_info_icon_version);
		final PhoneInfo info1 = new PhoneInfo(title, text, icon);
		//
		title = "ȫ�������ڴ�"
				+ CommonUtil
						.getFileSize(MemoryManager.getPhoneTotalRamMemory());
		text = "ʣ�������ڴ�"
				+ CommonUtil.getFileSize(MemoryManager
						.getPhoneFreeRamMemory(this));
		icon = getResources().getDrawable(R.drawable.setting_info_icon_space);
		final PhoneInfo info2 = new PhoneInfo(title, text, icon);
		//
		title = "cpu����:" + manager.getPhoneCpuName();
		text = "cpu����:" + manager.getPhoneCpuNumber();
		icon = getResources().getDrawable(R.drawable.setting_info_icon_cpu);
		final PhoneInfo info3 = new PhoneInfo(title, text, icon);
		//
		title = "�ֻ��ֱ���:" + manager.getResolution();
		text = "����ֱ���:" + manager.getMaxPhotoSize();
		icon = getResources().getDrawable(R.drawable.setting_info_icon_camera);
		final PhoneInfo info4 = new PhoneInfo(title, text, icon);
		//
		title = "�����汾:" + manager.getPhoneSystemBasebandVersion();
		text = "�Ƿ�ROOT:" + (manager.isRoot() ? "��" : "��");
		icon = getResources().getDrawable(R.drawable.setting_info_icon_root);
		final PhoneInfo info5 = new PhoneInfo(title, text, icon);
		//
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.add(info1);
				adapter.add(info2);
				adapter.add(info3);
				adapter.add(info4);
				adapter.add(info5);
				adapter.notifyDataSetChanged();
				pb_loading.setVisibility(View.INVISIBLE);
				exListView.setVisibility(View.VISIBLE);
			}
		});
	}
	private void initEvent() {
		bv_pc.setOnClickListener(this);
		bv_pc.setOnColorChangeListener(onColorChangeListener);
	}
	//��ɫ����ļ���
	private OnColorChangeListener onColorChangeListener=new OnColorChangeListener() {
		
		@Override
		public void onColorChangeListener(int red, int green) {
			Color color=new Color();
			//��RGB��ɫ�أ�תΪintֵ
			int backColor=color.argb(180, red, green, 0);
			//���ý��䱳��ɫ
			ll_Parent.setBackgroundColor(backColor);
			
		}
	};
	private void initView() {
		bv_pc=(BatteryView) findViewById(R.id.bv_pc);
		
		pb_loading = (ProgressBar) findViewById(R.id.progressBar);
		
		ll_Parent=(LinearLayout) findViewById(R.id.ll_Parent);
	}

	//�㲥ע��
	private void initReceiver() {
		
		receiver=new BatteryReceiver();
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		
		registerReceiver(receiver, filter);
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//ȡ��ע��㲥
		unregisterReceiver(receiver);
		//�رղ������߳�
		bv_pc.cancelTimer();
	}

	public class BatteryReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
				Bundle bundle=intent.getExtras();
				//�ܵ���
				int allPower=bundle.getInt(BatteryManager.EXTRA_SCALE);
				//��õ�ǰ����
				 current=bundle.getInt(BatteryManager.EXTRA_LEVEL);
				 temperature=(Integer)bundle.get(BatteryManager.EXTRA_TEMPERATURE);
				//��ȡ�����ٷֱ�
				int precent=(int)(current/(float)allPower*100);
				if(isUse){
					bv_pc.upWaterView(precent);
					isUse=false;
				}
			}
			
		}
		
	}

	@Override
	public void onClick(View v) {
		Builder builder=new Builder(this);
		builder.setTitle("�����Ϣ")
		//����¶ȵĵ�λ��0.1������Ҫ*0.1
		.setItems(new String[]{"��ǰ������"+current ,"����¶ȣ�"+temperature*0.1}, null)
		.show();
		
	}
	
}
