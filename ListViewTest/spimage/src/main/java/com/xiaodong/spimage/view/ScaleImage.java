package com.xiaodong.spimage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by yxd on 2016/6/8.
 */
public class ScaleImage extends ImageView {
    public ScaleImage(Context context) {
        super(context);
    }

    public ScaleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
