package us.nijikon.livelylauncher.launcher;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public interface ListItemListener {
    /**
     *  callback for onClick event in list item
     *  help to manager behavior in controller
     */
    void onItemClick(AppModel data,View view,int position);
    /**
     * callback for Drag event
     *
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
