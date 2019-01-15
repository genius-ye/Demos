package com.genius.qiyukfdemo;

import android.content.Context;
import android.net.Uri;

public interface IOnlineService {

    /**
     * 初始化在线客服
     *
     * @param context
     */
    void init(Context context);

    /**
     * 打开在线客服
     *
     * @param context
     * @param title       ：客服的标题
     * @param sourceUrl   ：来源页面的url
     * @param sourceTitle ：来源页面标题
     */
    void openOnlineService(Context context, String title, String sourceUrl, String sourceTitle);

    /**
     * 设置访客头像
     * @param header
     */
    void setVisitorHeader(String header);
}
