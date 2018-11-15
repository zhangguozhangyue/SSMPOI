package cn.zg.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	
	/**
	 * 日期转字符串
	 * @param date
	 * @param dateFormat
	 * @return
	 * @throws Exception
	 */
	public static String dateToString(Date date,String dateFormat) throws Exception{
		SimpleDateFormat format=new SimpleDateFormat(dateFormat);
		return format.format(date);
	}
	
	/**
	 * 字符串转日期
	 * @param str
	 * @param dateFormat
	 * @return
	 * @throws Exception
	 */
	public static Date strToDate(String str,String dateFormat) throws Exception{
		SimpleDateFormat format=new SimpleDateFormat(dateFormat);
		return format.parse(str);
	}
}

