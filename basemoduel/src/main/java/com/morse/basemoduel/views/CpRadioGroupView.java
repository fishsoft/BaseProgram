package com.morse.basemoduel.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.morse.basemoduel.R;

/**
 * Created by weiyx on 2016/9/5.
 * Company: bmsh
 * Description:
 */
public class CpRadioGroupView extends RadioGroup implements RadioGroup.OnCheckedChangeListener {
    Paint mPaint;
    public static final int TOP = 0;
    public static final int BOTTOM = 1;

    int director = BOTTOM;

    int itemWidth;

    int screenW;

    int itemH;

    int blurH;

    int itemSpace;

    int showCount = 2;

    int focusColor = 0xFFFE4249;

    int blurColor;

    int currPos = 0;

    OnCpCheckedChangedListener mListener;

    public static float MODE_PERCENT_10 = 0.1f;
    public static float MODE_PERCENT_20 = 0.2f;
    public static float MODE_PERCENT_30 = 0.3f;
    public static float MODE_PERCENT_40 = 0.4f;
    public static float MODE_PERCENT_50 = 0.5f;
    public static float MODE_PERCENT_60 = 0.6f;
    public static float MODE_PERCENT_70 = 0.7f;
    public static float MODE_PERCENT_80 = 0.8f;
    public static float MODE_PERCENT_90 = 0.9f;
    public static float MODE_PERCENT_100 = 1.0f;

    private float currMode = MODE_PERCENT_80;

    CharSequence[] itemContents = null;

    public CpRadioGroupView(Context context) {
        this(context, null);
    }

    public CpRadioGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        boolean isVSplitEnable = true;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CpRadioGroupViewStyle);

        itemContents = a.getTextArray(R.styleable.CpRadioGroupViewStyle_crg_texts);
        isVSplitEnable = a.getBoolean(R.styleable.CpRadioGroupViewStyle_crg_v_split_enable, true);
        a.recycle();

        showCount = itemContents == null ? 0 : itemContents.length;

        blurColor = getResources().getColor(R.color.default_line);
        focusColor = getResources().getColor(R.color.red);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        screenW = metrics.widthPixels;
        itemH = (int) getResources().getDimension(R.dimen.dip_2);
        blurH = (int) getResources().getDimension(R.dimen.dip_2);
        itemSpace = 0;
        itemWidth = showCount < 1 ? 0 : screenW / showCount;

        setOnCheckedChangeListener(this);
    }

    private void initView(CharSequence[] itemContents, boolean isVSplitEnable, AttributeSet attrs) {
        if (showCount < 1) return;

        RadioButton radioButton = null;
        View line = null;
        for (int i = 0; i < showCount; i++) {
            radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.include_radio_button, null);
            radioButton.setId(i);
            addView(radioButton);
            if (isVSplitEnable && i != showCount - 1) {
                line = LayoutInflater.from(getContext()).inflate(R.layout.include_cp_radio_groud_v_split, null);
            }
            radioButton.setText(itemContents[i]);
            if (i == 0) {
                radioButton.setChecked(true);
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        View view = null;
        int j = 0;
        for (int i = 0; i < count; i++) {
            view = getChildAt(i);
            if (view instanceof RadioButton) {
                view.setId(j);
                if (j == 0) {
                    ((RadioButton) view).setChecked(true);
                }
                if (itemContents != null && j < itemContents.length) {
                    ((RadioButton) view).setText(itemContents[j]);
                }
                j++;
            }
        }
        showCount = j;
        check(currPos);
        itemContents = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    private int getCurrentItem() {
        return getCheckedRadioButtonId();
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();

        if (showCount == 0) {
            canvas.restore();
            return;
        }

        currPos = getCurrentItem();

        if (currPos >= showCount) {
            currPos = showCount - 1;
        }

        if (currPos < 0) currPos = 0;

        if (director == BOTTOM) {
            drawBottomIndicator(canvas, currPos);
        } else {
            drawTopIndicator(canvas, currPos);
        }

        canvas.restore();
    }

    public void drawTopIndicator(Canvas canvas, int currPos) {
        int left = itemWidth * currPos;
        int right = left + itemWidth;
        int top = 0;
        int bottom = top + blurH;
        mPaint.setColor(blurColor);
        canvas.drawRect(new Rect(0, top, getWidth(), bottom), mPaint);

        bottom = top + blurH;
        mPaint.setColor(focusColor);
        int subItemW = (int) (itemWidth * currMode);
        left = left + (itemWidth - subItemW) / 2 + itemSpace * currPos;
        right = (int) (left + itemWidth * currMode);
        canvas.drawRect(new Rect(left, top, right, bottom), mPaint);
    }

    public void drawBottomIndicator(Canvas canvas, int currPos) {
        int left = itemWidth * currPos;
        int right = left + itemWidth;
        int top = getHeight() - blurH;
        int bottom = top + itemH;
        mPaint.setColor(blurColor);
        canvas.drawRect(new Rect(0, top, getWidth(), bottom), mPaint);

        mPaint.setColor(focusColor);
        top = getHeight() - itemH;
        bottom = top + itemH;
        int subItemW = (int) (itemWidth * currMode);
        left = left + (itemWidth - subItemW) / 2 + itemSpace * currPos;
        right = (int) (left + itemWidth * currMode);
        canvas.drawRect(new Rect(left, top, right, bottom), mPaint);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        currPos = checkedId;
        invalidate();
        if (mListener != null) {
            mListener.onCheckedChanged(group, checkedId, checkedId);
        }
    }

    public void setCheckedChangedListener(OnCpCheckedChangedListener listener) {
        mListener = listener;
    }

    public interface OnCpCheckedChangedListener {
        public void onCheckedChanged(RadioGroup group, int checkedId, int position);
    }
}
