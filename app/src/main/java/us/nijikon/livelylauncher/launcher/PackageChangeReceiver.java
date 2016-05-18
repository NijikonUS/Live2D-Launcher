package us.nijikon.livelylauncher.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import us.nijikon.livelylauncher.R;

/**
 * Created by bowang .
 */
public class PackageChangeReceiver extends BroadcastReceiver{
    public static final String tag = "PackageChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
         if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)
                 || intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)
                 ||intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)){
             AppDataHolder holder = AppDataHolder.getInstance();
             holder.update(context);
             holder.writeToFile(context);
             Log.d(tag, "update profile now sending local broadcast to update data set " + intent.getAction());
             Intent update = new Intent(context.getResources().getString(R.string.update));
            // update.setAction();
            // LocalBroadcastManager.getInstance(context).sendBroadcast(update);
             context.sendBroadcast(update);
         }

    }
}
