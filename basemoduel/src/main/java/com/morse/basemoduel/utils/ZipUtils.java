package com.morse.basemoduel.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author QiSheng
 * @time 2016-6-6上午9:17:03
 */
public class ZipUtils {
	public static String ENCODING = "utf-8";// "ISO-8859-1"

	// 压缩
	public static String compress(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		LogUtils.d("img", str);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(ENCODING));
			gzip.close();
			String gzipOut = out.toString(ENCODING);
			LogUtils.d("img", gzipOut);
			return gzipOut;
		} catch (IOException e) {
			LogUtils.show("gzip failure: " + e.getMessage());
			return str;
		}

	}

	// 解压缩
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(ENCODING));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		// toString()使用平台默认编码，也可以显式的指定如toString("GBK")
		return out.toString();
	}

}
