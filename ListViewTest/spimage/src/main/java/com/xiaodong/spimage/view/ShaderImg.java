package com.xiaodong.spimage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by yxd on 2016/6/1.
 */
public class ShaderImg extends ImageView{

    private String type = "circle";
    private Paint paint;
    Matrix matrix;
    public ShaderImg(Context context) {
        this(context, null);
    }

    public ShaderImg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaderImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        matrix = new Matrix();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void initPaint(){
       Bitmap bmp = getBitmap();
        BitmapShader bitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        int size = Math.min(bmp.getHeight(),bmp.getWidth());
        float scale = getWidth()*1.0f/size;
        matrix.setScale(scale,scale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
}

    private Bitmap getBitmap(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
        return  bitmapDrawable.getBitmap();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if(heightSize!=widthSize) {
            int size = Math.min(heightSize, widthSize);
            setMeasuredDimension(size, size);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();
        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2,paint);

    }
}
