package us.nijikon.livelylauncher.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import us.nijikon.livelylauncher.models.Event;
import us.nijikon.livelylauncher.models.Person;
import us.nijikon.livelylauncher.models.Type;

/**
 * Created by mjwei on 3/31/16.
 */
public class LivelyLauncherDB extends SQLiteOpenHelper {

    //version
    private static final int VERSION = 1;

    //database name
    private static final String DATABASE_NAME = "assistDataBase";

    //column of event
    private static final String TABLE_EVENT = "event";
    private static final String COLUMN_EVENT_ID = "id";
    private static final String COLUMN_EVENT_NOTE = "note";
    private static final String COLUMN_EVENT_DATE = "date";
    private static final String COLUMN_EVENT_REMIND_BEFORE = "remind_before";
    private static final String COLUMN_EVENT_ROW_NUMBER = "row_number";

    //column of person
    private static final String TABLE_PERSON = "person";
    private static final String COLUMN_PERSON_EVENTID = "person_event_id";
    private static final String COLUMN_PERSON_NAME = "person_name";
    private static final String COLUMN_PERSON_PHONE = "phone";
    private static final String COLUMN_PERSON_EMAIL = "email";

    //column of categoryType
    private static final String TABLE_TYPE = "type";
    private static final String COLUMN_TYPE_EVENTID = "type_event_id";
    private static final String COLUMN_TYPE_NAME = "type_name";


    //Construct the constructor to initiate the name and version of the database

    public LivelyLauncherDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_EVENT + "(" + COLUMN_EVENT_ID + " integer primary key autoincrement, "
                + COLUMN_EVENT_NOTE + " text, "
                //+ COLUMN_EVENT_REMIND + " integer, "
                + COLUMN_EVENT_DATE + " text, "
                + COLUMN_EVENT_REMIND_BEFORE + " integer, "
                + COLUMN_EVENT_ROW_NUMBER + " real)");

        Log.i(DATABASE_NAME, "finish create table");


        //create type table
        sqLiteDatabase.execSQL("create table " + TABLE_TYPE + "(" + COLUMN_TYPE_EVENTID + " integer references event(_id), "
                + COLUMN_TYPE_NAME + " text) ");

        //create person table
        sqLiteDatabase.execSQL("create table " + TABLE_PERSON + "(" + COLUMN_PERSON_EVENTID + " integer references event(_id), "
                + COLUMN_PERSON_NAME + " text, "
                + COLUMN_PERSON_PHONE + " text, "
                + COLUMN_PERSON_EMAIL + " text) ");

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);

        onCreate(sqLiteDatabase);
    }

    public long insertEvent(Event event){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EVENT_NOTE, event.getNote());
        cv.put(COLUMN_EVENT_DATE, event.getDate());
        cv.put(COLUMN_EVENT_REMIND_BEFORE, event.getRemindBefore());
        long rowNumber = getWritableDatabase().insert(TABLE_EVENT,null,cv);

//        ContentValues cv2 = new ContentValues();
//        cv2.put(COLUMN_EVENT_NOTE, event.getNote());
//        cv2.put(COLUMN_EVENT_DATE, event.getDate());
//        cv2.put(COLUMN_EVENT_REMIND_BEFORE, event.getRemindBefore());
//        cv2.put(COLUMN_EVENT_ROW_NUMBER, rowNumber);
//        getReadableDatabase().update(TABLE_EVENT, cv2, " where " + COLUMN_EVENT_DATE + "=" + event.getDate(), null);
        return rowNumber;

    }


    public long insertPerson(int eventId, Person person){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PERSON_EVENTID, eventId);
        cv.put(COLUMN_PERSON_NAME,person.getName());
        cv.put(COLUMN_PERSON_PHONE, person.getPhoneNumber());
        cv.put(COLUMN_PERSON_EMAIL, person.getEmail());
        return getWritableDatabase().insert(TABLE_PERSON,null,cv);

    }

    public long insertType(long eventId, Type type){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TYPE_EVENTID, eventId);
        cv.put(COLUMN_TYPE_NAME,type.getCategoryName());
        return getWritableDatabase().insert(TABLE_TYPE,null,cv);

    }

    public long addNewType(String typeName){

        ContentValues cv = new ContentValues();
        //cv.put(COLUMN_TYPE_EVENTID, eventId);
        cv.put(COLUMN_TYPE_NAME,typeName);
        return getWritableDatabase().insert(TABLE_TYPE,null,cv);

    }


    public Cursor queryDatabase(int id){
        Cursor c = getReadableDatabase().rawQuery("select * from " + TABLE_EVENT
                    + " where " + COLUMN_EVENT_ID + "=" + Integer.toString(id)
                , null);
        return c;
    }


    public Cursor queryDatabasePerson(int id){
        Cursor c = getReadableDatabase().rawQuery("select * from " + TABLE_PERSON
                + " where " + COLUMN_PERSON_EVENTID + "=" + Integer.toString(id)
                , null);
        return c;
    }

    public Cursor queryDatabaseType(int id){
        Cursor c = getReadableDatabase().rawQuery("select * from " + TABLE_TYPE
                + " where " + COLUMN_TYPE_EVENTID + "=" + Integer.toString(id)
                , null);
        return c;
    }

    public boolean queryTypeExistance(String categoryName){
        Cursor c = getReadableDatabase().rawQuery("select * from " + TABLE_TYPE
                + " where " + COLUMN_TYPE_NAME + "=" + categoryName
                , null);
        if(c ==null) return false;
        else return true;
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> eventLists = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_EVENT, null);
        // loop through all query results
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Event event = new Event();
            event.setEventId(cursor.getInt(0));
            event.setNote(cursor.getString(1));
            event.setDate(cursor.getString(2));
            event.setRemindBefore(cursor.getInt(3));
            //event.setRowId(cursor.getLong(4));
            eventLists.add(event);

        }
        return eventLists;
    }

    public ArrayList<Type> getAllTypes() {
        ArrayList<Type> typeLists = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_TYPE, null);
        // loop through all query results
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Type type = new Type();
            type.setTypeId(cursor.getLong(0));
            type.setTypeName(cursor.getString(1));
            typeLists.add(type);

        }
        return typeLists;
    }

    public Cursor getLatestCursor(){
        String sql ="select "+ COLUMN_EVENT_ID+ " from "+ TABLE_EVENT+ " order by "+ COLUMN_EVENT_ID+ " DESC limit 1";
        return getReadableDatabase().rawQuery(sql,null,null);
    }


}
