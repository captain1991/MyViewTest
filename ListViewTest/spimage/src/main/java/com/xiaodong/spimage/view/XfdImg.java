package com.xiaodong.spimage.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import com.xiaodong.spimage.R;
import java.lang.ref.WeakReference;

/**
 * Created by yxd on 2016/6/1.
 */
public class XfdImg extends ImageView {
    private WeakReference<Bitmap> bitmapWeak;
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Paint paint;
    private int radius;

    private int round_radius;
//    圆角矩形默认半径
    private final static int DEFAULT_ROUND_RADIUS=10;
    private int type;
    private static int CIRCLE=0;
    private static int ROUND=1;
    // 覆盖图形的bitmap
    private Bitmap maskBitmap;

    public XfdImg(Context context) {
        this(context, null);
    }

    public XfdImg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XfdImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XfdImg);
        type = array.getInt(R.styleable.XfdImg_type,0);
        round_radius = array.getDimensionPixelSize(R.styleable.XfdImg_roundradiu,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics()));
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(type==CIRCLE) {
            int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
            radius = width / 2;
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Bitmap finalBitmap =bitmapWeak==null ? null: bitmapWeak.get();
        if(finalBitmap==null||finalBitmap.isRecycled()) {
            Drawable drawable = getDrawable();

            int dWidth = drawable.getIntrinsicWidth();
            int dHeight = drawable.getIntrinsicHeight();

            Bitmap bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas drawableCanvas = new Canvas(bmp);
            float scale = getWidth() * 1.0F / Math.min(dWidth, dHeight);
            drawable.setBounds(0, 0, (int) (scale * dWidth),
                    (int) (scale * dHeight));
            drawable.draw(drawableCanvas);
            if(maskBitmap==null||maskBitmap.isRecycled())
                maskBitmap = getBitmap();
            paint.reset();
            paint.setFilterBitmap(false);
            paint.setXfermode(xfermode);
            drawableCanvas.drawBitmap(maskBitmap, 0, 0, paint);
            paint.setXfermode(null);
            canvas.drawBitmap(bmp, 0, 0, null);
            bitmapWeak = new WeakReference<Bitmap>(bmp);
        }

        if(finalBitmap!=null){
            canvas.drawBitmap(finalBitmap, 0, 0, null);
            return;
        }
    }

    public Bitmap getBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        Canvas canvas = new Canvas(bitmap);

        if(type==0) {
            canvas.drawCircle(radius, radius, radius, paint);
        }else{
            canvas.drawRoundRect(new RectF(0,0,getWidth(),getHeight()),round_radius,round_radius,paint);
        }

        return bitmap;
    }
}
