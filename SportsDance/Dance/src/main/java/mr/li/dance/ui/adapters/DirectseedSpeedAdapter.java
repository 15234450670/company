package mr.li.dance.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import mr.li.dance.R;
import mr.li.dance.models.QuickZhiboInfo;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 直播或者视频控件下边的类似进度的适配器
 * 修订历史:
 */

public class DirectseedSpeedAdapter extends BaseRecyclerAdapter<QuickZhiboInfo> {
    public DirectseedSpeedAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_directseedspeed;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, QuickZhiboInfo item) {
        holder.setImageByUrlOrFilePath(R.id.picture_tv, item.getPicture(), R.drawable.default_video);
    }
}
