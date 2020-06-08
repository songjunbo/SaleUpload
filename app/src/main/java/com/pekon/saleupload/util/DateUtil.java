package com.pekon.saleupload.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	/**
	 * 返回当前时间字符串。
	 * <p>
	 * 格式：yyyy-MM-dd HH:mm:ss.SSS
	 *
	 * @return String 指定格式的日期字符串.
	 */
	public static String getCurrentTime() {
		return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据给定的格式与时间(Date类型的)，返回时间字符串<br>
	 *
	 * @param date   指定的日期
	 * @param format 日期格式字符串
	 * @return String 指定格式的日期字符串.
	 */
	public static String getFormatDateTime(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
