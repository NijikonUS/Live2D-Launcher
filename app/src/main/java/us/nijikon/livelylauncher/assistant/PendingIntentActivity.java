package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import us.nijikon.livelylauncher.dao.LivelyLauncherDB;
import us.nijikon.livelylauncher.R;

public class PendingIntentActivity extends Activity {

    private static final String tag = "PendingIntentActivity";
    LivelyLauncherDB db;
    TextView time,note,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_intent);

        time = (TextView)findViewById(R.id.time);
        note = (TextView)findViewById(R.id.note);
        type = (TextView)findViewById(R.id.type);

        db= new LivelyLauncherDB(this);
        Cursor c = db.getLatestCursor();
        c.moveToFirst();

        Cursor event = db.queryDatabase(c.getInt(0));
        event.moveToFirst();
        note.setText(event.getString(1));
        time.setText(event.getString(2));

        Cursor typeCursor = db.queryDatabaseType(c.getInt(0));
        typeCursor.moveToFirst();
        Log.e(tag, "$$$" + typeCursor.getString(1));
        type.setText(typeCursor.getString(1));

        //Has Contact_Person
        if(getIntent().getStringArrayExtra("key")!=null){

                Log.e("FINAL", getIntent().getStringArrayExtra("key").toString());

                String[] phoneNumber = getIntent().getStringArrayExtra("key");
                String[] personName = getIntent().getStringArrayExtra("name");

                Intent intentToCall = new Intent(this,PhoneCallActivity.class);
                intentToCall.putExtra("key",phoneNumber);
                intentToCall.putExtra("name", personName);

            if(getIntent().getStringExtra("text") != null){
                intentToCall.putExtra("text",getIntent().getStringExtra("text"));
            }
            startActivity(intentToCall);
        }
        // Not Contact Not invite
        else{

        }
    }
}
