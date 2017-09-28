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
	 * @param title  Ҫ��ʾ�ı�������
	 * @param left_id  ���ImageViewҪ��ʾ��ͼƬ��Դid
	 * @param right_id  �ұ�ImageViewҪ��ʾ��ͼƬ��Դid
	 * @param left_click ���ImageView�ĵ������
	 * @param right_click  �ұ�ImageView�ĵ������
	 */
	public void setTitleView(String title,int left_id,int right_id,OnClickListener left_click,OnClickListener right_click){
		
		if(title==null){
			tv_title.setText("δ֪");
		}else {
			tv_title.setText(title);
		}
		if(left_id==0){//���������ͼƬ
			//����imageview
			iv_left.setVisibility(View.INVISIBLE);
		}else {//���������ͼƬ
			//��ʾimagview
			iv_left.setImageResource(left_id);
			if(left_click!=null){//��������������¼�
				iv_left.setOnClickListener(left_click);
			}
			
		}
		if(right_id==0){//���������ͼƬ
			//����imageview
			iv_right.setVisibility(View.INVISIBLE);
		}else {//���������ͼƬ
			//��ʾimagview
			iv_right.setImageResource(right_id);
			if(right_click!=null){//��������������¼�
				iv_right.setOnClickListener(right_click);
			}
		}
		
	}
	/**
	 * ��title����������ɫ
	 * @param color
	 */
	public void setTextColor(int color){
		tv_title.setTextColor(color);
	}
	

}
