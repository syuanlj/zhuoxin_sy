package com.feicui.adapter;

import com.feicui.activity.R;
import com.feicui.base.MyBaseAdapter;
import com.feicui.bean.TelList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TelListAdapter extends MyBaseAdapter<TelList> {

	public TelListAdapter(Context context) {
		super(context);
		
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_lv_tellist, null);
			vh=new ViewHolder(convertView);
			
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		TelList info=mList.get(position);
		vh.tv_name.setText(info.getName());
		vh.tv_number.setText(info.getNumber());
		
		return convertView;
	}

	class ViewHolder{
		TextView tv_name;
		TextView tv_number;
		public ViewHolder(View itemView) {
			tv_name=(TextView) itemView.findViewById(R.id.tv_name);
			tv_number=(TextView) itemView.findViewById(R.id.tv_number);
		}
	}
}
