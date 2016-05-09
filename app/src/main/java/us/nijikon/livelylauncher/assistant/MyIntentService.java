package us.nijikon.livelylauncher.assistant;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.models.Event;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "us.nijikon.livelylauncher.action.FOO";
    private static final String ACTION_BAZ = "us.nijikon.livelylauncher.action.BAZ";
    private static final String TAG = ".MyIntentService";
    private static final int POLL_INTERVAL = 100 * 10;
    private static String[] phone;
    private static String[] personName;
    private static String text;
    private static long rowId;

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "us.nijikon.livelylauncher.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "us.nijikon.livelylauncher.extra.PARAM2";

    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent in = new Intent(this,PendingIntentActivity.class);
        in.putExtra("key",phone);
        Log.e(TAG, "phone:" + phone);
        in.putExtra("name",personName);
        Log.e(TAG, "name:" + personName);
        in.putExtra("text",text);
        Log.e(TAG, "text:" + text);
        in.putExtra("rowId", rowId);
        Log.e(TAG, "rowId:" + rowId);

        PendingIntent pi = PendingIntent.getActivity(this, (int) rowId, in, 0);

        Notification notification = new NotificationCompat.Builder(this).setTicker("Event Notify")
                .setSmallIcon(R.drawable.contact).setContentTitle("Remind From Assistant").setContentText("New coming event")
                .setContentIntent(pi).setAutoCancel(true).build();

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int)rowId, notification);

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void setServiceAlarm(Context context, boolean isOn, Date date, Event event){

        if(event.getContactPerson()!=null){
            int personCount = event.getContactPerson().size();
            phone = new String[personCount];
            personName = new String[personCount];
            for(int i=0; i<personCount; i++){

                Log.e(TAG, "PERSON:" + event.getContactPerson().size());
                Log.e(TAG, "PERSON:" + event.getContactPerson().get(i).getPhoneNumber());
                phone[i] = event.getContactPerson().get(i).getPhoneNumber();
                personName[i] = event.getContactPerson().get(i).getName();
            }

            if(!event.getType().getCategoryName().equals("Contact")){
                text = "SMS";
            }
        }

        rowId = event.getRowId();

        Intent i = new Intent(context, MyIntentService.class);
        PendingIntent pi = PendingIntent.getService(context, (int) rowId, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if(isOn) {
            long trigger = date.getTime();
//          alarmManager.setRepeating(AlarmManager.RTC, trigger, POLL_INTERVAL, pi);
            alarmManager.set(AlarmManager.RTC, trigger, pi);

        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

}
