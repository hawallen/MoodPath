package com.hawallen.moodpath;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hawallen.moodpath.bean.Setting;
import com.hawallen.moodpath.layout.AdviseLayout;
import com.hawallen.moodpath.layout.SettingLayout;
import com.hawallen.moodpath.layout.TodayLayout;
import com.hawallen.moodpath.layout.TrendLayout;
import com.hawallen.moodpath.utils.FileManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

	private LinearLayout content;
	public LinearLayout llMain;
	public int llMainBG;
	private LinearLayout llToday, llTrend, llAdvise;
	public RelativeLayout rlSetting;
	private TextView menuToday, menuTrend, menuAdvise, menuSetting, tvUpdate;
	public TodayLayout todayLayout;
	public TrendLayout trendLayout;
	private AdviseLayout adviseLayout;
	public SettingLayout settingLayout;
	private Setting setting;
	
	public SensorManager sensorManager;  
    private static final int SENSOR_SHAKE = 10;
    

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        
		init();

		com.umeng.common.Log.LOG = true;
		UMFeedbackService.enableNewReplyNotification(this, NotificationType.AlertDialog);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UpdateListener());
		UmengUpdateAgent.update(MainActivity.this);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "退出");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			finish();
			return true;
		default:
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Builder builder = new Builder(this);
		builder.setTitle("是否退出软件");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		String strSetting = FileManager.getSetting("setting");
		if (!strSetting.equals("")) {
			setting = new Gson().fromJson(strSetting, Setting.class);
		} else {
			setting = new Setting();
		}
		if (setting.getSkin() == 0) {
			llMain.setBackgroundResource(R.drawable.main_bg2);
			llMainBG = R.drawable.main_bg2;
		} else {
			llMain.setBackgroundResource(setting.getSkin());
			llMainBG = setting.getSkin();
		}
		todayLayout.resetViews();
		trendLayout.trendDayLayout.refresh();
		
		if (sensorManager != null) {// ע�������   
			sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		MobclickAgent.onPause(this);
		if (sensorManager != null) {// ȡ�������   
            sensorManager.unregisterListener(sensorEventListener);  
        }  

		super.onPause();
	}
	/** 
     * ������Ӧ���� 
     */  
    private SensorEventListener sensorEventListener = new SensorEventListener() {  
  
        @Override  
        public void onSensorChanged(SensorEvent event) {  
            // ��������Ϣ�ı�ʱִ�и÷���   
            float[] values = event.values;  
            float x = values[0]; // x�᷽����������ٶȣ�����Ϊ��   
            float y = values[1]; // y�᷽����������ٶȣ���ǰΪ��   
            float z = values[2]; // z�᷽����������ٶȣ�����Ϊ��   
//            Log.i(TAG, "x�᷽����������ٶ�" + x +  "��y�᷽����������ٶ�" + y +  "��z�᷽����������ٶ�" + z);  
            // һ���������������������ٶȴﵽ40�ʹﵽ��ҡ���ֻ��״̬��   
            int medumValue = 19;// ���� i9250��ô�ζ����ᳬ��20��û�취��ֻ����19��   
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {  
                Message msg = new Message();  
                msg.what = SENSOR_SHAKE;  
                handler.sendMessage(msg);  
            }  
        }  
  
        @Override  
        public void onAccuracyChanged(Sensor sensor, int accuracy) {  
  
        }  
    };  
  
    /** 
     * ����ִ�� 
     */  
    Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
        		switch (msg.what) {  
                case SENSOR_SHAKE:
                	Intent intent = new Intent();
                	intent.setClass(MainActivity.this, ShakeActivity.class);
                	startActivity(intent);
                    break;
                }
        }  
  
    };

	private void init() {
		llMain = (LinearLayout) findViewById(R.id.ll_main_bg);

		content = (LinearLayout) findViewById(R.id.ll_main_content);

		menuToday = (TextView) findViewById(R.id.tv_main_menu1);
		menuTrend = (TextView) findViewById(R.id.tv_main_menu2);
		menuAdvise = (TextView) findViewById(R.id.tv_main_menu3);
		menuSetting = (TextView) findViewById(R.id.tv_main_menu4);
		tvUpdate = (TextView) findViewById(R.id.tv_main_update);

		llToday = (LinearLayout) findViewById(R.id.ll_main_menu1);
		llTrend = (LinearLayout) findViewById(R.id.ll_main_menu2);
		llAdvise = (LinearLayout) findViewById(R.id.ll_main_menu3);
		rlSetting = (RelativeLayout) findViewById(R.id.ll_main_menu4);

		todayLayout = new TodayLayout(this);
		trendLayout = new TrendLayout(this);
		adviseLayout = new AdviseLayout(this);
		settingLayout = new SettingLayout(this);

//		MenuOnTouchListener motl = new MenuOnTouchListener();
		MenuOnClickListener mocl = new MenuOnClickListener();

		llToday.setOnClickListener(mocl);
		llTrend.setOnClickListener(mocl);
		llAdvise.setOnClickListener(mocl);
		rlSetting.setOnClickListener(mocl);

		// llToday.setOnTouchListener(motl);
		// llTrend.setOnTouchListener(motl);
		// llAdvise.setOnTouchListener(motl);
		// llSetting.setOnTouchListener(motl);

		llToday.performClick();

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				copy();
			}
		});
		thread.start();
	}

	public void copy() {
		try {
			AssetManager assetManager = getAssets();
			String[] list = assetManager.list("article");
			for (int i = 0; i < list.length; i++) {
				String[] subList = assetManager.list("article/"+list[i]);
				String[] fileList = FileManager.getFileList("article/" + list[i]);
				if (fileList.length == 0) {
					for (int j = 0; j < subList.length; j++) {
						InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("article/" + list[i] + "/" + subList[j]));
						BufferedReader bufReader = new BufferedReader(inputReader);
						String line = "";
						String result = "";
						while ((line = bufReader.readLine()) != null)
							result += line;
						FileManager.saveArticle(result, list[i], subList[j].substring(0, subList[j].length() - 4));
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void setUpdateMarkVisibility(int visibility) {
		tvUpdate.setVisibility(visibility);
	}

	private class UpdateListener implements UmengUpdateListener {
		public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
			switch (updateStatus) {
			case 0:
				// has update
				setUpdateMarkVisibility(View.VISIBLE);
				settingLayout.setUpdateMarkVisibility(View.VISIBLE);
				break;
			}
		}
	}

	private class MenuOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			resetViews();

			switch (v.getId()) {
			case R.id.ll_main_menu1:
				llToday.setBackgroundResource(R.drawable.menu_bg2_pd);
				menuToday.setBackgroundResource(R.drawable.menu21_pd);
				content.addView(todayLayout);
				break;
			case R.id.ll_main_menu2:
				llTrend.setBackgroundResource(R.drawable.menu_bg2_pd);
				menuTrend.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu22_pd));
				content.addView(trendLayout);
				break;
			case R.id.ll_main_menu3:
				llAdvise.setBackgroundResource(R.drawable.menu_bg2_pd);
				menuAdvise.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu23_pd));
				content.addView(adviseLayout);
				break;
			case R.id.ll_main_menu4:
				rlSetting.setBackgroundResource(R.drawable.menu_bg2_pd);
				menuSetting.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu24_pd));
				content.addView(settingLayout);
				break;
			default:
				break;
			}
		}
	}

	private void resetViews() {
		content.removeAllViews();

		llToday.setBackgroundColor(Color.TRANSPARENT);
		llTrend.setBackgroundColor(Color.TRANSPARENT);
		llAdvise.setBackgroundColor(Color.TRANSPARENT);
		rlSetting.setBackgroundColor(Color.TRANSPARENT);

		menuToday.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu21));
		menuTrend.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu22));
		menuAdvise.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu23));
		menuSetting.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu24));
	}

}