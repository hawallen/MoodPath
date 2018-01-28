package com.hawallen.moodpath;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hawallen.moodpath.layout.UnlockLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;

public class UnlockActivity extends Activity {

	private UnlockLayout unlockLayout;
	private TextView tvHour, tvMinute, tvDate, tvWeek;
	private int hour, minute, year, month, day, week;
//	private Setting setting;

	private int[] sources = new int[6];
	private int notification_id = 060243110;
	public NotificationManager nm;
	public Notification.Builder builder;

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.obj.equals("FinishLockActivity")) {
				showNotification(msg.arg1);
				finish(); // 锁屏成功时，结束我们的Activity界面
			} else if (msg.obj.equals("RefreshTime")) {
				setTimeView();
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		// 全屏显示窗口
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.unlock);
		init();
	}

	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
//		String strSetting = FileManager.getSetting("setting");
//		if (!strSetting.equals("")) {
//			setting = new Gson().fromJson(strSetting, Setting.class);
//		} else {
//			setting = new Setting();
//		}

		// if (setting.getUnlockBG() == 0) {
		unlockLayout.setBackgroundResource(sources[(int) (Math.random() * 6)]);
		// } else {
		// unlockLayout.setBackgroundResource(setting.getUnlockBG());
		// }

		super.onResume();
	}

	private void init() {

		sources[0] = R.drawable.main_bg2;
		sources[1] = R.drawable.main_bg3;
		sources[2] = R.drawable.main_bg4;
		sources[3] = R.drawable.main_bg5;
		sources[4] = R.drawable.main_bg6;
		sources[5] = R.drawable.main_bg7;

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new Notification.Builder(this);
		unlockLayout = (UnlockLayout) findViewById(R.id.ll_unlock);
		unlockLayout.setUnlockHandler(myHandler);

		tvHour = (TextView) unlockLayout.findViewById(R.id.tv_unlock_hour);
		tvMinute = (TextView) unlockLayout.findViewById(R.id.tv_unlock_minute);
		tvDate = (TextView) unlockLayout.findViewById(R.id.tv_unlock_date);
		tvWeek = (TextView) unlockLayout.findViewById(R.id.tv_unlock_week);

		new Thread(new UnlockThread()).start();
	}

	private void setTimeView() {
		tvHour.setText(String.format("%1$02d", hour) + ":");
		tvMinute.setText(String.format("%1$02d", minute));
		tvDate.setText(year + "-" + month + "-" + day);
		switch (week) {
		case Calendar.SUNDAY:
			tvWeek.setText("星期天");
			break;
		case Calendar.MONDAY:
			tvWeek.setText("星期一");
			break;
		case Calendar.TUESDAY:
			tvWeek.setText("星期二");
			break;
		case Calendar.WEDNESDAY:
			tvWeek.setText("星期三");
			break;
		case Calendar.THURSDAY:
			tvWeek.setText("星期四");
			break;
		case Calendar.FRIDAY:
			tvWeek.setText("星期五");
			break;
		case Calendar.SATURDAY:
			tvWeek.setText("星期六");
			break;
		default:
			break;
		}
	}

	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		super.onAttachedToWindow();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			break;
		case KeyEvent.KEYCODE_BACK:
			break;
		default:
			break;
		}
		return false;
	}

	private class UnlockThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
					Calendar calendar = Calendar.getInstance();
					hour = calendar.get(Calendar.HOUR_OF_DAY);
					minute = calendar.get(Calendar.MINUTE);
					year = calendar.get(Calendar.YEAR);
					month = calendar.get(Calendar.MONTH) + 1;
					day = calendar.get(Calendar.DAY_OF_MONTH);
					week = calendar.get(Calendar.DAY_OF_WEEK);
					Message msg = new Message();
					msg.obj = "RefreshTime";
					myHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void showNotification(int moodAvg) {
		long when = System.currentTimeMillis();
        int icon = 0;
        String tickerText = "";
		String title = "心迹";
		String content = "";
		if (moodAvg != -1) {
			if (moodAvg >= 1 && moodAvg < 1.5) {
				icon = R.drawable.trend_day_angry2;
				tickerText = "人生短暂，善待自己，明天会更好";
				content = "人生短暂，善待自己，明天会更好";
			} else if (moodAvg >= 1.5 && moodAvg < 2.5) {
				icon = R.drawable.trend_day_sad2;
				tickerText = "每一种创伤，都是一种成熟";
				content = "每一种创伤，都是一种成熟";
			} else if (moodAvg >= 2.5 && moodAvg < 3.5) {
				icon = R.drawable.trend_day_uncertain2;
				tickerText = "平淡就是福，珍惜所拥有的一切吧";
				content = "守住属于自己的一份平淡的生活，你就是一个幸福的人了";
			} else if (moodAvg >= 3.5 && moodAvg < 4.5) {
				icon = R.drawable.trend_day_smile2;
				tickerText = "对生活笑吧，这样，你能察觉它的美";
				content = "对生活笑吧，这样，你能察觉它的美";
			} else if (moodAvg >= 4.5 && moodAvg <= 5) {
				icon = R.drawable.trend_day_happy2;
				tickerText = "笑一笑，十年少。 一日三笑，不用吃药";
				content = "笑一笑，十年少。 一日三笑，不用吃药";
			}
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setWhen(when).setSmallIcon(icon).setTicker(tickerText).setContentTitle(title)
            .setContentText(content).setContentIntent(pendingIntent);
            Notification notification  = builder.build();
            notification.defaults = Notification.DEFAULT_LIGHTS;
			nm.cancel(notification_id);
			nm.notify(notification_id, notification);
		}
	}
}