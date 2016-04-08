package us.nijikon.livelylauncher.adapters;

/**
 * Created by bowang .
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * delete data at {@code position},
     *
     * @param position
     */
    void onItemDismiss(int position);

    void onRefresh(int position);
}
