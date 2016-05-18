package us.nijikon.livelylauncher.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mjwei on 3/27/16.
 */
public class Person implements Parcelable {

    // Member fields should exist here, what else do you need for a person?
    // Please add additional fields
    private String name;
    private String email;
    private String phoneNumber;
    private long personId;
    private Uri mPhotoUri;

    /**
     * Parcelable creator. Do not modify this function.
     */
    public static final Creator<Person> CREATOR = new Creator<Person>() {
        public Person createFromParcel(Parcel p) {
            return new Person(p);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    /**
     * Create a Person model object from a Parcel. This
     * function is called via the Parcelable creator.
     *
     * @param p The Parcel used to populate the
     * Model fields.
     */
    public Person(Parcel p) {

        name = p.readString();
        email = p.readString();
        phoneNumber = p.readString();
        personId = p.readLong();
    }

    /**
     * Create a Person model object from arguments
     *
     * @param name Add arbitrary number of arguments to
     * instantiate Person class based on member variables.
     */
    public Person(String name,String location, String phoneNumber, Long personId) {

        this.name = name;
        this.email = location;
        this.phoneNumber = phoneNumber;
        this.personId = personId;
    }

    public Person(String name, long id, String phoneNumber){
        this.name = name;
        this.personId = id;
        this.phoneNumber = phoneNumber;
    }
    public Person(String name, long id, String phoneNumber,Uri uri){
        this.name = name;
        this.personId = id;
        this.phoneNumber = phoneNumber;
        this.mPhotoUri = uri;
    }

    public String getName(){

        return name;
    }
    public String getEmail(){

        return email;
    }
    public String getPhoneNumber(){

        return phoneNumber;
    }
    public long getpersonId(){

        return personId;
    }

    public Uri getPhotoUri() {
        return mPhotoUri;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // TODO - fill in here
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeLong(personId);
    }

    /**
     * Feel free to add additional functions as necessary below.
     */

    /**
     * Do not implement
     */
    @Override
    public int describeContents() {
        // Do not implement!
        return 0;
    }
}
