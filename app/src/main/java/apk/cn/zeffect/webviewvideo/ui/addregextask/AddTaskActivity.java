package apk.cn.zeffect.webviewvideo.ui.addregextask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.base.BaseActivity;
import apk.cn.zeffect.webviewvideo.utils.Constant;

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

public class AddTaskActivity extends BaseActivity {
    private Fragment mAddTaskFm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_regex);
        if (mAddTaskFm == null) mAddTaskFm = new FragmentAddTask();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_UPDATE_KEY, getIntent().getBooleanExtra(Constant.IS_UPDATE_KEY, false));
        mAddTaskFm.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.far_root_layout, mAddTaskFm).show(mAddTaskFm).commit();
    }
}
