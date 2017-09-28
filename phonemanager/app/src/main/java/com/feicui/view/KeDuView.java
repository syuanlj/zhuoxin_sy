package com.feicui.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class KeDuView extends View{

	private int radius;
	private int targetAngle;

	public KeDuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public void changeAngle(final int trueAngle) {
		
		final Timer timer=new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				targetAngle+=10;
				if(targetAngle>=trueAngle){
					targetAngle=trueAngle;
					timer.cancel();
				}
				postInvalidate();
			}
		}, 0,30);
		
		
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width=MeasureSpec.getSize(widthMeasureSpec);
		int height=MeasureSpec.getSize(heightMeasureSpec);
		int len=Math.min(width, height);
		radius=len/2;
		setMeasuredDimension(len, len);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		drawKeDu(canvas);
	}

	private void drawKeDu(Canvas canvas) {
		canvas.save();
		canvas.translate(radius, radius);
		canvas.rotate(30);
		Paint linePaint=new Paint();
		linePaint.setAntiAlias(true);
		
		
		int rotateAngle=300/100;
		int canvasAngle=0;
		for(int i=0;i<=100;i++){
			if(canvasAngle<=targetAngle&&targetAngle!=0){
				int red=(int) (255*(float)canvasAngle/300);
				int green = 255-(int) (255*(float)canvasAngle/300);
				linePaint.setARGB(255, red, green, 0);
				canvas.drawLine(0, radius-5, 0, radius-20, linePaint);
			}else{
				linePaint.setColor(Color.WHITE);
				canvas.drawLine(0, radius-5, 0, radius-20, linePaint);
			}
			canvasAngle+=rotateAngle;
			canvas.rotate(rotateAngle);
		}
		
		
		canvas.restore();
		
		
	}

}
