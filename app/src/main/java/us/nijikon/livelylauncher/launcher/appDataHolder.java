package us.nijikon.livelylauncher.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 *
 * this class provide app data
 * singleton pattern
 */
public class AppDataHolder {

    public static final String tag = "AppDataHolder";
    private static final String IF_WRITTEN = "IF_WRITTEN";
    private static final Comparator APP_SORT_BY_ALPHABET = new ChineseComparator();

    private static class Singleton{
        private static final AppDataHolder Instance = new AppDataHolder();
    }

    private static class ChineseComparator implements Comparator<AppModel> {
        @Override
        public int compare(AppModel lhs, AppModel rhs) {
            return Collator.getInstance(Locale.CHINA).compare(lhs.getAppName(),rhs.getAppName());

        }
    }

    /*
     * Using this function to get instance. Singleton pattern
     */
    public static final AppDataHolder getInstance(){
        return Singleton.Instance;
    }

    private List<AppModel> data;

    private AppDataHolder(){
    }


    /**
     * load from sharedPreference
     *
     * @param context
     *
     */
    public boolean loadFromFile(Context context){
        //already have data, no need to load again
        if(data != null) return true;
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.allApps), Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean(IF_WRITTEN,false)) {
            Log.d(tag, "sharedPreference is null");
            return false;
        }
        Log.d(tag, "sharedPreference is not null");
        ArrayList<AppModel> apps = new ArrayList<>();
        Set<String> set = sharedPreferences.getAll().keySet();
        Iterator<String> iterator = set.iterator();
        PackageManager packageManager = context.getPackageManager();
        while (iterator.hasNext()){
            String key = iterator.next();
            if(key.equals(IF_WRITTEN)){
                continue;
            }
            int times = sharedPreferences.getInt(key, -1);
            //String[] pack_name = key.split("::");
            ApplicationInfo info = null;
            try {
                info = packageManager.getApplicationInfo(key, PackageManager.MATCH_ALL);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                continue;
            }
            apps.add(new AppModel((String) info.loadLabel(packageManager), info.loadIcon(packageManager), key, times));
        }
        setData(apps);
        Log.d("LOAD FROM FILE", String.valueOf(data.size()));
        return true;
    }
    /**
     *  write to sharedPreference
     *  key(packageName:appName) - value(clickTimes)
     *
     * @param context
     */
    public void writeToFile(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.allApps),Context.MODE_PRIVATE).edit();
        editor.putBoolean(IF_WRITTEN, true);
        for(int i = 0; i< data.size();i++){
            AppModel app = data.get(i);
            editor.putInt(app.getPackageName() /*+ "::" + app.getAppName()*/, app.getClickTimes());
        }
        editor.apply();
        Log.d("WRITE FILE"," done");
    }

    /**
     * no record before, first load
     *
     * @param context
     *
     */
    public boolean firstLoad(Context context){
        PackageManager packageManager = context.getPackageManager();
        Intent mainIntent = new Intent();
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setAction(Intent.ACTION_MAIN);
        List<ResolveInfo> appInfoList = packageManager.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL);
        //one package, one entry
        HashMap<String,AppModel> apps = new HashMap<>();
        for(int i =0;i<appInfoList.size();i++){
            apps.put(appInfoList.get(i).activityInfo.packageName, new AppModel((String) appInfoList.get(i).loadLabel(packageManager), appInfoList.get(i).loadIcon(packageManager), appInfoList.get(i).activityInfo.packageName));
        }
        Log.d("FIRST LOAD TOTAL",String.valueOf(apps.size()));
        setData(new ArrayList<>(apps.values()));
        return true;
    }

    public List<AppModel> getData(){
        return this.data;
    }

    private void setData(List<AppModel> data){
        Collections.sort(data, APP_SORT_BY_ALPHABET);
        this.data = data;
    }

    public AppModel[] getTop4(){
        if(data == null) return null;
        AppModel[] top4 = new AppModel[4];
        //init top4
        for(int i =0 ;i< 4;i++){
            top4[i] = data.get(i);
        }

        for(int i = 4;i<data.size();i++){
            int place = 4;
            for(int j = 3;j > -1;j--){
                if(data.get(i).getClickTimes() > top4[j].getClickTimes()){
                    place--;
                }
            }
            if(place < 4){
                top4[place] = data.get(i);
            }
        }
        return top4;
    }
}
