package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.launcher.Launcher;
import us.nijikon.livelylauncher.models.Event;

public class RemindFragment extends Fragment {

    Button on, off, bt_0, bt_10, bt_30, bt_60, confirm, add;
    int remindBefore;
    TextView show;
    boolean alert = true;
    private Launcher launcher;

    OnClickAtFrameListener callback;
    public static final String tag = "RemindActivity";

    public interface OnClickAtFrameListener {
        void saveRemind(int remindBefore);
        void saveToDatebase();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        callback = (OnClickAtFrameListener)activity;
    }

    public RemindFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.activity_remind, container, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(((Launcher)getActivity()).getUsableWidth(),((Launcher)getActivity()).getUsableHeight()));
//        LinearLayout linear = (LinearLayout)v.findViewById(R.id.linear);
//        //linear.getBackground().setAlpha(130);

        final Event event = new Event();

        show = (TextView)v.findViewById(R.id.show);
        //Switch swh = (Switch)v.findViewById(R.id.switchkk);
        on = (Button)v.findViewById(R.id.on);
        off = (Button)v.findViewById(R.id.off);
        bt_0 = (Button)v.findViewById(R.id.bt_0);
        bt_10 = (Button)v.findViewById(R.id.bt_10);
        bt_30 = (Button)v.findViewById(R.id.bt_30);
        bt_60 = (Button)v.findViewById(R.id.bt_60);
        confirm = (Button)v.findViewById(R.id.confirm);
        add = (Button)v.findViewById(R.id.add);
        //default unclickable
        bt_0.setVisibility(View.INVISIBLE);
        bt_10.setVisibility(View.INVISIBLE);
        bt_30.setVisibility(View.INVISIBLE);
        bt_60.setVisibility(View.INVISIBLE);


        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_0.setVisibility(View.VISIBLE);
                bt_10.setVisibility(View.VISIBLE);
                bt_30.setVisibility(View.VISIBLE);
                bt_60.setVisibility(View.VISIBLE);
                bt_0.setClickable(true);
                bt_10.setClickable(true);
                bt_30.setClickable(true);
                bt_60.setClickable(true);

            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindBefore = 0;
                alert = false;
                bt_0.setVisibility(View.INVISIBLE);
                bt_10.setVisibility(View.INVISIBLE);
                bt_30.setVisibility(View.INVISIBLE);
                bt_60.setVisibility(View.INVISIBLE);

            }
        });

        bt_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindBefore=0;
                //event.setRemindBefore(0);
                show.setText("you set :" + remindBefore + " minutes");
            }
        });

        bt_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindBefore = 10;
                //event.setRemindBefore(10);
                show.setText("you set :" + remindBefore + " minutes");
            }
        });

        bt_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindBefore = 30;
                //event.setRemindBefore(30);
                show.setText("you set :"+ remindBefore+" minutes");
            }
        });

        bt_60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindBefore = 60;
                //event.setRemindBefore(60);
                show.setText("you set :"+ remindBefore+" minutes");
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(alert){
                    callback.saveRemind(remindBefore);

                }

                callback.saveToDatebase();
                ((Launcher)getActivity()).goFragment(ItemFragment.TAG);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.saveRemind(remindBefore);
                // ((AssistActivity)getActivity()).backToSelectTime(v);
//                Intent intent = new Intent(v.getContext(), TimeSelect.class);
//                v.getContext().startActivity(intent);

            }
        });

        return v;
    }

    public RemindFragment setParent(Launcher launcher){
        this.launcher = launcher;
        return this;
    }
}
