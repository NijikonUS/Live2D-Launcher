package us.nijikon.livelylauncher.models;

import java.util.ArrayList;

import us.nijikon.livelylauncher.adapters.ImageAsyncTaskLoader;

/**
 * Created by bowang .
 */
public class AsycTaskList {
    ArrayList<ImageAsyncTaskLoader> list;
   public AsycTaskList(){
        list = new ArrayList<>();
    }
    public void executeAll(){
        for(int i = 0;i < list.size();i++){
            list.get(i).execute(list.get(i).getPath());
        }
        list.clear();
    }

    public void add(ImageAsyncTaskLoader t){
        list.add(t);
    }
}
