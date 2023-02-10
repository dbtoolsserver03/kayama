package jp.co.csj.tools.utils.date;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.csj.tools.utils.str.CsjStrUtils;
public final class CsjDateUtil implements Serializable {
	private static final long serialVersionUID = -3098985139095632110L;

	private CsjDateUtil() {
	}


	public static String getDateTime(long microsecond, String pattarn)throws Throwable {
		return getFormatDateTime(new Date(microsecond), pattarn);
	}

	public static String getFormatDateTime(Date date, String format) throws Throwable{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	public static Date getDateFromString(String dateStr, String pattern) throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date resDate = null;
			resDate = sdf.parse(dateStr);
		return resDate;
	}

	public static String getHour(long second) {
		long hour = second / 60 / 60;
		long minute = (second - hour * 60 * 60) / 60;
		long sec = (second - hour * 60 * 60) - minute * 60;

		return hour + "小时" + minute + "分钟" + sec + "秒";

	}

	public static String getMsHour(long ms) {

		long lms = ms % 1000;

		long lm = ms / 1000;

		long hour = lm / 60 / 60;

		long minute = (lm - hour * 60 * 60) / 60;

		long sec = (lm - hour * 60 * 60) - minute * 60;

		String strHour = "h";

		String strMinute = "m";

		String strSec = "s";

		String strLms = "ms";

		if (hour < 10) {

			strHour = "0" + hour + strHour;

		} else {

			strHour = hour + strHour;

		}

		if (minute < 10) {

			strMinute = "0" + minute + strMinute;

		} else {

			strMinute = minute + strMinute;

		}

		if (sec < 10) {

			strSec = "0" + sec + strSec;

		} else {

			strSec = sec + strSec;

		}

		if (lms < 10) {

			strLms = "00" + lms + strLms;

		} else if (lms < 100) {

			strLms = "0" + lms + strLms;

		} else {

			strLms = lms + strLms;

		}

		String retVal = "0";

		if (strHour.contains("00") == false) {
			retVal += strHour;

		}
		if (strMinute.contains("00") == false) {
			retVal += strMinute;

		}
		if (strSec.contains("00") == false) {
			retVal += strSec;

		}
		if (strLms.contains("000") == false) {
			retVal += strLms;

		}
		if ("0".equals(retVal)) {
			retVal =  "0ms";
		} else {
			retVal =  CsjStrUtils.trimLeftChar(retVal,"0");
		}
		return retVal;
	}


	public static boolean checkDate(String sDate, String dateFormat)throws Throwable {
		Date date = getDateFromString(sDate, dateFormat);
		if (date == null) {
			return false;
		}
		String str = getFormatDateTime(date, dateFormat);
		if (!CsjStrUtils.isEqual(str, sDate)) {
			return false;
		}
		return true;
	}


}
