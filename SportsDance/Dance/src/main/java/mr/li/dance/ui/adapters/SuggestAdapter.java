package mr.li.dance.ui.adapters;

import android.content.Context;
import android.view.View;

import mr.li.dance.R;
import mr.li.dance.models.SuggestInfo;
import mr.li.dance.utils.UserInfoManager;
import mr.li.dance.utils.glide.ImageLoaderManager;
import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Administrator
 * 时间: 2017/5/25.
 * 功能:
 */

public class SuggestAdapter extends BaseRecyclerAdapter<SuggestInfo> {

    public SuggestAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_suggest;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, SuggestInfo item) {
        holder.setText(R.id.reply, item.getReply());
        holder.setText(R.id.problem, item.getProblem());

        if (!MyStrUtil.isEmpty(item.getReply())) {
            ImageLoaderManager.getSingleton().LoadCircle(mContext, "", holder.getImageView(R.id.headicon), R.drawable.kefu_icon);
            holder.getView(R.id.headicon).setVisibility(View.VISIBLE);
            holder.getView(R.id.left_layout).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.headicon).setVisibility(View.GONE);
            holder.getView(R.id.left_layout).setVisibility(View.GONE);
        }

        if (!MyStrUtil.isEmpty(item.getProblem())) {
            String picUrl = UserInfoManager.getSingleton().getUserInfo(mContext).getPicture();
            holder.getView(R.id.head2icon).setVisibility(View.VISIBLE);
            holder.getView(R.id.right_layout).setVisibility(View.VISIBLE);
            ImageLoaderManager.getSingleton().LoadCircle(mContext, picUrl, holder.getImageView(R.id.head2icon), R.drawable.kefu_icon);
        } else {
            holder.getView(R.id.head2icon).setVisibility(View.GONE);
            holder.getView(R.id.right_layout).setVisibility(View.GONE);
        }
    }

}
