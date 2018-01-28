package com.hawallen.moodpath.layout;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hawallen.moodpath.MainActivity;
import com.hawallen.moodpath.R;
import com.hawallen.moodpath.bean.MoodData;
import com.hawallen.moodpath.utils.FileService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TrendDayLayout extends LinearLayout {
	
	private MainActivity mContext;
	private LayoutParams params;
	private LinearLayout llTrendDay;
	private TrendDayMoodLayout trendDayMoodLayout;
	private TextView tvMood061, tvMood08, tvMood10, tvMood12, tvMood14, tvMood16, tvMood181, tvMood182, tvMood20, tvMood22, tvMood00, tvMood02, tvMood04, tvMood062; 
	public String date;

	private List<Double> points = new ArrayList<Double>();
	private List<Integer> hasDataHours = new ArrayList<Integer>();

	private String shareString = "";
	
	public TrendDayLayout(MainActivity context) {
		super(context);
		this.mContext = context;
		
		Calendar calendar = Calendar.getInstance();
		date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
		
		init();
	}
	
	public TrendDayLayout(MainActivity context, String date) {
		super(context);
		this.mContext = context;
		this.date = date;
		init();
	}
	
	private void init() {
		params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(params);

		View dayView = View.inflate(mContext, R.layout.trend_day, null);
		dayView.setLayoutParams(params);
		addView(dayView);

		llTrendDay = (LinearLayout) dayView.findViewById(R.id.ll_trend_day);

		tvMood061 = (TextView) findViewById(R.id.tv_trend_day_mood061);
		tvMood08 = (TextView) findViewById(R.id.tv_trend_day_mood08);
		tvMood10 = (TextView) findViewById(R.id.tv_trend_day_mood10);
		tvMood12 = (TextView) findViewById(R.id.tv_trend_day_mood12);
		tvMood14 = (TextView) findViewById(R.id.tv_trend_day_mood14);
		tvMood16 = (TextView) findViewById(R.id.tv_trend_day_mood16);
		tvMood181 =(TextView)  findViewById(R.id.tv_trend_day_mood181);
		tvMood182 =(TextView)  findViewById(R.id.tv_trend_day_mood182);
		tvMood20 = (TextView) findViewById(R.id.tv_trend_day_mood20);
		tvMood22 = (TextView) findViewById(R.id.tv_trend_day_mood22);
		tvMood00 = (TextView) findViewById(R.id.tv_trend_day_mood00);
		tvMood02 = (TextView) findViewById(R.id.tv_trend_day_mood02);
		tvMood04 = (TextView) findViewById(R.id.tv_trend_day_mood04);
		tvMood062 = (TextView) findViewById(R.id.tv_trend_day_mood062);
		
		refresh();
	}
	
	public void refresh() {
		points.clear();
		llTrendDay.removeAllViews();
		setPoints(date);
		trendDayMoodLayout = new TrendDayMoodLayout(mContext, points, hasDataHours);
		llTrendDay.addView(trendDayMoodLayout);
		setMood(points);
	}
	
	private void setMood(List<Double> points) {
		if (points.size() == 12) {
			setMoodText(tvMood00, points.get(0));
			setMoodText(tvMood02, points.get(1));
			setMoodText(tvMood04, points.get(2));
			setMoodText(tvMood061, points.get(3));
			setMoodText(tvMood062, points.get(3));
			setMoodText(tvMood08, points.get(4));
			setMoodText(tvMood10, points.get(5));
			setMoodText(tvMood12, points.get(6));
			setMoodText(tvMood14, points.get(7));
			setMoodText(tvMood16, points.get(8));
			setMoodText(tvMood181, points.get(9));
			setMoodText(tvMood182, points.get(9));
			setMoodText(tvMood20, points.get(10));
			setMoodText(tvMood22, points.get(11));
		}
	}

	public String getShareString() {
		return shareString;
	}
	
	private void setMoodText(TextView textView, double mood) {
		if (mood >= 1 && mood < 1.5) {
			textView.setText("火气");
			shareString = "你不在沉默中爆发，就在沉默中灭亡！";
		} else if (mood >= 1.5 && mood < 2.5) {
			textView.setText("忧伤");
			shareString = "脸上的快乐，别人看得到。心里的痛又有谁能感觉到";
		} else if (mood >= 2.5 && mood < 3.5) {
			textView.setText("淡淡");
			shareString = "平淡是生活的主线，精彩是人生的点缀";
		} else if (mood >= 3.5 && mood < 4.5) {
			textView.setText("开怀");
			shareString = "开心是自然的，高兴是养成的，快乐是无形的";
		} else if (mood >= 4.5 && mood <= 5) {
			textView.setText("兴奋");
			shareString = "心马上要跳出来一样";
		}
	}

	private void setPoints(String date) {
		MoodData moodData = FileService.getMoodData(date);
		int[] xValues;
		double[] yValues;
		if (moodData != null) {
			Map<String, Integer> uMood = moodData.getuMood();
			Iterator<String> iterator = uMood.keySet().iterator();
			xValues = new int[uMood.size()];
			yValues = new double[uMood.size()];
			int i = 0;
			int hour = -1;
			Map<Integer, List<Double>> hourMood = new HashMap<Integer, List<Double>>();
			List<Double> moodList = new ArrayList<Double>();
			//截取“时”的记录
			while (iterator.hasNext()) {
				String time = (String) iterator.next();
				String[] HMS = time.split(":");
				xValues[i] = Integer.valueOf(HMS[0]);
				yValues[i] = uMood.get(time);
				if (hour == xValues[i]) {
					moodList.add(yValues[i]);
					hourMood.put(hour, moodList);
				} else {
					moodList = new ArrayList<Double>();
					hour = xValues[i];
					moodList.add(yValues[i]);
					hourMood.put(hour, moodList);
				}
				i++;
			}
			Iterator<Integer> iterator2 = hourMood.keySet().iterator();
			xValues = new int[hourMood.size()];
			yValues = new double[hourMood.size()];
			i = 0;
			//计算每个小时的心情平均值
			while (iterator2.hasNext()) {
				int hour2 = iterator2.next();
				List<Double> moodT = hourMood.get(hour2);
				double total = 0;
				for (int j = 0; j < moodT.size(); j++) {
					total = total + moodT.get(j);
				}
				double avg = total / moodT.size();
				xValues[i] = hour2;
				yValues[i] = avg;
				i++;
			}
			
			//排序
			for (int j = 0; j < xValues.length; j++) {
				for (int k = j+1; k < xValues.length; k++) {
					if (xValues[j] > xValues[k]) {
						int tempX = xValues[j];
						xValues[j] = xValues[k];
						xValues[k] = tempX;
						double tempY = yValues[j];
						yValues[j] = yValues[k];
						yValues[k] = tempY;
					}
				}
				
			}
			
			//初始化所有点，并设置第一个点
			boolean hasAdd = false;
			for (int j = 0; j < 12; j++) {
				if (!hasAdd && xValues.length > 0 && xValues[0]/2+1 == j) {
						points.add(yValues[0]);
						hasAdd = true;
				} else {
					points.add(-0.1);
				}
			}
			//设置有记录的点
			boolean last2HoursHaveRecord = false;
			int nowP;
			for (int j = 0; j < yValues.length; j++) {
				nowP = xValues[j]/2+1;
				if (nowP == 12) {
					last2HoursHaveRecord = true;
					nowP = 0;
				}
				double mood = points.get(nowP);
				if (mood != -0.1) {
					points.set(nowP, (mood + yValues[j])/2);
				} else {
					points.set(nowP, yValues[j]);
				}
				if (!hasDataHours.contains(nowP)) {
					hasDataHours.add(nowP);
				}
			}
			
			
			//设置2点到第一个点的值
			double before = -0.1;
			double now;
			for (int j = 1; j < points.size(); j++) {
				now = points.get(j);
				if (now != -0.1) {
					before = now;
				} else {
					points.set(j, before);
				}
			}
			//设置最后一个点到0点的值
			for (int j = 1; j < points.size(); j++) {
				if (points.get(j) == -0.1) {
					points.set(j, 3.0);
				}
			}
			//设置0点
			if (!last2HoursHaveRecord) {
				points.set(0, points.get(points.size()-1));
			}
			
		}
	}
}
