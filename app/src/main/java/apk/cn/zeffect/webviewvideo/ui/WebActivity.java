package apk.cn.zeffect.webviewvideo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.orm.OrmHelp;
import apk.cn.zeffect.webviewvideo.utils.Constant;
import apk.cn.zeffect.webviewvideo.utils.MoreUtils;
import apk.cn.zeffect.webviewvideo.utils.VideoUrlResolve;
import apk.cn.zeffect.webviewvideo.utils.WeakAsyncTask;
import apk.cn.zeffect.webviewvideo.utils.WeakHandler;
import cn.refactor.kmpautotextview.KMPAutoComplTextView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

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

public class WebActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, KMPAutoComplTextView.OnPopupItemClickListener {
    //    private RelativeLayout mLayout;
    private WebView mWebView;
    private ImageButton mSearchBtn;
    private KMPAutoComplTextView mInputTT;
    private MaterialProgressBar mProgressBar;
    private ImageButton mMoreBtn;
    private Context mContext;
    private NestedScrollView mWebScroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mContext = this;
        initView();

    }


    private void initView() {
        mWebScroll = (NestedScrollView) findViewById(R.id.web_scroll);
        mMoreBtn = (ImageButton) findViewById(R.id.more_action);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.progress_bar);
        mInputTT = (KMPAutoComplTextView) findViewById(R.id.input_url);
        //读取本地所有保存过的地址
        new WeakAsyncTask<Void, Void, ArrayList<String>, WebActivity>(this) {
            @Override
            protected ArrayList<String> doInBackground(WebActivity pActivity, Void... params) {
                return OrmHelp.getHistory();
            }

            @Override
            protected void onPostExecute(WebActivity pActivity, ArrayList<String> pVoid) {
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WebActivity.this, android.R.layout.simple_list_item_1, pVoid);
                if (pVoid == null) pVoid = new ArrayList<String>();
                mInputTT.setDatas(pVoid);
            }
        }.execute();
        mSearchBtn = (ImageButton) findViewById(R.id.load_url);
        mSearchBtn.setOnClickListener(this);
//        mLayout = (RelativeLayout) findViewById(R.id.web_layout);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(mWeakHandler), "java_obj");
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 自适应屏幕
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        // 不支持缩放(如果要支持缩放，html页面本身也要支持缩放：不能加user-scalable=no)
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setDisplayZoomControls(false);
        // 隐藏scrollbar
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mMoreBtn.setOnClickListener(this);
        //
        mInputTT.setOnEditorActionListener(this);
        mInputTT.setOnPopupItemClickListener(this);
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

    private void showChoseDialog(final WebActivity webActivity, final ArrayList<String> pResult) {
        if (pResult == null || pResult.size() < 1) return;
        new MaterialDialog.Builder(webActivity)
                .title("视频地址")
                .items(pResult)
                .cancelable(false)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == -1) return true;
//                        gotoPlay(webActivity, text.toString());
                        playVideo(text.toString());
                        if (pResult.size() == 1) dialog.dismiss();
                        return false;
                    }
                })
//                .neutralText("下载")
                .negativeText("取消")
                .canceledOnTouchOutside(false)
                .show();
    }

//    private void gotoPlay(WebActivity webActivity, String url) {
//        if (TextUtils.isEmpty(url)) return;
//        startActivity(new Intent(webActivity, PlayActivity.class).putExtra(Constant.URL_KEY, url));
//    }


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mWebScroll.smoothScrollTo(0, 0);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                    "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            mInputTT.setText(url);
            super.onPageFinished(view, url);
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
//            mProgressBar.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
            mProgressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_url:
                search("");
                break;
            case R.id.more_action:
                showPopView();
                break;
            case R.id.clear_cookie:
                MoreUtils.clearCookies(mContext);
                disPopView();
                break;
            case R.id.save_url:
                String url2 = mInputTT.getText().toString().trim();
                MoreUtils.saveUrl(url2);
                disPopView();
                break;
            case R.id.refresh_url:
                mWebView.reload();
                disPopView();
                break;
        }
    }

    PopupWindow mPopWindow;

    private void showPopView() {
        if (mPopWindow == null) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_more_layout, null);
            mPopWindow = new PopupWindow(mContext);
            mPopWindow.setContentView(contentView);
            mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopWindow.setOutsideTouchable(false);
            mPopWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopWindow.setTouchable(true);
            mPopWindow.setFocusable(true);
            backgroundAlpha(mContext, 0.5f);
            mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(mContext, 1f);
                }
            });
            //事件监听
            contentView.findViewById(R.id.clear_cookie).setOnClickListener(this);
            contentView.findViewById(R.id.save_url).setOnClickListener(this);
            contentView.findViewById(R.id.refresh_url).setOnClickListener(this);
            //
        }
        if (!mPopWindow.isShowing()) mPopWindow.showAsDropDown(mMoreBtn);
    }

    private void disPopView() {
        if (mPopWindow != null && mPopWindow.isShowing()) mPopWindow.dismiss();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param pContext 上下文
     * @param bgAlpha  透明度 popwindow.show的时候backgroundAlpha(0.5f);popwindow.setOnDismissListener的时候backgroundAlpha(1f);
     * @return true修改成功false修改失败
     */
    public static boolean backgroundAlpha(Context pContext, float bgAlpha) {
        if (pContext instanceof Activity) {
            WindowManager.LayoutParams lp = ((Activity) pContext).getWindow().getAttributes();
            lp.alpha = bgAlpha; //0.0-1.0
            ((Activity) pContext).getWindow().setAttributes(lp);
            return true;
        } else {
            return false;
        }
    }


    public static final int HTML_WHAT = 0x64;

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.input_url) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search("");
                return true;
            }
        }
        return false;
    }

    private void search(String url) {
        if (TextUtils.isEmpty(url))
            url = mInputTT.getText().toString().trim();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        mWebView.loadUrl(url);
        MoreUtils.closeKeyBroad(this);
    }

    @Override
    public void onPopupItemClick(CharSequence charSequence) {
        search(charSequence.toString());
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
            Message tempMessage = new Message();
            tempMessage.what = HTML_WHAT;
            tempMessage.obj = html;
            mHandler.sendMessage(tempMessage);
        }
    }

    private JCVideoPlayerStandard mVideoPlay;

    private void playVideo(String url) {
        if (mVideoPlay == null) {
            mVideoPlay = new JCVideoPlayerStandard(this);// (JCVideoPlayerStandard) findViewById(R.id.video_play);
        }
        if (TextUtils.isEmpty(url)) return;
        if (!MoreUtils.isUrl(url)) return;
        JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class, url, "");//直接全屏
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (JCVideoPlayer.backPress()) {
            return true;
        }
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
