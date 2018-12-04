package com.genius.views.coordinatelayoutdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genius.R;

public class CoordinateLayoutDemoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate_layout_demo);

        mContext = this;

        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new RecyclerViewAdapter());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHoler>
    {

        @NonNull
        @Override
        public ViewHoler onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            TextView textView = new TextView(mContext);
            textView.setText("测试");
            textView.setHeight(60);
            return new ViewHoler(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHoler holder, final int position) {

        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class ViewHoler extends RecyclerView.ViewHolder
        {

            public ViewHoler(final View itemView) {
                super(itemView);
            }
        }
    }
}
