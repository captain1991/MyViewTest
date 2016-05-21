package com.xiaodong.getapppic;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yxd on 2016/5/17.
 */
public class MyFileUtil {

    public static String images_path="Android"+File.separator+"data"
            +File.separator+ "com.netease.newsreader.activity"+File.separator+"cache"+File.separator+"bitmap";

    public static void getCachepic(){
        File file = Environment.getExternalStorageDirectory();
        File images = new File(file.getAbsolutePath()+File.separator+images_path);

        File targerDir = new File(file.getAbsolutePath()+File.separator+"AutoSave");
        if(!targerDir.exists()){
            targerDir.mkdir();
        }

        if(images.exists()){
            File[] files = images.listFiles();
            int size = 0;
            if (files != null && files.length > 0) {
                for (int i = 0;i<files.length;i++) {
                    File targetImg = new File(targerDir.getAbsolutePath()+File.separator+files[i].getName()+".jpg");
                    if (targetImg.exists()){
                        break;
                    }
                    try {
                        FileInputStream is = new FileInputStream(files[i]);
                        FileOutputStream os = new FileOutputStream(targetImg);
                        byte[] buff = new byte[1024];
                        int len;
                        while ((len=is.read(buff))!=-1){
                            os.write(buff,0,len);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static long getDirSize(File file) {
        long totalSize = 0;

        if (file != null && file.exists()) {

            if (file.isDirectory()) {
                File[] files = file.listFiles();

                for (int i = 0; i < files.length; i++) {

                    //是目录；
                    if (file.isDirectory()) {
                        totalSize += getDirSize(files[i]);
                    } else {
                        totalSize += file.length();
                    }
                }
            } else {
                totalSize += file.length();
            }
        }

        return totalSize;
    }

    public static void clearCacheDir(File cacehDir) {
        if (cacehDir != null) {

            if (cacehDir.isDirectory()) {
                File[] files = cacehDir.listFiles();

                if (files != null && files.length > 0) {

                    for (File itemFile : files) {

                        //是文件，则删除；
                        if (itemFile.isFile()) {
                            itemFile.delete();
                        } else {
                            clearCacheDir(itemFile);
                        }
                    }
                }

            } else {
                cacehDir.delete();
            }
        }
    }
}
