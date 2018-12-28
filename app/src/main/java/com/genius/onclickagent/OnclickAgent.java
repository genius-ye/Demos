package com.genius.onclickagent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 点击代理类
 */
public class OnclickAgent implements View.OnClickListener{

    Context context;
    OnClickListener mOnClickListener;

    public OnclickAgent(final Context context, final OnClickListener onClickListener) {
        this.context = context;
        mOnClickListener = onClickListener;
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        Context context = v.getContext();
        if(context instanceof Activity)
        {
            ComponentName name = ((Activity) context).getComponentName();
        }
        String packageName = v.getContext().getPackageName();
        String name = v.getResources().getResourceName(id);
        String resourcePackageName = v.getResources().getResourcePackageName(id);
        String resourceTypeName = v.getResources().getResourceTypeName(id);
        String resourceEntryName = v.getResources().getResourceEntryName(id);
        Toast.makeText(this.context,"我是代理",Toast.LENGTH_LONG).show();

        if(mOnClickListener!=null)
        {
            mOnClickListener.onclick(v);
        }

    }

    interface OnClickListener
    {
        void onclick(View v);
    }

}
