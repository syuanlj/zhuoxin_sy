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
		
		//将xml动画加载为animation对象
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.set_logo);
		
		
		//启动动画
		rl.startAnimation(animation);
		//设置动画监听
		animation.setAnimationListener(new AnimationListener() {
			
			/**
			 * 动画开始时调用
			 */
			@Override
			public void onAnimationStart(Animation animation) {
				// 让动画结束的时候保持结束时状态
				animation.setFillAfter(true);
			}
			/**
			 * 动画重复时调用
			 */
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			/**
			 * 动画结束时调用
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
//				//界面跳转时的动画，参数一：进入界面的动画，参数二就是退出界面的动画
//				overridePendingTransition(R.anim.set_main, R.anim.set_logo);
//			}
//		});
		
//		//为了实现自动跳转，需要借助子线程
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					//线程休眠
//					Thread.sleep(2000);
//					runOnUiThread(new Runnable() {//从子线程转到UI线程
//						
//						@Override
//						public void run() {
//							Intent intent =new Intent(LogoActivity.this, MainActivity.class);
//							startActivity(intent);
//							//界面跳转时的动画，参数一：进入界面的动画，参数二就是退出界面的动画
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
