package us.nijikon.livelylauncher.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.assistant.CategoryFragment;
import us.nijikon.livelylauncher.assistant.CategoryFragment;
import us.nijikon.livelylauncher.assistant.NoteFragment;
import us.nijikon.livelylauncher.assistant.NoteFragment;
import us.nijikon.livelylauncher.assistant.ShowContactFragment;
import us.nijikon.livelylauncher.launcher.Launcher;
import us.nijikon.livelylauncher.models.Event;

//import us.nijikon.livelylauncher.assistant.AssistActivity;
//import us.nijikon.livelylauncher.assistant.ContactActivity;

/**
 * Created by mjwei on 3/24/16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<String> mDataset;
    private String typeName;
    private Event event;
    private CategoryFragment category;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holderl


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            cv = (CardView)v.findViewById(R.id.cv);
            mTextView = (TextView)v.findViewById(R.id.info_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CategoryAdapter(List<String> mDataset, CategoryFragment category) {
        this.mDataset = mDataset;
        this.category = category;
    }

    public String getTypeName(){
        return typeName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false));
        // set the view's size, margins, paddings and layout parameters

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        holder.mTextView.setText(mDataset.get(position));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typeName = mDataset.get(position);
                category.setCategoryName(typeName);
                Log.e("type:", typeName);

                switch(position){
                    case 0:{
//                        Intent intent = new Intent(v.getContext(), ContactActivity.class);
//                        v.getContext().startActivity(intent);

                        ((Launcher)v.getContext()).goFragment(ShowContactFragment.tag);
                        break;
                    }
                    default:{
                        ((Launcher)v.getContext()).goFragment(NoteFragment.tag);
                        break;
                    }
                }

            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
