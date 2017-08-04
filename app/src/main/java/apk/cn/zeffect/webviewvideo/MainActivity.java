package apk.cn.zeffect.webviewvideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import apk.cn.zeffect.webviewvideo.ui.PlayActivity;
import apk.cn.zeffect.webviewvideo.ui.WebActivity;
import apk.cn.zeffect.webviewvideo.utils.Constant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, WebActivity.class));//.putExtra(Constant.URL_KEY, "http://cdn.qimonjy.cn//netstudy/article/0a0e4a6aaea24a2199c87f35b7db2a64/%E6%95%B0%E5%AD%A6%E5%B0%8F%E5%AD%A65%E4%B8%8A__%E7%AC%AC1%E8%AF%BE%E7%AC%AC1%E8%8A%82%C2%B7%E7%B2%BE%E6%89%93%E7%BB%86%E7%AE%97%E3%80%81%E6%89%93%E6%89%AB%E5%8D%AB%E7%94%9F_%E6%A0%87%E6%B8%85_201706191437211683215.mp4"));
    }
}
