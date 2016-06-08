package xiaodong.com.backgroundservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

public class WakeUpService extends Service {
    public static boolean isRuning = false;

    public WakeUpService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        cancleNotification();
        if (!isRuning) {
            isRuning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (!DemoService.isRuning()) {
                            Intent intent1 = new Intent(WakeUpService.this, DemoService.class);
                            startService(intent1);
                            Log.e("WakeUpService", "restart");
                        }
                    }
                }
            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void cancleNotification(){
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        startForeground(205,builder.build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                stopForeground(true);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(205);
            }
        }).start();
    }

    public static boolean isRuning() {

        return isRuning;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRuning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
