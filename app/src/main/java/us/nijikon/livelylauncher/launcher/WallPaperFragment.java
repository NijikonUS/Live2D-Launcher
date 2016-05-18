package us.nijikon.livelylauncher.launcher;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.XMLFormatter;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.adapters.WallPaperAdapter;
import us.nijikon.livelylauncher.live2dHelpers.LAppDefine;

/**
 * Created by bowang .
 */
public class WallPaperFragment extends Fragment{

    public static final String tag = "WallPaperFragment";
    private Launcher launcher;
    private RecyclerView recyclerView;
    private Button defaultButton;
    private Button galleryButton;
    private WallPaperAdapter adapter;

    public WallPaperFragment setParent(Launcher launcher){
        this.launcher = launcher;
        return this;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallpaper, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(launcher.getUsableWidth(), launcher.getUsableHeight()));
        defaultButton = (Button)view.findViewById(R.id.defaultButton);
        galleryButton = (Button)view.findViewById(R.id.galleryButton);

        adapter = new WallPaperAdapter(launcher);
        recyclerView = (RecyclerView)view.findViewById(R.id.wallpapers);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setItemViewCacheSize(3);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_SETTLING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ((WallPaperAdapter) recyclerView.getAdapter()).getTaskList().executeAll();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx == 0) {
                    // execute all tasks when dx == 0
                    ((WallPaperAdapter) recyclerView.getAdapter()).getTaskList().executeAll();
                }
            }

        });
        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultButton.setBackgroundColor(getResources().getColor(R.color.primaryBack));
                galleryButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                LAppDefine.inApp = true;
                //switch list
                try {
                    adapter.setPaths(Arrays.asList(getResources().getAssets().list("image")),true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                galleryButton.setBackgroundColor(getResources().getColor(R.color.primaryBack));
                //switch list
                ArrayList<String> paths = new ArrayList<String>();
                Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media.DATA,MediaStore.Images.Media.MIME_TYPE}
                        ,null,null,MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                while(cursor.moveToNext()){
                    String x = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    paths.add(x);
                }
                cursor.close();
                adapter.setPaths(paths,false);

            }
        });

        return view;
    }
}
