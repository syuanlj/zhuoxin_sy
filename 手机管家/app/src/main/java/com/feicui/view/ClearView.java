package com.feicui.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ClearView extends View {

	private int len;
	private int targetAngle = 180;
	private float rotate;
	private boolean isCancle=false;

	public ClearView(Context context, AttributeSet attrs) {
		super(context, attrs);
		move();
	}
	private void move() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				rotate += 2;
				if(isCancle){
					timer.cancel();
				}
				postInvalidate();

			}
		}, 0, 30);

	}
	
	public void cancle(){
		isCancle=true;
	}
	public void start(){
		isCancle=false;
		move();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 通过测量规则获得宽和高
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		// 取出最小值
		len = Math.min(width, height);
		setMeasuredDimension(len, len);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawKe(canvas);
		drawup(canvas);
	}

	private void drawKe(Canvas canvas) {
		canvas.save();
		canvas.translate(len / 2, len / 2);
		// canvas.rotate(rotate);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		int rotateAngle = 360 / 120;
		for (int i = 0; i < 120; i++) {
			paint.setARGB(50, 236, 241, 243);

			canvas.drawLine(0, len / 2, 0, len / 2 - 20, paint);

			canvas.rotate(rotateAngle);
		}
		canvas.restore();

	}

	private void drawup(Canvas canvas) {
		canvas.save();
		canvas.translate(len / 2, len / 2);
		canvas.rotate(rotate);
		int rotateAngle = 180 / 60;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		int canvasAngle = 0;
		for (int i = 0; i <= 60; i++) {
			int a = (int) (255 * canvasAngle / (float) targetAngle);
			paint.setARGB(a, 255, 255, 255);
			canvas.drawLine(0, len / 2, 0, len / 2 - 20, paint);
			if (canvasAngle == targetAngle) {
				canvas.drawCircle(0, len / 2 - 40, 10, paint);
			}
			canvas.rotate(rotateAngle);
			canvasAngle += rotateAngle;
		}

		canvas.restore();
	}
}
