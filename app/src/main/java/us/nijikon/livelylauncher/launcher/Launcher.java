package us.nijikon.livelylauncher.launcher;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    private ImageButton appButton;
    private AppFragment appFragment;

    private   int usableHeight;
    private   int usableWidth;



    public Launcher(){
 //       instance = this;
        live2DMgr = new LAppLive2DManager();
        appFragment = new AppFragment();
        appFragment.setParent(this);


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
        appButton = (ImageButton)findViewById(R.id.appButton);
        appButton.setEnabled(true);
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // avoid multiple clicking
                appButton.setEnabled(false);
                appButton.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.main, appFragment);
                fragmentTransaction.addToBackStack(AppFragment.tag);
                fragmentTransaction.setCustomAnimations(R.animator.left_in,0).show(appFragment);
                fragmentTransaction.commit();

            }
        });


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
        fragmentManager.popBackStack(AppFragment.tag,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        live2DMgr.onPause() ;
        super.onPause();
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
