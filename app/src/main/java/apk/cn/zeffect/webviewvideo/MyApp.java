package apk.cn.zeffect.webviewvideo;

import android.app.Application;

import apk.cn.zeffect.webviewvideo.orm.OrmUtils;

/**
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/08/04
 *      desc    ：
 *      version:：1.0
 * </pre>
 *
 * @author zzx
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OrmUtils.defaultInit(getApplicationContext());
    }
}
