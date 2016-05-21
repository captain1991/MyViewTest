package com.xiaodong.listviewtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yxd on 2016/4/20.
 */
public class MyAdapter extends BaseAdapter{
    Context context;
    List<String> strings;
    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return strings==null ? 0:strings.size();
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
        TextView textView = (TextView) convertView.findViewById(R.id.item_text);
        textView.setText(">>>>>>>>>>>>>>>>>>");
        return convertView;
    }
}
