package us.nijikon.livelylauncher.launcher;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class AppSearchLoader extends AsyncTaskLoader<List<AppModel>> {
    String target;

    public AppSearchLoader(Context context,String target) {
        super(context);
        this.target = target;
    }

    @Override
    public List<AppModel> loadInBackground() {
        List<AppModel> list = AppDataHolder.getInstance().getData();
        if(list == null){
            return null;
        }
        List<AppModel> res= new LinkedList<>();
        for(int i = 0;i<list.size();i++){
            if(list.get(i).getAppName().toLowerCase().contains(target.toLowerCase())){
                res.add(list.get(i));
            }
        }
        return res;
    }
    @Override
    protected void onStartLoading(){
        forceLoad();
    }
}
