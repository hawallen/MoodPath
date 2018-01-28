package com.hawallen.moodpath;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hawallen.moodpath.bean.Setting;
import com.hawallen.moodpath.utils.FileManager;

public class SettingSkinActivity extends Activity {
	
	private LinearLayout llBack;
	private TextView tvTitle;
	private ImageView ivTarget;
	private LinearLayout llSources;
	private ImageButton ibOk;
	private Setting setting;

	private int[] sources = new int[6];
	private int[] sourcesPre = new int[6];
	private View[] views = new View[6];
	
	private int mark;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_skin);
		
		Intent intent = getIntent();
		mark = intent.getIntExtra("mark", -1);
		
		init();
		operate();
	}
	
	private void init(){
		llBack = (LinearLayout) findViewById(R.id.ll_setting_skin_back);
		tvTitle = (TextView) findViewById(R.id.tv_setting_title);
		ivTarget = (ImageView) findViewById(R.id.iv_setting_target);
		llSources = (LinearLayout) findViewById(R.id.ll_setting_sources);
		ibOk = (ImageButton) findViewById(R.id.ib_setting_ok);

		sources[0] = R.drawable.main_bg2;
		sources[1] = R.drawable.main_bg3;
		sources[2] = R.drawable.main_bg4;
		sources[3] = R.drawable.main_bg5;
		sources[4] = R.drawable.main_bg6;
		sources[5] = R.drawable.main_bg7;
		
		sourcesPre[0] = R.drawable.main_bg2_preview;
		sourcesPre[1] = R.drawable.main_bg3_preview;
		sourcesPre[2] = R.drawable.main_bg4_preview;
		sourcesPre[3] = R.drawable.main_bg5_preview;
		sourcesPre[4] = R.drawable.main_bg6_preview;
		sourcesPre[5] = R.drawable.main_bg7_preview;
		
		String strSetting = FileManager.getSetting("setting");
		if (!strSetting.equals("")) {
			setting = new Gson().fromJson(strSetting, Setting.class);
		} else {
			setting = new Setting();
		}
		if (mark == 0) {
			ivTarget.setImageResource(setting.getSkin());
//		} else {
//			ivTarget.setImageResource(setting.getUnlockBG());
		}
	}
	
	private void operate() {
		llBack.setOnClickListener(new BackOnClickListener());
		PreviewOnClickListener previewOnClickListener = new PreviewOnClickListener();
		for (int i = 0; i < sourcesPre.length; i++) {
			View one = View.inflate(this, R.layout.setting_skin_one, null);
			one.setOnClickListener(previewOnClickListener);
			views[i] = one;
			ImageView ivOne = (ImageView) one.findViewById(R.id.iv_setting_preview);
			ivOne.setImageResource(sourcesPre[i]);
			llSources.addView(one);
		}
		ibOk.setOnClickListener(new OkOnClickListener());
		if (mark == 1) {
			tvTitle.setText("");
		}
	}
	
	private class BackOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			finish();
		}
	}
	
	private class PreviewOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			for (int i = 0; i < views.length; i++) {
				if (v == views[i]) {
					ivTarget.setImageResource(sources[i]);
					if (mark == 0) {
						setting.setSkin(sources[i]);
//					} else {
//						setting.setUnlockBG(sources[i]);
					}
					break;
				}
			}
		}
	}
	
	private class OkOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			String strSetting = new Gson().toJson(setting);
			FileManager.saveSetting(strSetting, "setting");
			finish();
		}
	}
}
