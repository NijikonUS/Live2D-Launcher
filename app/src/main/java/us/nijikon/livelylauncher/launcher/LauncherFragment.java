package us.nijikon.livelylauncher.launcher;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Locale;

import jp.live2d.utils.android.FileManager;
import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.assistant.ItemFragment;
import us.nijikon.livelylauncher.assistant.LocationActivity;
import us.nijikon.livelylauncher.assistant.TimeSelectFragment;
import us.nijikon.livelylauncher.assistant.TimeSelectFragment;
import us.nijikon.livelylauncher.live2dHelpers.LAppDefine;
import us.nijikon.livelylauncher.live2dHelpers.LAppRenderer;
import us.nijikon.livelylauncher.models.Event;
import us.nijikon.livelylauncher.models.Weather;
import us.nijikon.livelylauncher.speech.RecognitionHelper;
import us.nijikon.livelylauncher.speech.RecognitionHelperImp;
import us.nijikon.livelylauncher.speech.RecognitionUtil;
import us.nijikon.livelylauncher.speech.SpeechResultListener;

/**
 * Created by bowang .
 */
public class LauncherFragment extends Fragment{

    public static final String tag = "LauncherFragment";

    Launcher launcher;
    ImageButton showApps;
    ImageButton speech;
    RelativeLayout launcherfragment;
    TextView textView;
    TextView queryTextView;
    // finger motion
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    private RecognitionHelper recognitionHelper;

    private TextToSpeech mSpeeker;



    public LauncherFragment setParent(Launcher launcher){
        this.launcher = launcher;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(launcher.getUsableWidth(),launcher.getUsableHeight()));
        queryTextView = (TextView)view.findViewById(R.id.query_view);
        launcherfragment = (RelativeLayout)view.findViewById(R.id.launcherFragment);
        showApps = (ImageButton)view.findViewById(R.id.showApps);
        speech = (ImageButton)view.findViewById(R.id.speech);
        textView= (TextView)view.findViewById(R.id.hookText);

        mSpeeker = new TextToSpeech(launcher, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mSpeeker.setLanguage(Locale.US);
            }
        });


        recognitionHelper = new RecognitionHelperImp(launcher);
        recognitionHelper.setSpeechResultListener(new SpeechResultListener() {
            @Override
            public void handleResult(String query, String content, String response) {
                showQuery(query);
                mSpeeker.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);
                Log.d("speech is ", response);
                textView.setText(response);
                speech.setAlpha(0.6f);
                new TextResumeTask(textView,speech).execute();
                switch (response) {
                    case RecognitionHelperImp.RESPONSE_SHOW_LOCATION:
                        handleLocation();
                        break;
                    case RecognitionHelperImp.RESPONSE_SHOW_WEATHER:
                        new AsyncTask<Void, Void, Weather>() {
                            @Override
                            protected Weather doInBackground(Void... params) {

                                try {
                                    return new Weather(RecognitionUtil.requestGet(RecognitionUtil.getUrlToWeather(RecognitionUtil.getLocation(getActivity()), getActivity())));
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                    Log.d("fafdsafsfsfasfsafsadfs","fafsfsdf");
                                    return null;
                                }
                            }

                            @Override
                            protected void onPostExecute(Weather weather) {
                                super.onPostExecute(weather);
                                Log.d(tag, "weather:" + weather.getWeatherMain());

                                ((WeatherFragment) launcher.goFragment(WeatherFragment.tag)).setWeather(weather);
                            }
                        }.execute();
                        break;
                    case RecognitionHelperImp.RESPONSE_SHOW_EVENT:
                        //TODO
                        launcher.goFragment(ItemFragment.TAG);
                        break;
                    case RecognitionHelperImp.RESPONSE_TO_CALL:
                        //TODO
                        RecognitionUtil.call(content, launcher);
                        break;
                }
            }

            @Override
            public void handleContact(String query, String content,
                                      String response, String personName) {
                mSpeeker.speak(response + personName, TextToSpeech.QUEUE_FLUSH, null, null);
                Log.d("speech is ", response);
                speech.setAlpha(0.6f);
                textView.setText(response + personName);
                new TextResumeTask(textView,speech).execute();
                if (response == RecognitionHelperImp.RESPONSE_TO_CALL) {
                    RecognitionUtil.call(content, launcher);
                }
            }
        });




        showApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.goFragment(AppFragment.tag);
            }
        });

        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 launcher.getLive2DMgr().startMotion(LAppDefine.MOTION_POSITIVE);
                // recognitionHelper.handleClickEvent(1);
//                LAppDefine.back_image_path = "image/images.jpeg";
//                launcher.getLive2DMgr().updateBackground(((LAppDefine.back_image_path)));
                recognitionHelper.handleClickEvent(1);
                speech.setAlpha(1f);
                //getFragmentManager().beginTransaction().add(R.id.main,new WallPaperFragment().setParent(launcher)).commit();
               // launcher.goFragment(WeatherFragment.tag);
                //launcher.findViewById(R.id.live2dLayout).invalidate();
            }
        });

        launcherfragment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    Log.d("asdfsf", "down");
                    x1 = event.getX();
                    y2 = event.getY();
                }
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP) {
                    Log.d("asdfsf", "move");
                    x2 = event.getX();
                    y2 = event.getY();
                    if (x1 - x2 > 150) {
                        Log.d("asdfsf", "swipe");
                        // swipe right
                        launcher.goFragment(TimeSelectFragment.tag);
                    }
                }
                if (Math.abs(launcher.getUsableWidth() - x1) > 80) {
                    return false;
                }
                return true;
            }
        });

        return view;
    }

    private void handleLocation() {
        Intent intent = new Intent(launcher, LocationActivity.class);
        intent.putExtra("LOCATION", RecognitionUtil.getLocation(launcher));
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSpeeker.shutdown();
    }

    private void showQuery(String query) {
        queryTextView.setText(query);
        new TextResumeTask(queryTextView,null).execute();/// 还原文字


    }
}
