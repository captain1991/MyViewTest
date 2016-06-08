package com.xiaodong.spimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xiaodong.spimage.view.PinTuGame;

public class MainActivity extends Activity {
    PinTuGame pinTuGame;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        pinTuGame = (PinTuGame) findViewById(R.id.pintu);
//        intent = getIntent();
//        setBitmapTogame();
    }

    public void setBitmapTogame(){

        String path = intent.getStringExtra("filePath");
        if(path!=null) {
            Bitmap b = BitmapFactory.decodeFile(path);
            pinTuGame.setBitmap(b);
        }
    }
}
