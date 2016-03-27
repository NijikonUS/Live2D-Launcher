package us.nijikon.livelylauncher.launcher;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import jp.live2d.Live2D;
import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.utils.android.FileManager;
import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.live2dHelpers.LAppLive2DManager;
import us.nijikon.livelylauncher.live2dHelpers.LAppView;

public class Launcher extends Activity {
    private LAppLive2DManager live2DMgr;
    private FragmentManager fragmentManager;
    private ImageButton appButton;
    private AppFragment appFragment;


    public Launcher(){
 //       instance = this;
        live2DMgr = new LAppLive2DManager();
        appFragment = new AppFragment();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause()
    {
        fragmentManager.popBackStack(AppFragment.tag,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        live2DMgr.onPause() ;
        super.onPause();
    }
}
