package com.genius;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.genius.hotfix.HotFix;
import com.genius.qiyukfdemo.OnlineService;

/**
 * Created by geniusye on 2018/7/2.
 */

public class App extends Application {

    private static MyActivityLifecycle myActivityLifecycle;

    @Override
    public void onCreate() {
        super.onCreate();
        myActivityLifecycle = new MyActivityLifecycle();
        registerActivityLifecycleCallbacks(myActivityLifecycle);

        OnlineService.getInstance().init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        HotFix.loadFixedDex(base);
    }

    /**
     *
     * @return  true  处于后台   false  前台
     */
    public static boolean isAppBackground() {
        if (myActivityLifecycle.getStartCount() == 0) {
            return true;
        }
        return false;
    }


    public class MyActivityLifecycle implements Application.ActivityLifecycleCallbacks {
        private int startCount;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.e("==============","======>onActivityCreated");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.e("==============","======>onActivityStarted");
            startCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.e("==============","======>onActivityResumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.e("==============","======>onActivityPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.e("==============","======>onActivityStopped");
            startCount--;
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.e("==============","======>onActivityDestroyed");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.e("==============","======>onActivitySaveInstanceState");
        }

        public int getStartCount(){
            return startCount;
        }
    }

}
