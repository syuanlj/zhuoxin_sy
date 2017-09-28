package com.feicui.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.feicui.DBManager.SelectForTel;
import com.feicui.adapter.GridViewAdapter;
import com.feicui.base.BaseActivity;
import com.feicui.bean.TelType;

public class TelManagerActivity extends BaseActivity {

	private GridView gv;
	private GridViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tel_manager);
		setTitle("ͨѶ��ȫ");
		gv = (GridView) findViewById(R.id.gv);
		//ʵ����������
		adapter = new GridViewAdapter(this);
		
		// �������
		List<TelType> list = SelectForTel.getTelType(this);
		adapter.add(list);
		
		// ����������
		gv.setAdapter(adapter);
		
		//����gridview��item����¼�
		
	
		gv.setOnItemClickListener(listener);

	}
	private OnItemClickListener listener=new OnItemClickListener() {

		/**
		 * ����1��gridview
		 * <p>����2�������itemview </p>
		 * <p>����2�������itemview����position </p>
		 * <p>����2�������itemview��id </p>
		 */
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			//ͨ������������˶�Ӧitem��ʵ������󣨵绰���ͣ�
			TelType info = (TelType) adapter.getItem(position);
			Log.e("info=====", info.toString());
			shortToast(position+"----"+info.toString());
			
			Intent intent =new Intent(TelManagerActivity.this, TelListActivity.class);
			//ʵ����Bundle����
			Bundle bundle=new Bundle();
			//�����λ�õ�itemʵ������󴫵ݹ�ȥ��ʵ�������Ҫȥʵ��serializable�ӿڣ�
			bundle.putSerializable("info", info);
			//��bundle�������intent����
			intent.putExtras(bundle);
			//����intent����ת
			startActivity(intent);
		}
	};
}
