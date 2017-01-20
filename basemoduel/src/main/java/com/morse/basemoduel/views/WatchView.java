package com.morse.basemoduel.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/1/19.
 */

public class WatchView extends View {

    private Paint paint;
    private Calendar calendar;
    private int r;

    public WatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        calendar = Calendar.getInstance();
    }

    public void run() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                postInvalidate();
            }
        }, 0, 1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getPaddingLeft() + 2 * r + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getPaddingBottom() + 2 * r + getPaddingTop();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取组件宽
        int width = this.getMeasuredWidth();
        //获取组件高
        int height = this.getMeasuredHeight();
        //计算圆盘半径
        int len = Math.min(width, height);
        r = len / 2;
        //绘制圆盘
        drawPlate(canvas);
        //绘制指针
        drawPoints(canvas, len);
    }

    /**
     * 绘制圆盘
     *
     * @param canvas 画布
     */
    protected void drawPlate(Canvas canvas) {
        canvas.save();
        //画圆
        canvas.drawCircle(r, r, r, paint);
        //画60个刻度
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                //长刻度，占半径的十分之一
                paint.setColor(Color.RED);
                paint.setStrokeWidth(4);
                canvas.drawLine(r + 9 * r / 10, r, 2 * r, r, paint);
            } else {
                //短刻度占半径十五分之一
                paint.setColor(Color.GRAY);
                paint.setStrokeWidth(1);
                canvas.drawLine(r + 14 * r / 15, r, 2 * r, r, paint);
            }
            //以(r,r)为圆心，将画布选择6度
            canvas.rotate(6, r, r);
        }
        canvas.restore();
    }

    /**
     * 绘制指针
     *
     * @param canvas 画布
     * @param len    半径
     */
    protected void drawPoints(Canvas canvas, int len) {
        //获取系统时间
        calendar.setTimeInMillis(System.currentTimeMillis());
        //获取时分秒
        int hours = calendar.get(Calendar.HOUR) % 12;
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        //画时针，角度
        int degree = 360 / 12 * hours;
        //转换成弧度
        double radians = Math.toRadians(degree);
        //根据当前时间的时针位置
        int startX = r;
        int startY = r;
        int endX = (int) (startX + r * 0.5 * Math.cos(radians));
        int endY = (int) (startY + r * 0.5 * Math.sin(radians));
        canvas.save();
        paint.setStrokeWidth(3);
        canvas.rotate(-90, r, r);
        canvas.drawLine(startX, startY, endX, endY, paint);
        canvas.restore();

        //绘制分针，角度
        degree = 360 / 60 * minutes;
        radians = Math.toRadians(degree);
        endX = (int) (startX + r * 0.6 * Math.cos(radians));
        endY = (int) (startY + r * 0.6 * Math.sin(radians));
        canvas.save();
        paint.setStrokeWidth(2);
        //0度从3点开始，时间从12点开始，将画布选择90度
        canvas.rotate(-90, r, r);
        canvas.drawLine(startX, startY, endX, endY, paint);
        canvas.restore();

        //绘制秒针
        degree = 360 / 60 * seconds;
        radians = Math.toRadians(degree);
        endX = (int) (startX + r * 0.8 * Math.cos(radians));
        endY = (int) (startY + r * 0.8 * Math.sin(radians));
        canvas.save();
        paint.setStrokeWidth(1);
        canvas.rotate(-90, r, r);
        canvas.drawLine(startX, startY, endX, endY, paint);
        radians = Math.toRadians(degree - 180);
        endX = (int) (startX + r * 0.15 * Math.cos(radians));
        endY = (int) (startY + r * 0.15 * Math.sin(radians));
        canvas.drawLine(startX, startY, endX, endY, paint);
        canvas.restore();
    }
}
