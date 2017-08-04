package apk.cn.zeffect.webviewvideo.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析网页中的视频地址
 * Created by Administrator on 2017/8/4.
 */

public class VideoUrlResolve {

    public static ArrayList<String> anyURL(String url, String html) {
        ArrayList<String> tempList = new ArrayList<>();
        if (TextUtils.isEmpty(html)) return tempList;
        String regex = "video_url: '(.+?)/'";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            tempList.add(matcher.group(1));
        }
        return tempList;
    }
}
