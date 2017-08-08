package apk.cn.zeffect.webviewvideo.ui.addregextask;

/**
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/08/08
 *      desc    ：
 *      version:：1.0
 * </pre>
 *
 * @author zzx
 */

public class AddBean {
    /***
     * 1为规则2为拼接字符串
     */
    public static final int TYPE_1 = 1, TYPE_2 = 2;
    private int type;
    private String showText;

    public int getType() {
        return type;
    }

    public AddBean setType(int pType) {
        type = pType;
        return this;
    }

    public String getShowText() {
        return showText;
    }

    public AddBean setShowText(String pShowText) {
        showText = pShowText;
        return this;
    }
}
