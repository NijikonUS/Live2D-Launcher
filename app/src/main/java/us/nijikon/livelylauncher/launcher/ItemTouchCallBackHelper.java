package us.nijikon.livelylauncher.launcher;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.text.Collator;
import java.util.concurrent.atomic.AtomicBoolean;

import us.nijikon.livelylauncher.adapters.ItemTouchHelperAdapter;
import us.nijikon.livelylauncher.models.AppModel;

/**
 * Created by bowang .
 */
public class ItemTouchCallBackHelper extends ItemTouchHelper.Callback {

    public static final String tag = "ItemTouchCallBackHelper";

    public static final float ALPHA_FULL = 1.0f;

    private AtomicBoolean vibarated = new AtomicBoolean(false);

    private final ItemTouchHelperAdapter mAdapter;

    public ItemTouchCallBackHelper(ItemTouchHelperAdapter itemTouchHelperAdapter){
        this.mAdapter = itemTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();

            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
            if(dX > viewHolder.itemView.getWidth()/2 && isCurrentlyActive) {
               shake(recyclerView.getContext());
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.RIGHT ;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if(viewHolder.getItemViewType() != target.getItemViewType()){
            return false;
        }
        mAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return  true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState){
        vibarated.set(false);
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        shake(viewHolder.itemView.getContext());
        final int position = viewHolder.getAdapterPosition();
        AppDataHolder.getInstance().uninstall(position,viewHolder.itemView.getContext());
//        AppModel thisApp =  AppDataHolder.getInstance().getData().get(position);
//        new AlertDialog.Builder(viewHolder.itemView.getContext()).setIcon(thisApp.getAppIcon())
//                .setTitle(thisApp.getAppName()).setMessage("Do you want to uninstall this app?")
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mAdapter.onRefresh(position);
//                    }
//                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (AppDataHolder.getInstance().uninstall(position,viewHolder.itemView.getContext())){
//                    mAdapter.onItemDismiss(position);
//                }
//            }
//        }).create().show();
    }

    private void shake(Context context){
        Vibrator myVibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
        if(myVibrator.hasVibrator() && !vibarated.get()){
            vibarated.set(true);
            myVibrator.vibrate(new long[]{0,200,0,0},-1);
        }
    }
}
