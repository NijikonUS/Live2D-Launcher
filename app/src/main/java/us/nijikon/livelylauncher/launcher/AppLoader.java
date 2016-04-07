package us.nijikon.livelylauncher.launcher;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class AppLoader extends AsyncTaskLoader<AppDataHolder> {

    public static final String tag = "AppLoader";


    private AppDataHolder data;

    public AppLoader(Context context) {
        super(context);

    }



    @Override
    public AppDataHolder loadInBackground() {
        data = AppDataHolder.getInstance();
        if(!data.loadFromFile(getContext())){
            data.firstLoad(getContext());
        }
        data.writeToFile(getContext());
        return data;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

}
