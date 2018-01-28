package com.hawallen.moodpath;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hawallen.moodpath.bean.Setting;
import com.hawallen.moodpath.utils.FileManager;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;

import java.io.File;
import java.io.IOException;

public class MyShareActivity extends Activity implements RequestListener {

	private Button btnBack, btnSend;
	private TextView mTextNum;
	private EditText mEdit;
	private LinearLayout llTextDel;
	private ImageView ivShare;
	private ImageView ivDel;
	private FrameLayout mPiclayout;

	private String mPicPath = "";
	private String mContent = "";
	private final String mAdress = " 下载地址：http://955.cc/3Pu";
	private Oauth2AccessToken oauth2AccessToken;

	public static final String EXTRA_WEIBO_CONTENT = "com.weibo.android.content";
	public static final String EXTRA_PIC_URI = "com.weibo.android.pic.uri";
	public static final String EXTRA_ACCESS_TOKEN = "com.weibo.android.accesstoken";
	public static final String EXTRA_TOKEN_SECRET = "com.weibo.android.token.secret";

	public static final int WEIBO_MAX_LENGTH = 140;

	private MyHandler myHandler = new MyHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);

		init();
		operate();
	}

	@Override
	public void onBackPressed() {
		btnBack.performClick();
	}

	private void init() {
		btnBack = (Button) findViewById(R.id.btn_share_back);
		btnSend = (Button) findViewById(R.id.btn_share_send);
		mEdit = (EditText) findViewById(R.id.et_share_text);
		mTextNum = (TextView) findViewById(R.id.tv_share_text_limit);
		llTextDel = (LinearLayout) findViewById(R.id.ll_share_text_del);
		ivShare = (ImageView) findViewById(R.id.iv_share_pic);
		ivDel = (ImageView) findViewById(R.id.iv_share_del);
		mPiclayout = (FrameLayout) findViewById(R.id.fl_share_pic);

		Intent intent = getIntent();
		mContent = intent.getStringExtra("shareString");
		mPicPath = intent.getStringExtra("picPath");
	}

	private void operate() {
		btnBack.setOnClickListener(new BackOnClickListener());
		btnSend.setOnClickListener(new SendOnClickListener());
		mEdit.addTextChangedListener(new MyTextWatcher());
		mEdit.setText(mContent);

		if (TextUtils.isEmpty(this.mPicPath)) {
			mPiclayout.setVisibility(View.GONE);
		} else {
			mPiclayout.setVisibility(View.VISIBLE);
			File file = new File(mPicPath);
			if (file.exists()) {
				Bitmap pic = BitmapFactory.decodeFile(this.mPicPath);
				ivShare.setImageBitmap(pic);
			} else {
				mPiclayout.setVisibility(View.GONE);
			}
		}

		llTextDel.setOnClickListener(new OnTextDelLisetener());
		ivDel.setOnClickListener(new OnPicDelLisetener());

		oauth2AccessToken = AccessTokenKeeper.readAccessToken(this);
		if (!oauth2AccessToken.isSessionValid()) {
			Toast.makeText(this, "请先绑定微博", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	private class MyTextWatcher implements TextWatcher {
		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String mText = mEdit.getText().toString() + mAdress;
			int len = mText.length();
			if (len <= WEIBO_MAX_LENGTH) {
				len = WEIBO_MAX_LENGTH - len;
				mTextNum.setTextColor(getResources().getColor(R.color.text_num_gray));
				if (!btnSend.isEnabled())
					btnSend.setEnabled(true);
			} else {
				len = len - WEIBO_MAX_LENGTH;
				mTextNum.setTextColor(Color.RED);
				if (btnSend.isEnabled())
					btnSend.setEnabled(false);
			}
			mTextNum.setText(String.valueOf(len));
		}
	}

	private class BackOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dealPic();
			finish();
		}
	}

	private class SendOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			StatusesAPI api = new StatusesAPI(oauth2AccessToken);
				mContent = mEdit.getText().toString();
				if (TextUtils.isEmpty(mContent)) {
					Toast.makeText(MyShareActivity.this, "Text Empty", Toast.LENGTH_LONG).show();
					return;
				}
				if (!TextUtils.isEmpty(mPicPath)) {
					api.upload(mContent, mPicPath, "", "", MyShareActivity.this);
					finish();
				} else {
					// Just update a text Weibo
					api.update(mContent, "", "", MyShareActivity.this);
				}

		}

	}

	private class OnTextDelLisetener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String mText = mEdit.getText().toString();
			int len = mText.length();
			if (len != 0) {
				Dialog dialog = new AlertDialog.Builder(MyShareActivity.this).setTitle(R.string.attention).setMessage(R.string.delete_all).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mEdit.setText("");
					}
				}).setNegativeButton(R.string.cancel, null).create();
				dialog.show();
			}
		}
	}

	private class OnPicDelLisetener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Dialog dialog = new AlertDialog.Builder(MyShareActivity.this).setTitle(R.string.attention).setMessage(R.string.del_pic).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mPiclayout.setVisibility(View.GONE);
				}
			}).setNegativeButton(R.string.cancel, null).create();
			dialog.show();
		}
	}

	@Override
	public void onComplete(String response) {
		myHandler.sendMessage(new Message());
		System.out.println("Request:onComplete");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MyShareActivity.this, R.string.send_sucess, Toast.LENGTH_LONG).show();
				dealPic();
			}
		});
		finish();
	}

	private void dealPic() {
		String strSetting = FileManager.getSetting("setting");
		Setting setting;
		if (!strSetting.equals("")) {
			setting = new Gson().fromJson(strSetting, Setting.class);
		} else {
			setting = new Setting();
		}
		if (!setting.isSavePic()) {
			File file = new File(mPicPath);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	@Override
	public void onIOException(IOException e) {
		myHandler.sendMessage(new Message());
		System.out.println("Request:onIOException");
	}

	@Override
	public void onError(WeiboException e) {
		myHandler.sendMessage(new Message());
		System.out.println("Request:onError");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MyShareActivity.this, String.format(MyShareActivity.this.getString(R.string.send_failed)), Toast.LENGTH_LONG).show();
			}
		});
	}

	private class MyHandler extends Handler {
		public void handleMessage(Message msg) {
		}
	}
}