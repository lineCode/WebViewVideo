package apk.cn.zeffect.webviewvideo.ui.regextask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import apk.cn.zeffect.webviewvideo.R;

/**
 * Created by Administrator on 2017/8/5.
 */

public class RegexActivity extends FragmentActivity {
    private RegexFragment regexFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regex);
        if (regexFragment == null) regexFragment = new RegexFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ar_root_layout, regexFragment).show(regexFragment).commit();
    }
}
