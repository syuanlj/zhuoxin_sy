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
		setTitle("�ֻ�����");
		initView();
		initEvent();
		initData();
		adapter = new SpeedUpAdapter(this);
		lv_loadProcess.setAdapter(adapter);
		// ��ȡ��������
		addProcessData(1);
	}

	private void addProcessData(final int type) {
		lv_loadProcess.setVisibility(View.INVISIBLE);
		pb_loding.setVisibility(View.VISIBLE);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// ���ù������м������ݵķ���
				RunAppManager.getRnAppManager(SpeedUpActivity.this).getRunApp();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adapter.clear();
						switch (type) {
						case 1:
							//����Ϊ1����û�����
							adapter.add(RunAppManager.getUserList());
							break;
						case 2:
							//����Ϊ2���ϵͳ����
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
	 * ��ʼ������
	 */
	private void initData() {
		// ��������ڴ��С
		long allRam = com.feicui.manager.MemoryManager.getPhoneTotalRamMemory();
		// ��������ڴ����ô�С
		long uesdRam = com.feicui.manager.MemoryManager
				.getPhoneTotalRamMemory()
				- com.feicui.manager.MemoryManager.getPhoneFreeRamMemory(this);
		// ���ڴ��СתΪ�ַ���
		String s = CommonUtil.getFileSize(uesdRam);

		String s1 = CommonUtil.getFileSize(allRam);
		tv_ramMessage.setText("�����ڴ�" + s + "/" + s1);

		// ���������ڴ��С���������ý�����
		pb_ramUsed.setProgress((int) (uesdRam / (float) allRam * 100));
		// �豸����
		tv_phoneName.setText(SystemManager.getPhoneName());
		// �豸�ͺ�
		tv_phoneModel.setText(SystemManager.getPhoneModelName());
	}

	/**
	 * ��ʼ���¼�
	 */
	private void initEvent() {
		// һ������ĵ���¼�
		btn_clear.setOnClickListener(this);
		// ѡ����ʾϵͳ�����û����̵�togglebutton��״̬�ı����
		tb_showapp.setOnCheckedChangeListener(this);
		// ȫѡ��״̬����
		cb_checkAll.setOnCheckedChangeListener(this);

	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// �豸����
		tv_phoneName = (TextView) findViewById(R.id.tv_phoneName);
		// �豸����
		tv_phoneModel = (TextView) findViewById(R.id.tv_phoneModel);
		// ramʹ�����
		tv_ramMessage = (TextView) findViewById(R.id.tv_ramMessage);
		// ramʹ�ý�����
		pb_ramUsed = (ProgressBar) findViewById(R.id.pb_ramUsed);
		// ���ؽ��������
		pb_loding = (ProgressBar) findViewById(R.id.pb_loding);
		// һ������ť
		btn_clear = (Button) findViewById(R.id.btn_clear);
		// �л��û���ϵͳ
		tb_showapp = (ToggleButton) findViewById(R.id.tb_showapp);
		// ���ؽ���Ľ�����
		lv_loadProcess = (ListView) findViewById(R.id.lv_loadProcess);
		// ȫ��ѡ��
		cb_checkAll = (CheckBox) findViewById(R.id.cb_checkAll);

	}

	//�л�ϵͳ�û�����
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		switch (buttonView.getId()) {
		case R.id.tb_showapp:
			if(isChecked){//ѡ���˾ͼ���ϵͳ����
				addProcessData(2);
			}else{//��֮�����û�����
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
		//���һ������
		//�õ��������д�����ݵļ���
		List<RunningAppInfo> infoLists = adapter.getList();
		//�鿴��������Щ���ݿ��Ա�����
		for (RunningAppInfo runningAppInfo : infoLists) {
			if(runningAppInfo.isClear()){
				//ɱ������
				RunAppManager.killProcess(SpeedUpActivity.this, runningAppInfo.getPackageName());
			}
		}
		//�������������
		adapter.clear();
		//���»�ȡ������Ϣ
		//����toggleButton״̬ȥ�жϣ���ʾ��һ��
		if(tb_showapp.isChecked()){
			addProcessData(2);
		}else{
			addProcessData(1);
		}
		
		initData();
		
	}
}
