package apk.cn.zeffect.webviewvideo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.bean.Rule;

/**
 * Created by Administrator on 2017/8/5.
 */

public class RegexAdapter extends RecyclerView.Adapter<RegexAdapter.MyHolder> {
    private Context mContext;
    private ArrayList<Rule> mRules;

    public RegexAdapter(Context pContext, ArrayList<Rule> pList) {
        this.mContext = pContext;
        this.mRules = pList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_regex, null));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mRules.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
