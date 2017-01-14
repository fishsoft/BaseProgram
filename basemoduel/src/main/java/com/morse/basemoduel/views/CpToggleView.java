package com.morse.basemoduel.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.morse.basemoduel.R;
import com.morse.basemoduel.interfaces.IBmzgView;
import com.morse.basemoduel.interfaces.OnCpViewClickListener;

/**
 * @company: bmsh
 *
 * @author: weiyx 2016-8-4下午6:49:27
 *
 * @description:
 *
 */
public class CpToggleView extends RadioGroup implements View.OnClickListener, IBmzgView {

    TextView mTvName;
    ImageButton mRbBtn;

    OnCpViewClickListener mCpViewClickListener;

    private boolean checked;

    private boolean enableAutoChgStatus;

    float topLineStart = -1,bottomeLineStart = -1;

    float topLineEnd = -1, bottomLineEnd = -1;

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CpToggleView(Context context) {
        this(context, null);
    }

    public CpToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.include_cp_toggle_item, this);

        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mRbBtn = (ImageButton) view.findViewById(R.id.rb_toggle);

        TypedArray mType = context.obtainStyledAttributes(attrs, R.styleable.CpTextItemStyle);
        String content = mType.getString(R.styleable.CpTextItemStyle_content);
        float contentTextSize = mType.getDimension(R.styleable.CpTextItemStyle_content_text_size, -1);
        int contentTextColor = mType.getColor(R.styleable.CpTextItemStyle_content_text_color, getResources().getColor(R.color.default_text_color));
        checked = mType.getBoolean(R.styleable.CpTextItemStyle_cti_checked, false);

        topLineStart = mType.getDimension(R.styleable.CpTextItemStyle_cti_top_line_start, -1);
        bottomeLineStart = mType.getDimension(R.styleable.CpTextItemStyle_cti_bottome_line_start, -1);
        topLineEnd = mType.getDimension(R.styleable.CpTextItemStyle_cti_top_line_end, -1);
        bottomLineEnd = mType.getDimension(R.styleable.CpTextItemStyle_cti_bottom_line_end, -1);

        mType.recycle();

        mTvName.setText(content);
        if(contentTextSize > 0){
            mTvName.setTextSize(contentTextSize);
        }

        mTvName.setTextColor(contentTextColor);

        onCheckedChange();

        mRbBtn.setOnClickListener(this);

        mPaint.setColor(getResources().getColor(R.color.default_line));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dip_0_7));
    }

    public void setContent(String content){
        mTvName.setText(content);
    }

    public String getContent(){
        return mTvName.getText().toString();
    }


    public void setContentColor(int color){
        mTvName.setTextColor(color);
    }

    public void setContentColorResource(int resId){
        if(resId <= 0) return;
        setContentColor(getResources().getColor(resId));
    }

    public void setContentTextSizeResource(int resId){
        setContentTextSize(getResources().getDimension(resId));
    }

    public void setContentTextSize(float size){
        mTvName.setTextSize(size);
    }


    public void setOnCpViewClickListener(OnCpViewClickListener listener){
    	mCpViewClickListener = listener;
    }

    @Override
    public void onClick(View v) {
    	//mRbBtn.setChecked(!mRbBtn.isChecked());
    	if(enableAutoChgStatus){
    		checked = !checked;
        	onCheckedChange();
    	}
        if(mCpViewClickListener != null){
        	mCpViewClickListener.onItemClick(this,false);
        }
    }

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
		onCheckedChange();
	}

	private void onCheckedChange(){
		if(checked){
			mRbBtn.setImageResource(R.drawable.icon_cb_open);
		}else{
			mRbBtn.setImageResource(R.drawable.icon_cb_close);
		}
	}

	/**
	 * @return the enableAutoChgStatus
	 */
	public boolean isEnableAutoChgStatus() {
		return enableAutoChgStatus;
	}

	/**
	 * @param enableAutoChgStatus the enableAutoChgStatus to set
	 */
	public void setEnableAutoChgStatus(boolean enableAutoChgStatus) {
		this.enableAutoChgStatus = enableAutoChgStatus;
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
}
