package us.nijikon.livelylauncher.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.launcher.AppDataHolder;
import us.nijikon.livelylauncher.launcher.AppLoader;
import us.nijikon.livelylauncher.launcher.ListItemListener;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> implements ItemTouchHelperAdapter{

    public static final String tag = "AppAdapter";

    public static class AppViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private ImageView appIcon;
        private TextView appName;
        public AppViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.appCard);
            appIcon = (ImageView)itemView.findViewById(R.id.appIcon);
            appName = (TextView)itemView.findViewById(R.id.appName);
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getAppIcon() {
            return appIcon;
        }

        public TextView getAppName() {
            return appName;
        }
    }

    private List<AppModel> data;
    private ListItemListener listener;

    public AppAdapter(){}

    public AppAdapter(ArrayList<AppModel> apps, ListItemListener listener){
        this.data = apps;
        this.listener = listener;
    }

    public AppAdapter(ListItemListener listener){
        this.listener = listener;
    }

    public void setListener(ListItemListener listener){
        this.listener = listener;
    }

    public void setData(List<AppModel> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AppViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_cardview,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(final AppViewHolder appViewHolder, final int i) {
        final AppModel app = data.get(i);
        appViewHolder.appName.setText(app.getAppName());
        appViewHolder.appIcon.setImageDrawable(app.getAppIcon());
      //  appViewHolder.cardView.onTouchEvent(new MotionEvent(){})
        appViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callback
                listener.onItemClick(app, v, i);
            }
        });
        //drag event
        appViewHolder.cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    listener.onStartDrag(appViewHolder);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
      //  Log.d(tag, String.valueOf(data.size()));
        if(data!=null)
            return data.size();
        return 0;
    }
    // ItemTouchHelperAdapter

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }


    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);

    }

   @Override
    public void onRefresh(int position){
       this.notifyItemChanged(position);
   }


}
