package us.nijikon.livelylauncher.adapters;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import us.nijikon.livelylauncher.R;

/**
 * Created by bowang .
 */
public class ImageAsyncTaskLoader extends AsyncTask<String,Void,Bitmap> {

    ImageAsyncTaskLoader(WallPaperAdapter.WallPaper wallPaper,String path,boolean inapp, Context context){
        wallPaper.getImageView().setImageDrawable(wallPaper.getImageView().getContext().getApplicationContext().getDrawable(R.drawable.boarder));
        this.wallPaper =wallPaper;
        this.path = path;
        this.inApp = inapp;
        this.context = context;
    }

    private WallPaperAdapter.WallPaper wallPaper;
    private String path;
    private boolean inApp;
    private Context context;

    public String getPath(){
        return this.path;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if(isCancelled()) return null;
        /* wallpaper path may change by other thread */
        if(!wallPaper.getPath().equals(path)) return null;
        for(String path: params){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap pic = null;
            try {
                if(inApp) {
                    pic = BitmapFactory.decodeStream(context.getResources().getAssets().open("image/" + path), null, options);
                }else {
                    pic = BitmapFactory.decodeStream(new FileInputStream(path),null,options);
                }
                return pic;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
          }
            return null;
    }
    @Override
    public void onProgressUpdate(Void ... voids){
        if(isCancelled()){
            return;
        }
        if(!wallPaper.getPath().equals(path)) return;
    }
    @Override
    public void onPostExecute(Bitmap f){
        if(wallPaper!=null && !isCancelled() && wallPaper.getPath().equals(path)) {
            wallPaper.getImageView().setImageBitmap(f);
        }
    }

}
