package mr.li.dance.ui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import mr.li.dance.R;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.Match;
import mr.li.dance.models.Video;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.glide.ImageLoaderManager;

/**
 * Created by Lixuewei on 2017/6/3.
 */

public class WonderfulPicAdapter extends BaseRecyclerAdapter<AlbumInfo> {
    private final int TYPE_1 = 0x001;
    private final int TYPE_2 = 0x002;
    private final int TYPE_3 = 0x003;


    private Match mMatchInfo;
    public WonderfulPicAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public AlbumInfo getmItem(int position) {
        if(position>1){
            return mData.get(position - 2);
        }else{
            return null;
        }

    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == TYPE_1) {
            return R.layout.item_match_type5;
        } else if(viewType == TYPE_2){
            return R.layout.split_line;
        }else{
            return R.layout.item_album;
        }

    }

    @Override
    public int getItemCount() {
        if (MyStrUtil.isEmpty(mData) && mMatchInfo == null) {
            return 0;
        }
        return super.getItemCount()+2;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, AlbumInfo item) {
        if(position == 0){
            bindTitle(holder);
        }else if(position == 1){

        }else{
            bindList(holder,position);
        }
    }

    private void bindTitle(RecyclerViewHolder holder) {
        if (null == mMatchInfo) {
            return;
        }
        switch (mMatchInfo.getType()) {
            case 10001:
                holder.setImageResDrawable(R.id.imageView, R.drawable.wdsf_icon);
                break;
            case 10002:
                holder.setImageResDrawable(R.id.imageView, R.drawable.cdsf_icon);
                break;
            case 10003:
                holder.setImageResDrawable(R.id.imageView, R.drawable.addmathc_icon);
                break;
        }
        holder.setText(R.id.name, mMatchInfo.getTitle());
        holder.setText(R.id.timemark, mMatchInfo.getAddress());
    }
    private void bindList(RecyclerViewHolder holder,int position) {
        AlbumInfo albumInfo = mData.get(position-2);
        holder.setRoundImageByUrlOrFilePath(R.id.imageView, albumInfo.getImg_fm(), R.drawable.default_video);
        holder.setText(R.id.name, albumInfo.getClass_name());
        holder.setText(R.id.time_tv, albumInfo.getInserttime());
        holder.setText(R.id.num_tv, "共"+albumInfo.getPhotos()+"张");
        holder.getView(R.id.picnum_tv).setVisibility(View.VISIBLE);
        holder.setText(R.id.username_tv, albumInfo.getUsername());
        ImageLoaderManager.getSingleton().LoadCircle(mContext, albumInfo.getPicture_src(), (ImageView) holder.getView(R.id.headicon), R.drawable.icon_mydefault);


    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        } else if(position == 1){
            return TYPE_2;
        }else{
            return TYPE_3;
        }
    }

    public void setmMatchInfo(Match mMatchInfo) {
        this.mMatchInfo = mMatchInfo;
    }
}
