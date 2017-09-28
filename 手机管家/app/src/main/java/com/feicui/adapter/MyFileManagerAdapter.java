package com.feicui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feicui.activity.R;
import com.feicui.base.MyBaseAdapter;
import com.feicui.bean.FileTypeInfo;
import com.feicui.utils.CommonUtil;

public class MyFileManagerAdapter extends MyBaseAdapter<FileTypeInfo> {

	public MyFileManagerAdapter(Context context) {
		super(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler vh = null;
		if (convertView == null) {
			// å®žä¾‹åŒ–ç¼“å­˜çš„æŽ§ä»¶ç±?
			vh = new ViewHodler();
			convertView = inflater.inflate(R.layout.item_lv_filemanager, null);
			vh.iv_go = (ImageView) convertView.findViewById(R.id.iv_go);
			vh.tv_fileType = (TextView) convertView
					.findViewById(R.id.tv_fileType);
			vh.tv_fileSize = (TextView) convertView
					.findViewById(R.id.tv_fileSize);
			vh.pb_loding = (ProgressBar) convertView
					.findViewById(R.id.pb_loding);
			
			convertView.setTag(vh);
		}else{
			vh=(ViewHodler) convertView.getTag();
		}
		
		FileTypeInfo info=getItem(position);
		vh.tv_fileType.setText(info.getType());
		if(info.getFileSize()==-1){
			vh.tv_fileSize.setText("¼ÆËãÖÐ");
		}else{
			vh.tv_fileSize.setText(CommonUtil.getFileSize(info.getFileSize()));
		}
		
		if(info.isLoding()){
			vh.pb_loding.setVisibility(View.VISIBLE);
			vh.iv_go.setVisibility(View.INVISIBLE);
		}else{
			vh.pb_loding.setVisibility(View.INVISIBLE);
			vh.iv_go.setVisibility(View.VISIBLE);
		}
		
		
	
		return convertView;
	}

	public class ViewHodler {
		TextView tv_fileType, tv_fileSize;
		ImageView iv_go;
		ProgressBar pb_loding;
	}

}
