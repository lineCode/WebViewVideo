package apk.cn.zeffect.webviewvideo.ui.regextask;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.adapter.RegexAdapter;
import apk.cn.zeffect.webviewvideo.bean.Rule;
import apk.cn.zeffect.webviewvideo.orm.OrmHelp;
import apk.cn.zeffect.webviewvideo.orm.OrmUtils;
import apk.cn.zeffect.webviewvideo.utils.Constant;
import apk.cn.zeffect.webviewvideo.utils.MoreUtils;
import apk.cn.zeffect.webviewvideo.utils.VideoUrlResolve;
import apk.cn.zeffect.webviewvideo.utils.WeakAsyncTask;

/**
 * 用于添加正则表达式规则的Fragment
 * Created by Administrator on 2017/8/5.
 */

public class RegexFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private RecyclerView mRegexsView;
    private Context mContext;
    private List<Rule> mRules = new ArrayList<>();
    private RegexAdapter mAdapter;
    /***
     * 正在访问的地址
     */
    private String mCallUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallUrl = getArguments().getString(Constant.URL_KEY, "");
        mContext = getContext();
        mAdapter = new RegexAdapter(mContext, mRules);
        addAllRules();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_regex, container, false);
            initView();
            mAdapter.setView(mView);
        }
        return mView;
    }

    private void initView() {
        mView.findViewById(R.id.title_back).setOnClickListener(this);
        ((TextView) mView.findViewById(R.id.title_text)).setText("匹配规则");
        ImageButton rightBtn = (ImageButton) mView.findViewById(R.id.title_right_action);
        rightBtn.setImageResource(R.drawable.ic_add_selector);
        rightBtn.setOnClickListener(this);
        mRegexsView = (RecyclerView) mView.findViewById(R.id.regex_rcv);
        //
        mRegexsView.setLayoutManager(new LinearLayoutManager(mContext));
        mRegexsView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_back) {
            getActivity().finish();
        } else if (v.getId() == R.id.title_right_action) {
            showInputDialog();
        }
    }

    /***
     * 添加本地所有策略
     */
    private void addAllRules() {
        new WeakAsyncTask<Void, Void, List<Rule>, RegexFragment>(this) {
            @Override
            protected List<Rule> doInBackground(RegexFragment pFragment, Void... params) {
                return OrmHelp.getAllRules();
            }

            @Override
            protected void onPostExecute(RegexFragment pFragment, List<Rule> pRules) {
                mRules.clear();
                mRules.addAll(pRules);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();

    }


    private void showInputDialog() {
        View tempView = LayoutInflater.from(mContext).inflate(R.layout.layout_regex_input, null);
        final EditText inputUrl = (EditText) tempView.findViewById(R.id.lri_input_url);
        if (!TextUtils.isEmpty(mCallUrl) && MoreUtils.isUrl(mCallUrl)) inputUrl.setText(mCallUrl);
        final EditText inputRegex = (EditText) tempView.findViewById(R.id.lri_input_regex);
        new MaterialDialog.Builder(mContext)
                .customView(tempView, false)
                .positiveText("保存")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String tempUrl = inputUrl.getText().toString().trim();
                        if (TextUtils.isEmpty(tempUrl)) {
                            Snackbar.make(mView, "您好像没有输入网址！", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (!MoreUtils.isUrl(tempUrl)) tempUrl = "http://" + tempUrl;
                        List<String> regexUrls = VideoUrlResolve.regexUrl(tempUrl);
                        if (regexUrls != null && !regexUrls.isEmpty()) {
                            String regexUrl = regexUrls.get(0);
                            String rule = inputRegex.getText().toString().trim();
                            if (TextUtils.isEmpty(rule)) {
                                Snackbar.make(mView, "你还没有输入规则", Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                            if (OrmHelp.saveRule(regexUrl, rule)) {
                                Snackbar.make(mView, "保存成功，下次访问网页即可生效！", Snackbar.LENGTH_SHORT).show();
                                addAllRules();
                            }
                        } else {
                            Snackbar.make(mView, "您输入的地址有误", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
