package com.genius.views.scrollnumberview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.genius.R;
import com.genius.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class AutoScrollViewActivity extends AppCompatActivity {
    private AutoScrollNumView autoscrolltextview1;
    private AutoScrollNumView autoscrolltextview2;
    private AutoScrollNumView autoscrolltextview3;

    private LinearLayout linear_num;
    private List<AutoScrollNumView> mAutoScrollNumViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_scroll_view);

        autoscrolltextview1 = findViewById(R.id.autoscrolltextview1);
        autoscrolltextview2 = findViewById(R.id.autoscrolltextview2);
        autoscrolltextview3 = findViewById(R.id.autoscrolltextview3);

        linear_num = findViewById(R.id.linear_num);
        mAutoScrollNumViews = new ArrayList<>();

        initData();

        for (int i = 0; i < 5; i++) {
            AutoScrollNumView autoScrollNumView = new AutoScrollNumView(this);
            autoScrollNumView.setTextSize(40);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtils.dip2px(this,40),DisplayUtils.dip2px(this,50));
            params.leftMargin = DisplayUtils.dip2px(this,5);
            autoScrollNumView.setLayoutParams(params);
            autoScrollNumView.setTextColor(Color.parseColor("#FFFC6156"));
            autoScrollNumView.setDuration(1000+i*100);
            mAutoScrollNumViews.add(autoScrollNumView);
            linear_num.addView(autoScrollNumView);
        }


        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                initData();
                for (int i = 0; i < mAutoScrollNumViews.size(); i++) {
                 mAutoScrollNumViews.get(i).startAnim(0,9);
                }
            }
        });
    }

    private void initData() {
        autoscrolltextview1.startAnim(0,9);
        autoscrolltextview2.startAnim(8,1);
        autoscrolltextview3.startAnim(3,7);
        autoscrolltextview1.setDuration(1000);
        autoscrolltextview2.setDuration(2000);
        autoscrolltextview3.setDuration(4000);

        autoscrolltextview3.setTextSize(40);

    }
}
