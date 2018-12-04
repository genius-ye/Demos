package com.genius.views.shadowdrawable;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.genius.R;

public class ShadowDrawableActivity extends AppCompatActivity {
    private CardView mCardView;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow_drawable);

        mCardView = findViewById(R.id.cardview);
        mTextView = findViewById(R.id.textview);
        ShadowDrawable.setShadowDrawable(mCardView,Color.parseColor("#ffffff"),20,
                Color.parseColor("#12000000"), 5, 0, 5);
    }
}
