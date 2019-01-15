package com.genius.hotfix;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.App;
import com.genius.BaseActivity;
import com.genius.MainActivity;
import com.genius.R;

import java.io.File;

public class HotfixActivity extends BaseActivity {
    private TextView tv_bug;
    private TextView tv_fix;
    private BugTest mBugTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotfix);

        tv_bug = findViewById(R.id.tv_bug);
        tv_fix = findViewById(R.id.tv_fix);
        mBugTest = new BugTest();

        tv_bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                Toast.makeText(mContext,mBugTest.test(),Toast.LENGTH_LONG).show();
                mBugTest.caculate(mContext);
            }
        });
        tv_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                fix();
            }
        });
    }

    private void fix() {
        try {
            String name = "hotfix.dex";
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + name;
            File dex = new File(filePath);
            HotFix.copyDex(HotfixActivity.this,dex);
            Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "修复失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
