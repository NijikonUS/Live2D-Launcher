package us.nijikon.livelylauncher.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.models.Person;

/**
 * Created by bowang .
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.PeopleItem>{

    public static final String tag = "ContactAdapter";
    /*
     * this enum contains all colors may be used to fill background of contact photo
     * {@code Colors.Any} would return a randomly chosen color by hashcode
     */
    public  enum Colors{
        Red(0xFFF44336),
        Pink(0xFFE91E63),
        Orange(0xFFFF9800),
        DeepPurple(0xFF673AB7),
        Indigo(0xFF3F51B5),
        Blue(0xFF2196F3),
        Green(0xFF4CAF50),
        Brown(0xFF795548),
        Lime(0xFFCDDC39),
        Amber(0xFFFFC107),
        Any(0);
        private final int color;
        Colors(int color){
            this.color = color;
        }
        public int getColor(int hashCode){
            if(this == Colors.Any){
                int hash = Math.abs(hashCode) % 10;
                Colors[] colors = Colors.values();
                return colors[hash].getColor(0);
            }else {
                return this.color;
            }
        }
    }

    private ArrayList<Person> data;
    private Context context;
    private Hashtable<String,Person> result;

    private int Type;
    public static final int tiny = 1;
    public static final int normal = 0;

    private static final String[] CONTACT_PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };
    private static final String[] PHONENUMBER_PROJECTION = {
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private  static final String SELECT = "((" + ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " NOTNULL) AND ("
            + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
            + ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " != '' ))";

    public ContactAdapter(Context context, int type){
        this.Type = type;
        this.context = context;
        data = new ArrayList<>();
        result = new Hashtable<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,CONTACT_PROJECTION,
                SELECT,null,null);
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phoneNumber = phoneNumberHelper(id);
            Uri uri = getPhotoURI(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
            data.add(new Person(name,0,phoneNumber,uri));
        }
        cursor.close();
    }
    public ContactAdapter(Context context, int type, ArrayList<Person> data){
        this.Type = type;
        this.context = context;
        this.data = data;
        result = new Hashtable<>();
    }

    /*
     * return selected results
     */
    public Hashtable<String, Person> getResult() {
        return result;
    }

    /**
     * return phone number of {@code contactID}
     * if do not have a phone number return {@code null}
      */
    public String phoneNumberHelper(String contactID){
        String phone = null;
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONENUMBER_PROJECTION,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID, null, null);
        if(cursor!=null && cursor.moveToFirst()){
            phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursor.close();
        return phone;
    }

    /*
     * get photo Uri
     */
    public Uri getPhotoURI(String contactID){
        return Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)), ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
    }

    /*
     * return {@code InputStream} of given Uri
     * if no file found there return null
     */
    public InputStream photoHelper(Uri uri) {
        Uri photoUri = uri;
        try {
            AssetFileDescriptor fd = context.getContentResolver().openAssetFileDescriptor(photoUri, "r");
            return fd.createInputStream();
        } catch(IOException e) {
            return null;
        }
    }

    /**
     * tiny for ViewTripActivity normal for ShowContactActivity
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){
        if(this.Type == tiny) return tiny;
        else return normal;
    }

    @Override
    public PeopleItem onCreateViewHolder(ViewGroup parent, int viewType) {
   //     if(viewType == normal) {
            return new PeopleItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.people_card, parent, false));
//        }else{
//            return new PeopleItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.tiny_people_item, parent, false));
//        }
    }

    @Override
    public void onBindViewHolder(final PeopleItem holder, int position) {
            final Person one = data.get(position);
            //recover the checkbox state before
        if(holder.ifSelected!=null) {
            holder.ifSelected.setOnCheckedChangeListener(null);
            holder.ifSelected.setChecked(result.containsKey(one.getPhotoUri().toString()));
        }
            holder.contactName.setText(one.getName());
            holder.phoneNumber.setText(one.getPhoneNumber());

            InputStream is = photoHelper(one.getPhotoUri());
            int thisColor = Colors.Any.getColor(one.getName().hashCode());

            holder.phoneIcon.setBackgroundColor(thisColor);
            if(is!=null){
                Log.d(tag, "HAS PHOTO!!");
                holder.contactImage.setImageBitmap(BitmapFactory.decodeStream(is));

            }else{
                holder.contactImage.setBackgroundColor(thisColor);
                holder.contactImage.setImageResource(R.drawable.contact_people);
                Log.d(tag, "NO PHOTO!!");
            }

        // checkbox event
        if(holder.ifSelected!=null) {
            holder.ifSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //holder.ifSelected.setSelected(true);
                        result.put(one.getPhotoUri().toString(), one);
                    } else {
                        result.remove(one.getPhotoUri().toString());
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class PeopleItem extends RecyclerView.ViewHolder{
        private CardView peopleCard;
        private ImageView contactImage;
        private ImageView phoneIcon;
        private TextView contactName;
        private TextView phoneNumber;
        private CheckBox ifSelected;
        public PeopleItem(View itemView) {
            super(itemView);
            phoneIcon = (ImageView) itemView.findViewById(R.id.phoneIcon);
            peopleCard = (CardView)itemView.findViewById(R.id.singleContact);
            contactImage = (ImageView)itemView.findViewById(R.id.contactImage);
            contactName = (TextView)itemView.findViewById(R.id.contactName);
            ifSelected = (CheckBox)itemView.findViewById(R.id.ifSelected);
            phoneNumber = (TextView) itemView.findViewById(R.id.phoneNumber);
        }
    }
}
