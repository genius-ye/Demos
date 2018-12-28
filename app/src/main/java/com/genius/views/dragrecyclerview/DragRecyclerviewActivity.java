package com.genius.views.dragrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import com.genius.R;

import java.util.ArrayList;
import java.util.List;

public class DragRecyclerviewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_recyclerview);

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerviewAdapter = new RecyclerviewAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(recyclerviewAdapter);

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("测试---"+i);
        }

        recyclerviewAdapter.bindDatas(strings);

        CustomItemTouchHelperCallback customItemTouchHelperCallback = new CustomItemTouchHelperCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(customItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        customItemTouchHelperCallback.setItemTouchListener(new ItemTouchListener() {
            @Override
            public void onItemSwiped(final int position, final int direction) {
                recyclerviewAdapter.notifyItemChanged(position);
            }
        });
    }


}
