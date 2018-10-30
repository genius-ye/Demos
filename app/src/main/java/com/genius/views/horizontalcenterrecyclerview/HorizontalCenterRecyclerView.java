package com.genius.views.horizontalcenterrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by wangly on 2018/8/7.
 */

public class HorizontalCenterRecyclerView extends RecyclerView {

    /**
     * 每个item的宽度
     */
    private int childWidth;
    private OnItemSelectCallback mOnItemSelectCallback;
    /**
     * 上一次选中的item
     */
    private int lastItemSelect;
    /**
     * 滑动的距离
     */
    private int count;
    public HorizontalCenterRecyclerView(final Context context) {
        super(context);
    }

    public HorizontalCenterRecyclerView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalCenterRecyclerView(final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }


    public void init(Context context)
    {
        DisplayMetrics defaultDisplay = context.getResources().getDisplayMetrics();
        int heigth = defaultDisplay.heightPixels;
        int width = defaultDisplay.widthPixels;
        int pading = width/2-getChildWidth()/2;
        setPadding(pading,0,pading,0);
    }

    @Override
    public void onScrollStateChanged(final int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE) {
            if (isCheck) {
                refreshFocusItemToCenter();
            }
        }
    }

    @Override
    public void onScrolled(final int dx, final int dy) {
        super.onScrolled(dx, dy);
        count += dx;
    }

    /**
     * 是否需要检测
     */
    boolean isCheck;

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        if (ev.getAction() == KeyEvent.ACTION_UP) {
            if (this.hasFocus()) {
                isCheck = true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void refreshFocusItemToCenter() {
        if (getChildCount() > 0) {
            int dis;
            if (childWidth <= 0) {
                childWidth = getChildAt(0).getWidth();
            }
            if (count > -childWidth / 2 && count < childWidth / 2) {
                dis = -Math.abs(count);
            }
            else
            {
                dis = childWidth - Math.abs(count) % childWidth;
            }
            isCheck = false;
            if (count > 0) {
                scrollBy(dis, 0);
            } else {
                scrollBy(-dis, 0);
            }

            if(mOnItemSelectCallback!=null)
            {
                if(count%childWidth == 0)
                {
                    lastItemSelect = lastItemSelect+count/childWidth;
                }
                else {
                    if(count>0)
                    {
                        lastItemSelect = lastItemSelect+count/childWidth+1;
                    }
                    else
                    {
                        lastItemSelect = lastItemSelect+count/childWidth-1;
                    }
                }
                mOnItemSelectCallback.onItemSelect(lastItemSelect);
            }
            count = 0;
        }
    }

    public void setChildWidth(final int childWidth) {
        this.childWidth = childWidth;
    }

    public int getChildWidth() {
        return childWidth;
    }

    public void setOnItemSelectCallback(final OnItemSelectCallback onItemSelectCallback) {
        mOnItemSelectCallback = onItemSelectCallback;
    }

    interface OnItemSelectCallback
    {
        void onItemSelect(int position);
    }

}