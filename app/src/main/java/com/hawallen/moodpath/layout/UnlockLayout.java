package com.hawallen.moodpath.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hawallen.moodpath.R;
import com.hawallen.moodpath.utils.FileService;

import java.util.Calendar;


public class UnlockLayout extends RelativeLayout {

	private ImageView ivUnlocker = null;
	private ImageView[] ivEmotes = new ImageView[5];
	private Bitmap dragBitmap = null; // 初始化图片拖拽时的Bitmap对象
	private Context mContext = null;
	private Handler unLockHandler = null; // 与主Activity通信的Handler对象

	private int x2;
	private int y2;
	private double width;
	private double ivUnlockerWidth, ivUnlockerHeight;
	private double[] ivEmotesWidth = new double[5];
	private double[] ivEmotesHeight = new double[5];
	private LayoutParams[] params = new LayoutParams[5];

	private double marginLeftAndRight = 0;
	private double marginBottom = 20;
	private double[] betas = new double[5];
	private double r;

	private int mLastMoveX = 1000; // 当前bitmap应该绘制的地方 ， 初始值为足够大，可以认为看不见
	private int mLastMoveY = 1000;
	private boolean isHit;
	private boolean isFinishLoad = false;

	public UnlockLayout(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public UnlockLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		mContext = context;
		init();
	}

	public UnlockLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	// 初始化图片拖拽时的Bitmap对象
	private void init() {
		dragBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.unlocker2_pd);

		betas[0] = 18 * Math.PI / 180;
		betas[1] = 54 * Math.PI / 180;
		betas[2] = 90 * Math.PI / 180;
		betas[3] = 54 * Math.PI / 180;
		betas[4] = 18 * Math.PI / 180;
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		ivUnlocker = (ImageView) findViewById(R.id.iv_unlock_locked);
		ivEmotes[0] = (ImageView) findViewById(R.id.iv_unlock1);
		ivEmotes[1] = (ImageView) findViewById(R.id.iv_unlock2);
		ivEmotes[2] = (ImageView) findViewById(R.id.iv_unlock3);
		ivEmotes[3] = (ImageView) findViewById(R.id.iv_unlock4);
		ivEmotes[4] = (ImageView) findViewById(R.id.iv_unlock5);

		super.onFinishInflate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mLastMoveX = (int) event.getX();
		mLastMoveY = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return handleActionDownEvenet(event);
		case MotionEvent.ACTION_MOVE:
			return handleActionMoveEvent(event);
		case MotionEvent.ACTION_UP:
			return handleActionUpEvent(event);
		}
		return super.onTouchEvent(event);
	}

	// 绘制拖动时的图片
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isFinishLoad) {
			width = getWidth();
			ivUnlockerWidth = ivUnlocker.getWidth();
			ivUnlockerHeight = ivUnlocker.getHeight();

			for (int i = 0; i < ivEmotesWidth.length; i++) {
				ivEmotesWidth[i] = ivEmotes[i].getWidth();
				ivEmotesHeight[i] = ivEmotes[i].getHeight();
				params[i] = (LayoutParams) ivEmotes[i].getLayoutParams();
			}

			r = (width - ivEmotesWidth[0]) / 2 - marginLeftAndRight;

			LayoutParams unlockerParams = (LayoutParams) ivUnlocker.getLayoutParams();
			unlockerParams.setMargins(0, 0, 0, (int) marginBottom);
			ivUnlocker.setLayoutParams(unlockerParams);

			params[0].setMargins(0, 0, (int) (r * Math.cos(betas[0]) - (ivUnlockerWidth + ivEmotesWidth[0]) / 2), (int) (marginBottom + r * Math.sin(betas[0]) - ivEmotesHeight[0] / 2));
			ivEmotes[0].setLayoutParams(params[0]);

			params[1].setMargins(0, 0, (int) (r * Math.cos(betas[1]) - (ivUnlockerWidth + ivEmotesWidth[1]) / 2), (int) (r * Math.sin(betas[1]) - ivEmotesHeight[1] / 2 - ivUnlockerHeight));
			ivEmotes[1].setLayoutParams(params[1]);

			params[2].setMargins(0, 0, 0, (int) (r - ivUnlockerHeight - ivEmotesHeight[2] / 2));
			ivEmotes[2].setLayoutParams(params[2]);

			params[3].setMargins((int) (r * Math.cos(betas[3]) - (ivUnlockerWidth + ivEmotesWidth[3]) / 2), 0, 0, (int) (r * Math.sin(betas[3]) - ivEmotesHeight[3] / 2 - ivUnlockerHeight));
			ivEmotes[3].setLayoutParams(params[3]);

			params[4].setMargins((int) (r * Math.cos(betas[4]) - (ivUnlockerWidth + ivEmotesWidth[4]) / 2), 0, 0, (int) (marginBottom + r * Math.sin(betas[4]) - ivEmotesHeight[4] / 2));
			ivEmotes[4].setLayoutParams(params[4]);
			x2 = ivUnlocker.getLeft() + ivUnlocker.getWidth() / 2;
			y2 = ivUnlocker.getTop() + ivUnlocker.getHeight() / 2;
			isFinishLoad = true;
		}

		if (isHit) {
			// 以合适的坐标值绘制该图片
			int drawXCor = mLastMoveX - dragBitmap.getWidth() / 2;
			int drawYCor = mLastMoveY - dragBitmap.getHeight() / 2;
			canvas.drawBitmap(dragBitmap, drawXCor, drawYCor, null);
		}
	}

	// 手势落下是，是否点中了图片，即是否需要开始移动
	private boolean handleActionDownEvenet(MotionEvent event) {
		Rect rect = new Rect();

		ivUnlocker.getHitRect(rect);
		isHit = rect.contains(mLastMoveX, mLastMoveY);
		if (isHit) {// 开始拖拽 ，隐藏该图片
			ivUnlocker.setVisibility(View.INVISIBLE);
			invalidate();
		}
		return isHit;
	}

	private boolean handleActionMoveEvent(MotionEvent event) {
		getHitNum();
		invalidate();
		return false;
	}

	// 判断松开手指时，是否达到末尾即可以开锁了 , 是，则开锁，否则，通过一定的算法使其回退。
	private boolean handleActionUpEvent(MotionEvent event) {
		int hitNum = getHitNum();
		if (hitNum > -1) {
			dragBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.unlocker2_pd);
			save(hitNum + 1);
			Toast.makeText(mContext, "解锁成功", Toast.LENGTH_LONG).show();
			virbate(); // 震动一下
			Message message = new Message();
			message.obj = "FinishLockActivity";
			message.arg1 = hitNum + 1;
			unLockHandler.sendMessage(message);
			return true;
		} else {
			dragBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.unlocker2_pd);
			resetViewState();
			return false;
		}
	}

	// 点到直线的最短距离的判断 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )
	private double pointToLine(int x1, int y1, int x2, int y2, int x0, int y0) {
		double space = 0;
		double a, b, c;
		a = lineSpace(x1, y1, x2, y2);// 线段的长度
		b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
		c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离
		if (c <= 0.000001 || b <= 0.000001) {
			space = 0;
			return space;
		}
		if (a <= 0.000001) {
			space = b;
			return space;
		}
		if (c * c >= a * a + b * b) {
			space = b;
			return space;
		}
		if (b * b >= a * a + c * c) {
			space = c;
			return space;
		}
		double p = (a + b + c) / 2;// 半周长
		double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
		space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
		return space;
	}

	// 计算两点之间的距离
	private double lineSpace(int x1, int y1, int x2, int y2) {
		double lineLength = 0;
		lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		return lineLength;
	}

	private void save(int uMood) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		FileService.saveMoodData(year + "-" + month + "-" + day, hour + ":" + minute + ":" + second, uMood);
	}

	private int getHitNum() {
		int hitNum = -1;
		for (int i = 0; i < 5; i++) {
			int x0 = ivEmotes[i].getLeft() + ivEmotes[i].getWidth() / 2;
			int y0 = ivEmotes[i].getTop() + ivEmotes[i].getHeight() / 2;
			if (r * Math.sin(betas[0]) > pointToLine(mLastMoveX, mLastMoveY, x2, y2, x0, y0)) {
				hitNum = i;
			}
		}
		setHitHighlight(hitNum);
		return hitNum;
	}

	private void setHitHighlight(int i) {
		ivEmotes[0].setBackgroundResource(R.drawable.unlock_angry2);
		ivEmotes[1].setBackgroundResource(R.drawable.unlock_sad2);
		ivEmotes[2].setBackgroundResource(R.drawable.unlock_uncertain2);
		ivEmotes[3].setBackgroundResource(R.drawable.unlock_smile2);
		ivEmotes[4].setBackgroundResource(R.drawable.unlock_happy2);
		if (i > -1) {
			switch (i) {
			case 0:
				ivEmotes[0].setBackgroundResource(R.drawable.unlock_angry2_checked);
				break;
			case 1:
				ivEmotes[1].setBackgroundResource(R.drawable.unlock_sad2_checked);
				break;
			case 2:
				ivEmotes[2].setBackgroundResource(R.drawable.unlock_uncertain2_checked);
				break;
			case 3:
				ivEmotes[3].setBackgroundResource(R.drawable.unlock_smile2_checked);
				break;
			case 4:
				ivEmotes[4].setBackgroundResource(R.drawable.unlock_happy2_checked);
				break;
			default:
				break;
			}
		}
	}

	// 重置初始的状态，显示tv_slider_icon图像，使bitmap不可见
	private void resetViewState() {
		mLastMoveX = 1000;
		mLastMoveY = 1000;
		ivUnlocker.setVisibility(View.VISIBLE);
		invalidate(); // 重绘最后一次
	}

	// 震动一下下咯
	private void virbate() {
		Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}

	public void setUnlockHandler(Handler handler) {
		unLockHandler = handler;// activity所在的Handler对象
	}
}
