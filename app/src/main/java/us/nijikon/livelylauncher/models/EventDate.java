package us.nijikon.livelylauncher.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mjwei on 4/8/16.
 */
public class EventDate implements Parcelable {

    public static final Creator<EventDate> CREATOR = new Creator<EventDate>() {
        public EventDate createFromParcel(Parcel p) {
            return new EventDate(p);
        }

        public EventDate[] newArray(int size) {
            return new EventDate[size];
        }
    };

    private String date;

    public EventDate(String date) {
        this.date = date;
    }

    public EventDate(Parcel p) {
        date = p.readString();
    }

    public String getDate(){
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(date);
    }
}
