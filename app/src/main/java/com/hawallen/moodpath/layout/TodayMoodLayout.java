package com.hawallen.moodpath.layout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import com.hawallen.moodpath.R;

import java.util.ArrayList;
import java.util.List;


public class TodayMoodLayout extends LinearLayout {

	private List<Integer> moodTimes;
	private List<Integer> copyMoodTimes;
	private int moodColor;
	private int minNum = 0;
	private int minMoodNum = 0;
	private int marginTop = 130;
	private int marginBottom = 55;
	private int marginLeftAndRight = 8;

	private int width;
	private int height;
	private Paint paint;
	private int j = 0;
	private MyHandler myHandler;
//	private boolean isDrawOver = false;

	public TodayMoodLayout(Context context, List<Integer> moodTimes) {
		super(context);
		this.moodTimes = moodTimes;

		init();
	}

	private void init() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(layoutParams);
		setBackgroundResource(R.drawable.today_bottle_ice2);

		myHandler = new MyHandler();
		copyMoodTimes = new ArrayList<Integer>(moodTimes);
		paint = new Paint();

		Thread thread = new Thread(new MyRunnable());
		thread.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		if (isDrawOver) {
//			j = 5;
//		}
		width = getWidth();
		height = getHeight() - marginTop - marginBottom;
		int totle = 0;
		int hasDrawHeight = 0;
		copyMoodTimes = new ArrayList<Integer>(moodTimes);
		for (int i = 0; i < copyMoodTimes.size(); i++) {
			totle += copyMoodTimes.get(i);
		}

		for (int i = 0; i < j; i++) {
			resetMinMoodColorAndNum();
			paint.setColor(moodColor);
			canvas.drawRect(marginLeftAndRight, height + marginTop - minNum * height / totle - hasDrawHeight, width - marginLeftAndRight - 2, height + marginTop - hasDrawHeight, paint);
			hasDrawHeight += minNum * height / totle;
		}
	}

	private class MyRunnable implements Runnable {
		@Override
		public void run() {
			try {
				for (int i = 0; i < copyMoodTimes.size(); i++) {
					Thread.sleep(200);
					j = i+1;
					myHandler.sendMessage(new Message());
				}
//				isDrawOver = true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void resetMinMoodColorAndNum() {
		Resources res = getResources();
		moodColor = res.getColor(R.color.transparent);
		if (!copyMoodTimes.isEmpty()) {
			minNum = copyMoodTimes.get(0);
			minMoodNum = 0;
			for (int i = 0; i < copyMoodTimes.size(); i++) {
				if (minNum > copyMoodTimes.get(i)) {
					minNum = copyMoodTimes.get(i);
					minMoodNum = i;
				}
			}
			copyMoodTimes.set(minMoodNum, 10000);
			switch (minMoodNum) {
			case 0:
				moodColor = res.getColor(R.color.today_angry);
				break;
			case 1:
				moodColor = res.getColor(R.color.today_sad);
				break;
			case 2:
				moodColor = res.getColor(R.color.today_uncertain);
				break;
			case 3:
				moodColor = res.getColor(R.color.today_smile);
				break;
			case 4:
				moodColor = res.getColor(R.color.today_happy);
				break;
			default:
				break;
			}
		}
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			invalidate();
		}
	}
}
