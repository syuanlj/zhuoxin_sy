package com.feicui.activity;

import java.util.List;

import com.feicui.adapter.SoftListAdapter;
import com.feicui.base.BaseActivity;
import com.feicui.bean.SoftList;
import com.feicui.manager.SoftManager;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SoftListActivity extends BaseActivity implements OnClickListener {

	ListView lv_softList;
	SoftListAdapter adapter;
	Button bt_delet;
	private SoftDeleteReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soft_list);
		initTitle();

		intiView();
		initEvent();
		lv_softList.setAdapter(adapter);
		//动态注册广播接收器
		initReceiver();
	}

	private void initReceiver() {
		//得到广播接收器的对象
		receiver=new SoftDeleteReceiver();
		//广播过滤，选择要监听的广播(软件的卸载)
		IntentFilter filter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		//信息的匹配类型
		filter.addDataScheme("package");
		//注册广播接收器的方法
		registerReceiver(receiver, filter);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//取消广播接收器对象
		unregisterReceiver(receiver);
	}
	private void initEvent() {
		bt_delet.setOnClickListener(this);

	}

	private void intiView() {
		lv_softList = (ListView) findViewById(R.id.lv_softList);

		lv_softList.setOnItemClickListener(listener);

		bt_delet = (Button) findViewById(R.id.bt_delet);

	}

	// listviewitem的点击监听
	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			openApp(adapter.getItem(position));

		}

	};
	private Bundle bundle;
	private int type;

	private void initTitle() {
		// 获取数据
		// 获得对象和将数据放到集合
		SoftManager.getSoftManager(this).getSoft();
		adapter = new SoftListAdapter(this);
		 bundle = getIntent().getExtras();
		 type = bundle.getInt("type");
		switch (type) {
		case 1:
			setTitle("所有软件列表");
			adapter.add(SoftManager.getAllList());
			break;
		case 2:
			setTitle("系统软件列表");
			adapter.add(SoftManager.getSysList());
			break;
		case 3:
			setTitle("用户软件列表");
			adapter.add(SoftManager.getUserList());
			break;
		default:
			break;
		}
	}

	/**
	 * 打开应用程序
	 */
	private void openApp(final SoftList info) {
		Builder builder = new Builder(SoftListActivity.this);
		builder.setIcon(R.drawable.ic_launcher).setTitle("启动软件")
				.setMessage("你确定要启动" + info.getName())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 通过包名得到intent对象
						Intent intent = getPackageManager()
								.getLaunchIntentForPackage(
										info.getPackageName());

						if (intent == null) {
							shortToast("无法启动");
						} else {
							startActivity(intent);
						}
					}
				}).setNegativeButton("取消", null).show();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_DELETE);

		List<SoftList> infoList = adapter.getList();
		for (SoftList softList : infoList) {

			if (softList.isChear()) {
				// 不卸载自己的
				if (softList.getPackageName().equals(this.getPackageName())) {
					shortToast("不能卸载自己");

				} else {
					intent.setData(Uri.parse("package:"
							+ softList.getPackageName()));
					startActivity(intent);
			
				}
			}
		}

	}
	
	/**
	 * 实现一个监听软件卸载的广播接收器
	 * 
	 */
	public class SoftDeleteReceiver extends BroadcastReceiver{

		//接收到，指定的广播时自动调用
		@Override
		public void onReceive(Context context, Intent intent) {
			//卸载完成后，清空适配器，重新获取最新数据
			adapter.clear();
			//卸载完成后，重新获取系统中的软件信息。
			SoftManager.getSoftManager(context).getSoft();
			switch (type) {//根据当前加载的种类，去重新加载。
			case 1:
				adapter.add(SoftManager.getAllList());
				break;
			case 2:
				adapter.add(SoftManager.getSysList());
				break;
			case 3:
				adapter.add(SoftManager.getUserList());
				break;
		}
			adapter.notifyDataSetChanged();
		
	}
	
}
}
