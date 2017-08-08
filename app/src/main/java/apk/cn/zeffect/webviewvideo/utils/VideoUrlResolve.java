package apk.cn.zeffect.webviewvideo.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apk.cn.zeffect.webviewvideo.bean.Rule;
import apk.cn.zeffect.webviewvideo.orm.OrmHelp;

/**
 * 解析网页中的视频地址
 * Created by Administrator on 2017/8/4.
 */

public class VideoUrlResolve {

    /***
     * 解析指定网址内容
     * @param url 网址
     * @param html html内容
     * @return 视频链接
     */
    public static List<String> anyURL(String url, String html) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(html)) return Collections.emptyList();
        List<String> tempUrl = regexUrl(url);
        if (tempUrl == null || tempUrl.isEmpty()) return Collections.emptyList();
        List<String> urls = regexUrl(url);
        if (urls == null || urls.isEmpty()) return Collections.emptyList();
        List<Rule> rules = OrmHelp.getRules(urls.get(0));
        if (rules == null || rules.isEmpty()) return Collections.emptyList();
        List<String> joinUrls = new ArrayList<>();
        try {
            for (int i = 0; i < rules.size(); i++) {
                String regexs = rules.get(i).getRule();
                String joinString = rules.get(i).getJoin();
                JSONArray regexArray = new JSONArray(regexs);
                for (int j = 0; j < regexArray.length(); j++) {
                    String regex = (String) regexArray.get(j);
                    if (joinString.contains(regex)) {
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(html);
                        while (matcher.find()) {
                            String tempJoinUrl = joinString.replace(regex, matcher.group(1));
                            joinUrls.add(tempJoinUrl);
                        }
                        joinString = joinString.replace(regex, "");
                    }
                }
            }
        } catch (JSONException pE) {
            pE.printStackTrace();
        } finally {
            return joinUrls;
        }
    }


    /***
     * 匹配出Url中的地址网址：https://www.baidu.com/index.php?tn=98012088_3_dg要匹配出www.baidu.com
     * 匹配http和https
     * @param pUrl 网址
     * @return
     */
    public static List<String> regexUrl(String pUrl) {
        if (TextUtils.isEmpty(pUrl)) return Collections.emptyList();
        if (!pUrl.endsWith("/")) pUrl += "/";//防止匹配不到，先在后面加一个/
        if (pUrl.startsWith("http://")) {
            return regex(pUrl, REGEX_HTTP_URL);
        } else if (pUrl.startsWith("https://")) {
            return regex(pUrl, REGEX_HTTPS_URL);
        } else return Collections.emptyList();

    }

    public static final String REGEX_HTTP_URL = "http://(.+?)/";

    public static final String REGEX_HTTPS_URL = "https://(.+?)/";

    /***
     * 正则匹配
     * @param content 内容
     * @param regex 匹配规则
     * @return 匹配成功的列表
     */
    public static ArrayList<String> regex(String content, String regex) {
        ArrayList<String> tempList = new ArrayList<>();
        if (TextUtils.isEmpty(content)) return tempList;
        if (regex == null) regex = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            tempList.add(matcher.group(1));
        }
        return tempList;
    }


}
