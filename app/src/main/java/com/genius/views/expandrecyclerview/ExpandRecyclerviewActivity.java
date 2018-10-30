package com.genius.views.expandrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.R;

import java.util.ArrayList;

public class ExpandRecyclerviewActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private ArrayList<DataTree<GroupBean,String>> datas;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_recyclerview);
        mContext = this;
        recyclerview = findViewById(R.id.recyclerview);

        //模拟数据
        datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GroupBean groupBean = new GroupBean("group: "+i,false);
            ArrayList<String> tmp = new ArrayList();
            for (int j = 0; j < 3; j++) {
                tmp.add("subitem: "+j);
            }
            DataTree<GroupBean,String> dataTree = new DataTree<>(groupBean,tmp);
            datas.add(dataTree);
        }

        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        //添加Android自带的分割线
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerview.setAdapter(new RecyclerviewAdapter());
    }

   public class RecyclerviewAdapter extends RecyclerView.Adapter
    {
        final int ITEM_TYPE_GROUP = 0x001;
        final int ITEM_TYPE_SUBITEM = 0x002;
        ArrayList<DataBean> mDataBeans;
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            switch (viewType)
            {
                case ITEM_TYPE_GROUP:
                    View view= LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_group,parent,false);
                    viewHolder = new GroupViewHolder(view);
                    break;
                case ITEM_TYPE_SUBITEM:
                    View view1= LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_subitem,parent,false);
                    viewHolder = new SubItemViewHolder(view1);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            switch (mDataBeans.get(position).getItemType())
            {
                case ITEM_TYPE_GROUP:
                    GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
                    GroupBean groupBean = (GroupBean) mDataBeans.get(position).getBean();
                    groupViewHolder.text_recyclerview_group.setText(groupBean.getName());
                    break;
                case ITEM_TYPE_SUBITEM:
                    SubItemViewHolder subItemViewHolder = (SubItemViewHolder) holder;
                    String string = (String) mDataBeans.get(position).getBean();
                    subItemViewHolder.text_recyclerview_subitem.setText(string);
                    break;
            }
        }

        @Override
        public int getItemViewType(final int position) {
            return mDataBeans.get(position).getItemType();
        }

        @Override
        public int getItemCount() {
            int count = 0;
            mDataBeans = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                count++;
                DataBean groupBean = new DataBean<GroupBean>(ITEM_TYPE_GROUP,datas.get(i).getGroupBean());
                mDataBeans.add(groupBean);
                if(datas.get(i).getGroupBean().isExpand)
                {
                    for (int j = 0; j < datas.get(i).getSubItems().size(); j++) {
                        DataBean itemBean = new DataBean<String>(ITEM_TYPE_SUBITEM,datas.get(i).getSubItems().get(j));
                        mDataBeans.add(itemBean);
                        count++;
                    }
                }
            }
            return count;
        }

        class GroupViewHolder extends RecyclerView.ViewHolder
        {
            TextView text_recyclerview_group;
            public GroupViewHolder(final View itemView) {
                super(itemView);
                text_recyclerview_group = itemView.findViewById(R.id.text_recyclerview_group);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        GroupBean groupBean = (GroupBean) mDataBeans.get(getLayoutPosition()).getBean();
                        groupBean.setExpand(!groupBean.isExpand);
                       notifyDataSetChanged();
                    }
                });
            }
        }

        class SubItemViewHolder extends RecyclerView.ViewHolder
        {
            TextView text_recyclerview_subitem;
            public SubItemViewHolder(final View itemView) {
                super(itemView);
                text_recyclerview_subitem = itemView.findViewById(R.id.text_recyclerview_subitem);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        String string = (String) mDataBeans.get(getLayoutPosition()).getBean();
                        Toast.makeText(ExpandRecyclerviewActivity.this,"点击***"+string,Toast.LENGTH_LONG).show();
                        Log.d("genius","点击***"+string);
                    }
                });
            }
        }

        class DataBean<T>
        {
            int itemType;
            T bean;

            public DataBean(final int itemType, final T bean) {
                this.itemType = itemType;
                this.bean = bean;
            }

            public int getItemType() {
                return itemType;
            }

            public void setItemType(final int itemType) {
                this.itemType = itemType;
            }

            public T getBean() {
                return bean;
            }

            public void setBean(T bean) {
                this.bean = bean;
            }
        }
    }

    public class GroupBean
    {
        String name;
        boolean isExpand;

        public GroupBean(final String name, final boolean isExpand) {
            this.name = name;
            this.isExpand = isExpand;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public boolean isExpand() {
            return isExpand;
        }

        public void setExpand(final boolean expand) {
            isExpand = expand;
        }
    }

    public class DataTree<K,V>
    {
        private K groupBean;
        private ArrayList<V> subItems;

        public DataTree(final K groupBean, final ArrayList<V> subItems) {
            this.groupBean = groupBean;
            this.subItems = subItems;
        }

        public K getGroupBean() {
            return groupBean;
        }

        public void setGroupBean(final K groupBean) {
            this.groupBean = groupBean;
        }

        public ArrayList<V> getSubItems() {
            return subItems;
        }

        public void setSubItems(final ArrayList<V> subItems) {
            this.subItems = subItems;
        }
    }
}
