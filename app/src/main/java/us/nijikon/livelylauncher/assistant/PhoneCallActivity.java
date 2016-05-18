package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import us.nijikon.livelylauncher.R;

public class PhoneCallActivity extends Activity {

    //ArrayList<String> contact;
    private static final String TAG = ".PhoneCallActivity";
    private static final int REQUEST_CALL = 1;
    private String[] phone;
    private String[] personName;
    private String number,toSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);

        ListView lstv = (ListView) findViewById(R.id.listView);

        phone = getIntent().getStringArrayExtra("key");
        personName = getIntent().getStringArrayExtra("name");
        toSMS = getIntent().getStringExtra("text");

        ArrayAdapter<String> listAd = new ArrayAdapter<>(PhoneCallActivity.this, android.R.layout.simple_list_item_1, personName);
        lstv.setAdapter(listAd);

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = personName[position];
                number = phone[position];

                if(toSMS==null){
                    selectContactPerson(view);
                }else{
                    sendSMS(view);
                }


            }
        });
    }

    public void selectContactPerson(View view){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try {
            startActivityForResult(callIntent, REQUEST_CALL);
            //finish();
            Log.e("start call :", "_____");
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e("OOPS!", "" + ex.getMessage());
        }
    }

    public void sendSMS(View view){
        Log.e(TAG, "SUCCESS!");
        Intent sendMsgIntent = new Intent(this,SendSSMActivity.class);
        sendMsgIntent.putExtra("NUMBER",number);
        startActivity(sendMsgIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
