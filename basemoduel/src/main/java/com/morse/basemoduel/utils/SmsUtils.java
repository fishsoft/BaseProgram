package com.morse.basemoduel.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * author：Morse
 * time：2016/11/10 16:12
 * Descripte：
 */

public class SmsUtils {

    /**
     * 返回短信数组
     *
     * @param activity
     * @return
     */
    public static JSONArray getSmsJson(Activity activity) {
        ContentResolver cr = activity.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
//        List<Message> messages = new ArrayList<>();
//        messages.clear();
//
//        Cursor c = cr.query(uri, null, null, null, null);
//        int totalSms = c.getCount();
//        if (totalSms <= 0) {
//            return null;
//        }
//        if (c.moveToFirst()) {
//            for (int i = 0; i < totalSms; i++) {
//                Message message = new Message();
//                message.setType(c.getInt(c.getColumnIndexOrThrow(Telephony.Sms.Inbox.TYPE)));
//                message.setAddress(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.Inbox.ADDRESS)));
//                message.setPerson(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.Inbox.PERSON)));
//                message.setDate(c.getLong(c.getColumnIndexOrThrow(Telephony.Sms.Inbox.DATE)));
//                message.setRead(c.getInt(c.getColumnIndexOrThrow(Telephony.Sms.Inbox.READ)));
//                message.setStatus(c.getInt(c.getColumnIndexOrThrow(Telephony.Sms.Inbox.STATUS)));
//                message.setBody(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.Inbox.BODY)));
//                message.setProtocol(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.Inbox.PROTOCOL)));
//                messages.add(message);
//                c.moveToNext();
//            }
//        }
//        c.close();

        JSONArray object = null;
        try {
//            object = JSON.parseArray(messages.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 获取本机电话号码
     *
     * @return
     */
    private static String getPhone(Activity activity) {
        TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getLine1Number();
    }

    public static void sendMessage(String phone, String message) {
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> list = manager.divideMessage(message);  //因为一条短信有字数限制，因此要将长短信拆分
        for (String text : list) {
            manager.sendTextMessage(phone, null, text, null, null);
        }
    }

}
