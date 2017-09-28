package com.feicui.activity;

import java.io.File;
import java.util.concurrent.Executors;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.feicui.adapter.MyFileManagerAdapter;
import com.feicui.base.BaseActivity;
import com.feicui.bean.FileTypeInfo;
import com.feicui.manager.MemoryManager;
import com.feicui.manager.MyTask;
import com.feicui.manager.MyTask.ProgressListener;
import com.feicui.utils.CommonUtil;
import com.feicui.view.KeDuView;
import com.feicui.view.RadarUpView;

public class FileManagerActivity extends BaseActivity {
	// 一级页面listview
	private ListView lv_fileManager;
	// 显示总共的大小
	private TextView tv_allSize, tv_size, tv_score;
	private MyFileManagerAdapter adapter;
	private long fileSize;
	private RadarUpView ruv_fm;
	FrameLayout fl_radar, fl_result;
	protected boolean isTaskCancle;
	private KeDuView kedu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_manager);
		setTitle("文件管理").setTextColor(Color.WHITE);

		initView();

		adapter = new MyFileManagerAdapter(this);

		adapter.add(new FileTypeInfo(-1, "文档", true));
		adapter.add(new FileTypeInfo(-1, "图片", true));
		adapter.add(new FileTypeInfo(-1, "视频", true));
		adapter.add(new FileTypeInfo(-1, "音频", true));
		adapter.add(new FileTypeInfo(-1, "压缩包", true));
		adapter.add(new FileTypeInfo(-1, "安装包", true));
		adapter.add(new FileTypeInfo(-1, "其它", true));
		lv_fileManager.setAdapter(adapter);
		getData();
		initEvent();
	}

	/**
	 * 事件监听的方法
	 */
	private void initEvent() {
		lv_fileManager.setOnItemClickListener(itemListener);
	}

	// 条目的点击监听
	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (!isTaskCancle) {
				shortToast("文件搜索未结束");
				return;
			}
			Intent intent = new Intent(FileManagerActivity.this,
					FileListActivity.class);

			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			FileTypeInfo info = adapter.getItem(position);
			bundle.putLong("oneTypeSize", info.getFileSize());
			bundle.putString("fileType", info.getType());

			intent.putExtras(bundle);
			// 下一个界面，向这个界面传值
			startActivityForResult(intent, 1, bundle);

		}
	};

	/**
	 * 用来接收传递过的数据
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == 2) {
				Bundle bundle = data.getExtras();
				long oneTypeSize = bundle.getLong("oneTypeSize");
				int position = bundle.getInt("position");
				FileTypeInfo info = adapter.getItem(position);
				// 改变该类型文件的总大小
				info.setFileSize(oneTypeSize);
				adapter.notifyDataSetChanged();
				setTextForSize();
			}
		}
	};

	private MyTask myTask;

	private void getData() {
		File file = Environment.getExternalStorageDirectory();
		if (file.exists()) {
			myTask = new MyTask();
			myTask.executeOnExecutor(Executors.newCachedThreadPool(), file);
			myTask.setProgressListener(progressListener);
		}

	}

	private ProgressListener progressListener = new ProgressListener() {

		// 线程结束后的回调
		@Override
		public void onResultListener(String result) {

			if (result.equals("ok")) {
				for (int i = 0; i < 7; i++) {
					FileTypeInfo info = adapter.getItem(i);
					// 如果没内容
					if (info.getFileSize() == -1) {
						info.setFileSize(0);
					}
					// 显示箭头，隐藏进度条
					info.setLoding(false);

				}
			}
			// 循环结束，刷新适配器
			adapter.notifyDataSetChanged();
			ruv_fm.cancle();
			ruv_fm.setVisibility(View.GONE);
			fl_radar.setVisibility(View.GONE);
			fl_result.setVisibility(View.VISIBLE);
			isTaskCancle = true;
			setTextForSize();
			
		}

		@Override
		public void onProgressListener(long oneTypeSize, long allTyepSize,
				int position) {
			fileSize = oneTypeSize;
			tv_allSize.setText(CommonUtil.getFileSize(allTyepSize));
			// 根据位置，获取每一个item数据，去改变对应的文件大小
			FileTypeInfo info = adapter.getItem(position);
			info.setFileSize(oneTypeSize);
			// 刷新适配器
			adapter.notifyDataSetChanged();

		}
	};

	private void initView() {
		lv_fileManager = (ListView) findViewById(R.id.lv_fileManager);
		tv_allSize = (TextView) findViewById(R.id.tv_allSize);
		tv_size = (TextView) findViewById(R.id.tv_size);
		tv_score = (TextView) findViewById(R.id.tv_score);
		ruv_fm = (RadarUpView) findViewById(R.id.ruv_fm);
		fl_radar = (FrameLayout) findViewById(R.id.fl_radar);
		fl_result = (FrameLayout) findViewById(R.id.fl_result);
		kedu=(KeDuView) findViewById(R.id.kedu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		myTask.cancel(true);
	}

	
	/**
	 * 搜索结束后file大小和刻度盘角度
	 */
	public void setTextForSize() {
		// 总内置空间
		long pss = MemoryManager.getPhoneSelfSDCardSize();
		// 内置空闲空间
		long pfss = MemoryManager.getPhoneSelfSDCardFreeSize();
		// 内置已使用空间
		long puss = pss - pfss;

		tv_size.setText(CommonUtil.getFileSize(puss) + "/"
				+ CommonUtil.getFileSize(pss));
		
		int score = (int) ((float)puss/pss*100);
		tv_score.setText(score+"");
		kedu.changeAngle(score*3);
	}

}
