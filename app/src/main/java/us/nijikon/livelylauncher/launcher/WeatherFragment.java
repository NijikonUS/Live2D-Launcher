package us.nijikon.livelylauncher.launcher;

import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.models.Weather;
import us.nijikon.livelylauncher.speech.RecognitionUtil;

/**
 * Created by bowang .
 */
public class WeatherFragment extends Fragment{

    private static final String URL_TO_WEATHER =
            "http://api.openweathermap.org/data/2.5/weather?";

    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;

    private TextView weatherConditionView;
    private TextView currentTemperatureView;
    private TextView minTemperatureView;
    private TextView maxTemperatureView;
    private TextView windSpeedView;
    private TextView windDegreeView;
    private ImageView weatherImageView;
    private Weather weather;

    public static final String tag = "WeatherFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherConditionView = (TextView) view.findViewById(R.id.text_weather_condition);
        currentTemperatureView = (TextView) view.findViewById(R.id.text_current_temperature);
        minTemperatureView = (TextView) view.findViewById(R.id.text_min_temperature);
        maxTemperatureView = (TextView) view.findViewById(R.id.text_max_temperature);
        windSpeedView = (TextView) view.findViewById(R.id.text_wind_speed);
        windDegreeView = (TextView) view.findViewById(R.id.text_wind_degree);
        weatherImageView = (ImageView) view.findViewById(R.id.image_weather_condition);


//        new AsyncTask<Void, Void, Weather>() {
//            @Override
//            protected Weather doInBackground(Void... params) {
//                try {
//                    return new Weather(requestGet(getUrlToWeather(RecognitionUtil.getLocation(getActivity()))));
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(Weather weather) {
//                super.onPostExecute(weather);
//                Log.d(tag, "weather:" + weather.getWeatherMain());
//                showWeather(weather);
//            }
//        }.execute();

        if(weather != null) {
            showWeather(weather);
        }

        return view;

    }

    public void setWeather(Weather weather){
        this.weather = weather;
    }

    public void closeFragment (View v) {
        Toast.makeText(getActivity(),"sdafsdafsfsf",Toast.LENGTH_SHORT).show();
    }

    private String getUrlToWeather(Location location) {
        return URL_TO_WEATHER +
                "lat=" +
                location.getLatitude() +
                "&lon=" +
                location.getLongitude() +
                "&appid=" +
                getString(R.string.open_weather_map_app_id) +
                "&units=imperial&type=accurate";
    }

    private String requestGet (final String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Log.d("Get URL", url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        String response = client.newCall(request).execute().body().string();
        Log.d("Response", response);
        return response;
    }

    private void showWeather (Weather weather) {
        int id = weather.getWeatherId();
        String updateTime = convertUnixTimeToNormal(weather.getTime());

        //weather condition
        if (id < 300 && id >= 200) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.little_rain_lightning, null));
        }
        else if ((id >= 300 && id < 400) || (id >= 502 && id <= 520)) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.much_rain, null));
        }
        else if (id == 500 || id == 501) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.little_rain, null));
        }
        else if (id > 520 && id < 600) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.extremly_much_rain, null));
        }
        else if (id >= 600 && id <= 601) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.little_snow, null));
        }
        else if (id >= 602 && id <= 611) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.much_snow, null));
        }
        else if (id > 611 && id < 700) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.extremly_much_snow, null));
        }
        else if (id >= 700 && id < 800) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.flog, null));
        }
        else if (id == 800) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.sun, null));
        }
        else if (id == 801) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.cloud, null));
        }
        else if (id > 801 && id < 900) {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.much_cloud, null));
        }
        else {
            weatherImageView.setImageDrawable(getResources().
                    getDrawable(R.drawable.extreme, null));
        }

        weatherConditionView.
                setText(getString(R.string.weather_condition, weather.getWeatherMain()));

        currentTemperatureView.
                setText(getString(R.string.current_temperature, weather.getCurrentTemperature()));

        minTemperatureView.
                setText(getString(R.string.min_temperature, weather.getMinTemperature()));

        maxTemperatureView.
                setText(getString(R.string.max_temperature, weather.getMaxTemperature()));


        windSpeedView.
                setText(getString(R.string.wind_speed, weather.getWindSpeed()));

        windDegreeView.
                setText(getString(R.string.wind_degree, weather.getWindDegree()));
    }

    private String convertUnixTimeToNormal(long time) {
        String result = "";
        long day = time / SECONDS_PER_DAY;
        time = time % SECONDS_PER_DAY;
        long hour = time / SECONDS_PER_HOUR;
        time = time % SECONDS_PER_HOUR;
        long minute = time / SECONDS_PER_MINUTE;
        time = time % SECONDS_PER_MINUTE;
        long second = time;
        result = validTime(result, day, "day") +
                validTime(result, hour, "hour") +
                validTime(result, minute, "minute") +
                validTime(result, second, "second");
        return result;
    }

    private String validTime (String result, long time, String timeName) {
        if (time != 0) {
            if (time == 1) {
                result = time + " " + timeName + " ";
            }
            else {
                result = time + " " + timeName + "s ";
            }
        }
        return result;
    }
}
