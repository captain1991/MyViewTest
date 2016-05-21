package com.xiaodong.getapppic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

/**
 * Created by yxd on 2016/5/17.
 */
public class GridAdapter extends BaseAdapter{

    List<File> fileList;
    Context context;

    public GridAdapter(Context context) {
        this.context = context;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fileList ==null ? 0:fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        File file = fileList.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        holder.imageView.setImageBitmap(bitmap);
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
    }
}
