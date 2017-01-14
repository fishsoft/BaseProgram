package com.morse.basemoduel.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.morse.basemoduel.R;
import com.morse.basemoduel.interfaces.IBmzgView;
import com.morse.basemoduel.interfaces.OnCpViewClickListener;

/**
 * 由一个textview、一个imageview和一个textview组成的控件
 *
 */
public class CpImageItemView extends LinearLayout implements View.OnClickListener, IBmzgView {

    TextView mTvName,mTvRight;
    ImageView mIvImg;


    Drawable leftIco,rightIco;

    OnCpViewClickListener mCpViewClickListener;

    float topLineStart = -1, bottomeLineStart = -1;

    float topLineEnd = -1, bottomLineEnd = -1;

    public CpImageItemView(Context context) {
        this(context, null);
    }

    public CpImageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public CpImageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.include_cp_img_item, this);

        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvRight = (TextView) view.findViewById(R.id.tv_right);
        mIvImg = (ImageView) view.findViewById(R.id.iv_img);


        TypedArray mType = context.obtainStyledAttributes(attrs, R.styleable.CpImageItemStyle);
        leftIco = mType.getDrawable(R.styleable.CpImageItemStyle_cii_left_ico);
        rightIco = mType.getDrawable(R.styleable.CpImageItemStyle_cii_right_ico);
        String content = mType.getString(R.styleable.CpImageItemStyle_cii_content);
        float contentTextSize = mType.getDimension(R.styleable.CpImageItemStyle_cii_content_text_size, -1);
        int contentTextColor = mType.getColor(R.styleable.CpImageItemStyle_cii_content_text_color, Color.BLACK);
        int imgWidth = (int) mType.getDimension(R.styleable.CpImageItemStyle_cii_img_width, -1);
        int imgHeight = (int) mType.getDimension(R.styleable.CpImageItemStyle_cii_img_height, -1);
        Drawable imgDrawable = mType.getDrawable(R.styleable.CpImageItemStyle_cii_img);
        mType.recycle();

        mTvName.setText(content);
        if(contentTextSize > 0){
            mTvName.setTextSize(contentTextSize);
        }
        

        mTvName.setTextColor(contentTextColor);
        if(leftIco != null){
            mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, null, null);
        }
        if(rightIco != null){
            mTvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, rightIco, null);
        }else{
        	mTvRight.setVisibility(View.GONE);
        }
        
        if(imgDrawable != null){
        	mIvImg.setImageDrawable(imgDrawable);
        }
        
        if(imgWidth > 0 && imgHeight > 0){
        	LayoutParams llp =  (LayoutParams) mIvImg.getLayoutParams();
        	llp.width = imgWidth;
        	llp.height = imgHeight;
        	mIvImg.setLayoutParams(llp);
        }
        
        setOnClickListener(this);

    }

    public void setContent(String content){
        mTvName.setText(content);
    }

    public String getContent(){
        return mTvName.getText().toString();
    }

    public void setContentColor(int color){
        if(color <= 0) return;
        mTvName.setTextColor(color);
    }

    public void setContentColorResource(int resId){
        if(resId <= 0) return;
        setContentColor(getResources().getColor(resId));
    }


    public void setContentTextSizeResource(int resId){
        if(resId <= 0 )return;
        setContentTextSize(getResources().getDimension(resId));
    }

    public void setContentTextSize(float size){
        if(size <= 0 )return;
        mTvName.setTextSize(size);
    }

    public void setLeftIco(int resId){
        setLeftIco(getResources().getDrawable(resId));
    }

    public void setLeftIco(Drawable drawable){
        leftIco = drawable;
        mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, null, null);
    }

    public void setRightIco(int resId){
        setRightIco(getResources().getDrawable(resId));
    }

    public void setRightIco(Drawable drawable){
        rightIco = drawable;
        mTvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, null, rightIco);
    }

    public void hideLeftIco(){
        mTvName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

    }

    public void hideRightIco(){
    	mTvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    public void showLeftIco(){
        mTvName.setCompoundDrawablesWithIntrinsicBounds(leftIco, null, null, null);
    }

    public void showRightIco(){
    	mTvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, rightIco, null);
    }
    
    public void setImg(Bitmap bitmap){
    	if(bitmap != null){
    		mIvImg.setScaleType(ScaleType.FIT_XY);
    		mIvImg.setImageBitmap(bitmap);
    		
    	}
    }
    
    public void setImg(Drawable drawable){
    	if(drawable != null){
    		mIvImg.setImageDrawable(drawable);
    	}
    }
    
    public ImageView getImageView(){
    	return mIvImg;
    }

    public void setOnCpViewClickListener(OnCpViewClickListener listener){
    	mCpViewClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(mCpViewClickListener != null){
        	mCpViewClickListener.onItemClick(this,false);
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
