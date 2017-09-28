package com.feicui.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BatteryView extends View {

	private int len;
	private float[] firstWaterLine;
	private float[] secondWaterLine;
	private Paint linePaint;
	private float clipRadius;
	private float move;
	private Paint biggerPaint;
	// 电量
	private int score;
	// 控制水球上升下降
	private double up;

	public BatteryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.GREEN);

		biggerPaint = new Paint();
		biggerPaint.setAntiAlias(true);
		biggerPaint.setARGB(50, 236, 241, 243);
		score = 100;
		green=255;
		move();

	}

	boolean isRunning;
	// 2表示下降
	int state = 2;
	private int red;
	private int green;

	public void upWaterView(final float pawer) {
		if (isRunning) {
			return;
		}
		// 将电量转为等比例的水球高度
		
		final Timer timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				switch (state) {
				case 1:
					score += 1;
					if (score >= pawer) {
						score = (int) pawer;
						state = 2;
						isRunning = false;
						timer.cancel();
					}
					break;
				case 2:
					isRunning = true;
					score -= 1;
					if (score <= 0) {
						score = 0;
						state = 1;
					}
					break;
				default:
					break;
				}
				up = (float)score /100 * len;
				red = 255 - (int) (255 * (up / len));
				green = (int) (255 * (up / len));
				postInvalidate();

			}
		}, 500, 30);

	}

	// 线程开关
	protected boolean isCancel;

	public void move() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				move += 10;
				if (isCancel) {
					timer.cancel();
				}
				postInvalidate();
			}
		}, 500, 200);
	}

	public void cancelTimer() {
		isCancel = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 通过测量规则获得宽和高
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		// 取出最小值
		len = Math.min(width, height);
		up = len;
		clipRadius = len / 2;
		// 设置宽高
		setMeasuredDimension(len, len);
		// 声明两个数组，用来保存波点上的Y值
		firstWaterLine = new float[len];
		secondWaterLine = new float[len];
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		len = w;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 先画一个大圆
		canvas.drawCircle(len / 2, len / 2, clipRadius, biggerPaint);
		drawWaterView(canvas);
		drawWaterText(canvas);

	}

	/**
	 * 画固定文字
	 * 
	 * @param canvas
	 */
	private void drawWaterText(Canvas canvas) {

		Paint textPaint = new Paint();
		// 设置字体居中
		textPaint.setTextAlign(Align.CENTER);
		int textSize = (int) ((clipRadius - 10) / 2);
		textPaint.setColor(Color.WHITE);
		// 文字大小
		textPaint.setTextSize(textSize);
		// 画电量值
		canvas.drawText("" + score, len / 2, len / 2, textPaint);
		// 在电量值下方画点击查看温度
		textPaint.setTextSize(textSize / 3);
		canvas.drawText("点击查看温度", len / 2, len / 2 + textSize, textPaint);

	}

	/**
	 * 画水波
	 * 
	 * @param canvas
	 */
	private void drawWaterView(Canvas canvas) {
		canvas.save();
		// 裁剪成圆形区域
		Path path = new Path();
		// 重置
		path.reset();
		// 添加一个圆形路径
		path.addCircle(len / 2, len / 2, clipRadius - 10, Path.Direction.CCW);
		// 开始剪切路径，（显示剪切范围内的内容。）
		canvas.clipPath(path, android.graphics.Region.Op.REPLACE);

		// y = Asin(wx+b)+h ，这个公式里：w影响周期，A影响振幅，h影响y位置，b为初相；
		// 将周期定为view总宽度
		float mCycleFactorW = (float) (2 * Math.PI / len);

		// 得到第一条波的y值
		for (int i = 0; i < len; i++) {
			firstWaterLine[i] = (float) (10 * Math
					.sin(mCycleFactorW * i + move) - up);
		}
		// 得到第一条波的y值(在第一条的左边)
		for (int i = 0; i < len; i++) {
			secondWaterLine[i] = (float) (15 * Math.sin(mCycleFactorW * i
					+ move + 10) - up);

		}

		canvas.translate(0, len - 10);
		linePaint.setARGB(255, red, green, 0);
		//接口回调
		if(onColorChangeListener!=null){
			onColorChangeListener.onColorChangeListener(red, green);
		}
		// 画第一条波下方的填充色
		for (int i = 0; i < len; i++) {
			canvas.drawLine(i, firstWaterLine[i], i, len, linePaint);
		}
		// 画第二条波下方的填充色
		for (int i = 0; i < len; i++) {
			canvas.drawLine(i, secondWaterLine[i], i, len, linePaint);
		}
		// 恢复画布，结合新画的内容
		canvas.restore();
	}
	
	
	/**
	 * 实例化接口对象的方法
	 * @param onColorChangeListener
	 */
	public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener) {
		this.onColorChangeListener = onColorChangeListener;
	}

	private OnColorChangeListener onColorChangeListener;

	/**
	 * 监听颜色渐变的接口
	 * @author Administrator
	 *
	 */
	public interface OnColorChangeListener{
		//颜色回调
		void onColorChangeListener(int red,int green);
	}

}
