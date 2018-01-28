package com.hawallen.moodpath.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.RelativeLayout;

import com.hawallen.moodpath.R;


public class TrendMonthOneDayLayout extends RelativeLayout {
	
	private LayoutParams params;
	private double mood;
	
	public TrendMonthOneDayLayout(Context context, double mood) {
		super(context);
		this.mood = mood;
		init();
	}
	
	private void init() {
		params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(params);
		setBackgroundResource(R.drawable.transparent);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int height = getHeight();
		int width = getWidth();
		int textSize = 20;
		Paint paint = new Paint();
		paint.setColor(getMoodColor(mood));
		paint.setStrokeWidth(3);
		canvas.drawRect(0, 0, width, (float) (mood/5*height), paint);
		paint.setColor(getResources().getColor(R.color.black));
		paint.setTextSize(textSize);
		if (mood != 5) {
			canvas.drawText(String.format("%.0f", mood*20)+"ml", (width)/2-textSize, (float) (mood/5*height-textSize/2), paint);
		} else {
			canvas.drawText(String.format("%.0f", mood*20)+"ml", (width)/2-textSize*4/3, (float) (mood/5*height-textSize/2), paint);
		}
		
	}
	

	private int getMoodColor(double mood) {
		int color = 0;
		if (mood >= 1 && mood < 1.5) {
			color = getResources().getColor(R.color.angry);
		} else if (mood >= 1.5 && mood < 2.5) {
			color = getResources().getColor(R.color.sad);
		} else if (mood >= 2.5 && mood < 3.5) {
			color = getResources().getColor(R.color.uncertain);
		} else if (mood >= 3.5 && mood < 4.5) {
			color = getResources().getColor(R.color.smile);
		} else if (mood >= 4.5 && mood <= 5) {
			color = getResources().getColor(R.color.happy);
		}
		return color;
	}

}
