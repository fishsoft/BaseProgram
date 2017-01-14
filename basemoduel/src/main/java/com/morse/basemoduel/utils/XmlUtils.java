package com.morse.basemoduel.utils;

import android.text.TextUtils;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * XML解析类
 */
public class XmlUtils {

    public Object parse(String text, Class clasz) {
        return parse(text.getBytes(), clasz);
    }

    public Object parse(byte[] bytes, Class clasz) {
        return parse(new ByteArrayInputStream(bytes), clasz);
    }

    public Object parse(InputStream is, Class clasz) {
        XmlPullParser xpp = Xml.newPullParser();
        Object obj = null;
        try {
            xpp.setInput(is, "UTF-8");
            int eventType = xpp.getEventType();
            String noteName = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        obj = Class.forName(clasz.getName()).newInstance();
                        clasz = obj.getClass();
                        break;
                    case XmlPullParser.START_TAG:
                        noteName = xpp.getName();
                        if (!TextUtils.isEmpty(noteName)) {
                            Field[] fields = clasz.getDeclaredFields();
                            if (fields != null && fields.length > 0) {
                                for (Field field : fields) {
                                    if (field == null) continue;
                                    if (noteName.equals(field.getName())) {
                                        field.setAccessible(true);
                                        if (isBaseClass(field)) {

                                            eventType = xpp.next();
                                            field.set(obj, xpp.getText());
                                        } else {

                                            //field.set(obj, );
                                        }
                                    }
                                }
                            }
                        }

                        break;
                    case XmlPullParser.END_TAG:

                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public boolean isBaseClass(Field field) {
        try {
            return ((Class) field.get(null)).isPrimitive() || ((Class) field.get(null)).getName().equals(String.class.getName());
        } catch (Exception e) {
            return false;
        }
    }
}
