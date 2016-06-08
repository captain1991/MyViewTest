package com.xiaodong.spimage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TupianActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
GridView gridView;
    GridAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tupian);
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new GridAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        initData();
    }
    List<String> pathList = new ArrayList<String>();
    public void initData(){
//        Cursor c = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
//        c.moveToFirst();
//        while (c.moveToNext()){
//            String path =
//                    c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
//            pathList.add(path);
//        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+File.separator+"DCIM"+File.separator+"MCamera");
        File[] files = file.listFiles();
        Log.e("initData>>",""+file.getAbsolutePath());
        if(!file.exists()){
            Intent intent = new Intent(TupianActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (files!=null&&files.length>0) {
            for (File f : files) {
                String path = f.getAbsolutePath();
                pathList.add(path);
            }
        }
        adapter.setFilePaths(pathList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(TupianActivity.this,MainActivity.class);
        intent.putExtra("filePath",pathList.get(position));
        startActivity(intent);
    }
}
