package com.feicui.activity;



import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.feicui.adapter.ViewPagerAdapter;
import com.feicui.base.BaseActivity;

public class LeadActivity extends BaseActivity implements OnClickListener {

	ViewPager vp;
	TextView tv;
	private PagerAdapter adapter;
	RadioGroup rg;
	RadioButton rb0;
	RadioButton rb1;
	RadioButton rb2;
	SharedPreferences sp;
	private boolean isFirst;
	private boolean isFromSetting;
	private Editor ed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp=getSharedPreferences("isShowLead", MODE_PRIVATE);
		ed=sp.edit();
		isFirst=sp.getBoolean("isFirst", true);
		//用来判断是不是从settingactivity进入引导界面的
		isFromSetting=sp.getBoolean("isFromSetting", false);
		
		if(isFromSetting){//如果是
			setContentView(R.layout.activity_lead);
			initView();
			// 实例化适配器
			adapter = new ViewPagerAdapter(this);
			// 设置vp的适配器
			vp.setAdapter(adapter);
			initEvent();
			
		}else{//如果不是
			if(isFirst){
				setContentView(R.layout.activity_lead);
				initView();
				// 实例化适配器
				adapter = new ViewPagerAdapter(this);
				// 设置vp的适配器
				vp.setAdapter(adapter);
				initEvent();
			}else {
				//跳转
				startActivityFinish(LogoActivity.class);
			}
		}

	}

	private void initEvent() {
		vp.setOnPageChangeListener(onPageChangeListener);
		rg.setOnCheckedChangeListener(onCheckedChangeListener);
		tv.setOnClickListener(this);
	}

	private void initView() {
		// 实例化ViewPager
		vp = (ViewPager) findViewById(R.id.vp);
		tv = (TextView) findViewById(R.id.tv);
		rg = (RadioGroup) findViewById(R.id.rg);
		rb0=(RadioButton) findViewById(R.id.radio0);
		rb1=(RadioButton) findViewById(R.id.radio1);
		rb2=(RadioButton) findViewById(R.id.radio2);
	}

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		/**
		 * 3、onPageSelected(int arg0) 此方法是页面跳转完后被调用，arg0是你当前选中的页面的Position（位置编号）
		 */
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				tv.setVisibility(View.INVISIBLE);
				rb0.setChecked(true);
				rb1.setChecked(false);
				rb2.setChecked(false);
				break;
			case 1:
				tv.setVisibility(View.INVISIBLE);
				rb0.setChecked(false);
				rb1.setChecked(true);
				rb2.setChecked(false);
				break;
			case 2:
				tv.setVisibility(View.VISIBLE);
				rb0.setChecked(false);
				rb1.setChecked(false);
				rb2.setChecked(true);
				break;
			default:
				break;
			}

		}

		/**
		 * 2、onPageScrolled(int arg0,float arg1,int arg2)
		 * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直被调用。 其中三个参数的含义分别为： arg0
		 * :当前页面，及你点击滑动的页面 arg1:当前页面偏移的百分比 arg2:当前页面偏移的像素位置
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 1、onPageScrollStateChanged(int arg0) 此方法是在状态改变的时候调用。
		 * 其中arg0这个参数有三种状态（0，1，2） arg0 ==1的时表示正在滑动，arg0==2的时表示滑动完毕了，
		 * arg0==0的时表示什么都没做 当页面开始滑动的时候，三种状态的变化顺序为1-->2-->0
		 * 
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio0:
				vp.setCurrentItem(0);
				
				break;
			case R.id.radio1:
				vp.setCurrentItem(1);
				break;
			case R.id.radio2:
				vp.setCurrentItem(2);
				break;

			default:
				break;
			}

		}
	};
	@Override
	public void onClick(View v) {
		ed.putBoolean("isFirst", false);
		ed.apply();
		if(isFromSetting){//如果是从设置界面进来
			//将isFromSetting改为false
			ed.putBoolean("isFromSetting", false);
			ed.commit();
			//点击跳过的时候，直接finish当前
			this.finish();
			
		}else{
			startActivityFinish(LogoActivity.class);
		}
		
		
		
	}
}
