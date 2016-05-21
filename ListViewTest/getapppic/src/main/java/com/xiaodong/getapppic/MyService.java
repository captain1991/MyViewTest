package com.xiaodong.getapppic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyService extends Service implements View.OnClickListener {
    private Timer timer;
    WindowManager windowManager;
    boolean isWindowShow;
    View view;
    public MyService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initWindowView();
//        if (timer == null) {
//            timer = new Timer();
//            timer.scheduleAtFixedRate(new MyTimerTask(), 0, 500);
//        }
//        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//        service.scheduleAtFixedRate(new MyTimerTask(),0,500, TimeUnit.SECONDS);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    Handler mHandler = new Handler();

    class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            if (isWindowShow) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initWindowView();
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.save){
        Log.d("Onclick","save");
        MyFileUtil.getCachepic();
        }else {
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        windowManager.removeView(view);
        view=null;
    }

    private void initWindowView() {

        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fast_view, null);
        Button save = (Button) view.findViewById(R.id.save);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
//        ImageView view = new ImageView(this);
//        view.setImageResource(R.drawable.eye1);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(imageParams);
        params.x = screenWidth - view.getWidth();
        params.y = screenHeight / 2;
        params.width = view.getLayoutParams().width;
        params.height = view.getLayoutParams().height;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(view, params);
        isWindowShow = true;
    }
}
