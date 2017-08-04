package apk.cn.zeffect.webviewvideo.utils;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Created by Administrator on 2017/8/4.
 */

public class MoreUtils {
    public static void clearCookies(Context context) {
        CookieSyncManager cookieSyncMngr =
                CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
}
