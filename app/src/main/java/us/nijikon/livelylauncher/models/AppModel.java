package us.nijikon.livelylauncher.models;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by bowang .
 */
public class AppModel {

    public static final String tag = "AppModel";


    private final Context context;
    private String appName;
    private Drawable appIcon;
    private String packageName;

    private final String sourceDir;

    public AppModel(Context context, String appName, Drawable appIcon, String packageName, String sourceDir) {
        this.context = context;
        this.appName =appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
        this.sourceDir =sourceDir;
    }


    public String getSourceDir() {
        return sourceDir;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public Context getContext() {
        return context;
    }



    public String getPackageName() {
        return packageName;
    }
}
