package com.genius.qiyukfdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.genius.BaseActivity;
import com.genius.R;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;

public class QiyukfDemoActivity extends BaseActivity {
    private TextView tv_kf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiyukf_demo);

        tv_kf = findViewById(R.id.tv_kf);
        tv_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                OnlineService.getInstance().setVisitorHeader(getResourcesUri(mContext,R.mipmap.ic_launcher));
                OnlineService.getInstance().openOnlineService(mContext,"测试客服","www.xxx.com","测试来源");
            }
        });
    }

    public String getResourcesUri(Context context, @DrawableRes int id) {
        Resources resources = context.getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }
}
