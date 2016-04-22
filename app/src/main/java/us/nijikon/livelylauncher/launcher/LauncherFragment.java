package us.nijikon.livelylauncher.launcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.assistant.TimeSelectFragment;
import us.nijikon.livelylauncher.assistant.TimeSelectFragment;
import us.nijikon.livelylauncher.live2dHelpers.LAppDefine;
import us.nijikon.livelylauncher.models.Event;
import us.nijikon.livelylauncher.speech.RecognitionHelper;
import us.nijikon.livelylauncher.speech.RecognitionHelperImp;
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
    // finger motion
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    private RecognitionHelper recognitionHelper;



    public LauncherFragment setParent(Launcher launcher){
        this.launcher = launcher;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sppech
//        recognitionHelper = new RecognitionHelperImp(launcher);
//        recognitionHelper.setSpeechResultListener(new SpeechResultListener() {
//            @Override
//            public void handleResult(String response) {
//                Log.d("speech is ", response);
//                ((TextView)launcher.findViewById(R.id.hookText)).setText(response);
//
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(launcher.getUsableWidth(),launcher.getUsableHeight()));

        launcherfragment = (RelativeLayout)view.findViewById(R.id.launcherFragment);
        showApps = (ImageButton)view.findViewById(R.id.showApps);
        speech = (ImageButton)view.findViewById(R.id.speech);
        textView= (TextView)view.findViewById(R.id.hookText);


        recognitionHelper = new RecognitionHelperImp(launcher);
        recognitionHelper.setSpeechResultListener(new SpeechResultListener() {
            @Override
            public void handleResult(String response) {
                Log.d("speech is ", response);
                textView.setText(response);

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
                //   ((TextView)launcher.findViewById(R.id.hookText)).setText(" ###### ");

                launcher.getLive2DMgr().startMotion(LAppDefine.MOTION_POSITIVE);
                recognitionHelper.handleClickEvent(1);
            }
        });

        launcherfragment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEventCompat.getActionMasked(event)== MotionEvent.ACTION_DOWN){
                    Log.d("asdfsf","down");
                    x1 = event.getX();
                    y2 = event.getY();
                }
                if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP){
                    Log.d("asdfsf","move");
                    x2 = event.getX();
                    y2 = event.getY();
                    if( x1 - x2 > 150){
                        Log.d("asdfsf","swipe");
                        // swipe right
                        launcher.goFragment(TimeSelectFragment.tag);
                    }
                }
                if(Math.abs(launcher.getUsableWidth()-x1) > 80 ){
                    return false;
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
