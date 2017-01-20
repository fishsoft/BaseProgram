package com.morse.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.morse.basemoduel.media.AudioRecordRunable;
import com.morse.basemoduel.media.CameraRecorderRunable;
import com.morse.basemoduel.views.TemperatureView;
import com.morse.basemoduel.views.WatchView;
import com.morse.basemoduel.views.chart.ChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ChartView myChartView;
    //    private SingleView mMySingleChartView;
    private List<Float> chartList;
    private List<Float> singlelist;
    private RelativeLayout relativeLayout;
    //    private NoScrollListView lv;
//    private ChartAdapter mAdapter;
//    private List<MyBean> horList;
    private LinearLayout llChart;
    private LinearLayout llSingle;
    private RelativeLayout rlSingle;
    //    private HorzinonlChartView mHorzinonlChartView;
    private TemperatureView tempControl;


    private WatchView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        myChartView=(ChartView)findViewById(R.id.my_chart_view);

//        initChatView();
//        tempControl = (TemperatureView) findViewById(R.id.temp_control);
//        tempControl.setTemp(15, 30, 15);
//
//        tempControl.setOnTempChangeListener(new TemperatureView.OnTempChangeListener() {
//            @Override
//            public void change(int temp) {
//                Toast.makeText(MainActivity.this, temp + "°", Toast.LENGTH_SHORT).show();
//            }
//        });

        iv = (WatchView) findViewById(R.id.watch);

//        Bitmap bitmap=Bitmap.createBitmap(400,400,Bitmap.Config.ARGB_8888);
//        Canvas canvas=new Canvas(bitmap);
//        //绘制文字
//        Paint paint=new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setTextAlign(Paint.Align.LEFT);
//
//        int sp=14;
//        paint.setTextSize(sp);
//        paint.setTextSkewX(0.5f);
//        paint.setUnderlineText(true);
//        paint.setFakeBoldText(true);
//        canvas.drawText("Hello World!",10,100,paint);
//
//
//        //绘制图形
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.RED);
//        paint.setStrokeWidth(20);
//        paint.setStrokeJoin(Paint.Join.BEVEL);
//        canvas.drawRect(new Rect(10,200,350,350),paint);
//
//        iv.setImageBitmap(bitmap);
        iv.run();
    }

    private void initChatView() {

        myChartView.setLefrColorBottom(getResources().getColor(R.color.leftColorBottom));
        myChartView.setLeftColor(getResources().getColor(R.color.leftColor));
        myChartView.setRightColor(getResources().getColor(R.color.rightColor));
        myChartView.setRightColorBottom(getResources().getColor(R.color.rightBottomColor));
        myChartView.setSelectLeftColor(getResources().getColor(R.color.selectLeftColor));
        myChartView.setSelectRightColor(getResources().getColor(R.color.selectRightColor));
        myChartView.setLineColor(getResources().getColor(R.color.xyColor));
        chartList = new ArrayList<>();

//        relativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
//        relativeLayout.removeView(llChart);
        Random random = new Random();
        while (chartList.size() < 24) {
            int randomInt = random.nextInt(100);
            chartList.add((float) randomInt);
        }
        myChartView.setList(chartList);
        myChartView.setListener(new ChartView.GetNumberListener() {
            @Override
            public void getNumer(int number, int x, int y) {
                relativeLayout.removeView(llChart);
                //反射加载点击柱状图弹出布局
                llChart = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_shouru_zhichu, null);
                TextView tvZhichu = (TextView) llChart.findViewById(R.id.tv_zhichu);
                TextView tvShouru = (TextView) llChart.findViewById(R.id.tv_shouru);
                tvZhichu.setText((number + 1) + "月支出" + " " + chartList.get(number * 2));
                tvShouru.setText("收入: " + chartList.get(number * 2 + 1));
                llChart.measure(0, 0);//调用该方法后才能获取到布局的宽度
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = x - 100;
                if (x - 100 < 0) {
                    params.leftMargin = 0;
                } else if (x - 100 > relativeLayout.getWidth() - llChart.getMeasuredWidth()) {
                    //设置布局距左侧屏幕宽度减去布局宽度
                    params.leftMargin = relativeLayout.getWidth() - llChart.getMeasuredWidth();
                }
                llChart.setLayoutParams(params);
                relativeLayout.addView(llChart);
            }
        });
    }

    public void audioRecord(View view) {
        new AudioRecordRunable().run();
    }

    public void cameraRecord(View view) {
        new CameraRecorderRunable().run();
    }
}
