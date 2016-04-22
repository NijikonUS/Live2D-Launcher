package us.nijikon.livelylauncher.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mjwei on 4/7/16.
 */
public class Category {

    private ArrayList<Person> contactPerson;
    private NoteArea note;
    private List<String> activity ;

    public Category(){
        contactPerson = null;
        note = null;
        activity = null;
    }

    public void setContactPerson(ArrayList<Person> contactPerson){
        this.contactPerson = contactPerson;
    }

    public void setNote(NoteArea note){
        this.note = note;
    }

    public void setActivity(List<String> activity){
        this.activity = activity;
    }

}
