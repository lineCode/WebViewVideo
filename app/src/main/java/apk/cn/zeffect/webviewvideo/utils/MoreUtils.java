package apk.cn.zeffect.webviewvideo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import apk.cn.zeffect.webviewvideo.orm.OrmHelp;
import apk.cn.zeffect.webviewvideo.orm.OrmUtils;

/**
 * Created by Administrator on 2017/8/4.
 */

public class MoreUtils {
    public static void saveUrl(String url) {
        if (TextUtils.isEmpty(url)) return;
        if (!isUrl(url)) return;
        OrmHelp.save(url);
    }


    public static boolean isUrl(String url) {
        if (TextUtils.isEmpty(url)) return false;
        if (!url.startsWith("http://") && !url.startsWith("https://")) return false;
        return true;
    }


    public static void clearCookies(Context context) {
        CookieSyncManager cookieSyncMngr =
                CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
}
