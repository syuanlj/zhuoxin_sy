package com.feicui.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
	// ��Բ���Ļ���
	private Paint paint;
	// �����εĿ��
	private int len;
	// Բ���İ뾶
	private float radius;
	// ����
	private RectF oval;
	// Բ������ʼ�Ƕ�
	private float startAngle = 120;
	// Բ���ľ����ܷ�Χ�ǶȽǶ�
	private float sweepAngle = 300;

	// �̶Ⱦ����Ƕȷ�Χ
	private float targetAngle = 300;

	// ��������
	Paint textPaint;

	// �����Ƕȱ仯��Ӧ����ɫ�仯
	private OnAngleColorListener onAngleColorListener;

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		textPaint = new Paint();
		textPaint.setARGB(255, 255, 255, 255);
		textPaint.setAntiAlias(true);

		waterPaint = new Paint();
		waterPaint.setAntiAlias(true);

		moveWaterLine();
	}

	/**
	 * ���ö���Ч�����������̶߳�ʱ����
	 * 
	 * @param trueAngle
	 */
	// ǰ�����ߺ��˵�״̬��1����ǰ����2������ˡ���ʼΪ����״̬��
	int state = 2;
	// ÿ�κ���ʱ��ֵ��ʵ��Խ��Խ���Ч��
	private int[] back = { 2, 2, 4, 4, 6, 6, 8, 8, 10 };
	// ÿ��ǰ��ʱ��ֵ��ʵ��Խ��Խ����Ч��
	private int[] go = { 10, 10, 8, 8, 6, 6, 4, 4, 2 };
	// ǰ�����±�
	private int go_index = 0;
	// ���˵��±�
	private int back_index = 0;
	private int score;
	private int color;
	private boolean isRunning;
	public void change(final float trueAngle) {
		if(isRunning==true){
			return;
		}
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				switch (state) {
				case 1:
					// ��ʼ����
					targetAngle += go[go_index];
					go_index++;
					if (go_index == go.length) {// ������Ԫ��ʱ���±�һֱΪ����
						go_index--;
					}
					if (targetAngle >= trueAngle) {// ��������̶ȴ��ڵ�����ʵ�Ƕ�
						// �����̶�=��ʵ�Ƕ�
						targetAngle = trueAngle;
						// ״̬��Ϊ1��ǰ����
						state = 2;
						isRunning=false;
						timer.cancel();
					}
					break;
				case 2:
					isRunning=true;
					targetAngle -= back[back_index];
					back_index++;
					if (back_index == back.length) {
						back_index--;
					}

					if (targetAngle <= 0) {
						targetAngle = 0;
						state = 1;
					}
					break;
				default:
					break;
				}
				// ���㵱ǰ����Ӧ�õĶ��ٷ�
				score = (int) (targetAngle / 300 * 100);
				// �������ǰ��ռ������Ӧ����������
				up = (int) (targetAngle / 360 * clipRadius * 2);

				postInvalidate();
			}
		}, 0, 30);

	}

	public void moveWaterLine() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				move += 10;
				postInvalidate();
			}
		}, 500, 200);
	}

	// ��ŵ�һ��ˮ��Yֵ
	private float[] firstWaterLine;
	// �ڶ���
	private float[] secondWaterLine;
	// ��ˮ��Ļ���
	private Paint waterPaint;
	// Ӱ�����Ǻ����ĳ���
	private float move;
	// ����Բ�İ뾶
	private int clipRadius;
	// ˮ�������ֵ
	int up = 0;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// ͨ�����������ÿ�͸�
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		// ȡ����Сֵ
		len = Math.min(width, height);
		oval = new RectF(0, 0, len, len);
		radius = len / 2;
		clipRadius = (len / 2) - 45;
		firstWaterLine = new float[len];
		secondWaterLine = new float[len];
		setMeasuredDimension(len, len);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// ����һ��Բ���������������ϵ����ת�����Բ�д��
		// canvas.drawArc(oval, startAngle, sweepAngle, false, paint);
		// ������Բ����ߣ��뾶����ʼ�Ƕȣ������Ƕ�,
		// ˵���˾���canvasû���ṩ������ͼ�εķ���������Ҫ�����Լ�ȥʵ�����ֹ�����
		// ���̶���
		drawLine(canvas);
		// ���̶����ڵ�����
		drawText(canvas);

	}

	/**
	 * ��ˮ��Ĺ���
	 * 
	 * @param canvas
	 */
	private void drawWaterView(Canvas canvas) {
		// y = Asin(wx+b)+h �������ʽ�wӰ�����ڣ�AӰ�������hӰ��yλ�ã�bΪ���ࣻ
		// �����ڶ�Ϊview�ܿ��
		float mCycleFactorW = (float) (2 * Math.PI / len);

		// �õ���һ������yֵ
		for (int i = 0; i < len; i++) {
			firstWaterLine[i] = (float) (10 * Math
					.sin(mCycleFactorW * i + move) - up);
		}
		// �õ���һ������yֵ
		for (int i = 0; i < len; i++) {
			secondWaterLine[i] = (float) (15 * Math.sin(mCycleFactorW * i
					+ move + 10) - up);

		}

		canvas.save();

		// �ü���Բ������
		Path path = new Path();
		waterPaint.setColor(color);
		path.reset();

		path.addCircle(len / 2, len / 2, clipRadius, Path.Direction.CCW);
		canvas.clipPath(path, android.graphics.Region.Op.REPLACE);
		// ������ϵ�Ƶ��ײ�
		canvas.translate(0, len / 2 + clipRadius);

		for (int i = 0; i < len; i++) {
			canvas.drawLine(i, firstWaterLine[i], i, len, waterPaint);
		}
		for (int i = 0; i < len; i++) {
			canvas.drawLine(i, secondWaterLine[i], i, len, waterPaint);
		}
		canvas.restore();
	}

	/**
	 * ʵ�ֻ��̶����ڵ�����
	 * 
	 * @param canvas
	 */
	private void drawText(Canvas canvas) {
		Paint cPaint = new Paint();
		// cPaint.setARGB(50, 236, 241, 243);
		cPaint.setAlpha(50);
		cPaint.setARGB(50, 236, 241, 243);
		// ��Բ�α���
		RectF smalloval = new RectF(40, 40, radius * 2 - 40, radius * 2 - 40);
		// ��ˮ��
		drawWaterView(canvas);
		canvas.drawArc(smalloval, 0, 360, true, cPaint);
		// ��СԲȦ����Χ��һ����ɫȦ
		// canvas.drawArc(smalloval, 0, 360, false, paint);
		// �����ı����뷽ʽ�����ж���
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(radius/2);
		
		// ������
		canvas.drawText("" + score, radius, radius, textPaint);
		textPaint.setTextSize(radius/6);
		// ���̶�ֵ��
		canvas.drawText("��", radius + radius/3+10, radius - radius/4, textPaint);
		textPaint.setTextSize(radius/6);
		// ���̶�ֵ�����Ż�
		canvas.drawText("����Ż�", radius, radius + radius/2, textPaint);

	}

	float a = sweepAngle / 100;
	private Paint linePaint;

	/**
	 * ʵ�ֻ��̶��ߵĹ���
	 * 
	 * @param canvas
	 */
	private void drawLine(final Canvas canvas) {
		// ����֮ǰ�Ļ���״̬
		canvas.save();
		// �ƶ�������ʵ�����Ǹı�����ϵ��λ��
		canvas.translate(radius, radius);
		// ��ת����ϵ,��Ҫȷ����ת�Ƕ�
		canvas.rotate(30);
		// ��ʼ������
		linePaint = new Paint();
		// ���û��ʵĿ�ȣ��ߵĴ�ϸ��
		linePaint.setStrokeWidth(2);
		// ���ÿ����
		linePaint.setAntiAlias(true);
		// �ۼƵ��ӵĽǶ�
		float c = 0;
		for (int i = 0; i <= 100; i++) {

			if (c <= targetAngle && targetAngle != 0) {// ����ۼƻ����ĽǶȣ�С�ڵ�ǰ��Ч�̶�
				// �����ۼƻ����Ŀ̶Ȱٷֱȣ������Ŀ̶ȱ����й������Ŀ̶ȣ�
				double p = c / (double) sweepAngle;

				int red = 255 - (int) (p * 255);
				int green = (int) (p * 255);
				color = linePaint.getColor();
				if (onAngleColorListener != null) {
					onAngleColorListener.onAngleColorListener(red, green);
				}
				linePaint.setARGB(255, red, green, 50);
				canvas.drawLine(0, radius, 0, radius - 20, linePaint);
				// �����ĽǶȽ��е���
				c += a;
			} else {
				linePaint.setColor(Color.WHITE);
				canvas.drawLine(0, radius, 0, radius - 20, linePaint);
			}

			canvas.rotate(a);
		}
		// �ָ�����״̬��
		canvas.restore();
	}

	public void setOnAngleColorListener(
			OnAngleColorListener onAngleColorListener) {
		this.onAngleColorListener = onAngleColorListener;
	}

	/**
	 * �����ǶȺ���ɫ�仯�Ľӿ�
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnAngleColorListener {
		void onAngleColorListener(int red, int green);
	}
}
