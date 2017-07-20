package mr.li.dance.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.response.HomeIndexResponse;
import mr.li.dance.https.response.HomeResponse;
import mr.li.dance.https.response.HomeZhiBoIndexResponse;
import mr.li.dance.https.response.HomeZhiBoResponse;
import mr.li.dance.models.HomeListItemInfo;
import mr.li.dance.models.QuickZhiboInfo;
import mr.li.dance.models.ZhiBoInfo;
import mr.li.dance.ui.adapters.viewholder.BaseViewHolder;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.ShareUtils;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-直播适配器
 * 修订历史:
 */
public class DirectseedingAdapter extends DanceBaseAdapter {
    public final int TYPE_2 = 0xff02;
    public final int TYPE_MAIN = 0xffff;
    Context mContext;
    ListViewItemClickListener mItemClickListener;
    private List<ZhiBoInfo> mDatas;
    private List<QuickZhiboInfo> mQuickList;
    private int EXARCOUNT = 0;


    /**
     * 构造器
     *
     * @param
     */
    public DirectseedingAdapter(Context context, ListViewItemClickListener listener) {
        mItemClickListener = listener;
        mContext = context;
        mDatas = new ArrayList<>();
        mQuickList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        if (mQuickList.size() == 0) {
            return mDatas.size();
        } else {
            return mDatas.size() + 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType) {
     /*       case TYPE_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_play, null);
                return new VideoViewholder1(view);*/
            case TYPE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhibo_type2, null);
                viewHolder = new VideoViewholder2(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base, null);
                viewHolder = new ViewHolderMain(view);
                break;

        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoViewholder2) {
            bindType2((VideoViewholder2) holder, position);
        } else if (holder instanceof ViewHolderMain) {
            bindTypeMain((ViewHolderMain) holder, position - EXARCOUNT);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (EXARCOUNT != 0 && position == 0) {
            return TYPE_2;
        } else {
            return TYPE_MAIN;
        }
    }

    class VideoViewholder1 extends BaseViewHolder {
        public VideoViewholder1(View view) {
            super(mContext, view);
        }
    }

    class ViewHolderMain extends BaseViewHolder {
        public ViewHolderMain(View view) {
            super(mContext, view);
        }
    }

    /**
     * 顶部轮播ViewHolder
     */
    class VideoViewholder2 extends BaseViewHolder {
        RecyclerView mRecyclerView;

        public VideoViewholder2(View view) {
            super(mContext, view);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    private void bindType1(VideoViewholder1 holder, int position) {
    }

    private void bindType2(final VideoViewholder2 holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (QuickZhiboInfo quickZhiboInfo : mQuickList) {
                    if (quickZhiboInfo.isPlaying()) {
                        ZhiBoInfo zhiBoInfo = new ZhiBoInfo();
                        zhiBoInfo.setId(quickZhiboInfo.getId());
                        zhiBoInfo.setActivity_id(quickZhiboInfo.getActivity_id());
                        mItemClickListener.itemClick(0, zhiBoInfo);
                        break;
                    }
                }
            }
        });
        if (!MyStrUtil.isEmpty(mQuickList)) {
            holder.danceViewHolder.setImageByUrlOrFilePath(R.id.video_bg, mQuickList.get(0).getPicture(), R.drawable.default_video);
        } else {
            holder.danceViewHolder.setViewVisibility(R.id.item_layout, View.GONE);
        }
        holder.danceViewHolder.setText(R.id.type_tv, "更多直播");
        BaseRecyclerAdapter speedAdapter = (BaseRecyclerAdapter) holder.mRecyclerView.getAdapter();
        if (speedAdapter == null) {
            speedAdapter = new DirectseedSpeedAdapter(mContext);
            speedAdapter.setItemClickListener(new ListViewItemClickListener<QuickZhiboInfo>() {
                @Override
                public void itemClick(int position, QuickZhiboInfo value) {
                    for (QuickZhiboInfo quickZhiboInfo : mQuickList) {
                        quickZhiboInfo.setPlaying(false);
                    }
                    value.setPlaying(true);
                    ZhiBoInfo zhiBoInfo = new ZhiBoInfo();
                    zhiBoInfo.setId(value.getId());
                    mItemClickListener.itemClick(0, zhiBoInfo);
                    holder.danceViewHolder.setText(R.id.matchname_tv, value.getBrief());
                }
            });
            if (mQuickList.size() > 0) {
                holder.danceViewHolder.setText(R.id.matchname_tv, mQuickList.get(0).getBrief());
            }

            speedAdapter.addList(mQuickList);
            holder.mRecyclerView.setAdapter(speedAdapter);
        } else {
            speedAdapter.getmList().clear();
            speedAdapter.addList(true, mQuickList);
            for (QuickZhiboInfo quickZhiboInfo : mQuickList) {
                if (quickZhiboInfo.isPlaying()) {
                    holder.danceViewHolder.setText(R.id.matchname_tv, quickZhiboInfo.getBrief());
                }
            }
            speedAdapter.notifyDataSetChanged();
        }
        if (MyStrUtil.isEmpty(mQuickList)) {
            holder.danceViewHolder.setViewVisibility(R.id.item_layout, View.GONE);
        } else {
            holder.danceViewHolder.setViewVisibility(R.id.item_layout, View.VISIBLE);
        }
    }


    private void bindTypeMain(ViewHolderMain holder, final int position) {
        final ZhiBoInfo zhiBoInfo = mDatas.get(position);

        holder.danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, zhiBoInfo.getPicture(), R.drawable.default_video);
        holder.danceViewHolder.setText(R.id.name, zhiBoInfo.getName());
        holder.danceViewHolder.setText(R.id.time_tv, zhiBoInfo.getStart_time());
        holder.danceViewHolder.setViewVisibility(R.id.typeicon_tv, View.VISIBLE);
        holder.danceViewHolder.setImageResDrawable(R.id.typeicon_tv, R.drawable.home_icon_008, R.drawable.home_icon_008);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.itemClick(0, mDatas.get(position));
            }
        });
        holder.danceViewHolder.setClickListener(R.id.share_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils shareUtils = new ShareUtils((Activity) mContext);
                String shareUrl = String.format(AppConfigs.SHARELIVE, zhiBoInfo.getId());
                String mShareContent = zhiBoInfo.getName();
                shareUtils.showShareDilaog(AppConfigs.CLICK_EVENT_19,shareUrl, mShareContent);
            }
        });
    }

    public void refresh(HomeZhiBoResponse homeZhiBoResponse) {
        super.refresh();
        ArrayList<ZhiBoInfo> matches = homeZhiBoResponse.getData().getZbRecList();
        mDatas.clear();
        if (!MyStrUtil.isEmpty(matches)) {
            mDatas.addAll(matches);
        }
        ArrayList<QuickZhiboInfo> quickZhiboInfos = homeZhiBoResponse.getData().getKszb();
        mQuickList.clear();
        if (!MyStrUtil.isEmpty(quickZhiboInfos)) {
            mQuickList.addAll(quickZhiboInfos);
            mQuickList.get(0).setPlaying(true);
            EXARCOUNT = 1;
        } else {
            EXARCOUNT = 0;
        }
        notifyDataSetChanged();
    }

    public void loadMore(HomeZhiBoIndexResponse indexResponse) {
        ArrayList<ZhiBoInfo> matches = indexResponse.getData();
        if (!MyStrUtil.isEmpty(matches)) {
            mDatas.addAll(matches);
            super.loadMore();
        }
        notifyDataSetChanged();
    }
}