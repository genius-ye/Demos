package com.genius.views.dragrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

public class CustomItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchListener mItemTouchListener;
    /**
     * 左右可滑动的距离
     */
    private int swipeDistance = 150;

    public void setItemTouchListener(final ItemTouchListener itemTouchListener) {
        mItemTouchListener = itemTouchListener;
    }

    @Override
    public int getMovementFlags(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
//        int dragFlags = ItemTouchListener.UP | ItemTouchListener.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
        Log.d("genius", "onSwiped");

        if(mItemTouchListener != null)
        {
            mItemTouchListener.onItemSwiped(viewHolder.getAdapterPosition(),direction);
        }
    }

    @Override
    public void onChildDraw(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //如果dX小于等于删除方块的宽度，那么我们把该方块滑出来
            if (Math.abs(dX) <= dip2px(recyclerView.getContext(), swipeDistance)) {
                viewHolder.itemView.scrollTo(-(int) dX, 0);
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Log.d("genius", "onChildDrawOver");
//        clearView(recyclerView,viewHolder);
    }

    @Override
    public void clearView(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        Log.d("genius", "clearView");
        if (viewHolder instanceof RecyclerviewAdapter.ViewHolder) {
            RecyclerviewAdapter.ViewHolder holder = (RecyclerviewAdapter.ViewHolder) viewHolder;
            viewHolder.itemView.scrollTo(0, 0);
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
