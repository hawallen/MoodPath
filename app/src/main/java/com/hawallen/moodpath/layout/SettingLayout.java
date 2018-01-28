package com.hawallen.moodpath.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hawallen.moodpath.MainActivity;
import com.hawallen.moodpath.R;
import com.hawallen.moodpath.SettingSkinActivity;
import com.hawallen.moodpath.UnlockService;
import com.hawallen.moodpath.bean.Setting;
import com.hawallen.moodpath.utils.FileManager;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.xp.common.ExchangeConstants;
import com.umeng.xp.controller.ExchangeDataService;
import com.umeng.xp.view.ExchangeViewManager;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.util.Utility;

public class SettingLayout extends LinearLayout {

	private MainActivity mContext;
	private View vSetting;
	private ImageView ivMessage, ivSavePic, ivService;
	private boolean messageIsOn = true;
	private boolean savePicIsOn = true;
	private boolean serviceIsOn = true;
	private LinearLayout llFB, llUpdate, llChangeSkin, llBindSina;
	private TextView tvUpdate, tvBind;

	private Setting setting;
	

	public SettingLayout(MainActivity context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(layoutParams);
		vSetting = View.inflate(context, R.layout.setting, null);
		vSetting.setLayoutParams(layoutParams);
		addView(vSetting);

		init();
		setView();
	}

	private void init() {
		ivMessage = (ImageView) vSetting.findViewById(R.id.iv_setting_push_message);
		ivSavePic = (ImageView) vSetting.findViewById(R.id.iv_setting_save_pic);
		ivService = (ImageView) vSetting.findViewById(R.id.iv_setting_on_down_service);
		llFB = (LinearLayout) vSetting.findViewById(R.id.ll_setting_fb);
		llUpdate = (LinearLayout) vSetting.findViewById(R.id.ll_setting_update);
		llChangeSkin = (LinearLayout) vSetting.findViewById(R.id.ll_setting_change_skin);
		llBindSina = (LinearLayout) vSetting.findViewById(R.id.ll_setting_bind);
		// llChangeUnlock = (LinearLayout)
		// vSetting.findViewById(R.id.ll_setting_change_unlock_bg);
		tvUpdate = (TextView) vSetting.findViewById(R.id.tv_setting_update);
		tvBind = (TextView) vSetting.findViewById(R.id.tv_setting_bind);

		ViewGroup parent = (ViewGroup) vSetting.findViewById(R.id.ll_setting_ad); 
		/* 应用联盟集成方式， 请在AndroidManifest.xml中添加 UMENG_APPKEY */ 
		ExchangeDataService service = new ExchangeDataService(); 
		ExchangeViewManager viewMgr = new ExchangeViewManager(mContext, service); 
		viewMgr.addView(parent, ExchangeConstants.type_standalone_handler); 
	}

	private void setView() {
		setting = getSetting();
		if (setting.isMessagePush()) {
			ivMessage.setBackgroundResource(R.drawable.setting_on2);
			messageIsOn = true;
		} else {
			ivMessage.setBackgroundResource(R.drawable.setting_off2);
			messageIsOn = false;
		}
		if (setting.isSavePic()) {
			ivSavePic.setBackgroundResource(R.drawable.setting_on2);
			savePicIsOn = true;
		} else {
			ivSavePic.setBackgroundResource(R.drawable.setting_off2);
			savePicIsOn = false;
		}
		if (setting.isOnDownService()) {
			ivService.setBackgroundResource(R.drawable.setting_on2);
			serviceIsOn = true;
			Intent intent = new Intent();
			intent.setClass(mContext, UnlockService.class);
			mContext.startService(intent);
		} else {
			ivService.setBackgroundResource(R.drawable.setting_off2);
			serviceIsOn = false;
		}

		OnSwitchClickListener onSwitchClickListener = new OnSwitchClickListener();
		OnSkinListener onSkinListener = new OnSkinListener();
		OnFBListener onFBListener = new OnFBListener();
		OnUpdateListener onUpdateListener = new OnUpdateListener();
		ivMessage.setOnClickListener(onSwitchClickListener);
		ivSavePic.setOnClickListener(onSwitchClickListener);
		ivService.setOnClickListener(onSwitchClickListener);
		llChangeSkin.setOnClickListener(onSkinListener);
		// llChangeUnlock.setOnClickListener(onSkinListener);
		llFB.setOnClickListener(onFBListener);
		llUpdate.setOnClickListener(onUpdateListener);
		llBindSina.setOnClickListener(new OnBindSinaListener());
	}
	
	public void bindSina() {
		if (llBindSina != null) {
			llBindSina.performClick();
		}
	}

	public void setUpdateMarkVisibility(int visibility) {
		tvUpdate.setVisibility(visibility);
	}

	private Setting getSetting() {
		String strSetting = FileManager.getSetting("setting");
		Setting setting = new Setting();
		if (!strSetting.equals("")) {
			setting = new Gson().fromJson(strSetting, Setting.class);
		}
		return setting;
	}

	private class OnSwitchClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == ivMessage) {
				if (messageIsOn) {
					setting.setMessagePush(false);
					ivMessage.setBackgroundResource(R.drawable.setting_off2);
					messageIsOn = false;
				} else {
					setting.setMessagePush(true);
					ivMessage.setBackgroundResource(R.drawable.setting_on2);
					messageIsOn = true;
				}
			} else if (v == ivSavePic) {
				if (savePicIsOn) {
					setting.setSavePic(false);
					ivSavePic.setBackgroundResource(R.drawable.setting_off2);
					savePicIsOn = false;
				} else {
					setting.setSavePic(true);
					ivSavePic.setBackgroundResource(R.drawable.setting_on2);
					savePicIsOn = true;
				}
			} else if (v == ivService) {
				Intent intent = new Intent();
				intent.setClass(mContext, UnlockService.class);
				if (serviceIsOn) {
					setting.setOnDownService(false);
					ivService.setBackgroundResource(R.drawable.setting_off2);
					serviceIsOn = false;
//					mContext.stopService(intent);
				} else {
					setting.setOnDownService(true);
					ivService.setBackgroundResource(R.drawable.setting_on2);
					serviceIsOn = true;
					mContext.startService(intent);
				}
			}

			FileManager.saveSetting(new Gson().toJson(setting), "setting");
		}
	}

	private class OnSkinListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			// if (v == llChangeSkin) {
			intent.putExtra("mark", 0);
			// } else {
			// intent.putExtra("mark", 1);
			// }
			intent.setClass(mContext, SettingSkinActivity.class);
			mContext.startActivity(intent);
		}
	}
	
	private class OnBindSinaListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			final Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(getContext());
			Weibo.isWifi=Utility.isWifi(getContext());
			if (oauth2AccessToken.isSessionValid()) {
				Toast.makeText(getContext(), "微博已绑定", Toast.LENGTH_LONG).show();
			} else {
				Weibo weibo = Weibo.getInstance(Weibo.app_key, Weibo.redirecturl);
				weibo.authorize(getContext(), new WeiboAuthListener() {
					@Override
					public void onWeiboException(WeiboException e) {
						
					}
					
					@Override
					public void onError(WeiboDialogError e) {
						
					}
					
					@Override
					public void onComplete(Bundle values) {
						CookieSyncManager.getInstance().sync();
						oauth2AccessToken.setToken(values.getString(Weibo.KEY_TOKEN));
						oauth2AccessToken.setExpiresIn(values.getString(Weibo.KEY_EXPIRES));
						oauth2AccessToken.setRefreshToken(values.getString(Weibo.KEY_REFRESHTOKEN));
						AccessTokenKeeper.keepAccessToken(getContext(), oauth2AccessToken);
					}
					
					@Override
					public void onCancel() {
						
					}
				});
			}
		}
	}

	private class OnFBListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			UMFeedbackService.openUmengFeedbackSDK(mContext);
		}
	}

	private class OnUpdateListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					switch (updateStatus) {
					case 0:
						// has update
						UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
						break;
					case 1:
						// has no update
						Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						// none wifi
						Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();

						break;
					case 3:
						// time out
						Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT).show();
					}
				}

			});
			UmengUpdateAgent.setOnDownloadListener(new UmengDownloadListener() {
				@Override
				public void OnDownloadEnd(int result) {
					Toast.makeText(mContext, "download result : " + result, Toast.LENGTH_SHORT).show();
					setUpdateMarkVisibility(View.GONE);
					mContext.setUpdateMarkVisibility(View.GONE);
				}
			});
			UmengUpdateAgent.update(mContext);
		}
	}

}
