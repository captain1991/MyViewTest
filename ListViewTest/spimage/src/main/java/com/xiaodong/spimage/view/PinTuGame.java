package com.xiaodong.spimage.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xiaodong.spimage.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yxd on 2016/6/1.
 */
public class PinTuGame extends RelativeLayout implements View.OnClickListener {

    private int mMargin;
    private int mPadding;
//    默认图片
    private Bitmap bitmap;

    private int count = 3;
    //宽
    int width;
    private ImageView[] imageViews;


    private int mItemWidth;
    //小图片
    private List<ImagePiece> imagePieces;
    private Context mContext;
    private boolean once;
    public PinTuGame(Context context) {
        this(context, null);
    }

    public PinTuGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mPadding =min(getPaddingTop(),getPaddingBottom(),getPaddingLeft(),getPaddingRight());
//        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PinTuGame);
//        count = a.getInt(R.styleable.PinTuGame_column,3);
    }

    public int min(int i,int j,int m,int n ){
        return Math.min(Math.min(i,j),Math.min(m,n));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = Math.min(getMeasuredHeight(),getMeasuredWidth());
        if(!once){
            initBitmap();
            initItem();
        }
        once = true;
        setMeasuredDimension(width,width);
    }

    public void setBitmap(Bitmap b){
        bitmap = b;
    }

    public void initBitmap(){
        if(bitmap==null)
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.buitful);

        imagePieces = ImageSplitor.split(bitmap,count);

        Log.e("imagePieces>>","imagePieces="+imagePieces.size());
        Collections.sort(imagePieces, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece lhs, ImagePiece rhs) {
                int value = lhs.index-rhs.index;
                return Math.random() > 0.5f ? 1 : -1;
            }
        });
    }

    public void initItem(){
        int childWidth = (width-mPadding*2-mMargin*(count-1))/count;
        mItemWidth = childWidth;
        imageViews = new ImageView[count*count];

        for (int i=0;i<count*count;i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageBitmap(imagePieces.get(i).bitmap);
            imageViews[i] = imageView;
            imageView.setClickable(true);
            imageView.setOnClickListener(this);
            imageView.setId(i + 1);
            imageView.setTag(i + "_" + imagePieces.get(i).index);


            RelativeLayout.LayoutParams lp = new LayoutParams(mItemWidth,mItemWidth);
            if ((i + 1) % count != 0) {
                lp.rightMargin = mMargin;
            }
            // 如果不是第一列
            if (i % count != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF,//
                        imageViews[i - 1].getId());
            }
            // 如果不是第一行，//设置纵向边距，非最后一行
            if ((i + 1) > count) {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW,//
                        imageViews[i - count].getId());
            }
            addView(imageView, lp);
        }

    }

    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View v) {
        if (isChangeing){
            return;
        }
        if(mFirst==v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }
        if(mFirst==null  ){
            mFirst =(ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#55000000"));
        }else{
            mSecond = (ImageView) v;
            changePosition();
        }
    }

    boolean isChangeing;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void changePosition(){
        mFirst.setColorFilter(null);
//        for(String s:spiecesIndex){
//        }
        setUpAnimLayout();
        ImageView tempFirstImage = new ImageView(mContext);
        tempFirstImage.setImageBitmap(getBitmap(mFirst));
        RelativeLayout.LayoutParams lp = new LayoutParams(mItemWidth,mItemWidth);
        lp.leftMargin = mFirst.getLeft()-mPadding;
        lp.topMargin = mFirst.getTop()-mPadding;
        tempFirstImage.setLayoutParams(lp);
        animLayout.addView(tempFirstImage);

        ImageView tempSecImage = new ImageView(mContext);
        tempSecImage.setImageBitmap(getBitmap(mSecond));
        RelativeLayout.LayoutParams lp2 = new LayoutParams(mItemWidth,mItemWidth);
        lp2.leftMargin = mSecond.getLeft()-mPadding;
        lp2.topMargin = mSecond.getTop()-mPadding;
        tempSecImage.setLayoutParams(lp2);
        animLayout.addView(tempSecImage);
        Animation anim = new TranslateAnimation(0,mSecond.getLeft()-mFirst.getLeft(),0,mSecond.getTop()-mFirst.getTop());
        anim.setDuration(300);
        anim.setFillAfter(true);
        tempFirstImage.startAnimation(anim);

        Animation anim2 = new TranslateAnimation(0,mFirst.getLeft()-mSecond.getLeft(),0,mFirst.getTop()-mSecond.getTop());
        anim2.setDuration(300);
        anim2.setFillAfter(true);
        tempSecImage.startAnimation(anim2);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isChangeing = true;
                mFirst.setVisibility(INVISIBLE);
                mSecond.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isChangeing = false;
                String firstTag = (String) mFirst.getTag();
                String[] fpiecesIndex = firstTag.split("_");
                String secondTag = (String) mSecond.getTag();
                String[] spiecesIndex = secondTag.split("_");
                mFirst.setImageBitmap(imagePieces.get(Integer.parseInt(spiecesIndex[0])).bitmap);
                mSecond.setImageBitmap(imagePieces.get(Integer.parseInt(fpiecesIndex[0])).bitmap);
                mFirst.setVisibility(VISIBLE);
                mSecond.setVisibility(VISIBLE);

                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);
                mFirst = null;
                mSecond = null;
                animLayout.removeAllViews();
                checkSuccess();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public Bitmap getBitmap(ImageView iv){
        Bitmap bitmap =null;
        String tag = (String) iv.getTag();
        String[] tags = tag.split("_");
        bitmap = imagePieces.get(Integer.parseInt(tags[0])).bitmap;
        return bitmap;
    }

    RelativeLayout animLayout;
    public void setUpAnimLayout(){
        animLayout = new RelativeLayout(mContext);
        addView(animLayout);
    }


    public void checkSuccess(){
        boolean isSuccess=true;
        for(int i = 0;i<imagePieces.size();i++){
            String tag = (String) imageViews[i].getTag();
            String[] index = tag.split("_");
            Log.e("checkSuccess: ", ""+tag);
            if(Integer.parseInt(index[1])!=i){
                isSuccess = false;
            }
        }
        if(isSuccess){
            Toast.makeText(mContext,"完成拼图",Toast.LENGTH_SHORT).show();
            count = count+1;
            removeAllViews();
            initBitmap();
            initItem();
        }
    }
}
