package apk.cn.zeffect.webviewvideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.orm.OrmHelp;
import apk.cn.zeffect.webviewvideo.utils.WeakAsyncTask;
import apk.cn.zeffect.webviewvideo.utils.WeakHandler;

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

public class WebActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mLayout;
    private WebView mWebView;
    private Button mSearchBtn;
    private AutoCompleteTextView mInputTT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();

    }


    private void initView() {
        mInputTT = (AutoCompleteTextView) findViewById(R.id.input_url);
        //读取本地所有保存过的地址
        new WeakAsyncTask<Void, Void, ArrayList<String>, WebActivity>(this) {
            @Override
            protected ArrayList<String> doInBackground(WebActivity pActivity, Void... params) {
                return OrmHelp.getHistory();
            }

            @Override
            protected void onPostExecute(WebActivity pActivity, ArrayList<String> pVoid) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WebActivity.this, android.R.layout.simple_list_item_1, pVoid);
                mInputTT.setAdapter(adapter);
            }
        }.execute();
        mSearchBtn = (Button) findViewById(R.id.load_url);
        mSearchBtn.setOnClickListener(this);
        mLayout = (RelativeLayout) findViewById(R.id.web_layout);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(mWeakHandler), "java_obj");
        mWebView.setWebViewClient(mWebViewClient);
    }

    private WeakHandler mWeakHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                    "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            super.onPageFinished(view, url);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_url:
                String url = mInputTT.getText().toString().trim();
                if (TextUtils.isEmpty(url)) return;
                OrmHelp.save(url);
                mWebView.loadUrl(url);
                break;
        }
    }

    /**
     * 逻辑处理
     *
     * @author linzewu
     */
    final class InJavaScriptLocalObj {
        private WeakHandler mHandler;

        public InJavaScriptLocalObj(WeakHandler pHandler) {
            this.mHandler = pHandler;
        }

        @JavascriptInterface
        public void getSource(String html) {
            mHandler.sendEmptyMessage(100);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
