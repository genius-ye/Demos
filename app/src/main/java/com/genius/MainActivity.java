package com.genius;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.genius.managers.mediaplaymanager.PlayerActivity;
import com.genius.onclickagent.DemoActivity;
import com.genius.views.coordinatelayoutdemo.CoordinateLayoutDemoActivity;
import com.genius.views.dragrecyclerview.DragRecyclerviewActivity;
import com.genius.views.drawfontview.DrawFrontActivity;
import com.genius.views.expandrecyclerview.ExpandRecyclerviewActivity;
import com.genius.views.horizontalcenterrecyclerview.RecyclerviewActivity;
import com.genius.views.piechartview.PieChartActivity;
import com.genius.views.scrollnumberview.AutoScrollViewActivity;
import com.genius.views.shadowdrawable.ShadowDrawableActivity;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 0x666;
    private String tag = MainActivity.class.getSimpleName();

    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<Bean> mBeans;
    private RecyclerviewAdapter recyclerviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mRecyclerView = findViewById(R.id.recyclerview);
        mBeans = new ArrayList<>();

        {
            Bean bean = new Bean();
            bean.setTitle("测试扫码");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
            mBeans.add(bean);
        }

        {
            Bean bean = new Bean();
            bean.setTitle("饼状图");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, PieChartActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }

        {
            Bean bean = new Bean();
            bean.setTitle("画字");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, DrawFrontActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }

        {
            Bean bean = new Bean();
            bean.setTitle("音频播放测试");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, PlayerActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }

        {
            Bean bean = new Bean();
            bean.setTitle("二级recyclerview");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, ExpandRecyclerviewActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }

        {
            Bean bean = new Bean();
            bean.setTitle("水平recyclerview");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, RecyclerviewActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }

        {
            Bean bean = new Bean();
            bean.setTitle("滚动的数字");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, AutoScrollViewActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }

        {
            Bean bean = new Bean();
            bean.setTitle("代理点击事件");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, DemoActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }

        {
            Bean bean = new Bean();
            bean.setTitle("CoordinateLayout使用");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, CoordinateLayoutDemoActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }

        {
            Bean bean = new Bean();
            bean.setTitle("shadowdrawable阴影使用");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, ShadowDrawableActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }
        {
            Bean bean = new Bean();
            bean.setTitle("DragRecyclerviewActivity实现列表侧滑菜单");
            bean.setOnClick(new Bean.OnClick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(mContext, DragRecyclerviewActivity.class);
                    startActivity(intent);
                }
            });
            mBeans.add(bean);
        }

        initRecyclerview();

    }

    /**
     * 初始化recyclerview
     */
    private void initRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerviewAdapter = new RecyclerviewAdapter(this);
        recyclerviewAdapter.bindDatas(mBeans);
        recyclerviewAdapter.setOnItemClick(new RecyclerviewAdapter.OnItemClick() {
            @Override
            public void onItemClick(final int position) {
                Bean bean = mBeans.get(position);
                if (bean.getOnClick() != null) {
                    bean.getOnClick().onClick();
                }
            }
        });
        mRecyclerView.setAdapter(recyclerviewAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CaptureActivity.RESULT_CODE_QR_SCAN) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
            Log.d(tag, "result= " + scanResult);
            Toast.makeText(MainActivity.this, scanResult, Toast.LENGTH_LONG).show();
        }
    }

}
