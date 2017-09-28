package com.feicui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.feicui.activity.R;

public class ViewPagerAdapter extends PagerAdapter {
	//����һ�������������棬ͼƬ
	List<ImageView> mImageViews;
	//������ʾ��ͼƬ��ԴId
	static int[] imageId={R.drawable.adware_style_applist,R.drawable.adware_style_banner,R.drawable.adware_style_creditswall};
	//����ʵ����ImageView����
	Context context;
	  public ViewPagerAdapter(Context context) {
		 this.context=context;
		mImageViews = new ArrayList<ImageView>();
		
	}
	/**
	 * ��ȡ��ʾ��view������
	 */
	@Override
	public int getCount() {
		
		return imageId.length;
	}

	/**
	 * �жϵ�ǰ��View�Ƿ�����һ��Object����
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}

	/**
	 * ���ٱ��Ƴ���view
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		container.removeView(mImageViews.get(position));
		
		
	}

	/**
	 * ���Ҫ��ʾ��view
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		//ʵ����ImageView����
		ImageView imageView =new ImageView(context);
		
		//�������Ÿ�ʽ
		imageView.setScaleType(ScaleType.FIT_CENTER);
		//����ImageViewҪ��ʾ��ͼƬ
		imageView.setImageResource(imageId[position]);
		//��ӵ�������
		container.addView(imageView);
		//��ͼƬ���뼯��
		mImageViews.add(imageView);
		return imageView;
	}

	
	
}
