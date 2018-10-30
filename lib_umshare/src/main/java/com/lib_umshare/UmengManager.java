package com.lib_umshare; /**
 * Created by WST on 2018/7/2.
 */

import android.app.Activity;
import android.content.Context;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * umeng管理器
 */
public class UmengManager {

    /**
     * 初始化友盟
     */
    public static void init(Context context)
    {
        UMConfigure.init(context,"565d71b7e0f55aca90003cc0","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");

        //微信
        PlatformConfig.setWeixin("wx45f57620c91d8cb0", "de9ed7a5e6f9793790d7e961fa04a690");
        //钉钉
        PlatformConfig.setDing("dingoa2w5wmxlyl88jfkpc");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        PlatformConfig.setAlipay("2015111700822536");
        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        PlatformConfig.setPinterest("1439206");
        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");

        PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J");
        PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a");

        UMConfigure.setLogEnabled(true);

    }

    /**
     * 分享(调用分享前确保umeng已经初始化过)
     */
    public static void share(Activity activity, UMShareListener umShareListener)
    {
        new ShareAction(activity).withText("test hello").setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QQ,SHARE_MEDIA.SMS,SHARE_MEDIA.DINGTALK)
                .setCallback(umShareListener).open();
    }

    /**
     * 第三方登录
     * @param activity
     * @param umAuthListener
     */
    public static void thirdLogin(Activity activity,UMAuthListener umAuthListener)
    {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(activity).setShareConfig(config);

        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
    }
}
