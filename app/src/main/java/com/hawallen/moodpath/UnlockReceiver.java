package com.hawallen.moodpath;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.hawallen.moodpath.bean.Setting;
import com.hawallen.moodpath.utils.FileManager;

public class UnlockReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String strSetting = FileManager.getSetting("setting");
		Setting setting = new Setting();
		if (!strSetting.equals("")) {
			setting = new Gson().fromJson(strSetting, Setting.class);
		}
		if (setting.isOnDownService()) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
			if (tm.getCallState() != TelephonyManager.CALL_STATE_RINGING && tm.getCallState() != TelephonyManager.CALL_STATE_OFFHOOK) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(context, UnlockActivity.class);
				context.startActivity(intent);
			}
		}


	}

}
