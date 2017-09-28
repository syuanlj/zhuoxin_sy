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
		//��̬ע��㲥������
		initReceiver();
	}

	private void initReceiver() {
		//�õ��㲥�������Ķ���
		receiver=new SoftDeleteReceiver();
		//�㲥���ˣ�ѡ��Ҫ�����Ĺ㲥(�����ж��)
		IntentFilter filter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		//��Ϣ��ƥ������
		filter.addDataScheme("package");
		//ע��㲥�������ķ���
		registerReceiver(receiver, filter);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//ȡ���㲥����������
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

	// listviewitem�ĵ������
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
		// ��ȡ����
		// ��ö���ͽ����ݷŵ�����
		SoftManager.getSoftManager(this).getSoft();
		adapter = new SoftListAdapter(this);
		 bundle = getIntent().getExtras();
		 type = bundle.getInt("type");
		switch (type) {
		case 1:
			setTitle("��������б�");
			adapter.add(SoftManager.getAllList());
			break;
		case 2:
			setTitle("ϵͳ����б�");
			adapter.add(SoftManager.getSysList());
			break;
		case 3:
			setTitle("�û�����б�");
			adapter.add(SoftManager.getUserList());
			break;
		default:
			break;
		}
	}

	/**
	 * ��Ӧ�ó���
	 */
	private void openApp(final SoftList info) {
		Builder builder = new Builder(SoftListActivity.this);
		builder.setIcon(R.drawable.ic_launcher).setTitle("�������")
				.setMessage("��ȷ��Ҫ����" + info.getName())
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// ͨ�������õ�intent����
						Intent intent = getPackageManager()
								.getLaunchIntentForPackage(
										info.getPackageName());

						if (intent == null) {
							shortToast("�޷�����");
						} else {
							startActivity(intent);
						}
					}
				}).setNegativeButton("ȡ��", null).show();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_DELETE);

		List<SoftList> infoList = adapter.getList();
		for (SoftList softList : infoList) {

			if (softList.isChear()) {
				// ��ж���Լ���
				if (softList.getPackageName().equals(this.getPackageName())) {
					shortToast("����ж���Լ�");

				} else {
					intent.setData(Uri.parse("package:"
							+ softList.getPackageName()));
					startActivity(intent);
			
				}
			}
		}

	}
	
	/**
	 * ʵ��һ���������ж�صĹ㲥������
	 * 
	 */
	public class SoftDeleteReceiver extends BroadcastReceiver{

		//���յ���ָ���Ĺ㲥ʱ�Զ�����
		@Override
		public void onReceive(Context context, Intent intent) {
			//ж����ɺ���������������»�ȡ��������
			adapter.clear();
			//ж����ɺ����»�ȡϵͳ�е������Ϣ��
			SoftManager.getSoftManager(context).getSoft();
			switch (type) {//���ݵ�ǰ���ص����࣬ȥ���¼��ء�
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
