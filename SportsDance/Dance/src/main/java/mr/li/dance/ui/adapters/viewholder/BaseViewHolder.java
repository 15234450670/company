package mr.li.dance.ui.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import mr.li.dance.utils.DanceViewHolder;

/**
 * 作者: Administrator
 * 时间: 2017/5/23.
 * 功能:
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public DanceViewHolder danceViewHolder;
    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        danceViewHolder = new DanceViewHolder(context,itemView);
    }
}
