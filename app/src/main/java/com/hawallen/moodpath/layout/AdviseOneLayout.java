package com.hawallen.moodpath.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hawallen.moodpath.R;
import com.hawallen.moodpath.bean.Status;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;


public class AdviseOneLayout extends RelativeLayout {

	private Status status;
	private View adviseOne;
	private ImageView ivUserPic;
	private ImageView ivGender;
	private TextView tvUserName;
	private TextView tvDate;
	private Button btnFollow;
	private TextView tvContent;
	private LinearLayout llComment;
	private LinearLayout llShare;
	private TextView tvCommentNum;
	private TextView tvShareNum;
	
	public AdviseOneLayout(Context context, Status status) {
		super(context);
		this.status = status;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(layoutParams);
		adviseOne = View.inflate(getContext(), R.layout.advise_one, null);
		adviseOne.setLayoutParams(layoutParams);
		addView(adviseOne);

		init();

		operate();
	}

	private void init() {
		ivUserPic = (ImageView) adviseOne.findViewById(R.id.iv_user_pic);
		ivGender = (ImageView) adviseOne.findViewById(R.id.iv_gender);
		tvUserName = (TextView) adviseOne.findViewById(R.id.tv_user_name);
		tvDate = (TextView) adviseOne.findViewById(R.id.tv_date);
		btnFollow = (Button) adviseOne.findViewById(R.id.btn_follow_me);
		tvContent = (TextView) adviseOne.findViewById(R.id.tv_status);
		llComment = (LinearLayout) adviseOne.findViewById(R.id.ll_comment);
		llShare = (LinearLayout) adviseOne.findViewById(R.id.ll_share);
		tvCommentNum = (TextView) adviseOne.findViewById(R.id.tv_comment_times);
		tvShareNum = (TextView) adviseOne.findViewById(R.id.tv_share_times);
		
	}

	private void operate() {
		if (status.user.gender.equals("m")) {
			ivGender.setBackgroundResource(R.drawable.advise_gender_male);
			tvContent.setTextColor(getResources().getColor(R.color.male));
		} else if (status.user.gender.equals("f")) {
			ivGender.setBackgroundResource(R.drawable.advise_gender_female);
			tvContent.setTextColor(getResources().getColor(R.color.female));
		} else {
			ivGender.setVisibility(View.GONE);
			tvContent.setTextColor(getResources().getColor(R.color.black));
		}
		
		tvUserName.setText(status.user.screen_name);
		tvContent.setText(status.text);
		long millisecond = Date.parse(status.created_at);
		Date date = new Date(millisecond);
		tvDate.setText(date.toLocaleString());
		tvCommentNum.setText(status.comments_count);
		if (status.user.profile_image_url != "") {
			setImageUrl(status.user.profile_image_url);
		}
		
	}

	private void setImageUrl(String url) {
		DownloadTask task = new DownloadTask();
		task.execute(url);
	}

	private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
			try {
				URLConnection connection = (new URL(url)).openConnection();
				InputStream is = connection.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append(current);
				}
				byte[] imageData = baf.toByteArray();
				return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
			} catch (Exception e) {
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			Drawable mImage = new BitmapDrawable(result);
			if (mImage != null) {
				ivUserPic.setBackgroundDrawable(mImage);
			}
		}

	}
}
