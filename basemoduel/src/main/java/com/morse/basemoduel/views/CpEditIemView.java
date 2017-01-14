package com.morse.basemoduel.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.morse.basemoduel.R;
import com.morse.basemoduel.interfaces.IBmzgView;
import com.morse.basemoduel.interfaces.OnCpViewClickListener;

/**
 * 由一个textview、一个editview和一个button组成的控件
 */
public class CpEditIemView extends LinearLayout implements View.OnClickListener, IBmzgView, TextWatcher {

    private TextView tvTitle;
    private EditText etVal;
    private Button btn;
    private OnCpViewClickListener mCpViewClickListener;

    private float topLineStart = -1, bottomeLineStart = -1;
    private float topLineEnd = -1, bottomLineEnd = -1;
    private boolean isWatchText;

    public void setWatchText(boolean watchText) {
        isWatchText = watchText;
        if (isWatchText && getVal().length() <= 0) {
            enableBtn(false);
        }
    }

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CpEditIemView(Context context) {
        this(context, null);
    }

    public CpEditIemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CpEditIemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public void setTopLineStart(int topLineStart) {
        this.topLineStart = topLineStart;
        invalidate();
    }

    public void setBottomeLineStart(int bottomeLineStart) {
        this.bottomeLineStart = bottomeLineStart;
        invalidate();
    }

    private void initView(Context context, AttributeSet attrs) {
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);

        setWillNotDraw(false);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.include_cp_edit_item, this);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        etVal = (EditText) view.findViewById(R.id.et_val);
        btn = (Button) view.findViewById(R.id.btn);

        int id = (int) System.currentTimeMillis();
        etVal.setId(id);

        TypedArray mType = context.obtainStyledAttributes(attrs, R.styleable.CpEditItemStyle);
        String title = mType.getString(R.styleable.CpEditItemStyle_cti_title);
        String val = mType.getString(R.styleable.CpEditItemStyle_cti_val);
        String hint = mType.getString(R.styleable.CpEditItemStyle_cti_hint);
        float defaultTextSize = getResources().getDimensionPixelSize(R.dimen.default_font_size);
        int defaultTextColor = getResources().getColor(R.color.default_text_color);
        String btnContent = mType.getString(R.styleable.CpEditItemStyle_cti_btn_content);
        float valTextSize = mType.getDimension(R.styleable.CpEditItemStyle_cti_val_text_size, defaultTextSize);
        int valTextColor = mType.getColor(R.styleable.CpEditItemStyle_cti_val_text_color, getResources().getColor(R.color.cp_edit_text_color));
        float btnTextSize = mType.getDimension(R.styleable.CpEditItemStyle_cti_btn_text_size, defaultTextSize);
        int btnTextColor = mType.getColor(R.styleable.CpEditItemStyle_cti_btn_text_color, defaultTextColor);
        float btnH = mType.getDimension(R.styleable.CpEditItemStyle_cti_btn_h, -1);

        float titleTextSize = mType.getDimension(R.styleable.CpEditItemStyle_cti_title_text_size, defaultTextSize);
        int titleTextColor = mType.getColor(R.styleable.CpEditItemStyle_cti_title_text_color, defaultTextColor);

        boolean enableBtn = mType.getBoolean(R.styleable.CpEditItemStyle_cti_btn_enable, false);
        Drawable btnImg = mType.getDrawable(R.styleable.CpEditItemStyle_cti_btn_img);
        Drawable btnBg = mType.getDrawable(R.styleable.CpEditItemStyle_cti_btn_bg);
        float btnPaddingLeft = mType.getDimension(R.styleable.CpEditItemStyle_cti_btn_padding_left, btn.getPaddingLeft());
        float btnPaddingRight = mType.getDimension(R.styleable.CpEditItemStyle_cti_btn_padding_right, btn.getPaddingRight());
        float btnDrawPadding = mType.getDimension(R.styleable.CpEditItemStyle_cti_btn_drawable_padding, btn.getCompoundDrawablePadding());

        boolean editEnable = mType.getBoolean(R.styleable.CpEditItemStyle_cti_edit_enable, true);

        topLineStart = mType.getDimension(R.styleable.CpEditItemStyle_cei_top_line_start, -1);
        bottomeLineStart = mType.getDimension(R.styleable.CpEditItemStyle_cei_bottome_line_start, -1);
        topLineEnd = mType.getDimension(R.styleable.CpEditItemStyle_cei_top_line_end, -1);
        bottomLineEnd = mType.getDimension(R.styleable.CpEditItemStyle_cei_bottom_line_end, -1);

        mType.recycle();

        tvTitle.setText(title);
        if (titleTextSize > 0) {
            //tvTitle.setTextSize(titleTextSize);
        }
        tvTitle.setTextColor(titleTextColor);

        etVal.setText(val);
        etVal.setEnabled(editEnable);
        if (valTextSize > 0) {
            //etVal.setTextSize(valTextSize);
        }
        if (!TextUtils.isEmpty(hint)) {
            etVal.setHint(hint);
        }
        etVal.setTextColor(valTextColor);
        etVal.addTextChangedListener(this);

        if (enableBtn || btnImg != null || !TextUtils.isEmpty(btnContent)) {
            btn.setVisibility(VISIBLE);
            if (!TextUtils.isEmpty(btnContent)) {
                btn.setText(btnContent);
                btn.setTextColor(btnTextColor);
                //btn.setTextSize(btnTextSize);
            }
            btn.setPadding((int) btnPaddingLeft, btn.getPaddingTop(), (int) btnPaddingRight, btn.getBottom());
            btn.setCompoundDrawablePadding((int) btnDrawPadding);
            btn.setCompoundDrawablesWithIntrinsicBounds(null, null, btnImg, null);
            if (btnH > 0) {
                LayoutParams lp = (LayoutParams) btn.getLayoutParams();
                lp.height = (int) btnH;
                btn.setLayoutParams(lp);
            }

            if (btnBg != null) {
                btn.setBackgroundDrawable(btnBg);
            }
            btn.setOnClickListener(this);
        } else {
            btn.setVisibility(GONE);
        }

        mPaint.setColor(getResources().getColor(R.color.default_line));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dip_0_7));
    }


    public void setOnCpViewClickListener(OnCpViewClickListener listener) {
        mCpViewClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (isWatchText && R.id.btn == v.getId() && getVal().length() >= 0) {
            setVal("");
            return;
        }
        // TODO Auto-generated method stub
        if (mCpViewClickListener != null) {
            mCpViewClickListener.onItemClick(v, v == this ? false : true);
        }
    }

    public String getVal() {
        return etVal.getText().toString();
    }

    public void setVal(CharSequence spannable) {
        etVal.setText(spannable);
    }

    public String getHint() {
        return etVal.getHint().toString();
    }

    public void setHint(String hint) {
        etVal.setHint(hint);
    }

    public void setVal(String val) {
        etVal.setText(val);
        etVal.setSelection(val == null ? 0 : getVal().length());
    }

    public void setValTextSize(int resId) {
        etVal.setTextSize(getResources().getDimension(resId));
    }

    public void setValTextColor(int resId) {
        etVal.setTextColor(getResources().getColor(resId));
    }

    public void setTitleTextSize(int resId) {
        tvTitle.setTextSize(getResources().getDimension(resId));
    }

    public void setTitleTextColor(int resId) {
        tvTitle.setTextColor(getResources().getColor(resId));
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setBtnTextSize(int resId) {
        enableBtn(true);
        btn.setTextSize(getResources().getDimension(resId));
    }

    public void setBtnTextColor(int resId) {
        enableBtn(true);
        btn.setTextColor(getResources().getColor(resId));
    }

    public void setBtnContent(String content) {
        enableBtn(true);
        btn.setText(content);
    }

    public void setBtnDrawable(int resId) {
        enableBtn(true);
        btn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(resId), null);
    }

    public void enableBtn(boolean flag) {
        if (flag) {
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.GONE);
        }
    }

    public void setInputFilter(InputFilter[] filters) {
        etVal.setFilters(filters);
    }

    public void setEditGravity(int gravity) {
        etVal.setGravity(gravity);
    }

    public void setInputType(int type) {
        etVal.setInputType(type);
    }

    public void setMaxLength(int length) {
        etVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    public void setDigits(String digits) {
    }

    public void addTextChangedListener(TextWatcher watcher) {
        etVal.addTextChangedListener(watcher);
    }

    public void setTtitleWidth(int width) {
        tvTitle.setWidth(width);
    }

    public int getTitleWidth() {
        return tvTitle.getWidth();
    }

    public Button getButton() {
        return btn;
    }

    public EditText getEditText() {
        return etVal;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setEditTextEnable(boolean flag) {
        etVal.setEnabled(flag);
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        if (topLineStart >= 0) {
            float end = getWidth() - (topLineEnd < 0 ? 0 : topLineEnd);
//            canvas.drawRect(topLineStart, 0, end, getResources().getDimensionPixelSize(R.dimen.line_h), mPaint);
            canvas.drawLine(topLineStart, 0, end, 0, mPaint);
        }

        if (bottomeLineStart >= 0) {
            float end = getWidth() - (bottomLineEnd < 0 ? 0 : bottomLineEnd);
//            canvas.drawRect(bottomeLineStart, getHeight() - getResources().getDimensionPixelSize(R.dimen.line_h), end, getHeight(), mPaint);
            canvas.drawLine(bottomeLineStart, getHeight(), end, getHeight(), mPaint);
        }
    }

    @Override
    public float getTopLineStart() {
        return topLineStart;
    }

    @Override
    public void setTopLineStart(float topLineStart) {
        this.topLineStart = topLineStart;
        invalidate();
    }

    @Override
    public float getBottomeLineStart() {
        return bottomeLineStart;
    }

    @Override
    public void setBottomeLineStart(float bottomeLineStart) {
        this.bottomeLineStart = bottomeLineStart;
        invalidate();
    }

    @Override
    public float getTopLineEnd() {
        return topLineEnd;
    }

    @Override
    public void setTopLineEnd(float topLineEnd) {
        this.topLineEnd = topLineEnd;
        invalidate();
    }

    @Override
    public float getBottomLineEnd() {
        return bottomLineEnd;
    }

    @Override
    public void setBottomLineEnd(float bottomLineEnd) {
        this.bottomLineEnd = bottomLineEnd;
        invalidate();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isWatchText) {
            if (s.length() <= 0) {
                enableBtn(false);
            } else {
                enableBtn(true);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
