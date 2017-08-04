package apk.cn.zeffect.webviewvideo.orm;

import android.text.TextUtils;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

import apk.cn.zeffect.webviewvideo.bean.UrlBean;
import apk.cn.zeffect.webviewvideo.utils.Constant;

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

public class OrmHelp {
    public static void save(String url) {
        if (TextUtils.isEmpty(url)) return;
        if (OrmUtils.getLiteOrm().queryCount(new QueryBuilder(UrlBean.class).whereEquals(Constant.URL_KEY, url)) > 0) {
            OrmUtils.getLiteOrm().delete(new WhereBuilder(UrlBean.class).andEquals(Constant.URL_KEY, url));
        }
        OrmUtils.getLiteOrm().save(new UrlBean().setUrl(url).setLastTime(System.currentTimeMillis()));
    }

    public static ArrayList<String> getHistory() {
        ArrayList<String> retuLists = new ArrayList<>();
        ArrayList<UrlBean> tempBeens = OrmUtils.getLiteOrm().query(new QueryBuilder<>(UrlBean.class).appendOrderDescBy(Constant.LAST_TIME_KEY));
        if (tempBeens != null && tempBeens.size() > 0) {
            for (UrlBean tempBean : tempBeens) {
                retuLists.add(tempBean.getUrl());
            }
        }
        return retuLists;
    }
}
