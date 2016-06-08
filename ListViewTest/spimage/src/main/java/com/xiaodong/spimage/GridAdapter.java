package com.xiaodong.spimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dashuai.core.DBitmap;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxd on 2016/6/2.
 */
public class GridAdapter extends BaseAdapter {
    Context mContext;
    DBitmap dBitmap;
    public GridAdapter(Context mContext) {
        this.mContext = mContext;
//        initBitmapUtil();
        dBitmap = DBitmap.create(mContext);
    }

    List<String> filePaths ;

    public void setFilePaths(List<String> files){
        filePaths = files;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filePaths == null ?0:filePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    BitmapUtils bitmapUtils;
    public void initBitmapUtil(){
        BitmapDisplayConfig displayConfig = new BitmapDisplayConfig();
        displayConfig.setBitmapMaxSize(new BitmapSize(120, 120));
        displayConfig.setBitmapConfig(Bitmap.Config.RGB_565);



        // 占用内存最多25%
        bitmapUtils = new BitmapUtils(mContext, null, 0.25f, 100 * 1024 * 1024);

        bitmapUtils.configThreadPoolSize(3)
                .configDefaultDisplayConfig(displayConfig)
                .configDiskCacheEnabled(true)
                .configMemoryCacheEnabled(true)
                .configDefaultConnectTimeout(15000)
                .configDefaultReadTimeout(15000);
//                .configDefaultLoadingImage(R.drawable.empty_photo)
//                .configDefaultLoadFailedImage(R.drawable.big_loadpic_fail_listpage);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item,null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.itemImage);
//        bitmapUtils.display(imageView,filePaths.get(position));
        dBitmap.display(imageView,filePaths.get(position),100,100);
        return convertView;
    }
}
