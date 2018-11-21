package com.genius.onclickagent;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import com.genius.R;
import com.genius.onclickagent.OnclickAgent.OnClickListener;

public class DemoActivity extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mContext = this;


//        findViewById(R.id.text).setOnClickListener(new OnclickAgent(this, new OnClickListener() {
//            @Override
//            public void onclick(final View v) {
//                Log.d("genius","click textview");
//            }
//        }));
//
//        findViewById(R.id.btn).setOnClickListener(new OnclickAgent(this, new OnClickListener() {
//            @Override
//            public void onclick(final View v) {
//                Log.d("genius","click btn");
//            }
//        }));

        OnclickAgent onclickAgent = new OnclickAgent(this, new OnClickListener() {
            @Override
            public void onclick(final View v) {
                switch (v.getId())
                {
                    case R.id.text:
                        Log.d("genius","click textview");

                        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 1.2f, 0.85f,1.0f);
                        v.setPivotY(v.getHeight());
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.setDuration(500);
                        animator.start();

                        break;
                    case R.id.btn:
                        Log.d("genius","click btn");
                        break;
                }
            }
        });

        findViewById(R.id.text).setOnClickListener(onclickAgent);

        findViewById(R.id.btn).setOnClickListener(onclickAgent);
    }

}
