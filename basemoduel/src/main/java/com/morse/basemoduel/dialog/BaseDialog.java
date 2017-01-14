package com.morse.basemoduel.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.morse.basemoduel.interfaces.ICancelListener;
import com.morse.basemoduel.interfaces.IConfirmListener;

/**
 * Created by Administrator on 2017/1/9.
 */

public abstract class BaseDialog extends Dialog implements View.OnClickListener {

    protected View view;
    protected Context mContext;
    protected TextView mTitle;
    protected TextView mMsg;

    protected IConfirmListener iConfirmListener;
    protected ICancelListener iCancelListener;

    /**
     * 初始化Dialog
     */
    public abstract void initDialog();

    /**
     * 设置DialogView
     *
     * @return
     */
    public abstract View setContentView();

    /**
     * Dialog点击事件
     *
     * @param view
     */
    public void viewClick(View view) {
    }

    /**
     * 确定事件监听
     *
     * @param listener
     */
    protected void setConfirmListener(IConfirmListener listener) {
        iConfirmListener = listener;
    }

    /**
     * 取消事件监听
     *
     * @param listener
     */
    protected void setCancelListener(ICancelListener listener) {
        iCancelListener = listener;
    }

    public BaseDialog(Context context) {
        this(context, 0);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    /**
     * 外部方法，显示对话框
     */
    @Override
    public void show() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);

        view = setContentView();
        initDialog();

        super.show();
        getWindow().setLayout(size.x * 9 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(int title) {
        if (null == mContext) {
            return;
        }
        String titleStr = mContext.getResources().getString(title);
        if (!TextUtils.isEmpty(titleStr)) {
            mTitle.setText(titleStr);
        }
    }

    /**
     * 设置提示信息
     *
     * @param msg
     */
    public void setMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            mMsg.setText(msg);
        }
    }

    /**
     * 设置提示信息
     *
     * @param msg
     */
    public void setMsg(int msg) {
        if (null == mContext) {
            return;
        }
        String msgStr = mContext.getResources().getString(msg);
        if (!TextUtils.isEmpty(msgStr)) {
            mMsg.setText(msgStr);
        }
    }

    @Override
    public void onClick(View v) {
        viewClick(v);
    }
}
