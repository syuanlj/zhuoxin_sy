package com.feicui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feicui.activity.R;
import com.feicui.bean.TelType;

public class GridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<TelType> mList;
	
	public GridViewAdapter(Context context) {
		
		inflater=LayoutInflater.from(context);
		mList= new ArrayList<TelType>();
		
	}
	/**
	 * 添加单个对象
	 * @param info  实例化对象
	 */
	public void add(TelType info){
		mList.add(info);
	}
	
	
	/**
	 * 添加整个集合
	 * @param list 要添加的集合
	 */
	public void add(List<TelType> list){
		mList.addAll(list);
	}
	
	/**
	 * 返回item数量
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	/**
	 * 返回item数据
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	/**
	 * 返回itemID，一般返回position
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 返回itemView
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			
			convertView=inflater.inflate(R.layout.item_gridview, null);
			vh=new ViewHolder(convertView);
			//绑定，间接实现vh的缓存
			convertView.setTag(vh);
			
			
			
		}else {
			vh=(ViewHolder) convertView.getTag();
		}
		
		TelType info = mList.get(position);
		vh.tv.setText(info.getName());
		if(position%3==0){
			vh.tv.setBackgroundResource(R.drawable.select_classlist_bg1);
		}
		if(position%3==1){
			vh.tv.setBackgroundResource(R.drawable.select_classlist_bg2);
		}
		if(position%3==2){
			vh.tv.setBackgroundResource(R.drawable.select_classlist_bg3);
		}
		return convertView;
	}

	class ViewHolder{
		TextView tv;
		public ViewHolder(View itemView) {
			tv=(TextView) itemView.findViewById(R.id.tv);
		}
	}
	
}
