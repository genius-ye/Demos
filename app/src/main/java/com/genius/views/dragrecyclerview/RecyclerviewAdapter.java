package com.genius.views.dragrecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.genius.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表适配器
 */
class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
    private List<String> mDatas = new ArrayList<>();
    private Context mContext;

    public RecyclerviewAdapter(final Context context) {
        mContext = context;
    }

    /**
     * 绑定数据
     * @param datas
     */
    public void bindDatas(List<String> datas)
    {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_drag,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.text.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        TextView tv_slide_left;
        TextView tv_slide_right;
        LinearLayout linear;
        LinearLayout layout_main;
        public ViewHolder(final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            tv_slide_left = itemView.findViewById(R.id.tv_slide_left);
            tv_slide_right = itemView.findViewById(R.id.tv_slide_right);
            linear = itemView.findViewById(R.id.linear);
            layout_main = itemView.findViewById(R.id.layout_main);
        }
    }
}