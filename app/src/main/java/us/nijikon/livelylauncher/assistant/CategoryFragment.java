package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import us.nijikon.livelylauncher.dao.LivelyLauncherDB;
import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.adapters.CategoryAdapter;
import us.nijikon.livelylauncher.launcher.Launcher;

public class CategoryFragment extends Fragment {

    public static final String tag = "CategoryActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static final String KEY = "us.nijikon.livelylauncher.assistant.key";
    private List<String> category ;   //{"Contact","Note","Movie","Eat","Shop","Sport"}
    private String typeName;
    private Launcher launcher;


    OnClickAtFrameListener callback;

    public interface OnClickAtFrameListener {
        void saveType(String name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        callback = (OnClickAtFrameListener)activity;
    }

    public CategoryFragment() {
        // Required empty public constructor

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_category, container, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(((Launcher)getActivity()).getUsableWidth(),((Launcher)getActivity()).getUsableHeight()));


        category = new ArrayList<>();

        category.add("Contact");
        category.add("Note");
        category.add("Movie");
        category.add("Eat");
        category.add("Shop");
        category.add("Sport");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.getBackground().setAlpha(130);

        Button next = (Button) v.findViewById(R.id.next);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CategoryAdapter(category,this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) v.findViewById(R.id.edit);
                String newText = editText.getText().toString();
                addNewType(newText);
            }
        });

        return  v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setCategoryName(String name){
        typeName = name;
        callback.saveType(typeName);

    }

    public void addNewType(String name){
        category.add(name);
        LivelyLauncherDB db = new LivelyLauncherDB(getActivity());
        db.addNewType(name);
        Log.e(tag, "addNewType:" + name);
        mAdapter.notifyDataSetChanged();
    }

    public CategoryFragment setParent(Launcher launcher){
        this.launcher = launcher;
        return this;
    }


}
