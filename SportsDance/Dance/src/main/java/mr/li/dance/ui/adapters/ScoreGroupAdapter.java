package mr.li.dance.ui.adapters;

import android.content.Context;

import mr.li.dance.R;
import mr.li.dance.models.ScoreGroupInfo;

/**
 * 作者: Administrator
 * 时间: 2017/6/2.
 * 功能:
 */

public class ScoreGroupAdapter extends BaseRecyclerAdapter<ScoreGroupInfo> {
    public ScoreGroupAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_scoregroup;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, ScoreGroupInfo item) {
        holder.setText(R.id.groupname_tv, item.getGroup_name());
        holder.setText(R.id.groupnum_tv, item.getNum());
    }

}
