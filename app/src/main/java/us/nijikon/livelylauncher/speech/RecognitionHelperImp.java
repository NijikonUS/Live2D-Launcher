package us.nijikon.livelylauncher.speech;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.OkHttpClient;
import us.nijikon.livelylauncher.launcher.AppDataHolder;
import us.nijikon.livelylauncher.launcher.Launcher;
import us.nijikon.livelylauncher.live2dHelpers.LAppDefine;
import us.nijikon.livelylauncher.models.Event;
import us.nijikon.livelylauncher.models.Type;
import us.nijikon.livelylauncher.speech.RecognitionHelper;
import us.nijikon.livelylauncher.speech.RecognitionUtil;
import us.nijikon.livelylauncher.speech.SpeechResultListener;

/**
 * Created by Ray on 2016/4/8.
 */
public class RecognitionHelperImp implements RecognitionHelper {

    private static final String TAG = "RecognitionHelperImp";

    public static final String CONTENT_NULL = "";
    public static final String CONTENT_CREATE_EVENT = "CREATEEVENT";
    public static final String CONTENT_ERROR = "ERROR";
    public static final String CONTENT_MY_LOCATION = "MYLOCATION";
    public static final String CONTENT_WEATHER = "WEATHER";
    public static final String CONTENT_SHOW_EVENT = "SHOWEVENT";
    private static final String CONTENT_PERSON_NOT_FOUND = "PERSONNOTFOUND";

    public static final String RESULT_FLAG_TAG = "FLAG";
    public static final String RESULT_CONTENT_TAG = "CONTENT";
    public static final String RESULT_RESPONSE_TAG = "RESPONSE";



    public static final String RESPONSE_NULL = "";
    public static final String RESPONSE_TO_CREATE_EVENT = "OK, what time is your event?";
    public static final String RESPONSE_TO_GET_TIME = "Got it, what date is your event?";
    public static final String RESPONSE_TO_GET_DATE = "And what type is your event?";
    public static final String RESPONSE_TO_GET_TYPE = "Make a note about your event.";
    public static final String RESPONSE_TO_GET_NOTE = "How long should I remind you ahead?";
    //    private static final String RESPONSE_TO_GET_PEOPLE = "How much minutes before the event do you want me to remind you?";
    public static final String RESPONSE_TO_GET_REMINDER = "Event created successfully.";
    public static final String RESPONSE_TO_GET_ERROR = "Something unexpected happened, please try again.";
    public static final String RESPONSE_TO_CANCEL = "Canceled successfully.";
    public static final String RESPONSE_TO_HELLO = "Hello, how are you doing?";
    public static final String RESPONSE_TO_NOT_FOUND = "Sorry I'm not sure what you said, please try again.";
    public static final String RESPONSE_TO_SEND_MESSAGE = "What do you want to send to ";
    public static final String RESPONSE_TO_CALL = "OK, calling ";
    public static final String RESPONSE_TO_SENT = "Message sent successfully.";
    public static final String RESPONSE_SHOW_EVENT = "OK, here are all your events.";
    public static final String RESPONSE_SHOW_LOCATION = "OK, here is your current location.";
    public static final String RESPONSE_SHOW_WEATHER = "OK, here is the weather of your city.";
    public static final String RESPONSE_PERSON_NOT_FOUND = "Sorry, there is no person in your contact named ";

    private static final String DEFAULT_JOKE = "Let me tell you a joke! PHP is the best programming Language in the world!";

    private static final String[] NORMAL_SENTENCES = {
            "hello",
            "cancel",
            "joke",
            "what time",
            "who"
    };

    private static final String[] NORMAL_RESPONSES = {
            RESPONSE_TO_HELLO,
            RESPONSE_TO_CANCEL,
            DEFAULT_JOKE,
            "It is ",
            "I am a launcher"
    };

    private static List<String> jokes;

    private static final String URL_JOKE = "http://tambal.azurewebsites.net/joke/random";

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
    private static final int FLAG_GET_NORMAL_RESPONSE = 4;
    private static final int FLAG_OPEN_APP = 2;
    private static final int FLAG_DELETE_APP = 3;
    private static final int FLAG_MY_LOCATION = 5;
    private static final int FLAG_WEATHER = 6;
    private static final int FLAG_SEND_MESSAGE = 7;
    private static final int FLAG_SHOW_EVENTS = 8;

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

        jokes = new ArrayList<>();
        jokes.add(DEFAULT_JOKE);

        responseMap = new HashMap<>();
        for (int i = 0; i < NORMAL_SENTENCES.length; i++) {
            responseMap.put(NORMAL_SENTENCES[i], NORMAL_RESPONSES[i]);
        }

//        event = ((Launcher)context).getScrEvent();
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
            event = new Event();
        }

        private void handle (String result) {
            wordMap.clear();
            result = result.toLowerCase();
            String[] words = result.split(" ");
            flag = keepGoing == 1 ? flag : (keepGoing == 2 ? lastFlag : FLAG_BEGIN_COMMAND);
            Log.d(TAG, "flag = " + flag);
            if (isNormalCommand(result, words)) {
                flag = FLAG_BEGIN_COMMAND;
            }
            else {
                switch (flag) {
                    case FLAG_BEGIN_COMMAND:
                        beginCommand(result, words);
                        break;
                    case FLAG_SEND_MESSAGE:
                        lastFlag = flag;
                        flag = FLAG_BEGIN_COMMAND;
                        String messageContent = result;
//                        Log.d("asdiugweiudhiqwe====", wordMap.get("personPhoneNumber"));
                        RecognitionUtil.sendMessage("9173108245", result);
                        broadcast(result, CONTENT_NULL, RESPONSE_TO_SENT);
                        break;
                    case FLAG_GET_TIME:

                        String time = parseTime(words);
                        if (time != "ERROR") {
                            lastFlag = flag;
                            flag = FLAG_GET_DATE;
                            dateAndTime += time;
                            broadcast(result, dateAndTime, RESPONSE_TO_GET_TIME);
                        }
                        else {
                            broadcast(result, CONTENT_NULL, RESPONSE_TO_GET_ERROR);
                            flag = lastFlag;
                            lastFlag = FLAG_BEGIN_COMMAND;
                            Log.d("TIME ERROR", result);
                        }
                        break;

                    case FLAG_GET_DATE:

                        String date = dateToString(parseDate(result + " 2016"));
                        if (date != null) {
                            lastFlag = flag;
                            flag = FLAG_GET_TYPE;
                            Log.d("DATEEEEEEE", date);
                            dateAndTime = date + " " + dateAndTime;
                            Log.d("TTTTTT", dateAndTime);
                           // event.setDate(dateAndTime);
                            broadcast(result, dateAndTime, RESPONSE_TO_GET_DATE);
                            ((Launcher) context).setDate(parseDatetest(dateAndTime));
                            event.setDate(dateAndTime);
                        }
                        else {
                            broadcast(result, CONTENT_NULL, RESPONSE_TO_NOT_FOUND);
                        }
                        break;

                    case FLAG_GET_TYPE:
                        lastFlag = flag;
                        flag = FLAG_GET_NOTE;
                        event.setType(new Type(result));
                        broadcast(result, result, RESPONSE_TO_GET_TYPE);
                        break;
                    case FLAG_GET_NOTE:
                        lastFlag = flag;
                        flag = FLAG_GET_REMINDER;
                        event.setNote(result);
                        ((Launcher) context).saveToDatebase(event);
                        broadcast(result, result, RESPONSE_TO_GET_REMINDER);
                        break;
//                    case FLAG_GET_REMINDER:
//                        lastFlag = flag;
//                        flag = FLAG_BEGIN_COMMAND;
//                        try {
//                            event.setRemindBefore(Integer.parseInt(words[0]));
//                            ((Launcher) context).saveToDatebase(event);
//                            broadcast(result, result, RESPONSE_TO_GET_REMINDER);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            lastFlag = flag;
//                            flag = FLAG_BEGIN_COMMAND;
//                            broadcast(result, CONTENT_NULL, RESPONSE_TO_NOT_FOUND);
//                        }
//                        break;
                    default:
                        Log.d("flagadasgaewe-----", ""+flag);
                        lastFlag = flag;
                        flag = FLAG_BEGIN_COMMAND;
                        broadcast(result, CONTENT_ERROR, RESPONSE_TO_GET_ERROR);
                }
            }
        }

        private boolean isNormalCommand(final String result, String[] words) {
            Log.d("aaaaaaaaaaaa", words[0]);
            for (String word : words) {
                Log.d("aaaaaaaaaaaabbbbbbb", word);
                if (Arrays.asList(NORMAL_SENTENCES).contains(word)) {
                    switch (word) {
                        case "hello":
                            broadcast(result, CONTENT_NULL, responseMap.get(word));
                            ((Launcher)context).getLive2DMgr().startMotion(LAppDefine.MOTION_NOD);
                            break;
                        case "joke":
                            ((Launcher)context).getLive2DMgr().startMotion(LAppDefine.MOTION_ANGRY);
                            new AsyncTask<String, Void, String>() {
                                @Override
                                protected String doInBackground(String... params) {
                                    try {
                                        String response = RecognitionUtil.requestGet(params[0]);
                                        return response;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(String response) {
                                    try {
                                        JSONObject jokeJSONObject = new JSONObject(response);
                                        String joke = jokeJSONObject.getString("joke");
                                        jokes.add(joke);
                                        for (String j : jokes) {
                                            Log.d("joke", j);
                                        }
                                        broadcast(result, CONTENT_NULL, jokes.get
                                                (Math.abs(new Random().nextInt()) % jokes.size()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    super.onPostExecute(response);
                                }
                            }.execute(URL_JOKE);
                            break;
                        default:
                            broadcast(result, CONTENT_NULL, responseMap.get(word));
                    }
                    return true;
                }
            }
            return false;
        }

        private void beginCommand(String result, String[] words) {
            String phoneNumber;
            if (result.contains("send a message to") || result.contains("send a text to")) {
                String personName = words[4];
//                String personName = RecognitionUtil.getName(words, 4, words.length);
                if ((phoneNumber = RecognitionUtil.personExists(((Launcher)context).getSimpleContactInfo(), personName, context)) != null) {
                    wordMap.put("personName", personName);
                    wordMap.put("personPhoneNumber", phoneNumber);
                    lastFlag = flag;
                    flag = FLAG_SEND_MESSAGE;
                    broadcastContact(result, phoneNumber, RESPONSE_TO_SEND_MESSAGE, personName + "?");
                }
                else {
                    broadcastContact(result, CONTENT_PERSON_NOT_FOUND, RESPONSE_PERSON_NOT_FOUND, personName);
                }
            }
            else if (result.contains("call")) {
                String personName = words[1];
//                String personName = RecognitionUtil.getName(words, 1, words.length);
                if ((phoneNumber = RecognitionUtil.personExists(
                        ((Launcher)context).getSimpleContactInfo(), personName, context)) != null) {
                    wordMap.put("personName", personName);
                    wordMap.put("personPhoneNumber", phoneNumber);
                    lastFlag = flag;
                    flag = FLAG_BEGIN_COMMAND;
                    broadcastContact(result, phoneNumber, RESPONSE_TO_CALL, personName);
                }
                else {
                    broadcastContact(result, CONTENT_PERSON_NOT_FOUND, RESPONSE_PERSON_NOT_FOUND, personName);
                }
            }
//            if (result.contains("send a message to")) {
//                wordMap.put("personName", words[4]);
//                wordMap.put("personPhoneNumber", phoneNumber);
//                lastFlag = flag;
//                flag = FLAG_SEND_MESSAGE;
//                broadcast(result, CONTENT_NULL, RESPONSE_TO_SEND_MESSAGE);
//            }
//            else if (result.contains("send a text to")) {
//                wordMap.put("personName", words[4]);
//                wordMap.put("personPhoneNumber", phoneNumber);
//                lastFlag = flag;
//                flag = FLAG_SEND_MESSAGE;
//                broadcast(result, CONTENT_NULL, RESPONSE_TO_SEND_MESSAGE);
//            }
//            else if (result.contains("call")) {
//                wordMap.put("personName", words[1]);
//                wordMap.put("personPhoneNumber", phoneNumber);
//                lastFlag = flag;
//                flag = FLAG_BEGIN_COMMAND;
//                broadcast(result, phoneNumber, RESPONSE_TO_CALL + words[1]);
//            }
            else {
                for (String word : words) {
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
                        case "show":
                            wordMap.put("verb", "show");
                            break;
                        case "my":
                            wordMap.put("adj", "my");
                            break;
                        case "location":
                            wordMap.put("noun", "location");
                            break;
                        case "where":
                            wordMap.put("whQuestion", "where");
                            break;
                        case "am":
                            wordMap.put("verb", "am");
                            break;
                        case "i":
                            wordMap.put("noun", "i");
                            break;
                        case "what's":
                            wordMap.put("whQuestion", "what");
                            wordMap.put("verb", "is");
                            break;
                        case "what":
                            wordMap.put("whQuestion", "what");
                            break;
                        case "is":
                            //TODO
                            wordMap.put("verb", "is");
                            break;
                        case "how":
                            wordMap.put("hQuestion", "how");
                            break;
                        case "how's":
                            wordMap.put("hQuestion", "how");
                            wordMap.put("verb", "is");
                            break;
                        case "weather":
                            wordMap.put("noun", "weather");
                            break;
                        case "send":
                            wordMap.put("verb", "send");
                            break;
                        case "message":
                            wordMap.put("noun", "message");
                            break;
                        case "text":
                            wordMap.put("noun", "message");
                            break;
                        case "to":
                            wordMap.put("prep", "to");
                            break;
                        default:
                            wordMap.put("other", word);
                    }
                }

//            if (flag != FLAG_GET_NORMAL_RESPONSE) {
                try {
                    if (flag == FLAG_BEGIN_COMMAND) {
                        switch (wordMap.get("verb")) {
                            case "create":
                                if (hasMapValue(wordMap, "noun", "event")) {
                                    lastFlag = flag;
                                    flag = FLAG_GET_TIME;
//                            event = new Event();
                                    broadcast(result, CONTENT_NULL, RESPONSE_TO_CREATE_EVENT);
                                }
                                break;
                            case "open":
                                lastFlag = flag;
                                flag = FLAG_OPEN_APP;
                                //TODO
                                //open
                        AppDataHolder.openByName(wordMap.get("noun"), context);

                                break;
                            case "delete":
                                lastFlag = flag;
                                flag = FLAG_DELETE_APP;
                                //TODO
                                //unistall
                        AppDataHolder.unistallByName(wordMap.get("noun"), context);
                                break;
                            case "am":
                                if (wordMap.get("whQuestion").equals("where") &&
                                        wordMap.get("noun").equals("i")) {
                                    lastFlag = flag;
                                    flag = FLAG_MY_LOCATION;
                                    broadcast(result, CONTENT_MY_LOCATION, RESPONSE_SHOW_LOCATION);
                                }
                                break;
                            //what is the weather like
                            //how is the weather
                            case "is":
                                if ((wordMap.get("whQuestion").equals("what") ||
                                        wordMap.get("hQuestion").equals("how")) &&
                                        wordMap.get("noun").equals("weather")) {
                                    lastFlag = flag;
                                    flag = FLAG_WEATHER;
                                    broadcast(result, CONTENT_WEATHER, RESPONSE_SHOW_WEATHER);
                                }
                                break;
                            case "show":
                                if (hasMapValue(wordMap, "noun", "location")) {
                                    lastFlag = flag;
                                    flag = FLAG_MY_LOCATION;
                                    broadcast(result, CONTENT_MY_LOCATION, RESPONSE_SHOW_LOCATION);
                                }
                                if (hasMapValue(wordMap, "noun", "event")) {
                                    lastFlag = flag;
                                    flag = FLAG_SHOW_EVENTS;
                                    broadcast(result, CONTENT_SHOW_EVENT, RESPONSE_SHOW_EVENT);
                                }
                                if (hasMapValue(wordMap, "noun", "weather")) {
                                    lastFlag = flag;
                                    flag = FLAG_WEATHER;
                                    broadcast(result, CONTENT_WEATHER, RESPONSE_SHOW_WEATHER);
                                }
                                break;
                            case "send":
                                if (hasMapValue(wordMap, "noun", "message")) {
                                    lastFlag = flag;
                                    flag = FLAG_SEND_MESSAGE;
                                    broadcast(result, words[words.length - 1], RESPONSE_TO_SEND_MESSAGE);
                                }
                            default:
                                lastFlag = flag;
                                flag = FLAG_BEGIN_COMMAND;
                                broadcast(result, CONTENT_ERROR, RESPONSE_TO_GET_ERROR);
                        }
                    }
                } catch (NullPointerException e) {
                    broadcast(result, CONTENT_ERROR, RESPONSE_TO_NOT_FOUND);
                } finally {

                    if (flag == FLAG_OPEN_APP ||
                            flag == FLAG_DELETE_APP ||
                            flag == FLAG_MY_LOCATION ||
                            flag == FLAG_WEATHER ||
                            flag == FLAG_SHOW_EVENTS) {
                        Log.d("#########$$$", "$$$$");
                        lastFlag = flag;
                        flag = FLAG_BEGIN_COMMAND;
                    } else if (flag != FLAG_GET_TIME) {
                        Log.d("######", "####");
                        lastFlag = flag;
                        flag = FLAG_BEGIN_COMMAND;
                        broadcast(result, CONTENT_ERROR, RESPONSE_TO_NOT_FOUND);
                    }
                }
            }
        }

        private boolean hasMapValue (Map<String, String> map, String key, String value)
                throws NullPointerException{
            return map.get(key).equals(value);
        }

        private void broadcast (String query, String content, String response) {
//            mSpeeker.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);
            Log.d(TAG, "response = " + response);
            Log.d(TAG, "content = " + content);
//            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_SPEECH).
//                    putExtra(RESULT_CONTENT_TAG, content).putExtra(RESULT_RESPONSE_TAG, response));
////            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_SPEECH).putExtra(RESULT_FLAG_TAG, flag));
            speechResultListener.handleResult(query, content, response);
        }

        private void broadcastContact (String query, String content, String response, String personName) {
//            mSpeeker.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);
            Log.d(TAG, "response = " + response);
            Log.d(TAG, "content = " + content);
            Log.d(TAG, "person name = " + personName);

//            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_SPEECH).
//                    putExtra(RESULT_CONTENT_TAG, content).putExtra(RESULT_RESPONSE_TAG, response));
////            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_SPEECH).putExtra(RESULT_FLAG_TAG, flag));
            speechResultListener.handleContact(query, content, response, personName);
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "buffer recieved ");
        }

        public void onError(int error) {
            //if critical error then exit
            Log.d(TAG, "error = " + String.valueOf(error));
//            switch (error) {
//                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
//                case SpeechRecognizer.ERROR_NO_MATCH:
//                    broadcast("", CONTENT_ERROR, RESPONSE_TO_NOT_FOUND);
//                    break;
//            }
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
        String[] formats = {"MMMM d'st' yyyy","MMMM d'nd' yyyy","MMMM d'rd' yyyy","MMMM d'th' yyyy", "MMMM d yyyy"};
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

    public static Date parseDatetest(String date){
        Log.d("f&sfsadf",date);
        Date d = null;
        String[] formats = {"dd/MM/yy hh:mm:ss","MMMM d'nd' yyyy hh:mm:ss","MMMM d'rd' yyyy hh:mm:ss","MMMM d'th' yyyy hh:mm:ss", "MMMM d yyyy hh:mm:ss"};
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
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            return sdf.format(date);
        }
        else {
            return null;
        }
    }
}
