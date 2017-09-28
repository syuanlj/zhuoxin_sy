package com.feicui.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyBaseAdapter<E> extends BaseAdapter{
	
	protected LayoutInflater inflater;
	protected List<E> mList;
	protected Context mContext;
	
	public MyBaseAdapter(Context context) {
		inflater=LayoutInflater.from(context);
		mList=new ArrayList<E>();
		this.mContext=context;
		
	}
	
	public void add(E e){
		mList.add(e);
		
	}
	
	public void add(List<E> list){
		mList.addAll(list);
	}
	
	public void clear(){
		mList.clear();
	}
	
	public List<E> getList(){
		return mList;
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public E getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
