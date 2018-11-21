package com.genius.onclickagent;

import android.app.Activity;
import android.content.Context;
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
        Toast.makeText(context,"我是代理",Toast.LENGTH_LONG).show();

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
