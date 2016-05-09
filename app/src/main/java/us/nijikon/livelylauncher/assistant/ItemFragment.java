package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import us.nijikon.livelylauncher.dao.LivelyLauncherDB;
import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.adapters.MyItemRecyclerViewAdapter;
import us.nijikon.livelylauncher.launcher.Launcher;
import us.nijikon.livelylauncher.launcher.LauncherFragment;
import us.nijikon.livelylauncher.models.Event;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    public static final String TAG = ".ItemFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private RecyclerView mRecyclerView;
    private List<Event> events ;
    private List<String> typeNames;
    private String[] personList;
    private OnListFragmentInteractionListener mListener;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    private Launcher launcher;
    public ItemFragment setParents(Launcher p){
        this.launcher = p;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
//        Button home = (Button)view.findViewById(R.id.home);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(),AssistActivity.class);
//                v.getContext().startActivity(intent);
//            }
//        });

        // Query database
        LivelyLauncherDB db = new LivelyLauncherDB(getActivity());
        events = db.getAllEvents();
        Log.e(TAG, "store events:"+events.get(0).getNote());

        typeNames = new ArrayList<>();
        personList = new String[events.size()];

        for(int i = 0;i<events.size();i++){
            Cursor c = db.queryDatabaseType(events.get(i).getEventId());
            c.moveToFirst();
            String typeName = c.getString(1);
            typeNames.add(typeName);
            Log.e(TAG, typeNames.get(i));

            if(typeName.equals("Contact")){
                StringBuilder personConcate = new StringBuilder();
                Cursor personCursor = db.queryDatabasePerson(events.get(i).getEventId());
                for (personCursor.moveToFirst(); !personCursor.isAfterLast(); personCursor.moveToNext()){
                    //Log.e(TAG, "test:"+personCursor.getString(1));
                    personConcate.append(personCursor.getString(1) + " ");

                }
                personList[i] = personConcate.toString();

            }else{
                personList[i] = null;
            }
        }
        // Set the adapter
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new MyItemRecyclerViewAdapter(events,typeNames,personList));

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }
}
