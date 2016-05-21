package com.xiaodong.listviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.xiaodong.listviewtest.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    ListView listView;
    MyAdapter adapter;
    PullToRefreshLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
//        listView = (ListView) findViewById(R.id.nomlistview);
//        ImageView empty = (ImageView) findViewById(R.id.empty_image);
//        listView.setEmptyView(empty);
        layout = (PullToRefreshLayout) findViewById(R.id.pull_layout);
        listView = (ListView) layout.getPullableView();
        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        List<String> strings = new ArrayList<String>();
        strings.add("string1");
        strings.add("string1");
        strings.add("string1");
        strings.add("string1");
        strings.add("string1");
        adapter.setStrings(strings);

    }
}
