package com.hawallen.moodpath.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class MoodData implements Serializable{
	
	private String uId = "";
	private String mDate = "";
	private Map<String,Integer> uMood = new HashMap<String, Integer>();
	private boolean isUploaded = false;
	
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getmDate() {
		return mDate;
	}
	public void setmDate(String mDate) {
		this.mDate = mDate;
	}
	public Map<String,Integer> getuMood() {
		return uMood;
	}
	public void setuMood(Map<String,Integer> uMood) {
		this.uMood = uMood;
	}
	public boolean isUploaded() {
		return isUploaded;
	}
	public void setUploaded(boolean isUploaded) {
		this.isUploaded = isUploaded;
	}
	
}
