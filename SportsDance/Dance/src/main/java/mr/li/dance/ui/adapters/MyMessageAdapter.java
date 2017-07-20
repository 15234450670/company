package mr.li.dance.ui.adapters;

import android.content.Context;

import mr.li.dance.R;
import mr.li.dance.models.MyMessageInfo;

/**
 * 作者: Administrator
 * 时间: 2017/5/23.
 * 功能:
 */

public class MyMessageAdapter extends BaseRecyclerAdapter<MyMessageInfo> {
    public MyMessageAdapter(Context ctx) {
        super(ctx);
    }



    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_message;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, MyMessageInfo item) {
        holder.setText(R.id.title_tv,item.getTitle());
        holder.setText(R.id.content_tv,item.getCreate_time());
    }
}
