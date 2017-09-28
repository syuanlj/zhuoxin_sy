package com.feicui.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
/**
 * 软件管理界面饼形图
 * @author Administrator
 *
 */
public class SoftView extends View{

	//矩形
	private RectF oval;
	//画笔
	private Paint outPaint;
	
	
	private float startAngle=-90;
	private float sweepAngle=0;
	
	private Paint inPaint;


	public SoftView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		outPaint=new Paint();
		outPaint.setARGB(255, 0, 255, 0);
		outPaint.setAntiAlias(true);
		
		inPaint=new Paint();
		inPaint.setColor(Color.BLUE);
		inPaint.setAntiAlias(true);
		
	}

	/**
	 * 通过角度变化，去实现动画效果
	 * @param targetAngle   真实角度
	 */
	public void changeAngle(final float targetAngle){
		
		final Timer timer=new Timer();
		//开启时间任务，每个30毫秒运行一次
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//每次经过角度都不断变化
				sweepAngle+=10;
				if(sweepAngle>=targetAngle){
					sweepAngle=targetAngle;
					//关闭线程
					timer.cancel();
				}
				//子线程中去重新调用绘制方法
				postInvalidate();
				
			}
		}, 500,30);
		
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width=MeasureSpec.getSize(widthMeasureSpec);
		int height=MeasureSpec.getSize(heightMeasureSpec);
		//去最小值
		int len=Math.min(width, height);
		//实例化矩形
		oval=new RectF(0, 0, len, len);
		setMeasuredDimension(len, len);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.drawArc(oval, 0, 360, true, outPaint);
		canvas.drawArc(oval, startAngle, sweepAngle, true, inPaint);
		
	}
}
