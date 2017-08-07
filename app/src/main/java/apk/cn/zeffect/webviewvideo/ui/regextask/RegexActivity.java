package apk.cn.zeffect.webviewvideo.ui.regextask;

import android.os.Bundle;
import android.support.annotation.Nullable;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.base.BaseActivity;

/**
 * Created by Administrator on 2017/8/5.
 */

public class RegexActivity extends BaseActivity {
    private RegexFragment regexFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regex);
        if (regexFragment == null) regexFragment = new RegexFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ar_root_layout, regexFragment).show(regexFragment).commit();
    }
}
