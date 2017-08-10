package apk.cn.zeffect.webviewvideo.orm;

import android.text.TextUtils;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apk.cn.zeffect.webviewvideo.bean.Rule;
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
    public static boolean save(String url) {
        if (TextUtils.isEmpty(url)) return false;
        if (OrmUtils.getLiteOrm().queryCount(new QueryBuilder(UrlBean.class).whereEquals(Constant.URL_KEY, url)) > 0) {
            OrmUtils.getLiteOrm().delete(new WhereBuilder(UrlBean.class).andEquals(Constant.URL_KEY, url));
        }
        return OrmUtils.getLiteOrm().save(new UrlBean().setUrl(url).setLastTime(System.currentTimeMillis())) > 0;
    }


    public static boolean saveRule(String url, String regex) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(regex)) return false;
        if (OrmUtils.getLiteOrm().queryCount(new QueryBuilder(Rule.class).whereEquals(Constant.URL_KEY, url).whereAppendAnd().whereEquals(Constant.RULE_KEY, regex)) > 0) {
            return false;
        }
        OrmUtils.getLiteOrm().save(new Rule().setUrl(url).setRule(regex));
        return true;
    }


    public static boolean delRule(Rule pRule) {
        if (pRule == null) return false;
        int id = pRule.getId();
        return OrmUtils.getLiteOrm().delete(new WhereBuilder(Rule.class).andEquals(Constant.ID_KEY, id)) > 0;
    }

    public static boolean delRule(int id) {
        if (id == -1) return false;
        return OrmUtils.getLiteOrm().delete(new WhereBuilder(Rule.class).andEquals(Constant.ID_KEY, id)) > 0;
    }


    public static boolean saveRule(Rule pRule) {
        if (pRule == null) return false;
        String url = pRule.getUrl();
        String regex = pRule.getRule();
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(regex)) return false;
        if (OrmUtils.getLiteOrm().queryCount(new QueryBuilder(Rule.class).whereEquals(Constant.URL_KEY, url).whereAppendAnd().whereEquals(Constant.RULE_KEY, regex)) > 0) {
            return false;
        }
        return OrmUtils.getLiteOrm().save(pRule) > 0;
    }


    public static ArrayList<String> getUrlHistory() {
        ArrayList<String> retuLists = new ArrayList<>();
        ArrayList<UrlBean> tempBeens = OrmUtils.getLiteOrm().query(UrlBean.class);//new QueryBuilder<>(UrlBean.class).appendColumns(new String[]{Constant.LAST_TIME_KEY}).appendOrderDescBy(Constant.LAST_TIME_KEY));
        if (tempBeens != null && !tempBeens.isEmpty()) {
            for (UrlBean tempBean : tempBeens) {
                retuLists.add(tempBean.getUrl());
            }
        }
        return retuLists;
    }

    /***
     * 获取指定网址的规则列表
     * @param pUrl 网址 这个网址一定是这种www.baidu.com，前面没有Http之类的
     * @return 规则列表
     */
    public static List<Rule> getRules(String pUrl) {
        if (TextUtils.isEmpty(pUrl)) return Collections.emptyList();
        return OrmUtils.getLiteOrm().query(new QueryBuilder<>(Rule.class).whereEquals(Constant.URL_KEY, pUrl));
    }


    public static List<Rule> getAllRules() {
        return OrmUtils.getLiteOrm().query(new QueryBuilder<>(Rule.class));
    }


}
