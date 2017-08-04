package apk.cn.zeffect.webviewvideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import apk.cn.zeffect.webviewvideo.ui.PlayActivity;
import apk.cn.zeffect.webviewvideo.ui.WebActivity;
import apk.cn.zeffect.webviewvideo.utils.Constant;
import apk.cn.zeffect.webviewvideo.utils.VideoUrlResolve;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        VideoUrlResolve.anyURL("","\tvideo_url: 'http://www.1717she.fun/get_file/1/9d4533098092c6a98af343da30537240/5000/5084/5084.mp4/', \t\t\tvideo_url: 'http://www.1717she.fun/get_file/1/9d4533098092c6a98af343da30537240/5000/5084/501.mp4/', \t");
        startActivity(new Intent(this, WebActivity.class));
//        startActivity(new Intent(this,PlayActivity.class).putExtra(Constant.URL_KEY, "http://www.1717she.fun/get_file/1/9d4533098092c6a98af343da30537240/5000/5084/5084.mp4/"));
    }
}
