package us.nijikon.livelylauncher.speech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import us.nijikon.livelylauncher.assistant.ShowContactFragment;
import us.nijikon.livelylauncher.launcher.AppDataHolder;
import us.nijikon.livelylauncher.launcher.Launcher;
import us.nijikon.livelylauncher.live2dHelpers.LAppDefine;
import us.nijikon.livelylauncher.models.Event;
import us.nijikon.livelylauncher.models.Type;

/**
 * Created by Ray on 2016/4/8.
 */
public class RecognitionHelperImp implements RecognitionHelper{

    private static final String TAG = "RecognitionHelperImp";

    public static final String CONTENT_CREATE_EVENT = "CREATEEVENT";
    public static final String CONTENT_ERROR = "ERROR";

    public static final String RESULT_FLAG_TAG = "FLAG";
    public static final String RESULT_CONTENT_TAG = "CONTENT";
    public static final String RESULT_RESPONSE_TAG = "RESPONSE";

    public static final String CONTENT_NULL = "";

    private static final String RESPONSE_NULL = "";
    private static final String RESPONSE_TO_CREATE_EVENT = "OK, what time is your event?";
    private static final String RESPONSE_TO_GET_TIME = "Got it, what date is your event?";
    private static final String RESPONSE_TO_GET_DATE = "And what type is your event?";
    private static final String RESPONSE_TO_GET_TYPE = "Make a note about your event.";
    private static final String RESPONSE_TO_GET_NOTE = "How long should I remind you ahead?";
    //    private static final String RESPONSE_TO_GET_PEOPLE = "How much minutes before the event do you want me to remind you?";
    private static final String RESPONSE_TO_GET_REMINDER = "Event created successfully.";
    private static final String RESPONSE_TO_GET_ERROR = "Something unexpected happened, please try again.";
    private static final String RESPONSE_TO_CANCEL = "Your event has been canceled successfully.";
    private static final String RESPONSE_TO_HELLO = "Hello, how are you doing?";
    private static final String RESPONSE_TO_JOKE = "Let me tell you a joke! PHP is the best programming Language in the world!";
    private static final String RESPONSE_TO_NOT_FOUND = "Sorry I'm not sure what you said, please try again.";

    private static final String[] NORMAL_SENTENCES = {
            "hello",
            "cancel",
            "joke",
            "what time",
    };

    private static final String[] NORMAL_RESPONSES = {
            RESPONSE_TO_HELLO,
            RESPONSE_TO_CANCEL,
            RESPONSE_TO_JOKE,
            "It is "
    };

    private final Map<String, String> responseMap;

    public static final String ACTION_SPEECH = "SPEECH";

    public static final int FLAG_CREATE_EVENT = 1;
    //    public static final int FLAG_OPEN_APP = 2;
//    public static final int FLAG_UNINSTALL_APP = 3;
    public static final int FLAG_GET_TIME = 1111;
    public static final int FLAG_GET_DATE = 2222;
    public static final int FLAG_GET_TYPE = 3333;
    public static final int FLAG_GET_NOTE = 4444;
    public static final int FLAG_GET_REMINDER = 5555;
    public static final int FLAG_GET_ERROR = 9999;
    public static final int FLAG_GET_RESPONSE = 6666;//get normal response and display directly
    public static final int FLAG_EXIT = 8;
    private static final int FLAG_GET_NORMAL_RESPONSE = 4;
    private static final int FLAG_OPEN_APP = 2;
    private static final int FLAG_DELETE_APP = 3;


    private static final int FLAG_BEGIN_COMMAND = 0;

    private final SpeechRecognizer mSpeechRecognizer;

    private final SpeechListener mSpeechLitsener;

    private final Intent mSpeechIntent;

    private final Context context;

    private SpeechResultListener speechResultListener;

    private Event event;
    private String eventNote = "";
    private String dateAndTime = "";

    public void setSpeechResultListener(SpeechResultListener speechResultListener) {
        this.speechResultListener = speechResultListener;
    }

    private final TextToSpeech mSpeeker;

    //keepGoing = 1 => keep going
    //keepGoing = 2 => go back
    //keepGoing = 3 => cancel present command
    private int keepGoing;

    //Beginning command flag = 0
    //Get time flag = 1
    //Get date flag = 2
    //Get location flag = 3
    //Get reminder flag = 5
    private int flag;
    private int lastFlag;

    //Need an Activity as an argument
    //@param Activity activity
    public RecognitionHelperImp(Context context) {
        this.context = context;

        responseMap = new HashMap<>();
        for (int i = 0; i < NORMAL_SENTENCES.length; i++) {
            responseMap.put(NORMAL_SENTENCES[i], NORMAL_RESPONSES[i]);
        }

        event = ((Launcher)context).getScrEvent();
        flag = FLAG_BEGIN_COMMAND;
        lastFlag = FLAG_BEGIN_COMMAND;
        keepGoing = 1;

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mSpeechLitsener = new SpeechListener(context);
        mSpeechRecognizer.setRecognitionListener(mSpeechLitsener);
        mSpeechIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);

        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"us.nijikon.livelylauncher");

        // Given an hint to the recognizer about what the user is going to say
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        // Specify how many results you want to receive. The results will be sorted
        // where the first result is the one with higher confidence.
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        mSpeeker = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mSpeeker.setLanguage(Locale.US);
            }
        });
    }

    @Override
    public void handleClickEvent(int keepGoing) {
        this.keepGoing = keepGoing;
        mSpeechRecognizer.startListening(mSpeechIntent);
    }

    class SpeechListener implements RecognitionListener {
        public static final String TAG = "SpeechListener";

        private final Context context;

        private Map<String, String> wordMap;//Save the verb and noun

        public SpeechListener (Context context) {
            this.context = context;
            wordMap = new HashMap<>();
        }

        private void handle (String result) {
            result = result.toLowerCase();
            String[] words = result.split(" ");
            flag = keepGoing == 1 ? flag : (keepGoing == 2 ? lastFlag : FLAG_BEGIN_COMMAND);
            Log.d(TAG, "flag = " + flag);
            if (isNormalCommand(words)) {
                flag = FLAG_BEGIN_COMMAND;
            }
            else {
                switch (flag) {
                    case FLAG_BEGIN_COMMAND:
                        beginCommand(words);
                        break;
                    case FLAG_GET_TIME:
                        lastFlag = flag;
                        flag = FLAG_GET_DATE;
                        String time = parseTime(words);
                        if (time != "ERROR") {
                            dateAndTime += time;
                            broadcast(dateAndTime, RESPONSE_TO_GET_TIME);
                        }
                        else {
                            broadcast(CONTENT_NULL, RESPONSE_TO_GET_ERROR);
                            flag = lastFlag;
                            lastFlag = FLAG_BEGIN_COMMAND;
                            Log.d("TIME ERROR", result);
                        }
                        break;
                    case FLAG_GET_DATE:
                        lastFlag = flag;
                        flag = FLAG_GET_TYPE;
                        String date = dateToString(parseDate(result + " 2016"));
                        Log.d("DATEEEEEEE", date);
                        dateAndTime = date + " " + dateAndTime;
                        event.setDate(dateAndTime);
                        broadcast(dateAndTime, RESPONSE_TO_GET_DATE);
                        break;
                    case FLAG_GET_TYPE:
                        lastFlag = flag;
                        flag = FLAG_GET_NOTE;
                        event.setType(new Type(result));
                        broadcast(result, RESPONSE_TO_GET_TYPE);
                        break;
                    case FLAG_GET_NOTE:
                        lastFlag = flag;
                        flag = FLAG_GET_REMINDER;
                        event.setNote(result);
                        broadcast(result, RESPONSE_TO_GET_NOTE);
                        break;
                    case FLAG_GET_REMINDER:
                        lastFlag = flag;
                        flag = FLAG_BEGIN_COMMAND;
                        event.setRemindBefore(Integer.parseInt(words[0]));
                        ((Launcher)context).saveToDatebase(event);
                        broadcast(result, RESPONSE_TO_GET_REMINDER);
                        break;
                    default:
                        lastFlag = flag;
                        flag = FLAG_BEGIN_COMMAND;
                        broadcast(CONTENT_ERROR, RESPONSE_TO_GET_ERROR);
                }
            }
        }

        private boolean isNormalCommand(String[] words) {
            Log.d("aaaaaaaaaaaa", words[0]);
            for (String word : words) {
                Log.d("aaaaaaaaaaaabbbbbbb", word);
                if (Arrays.asList(NORMAL_SENTENCES).contains(word)) {
                    switch (word) {
                        case "hello":
                            ((Launcher)context).getLive2DMgr().startMotion(LAppDefine.MOTION_NOD);
                            break;
                        case "joke":
                            ((Launcher)context).getLive2DMgr().startMotion(LAppDefine.MOTION_ANGRY);
                            break;
                    }
                    broadcast(CONTENT_NULL, responseMap.get(word));
                    return true;
                }
            }
            return false;
        }

        private void beginCommand(String[] words) {
            for (String word : words) {
                word = word.toLowerCase();
                switch (word) {
                    case "create":
                        wordMap.put("verb", "create");
                        break;
                    case "event":
                        wordMap.put("noun", "event");
                        break;
                    case "open":
                        wordMap.put("verb", "open");
                        wordMap.put("noun", words[1]);
                        Log.d("AppName", words[1]);
                        break;
                    case "delete":
                        wordMap.put("verb", "delete");
                        wordMap.put("noun", words[1]);
                        break;
                    default:
                        wordMap.put("other", word);
                }
            }

//            if (flag != FLAG_GET_NORMAL_RESPONSE) {
            if (flag == FLAG_BEGIN_COMMAND && wordMap.containsKey("verb")) {
                switch (wordMap.get("verb")) {
                    case "create":
                        if (wordMap.get("noun").equals("event")) {
                            lastFlag = flag;
                            flag = FLAG_GET_TIME;
//                            event = new Event();
                            broadcast(CONTENT_NULL, RESPONSE_TO_CREATE_EVENT);
                        }
                        break;
                    case "open":
                        lastFlag = flag;
                        flag = FLAG_OPEN_APP;
                        //TODO
                        //open
                        AppDataHolder.openByName(wordMap.get("noun"),context);

                        break;
                    case "delete":
                        lastFlag = flag;
                        flag = FLAG_DELETE_APP;
                        //TODO
                        //unistall
                        AppDataHolder.unistallByName(wordMap.get("noun"), context);
                        break;
                    default:
                        lastFlag = flag;
                        flag = FLAG_BEGIN_COMMAND;
                        broadcast(CONTENT_ERROR, RESPONSE_TO_GET_ERROR);
                }
            }
            if (flag == FLAG_OPEN_APP || flag == FLAG_DELETE_APP) {
                Log.d("#########$$$", "$$$$");
                lastFlag = flag;
                flag = FLAG_BEGIN_COMMAND;
            }
            else if (flag != FLAG_GET_TIME) {
                Log.d("######","####");
                lastFlag = flag;
                flag = FLAG_BEGIN_COMMAND;
                broadcast(CONTENT_ERROR, RESPONSE_TO_NOT_FOUND);
            }

        }

        private void broadcast (String content, String response) {
            mSpeeker.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);
            Log.d(TAG, "response = " + response);
            Log.d(TAG, "content = " + content);
//            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_SPEECH).
//                    putExtra(RESULT_CONTENT_TAG, content).putExtra(RESULT_RESPONSE_TAG, response));
////            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_SPEECH).putExtra(RESULT_FLAG_TAG, flag));
            speechResultListener.handleResult(response);
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "buffer recieved ");
        }

        public void onError(int error) {
            //if critical error then exit
            Log.d(TAG, "error = " + String.valueOf(error));
            if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
//                broadcast(CONTENT_ERROR, RESPONSE_TO_GET_ERROR);
            }
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent");
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).toString());
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
                    String result = matches.get(0);
                    Log.d("acacacacacacacac", matches.toString());
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AAA").putExtra(TAG, result));
                    handle(result);
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
    public static Date parseDate(String date){
        Date d = null;
        String[] formats = {"MMMM d'st' yyyy","MMMM d'nd' yyyy","MMMM d'rd' yyyy","MMMM d'th' yyyy"};
        ParsePosition position = new ParsePosition(0);
        for (String format : formats) {
            position.setIndex(0);
            position.setErrorIndex(-1);
            // no ParseException but a null return instead
            d = new SimpleDateFormat(format).parse(date, position);
            if (d != null) {
                return d;
            }
        }
        return d;
    }

    private static String parseTime(String[] words) {
        Log.d("mmmmmmmmmmmmmmmm", words[0]);
        if (words.length >= 2) {
            String hour = "";
            String minute = "";
            if (words[0].contains(":")) {
                hour = words[0].split(":")[0];
                minute = words[0].split(":")[1];
            }
            else {
                hour = words[0];
                minute = "00";
            }
            if (words[1].equals("am") || words[1].equals("a.m.")) {
                return words[0];
            }
            else if (words[1].equals("pm") || words[1].equals("p.m.")) {
                return String.valueOf(Integer.parseInt(hour) + 12) + ":" + minute + ":00";
            }
        }
        return "ERROR";
    }

    public static String dateToString(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        return sdf.format(date);
    }
}
