package us.nijikon.livelylauncher;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ray on 2016/3/27.
 */
public class VoiceRecognitionService {
    private static final String TAG = Launcher.class.getName();

    private final SpeechRecognizer speechRecognizer;

    private final SpeechListener speechLitsener;

    private final Intent speechIntent;

    private final TextView textView;

    public VoiceRecognitionService(Launcher launcher) {
        textView = launcher.getTextView();

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(launcher);
        speechLitsener = new SpeechListener();
        speechRecognizer.setRecognitionListener(speechLitsener);
        speechIntent = new Intent();

        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"us.nijikon.livelylauncher");

        // Given an hint to the recognizer about what the user is going to say
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Specify how many results you want to receive. The results will be sorted
        // where the first result is the one with higher confidence.
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);


        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
    }

    public void startListening () {
        speechRecognizer.startListening(speechIntent);
    }

    public void stopListening () {
        speechRecognizer.stopListening();
    }

    class SpeechListener implements RecognitionListener {
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "buffer recieved ");

        }
        public void onError(int error) {
            //if critical error then exit
            if(error == SpeechRecognizer.ERROR_CLIENT || error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS){
                Log.d(TAG, "client error");
            }
            //else ask to repeats
            else{
                Log.d(TAG, "other error");
            }
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent");
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "partial results");
        }

        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "on ready for speech");
        }

        public void onResults(Bundle results) {
            Log.d(TAG, "on results");
            ArrayList<String> matches = null;
            if (results != null) {
                matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    textView.setText(matches.get(0));
                    Log.d(TAG, matches.toString());
                    speechRecognizer.stopListening();
                }

            }
        }

        public void onRmsChanged(float rmsdB) {
            //			Log.d(TAG, "rms changed");
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "speach begining");
        }

        public void onEndOfSpeech() {
            Log.d(TAG, "speach done");
        }
    }

}
