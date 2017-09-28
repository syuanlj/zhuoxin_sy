package com.feicui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feicui.base.BaseActivity;
import com.feicui.manager.MemoryManager;
import com.feicui.utils.CommonUtil;
import com.feicui.view.SoftView;

public class SoftManagerActivity extends BaseActivity implements
		OnClickListener {
	private ProgressBar pb_self, pb_out;
	private TextView tv_selfFree, tv_outFree, tv_allSoft, tv_sysSoft,
			tv_userSoft;
	private SoftView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soft_manager);
		setTitle("�������");
		initView();
		initEvent();
		initData();
	}

	private void initView() {
		pb_self = (ProgressBar) findViewById(R.id.pb_self);
		pb_out = (ProgressBar) findViewById(R.id.pb_out);
		tv_selfFree = (TextView) findViewById(R.id.tv_selfFree);
		tv_outFree = (TextView) findViewById(R.id.tv_outFree);
		tv_allSoft = (TextView) findViewById(R.id.tv_allSoft);
		tv_sysSoft = (TextView) findViewById(R.id.tv_sysSoft);
		tv_userSoft = (TextView) findViewById(R.id.tv_userSoft);
		sv = (SoftView) findViewById(R.id.sv);

	}

	private void initEvent() {
		tv_allSoft.setOnClickListener(this);
		tv_sysSoft.setOnClickListener(this);
		tv_userSoft.setOnClickListener(this);
	}

	private void initData() {
		// �ֻ�����ռ�
		long phoneSelfTolal = MemoryManager.getPhoneSelfSize();
		long phoneSelfUnused = MemoryManager.getPhoneSelfFreeSize();
		long phoneSelfUsed = phoneSelfTolal - phoneSelfUnused;
		// �ֻ��Դ�sdcard�ռ�
		long phoneSelfSDCardTolal = MemoryManager.getPhoneSelfSDCardSize();
		long phoneSelfSDCardUnused = MemoryManager.getPhoneSelfSDCardFreeSize();
		long phoneSelfSDCardUsed = phoneSelfSDCardTolal - phoneSelfSDCardUnused;
		// �ֻ����ô洢���ռ�
		long phoneOutSDCardTolat = MemoryManager
				.getPhoneOutSDCardSize(getApplicationContext());
		long phoneOutSDCardUnused = MemoryManager
				.getPhoneOutSDCardFreeSize(getApplicationContext());
		long phoneOutSDCradUsed = phoneOutSDCardTolat - phoneOutSDCardUnused;
		// �ֻ��ܿռ�
		float phoneAllSpace = phoneSelfTolal + phoneSelfSDCardTolal
				+ phoneOutSDCardTolat;
		// �������
		int phoneSpaceF = (int) ((phoneSelfTolal + phoneSelfSDCardTolal)
				/ (float) phoneAllSpace * 360);
		int sdcardSpaceF = (int) (phoneOutSDCardTolat / (float) phoneAllSpace * 360);

		// ����ʹ�ñ���
		int selfUsed = (int) ((phoneSelfUsed + phoneSelfSDCardUsed)
				/ (float) (phoneSelfTolal + phoneSelfSDCardTolal) * 100);
		int outUsed = (int) (phoneOutSDCradUsed / (float) phoneOutSDCardTolat * 100);
		pb_self.setProgress(selfUsed);
		pb_out.setProgress(outUsed);

		tv_outFree.setText("���ã�" + CommonUtil.getFileSize(phoneOutSDCardUnused)
				+ "/" + CommonUtil.getFileSize(phoneOutSDCardTolat));
		tv_selfFree
				.setText("���ã�"
						+ CommonUtil.getFileSize(phoneSelfSDCardUnused
								+ phoneSelfUnused)
						+ "/"
						+ CommonUtil.getFileSize(phoneSelfSDCardTolal
								+ phoneSelfTolal));

		sv.changeAngle(phoneSpaceF);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_allSoft:
			Bundle bundle=new Bundle();
			bundle.putInt("type", 1);
			startActivity(SoftListActivity.class,bundle);
			break;
		case R.id.tv_sysSoft:
			Bundle bundle1=new Bundle();
			bundle1.putInt("type", 2);
			startActivity(SoftListActivity.class,bundle1);
			break;
		case R.id.tv_userSoft:
			Bundle bundle2=new Bundle();
			bundle2.putInt("type", 3);
			startActivity(SoftListActivity.class,bundle2);
			break;
		default:
			break;
		}

	}
}
