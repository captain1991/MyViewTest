package xiaodong.com.backgroundservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

public class ForegrountService extends Service {
    private static boolean sPower;
    public ForegrountService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(205, builder.build());
            startService(new Intent(ForegrountService.this,CancleService.class));
        }else {
            startForeground(205,new Notification());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sPower){
                    if(System.currentTimeMillis()>=123456789000000L){
                        sPower=false;
                    }
                    SystemClock.sleep(3000);
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }
}
