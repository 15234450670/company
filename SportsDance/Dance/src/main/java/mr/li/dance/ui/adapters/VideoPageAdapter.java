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
import mr.li.dance.https.response.HomeVideoIndexResponse;
import mr.li.dance.https.response.HomeVideoResponse;
import mr.li.dance.models.HomeTypeBtn;
import mr.li.dance.models.QuickZhiboInfo;
import mr.li.dance.models.Video;
import mr.li.dance.ui.activitys.video.VideoDetailActivity;
import mr.li.dance.ui.activitys.video.VideoListActivity;
import mr.li.dance.ui.adapters.viewholder.BaseViewHolder;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.ShareUtils;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-视频页面适配器
 * 修订历史:
 */
public class VideoPageAdapter extends DanceBaseAdapter {

    Context mContext;
    public static final int TYPE_1 = 0xff01;
    public static final int TYPE_2 = 0xff03;
    public static final int TYPE_MAIN = 0xffff;
    private List<Video> mDatas;
    private List<QuickZhiboInfo> mQuickList;
    private List<HomeTypeBtn> mTypeList;
    private int EXARCOUNT = 0;

    private boolean hasQuickList;
    private boolean hasTypeList;

    /**
     * 构造器
     *
     * @param
     */
    public VideoPageAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
        mQuickList = new ArrayList<>();
        mTypeList = new ArrayList<>();
    }


    @Override
    public int getItemCount() {
        if (MyStrUtil.isEmpty(mDatas) && MyStrUtil.isEmpty(mQuickList) && MyStrUtil.isEmpty(mTypeList)) {
            return 0;
        }
        return mDatas.size() + EXARCOUNT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType) {
            case TYPE_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_play, null);
                return new VideoViewholder1(view);
            case TYPE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videopage_type3, null);
                return new VideoViewholder3(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base, null);
                return new ViewHolderMain(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoViewholder1) {
            bindType1((VideoViewholder1) holder, position);
        } else if (holder instanceof VideoViewholder3) {
            bindType3((VideoViewholder3) holder, position);
        } else if (holder instanceof ViewHolderMain) {
            bindTypeMain((ViewHolderMain) holder, position - EXARCOUNT);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (EXARCOUNT == 2) {
            if (position == 0) {
                return TYPE_1;
            } else if (position == 1) {
                return TYPE_2;
            } else {
                return TYPE_MAIN;
            }
        } else if (EXARCOUNT == 1) {
            if (position == 0) {
                if (hasQuickList) {
                    return TYPE_1;
                } else {
                    return TYPE_2;
                }
            } else {
                return TYPE_MAIN;
            }
        } else {
            return TYPE_MAIN;
        }

    }

    class VideoViewholder1 extends BaseViewHolder {
        public RecyclerView mRecyclerView;

        public VideoViewholder1(View view) {
            super(mContext, view);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }


    class VideoViewholder3 extends BaseViewHolder {
        public RecyclerView mRecyclerView;

        public VideoViewholder3(View view) {
            super(mContext, view);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    class ViewHolderMain extends BaseViewHolder {
        public ViewHolderMain(View view) {
            super(mContext, view);
        }
    }

    private void bindType1(VideoViewholder1 holder, int position) {
        if (mQuickList.size() == 0) {
            holder.danceViewHolder.setViewVisibility(R.id.item_layout, View.GONE);
        } else {
            holder.danceViewHolder.setViewVisibility(R.id.item_layout, View.VISIBLE);
            final QuickZhiboInfo zhiboInfo = mQuickList.get(0);
            holder.danceViewHolder.setImageByUrlOrFilePath(R.id.video_bg, zhiboInfo.getPicture(), R.drawable.default_video);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoDetailActivity.lunch(mContext, zhiboInfo.getId());
                }
            });

            DirectseedSpeedAdapter speedAdapter = (DirectseedSpeedAdapter) holder.mRecyclerView.getAdapter();
            if (MyStrUtil.isEmpty(speedAdapter)) {
                speedAdapter = new DirectseedSpeedAdapter(mContext);
            }
            speedAdapter.setItemClickListener(new ListViewItemClickListener() {
                @Override
                public void itemClick(int position, Object value) {
                    VideoDetailActivity.lunch(mContext, mQuickList.get(position).getId());
                }
            });

            holder.mRecyclerView.setAdapter(speedAdapter);
            speedAdapter.addList(true, mQuickList);
            if (MyStrUtil.isEmpty(mQuickList)) {
                holder.mRecyclerView.setVisibility(View.GONE);
            } else {
                holder.mRecyclerView.setVisibility(View.VISIBLE);
            }

        }

    }

//    private void bindType2(VideoViewholder2 holder, int position) {
//        DirectseedSpeedAdapter speedAdapter = (DirectseedSpeedAdapter) holder.mRecyclerView.getAdapter();
//        if (MyStrUtil.isEmpty(speedAdapter)) {
//            speedAdapter = new DirectseedSpeedAdapter(mContext);
//        }
//        speedAdapter.setItemClickListener(new ListViewItemClickListener() {
//            @Override
//            public void itemClick(int position, Object value) {
//                VideoDetailActivity.lunch(mContext, mQuickList.get(position).getId());
//            }
//        });
//
//        holder.mRecyclerView.setAdapter(speedAdapter);
//        speedAdapter.addList(true, mQuickList);
//        if (MyStrUtil.isEmpty(mQuickList)) {
//            holder.mRecyclerView.setVisibility(View.GONE);
//        } else {
//            holder.mRecyclerView.setVisibility(View.VISIBLE);
//        }
//    }

    private void bindType3(VideoViewholder3 holder, int position) {

        holder.danceViewHolder.setText(R.id.type_tv, "视频");
        if (MyStrUtil.isEmpty(mTypeList)) {
            holder.mRecyclerView.setVisibility(View.GONE);
        } else {
            holder.mRecyclerView.setVisibility(View.VISIBLE);
            VideoSpeedAdapter adapter = (VideoSpeedAdapter) holder.mRecyclerView.getAdapter();
            if (MyStrUtil.isEmpty(adapter)) {
                adapter = new VideoSpeedAdapter(mContext);
                holder.mRecyclerView.setAdapter(adapter);
                adapter.setItemClickListener(new ListViewItemClickListener<HomeTypeBtn>() {
                    @Override
                    public void itemClick(int position, HomeTypeBtn value) {
                        VideoListActivity.lunch(mContext, value.getName(), value.getId());
                    }
                });
            }

            adapter.addList(true, mTypeList);
        }

    }

    private void bindTypeMain(ViewHolderMain holder, int position) {
        final Video video = mDatas.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoDetailActivity.lunch(mContext, video.getId());
            }
        });
        holder.danceViewHolder.setClickListener(R.id.share_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils shareUtils = new ShareUtils((Activity) mContext);
                String shareUrl = String.format(AppConfigs.SHAREMOV, video.getId());
                String mShareContent = video.getTitle();

                shareUtils.showShareDilaog(AppConfigs.CLICK_EVENT_18,shareUrl, mShareContent);
            }
        });

        holder.danceViewHolder.setViewVisibility(R.id.typeicon_tv, View.VISIBLE);
        holder.danceViewHolder.setImageResDrawable(R.id.typeicon_tv, R.drawable.home_icon_005, R.drawable.home_icon_005);
        holder.danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, video.getPicture(), R.drawable.default_video);
        holder.danceViewHolder.setText(R.id.name, video.getTitle());
        holder.danceViewHolder.setText(R.id.time_tv, video.getStart_time());
    }

    public void refresh(HomeVideoResponse response) {
        super.refresh();
        EXARCOUNT = 0;
        hasQuickList = false;
        hasTypeList = false;

        mDatas.clear();
        mTypeList.clear();
        mQuickList.clear();
        ArrayList<Video> videos = response.getData().getDb_rec();
        if (!MyStrUtil.isEmpty(videos)) {
            mDatas.addAll(videos);
        }
        ArrayList<HomeTypeBtn> videoTypes = response.getData().getDb_type();
        if (!MyStrUtil.isEmpty(videoTypes)) {
            mTypeList.addAll(videoTypes);
            EXARCOUNT = EXARCOUNT + 1;
            hasTypeList = true;
        }
        ArrayList<QuickZhiboInfo> quickZhiboInfos = response.getData().getDianbo();
        if (!MyStrUtil.isEmpty(quickZhiboInfos)) {
            mQuickList.addAll(quickZhiboInfos);
            EXARCOUNT = EXARCOUNT + 1;
            hasQuickList = true;
        }
        notifyDataSetChanged();
    }

    public void loadMore(HomeVideoIndexResponse response) {
        ArrayList<Video> videos = response.getData();
        if (!MyStrUtil.isEmpty(videos)) {
            mDatas.addAll(videos);
            super.loadMore();
        }
        notifyDataSetChanged();
    }
}