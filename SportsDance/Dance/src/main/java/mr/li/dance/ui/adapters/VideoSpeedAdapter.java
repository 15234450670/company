package mr.li.dance.ui.adapters;

import android.content.Context;
import android.view.View;

import mr.li.dance.R;
import mr.li.dance.models.HomeTypeBtn;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-视频页面 使用
 * 修订历史:
 */

public class VideoSpeedAdapter extends BaseRecyclerAdapter<HomeTypeBtn> {
    boolean isZixun;
    public VideoSpeedAdapter(Context ctx) {
        super(ctx);
    }
    public VideoSpeedAdapter(Context ctx,boolean isZixun) {
        super(ctx);
        this.isZixun = isZixun;
    }
    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_video_type3;
    }


    @Override
    public void bindData(RecyclerViewHolder holder, int position, HomeTypeBtn item) {
        holder.setText(R.id.content_tv,item.getName());
        if(isZixun){
            holder.setVisibility(R.id.sanjiao_icon, View.INVISIBLE);
        }
    }

}
