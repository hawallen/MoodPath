package com.hawallen.moodpath.bean;

import com.hawallen.moodpath.R;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Setting implements Serializable {
	
	private boolean messagePush = true;
	private boolean savePic = true;
	private boolean onDownService = true;
	private String sinaName = "";
	private int skin = R.drawable.main_bg2;
	
	public boolean isMessagePush() {
		return messagePush;
	}
	public void setMessagePush(boolean messagePush) {
		this.messagePush = messagePush;
	}
	public boolean isSavePic() {
		return savePic;
	}
	public void setSavePic(boolean savePic) {
		this.savePic = savePic;
	}
	public String getSinaName() {
		return sinaName;
	}
	public void setSinaName(String sinaName) {
		this.sinaName = sinaName;
	}
	public int getSkin() {
		return skin;
	}
	public void setSkin(int skin) {
		this.skin = skin;
	}
	public boolean isOnDownService() {
		return onDownService;
	}
	public void setOnDownService(boolean onDownService) {
		this.onDownService = onDownService;
	}
	
}
