package com.wz.wagemanager.tools;

import org.joda.time.DateTime;

import java.text.NumberFormat;

public class SerialNo {
	
	private static long sequence;
	private static String compareTime;
	private static NumberFormat numberFormat;
	private static long smallSequence;
	private static String smallTime;
	private static NumberFormat smallFormat;
	
	private static long longSequence;
	private static String longTime;
	private static NumberFormat longFormat;
	
	static {
		numberFormat = NumberFormat.getInstance();
		numberFormat.setGroupingUsed(false);
		numberFormat.setMinimumIntegerDigits(5);
		numberFormat.setMaximumIntegerDigits(5);
		smallFormat = NumberFormat.getInstance();
		smallFormat.setGroupingUsed(false);
		smallFormat.setMinimumIntegerDigits(4);
		smallFormat.setMaximumIntegerDigits(4);
		longFormat = NumberFormat.getInstance();
		// 是否允许分组 1123456=》1,123,456
		longFormat.setGroupingUsed(false);
		// 允许小整数位数
		longFormat.setMinimumIntegerDigits(13);
		// 允许大整数位数
		longFormat.setMaximumIntegerDigits(13);
	}
	
	private SerialNo() { }
	
	/**
	 * 长度为20
	 * @return
	 */
	public static synchronized String getUNID() {
		DateTime dateTime = new DateTime();
		String currentTime = dateTime.toString("yyMMddHHmmssSSS");
		if (compareTime == null || compareTime.compareTo(currentTime) != 0) {
			compareTime = currentTime;
			sequence = 1L;
		} else {
			sequence++;
		}
		return (new StringBuilder(String.valueOf(currentTime))).append(
				numberFormat.format(sequence)).toString();
	}
	
	public static synchronized String getDateID() {
		DateTime dateTime = new DateTime();
		String currentTime = dateTime.toString("yyyyMMdd");
		if (compareTime == null || compareTime.compareTo(currentTime) != 0) {
			compareTime = currentTime;
			sequence = 1L;
		} else {
			sequence++;
		}
		return (new StringBuilder(String.valueOf(currentTime))).append(
				numberFormat.format(sequence)).toString();
	}
	
	/**
	 * 字符串长度为30
	 * @return
	 */
	public static synchronized String getStringUNID() {
		DateTime dateTime = new DateTime();
		String currentTime = dateTime.toString("yyyyMMddHHmmssSSS");
		if (longTime == null || longTime.compareTo(currentTime) != 0) {
			longTime = currentTime;
			longSequence = 1L;
		} else {
			longSequence++;
		}
		return (new StringBuilder(String.valueOf(currentTime))).append(
				longFormat.format(longSequence)).toString();
	}

	public static String getSerialforDB() {
		DateTime dateTime = new DateTime();
		return dateTime.toString("yyMMddHHmmssSSS");
	}

	public static String getShortSerial() {
		DateTime dateTime = new DateTime();
		return dateTime.toString("mmssSSS");
	}

	/**
	 * 长度为16
	 * @return
	 */
	public static synchronized String getSmallUNID() {
		DateTime dateTime = new DateTime();
		String currentTime = dateTime.toString("yyMMddHHmmss");
		if (smallTime == null || smallTime.compareTo(currentTime) != 0) {
			smallTime = currentTime;
			smallSequence = 1L;
		} else {
			smallSequence++;
		}
		return (new StringBuilder(String.valueOf(currentTime))).append(
				smallFormat.format(smallSequence)).toString();
	}

}
