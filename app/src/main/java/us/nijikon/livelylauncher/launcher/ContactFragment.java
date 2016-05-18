package us.nijikon.livelylauncher.launcher;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.models.Event;
import us.nijikon.livelylauncher.models.Person;

public class ContactFragment extends Fragment {

    private ListView lstv;
    private Button btn;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private List<Person> personList;
    private List<Integer> checkedItem;
    private List<Person> selectedPersonList;
    private Event event;

    OnClickAtFrameListener callback;

    public interface OnClickAtFrameListener {
        void savePerson(List<Person> selectedPersonList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        callback = (OnClickAtFrameListener)activity;
    }

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.activity_contact, container, false);

        personList=new ArrayList<>();

        String[] from = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
        int[] to = {android.R.id.text1, android.R.id.text2};
        final Cursor c = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, from, null, null, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String t = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            final Cursor pCur = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + t, null, null);
            String phone = "";
            while (pCur.moveToNext()) {
                int code = Integer.valueOf(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
                switch(code){
                    case 1://Home
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.e("checkPhone1:", phone);
                        break;
                    case 2://Mobile
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.e("checkPhone2:", phone);
                        break;
                    case 3://Work
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.e("checkPhone3:", phone);
                        break;
                }
            }
            pCur.close();

            Person p = new Person(c.getString(0), Long.parseLong(c.getString(1)),phone);
            //Log.e("check_@@:",p.getName()+":"+p.getPhoneNumber());
            personList.add(p);

        }


        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, c, from, to);
        lstv = (ListView) v.findViewById(R.id.listView);
        btn = (Button) v.findViewById(R.id.button);
        lstv.setAdapter(adapter);

        lstv.setTextFilterEnabled(true);
        lstv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        checkedItem=new ArrayList<Integer>();

        lstv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Adapter selAdapter = parent.getAdapter();
                //Object selected = selAdapter.getItem(position);
                checkedItem.add(position);
                Toast.makeText(getActivity().getApplicationContext(), Integer.toString(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                checkedItem.add(0);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPersonList = new ArrayList<>();

                int number = checkedItem.size();
                Log.e("size!!", Integer.toString(number));

                Iterator<Integer> select = checkedItem.iterator();
                while (select.hasNext()) {
                    Person rr = personList.get(select.next());
                    selectedPersonList.add(rr);

                }
//                event = new Event();
//                event.setContactPerson(selectedPersonList);
//                Log.e("event save :", event.getContactPerson().get(0).getName());

                if (selectedPersonList != null) {
                    callback.savePerson(selectedPersonList);
                }


              //  ((Launcher) getActivity()).;
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
