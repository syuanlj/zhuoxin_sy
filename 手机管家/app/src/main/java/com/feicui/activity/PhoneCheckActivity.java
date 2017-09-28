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
	private ListView exListView; // 　手检测信息
	private LinearLayout ll_Parent;//父布局，用来动态设置渐变背景
	private PhoneCheckAdapter adapter;
	//当前电池电量
	private int current;
	//电池温度
	public int temperature;
	private boolean isUse=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_check);
		setTitle("手机检测").setTextColor(Color.WHITE);
		
		initView();
		initEvent();
		initReceiver();
		//
		exListView = (ListView) findViewById(R.id.listviewLoad);
		adapter = new PhoneCheckAdapter(this);
		exListView.setAdapter(adapter);
		// 初始化手机检测信息
				new Thread(new Runnable() {
					@Override
					public void run() {
						initAdapterData();
					}
				}).start();
		
	}
	
	/**
	 * 获取手机硬件信息并绑定在adapter中，在ListView中显示
	 */
	public void initAdapterData() {
		pb_loading.setVisibility(View.VISIBLE);
		exListView.setVisibility(View.INVISIBLE);
		PhoneManager manager = PhoneManager.getPhoneManage(this);
		String title;
		String text;
		Drawable icon;
		//
		title = "设备名称:" + manager.getPhoneName1();
		text = "系统版本:" + manager.getPhoneSystemVersion();
		icon = getResources().getDrawable(R.drawable.setting_info_icon_version);
		final PhoneInfo info1 = new PhoneInfo(title, text, icon);
		//
		title = "全部运行内存"
				+ CommonUtil
						.getFileSize(MemoryManager.getPhoneTotalRamMemory());
		text = "剩余运行内存"
				+ CommonUtil.getFileSize(MemoryManager
						.getPhoneFreeRamMemory(this));
		icon = getResources().getDrawable(R.drawable.setting_info_icon_space);
		final PhoneInfo info2 = new PhoneInfo(title, text, icon);
		//
		title = "cpu名称:" + manager.getPhoneCpuName();
		text = "cpu数量:" + manager.getPhoneCpuNumber();
		icon = getResources().getDrawable(R.drawable.setting_info_icon_cpu);
		final PhoneInfo info3 = new PhoneInfo(title, text, icon);
		//
		title = "手机分辩率:" + manager.getResolution();
		text = "相机分辩率:" + manager.getMaxPhotoSize();
		icon = getResources().getDrawable(R.drawable.setting_info_icon_camera);
		final PhoneInfo info4 = new PhoneInfo(title, text, icon);
		//
		title = "基带版本:" + manager.getPhoneSystemBasebandVersion();
		text = "是否ROOT:" + (manager.isRoot() ? "是" : "否");
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
	//颜色渐变的监听
	private OnColorChangeListener onColorChangeListener=new OnColorChangeListener() {
		
		@Override
		public void onColorChangeListener(int red, int green) {
			Color color=new Color();
			//将RGB三色素，转为int值
			int backColor=color.argb(180, red, green, 0);
			//设置渐变背景色
			ll_Parent.setBackgroundColor(backColor);
			
		}
	};
	private void initView() {
		bv_pc=(BatteryView) findViewById(R.id.bv_pc);
		
		pb_loading = (ProgressBar) findViewById(R.id.progressBar);
		
		ll_Parent=(LinearLayout) findViewById(R.id.ll_Parent);
	}

	//广播注册
	private void initReceiver() {
		
		receiver=new BatteryReceiver();
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		
		registerReceiver(receiver, filter);
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//取消注册广播
		unregisterReceiver(receiver);
		//关闭波动的线程
		bv_pc.cancelTimer();
	}

	public class BatteryReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
				Bundle bundle=intent.getExtras();
				//总电量
				int allPower=bundle.getInt(BatteryManager.EXTRA_SCALE);
				//获得当前电量
				 current=bundle.getInt(BatteryManager.EXTRA_LEVEL);
				 temperature=(Integer)bundle.get(BatteryManager.EXTRA_TEMPERATURE);
				//获取电量百分比
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
		builder.setTitle("电池信息")
		//电池温度的单位是0.1，所以要*0.1
		.setItems(new String[]{"当前电量："+current ,"电池温度："+temperature*0.1}, null)
		.show();
		
	}
	
}
