package com.morse.basemoduel.utils;

import java.net.Inet4Address;

public class IPUtils {

	public static long A_IP_MIN = 16777217l;
	public static long A_IP_MAX = 2130706430l;
	public static long B_IP_MIN = 2147549185l;
	public static long B_IP_MAX = 3221225470l;
	public static long C_IP_MIN = 3221225729l;
	public static long C_IP_MAX = 3758096126l;
	public static long D_IP_MIN = 3758096385l;
	public static long D_IP_MAX = 4026531838l;

	/**
	 * 将int型ip转成String型ip
	 * 
	 * @param intIp
	 * @return
	 */
	public static String int2Ip(int intIp) {
		byte[] bytes = int2byte(intIp);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb.append(bytes[i] & 0xFF);
			if (i < 3) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符串型ip转成int型ip
	 * 
	 * @param strIp
	 * @return
	 */
	public static int Ip2Int(String strIp) {
		return byte2Int(ip2byte(strIp));
	}

	/**
	 * 将字符串表示的ip地址转换为long表示.
	 * 
	 * @param ip
	 *            ip地址
	 * @return 以32位整数表示的ip地址
	 */
	public static long Ip2Long(String ip) {
		String[] ipNums = ip.split("\\.");
		return (Long.parseLong(ipNums[0]) << 24) + (Long.parseLong(ipNums[1]) << 16) + (Long.parseLong(ipNums[2]) << 8)
				+ (Long.parseLong(ipNums[3]));
	}

	/**
	 * 将long型ip转换成字符串ip
	 * 
	 * @param longIp
	 * @return
	 */
	public static String long2IP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	/**
	 * 将IP地址转换成字节型
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] int2byte(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (0xff & i);
		bytes[1] = (byte) ((0xff00 & i) >> 8);
		bytes[2] = (byte) ((0xff0000 & i) >> 16);
		bytes[3] = (byte) ((0xff000000 & i) >> 24);
		return bytes;
	}

	/**
	 * 将字节IP转换成整型
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byte2Int(byte[] bytes) {
		int n = bytes[0] & 0xFF;
		n |= ((bytes[1] << 8) & 0xFF00);
		n |= ((bytes[2] << 16) & 0xFF0000);
		n |= ((bytes[3] << 24) & 0xFF000000);
		return n;
	}

	/**
	 * 将IP地址转换成字节型
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] ip2byte(String strIp) {
		String[] ss = strIp.split("\\.");
		if (ss.length != 4) {
			return null;
		}
		byte[] bytes = new byte[ss.length];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(ss[i]);
		}
		return bytes;
	}

	/**
	 * Function: 将int类型的IP转换成字符串形式的IP<br>
	 * 
	 * @author ZYT DateTime 2014-5-14 下午12:28:16<br>
	 * @param ip
	 * @return<br>
	 */
	public static String ipIntToString(int ip) {
		try {
			return Inet4Address.getByAddress(int2byte(ip)).getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取IP地址的类型
	 * 
	 * @param ipStr
	 * @return
	 */
	public static String getIPType(String ipStr) {
		long ip;
		String type = "";
		ip = Ip2Long(ipStr);
		if (ip >= A_IP_MIN && ip <= A_IP_MAX) {
			type = "A类地址";
		} else if (ip >= B_IP_MIN && ip <= B_IP_MAX) {
			type = "B类地址";
		} else if (ip >= C_IP_MIN && ip <= C_IP_MAX) {
			type = "C类地址";
		} else if (ip >= D_IP_MIN && ip <= D_IP_MAX) {
			type = "D类地址";
		} else {
			type = "E类地址";
		}
		return type;
	}

	public String toString(String ipStr) {
		String type = getIPType(ipStr);
		return ipStr + ":" + type;
	}

}
