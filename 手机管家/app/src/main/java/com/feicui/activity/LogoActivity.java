package com.feicui.activity;

import com.feicui.base.BaseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class LogoActivity extends BaseActivity {
	RelativeLayout rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		rl=(RelativeLayout) findViewById(R.id.rl);
		
		//��xml��������Ϊanimation����
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.set_logo);
		
		
		//��������
		rl.startAnimation(animation);
		//���ö�������
		animation.setAnimationListener(new AnimationListener() {
			
			/**
			 * ������ʼʱ����
			 */
			@Override
			public void onAnimationStart(Animation animation) {
				// �ö���������ʱ�򱣳ֽ���ʱ״̬
				animation.setFillAfter(true);
			}
			/**
			 * �����ظ�ʱ����
			 */
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			/**
			 * ��������ʱ����
			 */
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent =new Intent(LogoActivity.this, MainActivity.class);
				startActivity(intent);
				LogoActivity.this.finish();
				overridePendingTransition(R.anim.set_main, 0);
			}
		});
		
//		rl.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				Intent intent =new Intent(LogoActivity.this, MainActivity.class);
//				startActivity(intent);
//				//������תʱ�Ķ���������һ���������Ķ����������������˳�����Ķ���
//				overridePendingTransition(R.anim.set_main, R.anim.set_logo);
//			}
//		});
		
//		//Ϊ��ʵ���Զ���ת����Ҫ�������߳�
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					//�߳�����
//					Thread.sleep(2000);
//					runOnUiThread(new Runnable() {//�����߳�ת��UI�߳�
//						
//						@Override
//						public void run() {
//							Intent intent =new Intent(LogoActivity.this, MainActivity.class);
//							startActivity(intent);
//							//������תʱ�Ķ���������һ���������Ķ����������������˳�����Ķ���
//							overridePendingTransition(R.anim.set_main, R.anim.set_logo);
//							
//						}
//					});
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		}).start();
		
	}
}
