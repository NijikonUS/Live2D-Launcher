package us.nijikon.livelylauncher.launcher;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.adapters.AppAdapter;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class AppFragment extends Fragment {

    static final String tag = "AppFragment";

    private AppAdapter appAdapter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ListItemListener itemListener;
    private ImageButton searchButton;


    public AppFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appfragment, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.applist);
        searchView = (SearchView)view.findViewById(R.id.searchView);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        searchButton = (ImageButton)view.findViewById(R.id.searchButton);
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

        final PackageManager packageManager = getActivity().getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appInfoList = packageManager.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL);
        final ArrayList<AppModel> apps = new ArrayList<>();
        for (int i = 0; i < appInfoList.size(); i++) {
                ResolveInfo resolveInfo = appInfoList.get(i);
                apps.add(new AppModel(getActivity(), (String)resolveInfo.loadLabel(packageManager), resolveInfo.loadIcon(packageManager), resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.applicationInfo.sourceDir));

        }
        // set itemListener
        itemListener = new ListItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage(apps.get(position).getPackageName());
                startActivity(intent);
            }
        };


        appAdapter = new AppAdapter(apps,itemListener);
        recyclerView.setAdapter(appAdapter);
        ((ImageButton)(getActivity().findViewById(R.id.appButton))).setEnabled(true);
        return view;
    }
    @Override
    public void onPause(){
        super.onPause();
        ((ImageButton)(getActivity().findViewById(R.id.appButton))).setVisibility(View.VISIBLE);

    }



}
