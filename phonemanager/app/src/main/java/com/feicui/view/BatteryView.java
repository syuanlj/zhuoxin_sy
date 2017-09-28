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
	// ����
	private int score;
	// ����ˮ�������½�
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
	// 2��ʾ�½�
	int state = 2;
	private int red;
	private int green;

	public void upWaterView(final float pawer) {
		if (isRunning) {
			return;
		}
		// ������תΪ�ȱ�����ˮ��߶�
		
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

	// �߳̿���
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
		// ͨ�����������ÿ�͸�
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		// ȡ����Сֵ
		len = Math.min(width, height);
		up = len;
		clipRadius = len / 2;
		// ���ÿ��
		setMeasuredDimension(len, len);
		// �����������飬�������沨���ϵ�Yֵ
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
		// �Ȼ�һ����Բ
		canvas.drawCircle(len / 2, len / 2, clipRadius, biggerPaint);
		drawWaterView(canvas);
		drawWaterText(canvas);

	}

	/**
	 * ���̶�����
	 * 
	 * @param canvas
	 */
	private void drawWaterText(Canvas canvas) {

		Paint textPaint = new Paint();
		// �����������
		textPaint.setTextAlign(Align.CENTER);
		int textSize = (int) ((clipRadius - 10) / 2);
		textPaint.setColor(Color.WHITE);
		// ���ִ�С
		textPaint.setTextSize(textSize);
		// ������ֵ
		canvas.drawText("" + score, len / 2, len / 2, textPaint);
		// �ڵ���ֵ�·�������鿴�¶�
		textPaint.setTextSize(textSize / 3);
		canvas.drawText("����鿴�¶�", len / 2, len / 2 + textSize, textPaint);

	}

	/**
	 * ��ˮ��
	 * 
	 * @param canvas
	 */
	private void drawWaterView(Canvas canvas) {
		canvas.save();
		// �ü���Բ������
		Path path = new Path();
		// ����
		path.reset();
		// ���һ��Բ��·��
		path.addCircle(len / 2, len / 2, clipRadius - 10, Path.Direction.CCW);
		// ��ʼ����·��������ʾ���з�Χ�ڵ����ݡ���
		canvas.clipPath(path, android.graphics.Region.Op.REPLACE);

		// y = Asin(wx+b)+h �������ʽ�wӰ�����ڣ�AӰ�������hӰ��yλ�ã�bΪ���ࣻ
		// �����ڶ�Ϊview�ܿ��
		float mCycleFactorW = (float) (2 * Math.PI / len);

		// �õ���һ������yֵ
		for (int i = 0; i < len; i++) {
			firstWaterLine[i] = (float) (10 * Math
					.sin(mCycleFactorW * i + move) - up);
		}
		// �õ���һ������yֵ(�ڵ�һ�������)
		for (int i = 0; i < len; i++) {
			secondWaterLine[i] = (float) (15 * Math.sin(mCycleFactorW * i
					+ move + 10) - up);

		}

		canvas.translate(0, len - 10);
		linePaint.setARGB(255, red, green, 0);
		//�ӿڻص�
		if(onColorChangeListener!=null){
			onColorChangeListener.onColorChangeListener(red, green);
		}
		// ����һ�����·������ɫ
		for (int i = 0; i < len; i++) {
			canvas.drawLine(i, firstWaterLine[i], i, len, linePaint);
		}
		// ���ڶ������·������ɫ
		for (int i = 0; i < len; i++) {
			canvas.drawLine(i, secondWaterLine[i], i, len, linePaint);
		}
		// �ָ�����������»�������
		canvas.restore();
	}
	
	
	/**
	 * ʵ�����ӿڶ���ķ���
	 * @param onColorChangeListener
	 */
	public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener) {
		this.onColorChangeListener = onColorChangeListener;
	}

	private OnColorChangeListener onColorChangeListener;

	/**
	 * ������ɫ����Ľӿ�
	 * @author Administrator
	 *
	 */
	public interface OnColorChangeListener{
		//��ɫ�ص�
		void onColorChangeListener(int red,int green);
	}

}
