package mr.li.dance.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.BaseHomeItem;
import mr.li.dance.models.Video;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.glide.ImageLoaderManager;

/**
 * Created by Lixuewei on 2017/5/30.
 */

public class CollectAdapter extends SwipeMenuAdapter<RecyclerViewHolder> {
    Context mContext;
    private ArrayList<BaseHomeItem> mDatas;
    private boolean isAlbum;
    ListViewItemClickListener<BaseHomeItem> mItemClickListener;

    public CollectAdapter(Context context, boolean isAlbum, ListViewItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
        mDatas = new ArrayList<>();
        this.isAlbum = isAlbum;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        if (isAlbum) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albumcollect, parent, false);
        } else {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect, parent, false);
        }
    }

    @Override
    public RecyclerViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        DefaultViewHolder viewHolder = new DefaultViewHolder(mContext, realContentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.itemClick(position, mDatas.get(position));
            }
        });
        if (isAlbum) {
            bindAlbum(holder, position);
        } else {
            bindVideo(holder, position);
        }
    }

    private void bindAlbum(RecyclerViewHolder holder, int position) {
        AlbumInfo albumInfo = (AlbumInfo) mDatas.get(position);
        holder.setImageByUrlOrFilePath(R.id.imageView, albumInfo.getImg_fm(), R.drawable.default_video);
        holder.setText(R.id.name, albumInfo.getClass_name());
        holder.setVisibility(R.id.typeicon_tv, View.INVISIBLE);
        holder.setImageResDrawable(R.id.typeicon_tv, R.drawable.home_icon_005);
        holder.setVisibility(R.id.picnum_tv, View.VISIBLE);
        holder.setText(R.id.username_tv, albumInfo.getUsername());
        holder.setText(R.id.time_tv, albumInfo.getInserttime());
        ImageLoaderManager.getSingleton().LoadCircle(mContext, albumInfo.getPicture_src(), (ImageView) holder.getView(R.id.headicon), R.drawable.icon_mydefault);

    }

    private void bindVideo(RecyclerViewHolder holder, int position) {
        Video video = (Video) mDatas.get(position);
        holder.setImageByUrlOrFilePath(R.id.imageView, video.getImg_fm(), R.drawable.default_video);
        holder.setText(R.id.name, video.getName());
        holder.setText(R.id.time_tv, video.getInserttime());
//        holder.setText(R.id.desc_tv, video.getCompete_name());
        holder.setVisibility(R.id.typeicon_tv, View.VISIBLE);
        holder.setImageResDrawable(R.id.typeicon_tv, R.drawable.home_icon_005);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class DefaultViewHolder extends RecyclerViewHolder {
        public DefaultViewHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    public void addList(boolean isRefresh, List list) {
        if (isRefresh) {
            mDatas.clear();
            currentPage = 0;
        }
        if (!MyStrUtil.isEmpty(list)) {
            mDatas.addAll(list);
            currentPage++;
            notifyDataSetChanged();
        }
    }

    public BaseHomeItem getItem(int position) {
        return mDatas.get(position);
    }

    public void removePosition(BaseHomeItem homeItem) {
        mDatas.remove(homeItem);
        notifyDataSetChanged();
    }

    public void removeByID(String deleteId) {
        BaseHomeItem deleteItem = null;
        for (BaseHomeItem homeItem : mDatas) {
            if (TextUtils.equals(homeItem.getId(), deleteId)) {
                deleteItem = homeItem;
                break;
            }
        }
        if (deleteItem != null) {
            removePosition(deleteItem);
        }
    }

    public int currentPage = 0;

    public int getNextPage() {
        return currentPage + 1;
    }
}
