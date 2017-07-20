package mr.li.dance.ui.adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.response.HomeIndexResponse;
import mr.li.dance.https.response.HomeResponse;
import mr.li.dance.models.BaseHomeItem;
import mr.li.dance.models.BannerInfo;
import mr.li.dance.models.MathcRecommend;
import mr.li.dance.ui.activitys.MyDanceWebActivity;
import mr.li.dance.ui.activitys.album.AlbumActivity;
import mr.li.dance.ui.activitys.match.MatchDetailActivity;
import mr.li.dance.ui.activitys.video.VideoDetailActivity;
import mr.li.dance.ui.activitys.video.ZhiBoDetailActivity;
import mr.li.dance.ui.adapters.viewholder.BaseViewHolder;
import mr.li.dance.ui.adapters.viewholder.HomeViewHolder;
import mr.li.dance.ui.widget.SlideShowView;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DownTimer;
import mr.li.dance.utils.DownTimerListener;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NLog;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-推荐页面适配器
 * 修订历史:
 */
public class HomeRecyclerAdapter extends DanceBaseAdapter {

    Context mContext;
    private List<BannerInfo> mLunBoDatas;
    private List<BaseHomeItem> mDatas;
    ArrayList<MathcRecommend> mathcRecommends;

    private int mExtraCount = 2;//除了列表额外的加载项目数
    private final int TYPE_1 = 1;//轮播页面
    private final int TYPE_2 = 2;//赛事上下滚动的
    private final int TYPE_ZHIBO = 3;//直播
    private final int TYPE_DianBO = 4;//点播
    private final int TYPE_ZIXUNONPIC = 5;//资讯1张图
    private final int TYPE_ZIXU_THREE_PIC = 6;//资讯3张图
    private final int TYPE_XIANGCE = 7;//相册
    private final int TYPE_SAISHI = 8;//赛事
    private final int TYPE_WAILIAN = 9;//外联
    private final int TYPE_MAIN = 10;//正常的列表加载页面


    /**
     * 构造器
     *
     * @param
     */
    public HomeRecyclerAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<BaseHomeItem>();
        mLunBoDatas = new ArrayList<>();
        mathcRecommends = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        if (MyStrUtil.isEmpty(mLunBoDatas) && MyStrUtil.isEmpty(mDatas) && MyStrUtil.isEmpty(mathcRecommends)) {
            return 0;
        } else {
            return mDatas.size() + mExtraCount;
        }

    }

    public void loadMore(HomeIndexResponse homeResponse) {
        ArrayList indexRec = homeResponse.getData();
        if (!MyStrUtil.isEmpty(indexRec)) {
            currentPage++;
        }
        addDataItems(indexRec);
    }

    public void refresh(HomeResponse homeResponse) {
        currentPage = 1;
        mLunBoDatas.clear();
        mathcRecommends.clear();
        ArrayList<BannerInfo> bannerInfos = homeResponse.getData().getBanner();
        if (!MyStrUtil.isEmpty(bannerInfos)) {
            mLunBoDatas.addAll(bannerInfos);
        }

        ArrayList<MathcRecommend> match_recommend = homeResponse.getData().getMatch_recommend();
        if (!MyStrUtil.isEmpty(match_recommend)) {
            mathcRecommends.addAll(match_recommend);
        }
        mDatas.clear();
        ArrayList indexRec = homeResponse.getData().getIndexRec();
        addDataItems(indexRec);
    }


    public void addDataItems(ArrayList<BaseHomeItem> dataArray) {
        if (!MyStrUtil.isEmpty(dataArray)) {
            mDatas.addAll(dataArray);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType) {
            case TYPE_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_type1, null);
                viewHolder = new MyViewHolder1(view);
                break;
            case TYPE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item2, null);
                viewHolder = new AdverViewHolder(view);
                break;
            case TYPE_ZIXUNONPIC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation_type1, null);
                viewHolder = new HomeViewHolder(mContext, view);
                break;
            case TYPE_ZIXU_THREE_PIC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultation_item_type4, null);
                viewHolder = new HomeViewHolder(mContext, view);
                break;
            case TYPE_XIANGCE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albumnotitle, null);
                viewHolder = new HomeViewHolder(mContext, view);
                break;
            case TYPE_SAISHI:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_notitle, null);
                viewHolder = new HomeViewHolder(mContext, view);
                break;
            case TYPE_ZHIBO:
            case TYPE_DianBO:
            case TYPE_WAILIAN:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base, null);
                viewHolder = new HomeViewHolder(mContext, view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder1) {
            bindType1((MyViewHolder1) holder, position);
        } else if (holder instanceof AdverViewHolder) {
            bindType2((AdverViewHolder) holder, position);
        } else if (holder instanceof HomeViewHolder) {
            bindType3((HomeViewHolder) holder, position - mExtraCount);
        }
    }


    private void bindType1(MyViewHolder1 holder, int position) {
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
        if (!MyStrUtil.isEmpty(mLunBoDatas)) {
            holder.slideShowView.setImageUrls(mLunBoDatas);
            holder.slideShowView.startPlay();
        }
    }


    private void bindType2(final AdverViewHolder holder, int position) {
        holder.danceViewHolder.setText(R.id.type_tv, "热门推荐");
        if (null == mathcRecommends || mathcRecommends.size() == 0) {
            holder.danceViewHolder.setViewVisibility(R.id.item_layout, View.GONE);
        } else {
            holder.danceViewHolder.setViewVisibility(R.id.item_layout, View.VISIBLE);
            holder.viewSwitcher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = "";
                    switch (holder.mRadioGroup.getCheckedRadioButtonId()) {
                        case R.id.first_rb:
                            id = mathcRecommends.get(0).getId();
                            break;
                        case R.id.second_rb:
                            id = mathcRecommends.get(1).getId();
                            break;
                        case R.id.third_rb:
                            id = mathcRecommends.get(2).getId();
                            break;
                    }
                    MatchDetailActivity.lunch(mContext, id);
                }
            });
            holder.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                    MathcRecommend mathcRecommend = mathcRecommends.get(0);
                    holder.viewSwitcher.setInAnimation(mContext, R.anim.enter_bottom);
                    // 为ViewSwitcher的组件隐藏过程设置动画
                    holder.viewSwitcher.setOutAnimation(mContext, R.anim.leave_top);
                    View view = holder.viewSwitcher.getNextView();
                    ImageView match_icon = (ImageView) view.findViewById(R.id.match_icon);
                    TextView contentTv = (TextView) view.findViewById(R.id.content_tv);

                    if (checkedId == R.id.first_rb) {
                        mathcRecommend = mathcRecommends.get(0);
                        contentTv.setText(mathcRecommend.getTitle());
                    } else if (checkedId == R.id.second_rb) {
                        if (mathcRecommends.size() > 1) {
                            mathcRecommend = mathcRecommends.get(1);
                            contentTv.setText(mathcRecommend.getTitle());
                        }
                    } else {
                        if (mathcRecommends.size() > 2) {
                            mathcRecommend = mathcRecommends.get(2);
                            contentTv.setText(mathcRecommend.getTitle());
                        }
                    }
                    if (mathcRecommend != null) {
                        int imageResId;
                        switch (mathcRecommend.getType()) {
                            case 10001:
                                imageResId = R.drawable.wdsf_icon;
                                break;
                            case 10002:
                                imageResId = R.drawable.cdsf_icon;
                                break;
                            case 10003:
                                imageResId = R.drawable.addmathc_icon;
                                break;
                            default:
                                imageResId = R.drawable.addmathc_icon;

                                break;
                        }
                        match_icon.setImageResource(imageResId);
//                        ImageLoaderManager.getSingleton().Load(mContext,imageResId,match_icon);

                    }
                    holder.viewSwitcher.showNext();
                }
            });
            holder.downTimer.stopDown();
            holder.downTimer.startDown(1000 * 60 * 60 * 24, 2000);
        }
    }

    private void bindType3(HomeViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mDatas.get(position).getType()) {
                    case 10101://直播
                        ZhiBoDetailActivity.lunch(mContext, mDatas.get(position).getId());
                        break;
                    case 10102://点播
                        VideoDetailActivity.lunch(mContext, mDatas.get(position).getId());
                        break;
                    case 10103://赛事资讯
                        String saiShiurl = String.format(AppConfigs.ZixunShareUrl, String.valueOf(mDatas.get(position).getId()));
                        MyDanceWebActivity.lunch(mContext, MyDanceWebActivity.ZIXUNTYPE, mDatas.get(position).getCompete_name(), saiShiurl,true);
                        break;
                    case 10104://赛事相册
                        AlbumActivity.lunch(mContext, mDatas.get(position).getId(), mDatas.get(position).getCompete_name());
                        break;
                    case 10105://赛事
                        MatchDetailActivity.lunch(mContext, mDatas.get(position).getCompete_id());
                        break;
                    case 10106://外联
                        String url = mDatas.get(position).getUrl();
                        String wailianId = mDatas.get(position).getId();
                        if (!MyStrUtil.isEmpty(url)) {
                            MyDanceWebActivity.lunch(mContext, MyDanceWebActivity.OTHERTYPE, mDatas.get(position).getTitle(), url,wailianId);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        holder.setViewDatas(mDatas.get(position));
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        } else if (position == 1) {
            return TYPE_2;
        } else {
            BaseHomeItem homeItem = mDatas.get(position - mExtraCount);
            switch (homeItem.getType()) {
                case 10101://直播
                    return TYPE_ZHIBO;
                case 10102://点播
                    return TYPE_DianBO;
                case 10103://赛事资讯
                    if ("1".equals(homeItem.getImg_num())) {
                        return TYPE_ZIXUNONPIC;
                    } else {
                        return TYPE_ZIXU_THREE_PIC;
                    }
                case 10104://赛事相册
                    return TYPE_XIANGCE;
                case 10105://赛事
                    return TYPE_SAISHI;
                case 10106://外联
                    return TYPE_WAILIAN;
                default:
                    return TYPE_MAIN;

            }
        }
    }


    class AdverViewHolder extends BaseViewHolder {
        public DownTimer downTimer;
        public RadioGroup mRadioGroup;
        public RadioButton first_rb;
        public RadioButton second_rb;
        public RadioButton third_rb;
        public ViewSwitcher viewSwitcher;
        LayoutInflater inflater;

        public AdverViewHolder(View view) {
            super(mContext, view);
            inflater = LayoutInflater.from(mContext);

            first_rb = (RadioButton) itemView.findViewById(R.id.first_rb);
            second_rb = (RadioButton) itemView.findViewById(R.id.second_rb);
            third_rb = (RadioButton) itemView.findViewById(R.id.third_rb);
            viewSwitcher = (ViewSwitcher) itemView.findViewById(R.id.textswitcher);
            viewSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    return inflater.inflate(R.layout.home_tuijian_child, null, false);
                }
            });

            mRadioGroup = (RadioGroup) itemView.findViewById(R.id.title_rg);
            downTimer = new DownTimer();
            downTimer.setListener(new DownTimerListener() {
                @Override
                public void onTick(long millisUntilFinished) {
                    long index = millisUntilFinished / 1000;
                    NLog.i("AdverViewHolder", "index == " + index);
                    if (mRadioGroup.getCheckedRadioButtonId() == R.id.first_rb) {
                        mRadioGroup.check(R.id.second_rb);
                    } else if (mRadioGroup.getCheckedRadioButtonId() == R.id.second_rb) {
                        mRadioGroup.check(R.id.third_rb);
                    } else {
                        mRadioGroup.check(R.id.first_rb);
                    }
                    NLog.i("AdverViewHolder", "millisUntilFinished == " + millisUntilFinished);
                }

                @Override
                public void onFinish() {

                }
            });
        }

    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        public SlideShowView slideShowView;

        public MyViewHolder1(View itemView) {
            super(itemView);
            slideShowView = (SlideShowView) itemView.findViewById(R.id.slideShowView);
        }
    }

}
