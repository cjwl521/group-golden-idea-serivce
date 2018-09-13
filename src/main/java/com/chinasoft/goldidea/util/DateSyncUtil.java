package com.chinasoft.goldidea.util;

import java.math.BigDecimal;
import java.text.DateFormat;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.chinasoft.goldidea.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateSyncUtil {

	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	public static final String YYYYMMddHHmmss = "yyyyMMddHHmmss";
	
	public static final String YYYY_MM_DDHHMMSS_UPPER = "yyyy-MM-dd HH:mm:ss";
	
	public static final String YYYY_MM_DDHHMMSS_LOWER = "yyyy-MM-dd hh:mm:ss";
	
	public static final String YYYYMMDD = "yyyyMMdd";
	
	public static final String YYYY_MM_DDTHHMMSSSSS000Z = "yyyy-MM-dd'T'HH:mm:ss.SSS000Z";
	
	public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	
	public static final String YYYY_MM_DDHHMM = "yyyy-MM-dd hh:mm";

	public static final String TIMEZONE = "GMT+8:00";
	
	public static final String   YY_MM_DDHHMMSS_SLASH="yy/MM/dd hh:mm:ss";


	private static SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat(YYYY_MM_DD);
	private static SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat(YYYYMMddHHmmss);
	private static SimpleDateFormat yyyy_mm_ddhhmmss_upper = new SimpleDateFormat(YYYY_MM_DDHHMMSS_UPPER);
	private static SimpleDateFormat yyyy_mm_ddhhmmss_lower = new SimpleDateFormat(YYYY_MM_DDHHMMSS_LOWER);
	private static SimpleDateFormat yyyymmdd = new SimpleDateFormat(YYYYMMDD);
	private static SimpleDateFormat yyyy_mm_ddthhmmsssss000z = new SimpleDateFormat(YYYY_MM_DDTHHMMSSSSS000Z);
	private static SimpleDateFormat yyyymmddhhmmsssss = new SimpleDateFormat(YYYYMMDDHHMMSSSSS);
	private static SimpleDateFormat yyyy_mm_ddhhmm = new SimpleDateFormat(YYYY_MM_DDHHMM);
	private static SimpleDateFormat yy_mm_ddhhmmss_slash = new SimpleDateFormat(YY_MM_DDHHMMSS_SLASH);
	public static Date parse(String source, String farmat) {
 
		Date parse = null;
		try {
			switch (farmat) {
			case "yy/MM/dd hh:mm:ss":
				synchronized (yy_mm_ddhhmmss_slash) {
					parse = yy_mm_ddhhmmss_slash.parse(source);
				}
				break;
			case "yyyy-MM-dd":
				synchronized (yyyy_mm_dd) {
					parse = yyyy_mm_dd.parse(source);
				}
				break;
			case "yyyyMMddHHmmss":
				synchronized (yyyymmddhhmmss) {
					parse = yyyymmddhhmmss.parse(source);
				}
				break;
			case "yyyy-MM-dd HH:mm:ss":
				synchronized (yyyy_mm_ddhhmmss_upper) {
					parse = yyyy_mm_ddhhmmss_upper.parse(source);
				}
				break;
			case "yyyy-MM-dd hh:mm:ss":
				synchronized (yyyy_mm_ddhhmmss_lower) {
					parse = yyyy_mm_ddhhmmss_lower.parse(source);
				}
				break;
			case "yyyyMMdd":
				synchronized (yyyymmdd) {
					parse = yyyymmdd.parse(source);
				}
				break;
			case "yyyy-MM-dd'T'HH:mm:ss.SSS000Z":
				synchronized (yyyy_mm_ddthhmmsssss000z) {
					parse = yyyy_mm_ddthhmmsssss000z.parse(source);
				}
				break;
			case "yyyyMMddHHmmssSSS":
				synchronized (yyyymmddhhmmsssss) {
					parse = yyyymmddhhmmsssss.parse(source);
				}
				break;
			case "yyyy-MM-dd hh:mm":
				synchronized (yyyy_mm_ddhhmm) {
					parse = yyyy_mm_ddhhmm.parse(source);
				}
				break;
			}
			return parse;
		} catch (ParseException e) {
			log.info ("DateSyncUtil is error:{}",e.getMessage ());
			throw new BusinessException ("TM0001","DateSyncUtil is error");
		}
	}
	
	public static Date parse(String source, String format, TimeZone timeZone) {

		Date parse = null;
		try {
			switch (format) {
			case "yy/MM/dd hh:mm:ss":
				synchronized (yy_mm_ddhhmmss_slash) {
					yy_mm_ddhhmmss_slash.setTimeZone(timeZone);
					parse = yy_mm_ddhhmmss_slash.parse(source);
				}
				break;
			case "yyyy-MM-dd":
				synchronized (yyyy_mm_dd) {
					yyyy_mm_dd.setTimeZone(timeZone);
					parse = yyyy_mm_dd.parse(source);
				}
				break;
			case "yyyyMMddHHmmss":
				synchronized (yyyymmddhhmmss) {
					yyyymmddhhmmss.setTimeZone(timeZone);
					parse = yyyymmddhhmmss.parse(source);
				}
				break;
			case "yyyy-MM-dd HH:mm:ss":
				synchronized (yyyy_mm_ddhhmmss_upper) {
					yyyy_mm_ddhhmmss_upper.setTimeZone(timeZone);
					parse = yyyy_mm_ddhhmmss_upper.parse(source);
				}
				break;
			case "yyyy-MM-dd hh:mm:ss":
				synchronized (yyyy_mm_ddhhmmss_lower) {
					yyyy_mm_ddhhmmss_lower.setTimeZone(timeZone);
					parse = yyyy_mm_ddhhmmss_lower.parse(source);
				}
				break;
			case "yyyyMMdd":
				synchronized (yyyymmdd) {
					yyyymmdd.setTimeZone(timeZone);
					parse = yyyymmdd.parse(source);
				}
				break;
			case "yyyy-MM-dd'T'HH:mm:ss.SSS000Z":
				synchronized (yyyy_mm_ddthhmmsssss000z) {
					yyyy_mm_ddthhmmsssss000z.setTimeZone(timeZone);
					parse = yyyy_mm_ddthhmmsssss000z.parse(source);
				}
				break;
			case "yyyyMMddHHmmssSSS":
				synchronized (yyyymmddhhmmsssss) {
					yyyymmddhhmmsssss.setTimeZone(timeZone);
					parse = yyyymmddhhmmsssss.parse(source);
				}
				break;
			case "yyyy-MM-dd hh:mm":
				synchronized (yyyy_mm_ddhhmm) {
					yyyy_mm_ddhhmm.setTimeZone(timeZone);
					parse = yyyy_mm_ddhhmm.parse(source);
				}
				break;
			}
			return parse;
		} catch (ParseException e) {
			log.info ("DateSyncUtil is error:{}",e.getMessage ());
			throw new BusinessException ("TM0001","DateSyncUtil is error");
		}
	}

	public static String format(Date source, String format) {
		String result = null;
		switch (format) {
		case "yy/MM/dd hh:mm:ss":
			synchronized (yy_mm_ddhhmmss_slash) {
				result = yy_mm_ddhhmmss_slash.format(source);
			}
			break;
		case "yyyy-MM-dd":
			synchronized (yyyy_mm_dd) {
				result = yyyy_mm_dd.format(source);
			}
			break;
		case "yyyyMMddHHmmss":
			synchronized (yyyymmddhhmmss) {
				result = yyyymmddhhmmss.format(source);
			}
			break;
		case "yyyy-MM-dd HH:mm:ss":
			synchronized (yyyy_mm_ddhhmmss_upper) {
				result = yyyy_mm_ddhhmmss_upper.format(source);
			}
			break;
		case "yyyy-MM-dd hh:mm:ss":
			synchronized (yyyy_mm_ddhhmmss_lower) {
				result = yyyy_mm_ddhhmmss_lower.format(source);
			}
			break;
		case "yyyyMMdd":
			synchronized (yyyymmdd) {
				result = yyyymmdd.format(source);
			}
			break;
		case "yyyy-MM-dd'T'HH:mm:ss.SSS000Z":
			synchronized (yyyy_mm_ddthhmmsssss000z) {
				result = yyyy_mm_ddthhmmsssss000z.format(source);
			}
			break;
		case "yyyyMMddHHmmssSSS":
			synchronized (yyyymmddhhmmsssss) {
				result = yyyymmddhhmmsssss.format(source);
			}
			break;
		case "yyyy-MM-dd hh:mm":
			synchronized (yyyy_mm_ddhhmm) {
				result = yyyy_mm_ddhhmm.format(source);
			}
			break;
		}
		return result;
	}
	
	public static String format(Date source, String format ,TimeZone timeZone) {
		String result = null;
		switch (format) {
		case "yy/MM/dd hh:mm:ss":
			synchronized (yy_mm_ddhhmmss_slash) {
				yy_mm_ddhhmmss_slash.setTimeZone(timeZone);
				result = yy_mm_ddhhmmss_slash.format(source);
			}
			break;
		case "yyyy-MM-dd":
			synchronized (yyyy_mm_dd) {
				yyyy_mm_dd.setTimeZone(timeZone);
				result = yyyy_mm_dd.format(source);
			}
			break;
		case "yyyyMMddHHmmss":
			synchronized (yyyymmddhhmmss) {
				yyyymmddhhmmss.setTimeZone(timeZone);
				result = yyyymmddhhmmss.format(source);
			}
			break;
		case "yyyy-MM-dd HH:mm:ss":
			synchronized (yyyy_mm_ddhhmmss_upper) {
				yyyy_mm_ddhhmmss_upper.setTimeZone(timeZone);
				result = yyyy_mm_ddhhmmss_upper.format(source);
			}
			break;
		case "yyyy-MM-dd hh:mm:ss":
			synchronized (yyyy_mm_ddhhmmss_lower) {
				yyyy_mm_ddhhmmss_lower.setTimeZone(timeZone);
				result = yyyy_mm_ddhhmmss_lower.format(source);
			}
			break;
		case "yyyyMMdd":
			synchronized (yyyymmdd) {
				yyyymmdd.setTimeZone(timeZone);
				result = yyyymmdd.format(source);
			}
			break;
		case "yyyy-MM-dd'T'HH:mm:ss.SSS000Z":
			synchronized (yyyy_mm_ddthhmmsssss000z) {
				yyyy_mm_ddthhmmsssss000z.setTimeZone(timeZone);
				result = yyyy_mm_ddthhmmsssss000z.format(source);
			}
			break;
		case "yyyyMMddHHmmssSSS":
			synchronized (yyyymmddhhmmsssss) {
				yyyymmddhhmmsssss.setTimeZone(timeZone);
				result = yyyymmddhhmmsssss.format(source);
			}
			break;
		case "yyyy-MM-dd hh:mm":
			synchronized (yyyy_mm_ddhhmm) {
				yyyy_mm_ddhhmm.setTimeZone(timeZone);
				result = yyyy_mm_ddhhmm.format(source);
			}
			break;
		}
		return result;
	}
	/**
	 * 转化毫秒为分钟保留一位小数
	 * 
	 * @param ss
	 * @return
	 */
	public static String formatSsTOMin(long ss) {
		double dd = (double) ss / (60 * 1000);
		DecimalFormat dfmt = new DecimalFormat("#.0");
		return Double.parseDouble(dfmt.format(dd)) + "";

	}
	/**
	 * 转换毫秒为秒
	 * @param ss
	 * @return
	 */
	public static String formatSsTOSec(long ss){
		long s = ss/1000;
		DecimalFormat dfmt = new DecimalFormat("#");
		return dfmt.format(s);
	}

	/**
	 * 根据格式转化毫秒秒为分钟
	 * 
	 * @param
	 * @return
	 */
	public static String formatMsTOMin(long ms, String format) {
		double min = (double) ms / (60 * 1000);
		DecimalFormat dfmt = new DecimalFormat("#.0");
		try {
			dfmt = new DecimalFormat(format);
		} catch (Exception e) {
			dfmt = new DecimalFormat("#.0");
		}
		return Double.parseDouble(dfmt.format(min)) + "";
	}

	/**
	 * 根据格式转化毫秒秒为分钟
	 * 
	 * @param
	 * @return
	 */
	public static String formatMsTOMin(double ms, String format) {
		double min = (double) ms / (60 * 1000);
		DecimalFormat dfmt = new DecimalFormat("#.0");
		try {
			dfmt = new DecimalFormat(format);
		} catch (Exception e) {
			dfmt = new DecimalFormat("#.0");
		}
		return Double.parseDouble(dfmt.format(min)) + "";
	}

	public static String formatMsTODate(long ms) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ms);
		return formatter.format(calendar.getTime());
	}

	public static String formatSqldatestrTODate(String str) {
		SimpleDateFormat f_timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		f_timestamp.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		java.sql.Timestamp timestamp = null;
		try {
			timestamp = java.sql.Timestamp.valueOf(str);
		} catch (Exception e) {
			return "";
		}
		return f_timestamp.format(timestamp);
	}

	public static String timeConversion(Double millisecond){
        double doubleMin = millisecond/60000;
        
        double doubleSecond = (millisecond/1000%60)/60;
        
        BigDecimal bigDecimal = new BigDecimal(doubleMin+doubleSecond);
        double formatResult = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        String stringResult = String.valueOf(formatResult);
        int fastIndex = stringResult.indexOf(".");
        if("00".equals(stringResult.substring(fastIndex+1))){
            return stringResult.substring(0, fastIndex);
        }else if("0".equals(stringResult.substring(fastIndex+1))){
            return stringResult.substring(0, fastIndex);
        }else{
            return String.valueOf(stringResult);
        }
    }
}
