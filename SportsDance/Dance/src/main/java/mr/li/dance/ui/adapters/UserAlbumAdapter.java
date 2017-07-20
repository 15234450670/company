package mr.li.dance.ui.adapters;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.response.HomeAlbumResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.utils.glide.ImageLoaderManager;
import mr.li.dance.utils.MyStrUtil;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-图片页面适配器
 * 修订历史:
 */
public class UserAlbumAdapter extends BaseRecyclerAdapter<AlbumInfo> {
    private final int type1 = 0x001;
    private final int maintype = 0x002;
    Context mContext;
    private boolean isAttentioned;
    private boolean isNotSelfAlbum;
    private String mUserName = "";
    private String userIcon = "";

    /**
     * 构造器
     *
     * @param
     */
    public UserAlbumAdapter(Context context, boolean isNotSelfAlbum) {
        super(context);
        this.isNotSelfAlbum = isNotSelfAlbum;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return type1;
        } else {
            return maintype;
        }
    }

    @Override
    public int getItemCount() {
        if (MyStrUtil.isEmpty(mData) && MyStrUtil.isEmpty(mUserName)) {
            return 0;
        }
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == type1) {
            return R.layout.item_userhead_layout;
        } else {
            return R.layout.item_base;
        }

    }

    @Override
    public void bindData(RecyclerViewHolder holder, final int position, final AlbumInfo item) {
        if (position == 0) {
            ImageLoaderManager.getSingleton().LoadCircle(mContext, userIcon, holder.getImageView(R.id.headicon), R.drawable.icon_mydefault);
            ImageLoaderManager.getSingleton().LoadMoHu(mContext, userIcon, holder.getImageView(R.id.background_iv), R.drawable.icon_mydefault);
            holder.setClickListener(R.id.guanzhu_layout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.itemClick(position, item);
                }
            });
            holder.setText(R.id.username_tv, mUserName);
            if (isNotSelfAlbum) {
                if (isAttentioned) {
                    holder.setText(R.id.guanzhu_tv, "已关注");
                    holder.getImageView(R.id.guanzhu_icon).setImageResource(R.drawable.collect_icon_001);
                } else {
                    holder.setText(R.id.guanzhu_tv, "关注");
                    holder.getImageView(R.id.guanzhu_icon).setImageResource(R.drawable.aixin_icon);
                }
            } else {
                holder.setVisibility(R.id.guanzhu_layout, View.GONE);
            }

        } else {
            holder.getView(R.id.picnum_tv).setVisibility(View.VISIBLE);
            AlbumInfo albumInfo = mData.get(position - 1);
            holder.setText(R.id.num_tv, "共" + albumInfo.getPhotos() + "张");
            holder.setText(R.id.name, albumInfo.getCompete_name());
            holder.setText(R.id.time_tv, albumInfo.getStart_time());
            holder.setImageByUrlOrFilePath(R.id.imageView, albumInfo.getImg_fm(), R.drawable.default_video);
        }

    }

    public void refresh(HomeAlbumResponse response) {
        mData.clear();
        ArrayList<AlbumInfo> albumInfos = response.getData();
        if (!MyStrUtil.isEmpty(albumInfos)) {
            mData.addAll(albumInfos);
        }
        notifyDataSetChanged();
    }

    public boolean isAttentioned() {
        return isAttentioned;
    }

    public void setAttentioned(boolean attentioned) {
        isAttentioned = attentioned;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName, String userIcon) {
        this.mUserName = mUserName;
        this.userIcon = userIcon;
    }

    @Override
    public AlbumInfo getmItem(int position) {
        if (0 == position) {
            return null;
        } else {
            return mData.get(position - 1);
        }

    }

    public void addList(boolean isRefresh, List<AlbumInfo> list) {
        if (isRefresh) {
            currentPage = 1;
            mData.clear();
        } else {
            if (!MyStrUtil.isEmpty(list)) {
                currentPage++;
            }
        }
        if (!MyStrUtil.isEmpty(list)) {
            this.mData.addAll(list);
        }
        notifyDataSetChanged();
    }
}