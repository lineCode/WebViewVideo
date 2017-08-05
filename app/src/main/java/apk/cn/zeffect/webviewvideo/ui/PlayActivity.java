package apk.cn.zeffect.webviewvideo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.utils.Constant;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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

public class PlayActivity extends AppCompatActivity {
    private JCVideoPlayerStandard mVideoPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_play);
        initView();
    }


    private void initView() {
        mVideoPlay = new JCVideoPlayerStandard(this);// (JCVideoPlayerStandard) findViewById(R.id.video_play);
        String url = getIntent().getStringExtra(Constant.URL_KEY);
        if (TextUtils.isEmpty(url)) return;
//        mVideoPlay.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class, url, "");//直接全屏
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
