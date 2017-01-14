package com.morse.basemoduel.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.morse.basemoduel.R;

/**
 * 加载对话框
 * Created by Administrator on 2017/1/9.
 */

public class LoadingDialog extends BaseDialog {

    @Override
    public void initDialog() {

    }

    @Override
    public View setContentView() {
        return LayoutInflater.from(mContext).inflate(R.layout.dialog_loading_layout, null);
    }

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
}
