package com.morse.basemoduel.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.morse.basemoduel.ui.MBaseAdapter;
import com.morse.basemoduel.R;
import com.morse.basemoduel.interfaces.OnCpViewClickListener;

/**
 * 由一个textview一个、Spinner和一个textview组成的控件
 */
public class CpSelectView extends LinearLayout implements OnItemSelectedListener {

    TextView tvTitle, tvUnit;
    Spinner spVal;


    OnCpViewClickListener mCpViewClickListener;

    float itemTextSize;
    int itemTextColor;
    float itemH;
    CharSequence[] mDatas;

    //SpinnerAdapter mAdapter;

    ArrayAdapter<CharSequence> mAdapter;

    float topLineStart = -1, bottomeLineStart = -1;

    float topLineEnd = -1, bottomLineEnd = -1;

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CpSelectView(Context context) {
        this(context, null);
    }

    public CpSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CpSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        setWillNotDraw(false);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.include_cp_select_view, this);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvUnit = (TextView) view.findViewById(R.id.tv_unit);
        spVal = (Spinner) view.findViewById(R.id.sp_val);


        TypedArray mType = context.obtainStyledAttributes(attrs, R.styleable.CpSpinnerItemStyle);
        String title = mType.getString(R.styleable.CpSpinnerItemStyle_cpi_title);
        String unit = mType.getString(R.styleable.CpSpinnerItemStyle_cpi_unit);

        mDatas = mType.getTextArray(R.styleable.CpSpinnerItemStyle_cpi_arrays);
        ;

        float titleTextSize = mType.getDimension(R.styleable.CpSpinnerItemStyle_cpi_title_text_size, -1);
        int titleTextColor = mType.getColor(R.styleable.CpSpinnerItemStyle_cpi_title_text_color, getResources().getColor(R.color.default_text_color));
        itemH = mType.getDimension(R.styleable.CpSpinnerItemStyle_cpi_sp_item_h, 48);
        float unitTextSize = mType.getDimension(R.styleable.CpSpinnerItemStyle_cpi_unit_text_size, -1);
        int unitTextColor = mType.getColor(R.styleable.CpSpinnerItemStyle_cpi_unit_text_color, getResources().getColor(R.color.default_text_color));
        itemTextSize = mType.getDimension(R.styleable.CpSpinnerItemStyle_cpi_sp_item_text_size, -1);
        itemTextColor = mType.getColor(R.styleable.CpSpinnerItemStyle_cpi_sp_item_text_color, getResources().getColor(R.color.default_text_color));

        topLineStart = mType.getDimension(R.styleable.CpSpinnerItemStyle_cpi_top_line_start, -1);
        bottomeLineStart = mType.getDimension(R.styleable.CpSpinnerItemStyle_cpi_bottome_line_start, -1);
        topLineEnd = mType.getDimension(R.styleable.CpSpinnerItemStyle_cpi_top_line_end, -1);
        bottomLineEnd = mType.getDimension(R.styleable.CpSpinnerItemStyle_cpi_bottom_line_end, -1);

        mType.recycle();

        tvTitle.setText(title);
        if (titleTextColor > 0) {
            tvTitle.setTextSize(titleTextSize);
        }
        tvTitle.setTextColor(titleTextColor);

        if (!TextUtils.isEmpty(unit)) {
            tvUnit.setVisibility(View.VISIBLE);
            tvUnit.setText(unit);
            if (unitTextSize > 0) {
                tvUnit.setTextSize(unitTextSize);
            }
            tvUnit.setTextColor(unitTextColor);
        }

        // mAdapter = new SpinnerAdapter();
        // mAdapter.setDatas(new ArrayList<>(Arrays.asList(mDatas)));
        mAdapter = new ArrayAdapter<>(getContext(), R.layout.item_cp_spinner, mDatas);

        spVal.setAdapter(mAdapter);

        spVal.setOnItemSelectedListener(this);

        mPaint.setColor(getResources().getColor(R.color.default_line));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dip_0_7));


    }

    public void setDatas(String[] datas) {
        mAdapter.clear();
        mAdapter.addAll(datas);
        //mAdapter.setDatas(new ArrayList<>(Arrays.asList(mDatas)));
        mAdapter.notifyDataSetChanged();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public int getSelectedPos() {
        return spVal.getSelectedItemPosition();
    }

    public String getSelectedString() {
        return (String) spVal.getSelectedItem();
    }

    public Object getSelectedObj() {
        return (String) spVal.getSelectedItem();
    }

    public void setSelectPos(int pos) {
        if (pos > mAdapter.getCount() - 1) {
            pos = mAdapter.getCount() - 1;
        }

        if (pos < 0) {
            pos = 0;
        }
        spVal.setSelection(pos);
    }

    public void setOnCpViewClickListener(OnCpViewClickListener listener) {
        mCpViewClickListener = listener;
    }


    class SpinnerAdapter extends MBaseAdapter<CharSequence> {

        /* (non-Javadoc)
         * @see com.baimi.wallet.adapter.BAdapter#initView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public BHolder initView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = new Holder();
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            if (itemH > 0) {
                LayoutParams lp = (LayoutParams) convertView.getLayoutParams();
                lp.height = (int) itemH;
                convertView.setLayoutParams(lp);
            }
            if (itemTextSize > 0) {
                holder.tvContent.setTextSize(itemTextSize);
            }
            holder.tvContent.setTextColor(itemTextColor);
            return holder;
        }

        /* (non-Javadoc)
         * @see com.baimi.wallet.adapter.BAdapter#setItemView(android.content.Context)
         */
        @Override
        public View setItemView(Context context) {
            // TODO Auto-generated method stub
            return LayoutInflater.from(context).inflate(R.layout.item_cp_spinner, null);
        }

        /* (non-Javadoc)
         * @see com.baimi.wallet.adapter.BAdapter#afterInitView(int, com.baimi.wallet.adapter.BAdapter.BHolder, java.lang.Object)
         */
        @Override
        public void afterInitView(int position, BHolder bHolder, Object obj) {
            // TODO Auto-generated method stub
            Holder holder = (Holder) bHolder;
            String content = (String) getItem(position);
            holder.tvContent.setText(content);
        }

    }

    class Holder extends MBaseAdapter.BHolder {
        TextView tvContent;
    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        if (mCpViewClickListener != null) {
            this.setTag(arg2);
            mCpViewClickListener.onItemClick(this, false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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
}
