package com.xiaodong.getapppic;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    Button btn;
    GridView gridView;
    public WindowManager windowManager;
    GridViewAdapter adapter;
//    GridAdapter gridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn_show);
        gridView = (GridView) findViewById(R.id.image);
        adapter = new GridViewAdapter(this,gridView);

//        gridAdapter = new GridAdapter(this);
        gridView.setAdapter(adapter);
        btn.setOnClickListener(this);
        initData();
    }

    @Override
    public void onClick(View v) {
//        getCachepic();
//        initWindowView();
//        MyFileUtil.images_path = "";
        Intent intent = new Intent(MainActivity.this,MyService.class);
        startService(intent);
    }

    private void initData(){
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"AutoSave");
//        if(file==null){
//            Toast.makeText(MainActivity.this, "没有文件", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        File[] files = file.listFiles();
        List<File> fileList = new ArrayList<File>();
//        for (File f:files){
//            fileList.add(f);
//        }
//        adapter.setFiles(fileList);

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);

        cursor.moveToFirst();
        while (cursor.moveToNext()){
            String path =
            cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            File file1 = new File(path);
            if (file1!=null){
                fileList.add(file1);
            }
        }
        adapter.setFiles(fileList);

    }

    private void initWindowView(){
        windowManager = getWindowManager();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        View view = LayoutInflater.from(this).inflate(R.layout.fast_view,null);
//        ImageView view = new ImageView(this);
//        view.setImageResource(R.drawable.eye1);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(imageParams);
        params.x =screenWidth-view.getWidth();
        params.y = screenHeight/2;
        params.width = view.getLayoutParams().width;
        params.height = view.getLayoutParams().height;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(view,params);
    }

}
