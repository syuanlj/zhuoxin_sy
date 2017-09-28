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
		//�����ж��ǲ��Ǵ�settingactivity�������������
		isFromSetting=sp.getBoolean("isFromSetting", false);
		
		if(isFromSetting){//�����
			setContentView(R.layout.activity_lead);
			initView();
			// ʵ����������
			adapter = new ViewPagerAdapter(this);
			// ����vp��������
			vp.setAdapter(adapter);
			initEvent();
			
		}else{//�������
			if(isFirst){
				setContentView(R.layout.activity_lead);
				initView();
				// ʵ����������
				adapter = new ViewPagerAdapter(this);
				// ����vp��������
				vp.setAdapter(adapter);
				initEvent();
			}else {
				//��ת
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
		// ʵ����ViewPager
		vp = (ViewPager) findViewById(R.id.vp);
		tv = (TextView) findViewById(R.id.tv);
		rg = (RadioGroup) findViewById(R.id.rg);
		rb0=(RadioButton) findViewById(R.id.radio0);
		rb1=(RadioButton) findViewById(R.id.radio1);
		rb2=(RadioButton) findViewById(R.id.radio2);
	}

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		/**
		 * 3��onPageSelected(int arg0) �˷�����ҳ����ת��󱻵��ã�arg0���㵱ǰѡ�е�ҳ���Position��λ�ñ�ţ�
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
		 * 2��onPageScrolled(int arg0,float arg1,int arg2)
		 * ��ҳ���ڻ�����ʱ�����ô˷������ڻ�����ֹ֮ͣǰ���˷�����һֱ�����á� �������������ĺ���ֱ�Ϊ�� arg0
		 * :��ǰҳ�棬������������ҳ�� arg1:��ǰҳ��ƫ�Ƶİٷֱ� arg2:��ǰҳ��ƫ�Ƶ�����λ��
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 1��onPageScrollStateChanged(int arg0) �˷�������״̬�ı��ʱ����á�
		 * ����arg0�������������״̬��0��1��2�� arg0 ==1��ʱ��ʾ���ڻ�����arg0==2��ʱ��ʾ��������ˣ�
		 * arg0==0��ʱ��ʾʲô��û�� ��ҳ�濪ʼ������ʱ������״̬�ı仯˳��Ϊ1-->2-->0
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
		if(isFromSetting){//����Ǵ����ý������
			//��isFromSetting��Ϊfalse
			ed.putBoolean("isFromSetting", false);
			ed.commit();
			//���������ʱ��ֱ��finish��ǰ
			this.finish();
			
		}else{
			startActivityFinish(LogoActivity.class);
		}
		
		
		
	}
}
