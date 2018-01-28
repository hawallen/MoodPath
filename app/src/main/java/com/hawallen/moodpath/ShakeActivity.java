package com.hawallen.moodpath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.TextView;

import com.hawallen.moodpath.utils.ShareService;


public class ShakeActivity extends Activity implements Callback, Runnable, SensorEventListener {

	private int bgResource;
	private int moodResource;
	private String moodString;
//	private LinearLayout llShake;
	private TextView tvShake;
	private SurfaceView mSurfaceView;
	public static final int TIME_IN_FRAME = 30;
	public static final int TIME_IN_TOTLE = 150;
	public int i = 0;
	public int hitTime = 0;
	private Paint mPaint = null;
	private SurfaceHolder mSurfaceHolder = null;
	boolean mRunning = false;
	private Canvas mCanvas = null;
	boolean mIsRunning = false;
	private SensorManager mSensorMgr = null;
	private static final int SENSOR_SHAKE = 10;
	private Sensor mSensor = null;
	int mScreenWidth = 0;
	int mScreenHeight = 0;
	private int mScreenBallWidth = 0;
	private int mScreenBallHeight = 0;
	private Bitmap mbitmapBall;
	private float mPosX = 0;
	private float mPosY = 0;
	private float mGX = 0;
	private float mGY = 0;
	private float mGZ = 0;

	MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.today_shake);
		
		Intent intent = getIntent();
		bgResource = intent.getIntExtra("bgResource", getResources().getColor(R.color.light_gray2));
		moodResource = intent.getIntExtra("moodResource", R.drawable.today_angry2);
//		moodString = intent.getStringExtra("moodString");
		moodString = "愤怒";

		mMediaPlayer = MediaPlayer.create(ShakeActivity.this, R.raw.di);
		
//		llShake = (LinearLayout) findViewById(R.id.ll_today_shake);
		tvShake = (TextView) findViewById(R.id.tv_today_shake);
		tvShake.setBackgroundColor(bgResource);
//		llShake.setBackgroundResource(bgResource);
		mSurfaceView = (SurfaceView) findViewById(R.id.sv_today_shake);
//		mSurfaceView.setBackgroundResource(bgResource);
		

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mCanvas = new Canvas();
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		mbitmapBall = BitmapFactory.decodeResource(getResources(), moodResource);
//		mbitmapBg = BitmapFactory.decodeResource(getResources(), bgResource);
		mSensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorMgr.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

	}

	private void Draw() {
		/** 绘制游戏背景 **/
//		mCanvas.drawBitmap(mbitmapBg, 0, 0, mPaint);
		mCanvas.drawColor(bgResource);
//		mCanvas.drawColor(Color.、TRANSPARENT);
		/** 绘制小球 **/
		mCanvas.drawBitmap(mbitmapBall, mPosX, mPosY, mPaint);
		/** X轴 Y轴 Z轴的重力值 **/
//		mPaint.setTextSize(30);
//		mCanvas.drawText("不要摇掉你的手机哦！", 100, 40, mPaint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mIsRunning = true;
		new Thread(this).start();
		mScreenWidth = mSurfaceView.getWidth();
		mScreenHeight = mSurfaceView.getHeight();
		mScreenBallWidth = mScreenWidth - mbitmapBall.getWidth();
		mScreenBallHeight = mScreenHeight - mbitmapBall.getHeight();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mIsRunning = false;
	}

	@Override
	public void run() {
		while (mIsRunning && i < TIME_IN_TOTLE) {
			long startTime = System.currentTimeMillis();
			synchronized (mSurfaceHolder) {
				mCanvas = mSurfaceHolder.lockCanvas();
				Draw();
				i++;
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			}

			long endTime = System.currentTimeMillis();
			int diffTime = (int) (endTime - startTime);
			while (diffTime <= TIME_IN_FRAME) {
				diffTime = (int) (System.currentTimeMillis() - startTime);
				Thread.yield();
			}
		}
		mSensorMgr.unregisterListener(this);
		handler.sendEmptyMessage(SENSOR_SHAKE);

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SENSOR_SHAKE:
				finish();
				ShareService weiboSharer = new ShareService(ShakeActivity.this);
				String shareString = "#" + getResources().getString(R.string.app_name) + "#" + "大爷我很" + moodString + "，指数：" + hitTime;
				weiboSharer.share2sina(shareString, "");
				break;
			}
		}

	};

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		mGX = event.values[SensorManager.DATA_X];
		mGY = event.values[SensorManager.DATA_Y];
		mGZ = event.values[SensorManager.DATA_Z];
		// 这里乘以4是为了让小球移动的更快
		mPosX -= mGX * 4;
		mPosY += mGY * 4;

		if (mPosX < 0 || mPosX > mScreenBallWidth || mPosY < 0 || mPosY > mScreenBallHeight) {
			// 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
			int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
			if (Math.abs(mGX) > medumValue || Math.abs(mGY) > medumValue || Math.abs(mGZ) > medumValue) {
				if (mMediaPlayer != null) {
					mMediaPlayer.start();
				}
				hitTime++;
			}
		}

		// 检测小球是否超出边界
		if (mPosX < 0) {
			mPosX = 0;
		} else if (mPosX > mScreenBallWidth) {
			mPosX = mScreenBallWidth;
		}
		if (mPosY < 0) {
			mPosY = 0;
		} else if (mPosY > mScreenBallHeight) {
			mPosY = mScreenBallHeight;
		}
	}
}
