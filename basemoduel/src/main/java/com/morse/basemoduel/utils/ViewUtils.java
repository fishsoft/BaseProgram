/** Copyright © 2015-2020 100msh.com All Rights Reserved */
package com.morse.basemoduel.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import android.os.Build;
import android.view.View;

/**
 * View相关工具
 * 
 * @author Frank
 * @date 2015年9月7日下午7:13:55
 */

public class ViewUtils {

	/**
	 * 获取焦点
	 * 
	 * @param view
	 */
	public static void requestFocus(View view) {
		// 设置可以聚焦
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		// 设置聚焦
		view.requestFocus();
	}
	
	/**
	 * 解决android5.1版本一下，TextLine引起的内存泄漏问题
	 */
	public static void clearTextLineCache(){
		if(Build.VERSION.SDK_INT>=22){
			return;
		}
		Field textLineCache=null;
		Object cached=null;
		try{
			textLineCache=Class.forName("android.text.TextLine").getDeclaredField("sCached");
			textLineCache.setAccessible(true);
			cached=textLineCache.get(null);
			if(cached!=null){
				for(int i=0,l=Array.getLength(cached);i<l;i++){
					Array.set(cached, i, null);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
