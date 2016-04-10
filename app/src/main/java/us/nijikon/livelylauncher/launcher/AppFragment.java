package us.nijikon.livelylauncher.launcher;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import java.util.LinkedList;
import java.util.List;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.adapters.AppAdapter;
import us.nijikon.livelylauncher.adapters.Top4Adapter;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class AppFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<AppModel>>{

    public static final String tag = "AppFragment";

    private AppAdapter appAdapter;
    private Top4Adapter top4Adapter;
    private SearchView searchView;
    private RecyclerView appRecyclerView;
    private RecyclerView top4RecyclerView;
    private ListItemListener itemListener;
    private ImageButton searchButton;
    private Launcher launcher;
    private String target;
    private ItemTouchHelper touchHelper;





    public AppFragment setParent(Launcher launcher){
        this.launcher = launcher;
        return this;
   }

    public void setAppAdapterDate(List<AppModel> date){
       if(appAdapter!=null) {
           appAdapter.setData(date);
       }else{
           appAdapter = new AppAdapter();
           appAdapter.setData(date);
       }
    }

    public void setTop4AdapterData(AppModel[] data){
        if(top4Adapter!=null){
            top4Adapter.setTop4(data);
        }else{
            top4Adapter = new Top4Adapter();
            top4Adapter.setTop4(data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PackageManager packageManager = getActivity().getPackageManager();
        itemListener = new ListItemListener() {
            @Override
            public void onItemClick(AppModel data,View view, int position) {
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage(data.getPackageName());
                data.increaceClickTime();
                startActivity(intent);
            }

            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                if(viewHolder instanceof AppAdapter.AppViewHolder){
                    touchHelper.startDrag(viewHolder);
                }else{
                    // do nothing
                }
            }
        };
        if(appAdapter == null){
            appAdapter = new AppAdapter(itemListener);
            top4Adapter = new Top4Adapter(itemListener);
        }
        this.appAdapter.setListener(itemListener);
        this.top4Adapter.setListener(itemListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appfragment, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(launcher.getUsableWidth(),launcher.getUsableHeight()));

        appRecyclerView = (RecyclerView)view.findViewById(R.id.applist);
        top4RecyclerView = (RecyclerView)view.findViewById(R.id.top4list);

        top4RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        appRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        appRecyclerView.setAdapter(appAdapter);
        top4RecyclerView.setAdapter(top4Adapter);

        searchView = (SearchView)view.findViewById(R.id.searchView);
        searchView.setQuery("",false);
        searchButton = (ImageButton)view.findViewById(R.id.searchButton);

        ItemTouchHelper.Callback callback = new ItemTouchCallBackHelper(appAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(appRecyclerView);

        //

        //
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

        view.findViewById(R.id.space).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.goFragment(LauncherFragment.tag);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TEXTCHANGE", newText);
                target = newText;
                startSearchHelper();
                return false;
            }
        });



        return view;
    }

    private void startSearchHelper(){
        getLoaderManager().restartLoader(1,null,this);
    }

    @Override
    public void onResume(){
        super.onResume();
        searchView.setQuery("",false);
        appAdapter.setData(AppDataHolder.getInstance().getData());
        top4Adapter.setTop4(AppDataHolder.getInstance().getTop4());
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(tag, "on pause");
        launcher.getFragmentManager().popBackStack(tag, 0);
        launcher.goFragment(LauncherFragment.tag);
    }

    //Search loader

    @Override
    public Loader<List<AppModel>> onCreateLoader(int id, Bundle args) {
        return new AppSearchLoader(getActivity(),target);
    }

    @Override
    public void onLoadFinished(Loader<List<AppModel>> loader, List<AppModel> data) {
         appAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<AppModel>> loader) {
        loader = new AppSearchLoader(getActivity(),target);
    }

}
