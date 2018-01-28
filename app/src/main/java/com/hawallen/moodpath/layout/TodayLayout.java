package com.hawallen.moodpath.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hawallen.moodpath.MainActivity;
import com.hawallen.moodpath.R;
import com.hawallen.moodpath.bean.Article;
import com.hawallen.moodpath.bean.Articles;
import com.hawallen.moodpath.bean.MoodData;
import com.hawallen.moodpath.bean.Setting;
import com.hawallen.moodpath.bean.TipsRecord;
import com.hawallen.moodpath.utils.DayCalculator;
import com.hawallen.moodpath.utils.FileManager;
import com.hawallen.moodpath.utils.FileService;
import com.hawallen.moodpath.utils.ShareService;
import com.umeng.fb.UMFeedbackService;

import java.util.Calendar;
import java.util.List;

public class TodayLayout extends LinearLayout {

	private MainActivity mContext;

	private TextView tvMoodVolume, tvMood;
	private ImageView ivMood;
//	public int ivMoodResource;
//	public int bgResource;
//	public int mood;
//	public String moodString;
	private TodayMoodLayout todayMoodLayout;

	private View vTipsContent;
	private LinearLayout llBottle, llTipsContent;
	public LinearLayout llHealth, llSports, llJoke, llFB;
	private ImageView ivHealth, ivSports, ivJoke;
	private ImageView ivShare, ivRefresh;

	private AlertDialog alertDialog;
	private PopupWindow popupWindow;
	private MyHandler mHandler;

	public double moodAvg = -1;
	private String shareString = "";

	private int searchTime = 0;
	private Articles[] articless = new Articles[3];
	private TipsRecord tRecord;
	
	private int[] sources = new int[6];
    private Vibrator vibrator;  

	public TodayLayout(MainActivity context) {
		super(context);
		mContext = context;

		init();
//		setViews();
		operate();
	}

	private void init() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(layoutParams);
		View today = View.inflate(mContext, R.layout.today, null);
		today.setLayoutParams(layoutParams);
		addView(today);

		TopMenuOnClickListener topMenuOnClickListener = new TopMenuOnClickListener();
		ivShare = (ImageView) findViewById(R.id.iv_today_share);
		ivShare.setOnClickListener(topMenuOnClickListener);
		ivRefresh = (ImageView) findViewById(R.id.iv_today_refresh);
		ivRefresh.setOnClickListener(topMenuOnClickListener);

		tvMoodVolume = (TextView) today.findViewById(R.id.tv_today_mood_volume);
		tvMood = (TextView) today.findViewById(R.id.tv_today_mood);
		ivMood = (ImageView) today.findViewById(R.id.iv_today_mood);
		llBottle = (LinearLayout) today.findViewById(R.id.ll_today_bottle);

		llTipsContent = (LinearLayout) today.findViewById(R.id.ll_today_tips_content);
		llHealth = (LinearLayout) today.findViewById(R.id.ll_today_health);
		llSports = (LinearLayout) today.findViewById(R.id.ll_today_sports);
		llJoke = (LinearLayout) today.findViewById(R.id.ll_today_joke);
		llFB = (LinearLayout) today.findViewById(R.id.ll_today_fb);

		ivHealth = (ImageView) today.findViewById(R.id.iv_today_health);
		ivSports = (ImageView) today.findViewById(R.id.iv_today_sports);
		ivJoke = (ImageView) today.findViewById(R.id.iv_today_joke);

		sources[0] = R.drawable.main_bg2;
		sources[1] = R.drawable.main_bg3;
		sources[2] = R.drawable.main_bg4;
		sources[3] = R.drawable.main_bg5;
		sources[4] = R.drawable.main_bg6;
		sources[5] = R.drawable.main_bg7;
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
	}

	private void setViews() {
		Calendar calendar = Calendar.getInstance();
		String date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
		MoodData moodData = FileService.getMoodData(date);
		List<Integer> moodTimes;
		if (moodData != null && !moodData.getuMood().isEmpty()) {
			moodAvg = DayCalculator.getDayMoodAvg(moodData.getuMood());

			moodTimes = DayCalculator.getEachMoodUnlockTimes(moodData.getuMood());
			todayMoodLayout = new TodayMoodLayout(mContext, moodTimes);
			llBottle.addView(todayMoodLayout);
			// setMoodMost(moodTimes);
			setMoodText(moodAvg);
		}
	}

	private void operate() {
		TipsOnClickListener tipsOnClickListener = new TipsOnClickListener();
		llHealth.setOnClickListener(tipsOnClickListener);
		llSports.setOnClickListener(tipsOnClickListener);
		llJoke.setOnClickListener(tipsOnClickListener);
		llFB.setOnClickListener(new OnFBListener());
	}

	public void resetViews() {
		llBottle.removeAllViews();
		ivMood.setBackgroundResource(0);
		tvMood.setText("无心情记录哦");
		setViews();
	}

	private void setMoodText(double moodAvg) {
		tvMoodVolume.setText(String.format("%.0f", moodAvg * 20) + "ml");
		if (moodAvg >= 1 && moodAvg < 1.5) {
//			moodString = "愤怒";
			tvMood.setText("喝了会上火");
			ivMood.setBackgroundResource(R.drawable.today_angry2);
//			ivMoodResource = R.drawable.today_angry2;
//			bgResource = mContext.getResources().getColor(R.color.angry);
			shareString = "你不在沉默中爆发，就在沉默中灭亡！";
		} else if (moodAvg >= 1.5 && moodAvg < 2.5) {
//			moodString = "伤心";
			tvMood.setText("有点苦涩");
			ivMood.setBackgroundResource(R.drawable.today_sad2);
//			ivMoodResource = R.drawable.today_sad2;
//			bgResource = mContext.getResources().getColor(R.color.sad);
			shareString = "脸上的快乐，别人看得到。心里的痛又有谁能感觉到";
		} else if (moodAvg >= 2.5 && moodAvg < 3.5) {
			tvMood.setText("跟白开水差不多");
			ivMood.setBackgroundResource(R.drawable.today_uncertain2);
//			ivMoodResource = R.drawable.today_uncertain2;
//			bgResource = mContext.getResources().getColor(R.color.uncertain);
			shareString = "平淡是生活的主线，精彩是人生的点缀";
		} else if (moodAvg >= 3.5 && moodAvg < 4.5) {
//			moodString = "开心";
			tvMood.setText("味道不错哦");
			ivMood.setBackgroundResource(R.drawable.today_smile2);
//			ivMoodResource = R.drawable.today_smile2;
//			bgResource = mContext.getResources().getColor(R.color.smile);
			shareString = "开心是自然的，高兴是养成的，快乐是无形的";
		} else if (moodAvg >= 4.5 && moodAvg <= 5) {
//			moodString = "开心";
			tvMood.setText("味道好极了");
			ivMood.setBackgroundResource(R.drawable.today_happy2);
//			ivMoodResource = R.drawable.today_happy2;
//			bgResource = mContext.getResources().getColor(R.color.happy);
			shareString = "心马上要跳出来一样";
		}
//		if (moodAvg < 2.5 || moodAvg >= 3.5) {
			ivMood.setOnClickListener(new MoodOnClick());
//		} else {
//			ivMood.setOnClickListener(null);
//		}
	}

	private void setMoodMost(List<Integer> list) {
		if (!list.isEmpty()) {
			int mood = 3;
			int moodNum = list.get(0);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) >= moodNum) {
					moodNum = list.get(i);
					mood = i + 1;
				}
			}
			if (mood >= 1 && mood < 1.5) {
				ivMood.setBackgroundResource(R.drawable.today_angry2);
			} else if (mood >= 1.5 && mood < 2.5) {
				ivMood.setBackgroundResource(R.drawable.today_sad2);
			} else if (mood >= 2.5 && mood < 3.5) {
				ivMood.setBackgroundResource(R.drawable.today_uncertain2);
			} else if (mood >= 3.5 && mood < 4.5) {
				ivMood.setBackgroundResource(R.drawable.today_smile2);
			} else if (mood >= 4.5 && mood <= 5) {
				ivMood.setBackgroundResource(R.drawable.today_happy2);
			}
		}
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
				resetViews();
			}
		}
	}

	private class ShareByOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			alertDialog.cancel();
			ShareService mWeiboSharer = new ShareService(mContext);
			if (v.getId() == R.id.ll_share_alert_weibo) {
				share2Sina(mWeiboSharer);
			} else {
				mWeiboSharer.share2contact(shareString);
			}
		}
	}
	
	private void share2Sina(ShareService mWeiboSharer) {
		mContext.llMain.buildDrawingCache();
		Bitmap mainBitmap = mContext.llMain.getDrawingCache();
		Calendar calendar = Calendar.getInstance();
		String folderName = "pic/" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
		String fileName = calendar.get(Calendar.DAY_OF_MONTH) + "" + calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + "" + calendar.get(Calendar.SECOND);
		String picPath = FileManager.FilePath + folderName + "/" + fileName + ".png";
		FileManager.savePicture(mainBitmap, folderName, fileName);

		mWeiboSharer.share2sina("#" + getResources().getString(R.string.app_name) + "#" + shareString, picPath);
	}
	
	private class OnFBListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			UMFeedbackService.openUmengFeedbackSDK(mContext);
		}
	}

	private class TipsOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			vTipsContent = View.inflate(mContext, R.layout.today_tips_content, null);
			popupWindow = new PopupWindow(vTipsContent, llTipsContent.getWidth(), llTipsContent.getHeight(), true);
			popupWindow.setAnimationStyle(R.style.PopupAnimation);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					resetView();
				}
			});
			int[] location = new int[2];
			llTipsContent.getLocationOnScreen(location);
			popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

			TextView tipsContentTitle = (TextView) vTipsContent.findViewById(R.id.tv_today_tips_content_title);
			TextView tipsContent = (TextView) vTipsContent.findViewById(R.id.tv_today_tips_content);
			tipsContentTitle.setText("");
			tipsContent.setText("");
			if (v == llHealth) {
				ivHealth.setBackgroundResource(R.drawable.today_health2_pd);
				String[] fileList = FileManager.getFileList("article/" + "health");
				if (fileList.length != 0) {
					readRecord(0);

					Article article = getArticle(fileList, "health", 0);
					if (article != null) {
						tipsContentTitle.setText(article.getTitle());
						tipsContentTitle.setTextColor(getResources().getColor(R.color.yellow));
						tipsContent.setText(article.getContent());
					}
				}
			} else if (v == llSports) {
				ivSports.setBackgroundResource(R.drawable.today_sports2_pd);
				String[] fileList = FileManager.getFileList("article/" + "sports");
				if (fileList.length != 0) {
					readRecord(1);

					Article article = getArticle(fileList, "sports", 1);
					if (article != null) {
						tipsContentTitle.setText(article.getTitle());
						tipsContentTitle.setTextColor(getResources().getColor(R.color.blue));
						tipsContent.setText(article.getContent());
					}
				}
			} else if (v == llJoke) {
				ivJoke.setBackgroundResource(R.drawable.today_joke2_pd);
				String[] fileList = FileManager.getFileList("article/" + "advise");
				if (fileList.length != 0) {
					readRecord(2);

					Article article = getArticle(fileList, "advise", 2);
					if (article != null) {
						tipsContentTitle.setText(article.getTitle());
						tipsContentTitle.setTextColor(getResources().getColor(R.color.pink));
						tipsContent.setText(article.getContent());
					}
				}
			}
		}
	}

	private class MoodOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			Setting setting;
			int skin = sources[(int) (Math.random()*6)];
        	mContext.llMain.setBackgroundResource(skin);
        	mContext.llMainBG = skin;
        	String strSetting = FileManager.getSetting("setting");
    		if (!strSetting.equals("")) {
    			setting = new Gson().fromJson(strSetting, Setting.class);
    		} else {
    			setting = new Setting();
    		}
    		setting.setSkin(skin);
    		FileManager.saveSetting(new Gson().toJson(setting), "setting");
    		vibrator.vibrate(200);
    		
			
//			share2Sina(new ShareServer(mContext));
		}
	}
	private void readRecord(int type) {
		if (tRecord == null) {
			String strtRecord = FileManager.getArticleRecord("TipsRecord");
			if (!strtRecord.equals("")) {
				tRecord = new Gson().fromJson(strtRecord, TipsRecord.class);
			} else {
				tRecord = new TipsRecord();
			}
		}
	}

	private Article getArticle(String[] fileList, String typeName, int type) {
		Article article = null;
		boolean hasMoodValue = false;
		if (articless[type] == null) {
			for (int i = tRecord.fIndex[type]; i < fileList.length; i++) {
				String strArticle = FileManager.getArticle(typeName, fileList[i].substring(0, fileList[i].length() - 4));
				if (!strArticle.equals("")) {
					articless[type] = new Gson().fromJson(strArticle, Articles.class);
					tRecord.fIndex[type] = i;
					tRecord.aIndex[type] = 0;
					break;
				}
			}
		}
		if (articless[type] != null) {
			List<Article> articleList = articless[type].getArticles();
			for (int j = tRecord.aIndex[type]; j < articleList.size(); j++) {
				if (articleList.get(j).getValue() == getMood()) {
					hasMoodValue = true;
					searchTime = 0;
					article = articleList.get(j);
					tRecord.aIndex[type] = j + 1;
					FileManager.saveArticleRecord(new Gson().toJson(tRecord), "TipsRecord");
					break;
				}
			}
		}
		if (!hasMoodValue) {
			articless[type] = null;
			tRecord.fIndex[type]++;
			if (tRecord.fIndex[type] >= fileList.length) {
				tRecord.fIndex[type] = 0;
			}
			searchTime++;
			if (searchTime > fileList.length) {
				return article;
			} else {
				return getArticle(fileList, typeName, type);
			}
		}
		return article;

	}

	private int getMood() {
		int mood = 0;
		if (moodAvg >= 1 && moodAvg < 1.5) {
			mood = 1;
		} else if (moodAvg >= 1.5 && moodAvg < 2.5) {
			mood = 2;
		} else if (moodAvg >= 2.5 && moodAvg < 3.5) {
			mood = 3;
		} else if (moodAvg >= 3.5 && moodAvg < 4.5) {
			mood = 4;
		} else if (moodAvg >= 4.5 && moodAvg <= 5) {
			mood = 5;
		}
		return mood;
	}

	private void resetView() {
		ivHealth.setBackgroundResource(R.drawable.today_health2);
		ivSports.setBackgroundResource(R.drawable.today_sports2);
		ivJoke.setBackgroundResource(R.drawable.today_joke2);
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			popupWindow.dismiss();
			super.handleMessage(msg);
		}
	}

}
