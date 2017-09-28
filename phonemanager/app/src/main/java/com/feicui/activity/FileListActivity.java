package com.feicui.activity;

import java.util.ArrayList;
import java.util.List;

import com.feicui.adapter.MyFileListAdapter;
import com.feicui.base.BaseActivity;
import com.feicui.bean.FileInfo;
import com.feicui.manager.MyTask;
import com.feicui.utils.CommonUtil;
import com.feicui.utils.FileTypeUtil;
import com.feicui.view.TitleView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FileListActivity extends BaseActivity implements OnClickListener {
	private TextView tv_allNumber, tv_allSize;
	private ListView lv_fileList;
	private Button bt_fileDelet;

	private Bundle bundle;
	private String title;
	private int position;

	private long deletAllSize;
	private long fileSize;
	private List currentList;
	private MyFileListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_list);
		initView();
		initEvent();
		bundle = getIntent().getExtras();
		setTitle(bundle.getString("fileType"));
		// ��Ӧ���ϵ�λ��
		position = bundle.getInt("position");
		// �õ����������ļ����ܴ�С
		fileSize = bundle.getLong("oneTypeSize");
		// �õ���ǰ�����ļ��ļ���
		currentList = MyTask.list.get(position);

		tv_allNumber.setText(currentList.size() + "");
		tv_allSize.setText(CommonUtil.getFileSize(fileSize));

		adapter = new MyFileListAdapter(this);
		adapter.add(currentList);
		lv_fileList.setAdapter(adapter);
		lv_fileList.setOnItemClickListener(itemListener);
	}

	private void initEvent() {
	
		bt_fileDelet.setOnClickListener(this);
		
	}

	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			try {
				// ���ļ�
				FileInfo info = adapter.getItem(position);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(info.getFile()),
						FileTypeUtil.getMIMEType(info.getFile()));
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				shortToast("�ļ��޷���");
			}
			

		}
	};
	
	//��¼�ܹ�ɾ���ļ��Ĵ�С
	private long deleteFileSize;

	/**
	 * �ؼ�ʵ����
	 */
	private void initView() {
		tv_allNumber = (TextView) findViewById(R.id.tv_allNumber);
		tv_allSize = (TextView) findViewById(R.id.tv_allSize);
		lv_fileList = (ListView) findViewById(R.id.lv_fileList);
		bt_fileDelet = (Button) findViewById(R.id.bt_fileDelet);
	}

	@Override
	public void onClick(View v) {
		//�õ��������еļ���
		List<FileInfo> infoList = adapter.getList();
		//����һ��������������Ҫɾ��������
		List<FileInfo> deleteList=new ArrayList<FileInfo>();
		//�鿴��һ����ѡ��
		for (FileInfo fileInfo : infoList) {//��ͬһ�����ϲ���һ�߱���һ��ɾ��
			if(fileInfo.isIschecked()){//����ļ����ɾ������ӵ�ɾ���ļ�����
				deleteList.add(fileInfo);
			}
		}
		//ɾ���ļ�
		for (FileInfo fileInfo : deleteList) {
			if(fileInfo.getFile()!=null){
				deleteFileSize+=fileInfo.getFile().length();
				fileInfo.getFile().delete();
			}
			//ɾ��ԭ�����е�ͬһ������
			infoList.remove(fileInfo);
			//�Ƴ��������еļ�¼
			adapter.getList().remove(fileInfo);
			//�ļ����������еļ����Ƴ��ü�¼
			currentList.remove(fileInfo);
		}
		tv_allSize.setText(CommonUtil.getFileSize(fileSize-deleteFileSize));
		tv_allNumber.setText(currentList.size()+"");
		adapter.notifyDataSetChanged();
		
	}
	
	/**
	 * ���ؼ��ļ���
	 */
	@Override
	public void onBackPressed() {
		resultToFileManager();
		
		super.onBackPressed();
	}

	/**
	 * ����һ�����淵������
	 */
	private void resultToFileManager() {
		if(deleteFileSize>-1){
			Intent data=new Intent();
			Bundle bundle=new Bundle();
			bundle.putLong("oneTypeSize", fileSize-deleteFileSize);
			bundle.putInt("position", position);
			data.putExtras(bundle);
			setResult(2, data);
		}
	}
	
	/**
	 * ����ֻ�з��ؼ�����Title�ķ���
	 * @param title  Ҫ��ʾ�ı���
	 */
	protected TitleView setTitle(String title) {
	  OnClickListener left_click = new OnClickListener() {
			@Override
			public void onClick(View v) {
				resultToFileManager();
				FileListActivity.this.finish();
			}
		};
		
		TitleView titleView = (TitleView) findViewById(R.id.titleView1);
		titleView.setTitleView(title, R.drawable.btn_homeasup_default, 0,
				left_click, null);
		return titleView;
	}
}
