package com.morse.basemoduel.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;

public class StringUtils {

	public static String getLastString(String string, String c) {
		int index = string.lastIndexOf(c);
		return (String) string.subSequence(index + 1, string.length());
	}

	public String deleteString(ArrayList<String> string) {
		return string.toString().replace("[", "").replace("]", "").trim().replace(",", ";") + ";";
	}

	public static String deleString(String string) {
		if (string.contains("\""))
			return string.replace("\"", "");
		return string;
	}

	public static String getVersion(String string) {
		return string.substring(string.lastIndexOf("_") + 1, string.length());
	}

	/**
	 * 获取固件版本
	 * 
	 * @param string
	 *            /storage/emulated/0/bmsh/upgrade/100MSHBHU2S_R_2.1
	 *            .0_2014_12_10_17_53.bin
	 * @return
	 */
	public static String getRomVersion(String string) {
		return string.substring(string.lastIndexOf("/") + 1, string.length()).replace(".bin", "");
	}

	public static String formatTime(String string) {
		int time = Integer.parseInt(string);
		int miao = time % 60, sec = time / 60, hour = 0, day = 0;
		if (sec >= 60) {
			hour = sec / 60;
			sec = sec % 60;
			if (hour >= 24) {
				day = hour / 24;
				hour = hour % 24;
			}
		}

		if (day > 0) {
			return day + "天" + hour + "小时" + sec + "分" + miao + "秒";
		} else if (hour > 0) {
			return hour + "小时" + sec + "分" + miao + "秒";
		}

		return sec + "分" + miao + "秒";
	}

	public String getString(String string) {
		return string.trim().replace(";", ",");
	}

	public static String changgeString(String string) {
		if ("HT20".equals(string))
			string = "20M";
		if ("HT40".equals(string))
			string = "40M";
		if ("20M".equals(string))
			string = "HT20";
		if ("40M".equals(string))
			string = "HT40";
		return string;
	}

	public static String replaceString(String string) {
		if (string.contains("100msh")) {
			string = string.replace("100msh", "百米生活");
		} else {
			string = string.replace("百米生活", "100msh");
		}
		return string;
	}

	/**
	 * 获取xml里面的内容
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static String getString(int resId, Context context) {
		return context.getResources().getString(resId);
	}

//	public static String replaceWithBlank(String str) {
//		if (!TextUtils.isEmpty(str)) {
//			Pattern p = Pattern.compile("\\s*|\\t|\\r|\\n|\\");
//			Matcher m = p.matcher(str);
//			String finishedReplaceStr = m.replaceAll("");
//			return finishedReplaceStr;
//		}
//		return null;
//	}

	public static String splitIPString(String ip) {
		return ip.substring(0, ip.lastIndexOf("."));
	}

	public static String[] splitIPStrings(String ip) {
		return ip.split(".");
	}

	public static String addString(String string) {
		if (string.length() == 1) {
			string = "0" + string;
		}
		return string;
	}

	/**
	 * 判断如影随行地址是否合法
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isFollow(String string) {

		if (RegexUtils.checkURL(replaceString(replaceString(string, "http://"), "/ad.js"))) {
			return true;
		}
		return false;
	}

	/**
	 * 裁剪如影随行地址
	 * 
	 * @param string
	 * @return
	 */
	public static String replaceFollow(String string) {
		if (string.contains("http://")) {
			string = string.replace("http://", "");
		}
		if (string.contains("/ad.js")) {
			string = string.replace("/ad.js", "");
		}
		return string;
	}

	public static String replaceString(String string, String replaceString) {
		return string.replace(replaceString, "");
	}

	public static int string2Int(String string) {
		int number = 0;
		if (TextUtils.isEmpty(string)) {
			number = 0;
		} else if (RegexUtils.checkInteger(string)) {
			number = Integer.parseInt(string);
		}
		return number;
	}

}
