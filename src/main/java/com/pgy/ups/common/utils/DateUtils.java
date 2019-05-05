package com.pgy.ups.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 * 
 * @author 墨凉
 *
 */
public class DateUtils {

	public static String DATE_FORMAT = "yyyy-MM-dd";

	public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String DATE_FORMAT_CHINESE = "yyyy年M月d日";
	
	private static Logger logger=LoggerFactory.getLogger(DateUtils.class);

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		return new SimpleDateFormat(DateUtils.DATE_FORMAT).format(new Date());
	}

	/**
	 * 获取当前日期时间
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		return new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT).format(new Date());
	}

	/**
	 * 根据指定格式转换日期
	 * 
	 * @param date
	 * @param dateformat
	 * @return
	 */
	public static String dateToString(Date date, String dateformat) {
		return new SimpleDateFormat(dateformat).format(date);
	}

	/**
	 * 日期对象转日期字符串 yyyy-mm-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return new SimpleDateFormat(DATE_FORMAT).format(date);
	}

	/**
	 * 日期对象转日期时间字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String dateTimeToString(Date date) {

		return new SimpleDateFormat(DATE_TIME_FORMAT).format(date);
	}

	/**
	 * 日期加减
	 * 
	 * @param date
	 * @param iday
	 * @return
	 */
	public static Date addDay(Date date, int iday) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(Calendar.DAY_OF_MONTH, iday);
		return cd.getTime();
	}

	/**
	 * 获取昨天日期
	 * 
	 * @return
	 */
	public static Date getYesterday() {
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.add(Calendar.DAY_OF_MONTH, -1);
		return cd.getTime();
	}

	/**
	 * 字符串转date对象,指定解析格式
	 * 
	 * @param datestr
	 * @param dateformat
	 * @return
	 */
	public static Date stringToDate(String datestr, String dateformat) {
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat(dateformat);
		try {
			date = df.parse(datestr);
		} catch (ParseException e) {
			logger.error("日期解析异常：{}",ExceptionUtils.getStackTrace(e));
		}
		return date;
	}

	/**
	 * 字符串转date对象
	 * 
	 * @param datestr
	 * @return
	 */
	public static Date stringToDate(String datestr) {
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			date = df.parse(datestr);
		} catch (ParseException e) {
			logger.error("日期解析异常：{}",ExceptionUtils.getStackTrace(e));
		}
		return date;
	}

	/**
	 * 字符串转date对象
	 * 
	 * @param datestr
	 * @return
	 */
	public static Date stringToDateTime(String datestr) {
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
		try {
			date = df.parse(datestr);
		} catch (ParseException e) {
			logger.error("日期解析异常：{}",ExceptionUtils.getStackTrace(e));
		}
		return date;
	}


	/**
	 * 当前时间format
	 * @param datestr
	 * @return
	 */
	public static String  getCurrentDate(String datestr) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(datestr);
		return now.format(pattern);
	}
}
