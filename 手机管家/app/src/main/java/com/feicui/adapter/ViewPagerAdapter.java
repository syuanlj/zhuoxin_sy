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
	//创建一个集合用来保存，图片
	List<ImageView> mImageViews;
	//用来显示的图片资源Id
	static int[] imageId={R.drawable.adware_style_applist,R.drawable.adware_style_banner,R.drawable.adware_style_creditswall};
	//用来实例化ImageView对象
	Context context;
	  public ViewPagerAdapter(Context context) {
		 this.context=context;
		mImageViews = new ArrayList<ImageView>();
		
	}
	/**
	 * 获取显示的view的数量
	 */
	@Override
	public int getCount() {
		
		return imageId.length;
	}

	/**
	 * 判断当前的View是否来自一个Object对象
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}

	/**
	 * 销毁被移除的view
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		container.removeView(mImageViews.get(position));
		
		
	}

	/**
	 * 添加要显示的view
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		//实例化ImageView对象
		ImageView imageView =new ImageView(context);
		
		//设置缩放格式
		imageView.setScaleType(ScaleType.FIT_CENTER);
		//设置ImageView要显示的图片
		imageView.setImageResource(imageId[position]);
		//添加到容器中
		container.addView(imageView);
		//将图片加入集合
		mImageViews.add(imageView);
		return imageView;
	}

	
	
}
