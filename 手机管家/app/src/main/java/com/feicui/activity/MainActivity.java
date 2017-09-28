package com.feicui.activity;

import java.text.DecimalFormat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feicui.base.BaseActivity;
import com.feicui.manager.MemoryManager;
import com.feicui.manager.RunAppManager;
import com.feicui.view.ClearView;
import com.feicui.view.MyView;
import com.feicui.view.MyView.OnAngleColorListener;

public class MainActivity extends BaseActivity implements OnClickListener {
	TextView tv_tel, tv_speed, tv_score, tv_soft, tv_check, tv_file, tv_clear;
	LinearLayout main_rl;
	private StringBuffer sb;
	private MyView mv_speed;
	private FrameLayout fl_search;
	private ClearView cv_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sb = new StringBuffer();
		setTitle();
		initView();
		initEvent();
		mv_speed.setOnAngleColorListener(onAngleColorListener);
		showScore();
	}

	// �Ƕ���ɫ�������
	private OnAngleColorListener onAngleColorListener = new OnAngleColorListener() {

		@Override
		public void onAngleColorListener(int red, int green) {
			Color color = new Color();
			int c = color.argb(150, red, green, 0);
			main_rl.setBackgroundColor(c);
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		
	}

	private void initEvent() {
		tv_tel.setOnClickListener(this);
		tv_speed.setOnClickListener(this);
		tv_soft.setOnClickListener(this);
		tv_check.setOnClickListener(this);
		tv_file.setOnClickListener(this);
		tv_clear.setOnClickListener(this);
		// �ֻ����ٰ�ť����
		// ����Բ�ǶȰٷֱȼ���
		// �̶�����״��
		mv_speed.setOnClickListener(this);
		cv_search.cancle();
		

	}

	private void initView() {
		tv_tel = (TextView) findViewById(R.id.tv_tel);
		tv_speed = (TextView) findViewById(R.id.tv_speed);
		tv_score = (TextView) findViewById(R.id.tv_score);
		tv_soft = (TextView) findViewById(R.id.tv_soft);
		tv_check = (TextView) findViewById(R.id.tv_check);
		tv_file = (TextView) findViewById(R.id.tv_file);
		tv_clear = (TextView) findViewById(R.id.tv_clear);
		// �̶�����״��
		mv_speed = (MyView) findViewById(R.id.mv_speed);

		main_rl = (LinearLayout) findViewById(R.id.main_rl);
		fl_search = (FrameLayout) findViewById(R.id.fl_search);
		cv_search=(ClearView) findViewById(R.id.cv_search);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tel:
			startActivity(TelManagerActivity.class);
			break;
		case R.id.tv_speed:
			startActivity(SpeedUpActivity.class);
			break;
		case R.id.tv_soft:
			startActivity(SoftManagerActivity.class);
			break;
		case R.id.tv_check:
			startActivity(PhoneCheckActivity.class);
			break;
		case R.id.tv_file:
			startActivity(FileManagerActivity.class);
			break;
		case R.id.tv_clear:
			startActivity(ClearActivity.class);
			break;

		case R.id.mv_speed:
			fl_search.setVisibility(View.VISIBLE);
			mv_speed.setVisibility(View.GONE);
			cv_search.start();
			// ��ɱ����
			new Thread(new Runnable() {
				@Override
				public void run() {
					RunAppManager.killAllUser(MainActivity.this);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							fl_search.setVisibility(View.GONE);
							mv_speed.setVisibility(View.VISIBLE);
							cv_search.cancle();
						}
					});
					// ��̬��ʾ
					showScore();
				}
			}).start();

			break;

		default:
			break;
		}

	}

	/**
	 * �鿴��ǰ�ڴ�ʹ�����
	 */
	private void showScore() {
		// ��ȡ�ڴ�ʹ�����
		long allRam = MemoryManager.getPhoneTotalRamMemory();
		long freeRam = MemoryManager.getPhoneFreeRamMemory(this);
		// ��ռ���ڴ���ռ����
		float score = (freeRam) / (float) allRam * 300;
		// ��̬��ʾ����
		mv_speed.change(score);
		// ��̬ˮ��
		mv_speed.moveWaterLine();

	}

}
