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
	// һ��ҳ��listview
	private ListView lv_fileManager;
	// ��ʾ�ܹ��Ĵ�С
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
		setTitle("�ļ�����").setTextColor(Color.WHITE);

		initView();

		adapter = new MyFileManagerAdapter(this);

		adapter.add(new FileTypeInfo(-1, "�ĵ�", true));
		adapter.add(new FileTypeInfo(-1, "ͼƬ", true));
		adapter.add(new FileTypeInfo(-1, "��Ƶ", true));
		adapter.add(new FileTypeInfo(-1, "��Ƶ", true));
		adapter.add(new FileTypeInfo(-1, "ѹ����", true));
		adapter.add(new FileTypeInfo(-1, "��װ��", true));
		adapter.add(new FileTypeInfo(-1, "����", true));
		lv_fileManager.setAdapter(adapter);
		getData();
		initEvent();
	}

	/**
	 * �¼������ķ���
	 */
	private void initEvent() {
		lv_fileManager.setOnItemClickListener(itemListener);
	}

	// ��Ŀ�ĵ������
	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (!isTaskCancle) {
				shortToast("�ļ�����δ����");
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
			// ��һ�����棬��������洫ֵ
			startActivityForResult(intent, 1, bundle);

		}
	};

	/**
	 * �������մ��ݹ�������
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == 2) {
				Bundle bundle = data.getExtras();
				long oneTypeSize = bundle.getLong("oneTypeSize");
				int position = bundle.getInt("position");
				FileTypeInfo info = adapter.getItem(position);
				// �ı�������ļ����ܴ�С
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

		// �߳̽�����Ļص�
		@Override
		public void onResultListener(String result) {

			if (result.equals("ok")) {
				for (int i = 0; i < 7; i++) {
					FileTypeInfo info = adapter.getItem(i);
					// ���û����
					if (info.getFileSize() == -1) {
						info.setFileSize(0);
					}
					// ��ʾ��ͷ�����ؽ�����
					info.setLoding(false);

				}
			}
			// ѭ��������ˢ��������
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
			// ����λ�ã���ȡÿһ��item���ݣ�ȥ�ı��Ӧ���ļ���С
			FileTypeInfo info = adapter.getItem(position);
			info.setFileSize(oneTypeSize);
			// ˢ��������
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
	 * ����������file��С�Ϳ̶��̽Ƕ�
	 */
	public void setTextForSize() {
		// �����ÿռ�
		long pss = MemoryManager.getPhoneSelfSDCardSize();
		// ���ÿ��пռ�
		long pfss = MemoryManager.getPhoneSelfSDCardFreeSize();
		// ������ʹ�ÿռ�
		long puss = pss - pfss;

		tv_size.setText(CommonUtil.getFileSize(puss) + "/"
				+ CommonUtil.getFileSize(pss));
		
		int score = (int) ((float)puss/pss*100);
		tv_score.setText(score+"");
		kedu.changeAngle(score*3);
	}

}
