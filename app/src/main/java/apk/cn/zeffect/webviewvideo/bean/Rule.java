package apk.cn.zeffect.webviewvideo.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.Table;

import apk.cn.zeffect.webviewvideo.utils.Constant;

/**
 * 规则列表
 * Created by Administrator on 2017/8/4.
 */
@Table("rule")
public class Rule {
    private int id;
    @Column(Constant.URL_KEY)
    private String url;
    @Column(Constant.RULE_KEY)
    private String rule;
    /**
     * 用于标记当前条是否处在修改状态
     */
    @Ignore
    private boolean inEdit = false;

    public boolean isInEdit() {
        return inEdit;
    }

    public Rule setInEdit(boolean pInEdit) {
        inEdit = pInEdit;
        return this;
    }

    public int getId() {
        return id;
    }

    public Rule setId(int id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Rule setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getRule() {
        return rule;
    }

    public Rule setRule(String rule) {
        this.rule = rule;
        return this;
    }
}
