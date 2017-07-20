package mr.li.dance.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.response.HomeAlbumResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.HomeListItemInfo;
import mr.li.dance.ui.adapters.viewholder.BaseViewHolder;
import mr.li.dance.ui.widget.SlideShowView;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.ShareUtils;
import mr.li.dance.utils.glide.ImageLoaderManager;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-图片页面适配器
 * 修订历史:
 */
public class PicPageAdapter extends BaseRecyclerAdapter<AlbumInfo> {

    Context mContext;

    /**
     * 构造器
     *
     * @param
     */
    public PicPageAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_album;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, final int position, final AlbumInfo item) {
        final AlbumInfo albumInfo = mData.get(position);
        holder.getView(R.id.picnum_tv).setVisibility(View.VISIBLE);
        holder.setText(R.id.num_tv, "共" + albumInfo.getPhotos() + "张");
        holder.setText(R.id.name, albumInfo.getCompete_name());
        holder.setText(R.id.time_tv, albumInfo.getStart_time());
        holder.setRoundImageByUrlOrFilePath(R.id.imageView, albumInfo.getImg_fm(), R.drawable.default_video);
        holder.setText(R.id.username_tv, albumInfo.getUsername());
        ImageLoaderManager.getSingleton().LoadCircle(mContext, albumInfo.getPicture_src(), (ImageView) holder.getView(R.id.headicon), R.drawable.icon_mydefault);
        holder.setClickListener(R.id.share_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils shareUtils = new ShareUtils((Activity) mContext);
                String shareUrl = String.format(AppConfigs.SHAREPHOTOURL, albumInfo.getId());
                String mShareContent = albumInfo.getCompete_name();
                String countID = AppConfigs.CLICK_EVENT_19;
                shareUtils.showShareDilaog(countID,shareUrl, mShareContent);
            }
        });
    }

}