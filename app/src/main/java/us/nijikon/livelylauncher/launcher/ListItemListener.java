package us.nijikon.livelylauncher.launcher;

import android.view.View;

/**
 * Created by bowang .
 */
public interface ListItemListener {
    /*
     *  callback for onClick event in list item
     *  help to manager behavior in controller
     */
    void onItemClick(View view,int position);
}
