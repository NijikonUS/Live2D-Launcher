package us.nijikon.livelylauncher.adapters;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.assistant.ItemFragment.OnListFragmentInteractionListener;
import us.nijikon.livelylauncher.models.Event;
import us.nijikon.livelylauncher.models.Person;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = ".MyItemAdapter";
    private List<Event> mValues;
    private List<String> typeNames;
    private String[] personList;
    private OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<Event> items, List<String> typeNames,String[] personList) {
        this.mValues = items;
        this.typeNames = typeNames;
        this.personList = personList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.e(TAG, "store events:" + mValues.get(position).getNote());

        holder.mIdView.setText(" Type: " + typeNames.get(position));
        holder.mContentView.setText(" Note: "+mValues.get(position).getNote());
        holder.mEventTime.setText(" Time: " + mValues.get(position).getDate());

//        if(typeNames.get(position).equals("Contact")){
//            Log.e(TAG, "type!!!:"+typeNames.get(position));
//            holder.person.setText(personList[position]);
//        }

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public TextView mIdView,mContentView,mEventTime,person;

        public ViewHolder(View view) {
            super(view);
            cv = (CardView)view.findViewById(R.id.cv);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mEventTime = (TextView) view.findViewById(R.id.time);
            person = (TextView) view.findViewById(R.id.person);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
