package us.nijikon.livelylauncher.launcher;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private AppAdapter appAdapter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appfragment, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.applist);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> appInfoList = pm.getInstalledApplications(0);
        ArrayList<AppModel> apps = new ArrayList<>();
        for (int i = 0; i < appInfoList.size(); i++) {
            apps.add(new AppModel(getActivity(), appInfoList.get(i)));
        }
        appAdapter = new AppAdapter(apps);
        recyclerView.setAdapter(appAdapter);
        ((ImageButton)(getActivity().findViewById(R.id.appButton))).setEnabled(true);
        return view;
    }



}
