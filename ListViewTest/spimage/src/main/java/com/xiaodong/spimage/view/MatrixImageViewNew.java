package com.xiaodong.spimage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import com.xiaodong.spimage.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/1/22.
 */
public class MatrixImageViewNew extends ImageView {
    private static final int MODE_NONE = 0x00123;// 默认的触摸模式
    private static final int MODE_DRAG = 0x00321;// 拖拽模式
    private static final int MODE_ZOOM = 0x00132;// 缩放or旋转模式

    private int mode;// 当前的触摸模式

    private float preMove = 1F;// 上一次手指移动的距离
    private float saveRotate = 0F;// 保存了的角度值
    private float rotate = 0F;// 旋转的角度

    private float[] preEventCoor;// 上一次各触摸点的坐标集合

    private PointF start, mid;// 起点、中点对象
    private Matrix currentMatrix, savedMatrix;// 当前和保存了的Matrix对象
    private Context mContext;// Fuck……
    private int screenWidth,screenHeight;


    public MatrixImageViewNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        getScreen();
        // 初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
            /*
             * 实例化对象
             */
        currentMatrix = new Matrix();
        savedMatrix = new Matrix();
        start = new PointF();
        mid = new PointF();
//        DisplayMetrics dm = new DisplayMetrics();
//        dm = getResources().getDisplayMetrics();

        // 模式初始化
        mode = MODE_NONE;

            /*
             * 设置图片资源
             */
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        float scale =  Math.min(screenWidth * 1.0f / bitmap.getWidth(), screenHeight * 1.0f / bitmap.getHeight());

        int bitmapW = (int) (bitmap.getWidth()*scale);
        int bitmapH = (int) (bitmap.getHeight()*scale);

        //是图片位置在屏幕居中
        currentMatrix.setTranslate((screenWidth - bitmapW) / 2, (screenHeight - bitmapH) / 2);
        setImageMatrix(currentMatrix);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmapW,bitmapH , true);
        setImageBitmap(bitmap);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:// 单点接触屏幕时
                savedMatrix.set(currentMatrix);
                start.set(event.getX(), event.getY());
                mode = MODE_DRAG;
                preEventCoor = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:// 第二个点接触屏幕时
                preMove = calSpacing(event);
                if (preMove > 10F) {
                    savedMatrix.set(currentMatrix);
                    calMidPoint(mid, event);
                    mode = MODE_ZOOM;
                }
                preEventCoor = new float[4];
                preEventCoor[0] = event.getX(0);
                preEventCoor[1] = event.getX(1);
                preEventCoor[2] = event.getY(0);
                preEventCoor[3] = event.getY(1);
                saveRotate = calRotation(event);
                break;
            case MotionEvent.ACTION_UP:// 单点离开屏幕时
            case MotionEvent.ACTION_POINTER_UP:// 第二个点离开屏幕时
                mode = MODE_NONE;
                preEventCoor = null;
                break;
            case MotionEvent.ACTION_MOVE:// 触摸点移动时
                /*
                 * 单点触控拖拽平移
                 */
                if (mode == MODE_DRAG) {
                    currentMatrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    currentMatrix.postTranslate(dx, dy);
                }
                /*
                 * 两点触控拖放旋转
                 */
                else if (mode == MODE_ZOOM && event.getPointerCount() == 2) {
                    float currentMove = calSpacing(event);
                    currentMatrix.set(savedMatrix);
                    /*
                     * 指尖移动距离大于10F缩放
                     */
                    if (currentMove > 10F) {
                        float scale = currentMove / preMove;
                        currentMatrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    /*
                     * 保持两点时旋转
                     */
                    if (preEventCoor != null) {
                        rotate = calRotation(event);
                        float r = rotate - saveRotate;
                        currentMatrix.postRotate(r, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                    }
                }
                break;
        }

        setImageMatrix(currentMatrix);
        return true;
    }

    /**
     * 计算两个触摸点间的距离
     */
    private float calSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算两个触摸点的中点坐标
     */
    private void calMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * 计算旋转角度
     *
     *
     * @return 角度值
     */
    private float calRotation(MotionEvent event) {
        double deltaX = (event.getX(0) - event.getX(1));
        double deltaY = (event.getY(0) - event.getY(1));
        double radius = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radius);
    }
    /**
     *获得屏幕的宽高
     *
     */
    private void getScreen(){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels-getStatusBarHeight();
    }

    int statusBarHeight;
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.e("getStatusBarHeight==",""+statusBarHeight);
        return statusBarHeight;
    }
}
