/** 
 * Copyright © 2015网域互联. All rights reserved.
 *
 * @Package: cn.ymex.cute.toolbox
 * @Description: TODO
 * @author: ymex email:ymex@foxmail.com
 * @date: 2015-4-24 下午8:47:46
 * @version: V1.0
 */
package cn.ymex.cute.kits;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @ClassName: Formater
 * @Description: TODO 提供有限的 时间/单体格式相关转换
 * @author: ymex
 * @date: 2015-4-24 下午8:47:46
 */
@SuppressLint("SimpleDateFormat")
public class Formater {
	private static String DEFAULT_TIME_ZONE = "GMT+08";
	/**
	 * 判断用户的设备时区是否为某一时区
	 * 
	 * @return
	 */
	public static boolean isInEasternEightZones() {
		boolean defaultVaule = true;
		if (TimeZone.getDefault() == TimeZone.getTimeZone(DEFAULT_TIME_ZONE))
			defaultVaule = true;
		else
			defaultVaule = false;
		return defaultVaule;
	}

	/**
	 * 根据不同时区，转换时间 
	 * 
	 * @return
	 */
	public static Date transformTime(Date date, TimeZone oldZone,
			TimeZone newZone) {
		Date finalDate = null;
		if (date != null) {
			int timeOffset = oldZone.getOffset(date.getTime())
					- newZone.getOffset(date.getTime());
			finalDate = new Date(date.getTime() - timeOffset);
		}
		return finalDate;

	}
	
	/**
	 * 日期格式：yyyy-MM-dd HH:mm:ss
	 */
	public final static ThreadLocal<SimpleDateFormat> dateFormaterWithTime = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	/**
	 * 日期格式：yyyy-MM-dd
	 */
	@SuppressLint("SimpleDateFormat")
	public  final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	
	
	
	/**
	 * 将日期转换为 格式 ：yyyy-MM-dd HH:mm:ss 的字串
	 * @param date
	 * @return
	 */
	public static String date2String(Date date){
		return dateFormaterWithTime.get().format(date);
	}
	
	/**
	 * 将日期时间转换为 格式 ：yyyy-MM-dd HH:mm:ss 的字串
	 * @return
	 */
	public static String date2String(long milliseconds){
		return dateFormaterWithTime.get().format(new Date(milliseconds));
	}

	/**
	 * 将字符串转为日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormaterWithTime.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String semanticTime(String sdate) {
		Date time = null;
		if (isInEasternEightZones()) {
			time = toDate(sdate);
		} else {
			time = transformTime(toDate(sdate),
					TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());
		}
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater.get().format(cal.getTime());
		String paramDate = dateFormater.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater.get().format(today);
			String timeDate = dateFormater.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 返回long类型的今天的日期
	 * 
	 * @return
	 */
	public static long getToday() {
		Calendar cal = Calendar.getInstance();
		String curDate = dateFormater.get().format(cal.getTime());
		curDate = curDate.replace("-", "");
		return Long.parseLong(curDate);
	}
}
