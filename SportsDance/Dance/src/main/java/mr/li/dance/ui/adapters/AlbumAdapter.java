package mr.li.dance.ui.adapters;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.util.Util;

import mr.li.dance.R;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.utils.DensityUtil;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.Utils;
import mr.li.dance.utils.glide.ImageLoaderManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 相册适配器
 * 修订历史:
 */
public class AlbumAdapter extends BaseRecyclerAdapter<AlbumInfo> {
    private final int TYPE_1 = 0x001;
    private final int TYPE_2 = 0x002;

    private int item_width;

    public AlbumAdapter(Context ctx) {
        super(ctx);
        item_width = (int) (DensityUtil.getScreenWidth(mContext) - mContext.getResources().getDimension(R.dimen.spacing_20) * 3) / 2;

    }

    @Override
    public int getItemCount() {
        if (MyStrUtil.isEmpty(mData)) {
            return 0;
        } else {
            return super.getItemCount();
        }
    }

    @Override
    public int getItemLayoutId(int viewType) {
//        if (TYPE_1 == viewType) {
//            return R.layout.item_picture;
//        } else {
//            return R.layout.item_picture2;
//        }
        return R.layout.item_picture;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, AlbumInfo item) {
        ImageView imageView = holder.getImageView(R.id.item_img);

        int imageWidth = item.getWidth();
        int imageHeight = item.getHeight();
        if (imageWidth == 0) {
            imageWidth = 1334;
            imageHeight = 2000;

        }
        int itemHeightSize = imageHeight * item_width / imageWidth;
        int itemWidthSize = item_width;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemWidthSize, itemHeightSize);
        imageView.setLayoutParams(layoutParams);

        ImageLoaderManager.getSingleton().LoadBySize(mContext, item.getPhoto(), imageView, R.drawable.default_video, itemWidthSize, itemHeightSize);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }

    }
}
