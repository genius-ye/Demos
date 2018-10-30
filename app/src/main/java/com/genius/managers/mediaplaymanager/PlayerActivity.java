package com.genius.managers.mediaplaymanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.genius.R;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private Button btn1,btn2,btn3;
    private Context mContext;
    private String audioPath = "https://alitest.up360.com/upload/system/chinese_word/2018/08/13/32_1534124512.mp3";

//    private String audioPath = "https://data.up360.com/upload/system/chinese_word/2018/08/15/49_1534295189.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mContext = this;

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ArrayList<String> paths = new ArrayList<>();
                paths.add(audioPath);
                paths.add(audioPath);
                MediaPlayerManager.getInStance(mContext).play(paths);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MediaPlayerManager.getInStance(mContext).pause();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MediaPlayerManager.getInStance(mContext).stop();
            }
        });
    }
}
