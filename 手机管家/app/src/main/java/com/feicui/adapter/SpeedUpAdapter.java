package com.feicui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.activity.R;
import com.feicui.base.MyBaseAdapter;
import com.feicui.bean.RunningAppInfo;
import com.feicui.utils.CommonUtil;

public class SpeedUpAdapter extends MyBaseAdapter<RunningAppInfo> {

	public SpeedUpAdapter(Context context) {
		super(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_lv_speedup, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final RunningAppInfo info = getItem(position);
		vh.iv_icon.setImageDrawable(info.getIcon());

		
		vh.cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				info.setClear(isChecked);
			}
		});
		vh.cb_check.setChecked(info.isClear());
		
		vh.tv_size.setText(CommonUtil.getFileSize(info.getSize()));
		if (info.isSystem()) {
			vh.tv_type.setText("系统进程");
			vh.tv_type.setTextColor(Color.RED);
		} else {
			vh.tv_type.setText("用户进程");
			vh.tv_type.setTextColor(Color.BLACK);
		}
		vh.tv_lableName.setText(info.getLableName());

		return convertView;
	}

	public class ViewHolder {
		TextView tv_lableName, tv_size, tv_type;
		CheckBox cb_check;
		ImageView iv_icon;

		public ViewHolder(View itemView) {
			cb_check = (CheckBox) itemView.findViewById(R.id.cb_check);
			tv_lableName = (TextView) itemView.findViewById(R.id.tv_lableName);
			tv_size = (TextView) itemView.findViewById(R.id.tv_size);
			tv_type = (TextView) itemView.findViewById(R.id.tv_type);
			iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
		}
	}

}
