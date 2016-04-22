package us.nijikon.livelylauncher.speech;//package us.nijikon.livelylauncher.speech;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//public class MainActivity extends AppCompatActivity {
//    private static final String TAG = "MainActivity";
//
//    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
//
//    private RecognitionHelper voiceRecognitionService;
//
//    private SpeechServiceWithMS mSpeecher;
//
//    private TextView contentTextView;
//    private TextView intentTextView;
//
//    BroadcastReceiver broadcastReceiver;
//
//    private static int flag;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_voice_recognition);
//        contentTextView = (TextView) findViewById(R.id.textview_display_result);
//        intentTextView = (TextView) findViewById(R.id.textview_display_intent);
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
////                textView.setText(intent.getStringExtra(RecognitionHelperImp.SpeechListener.TAG));
//                /* content is what speaker says.
//                 * flag is denoting what type of things speaker wants to do
//                 *
//                 * i.e.1. if speak said open wechat
//                 * content equals to "wechat"
//                 * flag equals to FLAG_OPEN_APP
//                 * then what you need to do is writing a method openApp(String appName)
//                 * and write openApp(content) under the case RecognitionHelperImp.FLAG_OPEN_APP
//                 *
//                 * i.e.2. if user wants to create an event and said "create an event"
//                 * flag FLAG_CREATE_EVENT will be returned
//                 * typically you should do nothing but showing "What time will your event be?"
//                 * then waiting for user's click
//                 * when user click the button again
//                 * RecognitionHelperImp will automatically know that you are going to create an event
//                 * and what you are going to say is the time
//                 * so the content will be the time
//                 * and flag will be FLAG_GET_TIME
//                 *
//                 * known bug:
//                 * in i.e.2. if user doesn't click the button after said "create an event"
//                 * the RecognitionHelperImp can't automatically know that user doesn't want to create an event
//                 * it still thinks user is going to say the time
//                 * any commands will be considered as time
//                **/
//                String response = intent.getStringExtra(RecognitionHelperImp.RESULT_RESPONSE_TAG);
//                String content = intent.getStringExtra(RecognitionHelperImp.RESULT_CONTENT_TAG);
////                int flag = intent.getIntExtra(RecognitionHelperImp.RESULT_FLAG_TAG, 0);
//                Log.d(TAG, "content = " + content);
//                Log.d(TAG, "flag = " + flag);
//                contentTextView.setText(response);
////                voiceRecognitionService.handleClickEvent(1);
////                switch (flag) {
////                    case RecognitionHelperImp.FLAG_EXIT:
////                        //TODO
////                        break;
////                    case RecognitionHelperImp.FLAG_GET_RESPONSE:
////                        //TODO
////                        break;
////                    case RecognitionHelperImp.FLAG_CREATE_EVENT:
////                        //TODO
////                        break;
////                    case RecognitionHelperImp.FLAG_OPEN_APP:
////                        //TODO
////                        break;
////                    case RecognitionHelperImp.FLAG_UNINSTALL_APP:
////                        //TODO
////                        break;
////                    case RecognitionHelperImp.FLAG_GET_TIME:
////                        //TODO
////                        break;
////                    case RecognitionHelperImp.FLAG_GET_DATE:
////                        //TODO
////                        break;
////                    case RecognitionHelperImp.FLAG_GET_LOCATION:
////                        //TODO
////                        break;
////                    case RecognitionHelperImp.FLAG_GET_REMINDER:
////                        //TODO
////                        break;
////                    case RecognitionHelperImp.FLAG_GET_ERROR:
////                        //TODO
////                        break;
////                }
//            }
//        };
//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(RecognitionHelperImp.ACTION_SPEECH));
//    }
//
//    @Override
//    protected void onStart () {
//
////        mSpeecher = new SpeechServiceWithMS(this);
////        mSpeecher.setSpeechResultListener(new SpeechResultListener() {
////            @Override
////            public void handleResult(String intent, String response) {
////                contentTextView.setText(response);
////                intentTextView.setText(intent);
////            }
////        });
//        voiceRecognitionService = new RecognitionHelperImp(this);
//        voiceRecognitionService.setSpeechResultListener(new SpeechResultListener() {
//            @Override
//            public void handleResult(String content, String response) {
//                intentTextView.setText(content);
//                contentTextView.setText(response);
//            }
//        });
//
//        super.onStart();
//    }
//
//    public void beginSpeaking (View view) {
//
//        voiceRecognitionService.handleClickEvent(1);
////        mSpeecher.startListening();
//    }
//
//    public TextView getContentTextView () {
//        return contentTextView;
//    }
//
//    public TextView getIntentTextView () {
//        return intentTextView;
//    }
//}
