package com.hawallen.moodpath.layout;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hawallen.moodpath.MainActivity;
import com.hawallen.moodpath.R;
import com.hawallen.moodpath.bean.MoodData;
import com.hawallen.moodpath.utils.DayCalculator;
import com.hawallen.moodpath.utils.FileService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TrendMonthLayout extends LinearLayout {
	
	private MainActivity mContext;
	private LayoutParams params;
	private LinearLayout llTrendMonth;
	
	private ImageView ivMood;
	private TextView tvDate;
	
	private ArrayList<Double> monthAVGMoods = new ArrayList<Double>();
	
	private String shareString = "";

	public TrendMonthLayout(MainActivity context) {
		super(context);
		mContext = context;
		init();
	}
	
	private void init() {
		params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(params);

		View contentView = View.inflate(mContext, R.layout.trend_month, null);
		contentView.setLayoutParams(params);
		addView(contentView);
		
		tvDate = (TextView) findViewById(R.id.tv_trend_month_date);

		llTrendMonth = (LinearLayout) contentView.findViewById(R.id.ll_trend_month);
		
		refresh();
	}
	
	public void refresh() {
		monthAVGMoods.clear();
		llTrendMonth.removeAllViews();
		
		Calendar calendar = Calendar.getInstance();
		tvDate.setText(calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月");
		for (int i = 0; i < 30; i++) {
			calendar.setTimeInMillis(calendar.getTimeInMillis()-86400000);
			
			final String date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
			MoodData moodData = FileService.getMoodData(date);
			if (moodData!= null && !moodData.getuMood().isEmpty()) {
				View view = View.inflate(mContext, R.layout.trend_month_one, null);
				view.setLayoutParams(params);
				llTrendMonth.addView(view);
				
				TextView tvDay = (TextView) view.findViewById(R.id.tv_trend_month_one_day);
				LinearLayout llMood = (LinearLayout) view.findViewById(R.id.ll_trend_month_one_line);
				
				double moodAvg = DayCalculator.getDayMoodAvg(moodData.getuMood());
				monthAVGMoods.add(moodAvg);
				tvDay.setText(String.format("%1$02d", (calendar.get(Calendar.MONTH) + 1))+"-"+String.format("%1$02d", calendar.get(Calendar.DAY_OF_MONTH)));
				TrendMonthOneDayLayout trendMonthOneDayLayout = new TrendMonthOneDayLayout(mContext, moodAvg);
				trendMonthOneDayLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TrendDayLayout trendDayLayout = new TrendDayLayout(mContext, date);
						mContext.trendLayout.showSomeday(trendDayLayout);
					}
				});
				llMood.addView(trendMonthOneDayLayout);
			}
			ivMood = (ImageView) findViewById(R.id.iv_trend_month_mood);
			ivMood.setBackgroundResource(getMonthMoodBackgoundResource(monthAVGMoods));
		}
	}
	
	public String getShareString() {
		return shareString;
	}
	
	private int getMonthMoodBackgoundResource(List<Double> monthAVGMoods) {
		int res = R.drawable.trend_month_uncertain2;
		double sum = 0;
		double avg = 0;
		if (!monthAVGMoods.isEmpty()) {
			for (int i = 0; i < monthAVGMoods.size(); i++) {
				sum = sum + monthAVGMoods.get(i);
			}
			avg = sum/monthAVGMoods.size();
			if (avg >= 1 && avg < 1.5) {
				res = R.drawable.trend_month_angry2;
				shareString = "你不在沉默中爆发，就在沉默中灭亡！";
			} else if (avg >= 1.5 && avg < 2.5) {
				res = R.drawable.trend_month_sad2;
				shareString = "脸上的快乐，别人看得到。心里的痛又有谁能感觉到";
			} else if (avg >= 2.5 && avg < 3.5) {
				res = R.drawable.trend_month_uncertain2;
				shareString = "平淡是生活的主线，精彩是人生的点缀";
			} else if (avg >= 3.5 && avg < 4.5) {
				res = R.drawable.trend_month_smile2;
				shareString = "开心是自然的，高兴是养成的，快乐是无形的";
			} else if (avg >= 4.5 && avg <= 5) {
				res = R.drawable.trend_month_happy2;
				shareString = "心马上要跳出来一样";
			}
		}
		return res;
	}
	
}
