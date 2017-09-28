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
		setTitle("通讯大全");
		gv = (GridView) findViewById(R.id.gv);
		//实例化适配器
		adapter = new GridViewAdapter(this);
		
		// 添加数据
		List<TelType> list = SelectForTel.getTelType(this);
		adapter.add(list);
		
		// 设置适配器
		gv.setAdapter(adapter);
		
		//设置gridview的item点击事件
		
	
		gv.setOnItemClickListener(listener);

	}
	private OnItemClickListener listener=new OnItemClickListener() {

		/**
		 * 参数1：gridview
		 * <p>参数2：点击的itemview </p>
		 * <p>参数2：点击的itemview所在position </p>
		 * <p>参数2：点击的itemview的id </p>
		 */
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			//通过适配器获得了对应item的实体类对象（电话类型）
			TelType info = (TelType) adapter.getItem(position);
			Log.e("info=====", info.toString());
			shortToast(position+"----"+info.toString());
			
			Intent intent =new Intent(TelManagerActivity.this, TelListActivity.class);
			//实例化Bundle对象
			Bundle bundle=new Bundle();
			//将点击位置的item实体类对象传递过去（实体类必须要去实现serializable接口）
			bundle.putSerializable("info", info);
			//将bundle对象放入intent对象
			intent.putExtras(bundle);
			//启动intent，跳转
			startActivity(intent);
		}
	};
}
