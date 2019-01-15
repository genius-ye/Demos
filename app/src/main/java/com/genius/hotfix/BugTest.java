package com.genius.hotfix;

import android.content.Context;
import android.widget.Toast;

public class BugTest {

    int a = 100;
    int b = 0;

    public void caculate(Context context) {
        Toast.makeText(context, "计算结果为：" + a / b, Toast.LENGTH_LONG).show();
    }

    public String test() {
        return "我是一个bug！！！";
    }


}
