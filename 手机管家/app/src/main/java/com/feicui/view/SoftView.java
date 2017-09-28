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
 * �������������ͼ
 * @author Administrator
 *
 */
public class SoftView extends View{

	//����
	private RectF oval;
	//����
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
	 * ͨ���Ƕȱ仯��ȥʵ�ֶ���Ч��
	 * @param targetAngle   ��ʵ�Ƕ�
	 */
	public void changeAngle(final float targetAngle){
		
		final Timer timer=new Timer();
		//����ʱ������ÿ��30��������һ��
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//ÿ�ξ����Ƕȶ����ϱ仯
				sweepAngle+=10;
				if(sweepAngle>=targetAngle){
					sweepAngle=targetAngle;
					//�ر��߳�
					timer.cancel();
				}
				//���߳���ȥ���µ��û��Ʒ���
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
		//ȥ��Сֵ
		int len=Math.min(width, height);
		//ʵ��������
		oval=new RectF(0, 0, len, len);
		setMeasuredDimension(len, len);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.drawArc(oval, 0, 360, true, outPaint);
		canvas.drawArc(oval, startAngle, sweepAngle, true, inPaint);
		
	}
}
