package us.nijikon.livelylauncher;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Launcher extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private VoiceRecognitionService voiceRecognitionService;

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        textView = (TextView) findViewById(R.id.textview_display_result);
    }

    @Override
    protected void onStart () {

        voiceRecognitionService = new VoiceRecognitionService(this);

        super.onStart();
    }

    public void beginSpeaking (View view) {

        //Check if this device supports Voice Recognition Service
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
        }
        else {
            voiceRecognitionService.startListening();
        }
    }

    public void endSpeaking (View view) {
        voiceRecognitionService.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public TextView getTextView () {
        return textView;
    }

}


