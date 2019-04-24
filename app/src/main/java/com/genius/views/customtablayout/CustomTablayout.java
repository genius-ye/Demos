package com.genius.views.customtablayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustomTablayout extends TabLayout {
    private List<String> titles;
    private ViewPager mViewPager;
    /**
     * 滑动过程中指示器的水平偏移量
     */
    private int mTranslationX;
    private Paint mSelectedIndicatorPaint;
    /**
     * 指示器初始X偏移量
     */
    private int mInitTranslationX;
    private int mIndicatorWidth = 100;

    public CustomTablayout(final Context context) {
        super(context);
        init(context);
    }

    public CustomTablayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTablayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        titles = new ArrayList<>();
        mSelectedIndicatorPaint = new Paint();
        mSelectedIndicatorPaint.setAntiAlias(true);
        mSelectedIndicatorPaint.setColor(Color.RED);
//        this.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(Tab tab) {
//                /**
//                 * 设置当前选中的Tab为特殊高亮样式。
//                 */
//                if (tab != null && tab.getCustomView() != null) {
//                    TextView tab_text = tab.getCustomView().findViewById(R.id.tab_text);
//
//                    TextPaint paint = tab_text.getPaint();
//                    paint.setFakeBoldText(true);
//
//                    tab_text.setTextColor(Color.parseColor("#ff813e"));
//
//                    ImageView tab_layout_indicator = tab.getCustomView().findViewById(R.id.tab_indicator);
//                    tab_layout_indicator.setBackgroundResource(R.drawable.shape_tab_indicator_ff813e);
//                }
//
//                if (mViewPager != null) {
//                    mViewPager.setCurrentItem(tab.getPosition());
//                }
//            }
//
//            @Override
//            public void onTabUnselected(Tab tab) {
//                /**
//                 * 重置所有未选中的Tab颜色、字体、背景恢复常态(未选中状态)。
//                 */
//                if (tab != null && tab.getCustomView() != null) {
//                    TextView tab_text = tab.getCustomView().findViewById(R.id.tab_text);
//
//                    tab_text.setTextColor(getResources().getColor(android.R.color.holo_red_light));
//                    TextPaint paint = tab_text.getPaint();
//                    paint.setFakeBoldText(false);
//
//                    ImageView tab_indicator = tab.getCustomView().findViewById(R.id.tab_indicator);
//                    tab_indicator.setBackgroundResource(0);
//                }
//            }
//
//            @Override
//            public void onTabReselected(Tab tab) {
//
//            }
//        });

    }

    @Override
    public void setupWithViewPager(@Nullable final ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
        mViewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
//                Log.d("genius","positionOffsetPixels ="+positionOffsetPixels);
                redrawIndicator(position, positionOffset);
                if (position == viewPager.getCurrentItem()) {
                    Log.d("genius", "左滑");
                } else {
                    Log.d("genius", "右滑");
                }
            }

            @Override
            public void onPageSelected(final int position) {
                getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
    }

//    public void setTitle(List<String> titles) {
//        this.titles = titles;
//
//        /**
//         * 开始添加切换的Tab。
//         */
//        for (String title : this.titles) {
//            Tab tab = newTab();
//            tab.setCustomView(R.layout.item_tablayout);
//
//            if (tab.getCustomView() != null) {
//                TextView tab_text = tab.getCustomView().findViewById(R.id.tab_text);
//                tab_text.setText(title);
//            }
//
//            this.addTab(tab);
//        }
//    }

    /**
     * 重绘下标
     */
    public void redrawIndicator(int position, float positionOffset) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        View child = viewGroup.getChildAt(position);
//        Tab tab = getTabAt(position);
        int width = child.getWidth();
        Log.d("genius", "position = "+position+"   child.getWidth() =" + width);
        int totalWidth = 0;
        for (int i = 0; i < position; i++) {
            totalWidth += viewGroup.getChildAt(i).getWidth();
        }
//        mTranslationX = (int) ((position + positionOffset) * width);
        mTranslationX = (int) (totalWidth + positionOffset * viewGroup.getChildAt(position).getWidth());
        mInitTranslationX = (width-mIndicatorWidth)/2;
//        mInitTranslationX = mIndicatorWidth / 2;
        invalidate();
    }


    @Override
    protected void dispatchDraw(final Canvas canvas) {
        canvas.save();
        // 平移到正确的位置，修正tabs的平移量
        canvas.translate(mTranslationX + mInitTranslationX, 0);
        canvas.drawRoundRect(new RectF(0, getHeight() - 20,
                mIndicatorWidth, getHeight()), 100, 100, mSelectedIndicatorPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }
}
