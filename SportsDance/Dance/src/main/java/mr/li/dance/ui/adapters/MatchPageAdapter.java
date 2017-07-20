package mr.li.dance.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.response.MatchIndexResponse;
import mr.li.dance.https.response.MatchResponse;
import mr.li.dance.models.BannerInfo;
import mr.li.dance.models.Match;
import mr.li.dance.ui.activitys.MyDanceWebActivity;
import mr.li.dance.ui.activitys.album.AlbumActivity;
import mr.li.dance.ui.activitys.match.MatchDetailActivity;
import mr.li.dance.ui.activitys.match.MatchTypeListActivity;
import mr.li.dance.ui.activitys.video.VideoDetailActivity;
import mr.li.dance.ui.activitys.video.ZhiBoDetailActivity;
import mr.li.dance.ui.adapters.viewholder.BaseViewHolder;
import mr.li.dance.ui.widget.SlideShowView;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.ShareUtils;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-赛事-适配器
 * 修订历史:
 */
public class MatchPageAdapter extends DanceBaseAdapter {

    Context mContext;
    public static final int TYPE_1 = 0xff01;
    public static final int TYPE_2 = 0xff02;
    public static final int TYPE_3 = 0xff03;
    public static final int TYPE_MAIN = 0xffff;
    private List<Match> mDatas;
    private List<BannerInfo> mLunBoDatas;

    /**
     * 构造器
     *
     * @param
     */
    public MatchPageAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
        mLunBoDatas = new ArrayList<>();


    }


    @Override
    public int getItemCount() {
        if (MyStrUtil.isEmpty(mDatas) && MyStrUtil.isEmpty(mLunBoDatas)) {
            return 0;
        } else {
            return mDatas.size() + 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType) {
            case TYPE_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_type1, null);
                return new VideoViewholder1(view);
            case TYPE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matchpage_type2, null);
                return new VideoViewholder2(view);
            case TYPE_3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_type3, null);
                return new VideoViewholder3(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_type4, null);
                return new ViewHolderMain(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoViewholder1) {
            bindType1((VideoViewholder1) holder, position);
        } else if (holder instanceof VideoViewholder2) {
            bindType2((VideoViewholder2) holder, position);
        } else if (holder instanceof VideoViewholder3) {
            bindType3((VideoViewholder3) holder, position - 2);
        } else if (holder instanceof ViewHolderMain) {
            bindTypeMain((ViewHolderMain) holder, position - 2);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        } else if (position == 1) {
            return TYPE_2;
        } else {
            if (mDatas.get(position - 2).getItemType() == 7) {
                return TYPE_3;
            } else {
                return TYPE_MAIN;
            }
        }
    }

    class VideoViewholder1 extends BaseViewHolder {

        public SlideShowView slideShowView;

        public VideoViewholder1(View itemView) {
            super(mContext, itemView);
            slideShowView = (SlideShowView) itemView.findViewById(R.id.slideShowView);
        }
    }


    class VideoViewholder2 extends BaseViewHolder {
        public VideoViewholder2(View view) {
            super(mContext, view);
        }
    }

    class VideoViewholder3 extends BaseViewHolder {
        public RecyclerView mRecyclerView;

        public VideoViewholder3(View view) {
            super(mContext, view);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
        }
    }


    class ViewHolderMain extends BaseViewHolder {
        public ViewHolderMain(View view) {
            super(mContext, view);
        }
    }

    private void bindType1(VideoViewholder1 holder, int position) {
        holder.slideShowView.setOnGolistener(new SlideShowView.BannerClickListener() {
            @Override
            public void itemClick(int position) {
                BannerInfo bannerInfo = mLunBoDatas.get(position);
                switch (bannerInfo.getType()) {
                    case 10101://直播
                        ZhiBoDetailActivity.lunch(mContext, bannerInfo.getNumber());
                        break;
                    case 10102://点播
                        VideoDetailActivity.lunch(mContext, bannerInfo.getNumber());
                        break;
                    case 10103://z咨询
                        String url = String.format(AppConfigs.ZixunShareUrl, bannerInfo.getNumber());
                        MyDanceWebActivity.lunch(mContext, MyDanceWebActivity.ZIXUNTYPE, "", url, true);
                        break;
                    case 10104://图片
                        AlbumActivity.lunch(mContext, bannerInfo.getNumber(), "");
                        break;
                    case 10105://赛事
                        MatchDetailActivity.lunch(mContext, bannerInfo.getNumber());
                        break;
                    case 10106://外联
                        if (!MyStrUtil.isEmpty(bannerInfo.getUrl())) {
                            MyDanceWebActivity.lunch(mContext, MyDanceWebActivity.OTHERTYPE, "", bannerInfo.getUrl(), bannerInfo.getId());
                        }
                        break;
                }
            }
        });
        holder.slideShowView.setImageUrls(mLunBoDatas);
        holder.slideShowView.startPlay();
    }

    private void bindType2(VideoViewholder2 holder, int position) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = 10001;
                switch (view.getId()) {
                    case R.id.type1_layout:
                        type = 10001;
                        break;
                    case R.id.type2_layout:
                        type = 10002;
                        break;
                    case R.id.type3_layout:
                        type = 10003;
                        break;
                }
                MatchTypeListActivity.lunch(mContext, type);
            }
        };
        holder.danceViewHolder.setClickListener(R.id.type1_layout, onClickListener);
        holder.danceViewHolder.setClickListener(R.id.type2_layout, onClickListener);
        holder.danceViewHolder.setClickListener(R.id.type3_layout, onClickListener);

    }

    private void bindType3(VideoViewholder3 holder, final int position) {
        if (mDatas.get(position).isFirst()) {
            holder.danceViewHolder.setText(R.id.type_tv, "赛事直播");
            holder.danceViewHolder.setViewVisibility(R.id.top_layout, View.VISIBLE);
            holder.danceViewHolder.setViewVisibility(R.id.spit_line, View.VISIBLE);
        } else {
            holder.danceViewHolder.setViewVisibility(R.id.top_layout, View.GONE);
            holder.danceViewHolder.setViewVisibility(R.id.spit_line, View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchDetailActivity.lunch(mContext, mDatas.get(position).getId());
            }
        });
        final Match match = (Match) mDatas.get(position);
        holder.danceViewHolder.setText(R.id.name, match.getName());
        holder.danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, match.getPicture(), R.drawable.default_video);
        holder.danceViewHolder.setText(R.id.starttime, "开始时间:" + match.getStart_date());
        holder.danceViewHolder.setText(R.id.endtime, "结束时间:" + match.getEnd_date());

        switch (match.getType()) {
            case 10001:
                holder.danceViewHolder.setText(R.id.level_tv, "WDSF");
                break;
            case 10002:
                holder.danceViewHolder.setText(R.id.level_tv, "CDSF");
                break;
            case 10003:
                holder.danceViewHolder.setText(R.id.level_tv, "地方赛事");
                break;
        }
        holder.danceViewHolder.setClickListener(R.id.share_layout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtils shareUtils = new ShareUtils((Activity) mContext);
                String shareUrl = String.format(AppConfigs.SHAREGAME, match.getId());
                String mShareContent = match.getName();
                String countID = AppConfigs.CLICK_EVENT_22;
                shareUtils.showShareDilaog(countID,shareUrl, mShareContent);
            }
        });

    }

    private void bindTypeMain(ViewHolderMain holder, final int position) {
        final Match match = mDatas.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchDetailActivity.lunch(mContext, match.getId());
            }
        });

//        holder.danceViewHolder.setClickListener(R.id.share_layout, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShareUtils shareUtils = new ShareUtils((Activity) mContext);
//                String shareUrl = String.format(AppConfigs.SHAREGAME, match.getId());
//                String mShareContent = match.getTitle();
//                String countID = AppConfigs.CLICK_EVENT_22;
//                shareUtils.showShareDilaog(countID,shareUrl, mShareContent);
//            }
//        });


        if (mDatas.get(position).isFirst()) {
            holder.danceViewHolder.setText(R.id.type_tv, "热门赛事");
            holder.danceViewHolder.setViewVisibility(R.id.spit_line, View.VISIBLE);
            holder.danceViewHolder.setViewVisibility(R.id.top_layout, View.VISIBLE);
        } else {
            holder.danceViewHolder.setViewVisibility(R.id.top_layout, View.GONE);
            holder.danceViewHolder.setViewVisibility(R.id.spit_line, View.GONE);
        }

        holder.danceViewHolder.setText(R.id.name, match.getTitle());
        holder.danceViewHolder.setText(R.id.time_tv, match.getStart_date() + "-" + match.getEnd_date());
        holder.danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, match.getPicture(), R.drawable.default_video);
        switch (match.getType()) {
            case 10001:
                holder.danceViewHolder.setImageResDrawable(R.id.imageView, R.drawable.wdsf_icon, R.drawable.wdsf_icon);
                break;
            case 10002:
                holder.danceViewHolder.setImageResDrawable(R.id.imageView, R.drawable.cdsf_icon, R.drawable.cdsf_icon);
                break;
            case 10003:
                holder.danceViewHolder.setImageResDrawable(R.id.imageView, R.drawable.addmathc_icon, R.drawable.addmathc_icon);
                break;
        }
    }

    public void refresh(MatchResponse matchResponse) {
        super.refresh();
        mLunBoDatas.clear();
        mDatas.clear();
        ArrayList<BannerInfo> banner = matchResponse.getData().getBanner();
        if (!MyStrUtil.isEmpty(banner)) {
            mLunBoDatas.addAll(banner);
        }
        ArrayList<Match> zbMatchs = matchResponse.getData().getZbMatch();
        if (!MyStrUtil.isEmpty(zbMatchs)) {
            zbMatchs.get(0).setFirst(true);
            for (Match match : zbMatchs) {
                match.setItemType(7);
            }
            mDatas.addAll(zbMatchs);
        }
        ArrayList<Match> hotMatchs = matchResponse.getData().getHotMatch();
        if (!MyStrUtil.isEmpty(hotMatchs)) {
            hotMatchs.get(0).setFirst(true);
            for (Match match : hotMatchs) {
                match.setItemType(8);
            }
            mDatas.addAll(hotMatchs);
        }
        notifyDataSetChanged();
    }

    public void loadMore(MatchIndexResponse matchResponse) {
        ArrayList<Match> matches = matchResponse.getData();
        if (!MyStrUtil.isEmpty(matches)) {
            super.loadMore();
            for (Match match : matches) {
                match.setItemType(8);
            }
            mDatas.addAll(matches);
        }
        notifyDataSetChanged();
    }

}