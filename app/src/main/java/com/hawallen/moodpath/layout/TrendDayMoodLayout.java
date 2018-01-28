package com.hawallen.moodpath.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.RelativeLayout;

import com.hawallen.moodpath.R;

import java.util.List;


public class TrendDayMoodLayout extends RelativeLayout {

	private int marginLeftRight = 60;
	private int marginTop = 80;
	private int marginBottom = 20;

	private List<Double> mPoints;
	private List<Integer> hasDataHours;

	public TrendDayMoodLayout(Context context, List<Double> points, List<Integer> hasDataHours) {
		super(context);
		mPoints = points;
		this.hasDataHours = hasDataHours;
		setBackgroundResource(R.drawable.transparent);

		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(layoutParams);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!mPoints.isEmpty()) {
			int textSize = 16;
			Paint linePaint = new Paint();
			linePaint.setColor(getResources().getColor(R.color.draw_line_no_data));
			linePaint.setStrokeWidth(5);
			Paint pointPaint = new Paint();
			Paint textPaint = new Paint();
			textPaint.setColor(getResources().getColor(R.color.white));
			textPaint.setTextSize(textSize);
			int width = getWidth() - marginLeftRight;
			int height = getHeight() / 2 - marginTop - marginBottom;
			int eachX = width / 6;
			int eachY = height / 4;

			// 画上半段曲线
			for (int i = 3; i < mPoints.size() - 3; i++) {
				int x1 = (i - 3) * eachX + marginLeftRight / 2;
				int y1 = (int) (height - (mPoints.get(i)-1) * eachY + marginTop);
				int x2 = (i - 2) * eachX + marginLeftRight / 2;
				int y2 = (int) (height - (mPoints.get(i + 1)-1) * eachY + marginTop);
				if (hasDataHours.contains(i + 1)) {
					linePaint.setColor(getResources().getColor(R.color.draw_line_has_data));
				}
				canvas.drawLine(x1, y1, x2, y2, linePaint);
				linePaint.setColor(getResources().getColor(R.color.draw_line_no_data));
			}
			for (int i = 3; i < mPoints.size() - 2; i++) {
				int x1 = (i - 3) * eachX + marginLeftRight / 2;
				int y1 = (int) (height - (mPoints.get(i)-1) * eachY + marginTop);
				canvas.drawText(String.format("%.0f", mPoints.get(i)*20)+"ml", x1- textSize*4/3, y1 - 10, textPaint);
				Bitmap pointBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trend_point2);
				canvas.drawBitmap(pointBitmap, x1 - pointBitmap.getWidth() / 2, y1 - pointBitmap.getHeight() / 2, pointPaint);
				Bitmap bitmap = getBitmap(mPoints.get(i));
				canvas.drawBitmap(bitmap, x1 - bitmap.getWidth() / 2, y1 - bitmap.getHeight() - 30, pointPaint);
			}

			// 画下半段曲线
			for (int i = 9; i < mPoints.size() - 1; i++) {
				int x1 = (i - 9) * eachX + marginLeftRight / 2;
				int y1 = (int) (getHeight() - (mPoints.get(i)-1) * eachY - marginTop);
				int x2 = (i - 8) * eachX + marginLeftRight / 2;
				int y2 = (int) (getHeight() - (mPoints.get(i + 1)-1) * eachY - marginTop);
				if (hasDataHours.contains(i + 1)) {
					linePaint.setColor(getResources().getColor(R.color.draw_line_has_data));
				}
				canvas.drawLine(x1, y1, x2, y2, linePaint);
				linePaint.setColor(getResources().getColor(R.color.draw_line_no_data));
			}

			// 零点链接处
			int x22 = 3 * eachX + marginLeftRight / 2;
			int y22 = (int) (getHeight() - (mPoints.get(0)-1) * eachY - marginTop);
			int x11 = 2 * eachX + marginLeftRight / 2;
			int y11 = (int) (getHeight() - (mPoints.get(mPoints.size() - 1) -1)* eachY - marginTop);
			if (hasDataHours.contains(0)) {
				linePaint.setColor(getResources().getColor(R.color.draw_line_has_data));
			}
			canvas.drawLine(x11, y11, x22, y22, linePaint);
			linePaint.setColor(getResources().getColor(R.color.draw_line_no_data));

			for (int i = 9; i < mPoints.size(); i++) {
				int x1 = (i - 9) * eachX + marginLeftRight / 2;
				int y1 = (int) (getHeight() - (mPoints.get(i)-1) * eachY - marginTop);
				canvas.drawText(String.format("%.0f", mPoints.get(i)*20)+"ml", x1- textSize*4/3, y1 + 25, textPaint);
				Bitmap pointBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trend_point2);
				canvas.drawBitmap(pointBitmap, x1 - pointBitmap.getWidth() / 2, y1 - pointBitmap.getHeight() / 2, pointPaint);
				Bitmap bitmap = getBitmap(mPoints.get(i));
				canvas.drawBitmap(bitmap, x1 - bitmap.getWidth() / 2, y1 + 30, pointPaint);
			}

			for (int i = 0; i < mPoints.size() - 9; i++) {
				int x1 = (i + 3) * eachX + marginLeftRight / 2;
				int y1 = (int) (getHeight() - (mPoints.get(i)-1) * eachY - marginTop);
				int x2 = (i + 4) * eachX + marginLeftRight / 2;
				int y2 = (int) (getHeight() - (mPoints.get(i + 1)-1) * eachY - marginTop);
				if (i == 0) {
					linePaint.setColor(getResources().getColor(R.color.draw_line_on_zero));
				}
				if (hasDataHours.contains(i + 1)) {
					linePaint.setColor(getResources().getColor(R.color.draw_line_has_data));
				}
				canvas.drawLine(x1, y1, x2, y2, linePaint);
				linePaint.setColor(getResources().getColor(R.color.draw_line_no_data));
			}

			for (int i = 0; i < mPoints.size() - 8; i++) {
				int x1 = (i + 3) * eachX + marginLeftRight / 2;
				int y1 = (int) (getHeight() - (mPoints.get(i)-1) * eachY - marginTop);
				canvas.drawText(String.format("%.0f", mPoints.get(i)*20)+"ml", x1- textSize*4/3, y1 + 25, textPaint);
				Bitmap pointBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trend_point2);
				canvas.drawBitmap(pointBitmap, x1 - pointBitmap.getWidth() / 2, y1 - pointBitmap.getHeight() / 2, pointPaint);
				Bitmap bitmap = getBitmap(mPoints.get(i));
				canvas.drawBitmap(bitmap, x1 - bitmap.getWidth() / 2, y1 + 30, pointPaint);
			}

		}
	}

	private Bitmap getBitmap(double mood) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trend_day_happy2);
		if (mood >= 1 && mood < 1.5) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trend_day_angry2);
		} else if (mood >= 1.5 && mood < 2.5) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trend_day_sad2);
		} else if (mood >= 2.5 && mood < 3.5) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trend_day_uncertain2);
		} else if (mood >= 3.5 && mood < 4.5) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trend_day_smile2);
		} else if (mood >= 4.5 && mood < 5) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trend_day_happy2);
		}
		return bitmap;
	}

}
