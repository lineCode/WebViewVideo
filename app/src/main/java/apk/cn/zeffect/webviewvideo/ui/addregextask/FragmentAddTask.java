package apk.cn.zeffect.webviewvideo.ui.addregextask;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.bean.Rule;
import apk.cn.zeffect.webviewvideo.orm.OrmHelp;
import apk.cn.zeffect.webviewvideo.orm.OrmUtils;
import apk.cn.zeffect.webviewvideo.ui.addregextask.helper.OnStartDragListener;
import apk.cn.zeffect.webviewvideo.ui.addregextask.helper.SimpleItemTouchHelperCallback;
import apk.cn.zeffect.webviewvideo.utils.Constant;
import apk.cn.zeffect.webviewvideo.utils.VideoUrlResolve;

/**
 * 添加匹配规则的界面，可以同时添加多个，并且用字符串拼接起来
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

public class FragmentAddTask extends Fragment implements View.OnClickListener, OnStartDragListener {
    private View mView;
    private Context mContext;
    private boolean mIsUpdate;
    private Button mAddBtn, mJoinBtn;
    private RecyclerView mRegexsRV;
    private List<AddBean> mRegexs = new ArrayList<>();
    private JoinAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private EditText mInputUrl;
    private String mCallUrl = "";
    private int mUpdateId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
        mIsUpdate = getArguments().getBoolean(Constant.IS_UPDATE_KEY, false);
        mCallUrl = getArguments().getString(Constant.URL_KEY);
        if (mCallUrl == null) mCallUrl = "";
        mUpdateId = getArguments().getInt(Constant.ID_KEY, -1);
        mAdapter = new JoinAdapter(mContext, mRegexs, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_add_regex, container, false);
            init();
        }
        return mView;
    }

    private void init() {
        mView.findViewById(R.id.title_back).setOnClickListener(this);
        ((TextView) mView.findViewById(R.id.title_text)).setText("添加规则");
        ImageButton rightBtn = ((ImageButton) mView.findViewById(R.id.title_right_action));
        rightBtn.setImageResource(R.drawable.ic_save_slt);
        rightBtn.setOnClickListener(this);
        mAddBtn = (Button) mView.findViewById(R.id.add_regex_btn);
        mJoinBtn = (Button) mView.findViewById(R.id.join_regex_btn);
        mInputUrl = (EditText) mView.findViewById(R.id.lri_input_url);
        if (!TextUtils.isEmpty(mCallUrl)) {
            if (mCallUrl.startsWith("http://") || mCallUrl.startsWith("https://")) {
                List<String> tempurl = VideoUrlResolve.regexUrl(mCallUrl);
                if (tempurl != null && !tempurl.isEmpty()) {
                    mCallUrl = tempurl.get(0);
                }
            } else ;
            mInputUrl.setText(mCallUrl);
        }
        mRegexsRV = (RecyclerView) mView.findViewById(R.id.regexs_rv);
        mRegexsRV.setLayoutManager(new LinearLayoutManager(mContext));
        mRegexsRV.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRegexsRV);
        //
        mAddBtn.setOnClickListener(this);
        mJoinBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                getActivity().finish();
                break;
            case R.id.add_regex_btn:
                showAddDialog(AddBean.TYPE_1);
                break;
            case R.id.join_regex_btn:
                showAddDialog(AddBean.TYPE_2);
                break;
            case R.id.title_right_action:
                showHttpUrl(mRegexs);
                break;
        }
    }


    private void showHttpUrl(List<AddBean> pBeen) {
        String inputUrl = mInputUrl.getText().toString().trim();
        if (!inputUrl.startsWith("http://") && !inputUrl.startsWith("https://"))
            inputUrl = "http://" + inputUrl;
        if (TextUtils.isEmpty(inputUrl)) {
            Snackbar.make(mView, "您还没有输入网址", Snackbar.LENGTH_SHORT).show();
            return;
        }
        List<String> regexsUrl = VideoUrlResolve.regexUrl(inputUrl);
        if (regexsUrl == null) {
            Snackbar.make(mView, "您输入的网址不正确！", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (regexsUrl.isEmpty()) {
            Snackbar.make(mView, "您输入的网址不正确！", Snackbar.LENGTH_SHORT).show();
            return;
        }
        final String regexUrl = regexsUrl.get(0);
        final JSONArray regexArray = new JSONArray();
        final StringBuilder urlBuild = new StringBuilder("");
        for (int i = 0; i < pBeen.size(); i++) {
            AddBean tempBean = pBeen.get(i);
            urlBuild.append(tempBean.getShowText());
            if (tempBean.getType() == AddBean.TYPE_1) regexArray.put(tempBean.getShowText());
        }
        new MaterialDialog.Builder(mContext)
                .title("网址")
                .content("最终访问的网址为：" + urlBuild.toString())
                .negativeText("取消")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        Rule tempRule = new Rule();
                        tempRule.setJoin(urlBuild.toString())
                                .setRule(regexArray.toString())
                                .setUrl(regexUrl);
                        if (mUpdateId != -1) OrmHelp.delRule(mUpdateId);
                        if (OrmHelp.saveRule(tempRule)) {
                            Snackbar.make(mView, "保存成功", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }


    private void showAddDialog(final int type) {
        new MaterialDialog.Builder(mContext)
                .input(type == 1 ? "请输入规则" : "请输入拼接字符", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        dialog.dismiss();
                        if (TextUtils.isEmpty(input)) return;
                        mRegexs.add(new AddBean().setShowText(input.toString()).setType(type));
                        mAdapter.notifyItemInserted(mRegexs.size() - 1);
                    }
                }).show();
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
