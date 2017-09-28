package com.feicui.activity;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.feicui.adapter.SpeedUpAdapter;
import com.feicui.base.BaseActivity;
import com.feicui.bean.RunningAppInfo;
import com.feicui.manager.RunAppManager;
import com.feicui.manager.SystemManager;
import com.feicui.utils.CommonUtil;

public class SpeedUpActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private TextView tv_phoneName, tv_phoneModel, tv_ramMessage;
	private ProgressBar pb_ramUsed, pb_loding;
	private Button btn_clear;
	private ToggleButton tb_showapp;
	private ListView lv_loadProcess;
	private CheckBox cb_checkAll;
	private SpeedUpAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speed_up);
		setTitle("手机加速");
		initView();
		initEvent();
		initData();
		adapter = new SpeedUpAdapter(this);
		lv_loadProcess.setAdapter(adapter);
		// 获取进程数据
		addProcessData(1);
	}

	private void addProcessData(final int type) {
		lv_loadProcess.setVisibility(View.INVISIBLE);
		pb_loding.setVisibility(View.VISIBLE);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// 调用管理类中加载数据的方法
				RunAppManager.getRnAppManager(SpeedUpActivity.this).getRunApp();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adapter.clear();
						switch (type) {
						case 1:
							//类型为1添加用户进程
							adapter.add(RunAppManager.getUserList());
							break;
						case 2:
							//类型为2添加系统进程
							adapter.add(RunAppManager.getSysList());
							break;
						default:
							break;
						}
						adapter.notifyDataSetChanged();
						lv_loadProcess.setVisibility(View.VISIBLE);
						pb_loding.setVisibility(View.INVISIBLE);
					}
				});
			}
		}).start();

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 获得运行内存大小
		long allRam = com.feicui.manager.MemoryManager.getPhoneTotalRamMemory();
		// 获得运行内存已用大小
		long uesdRam = com.feicui.manager.MemoryManager
				.getPhoneTotalRamMemory()
				- com.feicui.manager.MemoryManager.getPhoneFreeRamMemory(this);
		// 将内存大小转为字符串
		String s = CommonUtil.getFileSize(uesdRam);

		String s1 = CommonUtil.getFileSize(allRam);
		tv_ramMessage.setText("已用内存" + s + "/" + s1);

		// 根据已用内存大小比例，设置进度条
		pb_ramUsed.setProgress((int) (uesdRam / (float) allRam * 100));
		// 设备名称
		tv_phoneName.setText(SystemManager.getPhoneName());
		// 设备型号
		tv_phoneModel.setText(SystemManager.getPhoneModelName());
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		// 一键清理的点击事件
		btn_clear.setOnClickListener(this);
		// 选择显示系统或者用户进程的togglebutton，状态改变监听
		tb_showapp.setOnCheckedChangeListener(this);
		// 全选框状态监听
		cb_checkAll.setOnCheckedChangeListener(this);

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 设备名称
		tv_phoneName = (TextView) findViewById(R.id.tv_phoneName);
		// 设备类型
		tv_phoneModel = (TextView) findViewById(R.id.tv_phoneModel);
		// ram使用情况
		tv_ramMessage = (TextView) findViewById(R.id.tv_ramMessage);
		// ram使用进度条
		pb_ramUsed = (ProgressBar) findViewById(R.id.pb_ramUsed);
		// 加载界面进度条
		pb_loding = (ProgressBar) findViewById(R.id.pb_loding);
		// 一键清理按钮
		btn_clear = (Button) findViewById(R.id.btn_clear);
		// 切换用户和系统
		tb_showapp = (ToggleButton) findViewById(R.id.tb_showapp);
		// 加载界面的进度条
		lv_loadProcess = (ListView) findViewById(R.id.lv_loadProcess);
		// 全部选择
		cb_checkAll = (CheckBox) findViewById(R.id.cb_checkAll);

	}

	//切换系统用户进程
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		switch (buttonView.getId()) {
		case R.id.tb_showapp:
			if(isChecked){//选择了就加载系统进程
				addProcessData(2);
			}else{//反之加载用户进程
				addProcessData(1);
			}
			break;
		case R.id.cb_checkAll:
			List<RunningAppInfo> infoLists = adapter.getList();
			for (RunningAppInfo runningAppInfo : infoLists) {
				
				runningAppInfo.setClear(isChecked);
			}
			adapter.notifyDataSetChanged();
			break;
			
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		//点击一键清理
		//拿到适配器中存放数据的集合
		List<RunningAppInfo> infoLists = adapter.getList();
		//查看集合中哪些数据可以被清理
		for (RunningAppInfo runningAppInfo : infoLists) {
			if(runningAppInfo.isClear()){
				//杀掉进程
				RunAppManager.killProcess(SpeedUpActivity.this, runningAppInfo.getPackageName());
			}
		}
		//情况适配器数据
		adapter.clear();
		//重新获取进程信息
		//根据toggleButton状态去判断，显示哪一种
		if(tb_showapp.isChecked()){
			addProcessData(2);
		}else{
			addProcessData(1);
		}
		
		initData();
		
	}
}
