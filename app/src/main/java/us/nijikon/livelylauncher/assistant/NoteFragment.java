package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.launcher.Launcher;

public class NoteFragment extends Fragment {

    //private Event event ;
    //private String note;

    OnClickAtFrameListener callback;

    public static final String tag = "Note";
    private Launcher launcher;

    public interface OnClickAtFrameListener {
        void saveNote(String note);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        callback = (OnClickAtFrameListener)activity;
    }

    public NoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final View v =inflater.inflate(R.layout.activity_note, container, false);

        Button btn = (Button)v.findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) v.findViewById(R.id.editText);

                String note = editText.getText().toString();
                Log.e("check", note);
                callback.saveNote(note);

                ((Launcher) getActivity()).goFragment(RemindFragment.tag);

            }
        });
        return  v;
    }

    public NoteFragment setParent(Launcher launcher){
        this.launcher = launcher;
        return this;
    }
}
