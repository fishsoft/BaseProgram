package com.morse.basemoduel.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.morse.basemoduel.R;
import com.morse.basemoduel.interfaces.IBmzgView;
import com.morse.basemoduel.interfaces.OnCpViewClickListener;

/**
 * 两个textview组成的控件
 */
public class CpTextItemView extends LinearLayout implements View.OnClickListener, IBmzgView {

    TextView mTvName;
    TextView mTvExtra;

    boolean extraEnable;

    Drawable leftIco, rightIco;

    OnCpViewClickListener mCpViewClickListener;

    float topLineStart = -1, bottomeLineStart = -1;

    float topLineEnd = -1, bottomLineEnd = -1;
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public CpTextItemView(Context context) {
        this(context, null);
    }

    public CpTextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CpTextItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    protected View inflaterView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.inclue_cp_text_item, this);
    }

    private void initView(Context context, AttributeSet attrs) {

        setWillNotDraw(false);

        View view = inflaterView();

        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvExtra = (TextView) view.findViewById(R.id.tv_extra);

        TypedArray mType = context.obtainStyledAttributes(attrs, R.styleable.CpTextItemStyle);
        leftIco = mType.getDrawable(R.styleable.CpTextItemStyle_left_ico);
        rightIco = mType.getDrawable(R.styleable.CpTextItemStyle_right_ico);
        String content = mType.getString(R.styleable.CpTextItemStyle_content);
        String extra = mType.getString(R.styleable.CpTextItemStyle_extra);
        float contentTextSize = mType.getDimension(R.styleable.CpTextItemStyle_content_text_size, -1);
        float extraTextSize = mType.getDimension(R.styleable.CpTextItemStyle_extra_text_size, -1);
        int contentTextColor = mType.getColor(R.styleable.CpTextItemStyle_content_text_color, getResources().getColor(R.color.default_text_color));
        int extraTextColor = mType.getColor(R.styleable.CpTextItemStyle_extra_text_color, getResources().getColor(R.color.subtitle));
        extraEnable = mType.getBoolean(R.styleable.CpTextItemStyle_extra_enable, false);
        topLineStart = mType.getDimension(R.styleable.CpTextItemStyle_cti_top_line_start, -1);
        bottomeLineStart = mType.getDimension(R.styleable.CpTextItemStyle_cti_bottome_line_start, -1);
        topLineEnd = mType.getDimension(R.styleable.CpTextItemStyle_cti_top_line_end, -1);
        bottomLineEnd = mType.getDimension(R.styleable.CpTextItemStyle_cti_bottom_line_end, -1);
        int extraTextGravity = mType.getInteger(R.styleable.CpTextItemStyle_cti_extra_text_gravity, Gravity.CENTER);
        mType.recycle();

        mTvName.setText(content);
        if (contentTextSize > 0) {
            mTvName.setTextSize(contentTextSize);
        }

        mTvName.setTextColor(contentTextColor);
        if (leftIco != null) {
            mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, null, null);
        }
        if (rightIco != null) {
            if (extraEnable) {
                mTvExtra.setCompoundDrawablesWithIntrinsicBounds(null, null, rightIco, null);
            } else {
                mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, rightIco, null);
            }
        }

        if (extraEnable) {
            mTvExtra.setText(extra);
            if (extraTextSize > 0) {
                mTvExtra.setTextSize(extraTextSize);
            }
            mTvExtra.setTextColor(extraTextColor);
        } else {
            mTvExtra.setVisibility(View.GONE);
        }

        mTvExtra.setGravity(extraTextGravity);


        setOnClickListener(this);

        mPaint.setColor(getResources().getColor(R.color.default_line));
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dip_0_7));
    }

    public String getContent() {
        return mTvName.getText().toString();
    }

    public void setContent(String content) {
        mTvName.setText(content);
    }

    public void setExtraContent(String content) {
        enableExtra(true);
        mTvExtra.setText(content);
    }

    public void setExtraContent(CharSequence content) {
        enableExtra(true);
        mTvExtra.setText(content);
    }

    public String getExtraContent() {
        return mTvExtra.getText().toString();
    }

    public void setExtraContent(int resId) {
        enableExtra(true);
        mTvExtra.setText(resId);
    }

    public TextView getExtraView() {
        return mTvExtra;
    }

    public void setContentColor(int color) {
        mTvName.setTextColor(color);
    }

    public void setContentColorResource(int resId) {
        if (resId <= 0)
            return;
        setContentColor(getResources().getColor(resId));
    }

    public void setDefaultExtraColor() {
        enableExtra(true);
        mTvExtra.setTextColor(getResources().getColor(R.color.subtitle));
    }

    public void setExtraResourceColor(int resId) {
        enableExtra(true);
        mTvExtra.setTextColor(resId);
        //setExtraColor(getResources().getColor(resId));
    }

    public void setExtraColor(int color) {
        enableExtra(true);
        mTvExtra.setTextColor(color);
    }

    public void setContentTextSizeResource(int resId) {
        setContentTextSize(getResources().getDimension(resId));
    }

    public void setContentTextSize(float size) {
        mTvName.setTextSize(size);
    }

    public void setExtraTextSizeResource(int resId) {
        enableExtra(true);
        setExtraTextSize(getResources().getDimension(resId));
    }

    public void setExtraTextSize(float size) {
        enableExtra(true);
        mTvExtra.setTextSize(size);
    }

    public void setLeftIco(int resId) {
        setLeftIco(resId == 0 ? null : getResources().getDrawable(resId));
    }

    public void setLeftIco(Drawable drawable) {
        leftIco = drawable;
        mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, null, null);
    }

    public void setRightIco(int resId) {
        setRightIco(resId == 0 ? null : getResources().getDrawable(resId));
    }

    public void setRightIco(Drawable drawable) {
        rightIco = drawable;
        if (extraEnable) {
            mTvExtra.setCompoundDrawablesWithIntrinsicBounds(null, null, rightIco, null);
        } else {
            mTvName.setCompoundDrawablesWithIntrinsicBounds(null, null, rightIco, null);
        }
    }

    public void hideLeftIco() {
        if (extraEnable) {
            mTvName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            mTvName.setCompoundDrawablesWithIntrinsicBounds(null, null, rightIco, null);
        }

    }

    public void hideRightIco() {
        mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, null, null);
        mTvExtra.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    public void showLeftIco() {
        mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, null, null);
    }

    public void showRightIco() {
        if (extraEnable) {
            mTvExtra.setCompoundDrawablesWithIntrinsicBounds(null, null, rightIco, null);
        } else {
            mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, rightIco, null);
        }
    }

    public void enableExtra(boolean enable) {
        extraEnable = enable;
        if (extraEnable) {
            mTvExtra.setVisibility(View.VISIBLE);
            mTvExtra.setCompoundDrawablesWithIntrinsicBounds(null, null, rightIco, null);
            mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, null, null);
        } else {
            mTvExtra.setVisibility(View.GONE);
            mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, rightIco, null);
        }
    }

    public void setOnCpViewClickListener(OnCpViewClickListener listener) {
        mCpViewClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mCpViewClickListener != null) {
            mCpViewClickListener.onItemClick(this, false);
        }
    }

    public TextView getContentView() {
        return mTvName;
    }

    public void setContentGravity(int gravity) {
        mTvName.setGravity(gravity);
    }


    /* (non-Javadoc)
     * @see android.view.View#draw(android.graphics.Canvas)
     */
    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        if (topLineStart >= 0) {
            float end = getWidth() - (topLineEnd < 0 ? 0 : topLineEnd);
//            canvas.drawRect(topLineStart, 0, end, getResources().getDimensionPixelSize(R.dimen.line_h), mPaint);
//            canvas.drawRect(topLineStart, 0, end, 1, mPaint);
            canvas.drawLine(topLineStart, 0, end, 0, mPaint);
        }

        if (bottomeLineStart >= 0) {
            float end = getWidth() - (bottomLineEnd < 0 ? 0 : bottomLineEnd);
            /*canvas.drawRect(bottomeLineStart, getHeight() - getResources().getDimensionPixelSize(R.dimen.line_h), end, getHeight(), mPaint);*/
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
}
