package us.nijikon.livelylauncher.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

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
    }

    private ArrayList<AppModel> data;

    public AppAdapter(ArrayList<AppModel> apps){
        this.data = apps;
    }

    public void setData(ArrayList<AppModel> data){
        this.data = data;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AppViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_cardview,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        final AppModel app = data.get(i);
        appViewHolder.appName.setText(app.getAppName());
        appViewHolder.appIcon.setImageDrawable(app.getAppIcon());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



}
