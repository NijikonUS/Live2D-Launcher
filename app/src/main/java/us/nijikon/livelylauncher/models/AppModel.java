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



    private String appName;
    private Drawable appIcon;
    private String packageName;
    private int clickTimes;
  //  private final String sourceDir;

    public AppModel(String appName, Drawable appIcon, String packageName) {
        this.appName =appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
        clickTimes = 1;
  //      this.sourceDir =sourceDir;
    }

    public AppModel(String appName, Drawable appIcon, String packageName,int clickTimes) {
        this.appName =appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
        this.clickTimes = clickTimes;
        //      this.sourceDir =sourceDir;
    }


   // public String getSourceDir() {
   //     return sourceDir;
   //}

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getClickTimes() {
        return clickTimes;
    }

    public void increaceClickTime(){
        this.clickTimes += 1;
    }
}
