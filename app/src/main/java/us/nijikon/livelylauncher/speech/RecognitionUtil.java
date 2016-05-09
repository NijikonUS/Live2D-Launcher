package us.nijikon.livelylauncher.speech;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import us.nijikon.livelylauncher.R;

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
        return locationManager.getLastKnownLocation(provider);
    }
}
