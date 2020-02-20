package com.sssj.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author TSY
 *
 * @Date: 2018-10-11
 *
 *        时间日期工具类
 */
public class DateUtil {
	public static final String DATE_NORMAL_FORMAT = "yyyy-MM-dd";
	public static final String DATETIME_NORMAL_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_COMPACT_FORMAT = "yyyyMMdd";
	public static final String DATETIME_COMPACT_FORMAT = "yyyyMMddHHmmss";
	public static final String YM_NORMAL_FORMAT = "yyyy-MM";
	public static final String YM_COMPACT_FORMAT = "yyyyMM";

	/**
	 * String转Timestamp
	 * 
	 * @param dateStr
	 * @return
	 * @author wul 2016-1-17
	 */
	public static Timestamp stringToTimestamp(String dateStr) {
		try {
			if (dateStr.length() <= 10) {
				dateStr += " 00:00:00";
			}
			return Timestamp.valueOf(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * String转Date
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 * @author wul 2016-1-17
	 */
	public static Date stringToDate(String dateStr, String format) {
		if (dateStr == null || "".equals(dateStr)) {
			return null;
		}
		Date date = null;
		// 注意format的格式要与日期String的格式相匹配
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Date转String
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @author wul 2016-1-17
	 */
	public static String dateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String currentDate = sdf.format(date);
		return currentDate;
	}

	/**
	 * Date转Timestamp
	 * 
	 * @param date
	 * @return
	 * @author wul 2016-1-17
	 */
	public static Timestamp dateToTimestamp(Date date) {
		Timestamp ts = new Timestamp(date.getTime());
		return ts;
	}

	/**
	 * Timestamp转String
	 * 
	 * @param ts
	 * @return
	 * @author wul 2016-1-17
	 */
	public static String timestampToString(Timestamp ts) {
		String tsStr = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_NORMAL_FORMAT);
		try {
			tsStr = sdf.format(ts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	/**
	 * Timestamp转Date
	 * 
	 * @param ts
	 * @return
	 * @author wul 2016-1-17
	 */
	public static Date timestampToDate(Timestamp ts) {
		return ts;
	}

	/**
	 * 获取当前Date型日期
	 * 
	 * @return Date() 当前日期
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 获得当前时间并格式化：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTimeNormal() {

		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_NORMAL_FORMAT);
		String currentDate = sdf.format(new Date());
		return currentDate;
	}

	/**
	 * 获得当前时间并格式化：yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getCurrentTimeCompact() {

		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_COMPACT_FORMAT);
		String currentDate = sdf.format(new Date());
		return currentDate;
	}

	/**
	 * 获得当前时间并格式化：yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentDateNormal() {

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_NORMAL_FORMAT);
		String currentDate = sdf.format(new Date());
		return currentDate;
	}

	/**
	 * 获得当前时间并格式化：yyyyMMdd
	 * 
	 * @return
	 */
	public static String getCurrentDateCompact() {

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_COMPACT_FORMAT);
		String currentDate = sdf.format(new Date());
		return currentDate;
	}

	/**
	 * 将20101202时间格式化为2010-12-02
	 * 
	 * @param DateString 时间格式:yyyyMMdd
	 * @return
	 */
	public static String getDateCompactToNormal(String DateString) {

		StringBuilder sb = new StringBuilder();
		sb.append(DateString.substring(0, 4)).append("-").append(DateString.subSequence(4, 6)).append("-")
				.append(DateString.substring(6, 8));
		return sb.toString();
	}

	/**
	 * 将20101202101423时间格式化为2010-12-02 10:14:23
	 * 
	 * @param DateString 时间格式:yyyyMMddHHmmss
	 * @return
	 */
	public static String getDateTimeCompactToNormal(String DateString) {

		StringBuilder sb = new StringBuilder();
		sb.append(DateString.substring(0, 4)).append("-").append(DateString.subSequence(4, 6)).append("-")
				.append(DateString.substring(6, 8)).append(" ").append(DateString.substring(8, 10)).append(":")
				.append(DateString.substring(10, 12)).append(":").append(DateString.substring(12));
		return sb.toString();
	}

	/**
	 * 把界面输入的时间转为间凑的时间字符串 将2010-12-02 10:14:23时间格式化为20101202101423
	 * 
	 * @param dateNormalStr String
	 * @return String
	 */
	public static String getCompactString(String dateNormalStr) {
		StringBuffer ret = new StringBuffer();
		try {
			ret.append(dateNormalStr.substring(0, 4));
			ret.append(dateNormalStr.substring(5, 7));
			ret.append(dateNormalStr.substring(8, 10));
			ret.append(dateNormalStr.substring(11, 13));
			ret.append(dateNormalStr.substring(14, 16));
			ret.append(dateNormalStr.substring(17, 19));
		} catch (Exception ex) {
			// 如果字串不够长度，则返回前面的部分
		}
		return ret.toString();
	}

	/**
	 * 将20101202(101423)时间格式 获得年份
	 * 
	 * @param DateString 时间格式:yyyyMMdd(HHmmss)
	 * @return
	 */
	public static String getYear(String DateString) {

		return DateString.substring(0, 4);
	}

	/**
	 * 将20101202(101423)时间格式 获得月份
	 * 
	 * @param DateString 时间格式:yyyyMMdd(HHmmss)
	 * @return
	 */
	public static String getMonth(String DateString) {

		return DateString.substring(4, 6);
	}

	/**
	 * 将20101202时间格式 获得日期
	 * 
	 * @param DateString 时间格式:yyyyMMdd
	 * @return
	 */
	public static String getDayNoTime(String DateString) {
		return DateString.substring(6);
	}

	/**
	 * 获取当前日期之前的日期，按天数向前推
	 * 
	 * @param numVal
	 * @param dateFormat
	 * @return
	 * @author wul 2016-1-17
	 */
	public static String getBeforeDatePlusDay(int numVal, String dateFormat) {
		Calendar calendar = Calendar.getInstance();
		long currentTimeMillis = calendar.getTimeInMillis();

		long hourInMillis = 60 * 60 * 1000;
		long dVal = numVal * 24 * hourInMillis;

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String currentDate = sdf.format(currentTimeMillis - dVal);
		return currentDate;
	}

	/**
	 * 获取当前日期之前的日期，按天数向前推
	 * 
	 * @param numVal
	 * @param dateFormat
	 * @return
	 * @author wul 2016-1-17
	 */
	public static String getAfterDatePlusDay(int numVal, String dateFormat) {
		Calendar calendar = Calendar.getInstance();
		long currentTimeMillis = calendar.getTimeInMillis();

		long hourInMillis = 60 * 60 * 1000;
		long dVal = numVal * 24 * hourInMillis;

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String currentDate = sdf.format(currentTimeMillis + dVal);
		return currentDate;
	}

	/**
	 * 获取当前日期之前的日期，按小时向前推
	 * 
	 * @param numVal
	 * @param dateFormat
	 * @return
	 * @author wul 2016-1-17
	 */
	public static String getBeforeDatePlusHour(int numVal, String dateFormat) {
		Calendar calendar = Calendar.getInstance();
		long currentTimeMillis = calendar.getTimeInMillis();

		long hourInMillis = 60 * 60 * 1000;
		long dVal = numVal * hourInMillis;

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String currentDate = sdf.format(currentTimeMillis - dVal);
		return currentDate;
	}

	/**
	 * 获取当前日期之前的日期，按小时向前推
	 * 
	 * @param numVal
	 * @param dateFormat
	 * @return
	 * @author wul 2016-1-17
	 */
	public static String getAfterDatePlusHour(int numVal, String dateFormat) {
		Calendar calendar = Calendar.getInstance();
		long currentTimeMillis = calendar.getTimeInMillis();

		long hourInMillis = 60 * 60 * 1000;
		long dVal = numVal * hourInMillis;

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String currentDate = sdf.format(currentTimeMillis + dVal);
		return currentDate;
	}

	/**
	 * 两个日期相差天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int daysBetween(Date beginDate, Date endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(endDate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	
	
	
	/**
	 * 获取某月天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getMonthdays(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 给时间加减年份
	 * 
	 * @param date
	 * @param plusTime
	 * @return
	 * @author wul 2016-1-18
	 */
	public static Date getDatePlusYear(Date date, int plusTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, plusTime);
		Date d = cal.getTime();
		return d;
	}

	/**
	 * 给时间加减月份
	 * 
	 * @param date
	 * @param plusTime
	 * @return
	 * @author wul 2016-1-18
	 */
	public static Date getDatePlusMonth(Date date, int plusTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, plusTime);
		Date d = cal.getTime();
		return d;
	}

	/**
	 * 给时间加减天数
	 * 
	 * @param date
	 * @param plusTime
	 * @return
	 * @author wul 2016-1-18
	 */
	public static Date getDatePlusDay(Date date, int plusTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, plusTime);
		Date d = cal.getTime();
		return d;
	}

	/**
	 * 给时间加减小时
	 * 
	 * @param date
	 * @param plusTime
	 * @return
	 * @author wul 2016-1-18
	 */
	public static Date getDatePlusHour(Date date, int plusTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, plusTime);
		Date d = cal.getTime();
		return d;
	}

	/**
	 * 给时间加减分钟
	 * 
	 * @param date
	 * @param plusTime
	 * @return
	 * @author wul 2016-1-18
	 */
	public static Date getDatePlusMinute(Date date, int plusTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, plusTime);
		Date d = cal.getTime();
		return d;
	}

	/**
	 * 给时间加减秒
	 * 
	 * @param date
	 * @param plusTime
	 * @return
	 * @author wul 2016-1-18
	 */
	public static Date getDatePlusSecond(Date date, int plusTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, plusTime);
		Date d = cal.getTime();
		return d;
	}

	/**
	 * 返回当前年
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(1);
	}

	/**
	 * 返回当前月
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(2) + 1;
	}

	/**
	 * 返回当前天
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(5);
	}

	/**
	 * 返回当前小时
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getCurrentHour() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(11);
	}

	/**
	 * 返回当前分钟
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getCurrentMinute() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(12);
	}

	/**
	 * 返回当前秒
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getCurrentSecond() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(13);
	}

	/**
	 * 返回当前年
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(1);
	}

	/**
	 * 返回当前月
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(2) + 1;
	}

	/**
	 * 返回当前天
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(5);
	}

	/**
	 * 返回当前小时
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(11);
	}

	/**
	 * 返回当前分钟
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(12);
	}

	/**
	 * 返回当前秒
	 * 
	 * @return
	 * @author wul 2016-1-18
	 */
	public static int getSecond(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(13);
	}
	
	
	public static void main(String[] args) {
		//String currentMonth = DateUtil.dateToString(new Date(),DateUtil.YM_NORMAL_FORMAT);
		String currentMonth ="2019-03-30 12:00:00";
		Date stringToDate = DateUtil.stringToDate(currentMonth,DateUtil.DATETIME_NORMAL_FORMAT);
		Date datePlusMonth = DateUtil.getDatePlusMonth(stringToDate,-1);
		String currentMonth1 = DateUtil.dateToString(datePlusMonth,DateUtil.DATETIME_NORMAL_FORMAT);
		System.out.println("currentMonth"+currentMonth);
		System.out.println("stringToDate"+stringToDate);
		System.out.println("datePlusMonth"+currentMonth1);
		
		System.out.println(dateToString(new Date(),YM_NORMAL_FORMAT));
		System.out.println(DateUtil.getMonthdays(2019, 10));
		Date date=DateUtil.stringToDate("2019-02",YM_NORMAL_FORMAT);
		System.out.println(date);
		System.out.println(DateUtil.getMonth(date));
		System.out.println(DateUtil.getYear(date));
		System.out.println(
				DateUtil.dateToString(new java.sql.Date(System.currentTimeMillis()), DateUtil.DATE_NORMAL_FORMAT));
	}
}
