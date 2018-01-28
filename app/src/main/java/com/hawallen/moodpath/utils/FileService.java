package com.hawallen.moodpath.utils;

import com.google.gson.Gson;
import com.hawallen.moodpath.bean.MoodData;

import java.util.HashMap;


public class FileService {
	
	public static void saveMoodData(String date, String time, int mood) {
		String folderName = date.substring(0, date.lastIndexOf("-"));
		String fileName = date.substring(date.lastIndexOf("-") + 1, date.length());
		String moodDataString = FileManager.getData("data/"+folderName, fileName);
		MoodData moodData;
		Gson gson = new Gson();
		if (moodDataString.equals("")) {
			moodData = new MoodData();
			HashMap<String, Integer> moodMap = new HashMap<String, Integer>();
			moodMap.put(time, mood);
			moodData.setuId("");
			moodData.setmDate(date);
			moodData.setuMood(moodMap);
		} else {
			moodData = gson.fromJson(moodDataString, MoodData.class);
			moodData.getuMood().put(time, mood);
		}
		moodDataString = gson.toJson(moodData);
		FileManager.saveData(moodDataString, "data/"+folderName, fileName);
	}
	
	public static MoodData getMoodData(String date) {
		String folderName = date.substring(0, date.lastIndexOf("-"));
		String fileName = date.substring(date.lastIndexOf("-") + 1, date.length());
		String moodDataString = FileManager.getData("data/"+folderName, fileName);
		if (!moodDataString.equals("")) {
			Gson gson = new Gson();
			return gson.fromJson(moodDataString, MoodData.class);
		}
		return null;
	}
	
	public void upload () {
		
	}
	
}
