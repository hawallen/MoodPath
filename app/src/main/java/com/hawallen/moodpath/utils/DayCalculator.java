package com.hawallen.moodpath.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DayCalculator {

	public static double getDayMoodAvg(Map<String, Integer> uMood) {
		if (!uMood.isEmpty()) {
			Iterator<String> iterator = uMood.keySet().iterator();
			int[] xValues = new int[uMood.size()];
			double[] yValues = new double[uMood.size()];
			int i = 0;
			int hour = -1;
			Map<Integer, List<Double>> hourMood = new HashMap<Integer, List<Double>>();
			List<Double> moodList = new ArrayList<Double>();
			// ��ȡ��ʱ���ļ�¼
			while (iterator.hasNext()) {
				String time = (String) iterator.next();
				String[] HMS = time.split(":");
				xValues[i] = Integer.valueOf(HMS[0]);
				yValues[i] = uMood.get(time);
				if (hour == xValues[i]) {
					moodList.add(yValues[i]);
					hourMood.put(hour, moodList);
				} else {
					moodList = new ArrayList<Double>();
					hour = xValues[i];
					moodList.add(yValues[i]);
					hourMood.put(hour, moodList);
				}
				i++;
			}
			Iterator<Integer> iterator2 = hourMood.keySet().iterator();
			xValues = new int[hourMood.size()];
			yValues = new double[hourMood.size()];
			i = 0;
			// ����ÿ��Сʱ������ƽ��ֵ
			double total = 0;
//			double avg = 0;
			int size = 0;
			while (iterator2.hasNext()) {
				int hour2 = iterator2.next();
				List<Double> moodT = hourMood.get(hour2);
				for (int j = 0; j < moodT.size(); j++) {
					total = total + moodT.get(j);
					size++;
				}
//				avg = total / moodT.size();
//				xValues[i] = hour2;
//				yValues[i] = avg;
//				i++;
			}

//			double avg = 0;
//			for (int j = 0; j < yValues.length; j++) {
//				avg += yValues[j];
//			}
			return total / size;
		} else {
			return -1;
		}
	}
	
	public static List<Integer> getEachMoodUnlockTimes(Map<String, Integer> uMood) {
		List<Integer> moodTimes = new ArrayList<Integer>();
		if (!uMood.isEmpty()) {
			for (int i = 0; i < 5; i++) {
				moodTimes.add(0);
			}
			Iterator<String> iterator = uMood.keySet().iterator();
			while (iterator.hasNext()) {
				String time = (String) iterator.next();
				int mood = uMood.get(time);
				moodTimes.set(mood-1, moodTimes.get(mood-1) + 1);
			}
		}
		return moodTimes;
	}

}
