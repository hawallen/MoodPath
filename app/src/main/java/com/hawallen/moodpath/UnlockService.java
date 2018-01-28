package com.hawallen.moodpath;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class UnlockService extends Service {

	private UnlockReceiver lockReceiver;

	@Override
	public IBinder onBind(Intent arg0) {
		// System.out.println("Service onBind");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		System.out.println("Service onCreate");

		lockReceiver = new UnlockReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.SCREEN_ON");
		filter.addAction("android.intent.action.BOOT_COMPLETED");
		registerReceiver(lockReceiver, filter);
		

//		/* ȡ�õ绰���� */
//		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		PhoneStateListener listener = new PhoneStateListener() {
//			@Override
//			public void onCallStateChanged(int state, String incomingNumber) {
//				switch (state) {
//				case TelephonyManager.CALL_STATE_IDLE:/* ���κ�״̬ */
//					System.out.println(TelephonyManager.CALL_STATE_IDLE);
//					break;
//				case TelephonyManager.CALL_STATE_OFFHOOK:/* ����绰 */
//					System.out.println(TelephonyManager.CALL_STATE_OFFHOOK);
//					break;
//				case TelephonyManager.CALL_STATE_RINGING:/* �绰���� */
//					System.out.println(TelephonyManager.CALL_STATE_RINGING);
//					break;
//				default:
//					break;
//				}
//			}
//
//		};
//		// ����绰��״̬
//		telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

	}
	
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// System.out.println("Service onStartCommand");

		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		// System.out.println("Service onDestroy");
		unregisterReceiver(lockReceiver);
		super.onDestroy();
	}

}
