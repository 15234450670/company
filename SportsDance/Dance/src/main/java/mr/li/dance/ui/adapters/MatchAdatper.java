package mr.li.dance.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import mr.li.dance.R;
import mr.li.dance.models.Match;
import mr.li.dance.utils.DateUtils;

/**
 * 作者: Administrator
 * 时间: 2017/6/2.
 * 功能:
 */

public class MatchAdatper extends BaseRecyclerAdapter<Match> {
    private int iconResId = -1;

    public MatchAdatper(Context ctx) {
        super(ctx);
    }

    public MatchAdatper(Context ctx, int iconResId) {
        super(ctx);
        this.iconResId = iconResId;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_match_type5;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, Match item) {
        if (iconResId != -1) {
            holder.setImageResDrawable(R.id.imageView, iconResId);
        } else {
            switch (item.getType()) {
                case 10001:
                    holder.setImageResDrawable(R.id.imageView, R.drawable.wdsf_icon);
                    break;
                case 10002:
                    holder.setImageResDrawable(R.id.imageView, R.drawable.cdsf_icon);
                    break;
                case 10003:
                    holder.setImageResDrawable(R.id.imageView, R.drawable.addmathc_icon);
                    break;
                default:
                    holder.setImageResDrawable(R.id.imageView, R.drawable.wdsf_icon);
                    break;
            }
        }
        holder.setText(R.id.name, item.getTitle());
        holder.setText(R.id.time_tv, item.getStart_date() + "-" + item.getEnd_date());
    }

}
