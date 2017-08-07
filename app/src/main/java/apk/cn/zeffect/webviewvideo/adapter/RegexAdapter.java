package apk.cn.zeffect.webviewvideo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.List;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.bean.Rule;
import apk.cn.zeffect.webviewvideo.orm.OrmHelp;
import apk.cn.zeffect.webviewvideo.orm.OrmUtils;
import apk.cn.zeffect.webviewvideo.utils.Constant;

/**
 * Created by Administrator on 2017/8/5.
 */

public class RegexAdapter extends RecyclerView.Adapter<RegexAdapter.MyHolder> {
    private Context mContext;
    private List<Rule> mRules;
    private View mView;


    public RegexAdapter(Context pContext, List<Rule> pList) {
        this.mContext = pContext;
        this.mRules = pList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_regex, null));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Rule tempRule = mRules.get(position);
        holder.url.setText("网址：" + tempRule.getUrl());
        holder.regex.setText(tempRule.getRule());
        holder.regex.setEnabled(tempRule.isInEdit() ? true : false);
        holder.regex.setBackgroundResource(tempRule.isInEdit() ? R.drawable.shape_fouced_bg : R.drawable.shape_round_bg);
        holder.regex.setTextColor(tempRule.isInEdit() ? ContextCompat.getColor(mContext, R.color.regex_edit_color) : ContextCompat.getColor(mContext, R.color.regex_content_color));
        holder.changeBtn.setOnClickListener(new ViewOnClick(this, holder, tempRule, position));
        holder.saveBtn.setOnClickListener(new ViewOnClick(this, holder, tempRule, position));
        holder.delBtn.setOnClickListener(new ViewOnClick(this, holder, tempRule, position));
    }

    @Override
    public int getItemCount() {
        return mRules.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        private TextView url;
        private EditText regex;
        private Button changeBtn, saveBtn, delBtn;

        public MyHolder(View itemView) {
            super(itemView);
            url = (TextView) itemView.findViewById(R.id.ir_url_tv);
            regex = (EditText) itemView.findViewById(R.id.ir_regex_et);
            changeBtn = (Button) itemView.findViewById(R.id.ir_change_btn);
            saveBtn = (Button) itemView.findViewById(R.id.ir_save_btn);
            delBtn = (Button) itemView.findViewById(R.id.ir_del_btn);
        }
    }


    class ViewOnClick implements View.OnClickListener {
        private Rule vRule;
        private RegexAdapter vAdapter;
        private int vPosition;
        private MyHolder vHolder;

        public ViewOnClick(RegexAdapter pAdapter, MyHolder pHolder, Rule pRule, int position) {
            this.vAdapter = pAdapter;
            this.vRule = pRule;
            this.vPosition = position;
            this.vHolder = pHolder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ir_change_btn:
                    if (vRule.isInEdit()) {
                        return;
                    }
                    vRule.setInEdit(true);
                    vAdapter.notifyItemChanged(vPosition);
                    Snackbar.make(mView, "正处于编辑模式，可直接对匹配规则进行修改，修改后请点击保存按钮。", Snackbar.LENGTH_SHORT).show();
                    break;
                case R.id.ir_save_btn:
                    if (!vRule.isInEdit()) {
                        return;
                    }
                    vRule.setInEdit(false);
                    vRule.setRule(vHolder.regex.getText().toString().trim());
                    OrmHelp.delRule(vRule);//删除旧的，保存新的
                    OrmHelp.saveRule(vRule);
                    vAdapter.notifyItemChanged(vPosition);
                    Snackbar.make(mView, "保存成功", Snackbar.LENGTH_SHORT).show();
                    break;
                case R.id.ir_del_btn:
                    new MaterialDialog.Builder(vAdapter.mContext)
                            .title("删除确认")
                            .content("你确定要删除这条匹配规则吗？")
                            .positiveText("删除")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    if (OrmHelp.delRule(vRule)) {
                                        Snackbar.make(mView, "删除成功", Snackbar.LENGTH_SHORT).show();
                                        vAdapter.mRules.remove(vPosition);
                                        vAdapter.notifyDataSetChanged();
                                    }
                                }
                            })
                            .negativeText("取消")
                            .show();
                    break;
            }
        }
    }


    public View getView() {
        return mView;
    }

    public RegexAdapter setView(View pView) {
        mView = pView;
        return this;
    }
}
