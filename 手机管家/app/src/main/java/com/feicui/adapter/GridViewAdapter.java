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
	 * ��ӵ�������
	 * @param info  ʵ��������
	 */
	public void add(TelType info){
		mList.add(info);
	}
	
	
	/**
	 * �����������
	 * @param list Ҫ��ӵļ���
	 */
	public void add(List<TelType> list){
		mList.addAll(list);
	}
	
	/**
	 * ����item����
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	/**
	 * ����item����
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	/**
	 * ����itemID��һ�㷵��position
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * ����itemView
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			
			convertView=inflater.inflate(R.layout.item_gridview, null);
			vh=new ViewHolder(convertView);
			//�󶨣����ʵ��vh�Ļ���
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
