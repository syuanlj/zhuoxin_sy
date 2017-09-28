package com.feicui.activity;

import java.io.File;
import java.util.List;

import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feicui.DBManager.SelectForClear;
import com.feicui.adapter.MyClearAdapter;
import com.feicui.base.BaseActivity;
import com.feicui.bean.ClearInfo;
import com.feicui.utils.CommonUtil;
import com.feicui.view.ClearView;

public class ClearActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private TextView tv_allSize;
	private ListView lv_clear;
	private CheckBox cb_checkAll;
	private Button bt_clear;
	private ProgressBar pb_loding;
	private MyClearAdapter adapter;
	private FrameLayout fl_result,fl_search;
	private ClearView cv_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clear);
		setTitle("垃圾清理").setTextColor(Color.WHITE);
		initView();
		initEvent();
		adapter = new MyClearAdapter(this);
		lv_clear.setAdapter(adapter);

		getData();

	}

	// 子线程显示数据
	private void getData() {
		adapter.clear();
		lv_clear.setVisibility(View.INVISIBLE);
		pb_loding.setVisibility(View.VISIBLE);
		fl_result.setVisibility(View.GONE);
		fl_search.setVisibility(View.VISIBLE);
		cv_search.start();
		SelectForClear.allSize = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 调用获取软件垃圾的方法
					final List<ClearInfo> infoList = SelectForClear
							.getClearList(ClearActivity.this);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							adapter.add(infoList);
							adapter.notifyDataSetChanged();
							lv_clear.setVisibility(View.VISIBLE);
							pb_loding.setVisibility(View.INVISIBLE);
							fl_result.setVisibility(View.VISIBLE);
							fl_search.setVisibility(View.GONE);
							cv_search.cancle();
							tv_allSize.setText(CommonUtil
									.getFileSize(SelectForClear.allSize));

						}
					});
				

			}
		}).start();

	}

	/**
	 * 实例化控件
	 */
	private void initView() {
		tv_allSize = (TextView) findViewById(R.id.tv_allSize);
		lv_clear = (ListView) findViewById(R.id.lv_clear);
		cb_checkAll = (CheckBox) findViewById(R.id.cb_checkAll);
		bt_clear = (Button) findViewById(R.id.bt_clear);
		pb_loding = (ProgressBar) findViewById(R.id.pb_loding);
		fl_result=(FrameLayout) findViewById(R.id.fl_result);
		fl_search=(FrameLayout) findViewById(R.id.fl_search);
		cv_search=(ClearView) findViewById(R.id.cv_search);
	}

	private void initEvent() {
		bt_clear.setOnClickListener(this);
		cb_checkAll.setOnCheckedChangeListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (adapter.getCount() <= 0) {
			shortToast("没有垃圾");
			return;

		}

		lv_clear.setVisibility(View.INVISIBLE);
		pb_loding.setVisibility(View.VISIBLE);
		List<ClearInfo> infoList = adapter.getList();
		boolean hasChecked = false;
		for (ClearInfo clearInfo : infoList) {
			if (clearInfo.isChecked()) {
				hasChecked = true;
				File file = clearInfo.getFile();
				SelectForClear.deleteFile(file);
			}
		}

		if (!hasChecked) {
			lv_clear.setVisibility(View.VISIBLE);
			pb_loding.setVisibility(View.INVISIBLE);
			shortToast("请选择要清理的垃圾");
			return;
		}
		getData();

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		List<ClearInfo> infoList = adapter.getList();
		for (ClearInfo clearInfo : infoList) {
			clearInfo.setChecked(isChecked);
		}
		adapter.notifyDataSetChanged();

	}
}
