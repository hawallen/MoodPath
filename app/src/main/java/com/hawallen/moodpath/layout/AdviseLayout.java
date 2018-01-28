package com.hawallen.moodpath.layout;

import android.content.res.AssetManager;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hawallen.moodpath.MainActivity;
import com.hawallen.moodpath.R;
import com.hawallen.moodpath.bean.Topic;
import com.hawallen.moodpath.bean.TopicList;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.SearchAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class AdviseLayout extends LinearLayout {

	private MainActivity mContext;
	private View advise;
	private SlidingDrawer slidingDrawer;
	private ImageView btnSlide;
	private ProgressBar pb;
	private LinearLayout llStatus;
	private Topic topic;
	private TopicList topicList;

	public AdviseLayout(MainActivity context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(layoutParams);
		advise = View.inflate(context, R.layout.advise, null);
		advise.setLayoutParams(layoutParams);
		addView(advise);

		init();
		operate();
	}

	private void init() {
		slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
		llStatus = (LinearLayout) findViewById(R.id.ll_status);
		btnSlide = (ImageView) findViewById(R.id.iv_adivse_slide);
		pb = (ProgressBar) findViewById(R.id.pb_adivse);

		try {
			AssetManager assetManager = mContext.getAssets();
			InputStreamReader inputReader = new InputStreamReader(assetManager.open("article/" + "topic_list.txt"), "gb2312");
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String result = "";
			while ((line = bufReader.readLine()) != null)
				result += line;
			Gson gson = new Gson();
			topicList = gson.fromJson(result, TopicList.class);
			ListView listView = (ListView) findViewById(R.id.content);
			listView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_expandable_list_item_1, topicList.topicNames));
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					slidingDrawer.animateClose();
					updateAdivse(topicList.topicNames[arg2]);
				}
			});
			updateAdivse(topicList.topicNames[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// slidingDrawer.seto
	}

	public void updateAdivse(String topicName) {
		Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(mContext);
		if (oauth2AccessToken.isSessionValid()) {
			llStatus.removeAllViews();
			pb.setVisibility(View.VISIBLE);
			SearchAPI searchAPI = new SearchAPI(oauth2AccessToken);
			searchAPI.topics(topicName, 20, new RequestListener() {
				@Override
				public void onIOException(IOException e) {

				}

				@Override
				public void onError(WeiboException e) {

				}

				@Override
				public void onComplete(String response) {
					System.out.println(response);
					try {
						byte[] bytes = response.getBytes("UTF-8");
						response = new String(bytes, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

					Gson gson = new Gson();
					topic = gson.fromJson(response, Topic.class);
					handler.sendEmptyMessage(1);

				}
			});
		} else {
			Toast.makeText(mContext, mContext.getString(R.string.please_login), Toast.LENGTH_SHORT).show();
			if (mContext.rlSetting != null) {
				mContext.rlSetting.performClick();
			}
		}
	}

	private void operate() {
		btnSlide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (slidingDrawer.isOpened()) {
					slidingDrawer.animateClose();
				} else {
					slidingDrawer.animateOpen();
				}
			}
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pb.setVisibility(View.GONE);
			for (int i = 0; i < topic.statuses.length; i++) {
				AdviseOneLayout adviseOne = new AdviseOneLayout(mContext, topic.statuses[i]);
				llStatus.addView(adviseOne);
			}
		};
	};

	private class MyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (slidingDrawer.isOpened()) {
				slidingDrawer.close();
			} else {
				slidingDrawer.open();
			}
		}
	}

}
