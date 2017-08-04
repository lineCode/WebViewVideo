package apk.cn.zeffect.webviewvideo.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

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
@Table("url_bean")
public class UrlBean {
    private int id;
    @Column(Constant.URL_KEY)
    private String url;
    @Column(Constant.LAST_TIME_KEY)
    private long lastTime;

    public int getId() {
        return id;
    }

    public UrlBean setId(int pId) {
        id = pId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UrlBean setUrl(String pUrl) {
        url = pUrl;
        return this;
    }

    public long getLastTime() {
        return lastTime;
    }

    public UrlBean setLastTime(long pLastTime) {
        lastTime = pLastTime;
        return this;
    }
}
