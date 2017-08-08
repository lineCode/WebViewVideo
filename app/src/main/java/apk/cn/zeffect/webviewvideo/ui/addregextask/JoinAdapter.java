package apk.cn.zeffect.webviewvideo.ui.addregextask;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import apk.cn.zeffect.webviewvideo.R;
import apk.cn.zeffect.webviewvideo.ui.addregextask.helper.ItemTouchHelperAdapter;
import apk.cn.zeffect.webviewvideo.ui.addregextask.helper.OnStartDragListener;

/**
 * 用来做那个排序的，添加规则的时候排序
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

public class JoinAdapter extends RecyclerView.Adapter<JoinAdapter.MyHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private List<AddBean> mRegexs;
    private final OnStartDragListener mDragStartListener;

    public JoinAdapter(Context pContext, List<AddBean> pList, OnStartDragListener dragStartListener) {
        this.mRegexs = pList;
        this.mContext = pContext;
        this.mDragStartListener = dragStartListener;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sort_regex, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.showTv.setText(mRegexs.get(position).getShowText());
        holder.sortView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRegexs.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mRegexs, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mRegexs.remove(position);
        notifyItemRemoved(position);
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        private TextView showTv;
        private TextView sortView;

        public MyHolder(View itemView) {
            super(itemView);
            showTv = (TextView) itemView.findViewById(R.id.isr_show_tv);
            sortView = (TextView) itemView.findViewById(R.id.isr_sort_tv);
        }
    }
}
