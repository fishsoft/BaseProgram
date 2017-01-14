package com.morse.basemoduel.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9.
 */

public abstract class MBaseAdapter<T> extends BaseAdapter {
    protected List<T> datas;
    protected int checkedPos;
    protected Context context;
    protected int PAGE_SIZE = 10;

    public MBaseAdapter() {

    }

    public MBaseAdapter(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return getCount() <= position ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Object getSelectedItem() {
        if (checkedPos < 0) return null;
        if (checkedPos >= getCount()) return null;
        return datas.get(checkedPos);
    }

    public abstract BHolder initView(int position, View convertView, ViewGroup parent);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BHolder holder = null;
        try {
            if (convertView == null) {
                if (context == null) {
                    context = parent.getContext();
                }
                convertView = setItemView(context);
                holder = initView(position, convertView, parent);
                convertView.setTag(holder);
            } else {
                holder = (BHolder) convertView.getTag();
            }
            Object obj = getItem(position);
            if (holder != null) {
                holder.vPos = position;
            }
            afterInitView(position, holder, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public abstract View setItemView(Context context);

    public abstract void afterInitView(int position, BHolder bHolder, Object obj);

    public void deleteItem(int pos) {
        if (pos < 0) return;
        if (pos >= getCount()) return;
        try {
            datas.remove(pos);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<T> getDatas() {
        return datas;
    }

    @SuppressWarnings("unchecked")
    public void setDatas(Object obj) {
        this.datas = obj == null ? null : (List<T>) obj;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public void clear() {
        if (getCount() > 0) {
            datas.clear();
        }
    }

    public void setDatas(int page, List<T> datas) {
        if (page < 2) {
            this.datas = datas;
        } else {
            if (getCount() < 1) {
                this.datas = datas;
            } else {
                //this.datas.addAll(datas);
                appendDatas(page, PAGE_SIZE, datas);
            }
        }
    }

    public void appendDatas(List<T> datas) {
        appendDatas(0, 0, datas);
    }

    public void appendDatas(int page, int pageCount, List<T> datas) {
        if (getCount() < 1) {
            this.datas = datas;
        } else {
            int index = page < 2 ? 0 : (page - 1) * pageCount;
            if (index == 0) {
                this.datas = datas;
            } else {
                int size = getCount();
                index = index > size ? size : index;
                if (index < size) {
                    for (int i = index; i < size; ) {
                        if (i >= getCount()) break;
                        this.datas.remove(i);
                    }
                }
                this.datas.addAll(datas);
            }

        }
    }

    public static class BHolder {
        protected int vPos;

        public int getVPos() {
            return vPos;
        }
    }

    public int getCheckedPos() {
        return checkedPos;
    }

    public void setCheckedPos(int checkedPos) {
        this.checkedPos = checkedPos;
    }
}
