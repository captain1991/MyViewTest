package com.xiaodong.listviewtest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Random;
import java.util.Stack;

/**
 * Created by yxd on 2016/5/19.
 */
public class ColourImageView extends ImageView {

    private Bitmap mBitmap;
    private Stack<Point> stack= new Stack<>();

    public ColourImageView(Context context) {
        super(context);
    }

    public ColourImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColourImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = getMeasuredWidth();
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(viewWidth,getDrawable().getIntrinsicHeight()*viewWidth/getDrawable().getIntrinsicWidth());

        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        Bitmap bm = drawable.getBitmap();
        mBitmap = bm.createScaledBitmap(bm,getMeasuredWidth(),getMeasuredHeight(),false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
                int x = (int) event.getX();
                int y = (int) event.getY();
                fitColorinSameArea(x,y);
        }
        return super.onTouchEvent(event);
    }

    private void fitColorinSameArea(int x,int y){

        Bitmap bm = mBitmap;
        int w = bm.getWidth();
        int h = bm.getHeight();
        int pixel = bm.getPixel(x,y);
        int[] pixels = new int[w*h];
        bm.getPixels(pixels,0,w,0,0,w,h);
        fillColor(pixels,pixel,w,h,randomColor(),x,y);
        bm.setPixels(pixels, 0, w, 0, 0, w, h);
        setImageDrawable(new BitmapDrawable(bm));
    }


    private void fillColor(int[] pixels, int pixel, int w, int h, int newColor, int x, int y){
        stack.push(new Point(x, y));
        while(!stack.isEmpty()){
            Point point = stack.pop();
            int count = fillLineLeft(pixels, pixel, w, h, newColor, point.x, point.y);
            int xLeft = point.x-count+1;
            count = fillLineRight(pixels, pixel, w, h, newColor, point.x+1, point.y);
            int xRight = point.x+count;
            if(point.y-1>=0) {
                findSeedInNewLine(pixels, pixel, w, h, point.y - 1, xLeft, xRight);
            }
            if(point.y+1<h) {
                findSeedInNewLine(pixels, pixel, w, h, point.y + 1, xLeft, xRight);
            }
        }

    }


    /**
     * 在新行找种子节点
     *
     * @param pixels
     * @param pixel
     * @param w
     * @param h
     * @param i
     * @param left
     * @param right
     */
    private void findSeedInNewLine(int[] pixels, int pixel, int w, int h, int i, int left, int right){
        /**
         * 获得该行的开始索引
         */
        int begin = i * w + left;
        /**
         * 获得该行的结束索引
         */
        int end = i * w + right;

        boolean hasSeed = false;

        int rx = -1, ry = -1;

        ry = i;

        /**
         * 从end到begin，找到种子节点入栈（AAABAAAB，则B前的A为种子节点）
         */
        while (end >= begin)
        {
            if (pixels[end] == pixel)
            {
                if (!hasSeed)
                {
                    rx = end % w;
                    stack.push(new Point(rx, ry));
                    hasSeed = true;
                }
            } else
            {
                hasSeed = false;
            }
            end--;
        }
    }

    private int fillLineRight(int[] pixels, int pixel, int w, int h, int newColor, int x, int y)
    {
        int count = 0;
        while (x < w)
        {
//            Log.e("dofillLineRight>>>","dofillLineRight="+x+"w="+w);
            //拿到索引
            int index = y * w + x;
            if (needFillPixel(pixels, pixel, index))
            {
                pixels[index] = newColor;
                count++;
                x++;
            } else
            {
                break;
            }

        }

        return count;
    }

    private int fillLineLeft(int[] pixels,int pixel,int w,int h,int newColor,int x,int y){
        int count=0;
        while (x>=0){
            int index = w*y+x;
           if(needFillPixel(pixels,pixel,index)) {
//               Log.e("dofillLineleft>>>","dofillLineLeft");
               pixels[index] = newColor;
               count++;
               x--;
           }else{
               break;
           }

        }

        return count;
    }

    private boolean needFillPixel(int[] pixels,int pixel,int index){
        return pixels[index] == pixel;
    }

    private int randomColor(){
        Random random =new Random();
        int color = Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255));
        return color;
    }

}
