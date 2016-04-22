package us.nijikon.livelylauncher.assistant;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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
    private static final int POLL_INTERVAL = 100 * 10;
    private static String phone;

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
//        if (intent != null) {
////            final String action = intent.getAction();
////            if (ACTION_FOO.equals(action)) {
////                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
////                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
////                handleActionFoo(param1, param2);
////            } else if (ACTION_BAZ.equals(action)) {
////                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
////                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
////                handleActionBaz(param1, param2);
////            }
//        }

        //Resources r = getResources();
        Intent in = new Intent(this,PendingIntentActivity.class);
        in.putExtra("key",phone);

        PendingIntent pi = PendingIntent.getActivity(this, 0, in, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this).setTicker("Event Alert")
                .setSmallIcon(R.drawable.contact).setContentTitle("Event Alert").setContentText("New coming event")
                .setContentIntent(pi).setAutoCancel(true).build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

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
            phone = event.getContactPerson().get(0).getPhoneNumber();
        }

        Intent i = new Intent(context, MyIntentService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

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
