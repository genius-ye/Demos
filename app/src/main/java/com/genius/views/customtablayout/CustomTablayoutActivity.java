package com.genius.views.customtablayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.genius.BaseActivity;
import com.genius.R;

import java.util.ArrayList;
import java.util.List;

public class CustomTablayoutActivity extends BaseActivity {

    private CustomTablayout mCustomTablayout;
    private MyTabLayout mMyTabLayout;
    private ViewPager mViewPager;
    private List<String> titles;
    public static void start(Activity activity)
    {
        Intent intent = new Intent(activity,CustomTablayoutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_tablayout);
        mCustomTablayout = findViewById(R.id.customtablayout);
        mMyTabLayout = findViewById(R.id.mytablayout);
        mViewPager = findViewById(R.id.viewPager);

        titles = new ArrayList<>();
        titles.add("标题");
        titles.add("我是一个很长很长的标题");
        titles.add("111");
        for (int i = 0; i < 10; i++) {
            titles.add("标题"+i);
        }
//        mCustomTablayout.setTitle(titles);
        mViewPager.setAdapter(new myPagerAdapter());


        mMyTabLayout.setupWithViewPager(mViewPager);
        mCustomTablayout.setupWithViewPager(mViewPager);
    }

    class myPagerAdapter extends PagerAdapter
    {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_drag,null);
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }

        @Override
        public void destroyItem(@NonNull final ViewGroup container, final int position, @NonNull final Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
            return view == object;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(final int position) {
            return titles.get(position);
        }
    }
}
