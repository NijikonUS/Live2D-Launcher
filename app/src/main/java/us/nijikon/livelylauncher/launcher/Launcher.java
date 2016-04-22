package us.nijikon.livelylauncher.launcher;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.live2d.utils.android.FileManager;
import us.nijikon.livelylauncher.assistant.CategoryFragment;
import us.nijikon.livelylauncher.assistant.NoteFragment;
import us.nijikon.livelylauncher.assistant.RemindFragment;
import us.nijikon.livelylauncher.assistant.TimeSelectFragment;
import us.nijikon.livelylauncher.dao.LivelyLauncherDB;
import us.nijikon.livelylauncher.assistant.MyIntentService;
import us.nijikon.livelylauncher.R;

//import us.nijikon.livelylauncher.assistant.ContactActivity;
import us.nijikon.livelylauncher.assistant.ItemFragment;

import us.nijikon.livelylauncher.assistant.ShowContactFragment;

import us.nijikon.livelylauncher.live2dHelpers.LAppDefine;
import us.nijikon.livelylauncher.live2dHelpers.LAppLive2DManager;
import us.nijikon.livelylauncher.live2dHelpers.LAppView;
import us.nijikon.livelylauncher.models.Event;
import us.nijikon.livelylauncher.models.Person;
import us.nijikon.livelylauncher.models.Type;

public class Launcher extends Activity implements LoaderManager.LoaderCallbacks<AppDataHolder>,TimeSelectFragment.OnClickAtFrameListener, CategoryFragment.OnClickAtFrameListener,
        ShowContactFragment.OnClickAtFrameListener, NoteFragment.OnClickAtFrameListener, RemindFragment.OnClickAtFrameListener,
        ItemFragment.OnListFragmentInteractionListener {


    private static final String TAG = ".AssistActivity";

    private LAppLive2DManager live2DMgr;
    private FragmentManager fragmentManager;
    //private ImageButton appButton;
    private AppFragment appFragment;
    private LauncherFragment launcherFragment;
    private Fragment currentFragment;

    private int usableHeight;
    private int usableWidth;
    private BroadcastReceiver receiver;
    private BroadcastReceiver speechReceiver;
    //assistant
    public Event event;
    public Event sevent;
    Date date;
    LivelyLauncherDB db;

    public Event getScrEvent(){
        sevent = new Event();
        return sevent;
    }



    public LAppLive2DManager getLive2DMgr() {
        return live2DMgr;
    }

    public Launcher() {
        //       instance = this;
        live2DMgr = new LAppLive2DManager();
        appFragment = new AppFragment().setParent(this);
        launcherFragment = new LauncherFragment().setParent(this);
        // this.deleteDatabase("assistDataBase");

    }


    public void goFragment(String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (currentFragment != null) {
            if (currentFragment instanceof LauncherFragment) {
                transaction.setCustomAnimations(0, R.animator.fade_out)
                        .detach(currentFragment);
            } else if (currentFragment instanceof AppFragment) {
                fragmentManager.popBackStack();
                transaction.setCustomAnimations(0, R.animator.left_out)
                        .detach(currentFragment);
            }
            else if(currentFragment instanceof ItemFragment){
                fragmentManager.popBackStack();
                transaction.setCustomAnimations(0,R.animator.fade_out)
                        .show(currentFragment)
                        .remove(currentFragment);
                //goFragment(LauncherFragment.tag);
            }
            else
//            if(currentFragment instanceof TimeSelect
//                    || currentFragment instanceof ShowContactFragment
//                    || currentFragment instanceof Note
//                    || currentFragment instanceof RemindActivity)
            {
                // fragmentManager.popBackStack();
                transaction.setCustomAnimations(0,R.animator.fade_out)
                        .show(currentFragment)
                        .remove(currentFragment);
            }
        }

        switch (tag) {
            case LauncherFragment.tag:
                if (launcherFragment == null) {
                    launcherFragment = new LauncherFragment().setParent(this);
                    transaction.add(R.id.main, launcherFragment);
                }
                transaction.attach(launcherFragment)
                        .show(launcherFragment);
                currentFragment = launcherFragment;
                break;
            case AppFragment.tag:
                if (appFragment == null) {
                    appFragment = new AppFragment().setParent(this);
                    transaction.add(R.id.main, appFragment);
                }
                transaction.addToBackStack(AppFragment.tag)
                        .setCustomAnimations(R.animator.left_in, 0, 0, R.animator.left_out)
                        .attach(appFragment)
                        .show(appFragment);
                currentFragment = appFragment;
                break;
            case TimeSelectFragment.tag:
                TimeSelectFragment timeSelect = new TimeSelectFragment();
                transaction
                        //.addToBackStack(TimeSelect.tag)
                        .setCustomAnimations(R.animator.right_in, 0)
                        .add(R.id.main, timeSelect)
                        .show(timeSelect);
                currentFragment = timeSelect;
                break;
            case CategoryFragment.TAG:
                CategoryFragment categoryActivity = new CategoryFragment();
                transaction
                        //.addToBackStack(CategoryActivity.TAG)
                        .setCustomAnimations(R.animator.right_in, 0)
                        .add(R.id.main,categoryActivity)
                        .show(categoryActivity);
                currentFragment = categoryActivity;
                break;
            case NoteFragment.tag:
                NoteFragment note = new NoteFragment();
                transaction
                        //.addToBackStack(Note.tag)
                        .setCustomAnimations(R.animator.right_in, 0)
                        .add(R.id.main,note)
                        .show(note);
                currentFragment = note;
                break;
            case RemindFragment.tag:
                RemindFragment remindActivity = new RemindFragment();
                transaction
                        //.addToBackStack(RemindActivity.tag)
                        .setCustomAnimations(R.animator.right_in, 0)
                        .add(R.id.main,remindActivity)
                        .show(remindActivity);
                currentFragment = remindActivity;
                break;
            case ItemFragment.TAG:
                ItemFragment itemFragment = (new ItemFragment()).setParents(this);
                transaction
                        .addToBackStack(ItemFragment.TAG)
                        .setCustomAnimations(R.animator.right_in, 0)
                        .add(R.id.main, itemFragment)
                        .show(itemFragment);
                currentFragment = itemFragment;
                break;
            case ShowContactFragment.tag:
                ShowContactFragment showContactFragment = (new ShowContactFragment()).setParent(this);
                transaction
                        //.addToBackStack(showContactFragment.tag)
                        .setCustomAnimations(R.animator.right_in, 0)
                        .add(R.id.main, showContactFragment)
                        .show(showContactFragment);
                currentFragment = showContactFragment;
                break;

        }
        transaction.commit();
    }

    public int getUsableHeight() {
        return usableHeight;
    }

    public int getUsableWidth() {
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
        getLoaderManager().initLoader(0, null, this);


        fragmentManager = getFragmentManager();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("local Receiver", "Gotcha");
                if (appFragment != null) {
                    appFragment.setAppAdapterDate(AppDataHolder.getInstance().getData());
                }
            }
        };

        speechReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("Speeach Receiver", "Gotcha");
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(speechReceiver, new IntentFilter("AAAAA"));


        /*
         * testing for voice reg
         */
//        ImageButton testbutton = (ImageButton) findViewById(R.id.testbutton);
//
//        testbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(i);
//                //LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(new Intent("AAAAA"));
//                live2DMgr.startMotion(LAppDefine.MOTION_ANGRY);
//            }
//        });
//        ImageButton testbutton2 = (ImageButton) findViewById(R.id.testbutton2);
//        final Intent ii = new Intent(this, TimeSelect.class);
//        testbutton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(ii);
//            }
//        });


/*
 * set up live2D
 */

        setupGUI();


        FileManager.init(this.getApplicationContext());
        fragmentManager.beginTransaction().add(R.id.main, launcherFragment).add(R.id.main, appFragment).hide(appFragment).hide(launcherFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LAUCNHER ACTIVITY", "ON RESUME");
        this.goFragment(LauncherFragment.tag);
        registerReceiver(receiver, new IntentFilter(getResources().getString(R.string.update)));
    }


    void setupGUI() {
        LAppView view = live2DMgr.createView(this);
        FrameLayout layout = (FrameLayout) findViewById(R.id.live2dLayout);
        layout.addView(view, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onPause() {
        fragmentManager.popBackStack(AppFragment.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        live2DMgr.onPause();
        AppDataHolder.getInstance().writeToFile(this);
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
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

    }

    // callback


    //call back function from frame
    @Override
    public void saveDate(Event event,Date date) {
        this.event =event;
        Log.e(TAG, this.event.getDate());
        this.date = date;
        Log.e(TAG, date.toString());

    }

    @Override
    public void savePerson(List<Person> selectedPersonList) {
        event.setContactPerson(selectedPersonList);
        for(int i=0;i<selectedPersonList.size();i++){
            Log.e(TAG, this.event.getContactPerson().get(i).getName());
        }
    }

    @Override
    public void saveNote(String note) {

        event.setNote(note);
        Log.e(TAG, this.event.getNote());
    }

    @Override
    public void saveType(String name) {
        event.setType(new Type(name));
        Log.e("save_type", event.getType().getCategoryName());

    }

    @Override
    public void saveRemind(int remindBefore) {

        event.setRemindBefore(remindBefore);
        //long alermTime= (date.getTime()- event.getRemindBefore()*1000*60)/1000*60;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -remindBefore);
        long alermTime = calendar.getTime().getTime();

        Log.e(TAG, "" + new Date(alermTime).toString() + "");
        Log.e("CHECK DB:", event.getDate() + "/" + event.getEventId() + "/" + event.getType().getCategoryName() + "/" + event.getRemindBefore());

        testAlerm(new Date(alermTime), event);
        Log.e(TAG, "set alerm");

    }


    @Override
    public void saveToDatebase() {
        if(db == null){
            db = new LivelyLauncherDB(this);
        }
        long id = db.insertType(db.insertEvent(event),event.getType());
        Log.e("insert DB:", event.getDate() + "/" + event.getEventId() + "/" + event.getType().getCategoryName() + "/" + event.getRemindBefore());

        if(event.getType().getCategoryName().equals("Contact")){
            for(int i =0; i<event.getContactPerson().size(); i++){

                db.insertPerson(id, event.getContactPerson().get(i));
            }
        }
    }

    public void saveToDatebase(Event event) {
        if(db == null){
            db = new LivelyLauncherDB(this);
        }
        long id = db.insertType(db.insertEvent(event),event.getType());
        Log.e("insert DB:", event.getDate() + "/" + event.getEventId() + "/" + event.getType().getCategoryName() + "/" + event.getRemindBefore());

        if(event.getType().getCategoryName().equals("Contact")){
            for(int i =0; i<event.getContactPerson().size(); i++){

                db.insertPerson(id, event.getContactPerson().get(i));
            }
        }
    }

    @Override
    public void onListFragmentInteraction() {

    }

    public void testAlerm(Date date, Event event){
        MyIntentService.setServiceAlarm(this, true, date, event);
    }
}
