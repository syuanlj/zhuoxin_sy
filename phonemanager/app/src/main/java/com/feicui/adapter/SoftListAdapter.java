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
import com.feicui.bean.SoftList;

public class SoftListAdapter extends MyBaseAdapter<SoftList> {

	
	public SoftListAdapter(Context context) {
		super(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_lv_softlist, null);
			vh.cb_check=(CheckBox) convertView.findViewById(R.id.cb_check);
			vh.tv_labelName=(TextView) convertView.findViewById(R.id.tv_labelName);
			vh.tv_packageName=(TextView) convertView.findViewById(R.id.tv_packageName);
			vh.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
			vh.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		
		final SoftList info = getItem(position);
		
		vh.tv_labelName.setText(info.getName());
		
		vh.tv_packageName.setText(info.getPackageName());
		
		vh.tv_time.setText(info.getTime());
		
		vh.iv_icon.setImageDrawable(info.getIcon());
		

		vh.cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				info.setChear(isChecked);
				
			}
		});
		vh.cb_check.setChecked(info.isChear());
		
		return convertView;
	}
	
	public class ViewHolder{
		CheckBox cb_check;
		TextView tv_labelName,tv_packageName,tv_time;
		ImageView iv_icon;
	}

	
	
}
