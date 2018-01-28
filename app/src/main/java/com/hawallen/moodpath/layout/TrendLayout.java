package com.hawallen.moodpath.layout;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hawallen.moodpath.MainActivity;
import com.hawallen.moodpath.R;
import com.hawallen.moodpath.utils.FileManager;
import com.hawallen.moodpath.utils.ShareService;

import java.util.Calendar;


public class TrendLayout extends LinearLayout {

	private MainActivity mContext;
	private LinearLayout llTrend, llSomeday;
	public TrendDayLayout trendDayLayout, trendSomedayLayout;
	private TrendMonthLayout trendMonthLayout;
	private TextView tvTrendDay, tvTrendMonth, tvTrendSomeday;

	private ImageView ivShare, ivRefresh;

	private AlertDialog alertDialog;
	
	private GestureDetector gestureDetector;
	
	private String shareString = "";
	
	public TrendLayout(MainActivity context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub

		init();
	}

	private void init() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(layoutParams);
		View trend = View.inflate(mContext, R.layout.trend, null);
		trend.setLayoutParams(layoutParams);
		addView(trend);

		TopMenuOnClickListener topMenuOnClickListener = new TopMenuOnClickListener();
		ivShare = (ImageView) findViewById(R.id.iv_trend_share);
		ivShare.setOnClickListener(topMenuOnClickListener);
		ivRefresh = (ImageView) findViewById(R.id.iv_trend_refresh);
		ivRefresh.setOnClickListener(topMenuOnClickListener);

		tvTrendDay = (TextView) trend.findViewById(R.id.tv_trend_day_button);
		tvTrendMonth = (TextView) trend.findViewById(R.id.tv_trend_month_button);
		tvTrendSomeday = (TextView) trend.findViewById(R.id.tv_trend_someday_button);
		DayMonthOnClickListener listener = new DayMonthOnClickListener();
		tvTrendDay.setOnClickListener(listener);
		tvTrendMonth.setOnClickListener(listener);
		tvTrendSomeday.setOnClickListener(listener);

		trendDayLayout = new TrendDayLayout(mContext);

		trendMonthLayout = new TrendMonthLayout(mContext);

		llTrend = (LinearLayout) trend.findViewById(R.id.ll_trend_content);
		llSomeday = (LinearLayout) trend.findViewById(R.id.ll_trend_someday);

		tvTrendDay.performClick();
		
		gestureDetector = new GestureDetector(new DefaultGestureDetector());
	}
	
	public void showSomeday(TrendDayLayout trendDayLayout) {
		trendSomedayLayout = trendDayLayout;
		llSomeday.setVisibility(View.VISIBLE);
		tvTrendSomeday.setText(trendDayLayout.date.substring(5));
		tvTrendSomeday.performClick();
	}

	private class TopMenuOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == ivShare) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("选择分享方式");
				View view = View.inflate(mContext, R.layout.share_alert, null);
				builder.setView(view);
				ShareByOnClickListener shareByOnClickListener = new ShareByOnClickListener();
				LinearLayout llWeibo = (LinearLayout) view.findViewById(R.id.ll_share_alert_weibo);
				llWeibo.setOnClickListener(shareByOnClickListener);
				LinearLayout llMessage = (LinearLayout) view.findViewById(R.id.ll_share_alert_message);
				llMessage.setOnClickListener(shareByOnClickListener);
				alertDialog = builder.create();
				alertDialog.show();
			} else if (v == ivRefresh) {
				trendDayLayout.refresh();
				trendMonthLayout.refresh();
			}
		}
	}

	private class ShareByOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			alertDialog.cancel();
			ShareService mWeiboSharer = new ShareService(mContext);
			if (v.getId() == R.id.ll_share_alert_weibo) {
				mContext.llMain.buildDrawingCache();
				Bitmap mainBitmap = mContext.llMain.getDrawingCache();
				Calendar calendar = Calendar.getInstance();
				String folderName = "pic/" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
				String fileName = calendar.get(Calendar.DAY_OF_MONTH) + "" + calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + "" + calendar.get(Calendar.SECOND);
				String picPath = FileManager.FilePath + folderName + "/" + fileName + ".png";
				FileManager.savePicture(mainBitmap, folderName, fileName);
				mWeiboSharer.share2sina("#"+getResources().getString(R.string.app_name)+"#"+shareString, picPath);
			} else {
				mWeiboSharer.share2contact(shareString);
			}
		}
	}
	
	private class DayMonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			resetView();
			if (v == tvTrendDay) {
				tvTrendDay.setBackgroundResource(R.drawable.trend_day_month_pd2);
				llTrend.addView(trendDayLayout);
				llTrend.setBackgroundResource(R.drawable.trend_day_bg2);
				shareString = trendDayLayout.getShareString();
			} else if (v == tvTrendMonth) {
				tvTrendMonth.setBackgroundResource(R.drawable.trend_day_month_pd2);
				llTrend.addView(trendMonthLayout);
				shareString = trendMonthLayout.getShareString();
			} else if (v == tvTrendSomeday) {
				tvTrendSomeday.setBackgroundResource(R.drawable.trend_day_month_pd2);
				llTrend.addView(trendSomedayLayout);
				shareString = trendSomedayLayout.getShareString();
			}
		}
	}

	private void resetView() {
		llTrend.removeAllViews();
		llTrend.setBackgroundResource(0);
		tvTrendDay.setBackgroundResource(0);
		tvTrendMonth.setBackgroundResource(0);
		tvTrendSomeday.setBackgroundResource(0);
	}

	private class DefaultGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			final int FLING_MIN_DISTANCE = 100;// X或者y轴上移动的距离(像素)
			final int FLING_MIN_VELOCITY = 200;// x或者y轴上的移动速度(像素/秒)
			if ((e1.getX() - e2.getX()) > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				tvTrendMonth.performClick();
			} else if ((e1.getX() - e2.getX()) < -FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				tvTrendDay.performClick();
			}
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
}
