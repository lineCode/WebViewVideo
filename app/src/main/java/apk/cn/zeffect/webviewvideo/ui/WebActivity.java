package apk.cn.zeffect.webviewvideo.ui;

import android.content.Intent;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.orm.OrmHelp;
import apk.cn.zeffect.webviewvideo.utils.Constant;
import apk.cn.zeffect.webviewvideo.utils.VideoUrlResolve;
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
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

    }

    private WeakHandler mWeakHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == HTML_WHAT) {
                if (msg.obj == null) return true;
                String html = msg.obj.toString();
                new WeakAsyncTask<String, Void, ArrayList<String>, WebActivity>(WebActivity.this) {
                    @Override
                    protected ArrayList<String> doInBackground(WebActivity pTarget, String... params) {
                        return VideoUrlResolve.anyURL("", params[0]);
                    }

                    @Override
                    protected void onPostExecute(WebActivity pTarget, ArrayList<String> pResult) {
                        showChoseDialog(pTarget, pResult);
                    }
                }.execute(html);
            }
            return false;
        }
    });

    private void showChoseDialog(final WebActivity webActivity, ArrayList<String> pResult) {
        if (pResult == null || pResult.size() < 1) return;
        new MaterialDialog.Builder(webActivity)
                .title("视频连接")
                .items(pResult)
                .cancelable(false)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == -1) return true;
                        gotoPlay(webActivity, text.toString());
                        return false;
                    }
                })
                .positiveText("播放")
                .negativeText("取消")
                .canceledOnTouchOutside(false)
                .show();
    }

    private void gotoPlay(WebActivity webActivity, String url) {
        if (TextUtils.isEmpty(url)) return;
        startActivity(new Intent(webActivity, PlayActivity.class).putExtra(Constant.URL_KEY, url));
    }


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
                if (!url.startsWith("http")) return;
                OrmHelp.save(url);
                mInputTT.setText("");
                mWebView.loadUrl(url);
                break;
        }
    }


    public static final int HTML_WHAT = 0x64;

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
            Message tempMessage = new Message();
            tempMessage.what = HTML_WHAT;
            tempMessage.obj = html;
            mHandler.sendMessage(tempMessage);
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
