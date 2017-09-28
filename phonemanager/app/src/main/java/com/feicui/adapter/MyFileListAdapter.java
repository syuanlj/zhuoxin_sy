package com.feicui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.activity.R;
import com.feicui.base.MyBaseAdapter;
import com.feicui.bean.FileInfo;
import com.feicui.utils.FileTypeUtil;

public class MyFileListAdapter extends MyBaseAdapter<FileInfo> {

	public MyFileListAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_lv_filelist, null);
			vh.cb_check = (CheckBox) convertView.findViewById(R.id.cb_check);
			vh.tv_fileName = (TextView) convertView
					.findViewById(R.id.tv_fileName);
			vh.tv_fileSize = (TextView) convertView
					.findViewById(R.id.tv_fileSize);
			vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final FileInfo info = getItem(position);
		vh.tv_fileName.setText(info.getFileName());
		vh.tv_fileSize.setText(info.getFileSize());
		vh.tv_time.setText(info.getLastTime());

		vh.cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				info.setIschecked(isChecked);
			}
		});
		vh.cb_check.setChecked(info.isIschecked());
		String iconID = info.getIconID();
		Log.e("icon_id", iconID);
		// 获得资源的id，参数1：资源名称；参数2：资源类型；参数3：应用包名
		int iconId = mContext.getResources().getIdentifier(iconID, "drawable",
				mContext.getPackageName());
		if (iconId != 0) {
			vh.iv_icon.setImageResource(iconId);
		} else {
			vh.iv_icon.setImageResource(R.drawable.ic_launcher);
		}

		return convertView;
	}

	public class ViewHolder {
		CheckBox cb_check;
		TextView tv_fileName, tv_fileSize, tv_time;
		ImageView iv_icon;
	}

}
