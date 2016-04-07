package us.nijikon.livelylauncher.assistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import us.nijikon.livelylauncher.R;

public class ContactActivity extends Activity {

    private ListView lstv;
    private Button btn;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String[] from = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, c, from, to);
        lstv = (ListView) findViewById(R.id.listView);
        btn = (Button) findViewById(R.id.button);
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RemindActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }
}
