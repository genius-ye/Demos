package com.genius.qiyukfdemo;

import android.content.Context;

public class OnlineService implements IOnlineService{

    private IOnlineService mOnlineService;

    private static OnlineService instance;

    public static OnlineService getInstance() {
        if (instance == null) {
            synchronized (OnlineService.class) {
                instance = new OnlineService();
            }
        }
        return instance;
    }

    private OnlineService() {
        mOnlineService = new QiyuOnlineService();
    }


    @Override
    public void init(Context context) {
        mOnlineService.init(context);
    }

    @Override
    public void openOnlineService(final Context context, final String title, final String sourceUrl, final String sourceTitle) {
        mOnlineService.openOnlineService(context,title,sourceUrl,sourceTitle);
    }

    @Override
    public void setVisitorHeader(final String header) {
        mOnlineService.setVisitorHeader(header);
    }
}
