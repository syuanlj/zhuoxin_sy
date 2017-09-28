package com.feicui.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.feicui.DBManager.SelectForTel;
import com.feicui.adapter.TelListAdapter;
import com.feicui.base.BaseActivity;
import com.feicui.bean.TelList;
import com.feicui.bean.TelType;

public class TelListActivity extends BaseActivity {

	ListView lv_tellist;
	TelListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tel_list);
		lv_tellist=(ListView) findViewById(R.id.lv_tellist);
		// 得到Intent对象
		Intent intent = getIntent();
		// 通过intent得到bundle对象
		Bundle bundle = intent.getExtras();
		TelType info = (TelType) bundle.getSerializable("info");
		setTitle(info.getName());
		//拿到了数据，就要添加适配器
		List<TelList> list = SelectForTel.getTelList(this, info);
		
		adapter=new TelListAdapter(this);
		adapter.add(list);
		lv_tellist.setAdapter(adapter);
		
		
		//listview设置item点击监听
		lv_tellist.setOnItemClickListener(listener);
	}
	OnItemClickListener listener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TelList info = adapter.getItem(position);
			showDilog(info);
			
		}

		private void showDilog(final TelList info) {
			Builder dilog = new Builder(TelListActivity.this);
			dilog.setIcon(R.drawable.ic_launcher);
			dilog.setTitle("警告");
			dilog.setMessage("是否要拨打"+info.getName()+"电话\n"+"Tel:"+info.getNumber());
			dilog.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				shortToast("开始拨打");
					Intent intent=new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"+info.getNumber()));
					startActivity(intent);
				}
			});
			dilog.setNegativeButton("取消", null);
			dilog.show();
		}
	};
}
