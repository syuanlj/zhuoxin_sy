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
import android.view.View.MeasureSpec;

public class RadarUpView extends View {

	private int len;
	private int radius;
	private RectF oval;
	private float startAngle=0;
	private float sweepAngle;
	private Paint paint;

	public RadarUpView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint=new Paint();
		paint.setAntiAlias(true);
		move();
	}
	public boolean isCancle;
	private void move() {
		final Timer timer=new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				startAngle+=1;
				if(isCancle){
					isCancle=false;
					timer.cancel();
				}
				postInvalidate();
			}
		}, 0,30);
		
	}
	public void cancle(){
		isCancle=true;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		len = Math.min(width, height);
		oval=new RectF(10, 10, len-10, len-10);
		setMeasuredDimension(len, len);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		for(int i=0;i<90;i++){
			int a=(int) ((255*(float)i/540));
			paint.setARGB(a, 0, 204, 204);
			canvas.drawArc(oval, startAngle+i, 2, true, paint);
		}
		
	}
}
