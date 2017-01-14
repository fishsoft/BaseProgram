package com.morse.basemoduel.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.morse.basemoduel.R;


/**
 * 由一个textview、editview和一个textview组成的控件
 * 例如在一些app中有注册时需要发送短信
 */
public class CpEditView extends LinearLayout implements View.OnClickListener {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    tvExtraText.setText(0 == msg.arg2 ? "获取" : msg.arg2 + "秒重新获取");
                    break;
            }
        }
    };
    TextView tvLabel, tvExtraText;
    EditText etEdit;
    int extra = 60;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.obtainMessage(1, 1, extra);
            extra--;
        }
    };
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CpEditView(Context context) {
        this(context, null);
    }

    public CpEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CpEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cp_edit, null);
        tvLabel = (TextView) view.findViewById(R.id.tv_cp_label);
        etEdit = (EditText) view.findViewById(R.id.et_cp_edit);
        tvExtraText = (TextView) view.findViewById(R.id.tv_extra_text);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CpEditView);
        String edit = array.getString(R.styleable.CpEditView_cp_edit_text);
        String label = array.getString(R.styleable.CpEditView_cp_edit_label);
        String extraString = array.getString(R.styleable.CpEditView_cp_edit_extra);
        String hint = array.getString(R.styleable.CpEditView_cp_edit_hint);

        array.recycle();

        tvLabel.setText(TextUtils.isEmpty(label) ? "" : label);
        etEdit.setText(TextUtils.isEmpty(edit) ? "" : edit);
        etEdit.setHint(TextUtils.isEmpty(hint) ? "" : hint);
        tvExtraText.setText(TextUtils.isEmpty(extraString) ? "" : extraString);
        tvExtraText.setVisibility(TextUtils.isEmpty(extraString) ? View.VISIBLE : View.GONE);

        mPaint.setColor(getResources().getColor(R.color.default_line));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dip_0_7));
    }

    public void setContent(String content) {
        tvLabel.setText(!TextUtils.isEmpty(content) ? content : "");
    }

    public void setContent(int resId) {
        tvLabel.setText(0 >= resId ? "" : getResources().getString(resId));
    }

    public void setEdit(String edit) {
        etEdit.setText(!TextUtils.isEmpty(edit) ? edit : "");
    }

    public String getEditText() {
        return etEdit.getText().toString();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_extra_text) {
            if (0 == extra) {
                handler.removeCallbacks(runnable);
                extra = 60;
                return;
            }
            handler.postDelayed(runnable, 1000);
        }
    }
}
