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

    private final ApplicationInfo info;
    private final Context context;
    private String appName;
    private Drawable appIcon;

    private final File appFile;

    public AppModel(Context context, ApplicationInfo info) {
        this.context = context;
        this.info = info;
        this.appFile = new File(info.sourceDir);
        appIcon = info.loadIcon(context.getPackageManager());
        appName = info.loadLabel(context.getPackageManager()).toString();
    }


    public File getAppFile() {
        return appFile;
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

    public ApplicationInfo getInfo() {
        return info;
    }
}
