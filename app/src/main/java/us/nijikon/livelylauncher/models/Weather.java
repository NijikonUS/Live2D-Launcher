package us.nijikon.livelylauncher.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ray on 2016/5/7.
 */
public class Weather implements Parcelable {
    private final int weatherId;
    private final String weatherMain;
    private final String weatherDescription;
    private final double currentTemperature;
    private final double minTemperature;
    private final double maxTemperature;
    private final String windSpeed;
    private final String windDegree;
    private final String clouds;
    private final long time;

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        public Weather createFromParcel(Parcel p) {
            return new Weather(p);
        }

        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    public Weather(Parcel p) {
        this.weatherId = p.readInt();
        this.weatherMain = p.readString();
        this.weatherDescription = p.readString();
        this.currentTemperature = p.readDouble();
        this.minTemperature = p.readDouble();
        this.maxTemperature = p.readDouble();
        this.windSpeed = p.readString();
        this.windDegree = p.readString();
        this.clouds = p.readString();
        this.time = p.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(weatherId);
        dest.writeString(weatherMain);
        dest.writeString(weatherDescription);
        dest.writeDouble(currentTemperature);
        dest.writeDouble(minTemperature);
        dest.writeDouble(maxTemperature);
        dest.writeString(windSpeed);
        dest.writeString(windDegree);
        dest.writeString(clouds);
        dest.writeLong(time);
    }

    public Weather(String weatherString) throws JSONException {
        JSONObject weather = new JSONObject(weatherString);
        JSONObject weatherJson = weather.getJSONArray("weather").getJSONObject(0);
        weatherId = weatherJson.getInt("id");
        weatherMain = weatherJson.getString("main");
        weatherDescription = weatherJson.getString("description");
        currentTemperature = weather.getJSONObject("main").getDouble("temp");
        minTemperature = weather.getJSONObject("main").getDouble("temp_min");
        maxTemperature = weather.getJSONObject("main").getDouble("temp_max");
        windSpeed = weather.getJSONObject("wind").getString("speed");
        windDegree = weather.getJSONObject("wind").getString("deg");
        clouds = weather.getJSONObject("clouds").getString("all");
        time = weather.getLong("dt");
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDegree() {
        return windDegree;
    }

    public String getClouds() {
        return clouds;
    }

    public long getTime() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
