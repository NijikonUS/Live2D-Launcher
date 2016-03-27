package us.nijikon.livelylauncher;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactActivity extends Activity {

    private ListView lstv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String[] from = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, c, from, to);
        lstv = (ListView) findViewById(R.id.listView);
        lstv.setAdapter(adapter);

        lstv.setTextFilterEnabled(true);
        lstv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Adapter selAdapter = parent.getAdapter();
                Object selected = selAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), selected.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
