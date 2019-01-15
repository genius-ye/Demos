package com.genius.qiyukfdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;

/**
 * 在线客服
 */
public class QiyuOnlineService implements IOnlineService {

    /**
     * 第三方客服的appKey
     */
    private String appKey = "312ae0d0b6017793caa1a1daedc7bc11";

    @Override
    public void init(Context context) {
        // appKey 可以在七鱼管理系统->设置->App 接入 页面找到
        Unicorn.init(context, "312ae0d0b6017793caa1a1daedc7bc11", options(), new UnicornImageLoader() {
            @Nullable
            @Override
            public Bitmap loadImageSync(final String uri, final int width, final int height) {
                return null;
            }

            @Override
            public void loadImage(final String uri, final int width, final int height, final ImageLoaderListener listener) {

            }
        });
    }

    @Override
    public void openOnlineService(final Context context, final String title, final String sourceUrl, final String sourceTitle) {
        /**
         * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
         * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
         * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
         */
        ConsultSource source = new ConsultSource(sourceUrl, sourceTitle, "custom information string");
        /**
         * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
         * 如果返回为false，该接口不会有任何动作
         *
         * @param context 上下文
         * @param title   聊天窗口的标题
         * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
         */
        Unicorn.openServiceActivity(context, title, source);
    }

    @Override
    public void setVisitorHeader(final String header) {
        YSFOptions options = new YSFOptions();
        options.uiCustomization = new UICustomization();
        options.uiCustomization.rightAvatar = header;
        Unicorn.updateOptions(options);
    }


    // 如果返回值为null，则全部使用默认参数。
    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        return options;
    }

}
