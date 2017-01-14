package com.morse.basemoduel.views.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.morse.basemoduel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */

public class ChartView extends View {

    private int leftColor;//双柱左侧
    private int rightColor;//双柱右侧
    private int lineColor;//横轴线
    private int selectLeftColor;//点击选中左侧
    private int selectRightColor;//点击选中右侧
    private int leftColorBottom;//左侧底部
    private int rightColorBottom;//右侧底部
    private Paint mPaint, mChartPaint, mShadowPaint;//横轴画笔、柱状图画笔、阴影画笔
    private int mWidth, mHeight, mStartWidth, mChartWidth, mSize;//屏幕宽度高度、柱状图起始位置、柱状图宽度
    private Rect mBound;
    private List<Float> list = new ArrayList<>();//柱状图高度占比
    private Rect rect;//柱状图矩形
    private GetNumberListener listener;//点击接口
    private int number = 1000;//柱状图最大值
    private int selectIndex = -1;//点击选中柱状图索引
    private List<Integer> selectIndexRoles = new ArrayList<>();

    public interface GetNumberListener {
        void getNumer(int i, int x, int y);
    }

    public void setList(List<Float> list) {
        this.list = list;
        mSize = getWidth() / 39;
        mStartWidth = getWidth() / 33;
        invalidate();
    }

    public void setListener(GetNumberListener listener) {
        this.listener = listener;
    }

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChartView);

        leftColor = array.getColor(R.styleable.ChartView_leftColor, Color.BLACK);
        rightColor = array.getColor(R.styleable.ChartView_rightColor, Color.BLACK);
        selectLeftColor = array.getColor(R.styleable.ChartView_selectLeftColor, Color.BLACK);
        selectRightColor = array.getColor(R.styleable.ChartView_selectRightColor, Color.BLACK);
        leftColorBottom = array.getColor(R.styleable.ChartView_leftColorBottom, Color.BLACK);
        rightColorBottom = array.getColor(R.styleable.ChartView_rightColorBottom, Color.BLACK);
        lineColor = array.getColor(R.styleable.ChartView_xyColor, Color.BLACK);

        array.recycle();

        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBound = new Rect();
        mChartPaint = new Paint();
        mChartPaint.setAntiAlias(true);
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        width = widthMode == MeasureSpec.EXACTLY ? widthSize : widthSize / 2;
        height = heightMode == MeasureSpec.EXACTLY ? heightSize : heightSize / 2;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mStartWidth = getWidth() / 13;
        mSize = getWidth() / 39;
        mChartWidth = getWidth() / 13 - mSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(lineColor);

        //坐标轴
        for (int i = 0; i < 12; i++) {
            //画数字
            mPaint.setTextSize(20);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.getTextBounds(String.valueOf(i + 1) + "", 0, String.valueOf(i).length(), mBound);
            canvas.drawText(String.valueOf(i + 1) + "月", mStartWidth - mBound.width() / 2, mHeight - 60 + mBound.height() / 2, mPaint);
            mStartWidth += getWidth() / 13;
        }

        //画柱状图
        for (int i = 0; i < list.size(); i++) {
            int size = mHeight / 120;
            if (selectIndexRoles.contains(i)) {
                mChartPaint.setShader(null);
                mChartPaint.setColor(i % 2 == 0 ? selectLeftColor : selectRightColor);
            } else {
                //偶数
                if (i % 2 == 0) {
                    LinearGradient lg = new LinearGradient(mChartWidth, mChartWidth + mSize, mHeight - 100,
                            (mHeight - 100 - list.get(i) * size), leftColorBottom, leftColor, Shader.TileMode.MIRROR);
                    mChartPaint.setShader(lg);
                } else {
                    LinearGradient lg = new LinearGradient(mChartWidth, mChartWidth + mSize, mHeight - 100,
                            (mHeight - 100 - list.get(i) * size), rightColorBottom, rightColor, Shader.TileMode.MIRROR);
                    mChartPaint.setShader(lg);
                }
            }

            mChartPaint.setStyle(Paint.Style.FILL);
            //画阴影
            mShadowPaint.setColor(i == number * 2 || i == number * 2 + 1 ? Color.BLUE : Color.WHITE);

            //画柱状图
            RectF rectf = new RectF();
            rectf.left = mChartWidth;
            rectf.right = mChartWidth + mSize;
            rectf.bottom = mHeight - 100;
            rectf.top = (mHeight - 100 - list.get(i) * size);
            canvas.drawRoundRect(rectf, 10, 10, mChartPaint);
            mChartWidth += (i % 2 == 0) ? (3 + getWidth() / 39) : (getWidth() / 13 - 3 - mSize);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            mSize = getWidth() / 39;
            mStartWidth = getWidth() / 13;
            mChartWidth = getWidth() / 13 - mSize - 3;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int left = 0;
        int top = 0;
        int right = mWidth / 12;
        int bottom = mHeight - 100;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < 12; i++) {
                    rect = new Rect(left, top, right, bottom);
                    left += mWidth / 12;
                    right += mWidth / 12;
                    if (rect.contains(x, y)) {
                        listener.getNumer(i, x, y);
                        number = i;
                        selectIndex = i;
                        selectIndexRoles.clear();
                        selectIndexRoles.add(selectIndex * 2 + 1);
                        selectIndexRoles.add(selectIndex * 2);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    public int getLeftColor() {
        return leftColor;
    }

    public void setLeftColor(int leftColor) {
        this.leftColor = leftColor;
    }

    public int getRightColor() {
        return rightColor;
    }

    public void setRightColor(int rightColor) {
        this.rightColor = rightColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getSelectLeftColor() {
        return selectLeftColor;
    }

    public void setSelectLeftColor(int selectLeftColor) {
        this.selectLeftColor = selectLeftColor;
    }

    public int getSelectRightColor() {
        return selectRightColor;
    }

    public void setSelectRightColor(int selectRightColor) {
        this.selectRightColor = selectRightColor;
    }

    public int getLefrColorBottom() {
        return leftColorBottom;
    }

    public void setLefrColorBottom(int lefrColorBottom) {
        this.leftColorBottom = lefrColorBottom;
    }

    public int getRightColorBottom() {
        return rightColorBottom;
    }

    public void setRightColorBottom(int rightColorBottom) {
        this.rightColorBottom = rightColorBottom;
    }
}
