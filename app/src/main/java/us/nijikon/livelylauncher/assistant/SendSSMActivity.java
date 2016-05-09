package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import us.nijikon.livelylauncher.R;

public class SendSSMActivity extends Activity {
    EditText edit;
    TextView number;
    String phoneNumber;;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_ssm);
        edit = (EditText)findViewById(R.id.edit);
        send = (Button)findViewById(R.id.send);
        number = (TextView)findViewById(R.id.number);

        phoneNumber = getIntent().getStringExtra("NUMBER");
        number.setText(phoneNumber);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean success = sendMessage(v.getContext(),null,phoneNumber,edit.getText().toString());

//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(phoneNumber, null, edit.getText().toString(), null, null);
                Log.e("SMS:", "send success:" + success);
            }
        });

    }
    public boolean sendMessage(Context context, String sourcePhoneNumber, String destinationPhoneNumber, String message)
    {
        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(destinationPhoneNumber, null, message, null, null);
            Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(context, "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
