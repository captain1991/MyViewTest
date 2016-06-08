package xiaodong.com.backgroundservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class DemoService extends Service {
    public static boolean isRuning = false;
    public DemoService() {
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            Notification.Builder builder = new Notification.Builder(this);
//            builder.setSmallIcon(R.mipmap.ic_launcher);
//            startForeground(205, builder.build());
//            startService(new Intent(DemoService.this,CancleService.class));
//        }else {
//            startForeground(205,new Notification());
//        }
//    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e("DemoService>>>","start");
        if(!isRuning) {
            isRuning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (!WakeUpService.isRuning()) {
                            Intent intent1 = new Intent(DemoService.this, WakeUpService.class);
                            startService(intent1);
                            Log.e("DemoService", "restart");
                        }
                    }
                }
            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRuning = false;
    }

    public static boolean isRuning(){

        return isRuning;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
