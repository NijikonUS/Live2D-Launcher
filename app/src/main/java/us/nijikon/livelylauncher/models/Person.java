package us.nijikon.livelylauncher.models;

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
    private long id;
    /**
     * Parcelable creator. Do not modify this function.
     */
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
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
        id = p.readLong();
    }

    /**
     * Create a Person model object from arguments
     *
     * @param name Add arbitrary number of arguments to
     * instantiate Person class based on member variables.
     */
    public Person(String name,String location, String phoneNumber, Long age) {

        this.name = name;
        this.email = location;
        this.phoneNumber = phoneNumber;
        this.id = age;
    }

    public String getName(){

        return name;
    }
    public String getLocation(){

        return email;
    }
    public String getPhoneNumber(){

        return phoneNumber;
    }
    public long getAge(){

        return id;
    }

    /**
     * Serialize Person object by using writeToParcel.
     * This function is automatically called by the
     * system when the object is serialized.
     *
     * @param dest Parcel object that gets written on
     * serialization. Use functions to write out the
     * object stored via your member variables.
     *
     * @param flags Additional flags about how the object
     * should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     * In our case, you should be just passing 0.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // TODO - fill in here
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeLong(id);
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
