package com.morse.basemoduel.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.morse.basemoduel.R;

/**
 * 提示对话框
 * Created by Administrator on 2017/1/9.
 */

public class NotifyDialog extends BaseDialog {

    private Button btnNotify;

    public NotifyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void initDialog() {
        btnNotify = (Button) view.findViewById(R.id.btn_notify);
        btnNotify.setOnClickListener(this);
    }

    /**
     * 设置btn文本
     * @param btnText
     */
    public void setBtnText(String btnText) {
        if (!TextUtils.isEmpty(btnText)) {
            btnNotify.setText(btnText);
        }
    }

    /**
     * 设置btn文本
     * @param btnText
     */
    public void setBtnText(int btnText) {
        if (null == mContext) {
            return;
        }
        String btnStr = mContext.getString(btnText);
        if (!TextUtils.isEmpty(btnStr)) {
            btnNotify.setText(btnText);
        }
    }

    @Override
    public View setContentView() {
        return LayoutInflater.from(mContext).inflate(R.layout.dialog_notify_layout, null);
    }

    public NotifyDialog(Context context) {
        super(context);
    }

    @Override
    public void viewClick(View view) {
        super.viewClick(view);
        if (R.id.btn_notify == view.getId()) {
            dismiss();
        }
    }
}
