package us.nijikon.livelylauncher.launcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import us.nijikon.livelylauncher.R;

/**
 * Created by bowang .
 */
public class LauncherFragment extends Fragment{

    public static final String tag = "LauncherFragment";

    Launcher launcher;
    ImageButton showApps;
    ImageButton speech;



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

        showApps = (ImageButton)view.findViewById(R.id.showApps);
        speech = (ImageButton)view.findViewById(R.id.speech);

        showApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.goFragment(AppFragment.tag);
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
