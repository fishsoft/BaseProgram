package com.morse.basemoduel.views;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.morse.basemoduel.R;

/**
 * Author: Morse
 * Time: 2016/12/12 09:34
 * Company：bmsh
 * Deprecated：可移动的view
 */
public class DragView extends FrameLayout {

    private ViewDragHelper mDragger;
    private ViewDragHelper.Callback callback;
    private View dragView;
    private boolean isVerticalMove;
    private Point mAutoBackOriginPos = new Point();
    private boolean isLeft;
    private boolean isFirst = true;
    private int mLeft, mTop;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        callback = new DraggerCallBack();
        //第二个参数就是滑动灵敏度的意思 可以随意设置
        mDragger = ViewDragHelper.create(this, 1.0f, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        dragView = findViewById(R.id.tv_cash_back);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //决定是否拦截当前事件
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //处理事件
        mDragger.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isFirst) {
            mAutoBackOriginPos.x = dragView.getLeft();
            mAutoBackOriginPos.y = dragView.getTop();
            //初始化默认位置
            mLeft = mAutoBackOriginPos.x;
            mTop = mAutoBackOriginPos.y;
            isFirst = false;
        }
        //设置view的位置
        dragView.layout(mLeft, mTop, mLeft + dragView.getMeasuredWidth(), mTop + dragView.getMeasuredHeight());
    }


    /**
     * 滚动
     */
    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    public void showDragView() {
        if (View.VISIBLE != dragView.getVisibility()) {
            dragView.setVisibility(VISIBLE);
        }
    }

    public void hideDragView() {
        if (GONE != dragView.getVisibility()) {
            dragView.setVisibility(GONE);
        }
    }

    public void setVerticalMove(boolean verticalMove) {
        isVerticalMove = verticalMove;
    }

    class DraggerCallBack extends ViewDragHelper.Callback {

        //这个地方实际上函数返回值为true就代表可以滑动 为false 则不能滑动
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        //这个地方实际上left就代表 你将要移动到的位置的坐标。返回值就是最终确定的移动的位置。
        // 我们要让view滑动的范围在我们的layout之内
        //实际上就是判断如果这个坐标在layout之内 那我们就返回这个坐标值。
        //如果这个坐标在layout的边界处 那我们就只能返回边界的坐标给他。不能让他超出这个范围
        //除此之外就是如果你的layout设置了padding的话，也可以让子view的活动范围在padding之内的.
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //取得左边界的坐标
            final int leftBound = getPaddingLeft();
            //取得右边界的坐标
            final int rightBound = getWidth() - child.getWidth() - leftBound;
            //这个地方的含义就是 如果left的值 在leftbound和rightBound之间 那么就返回left
            //如果left的值 比 leftbound还要小 那么就说明 超过了左边界 那我们只能返回给他左边界的值
            //如果left的值 比rightbound还要大 那么就说明 超过了右边界，那我们只能返回给他右边界的值
            return Math.min(Math.max(left, leftBound), rightBound);
        }

        //纵向的注释就不写了 自己体会
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - child.getHeight() - topBound;
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        //需要子view的点击事件时添加，并且在xml文件中设置click属性为true
        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        //需要子view的点击事件时添加，并且在xml文件中设置click属性为true
        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //手指释放时可以自动回去，实现回弹需要配合computeScroll方法的配合才能生效
            //如果移动的位置大于屏幕的一半，那么就回到右侧，否则移动到左侧
            if (releasedChild == dragView) {
                isLeft = dragView.getLeft() + dragView.getWidth() / 2 - getMeasuredWidth() / 2 < 0;
                //记住释放之后view的最终位置
                mLeft = isLeft ? 0 : mAutoBackOriginPos.x;
                mTop = isVerticalMove ? dragView.getTop() : mAutoBackOriginPos.y;
                mDragger.settleCapturedViewAt(mLeft, mTop);
//                dragView.setBackgroundResource(isLeft ? R.drawable.icon_cash_back_left : R.drawable.icon_cash_back);
                invalidate();
            }
        }

        //更换图片
        @Override
        public void onViewDragStateChanged(int state) {
            switch (state){
                case ViewDragHelper.STATE_DRAGGING:
//                    dragView.setBackgroundResource(R.drawable.icon_cash_back_draging);
                    break;
                case ViewDragHelper.STATE_IDLE:
//                    dragView.setBackgroundResource(isLeft ? R.drawable.icon_cash_back_left : R.drawable.icon_cash_back);
                    break;
            }
            super.onViewDragStateChanged(state);
        }
    }

}
