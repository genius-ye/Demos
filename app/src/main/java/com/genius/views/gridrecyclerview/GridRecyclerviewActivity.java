package com.genius.views.gridrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.genius.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择图片——网格列表
 */
public class GridRecyclerviewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerviewAdapter mRecyclerviewAdapter;
    private List<String> datas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_recyclerview);
        mRecyclerView = findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerviewAdapter = new RecyclerviewAdapter(this);
        mRecyclerView.setAdapter(mRecyclerviewAdapter);

        datas.add("add");
        mRecyclerviewAdapter.bindDatas(datas);

        mRecyclerviewAdapter.setCallback(new RecyclerviewAdapter.Callback() {
            @Override
            public void onItemClick(final int position) {
                if(position==datas.size()-1)
                {
                    datas.add("new");
                    mRecyclerviewAdapter.bindDatas(datas);
                }
            }
        });
    }

}
