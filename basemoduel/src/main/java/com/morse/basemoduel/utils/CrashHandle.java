package com.morse.basemoduel.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QiSheng
 * @time 2016-5-5上午10:38:18
 */
public class CrashHandle {

    private HashMap<String, String> infos = new HashMap<String, String>();
    // 用于格式化日期,作为日志文件名的一部分
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public void handleExcption(Context context, Thread thread, Throwable ex) {
        ex.printStackTrace();

        StringWriter sw = new StringWriter();
        PrintWriter err = new PrintWriter(sw);
        ex.printStackTrace(err);

        StringBuffer result = new StringBuffer();
        for (Map.Entry<String, String> entry : collectDeviceInfo(context).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            result.append(key + "=" + value + "\n");
        }
        result.append(sw.toString());

//		try {
//			FileOutputStream os = new FileOutputStream(new File(SdcardUtils.getCrashCache(), System.currentTimeMillis()
//					+ ".txt"));
//
//			os.write(result.toString().getBytes());
//			os.close();
//
//			android.os.Process.killProcess(android.os.Process.myPid());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        saveCrashInfo2File(ex);
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    private HashMap<String, String> collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("版本名称:", versionName);
                infos.put("版本编码:", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return infos;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    public String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        try {
            String time = formatter.format(new Date());

            String fileName = "crash-" + time + "-" + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = FileUtils.getCrashCache();
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
