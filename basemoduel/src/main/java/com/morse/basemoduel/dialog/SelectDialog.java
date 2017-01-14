package com.morse.basemoduel.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.morse.basemoduel.R;

/**
 * 选择对话框
 * Created by Administrator on 2017/1/9.
 */

public class SelectDialog extends BaseDialog {

    private Button btnSelectConfirm, btnSelectCancel;

    @Override
    public void initDialog() {
        btnSelectConfirm = (Button) view.findViewById(R.id.btn_select_confirm);
        btnSelectCancel = (Button) view.findViewById(R.id.btn_select_cancel);

        btnSelectConfirm.setOnClickListener(this);
        btnSelectCancel.setOnClickListener(this);
    }

    /**
     * 设置确认按钮文本
     *
     * @param confirmText
     */
    public void setConfirmText(String confirmText) {
        if (!TextUtils.isEmpty(confirmText)) {
            btnSelectConfirm.setText(confirmText);
        }
    }

    /**
     * 设置确认按钮文本
     *
     * @param confirmId
     */
    public void setConfirmText(int confirmId) {
        if (null == mContext) {
            return;
        }
        String confirmText = mContext.getString(confirmId);
        if (!TextUtils.isEmpty(confirmText)) {
            btnSelectConfirm.setText(confirmText);
        }
    }

    /**
     * 设置取消按钮文本
     *
     * @param cancelText
     */
    public void setCancelText(String cancelText) {
        if (!TextUtils.isEmpty(cancelText)) {
            btnSelectCancel.setText(cancelText);
        }
    }

    /**
     * 设置取消按钮文本
     *
     * @param cancelId
     */
    public void setCancelText(int cancelId) {
        if (null == mContext) {
            return;
        }
        String cancelText = mContext.getString(cancelId);
        if (!TextUtils.isEmpty(cancelText)) {
            btnSelectCancel.setText(cancelText);
        }
    }

    @Override
    public View setContentView() {
        return LayoutInflater.from(mContext).inflate(R.layout.dialog_select_layout, null);
    }

    public SelectDialog(Context context) {
        super(context);
    }

    public SelectDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void viewClick(View view) {
        super.viewClick(view);
        int i = view.getId();
        if (i == R.id.btn_select_confirm) {
            dismiss();
        } else if (i == R.id.btn_select_cancel) {
            dismiss();
        } else {
        }
    }
}
