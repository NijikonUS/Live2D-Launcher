package us.nijikon.livelylauncher.adapters;

import android.app.WallpaperManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.live2d.utils.android.FileManager;
import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.launcher.Launcher;
import us.nijikon.livelylauncher.live2dHelpers.LAppDefine;
import us.nijikon.livelylauncher.live2dHelpers.LAppLive2DManager;
import us.nijikon.livelylauncher.models.AsycTaskList;

/**
 * Created by bowang .
 */
public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.WallPaper> {


    private AsycTaskList list = new AsycTaskList();
    public AsycTaskList getTaskList(){
        return list;
    }
    List<String> paths;
    Context context;
    boolean inApp;

    public WallPaperAdapter(Context context){
        paths = new ArrayList<>();
        this.context = context;
        inApp = true;
        try {
            paths = Arrays.asList(context.getAssets().list("image"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WallPaperAdapter.WallPaper onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new WallPaper(LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item,parent,false));
    }

    @Override
    public void onBindViewHolder(WallPaperAdapter.WallPaper holder, int position) {
            //if(cursor.moveToPosition(position)) {

                final String path = paths.get(position);//cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.d("afdsf", path);
                holder.setPath(path);
                list.add(new ImageAsyncTaskLoader(holder, path, inApp,context));

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Launcher) context).getLive2DMgr().updateBackground(path,inApp);
                        LAppLive2DManager.writeWallPaper(context);
                    }
                });

    }

    @Override
    public int getItemCount() {
        // execute task immediately if size is too small
        if(paths.size() < 4){
            list.executeAll();
        }
        return paths.size();

    }

    public static class WallPaper extends RecyclerView.ViewHolder {
        public ImageView getImageView() {
            return imageView;
        }
        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
        private ImageView imageView;
        private String path;
        public String getPath() {
            return path;
        }
        public void setPath(String path) {
            this.path = path;
        }
        public WallPaper(View itemView) {
            super(itemView);
            imageView =  (ImageView)itemView.findViewById(R.id.wallpaperItem);
        }
    }

    public void setPaths(List<String> paths, boolean inApp){
        this.inApp = inApp;
        this.paths = paths;
        this.notifyDataSetChanged();

    }

}
