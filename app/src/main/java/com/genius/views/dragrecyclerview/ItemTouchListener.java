package com.genius.views.dragrecyclerview;

public interface ItemTouchListener {

    /**
     * item左右滑动监听
     * @param position
     */
    void onItemSwiped(int position,int direction);
    /**
     * 删除item
     * @param position
     */
//    void onItemDelete(int position);
}
