package com.xiaodong.getapppic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxd on 2016/5/17.
 */
public class GridViewAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    LruCache<String, Bitmap> memeryCache;
    GridView gridView;
    Context context;
    List<File> files;
    List<MyTask> myTaskList;

    public GridViewAdapter(Context context, GridView gridView) {
        int maxSize = (int) Runtime.getRuntime().maxMemory();

        memeryCache = new LruCache<String, Bitmap>(maxSize / 6) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        this.context = context;
        this.gridView = gridView;
    }

    public void setFiles(List<File> files) {
        this.files = files;
        loadImageToCache();
        gridView.setOnScrollListener(this);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return files == null ? 0 : files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setTag(files.get(position).getName());
        setImage(holder.imageView, position);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }

    public void setImage(ImageView image, int position) {
        Bitmap bitmap = getBitmapCache(files.get(position).getName());
        Log.e("setImage",files.get(position).getName());
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        } else {
            image.setImageResource(R.drawable.empty_photo);
        }
    }

    private Bitmap getBitmapCache(String name) {
        return memeryCache.get(name);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            loadImageToCache();
        }else {
            cancel();
        }
    }

    private void cancel(){
        if(myTaskList!=null) {
            for (MyTask task : myTaskList) {
                task.cancel(false);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private void loadImageToCache() {
        myTaskList = new ArrayList<MyTask>();
        for (File file : files) {
            Log.e("loadimaget","loadimaget");
            MyTask loadTask = new MyTask();
            loadTask.execute(file);
            myTaskList.add(loadTask);
        }
    }

    class MyTask extends AsyncTask<File, Void, Bitmap> {
        private String name;

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = (ImageView) gridView.findViewWithTag(name);
            if(imageView!=null){
                imageView.setImageBitmap(bitmap);
            }
        }

        @Override
        protected Bitmap doInBackground(File... params) {
            File file = params[0];
            name = file.getName();
            Log.e("savekey",name);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bitmap != null) {
                memeryCache.put(name, bitmap);
            }
            return bitmap;
        }
    }

}
