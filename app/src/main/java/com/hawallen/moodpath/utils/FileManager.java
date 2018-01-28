package com.hawallen.moodpath.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManager {

	public static String FilePath = Environment.getExternalStorageDirectory().toString() + "/" + "MoodUnlock" + "/"; // ��ȡSD��·��

	public static boolean create(String folderName) {
		File dir = new File(FilePath + folderName);
		try {
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static String[] getFileList(String folderName) {
		String[] list = null;
		if (create(folderName)) {
			File dir = new File(FilePath + folderName);
			list = dir.list();
		}
		return list;
	}
	
	public static void saveData(String data, String folderName, String fileName) {
		if (create(folderName)) {
			File file = new File(FilePath + folderName + "/" + fileName + ".txt");
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
	
	public static String getData(String folderName, String fileName) {
		String data = "";
		if (create(folderName)) {
			File file = new File(FilePath + folderName + "/" + fileName + ".txt");
			String line = "";
			try {
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isb = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isb);
				while ((line = br.readLine()) != null) {
					data = data + line;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public static void savePicture(Bitmap bitmap, String folderName, String fileName) {
		if (create(folderName)) {
			File file = new File(FilePath + folderName + "/" + fileName + ".png");
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public static Bitmap getPicture(String folderName, String fileName) {
		if (create(folderName)) {
			
		}
		return BitmapFactory.decodeFile(FilePath + folderName + "/" + fileName + ".png");
	}
	
	public static void saveSetting(String data, String fileName) {
		saveData(data, "setting", fileName);
	}

	public static String getSetting(String fileName) {
		return getData("setting", fileName);
	}

	public static void saveArticle(String data, String type, String fileName) {
		saveData(data, "article/"+type, fileName);
	}

	public static String getArticle(String type, String fileName) {
		return getData("article/"+type, fileName);
	}

	public static void saveArticleRecord(String data, String fileName) {
		saveData(data, "article", fileName);
	}

	public static String getArticleRecord(String fileName) {
		return getData("article", fileName);
	}
	
	
//	public static String getArticlePath() {
//		if (create("article")) {
//			return FilePath + "article" + "/";
//		}
//		return "";
//	}

//	public static String getTodayArticle(String fileName) {
//		return getData("article/today", fileName);
//	}

	public static void saveAccessToken(String data, String fileName) {
		saveData(data, "", fileName);
	}

	public static String getAccessToken(String fileName) {
		return getData("", fileName);
	}
	
//	public static void saveExpiresIn(String data,String fileName) {
//		saveData(data, "", fileName);
//	}
//
//	public static String getExpiresIn(String fileName) {
//		return getData("", fileName);
//	}

}
