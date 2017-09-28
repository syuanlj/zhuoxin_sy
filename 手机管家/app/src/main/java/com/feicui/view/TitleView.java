package com.feicui.view;


import com.feicui.activity.R;
import com.feicui.activity.R.id;
import com.feicui.activity.R.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleView extends LinearLayout {
	
	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TitleView(Context context) {
		super(context);
		
	}

	private TextView tv_title;
	private ImageView iv_left,iv_right;
	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.title_layout, this);
		tv_title=(TextView) findViewById(R.id.tv_title);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		iv_right=(ImageView)findViewById(R.id.iv_right);
	}
	
	/**
	 * 
	 * @param title  要显示的标题内容
	 * @param left_id  左边ImageView要显示的图片资源id
	 * @param right_id  右边ImageView要显示的图片资源id
	 * @param left_click 左边ImageView的点击监听
	 * @param right_click  右边ImageView的点击监听
	 */
	public void setTitleView(String title,int left_id,int right_id,OnClickListener left_click,OnClickListener right_click){
		
		if(title==null){
			tv_title.setText("未知");
		}else {
			tv_title.setText(title);
		}
		if(left_id==0){//如果不设置图片
			//隐藏imageview
			iv_left.setVisibility(View.INVISIBLE);
		}else {//如果设置了图片
			//显示imagview
			iv_left.setImageResource(left_id);
			if(left_click!=null){//如果设置了左点击事件
				iv_left.setOnClickListener(left_click);
			}
			
		}
		if(right_id==0){//如果不设置图片
			//隐藏imageview
			iv_right.setVisibility(View.INVISIBLE);
		}else {//如果设置了图片
			//显示imagview
			iv_right.setImageResource(right_id);
			if(right_click!=null){//如果设置了左点击事件
				iv_right.setOnClickListener(right_click);
			}
		}
		
	}
	/**
	 * 给title设置字体颜色
	 * @param color
	 */
	public void setTextColor(int color){
		tv_title.setTextColor(color);
	}
	

}
