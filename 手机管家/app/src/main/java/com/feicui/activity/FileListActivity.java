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
		// 对应集合的位置
		position = bundle.getInt("position");
		// 得到这种类型文件的总大小
		fileSize = bundle.getLong("oneTypeSize");
		// 得到当前类型文件的集合
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
				// 打开文件
				FileInfo info = adapter.getItem(position);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(info.getFile()),
						FileTypeUtil.getMIMEType(info.getFile()));
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				shortToast("文件无法打开");
			}
			

		}
	};
	
	//记录总共删除文件的大小
	private long deleteFileSize;

	/**
	 * 控件实例化
	 */
	private void initView() {
		tv_allNumber = (TextView) findViewById(R.id.tv_allNumber);
		tv_allSize = (TextView) findViewById(R.id.tv_allSize);
		lv_fileList = (ListView) findViewById(R.id.lv_fileList);
		bt_fileDelet = (Button) findViewById(R.id.bt_fileDelet);
	}

	@Override
	public void onClick(View v) {
		//拿到适配器中的集合
		List<FileInfo> infoList = adapter.getList();
		//创建一个集合用来保存要删除的数据
		List<FileInfo> deleteList=new ArrayList<FileInfo>();
		//查看哪一个被选中
		for (FileInfo fileInfo : infoList) {//对同一个集合不能一边遍历一边删除
			if(fileInfo.isIschecked()){//如果文件标记删除，添加到删除的集合中
				deleteList.add(fileInfo);
			}
		}
		//删除文件
		for (FileInfo fileInfo : deleteList) {
			if(fileInfo.getFile()!=null){
				deleteFileSize+=fileInfo.getFile().length();
				fileInfo.getFile().delete();
			}
			//删除原集合中的同一个对象
			infoList.remove(fileInfo);
			//移除适配器中的记录
			adapter.getList().remove(fileInfo);
			//文件搜索任务中的集合移除该记录
			currentList.remove(fileInfo);
		}
		tv_allSize.setText(CommonUtil.getFileSize(fileSize-deleteFileSize));
		tv_allNumber.setText(currentList.size()+"");
		adapter.notifyDataSetChanged();
		
	}
	
	/**
	 * 返回键的监听
	 */
	@Override
	public void onBackPressed() {
		resultToFileManager();
		
		super.onBackPressed();
	}

	/**
	 * 向上一个界面返回数据
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
	 * 设置只有返回键界面Title的方法
	 * @param title  要显示的标题
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
