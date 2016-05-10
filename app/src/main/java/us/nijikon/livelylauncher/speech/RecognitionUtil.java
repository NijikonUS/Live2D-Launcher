package us.nijikon.livelylauncher.speech;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.launcher.Launcher;
import us.nijikon.livelylauncher.models.Person;

/**
 * Created by Ray on 2016/5/8.
 */
public class RecognitionUtil {

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String URL_TO_WEATHER =
            "http://api.openweathermap.org/data/2.5/weather?";

    public static String requestGet (final String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Log.d("Get URL", url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        String response = client.newCall(request).execute().body().string();
        Log.d("Response", response);
        return response;
    }

    public static String getUrlToWeather(Location location, Context context) {
        return URL_TO_WEATHER +
                "lat=" +
                location.getLatitude() +
                "&lon=" +
                location.getLongitude() +
                "&appid=" +
                context.getString(R.string.open_weather_map_app_id) +
                "&units=imperial&type=accurate";
    }

    public static Location getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
//        locationManager.requestLocationUpdates(provider, 0, 0, new OnLocationListener());
        Log.d("fadsfnasdkfas",String.valueOf(locationManager.getLastKnownLocation(provider).getLongitude()));
        return locationManager.getLastKnownLocation(provider);
    }

    public static String personExists (Map<String, String> contactMap, String name, Context launcher) {
        if (contactMap != null && contactMap.containsKey(name)) {
            return contactMap.get(name);
        }
        return null;
//
//        List<Person> personList = new ArrayList<>();
//        String[] from = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
//        int[] to = {android.R.id.text1, android.R.id.text2};
//        final Cursor c = launcher.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, from, null, null, null);
//
//        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//            String t = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
//            final Cursor pCur = launcher.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + t, null, null);
//
//            String phone = "";
//            while (pCur.moveToNext()) {
//                int code = Integer.valueOf(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
//                switch (code) {
//                    case 1://Home
//                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        //Log.e("checkPhone1:", phone);
//                        break;
//                    case 2://Mobile
//                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        //Log.e("checkPhone2:", phone);
//                        break;
//                    case 3://Work
//                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        //Log.e("checkPhone3:", phone);
//                        break;
//                }
//            }
//            pCur.close();
//
//            Person p = new Person(c.getString(0), Long.parseLong(c.getString(1)), phone);
//            personList.add(p);
//
//            if(name != null && name.equals(c.getString(0))){
//                return phone;
//            }
//
//        }
//        return null;
    }

    public static void call(String number, Context launcher){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + saveNumberOnly(number)));
        try {
            launcher.startActivity(callIntent);
//            launcher.finish();
            Log.e("start call :", "_____");
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e("OOPS!", "" + ex.getMessage());
        }
    }

    public static String saveNumberOnly (String phoneNumber) {
        String result = "";
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (phoneNumber.charAt(i) >= 48 && phoneNumber.charAt(i) <= 57) {
                result += phoneNumber.charAt(i);
            }
        }
        Log.d("PhoneNumber", result);
        return result;
    }

    public static boolean sendMessage(String number,String message){

        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(saveNumberOnly(number), null, message, null, null);
//            Toast.makeText(this, "SMS sent.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
//            Toast.makeText(this, "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getName (String[] words, int count, int length) {
        String name = "";
        for (int i = count; i < length; i++) {
            name += " " + convertToName(words[i]);
        }
        return name.substring(1);
    }

    public static String convertToName (String lowercaseName) {
        return lowercaseName.substring(0, 1).toUpperCase() + lowercaseName.substring(1);
    }
}
