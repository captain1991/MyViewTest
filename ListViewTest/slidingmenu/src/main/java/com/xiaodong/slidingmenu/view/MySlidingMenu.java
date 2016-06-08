package com.xiaodong.slidingmenu.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import com.nineoldandroids.view.ViewHelper;
import com.xiaodong.slidingmenu.R;

/**
 * Created by yxd on 2016/6/3.
 */
public class MySlidingMenu extends HorizontalScrollView{


    private int screenWidth;
    private int screenHeight;
    private View menu;
    private View content;
    private int menuWidth;
    private int menuRightPadding;
    private int menuHeight;


    public MySlidingMenu(Context context) {
        this(context, null);
    }

    public MySlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MySlidingMenu);
        menuRightPadding = a.getDimensionPixelSize(R.styleable.MySlidingMenu_menupadding, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics()));
        a.recycle();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        Log.e("MySlidingMenu","screenWidth = "+screenWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            animator = new ObjectAnimator();
        }
    }
    boolean once;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("onMeasure","onMeasure ");
        if (!once){
            ViewGroup layout = (ViewGroup) getChildAt(0);
            menu = layout.getChildAt(0);
            content = layout.getChildAt(1);
            menuWidth = screenWidth-menuRightPadding;
            menu.getLayoutParams().width = menuWidth;
            content.getLayoutParams().width = screenWidth;
        }
        Log.e("onMeasure","menuHeight = "+menu.getLayoutParams().height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e("onLayout", "onLayout ");
        if(changed){
            this.scrollTo(menuWidth, 0);

            once = true;
        }

    }

    ObjectAnimator animator;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                int scrollX = getScrollX();
//                Log.e("onTouchEvent", "scrollX= "+scrollX);
                float multple =(menuWidth-scrollX)*1.0f/menuWidth*1.0f;

                ViewHelper.setScaleY(menu, (float) (0.7+0.3*multple));
                break;
            case MotionEvent.ACTION_DOWN:
                break;

        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
