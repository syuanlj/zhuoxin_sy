package com.feicui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.activity.R;
import com.feicui.base.MyBaseAdapter;
import com.feicui.bean.ClearInfo;
import com.feicui.utils.CommonUtil;

public class MyClearAdapter extends MyBaseAdapter<ClearInfo> {

	public MyClearAdapter(Context context) {
		super(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_lv_clearlist, null);
			vh.cb_check=(CheckBox) convertView.findViewById(R.id.cb_check);
			vh.tv_size=(TextView) convertView.findViewById(R.id.tv_size);
			vh.tv_labelName=(TextView) convertView.findViewById(R.id.tv_labelName);
			vh.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		
		final ClearInfo info = getItem(position);
		vh.cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				info.setChecked(isChecked);
				
			}
		});
		vh.cb_check.setChecked(info.isChecked());
		if(info.getIcon()==null){//获取到的icon图标为空
			vh.iv_icon.setImageResource(R.drawable.ic_launcher);
		}else{
			vh.iv_icon.setImageDrawable(info.getIcon());
		}
		
		vh.tv_labelName.setText(info.getSoftChinesename());
		vh.tv_size.setText(CommonUtil.getFileSize(info.getFileSize()));
		return convertView;
	}

	public class ViewHolder {
		private TextView tv_labelName, tv_size;
		private CheckBox cb_check;
		private ImageView iv_icon;
	}
}
