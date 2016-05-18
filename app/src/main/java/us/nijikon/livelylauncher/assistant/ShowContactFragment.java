package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.adapters.ContactAdapter;
import us.nijikon.livelylauncher.launcher.Launcher;
import us.nijikon.livelylauncher.models.Person;

/**
 * Created by bowang .
 */
public class ShowContactFragment extends Fragment {

    public final static String tag = "ShowContactFragment";
    public final static String INVITE_PEOPLE = "INVITE_PEOPLE";
    public final static int code = 77;

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private LinearLayoutManager manager;
    private Launcher launcher;
    private Button confirm;

    OnClickAtFrameListener callback;

    public interface OnClickAtFrameListener {
        void savePerson(List<Person> selectedPersonList);
    }
   // private Toolbar toolbar;

    public ShowContactFragment setParent(Launcher launcher){
        this.launcher = launcher;
        return  this;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        callback = (OnClickAtFrameListener)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_show_contacts, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(launcher.getUsableWidth(), launcher.getUsableHeight()));


        //bind
        recyclerView = (RecyclerView)view.findViewById(R.id.myListView);
        confirm = (Button)view.findViewById(R.id.confirm);
//        toolbar = (Toolbar) findViewById(R.id.myToolBar);
//        toolbar.setTitle("Invite People");
//        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        adapter = new ContactAdapter(launcher, ContactAdapter.normal);
        manager = new LinearLayoutManager(launcher);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishChoice();
                if(launcher.event!=null){
                    launcher.event.setContactPerson(new ArrayList<Person>(adapter.getResult().values()));
                }
                launcher.goFragment(NoteFragment.tag);
            }
        });

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finishChoice();
//                finish();
//            }
//        });
        return view;
    }

    /*
     * setting return result
     */
    private void finishChoice(){
        ArrayList<Person> results = new ArrayList<>(adapter.getResult().values());
        callback.savePerson(results);
    }



}
