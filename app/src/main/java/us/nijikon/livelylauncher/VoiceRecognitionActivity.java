package us.nijikon.livelylauncher;

import android.app.Activity;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class VoiceRecognitionActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private VoiceRecognitionService voiceRecognitionService;

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recognition);
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

    public TextView getTextView () {
        return textView;
    }

}


