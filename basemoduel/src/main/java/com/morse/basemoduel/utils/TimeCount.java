package com.morse.basemoduel.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.morse.basemoduel.R;

/**
 * Created by Administrator on 2017/1/12.
 */

/* 定义一个倒计时的内部类 */
public class TimeCount extends CountDownTimer {

    private Context mContext;
    private View mView;

    public TimeCount(Context context, View view, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void onFinish() {// 计时完毕时触发
        //防止当倒计时还没有结束，页面退入到后台的时候会报空指针
        if (null == mContext || null == mView) {
            cancel();
            return;
        }

        if (mView instanceof TextView) {
            ((TextView) mView).setText("获取");
            ((TextView) mView).setTextColor(mContext.getResources().getColor(R.color.blue));
        }

        if (mView instanceof Button) {
            ((Button) mView).setText("获取");
            ((Button) mView).setTextColor(mContext.getResources().getColor(R.color.blue));
        }

        if (!mView.isClickable()) {
            mView.setClickable(true);
        }

        cancel();
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        //防止当倒计时还没有结束，页面退入到后台的时候会报空指针
        if (null == mContext || null == mView) {
            cancel();
            return;
        }
        if (mView.isClickable()) {
            mView.setClickable(false);
        }

        if (mView instanceof TextView) {
            ((TextView) mView).setText(millisUntilFinished / 1000 + "秒重新获取");
        }

        if (mView instanceof Button) {
            ((Button) mView).setText(millisUntilFinished / 1000 + "秒重新获取");
        }
    }
}
