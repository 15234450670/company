package mr.li.dance.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.response.HomeZxResponse;
import mr.li.dance.https.response.ZiXunIndexResponse;
import mr.li.dance.models.BannerInfo;
import mr.li.dance.models.ZiXunInfo;
import mr.li.dance.models.HomeTypeBtn;
import mr.li.dance.ui.activitys.MyDanceWebActivity;
import mr.li.dance.ui.activitys.album.AlbumActivity;
import mr.li.dance.ui.activitys.match.MatchDetailActivity;
import mr.li.dance.ui.activitys.video.VideoDetailActivity;
import mr.li.dance.ui.activitys.video.ZhiBoDetailActivity;
import mr.li.dance.ui.activitys.zixun.ZiXunListActivity;
import mr.li.dance.ui.adapters.viewholder.BaseViewHolder;
import mr.li.dance.ui.widget.SlideShowView;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.ShareUtils;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-咨询适配器
 * 修订历史:
 */
public class ConsultationPageAdapter extends DanceBaseAdapter {

    Context mContext;
    public static final int TYPE_1 = 0xff01;
    public static final int TYPE_2 = 0xff02;
    public static final int TYPE_3 = 0xff03;
    public static final int TYPE_4 = 0xff04;
    private List<ZiXunInfo> mDatas;
    private List<BannerInfo> mLunBoDatas;
    private List<HomeTypeBtn> mTypeList;

    ListViewItemClickListener<ZiXunInfo> mItemClickListener;

    /**
     * 构造器
     *
     * @param
     */
    public ConsultationPageAdapter(Context context, ListViewItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
        mDatas = new ArrayList<>();
        mLunBoDatas = new ArrayList<>();
        mTypeList = new ArrayList<>();
    }


    @Override
    public int getItemCount() {
        if (MyStrUtil.isEmpty(mDatas) && MyStrUtil.isEmpty(mTypeList) && MyStrUtil.isEmpty(mLunBoDatas)) {
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
                return new ConsultationViewHolder1(view);
            case TYPE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontalrecyleview, null);
                return new ConsultationViewHolder2(view);
            case TYPE_3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation_type1, null);
                return new ConsultationViewHolder3(view);
            case TYPE_4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultation_item_type4, null);
                return new ConsultationViewHolder3(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConsultationViewHolder1) {
            bindType1((ConsultationViewHolder1) holder, position);
        } else if (holder instanceof ConsultationViewHolder2) {
            bindType2((ConsultationViewHolder2) holder, position);
        } else if (holder instanceof ConsultationViewHolder3) {
            bindType3((ConsultationViewHolder3) holder, position - 2);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        } else if (position == 1) {
            return TYPE_2;
        } else {
            ZiXunInfo ziXunInfo = mDatas.get(position - 2);
            if ("1".equals(ziXunInfo.getImg_num())) {
                return TYPE_3;
            } else {
                return TYPE_4;
            }
        }
    }

    class ConsultationViewHolder1 extends RecyclerView.ViewHolder {
        public SlideShowView slideShowView;

        public ConsultationViewHolder1(View itemView) {
            super(itemView);
            slideShowView = (SlideShowView) itemView.findViewById(R.id.slideShowView);
        }
    }


    class ConsultationViewHolder2 extends RecyclerView.ViewHolder {
        public RecyclerView mRecyclerView;

        public ConsultationViewHolder2(View view) {
            super(view);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }


    class ConsultationViewHolder3 extends BaseViewHolder {
        public TextView nameTv;

        public ConsultationViewHolder3(View view) {
            super(mContext, view);
            nameTv = (TextView) itemView.findViewById(R.id.name);
        }
    }


    private void bindType1(ConsultationViewHolder1 holder, int position) {
        if (!MyStrUtil.isEmpty(mLunBoDatas)) {
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
                            String url = String.format(AppConfigs.ZixunShareUrl,  bannerInfo.getNumber());
                            MyDanceWebActivity.lunch(mContext, MyDanceWebActivity.ZIXUNTYPE,  "",url, true);
                            break;
                        case 10104://图片
                            AlbumActivity.lunch(mContext, bannerInfo.getNumber(), "");
                            break;
                        case 10105://赛事
                            MatchDetailActivity.lunch(mContext, bannerInfo.getNumber());
                            break;
                        case 10106://外联
                            if (!MyStrUtil.isEmpty(bannerInfo.getUrl())) {
                                MyDanceWebActivity.lunch(mContext, MyDanceWebActivity.OTHERTYPE, "",bannerInfo.getUrl(), bannerInfo.getId());
                            }
                            break;
                    }
                }
            });
            holder.slideShowView.setImageUrls(mLunBoDatas);
            holder.slideShowView.startPlay();

        }
    }

    private void bindType2(ConsultationViewHolder2 holder, int position) {
        if (MyStrUtil.isEmpty(mTypeList)) {
            holder.mRecyclerView.setVisibility(View.GONE);
            return;
        } else {
            holder.mRecyclerView.setVisibility(View.VISIBLE);
        }
        VideoSpeedAdapter speedAdapter = (VideoSpeedAdapter) holder.mRecyclerView.getAdapter();
        if (speedAdapter == null) {
            speedAdapter = new VideoSpeedAdapter(mContext, true);
            speedAdapter.setItemClickListener(new ListViewItemClickListener<HomeTypeBtn>() {
                @Override
                public void itemClick(int position, HomeTypeBtn value) {
                    ZiXunListActivity.lunch(mContext, value.getName(), value.getId());
                }
            });
            holder.mRecyclerView.setAdapter(speedAdapter);
        }
        speedAdapter.addList(true, mTypeList);

    }

    private void bindType3(ConsultationViewHolder3 holder, final int position) {
        final ZiXunInfo ziXunInfo = mDatas.get(position);
        holder.danceViewHolder.setText(R.id.name, ziXunInfo.getTitle());
        holder.danceViewHolder.setText(R.id.time_tv, ziXunInfo.getInserttime());
        holder.danceViewHolder.setViewVisibility(R.id.typeicon_tv, View.INVISIBLE);
        holder.danceViewHolder.setText(R.id.from_tv, "来源: "+ziXunInfo.getWriter());

        if ("1".equals(ziXunInfo.getImg_num())) {
            holder.danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, ziXunInfo.getPicture(), R.drawable.default_video);
        } else {
            holder.danceViewHolder.setImageByUrlOrFilePath(R.id.pic1_iv, ziXunInfo.getPicture(), R.drawable.default_video);
            holder.danceViewHolder.setImageByUrlOrFilePath(R.id.pic2_iv, ziXunInfo.getPicture_2(), R.drawable.default_video);
            holder.danceViewHolder.setImageByUrlOrFilePath(R.id.pic3_iv, ziXunInfo.getPicture_3(), R.drawable.default_video);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.itemClick(position, mDatas.get(position));
            }
        });

        holder.danceViewHolder.setClickListener(R.id.share_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils shareUtils = new ShareUtils((Activity) mContext);
                String shareUrl = String.format(AppConfigs.ZixunShareUrl, ziXunInfo.getId());
                String mShareContent = ziXunInfo.getTitle();
                shareUtils.showShareDilaog(AppConfigs.CLICK_EVENT_21,shareUrl, mShareContent);
            }
        });

    }

    public void refresh(HomeZxResponse response) {
        super.refresh();
        mLunBoDatas.clear();
        mDatas.clear();
        mTypeList.clear();
        ArrayList<BannerInfo> bannerInfos = response.getData().getBanner();
        if (!MyStrUtil.isEmpty(bannerInfos)) {
            mLunBoDatas.addAll(bannerInfos);
        }

        ArrayList<ZiXunInfo> ziXunInfos = response.getData().getZxRec();
        if (!MyStrUtil.isEmpty(ziXunInfos)) {
            mDatas.addAll(ziXunInfos);
        }

        ArrayList<HomeTypeBtn> types = response.getData().getZx_type();
        if (!MyStrUtil.isEmpty(types)) {
            mTypeList.addAll(types);
        }

        notifyDataSetChanged();
    }

    public void loadMore(ZiXunIndexResponse indexResponse) {
        ArrayList<ZiXunInfo> ziXunInfos = indexResponse.getData();
        if (!MyStrUtil.isEmpty(ziXunInfos)) {
            mDatas.addAll(ziXunInfos);
            super.loadMore();
        }
        notifyDataSetChanged();
    }
}