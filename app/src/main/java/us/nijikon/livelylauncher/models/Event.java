package us.nijikon.livelylauncher.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by mjwei on 4/7/16.
 */
public class Event implements Parcelable {

    int eventId;
    String date;
    Type type;
    int remindBefore;
    List<Person> contactPerson=null;
    String note=null;


    public static final Creator<Event> CREATOR = new Creator<Event>() {
        public Event createFromParcel(Parcel p) {
            return new Event(p);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(Parcel p) {

        note = p.readString();
        eventId = p.readInt();
        remindBefore = p.readInt();
    }
    public Event(){
        //remindType = 0;
        remindBefore = 0;
    }

    public Event(int eventId,String date, String note, int remindBefore, List<Person> contactPerson, Type type) {

        this.eventId = eventId;
        this.date = date;
        this.note = note;
        //this.remindType = remindType;
        this.remindBefore = remindBefore;
        this.contactPerson = contactPerson;
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override

    public void writeToParcel(Parcel dest, int flags) {

    }
    public int getEventId() {
        return eventId;
    }

    public List<Person> getContactPerson() {
        return contactPerson;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public Type getType(){
        return type;
    }


    public int getRemindBefore() {
        return remindBefore;
    }

    //Setter
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public void setType(Type type){
        this.type = type;
    }

    public void setNote(String note) {
        this.note = note;
    }

//    public void setRemindType(int remindType) {
//        this.remindType = remindType;
//    }

    public void setRemindBefore(int remindBefore) {
        this.remindBefore = remindBefore;
    }

    public void setContactPerson(List<Person> contactPerson){
        this.contactPerson = contactPerson;
    }
}
