package com.feicui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class RadarUnderView  extends View{

	private int radius;
	private int len;
	
	public RadarUnderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width=MeasureSpec.getSize(widthMeasureSpec);
		int height=MeasureSpec.getSize(heightMeasureSpec);
		 len=Math.min(width, height);
		radius=len/2-10;
		setMeasuredDimension(len, len);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint=new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		//…Ë÷√≤ªÃÓ≥‰
		paint.setStyle(Style.STROKE);
		canvas.drawCircle(len/2, len/2, radius, paint);
		canvas.drawCircle(len/2, len/2, 2*radius/3, paint);
		
	}
	
}
