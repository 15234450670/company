package mr.li.dance.ui.adapters;

import android.content.Context;
import android.util.Log;

import mr.li.dance.R;

/**
 * 作者: Administrator
 * 时间: 2017/5/23.
 * 功能:
 */

public class MineAdapter extends BaseRecyclerAdapter {
    int[] iconResIds = new int[]{R.drawable.mine_icon_006, R.drawable.mine_icon_005, R.drawable.mine_icon_003,
            R.drawable.mine_icon_008, R.drawable.mine_icon_002, R.drawable.mine_icon_001, R.drawable.mine_icon_007};
    int[] nameStr = new int[]{R.string.collect_video, R.string.collect_album, R.string.mine_guanzhu,
            R.string.mine_album, R.string.mine_suggest, R.string.mine_about, R.string.mine_setting};


    public MineAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public int getItemCount() {
        return iconResIds.length;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_mine;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, Object item) {
        holder.setText(R.id.name, mContext.getResources().getString(nameStr[position]));
        holder.setImageResDrawable(R.id.icon, iconResIds[position]);
    }
}
