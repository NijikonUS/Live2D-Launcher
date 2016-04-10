package us.nijikon.livelylauncher.launcher;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import jp.live2d.Live2D;
import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.utils.android.FileManager;
import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.VoiceRecognitionActivity;
import us.nijikon.livelylauncher.adapters.AppAdapter;
import us.nijikon.livelylauncher.assistant.TimeSelect;
import us.nijikon.livelylauncher.live2dHelpers.LAppLive2DManager;
import us.nijikon.livelylauncher.live2dHelpers.LAppView;

public class Launcher extends Activity implements LoaderManager.LoaderCallbacks<AppDataHolder>{


    private LAppLive2DManager live2DMgr;
    private FragmentManager fragmentManager;
    //private ImageButton appButton;
    private AppFragment appFragment;
    private LauncherFragment launcherFragment;
    private Fragment currentFragment;

    private   int usableHeight;
    private   int usableWidth;

    private BroadcastReceiver receiver;



    public Launcher(){
 //       instance = this;
        live2DMgr = new LAppLive2DManager();
        appFragment = new AppFragment().setParent(this);
        launcherFragment = new LauncherFragment().setParent(this);

    }


    public void goFragment(String tag){
        if(currentFragment!=null){
            if(currentFragment instanceof LauncherFragment ){
                fragmentManager.beginTransaction()
                        .setCustomAnimations(0, R.animator.fade_out)
                        .detach(currentFragment)
                        .commit();
            }else if(  currentFragment instanceof AppFragment){
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(0, R.animator.left_out)
                        .detach(currentFragment)
                        .commit();
            }
        }

            switch (tag) {
                case LauncherFragment.tag:
                    if (launcherFragment == null) {
                        launcherFragment = new LauncherFragment().setParent(this);
                        fragmentManager.beginTransaction().add(R.id.main, launcherFragment).commit();
                    }
                    fragmentManager.beginTransaction()
                            .attach(launcherFragment)
                            .show(launcherFragment)
                            .commit();
                    currentFragment = launcherFragment;
                    break;
                case AppFragment.tag:
                    if (appFragment == null) {
                        appFragment = new AppFragment().setParent(this);
                        fragmentManager.beginTransaction().add(R.id.main, appFragment).commit();
                    }
                    fragmentManager.beginTransaction()
                            .addToBackStack(AppFragment.tag)
                            .setCustomAnimations(R.animator.left_in, 0, 0, R.animator.left_out)
                            .attach(appFragment)
                            .show(appFragment)
                            .commit();
                    currentFragment = appFragment;
            }

     }

    public int getUsableHeight(){
        return usableHeight;
    }
    public int getUsableWidth(){
        return usableWidth;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        //setup screen size used for fragment

        usableWidth = getResources().getDisplayMetrics().widthPixels;
        usableHeight = getResources().getDisplayMetrics().heightPixels;

        //load data
        getLoaderManager().initLoader(0,null,this);


        fragmentManager = getFragmentManager();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("local Receiver","Gocha");
                if(appFragment!=null) {
                    appFragment.setAppAdapterDate(AppDataHolder.getInstance().getData());
                }
            }
        };
       //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("AAAAA"));


        /*
         * testing for voice reg
         */
        ImageButton testbutton = (ImageButton)findViewById(R.id.testbutton);
        final Intent i = new Intent(this, VoiceRecognitionActivity.class);
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        ImageButton testbutton2 = (ImageButton) findViewById(R.id.testbutton2);
        final Intent ii = new Intent(this, TimeSelect.class);
        testbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ii);
            }
        });



        setupGUI();
        FileManager.init(this.getApplicationContext());

        fragmentManager.beginTransaction().add(R.id.main,launcherFragment).add(R.id.main,appFragment).hide(appFragment).hide(launcherFragment).commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("LAUCNHER ACTIVITY", "ON RESUME");
        this.goFragment(LauncherFragment.tag);
        registerReceiver(receiver, new IntentFilter(getResources().getString(R.string.update)));
    }


    void setupGUI()
    {

        LAppView view = live2DMgr.createView(this) ;


        FrameLayout layout=(FrameLayout) findViewById(R.id.live2dLayout);
        layout.addView(view, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();

    }


    @Override
    protected void onPause()
    {
        fragmentManager.popBackStack(AppFragment.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        live2DMgr.onPause() ;
        AppDataHolder.getInstance().writeToFile(this);
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected  void onStop(){
        super.onStop();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

    }

    //loader
    @Override
    public Loader<AppDataHolder> onCreateLoader(int id, Bundle args) {
        return new AppLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<AppDataHolder> loader, AppDataHolder data) {
        appFragment.setAppAdapterDate(data.getData());
        appFragment.setTop4AdapterData(data.getTop4());
    }

    @Override
    public void onLoaderReset(Loader<AppDataHolder> loader) {
        loader = null;
    }


}
