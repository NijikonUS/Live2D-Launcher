package us.nijikon.livelylauncher.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.launcher.ListItemListener;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    public static final String TAG = "AppAdapter";

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
    }

    private ArrayList<AppModel> data;
    private final ListItemListener listener;


    public AppAdapter(ArrayList<AppModel> apps, ListItemListener listener){
        this.data = apps;
        this.listener = listener;
    }

    public void setData(ArrayList<AppModel> data){
        this.data = data;
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
        appViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,i);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG,String.valueOf(data.size()));
        return data.size();
    }



}
