package com.genius.views.gridrecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.genius.R;

import java.util.ArrayList;
import java.util.List;

class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.Viewholder> {
    private List<String> datas = new ArrayList<>();
    private Callback mCallback;

    private Context mContext;

    public RecyclerviewAdapter(final Context context) {
        mContext = context;
    }

    public void bindDatas(List<String> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_grid, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        if (position == datas.size() - 1) {
            holder.img.setImageResource(R.mipmap.add);
        } else {
            holder.img.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setCallback(final Callback callback) {
        mCallback = callback;
    }

    class Viewholder extends RecyclerView.ViewHolder {

        ImageView img;

        public Viewholder(final View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            if (mCallback != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mCallback.onItemClick(getAdapterPosition());
                    }
                });
            }
        }
    }

    interface Callback {
        void onItemClick(int position);
    }
}