package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import us.nijikon.livelylauncher.dao.LivelyLauncherDB;
import us.nijikon.livelylauncher.R;

public class PendingIntentActivity extends Activity {

    LivelyLauncherDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_intent);

        String phoneNumber = getIntent().getStringExtra("key");

        if(phoneNumber!=null){
            Log.e("FINAL", phoneNumber);

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            try {
                startActivity(callIntent);
                finish();
                Log.e("start call :", "");
            } catch (android.content.ActivityNotFoundException ex) {
                Log.e("OOPS!", "" + ex.getMessage());
            }

        }else{
            Log.e("Do nothing :", "$$$");
        }

    }
}
