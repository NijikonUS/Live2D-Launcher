package us.nijikon.livelylauncher.launcher;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.adapters.AppAdapter;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class AppFragment extends Fragment{

    static final String tag = "AppFragment";

    private AppAdapter appAdapter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ListItemListener itemListener;
    private ImageButton searchButton;
    private Launcher launcher;
    private int height;
    private int width;



    public void setParent(Launcher launcher){
       this.launcher = launcher;
   }
    public void setAppAdapter(AppAdapter appAdapter){
        this.appAdapter = appAdapter;
    }
    public void setAppAdapterDate(List<AppModel> date){
       if(appAdapter!=null) {
           appAdapter.setData(date);
       }else{
           appAdapter = new AppAdapter();
           appAdapter.setData(date);
       }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PackageManager packageManager = getActivity().getPackageManager();
        itemListener = new ListItemListener() {
            @Override
            public void onItemClick(List<AppModel> data,View view, int position) {
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage(data.get(position).getPackageName());
                startActivity(intent);
            }
        };
        if(appAdapter == null){
            appAdapter = new AppAdapter(itemListener);
        }
        this.appAdapter.setListener(itemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appfragment, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(launcher.getUsableWidth(),launcher.getUsableHeight()));


        recyclerView = (RecyclerView)view.findViewById(R.id.applist);
        searchView = (SearchView)view.findViewById(R.id.searchView);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        searchButton = (ImageButton)view.findViewById(R.id.searchButton);
        recyclerView.setAdapter(appAdapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        return view;
    }
    @Override
    public void onPause(){
        super.onPause();

        ((ImageButton)(getActivity().findViewById(R.id.appButton))).setEnabled(true);
        ((ImageButton)(getActivity().findViewById(R.id.appButton))).setVisibility(View.VISIBLE);
    }


}
