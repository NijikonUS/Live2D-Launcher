package us.nijikon.livelylauncher.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.launcher.ListItemListener;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class Top4Adapter extends RecyclerView.Adapter<Top4Adapter.AppViewHolder>{

    private AppModel[] top4;

    private ListItemListener listener;

    public static class AppViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout item;
        private ImageView appIcon;
        private TextView appName;
        public AppViewHolder(View itemView) {
            super(itemView);
            item = (LinearLayout)itemView.findViewById(R.id.appGrid);
            appIcon = (ImageView)itemView.findViewById(R.id.appIcon);
            appName = (TextView)itemView.findViewById(R.id.appName);
        }
    }

    public Top4Adapter(){}

    public Top4Adapter(ListItemListener listener){
        this.listener = listener;
    }

    public void setListener(ListItemListener listener){
        this.listener = listener;
    }

    public void setTop4(AppModel[] data){
        top4 = data;
        notifyDataSetChanged();
    }

    @Override
    public Top4Adapter.AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new AppViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.app_single_grid,parent,false));
    }

    @Override
    public void onBindViewHolder(Top4Adapter.AppViewHolder holder, final int position) {
        final AppModel one = top4[position];
        holder.appIcon.setImageDrawable(one.getAppIcon());
        holder.appName.setText(one.getAppName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callback
                listener.onItemClick(one ,v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(top4 == null)
            return 0;
        return 4;
    }
}
