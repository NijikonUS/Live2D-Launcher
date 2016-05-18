package us.nijikon.livelylauncher.launcher;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import us.nijikon.livelylauncher.live2dHelpers.LAppLive2DManager;
import us.nijikon.livelylauncher.live2dHelpers.LAppView;
import us.nijikon.livelylauncher.models.Event;
import us.nijikon.livelylauncher.models.Person;
import us.nijikon.livelylauncher.models.Type;
import us.nijikon.livelylauncher.models.Weather;

public class Launcher extends Activity implements LoaderManager.LoaderCallbacks<AppDataHolder>,TimeSelectFragment.OnClickAtFrameListener, CategoryFragment.OnClickAtFrameListener,
        ShowContactFragment.OnClickAtFrameListener, NoteFragment.OnClickAtFrameListener, RemindFragment.OnClickAtFrameListener,
        ItemFragment.OnListFragmentInteractionListener {


    private static final String TAG = ".AssistActivity";

    private LAppLive2DManager live2DMgr;
    private FragmentManager fragmentManager;
    private AppFragment appFragment;
    private LauncherFragment launcherFragment;
    private Fragment currentFragment;
    private TimeSelectFragment timeSelectFragment;
    private CategoryFragment categoryFragment;
    private NoteFragment noteFragment;
    private RemindFragment remindFragment;
    private ItemFragment itemFragment;
    private ShowContactFragment showContactFragment;
    private WeatherFragment weatherFragment;
    private WallPaperFragment wallPaperFragment;
    private Weather currentWeather;
    private HashMap<String, String> simpleContactInfo;

    private int usableHeight;
    private int usableWidth;
    //assistant
    public Event event;
    Date date;

    public HashMap<String, String> getSimpleContactInfo() {
        return simpleContactInfo;
    }

    public LAppLive2DManager getLive2DMgr() {
        return live2DMgr;
    }

    public Launcher() {
        //       instance = this;
        live2DMgr = new LAppLive2DManager();

     //   appFragment = new AppFragment().setParent(this);
    //    launcherFragment = new LauncherFragment().setParent(this);
        // this.deleteDatabase("assistDataBase");

    }


    public Fragment goFragment(String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (tag) {
            case LauncherFragment.tag:
                if (launcherFragment == null) {
                    launcherFragment = new LauncherFragment().setParent(this);
                }
                if(!launcherFragment.isAdded()){
                    transaction.add(R.id.main, launcherFragment);
                }
               // transaction.attach(launcherFragment);
                if(currentFragment != null) {
                    if (currentFragment instanceof AppFragment) {
                        // leave appFragment
                        //getFragmentManager().popBackStack(AppFragment.tag,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        transaction.setCustomAnimations(R.animator.fade_in, R.animator.left_out);
                        transaction.hide(currentFragment).show(launcherFragment);
                    } else if(currentFragment instanceof LauncherFragment){
                        // do nothing
                        return currentFragment;
                    } else {
                        // leave assistant fragments
                        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                        transaction.hide(currentFragment).show(launcherFragment);
                    }

                }else {
                    transaction.show(launcherFragment);
                }
                transaction.commit();
                currentFragment = launcherFragment;
                //TODO clean the event or init other stuffs
                break;

            case AppFragment.tag:
                //if (appFragment == null) {
                    appFragment = new AppFragment().setParent(this);
              //  }
                if(!appFragment.isAdded()){
                    transaction.add(R.id.main, appFragment);
                }
                //currentFragment must be launcherFragment
                transaction//.addToBackStack(AppFragment.tag)
                            .setCustomAnimations(R.animator.left_in, R.animator.fade_out);
                              transaction.hide(launcherFragment);
                              transaction.show(appFragment);
                              transaction.commit();
                currentFragment = appFragment;
                break;
            case TimeSelectFragment.tag:
                if(timeSelectFragment == null){
                    timeSelectFragment = new TimeSelectFragment();
                }
                if(!timeSelectFragment.isAdded()){
                    transaction.add(R.id.main, timeSelectFragment);
                }
                transaction
                        //.addToBackStack(TimeSelect.tag)
                        .setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
                transaction.show(timeSelectFragment);
                transaction.hide(currentFragment);
                transaction.commit();
                currentFragment = timeSelectFragment;
                break;

            case CategoryFragment.tag:
                if(categoryFragment == null) {
                    categoryFragment = new CategoryFragment();
                }
                if(!categoryFragment.isAdded()){
                    transaction.add(R.id.main,categoryFragment);
                }
                transaction
                        //.addToBackStack(CategoryActivity.TAG)
                        .setCustomAnimations(R.animator.fade_in, 0)
                        .remove(currentFragment)
                        .show(categoryFragment).commit();
                currentFragment = categoryFragment;
                break;

            case NoteFragment.tag:
                if(noteFragment == null) {
                    noteFragment = new NoteFragment();
                }
                if(!noteFragment.isAdded()){
                    transaction.add(R.id.main,noteFragment);
                }
                transaction
                        //.addToBackStack(Note.tag)
                        .setCustomAnimations(R.animator.fade_in, 0)
                        .remove(currentFragment)
                        .show(noteFragment).commit();
                currentFragment = noteFragment;
                break;

            case RemindFragment.tag:
                if(remindFragment == null) {
                    remindFragment = new RemindFragment();
                }
                if(!remindFragment.isAdded()){
                    transaction.add(R.id.main,remindFragment);
                }
                transaction
                        //.addToBackStack(RemindActivity.tag)
                        .setCustomAnimations(R.animator.fade_in, 0)
                        .remove(currentFragment)
                        .show(remindFragment).commit();
                currentFragment = remindFragment;
                break;

            case ItemFragment.TAG:
                if(itemFragment == null) {
                     itemFragment = (new ItemFragment()).setParents(this);
                }
                if(!itemFragment.isAdded()){
                    transaction.add(R.id.main,itemFragment);
                }
                transaction.setCustomAnimations(R.animator.fade_in, 0);
                if(currentFragment instanceof RemindFragment) {
                    // call manually
                    transaction.remove(currentFragment);
                }else {
                    //call by voice
                    transaction.hide(currentFragment);
                }
                transaction.show(itemFragment).commit();
                currentFragment = itemFragment;

                break;

            case ShowContactFragment.tag:
                if(showContactFragment == null) {
                     showContactFragment = (new ShowContactFragment()).setParent(this);
                }
                if(!showContactFragment.isAdded()){
                    transaction.add(R.id.main,showContactFragment);
                }
                transaction.setCustomAnimations(R.animator.fade_in, 0)
                        .remove(currentFragment)
                        .show(showContactFragment).commit();
                currentFragment = showContactFragment;
                break;

            case WeatherFragment.tag:
                if(weatherFragment ==null){
                    weatherFragment = new WeatherFragment();
                }
                if(!weatherFragment.isAdded()){
                    transaction.add(R.id.hookView,weatherFragment);
                }
                //transaction.addToBackStack(WeatherFragment.tag);
                transaction.hide(currentFragment);
                transaction.commit();
                currentFragment =  weatherFragment;
                break;
            case WallPaperFragment.tag:
                if(wallPaperFragment == null){
                    wallPaperFragment = new WallPaperFragment().setParent(this);
                }
                if(!wallPaperFragment.isAdded()){
                    transaction.add(R.id.main, wallPaperFragment);
                }
                transaction.show(wallPaperFragment);
                transaction.hide(currentFragment);
                transaction.commit();
                currentFragment = wallPaperFragment;
                break;
        }
        return currentFragment;
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
        LAppLive2DManager.readWallPaper(this);

        //setup screen size used for fragment

        usableWidth = getResources().getDisplayMetrics().widthPixels;
        usableHeight = getResources().getDisplayMetrics().heightPixels;

        // init fragment manager
        fragmentManager =  getFragmentManager();
        //load data
        getLoaderManager().initLoader(0, null, this);

        // set up live2D
        setupGUI();
        FileManager.init(this.getApplicationContext());

        //prepare data
        new SimpleContactInfoTask().execute();
        // go
       // this.goFragment(LauncherFragment.tag);
        //fragmentManager.beginTransaction().add(R.id.main, launcherFragment).add(R.id.main, appFragment).hide(appFragment).hide(launcherFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LAUCNHER ACTIVITY", "ON RESUME");
        //if(currentFragment == null) {
            this.goFragment(LauncherFragment.tag);
        //}
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        if(currentFragment instanceof WallPaperFragment){
            getFragmentManager().beginTransaction().remove(currentFragment).commit();
            wallPaperFragment = null;
        }
        if(currentFragment instanceof WeatherFragment){
            getFragmentManager().beginTransaction().remove(currentFragment).commit();
            weatherFragment = null;
        }
        if(!(currentFragment instanceof LauncherFragment)){
            goFragment(LauncherFragment.tag);
        }
       // categoryFragment = null;
        //noteFragment = null;
        //timeSelectFragment = null;
        //remindFragment = null;
        itemFragment = null;

    }


    public void setupGUI() {
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
      //fragmentManager.popBackStack(AppFragment.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        live2DMgr.onPause();
        AppDataHolder.getInstance().writeToFile(this);
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
        if(appFragment!=null) {
            appFragment.setAppAdapterDate(data.getData());
            appFragment.setTop4AdapterData(data.getTop4());
        }
    }

    @Override
    public void onLoaderReset(Loader<AppDataHolder> loader) {

    }

    // callback
    // call back function from frame
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


    }


    @Override
    public void saveToDatebase() {
        if(event == null) return;
        LivelyLauncherDB db = new LivelyLauncherDB(this);

        long insertEventId = db.insertEvent(event);
        event.setRowId(insertEventId);

        long id = db.insertType(insertEventId,event.getType());

        Log.e(TAG, "insertDB:" + event.getDate() + "/" + event.getRowId() + "/" + event.getType().getCategoryName() + "/" + event.getRemindBefore());

        if(event.getType().getCategoryName().equals("Contact") && event.getContactPerson()!=null){
            Cursor c = db.getLatestCursor();
            c.moveToFirst();
            int queryId = c.getInt(0);
            Log.e(TAG,"queryId:"+queryId);

            for(int i =0; i<event.getContactPerson().size(); i++){
                db.insertPerson(queryId, event.getContactPerson().get(i));
                Log.e(TAG,"insert person:"+event.getContactPerson().get(i).getName());
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -event.getRemindBefore());
        long alermTime = calendar.getTime().getTime();

        testAlerm(new Date(alermTime), event);
        db.close();
    }

    @Override
    public void onListFragmentInteraction() {

    }

    public void testAlerm(Date date, Event event){
        MyIntentService.setServiceAlarm(this, true, date, event);
    }


    //pre load contacts info
    public HashMap<String,String> getContactInform(){
        HashMap<String,String> res = new HashMap<>();
        final Cursor c = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            String t = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            final Cursor pCur = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + t, null, null);
            String phone = "";
            while (pCur.moveToNext()) {
                int code = Integer.valueOf(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
                switch(code){
                    case 1://Home
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.e("checkPhone1:", phone);
                        break;
                    case 2://Mobile
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.e("checkPhone2:", phone);
                        break;
                    case 3://Work
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.e("checkPhone3:", phone);
                        break;
                }
            }
            pCur.close();
            res.put(c.getString(0).toLowerCase(),phone.toLowerCase());
        }
        return res;
    }

    private class SimpleContactInfoTask extends AsyncTask<Void,Void,HashMap<String,String>>{

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {
            return getContactInform();
        }

        @Override
        protected void onPostExecute(HashMap<String, String> v){
            simpleContactInfo = v;
            Log.d("laod contact","done");
        }
    }


    public void saveToDatebase(Event event) {
        if(event == null) return;
        LivelyLauncherDB db = new LivelyLauncherDB(this);

        long insertEventId = db.insertEvent(event);
        event.setRowId(insertEventId);

        long id = db.insertType(insertEventId,event.getType());

        Log.e(TAG, "insertDB:" + event.getDate() + "/" + event.getRowId() + "/" + event.getType().getCategoryName() + "/" + event.getRemindBefore());

        if(event.getType().getCategoryName().equals("Contact") && event.getContactPerson()!=null){
            Cursor c = db.getLatestCursor();
            c.moveToFirst();
            int queryId = c.getInt(0);
            Log.e(TAG,"queryId:"+queryId);

            for(int i =0; i<event.getContactPerson().size(); i++){
                db.insertPerson(queryId, event.getContactPerson().get(i));
                Log.e(TAG,"insert person:"+event.getContactPerson().get(i).getName());
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -5);
        long alermTime = calendar.getTime().getTime();

        testAlerm(new Date(alermTime), event);
        db.close();
    }

    public void setDate(Date date){
        this.date = date;
    }

}
