package com.morse.basemoduel.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class UIUtils {

    /**
     * 获得上下文
     *
     * @return
     */
    public static Context getContext() {
        return getContext();
    }

    /**
     * 获得资源
     *
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获得string类型的数据
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    /**
     * 获取string类型
     *
     * @param resId
     * @param formatArgs
     * @return
     */
    public static String getString(int resId, Object... formatArgs) {
        return getContext().getResources().getString(resId, formatArgs);
    }

    /**
     * 获得数组集合
     *
     * @param resId
     * @return
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获得颜色值
     *
     * @param resId
     * @return
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * px2dp
     *
     * @param px
     * @return
     */
    public static int px2dp(int px) {
        // px = dp * (dpi / 160)
        // dp = px * 160 / dpi

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        return (int) (px * 160f / dpi + 0.5f);
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        // px = dp * (dpi / 160)
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;

        return (int) (dp * (dpi / 160f) + 0.5f);
    }

    /**
     * px2sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp2px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获得包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }
}
