package xiaodong.com.backgroundservice;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by yxd on 2016/5/31.
 */
public class DemoPress {
    private static final String PROCESS = "xiaodong.com.backgroundservice.process";
    private static final File FILE =
            new File(new File(Environment.getDataDirectory(), "data"), "xiaodong.com.backgroundservice");
    public static boolean sPower=true;
    public static void  main(String[] args){
        Looper.prepare();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sPower) {
                    String cmd = String.format("am startservice%s-n xiaodong.com.backgroundservice/" +
                                    "xiaodong.com.backgroundservice.DemoService",
                            SysUtil.isAfter17() ? " --user 0 " : " ");
//                    LogUtil.i("CMD exec " + cmd);
                    try {
                        Runtime.getRuntime().exec(cmd);
                    } catch (IOException e) {
                    }
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        LogUtil.w("Thread sleep failed:" + e.toString());
                    }
                }

            }
        }).start();
       Looper.loop();
    }

    public static void start(Context context){
        Log.i("DemoPress","====================Daemon will be start====================");
        File[] processes = new File("/proc").listFiles();
        for (File file : processes) {
            if (file.isDirectory()) {
                File cmd = new File(file, "cmdline");
                if (!cmd.exists())
                    continue;
                try {
                    BufferedReader br = new BufferedReader(new FileReader(cmd));
                    String line = br.readLine();
                    if (null != line && line.startsWith(PROCESS)) {
                        Log.i("DemoPress", "Daemon already running");
                        return;
                    }
                    br.close();
                } catch (IOException e) {
                    Log.e("DemoPress", "Check daemon running with error:" + e.toString());
                }
            }
        }

        ProcessBuilder builder = new ProcessBuilder();
        Map<String,String> env = builder.environment();
        String classpath = env.get("CLASSPATH");
        if(classpath == null){
            classpath = context.getPackageCodePath();
            Log.e("DemoPresspanull",classpath);
        }else {
            classpath = classpath+":"+context.getPackageCodePath();
            Log.e("DemoPress",classpath);
        }
        env.put("CLASSPATH", classpath);
        builder.directory(new File("/"));

        try {
            Process process = builder.command("sh").redirectErrorStream(false).start();
            OutputStream os = process.getOutputStream();
            String cmd = "id\n";
            os.write(cmd.getBytes("utf8"));
            os.flush();
            Log.e("DemoPress", "Exec cmd" + cmd);
            cmd = "cd"+FILE.getAbsolutePath()+"\n";
            os.write(cmd.getBytes("utf8"));
            os.flush();
            Log.e("DemoPress", "Exec cmd" + cmd);
            cmd = "app_process/"+DemoPress.class.getName()+" --nice-name="+ PROCESS + " &\n";
            os.write(cmd.getBytes("utf8"));
            os.flush();
            Log.e("DemoPress", "Exec cmd" + cmd);
            os.write("exit\n".getBytes("utf8"));
            os.flush();
            Log.e("DemoPress", "Exec cmd"+cmd);
        } catch (IOException e) {
            Log.e("Demo","Exec cmd with error:" + e.toString());
        }

    }

}
