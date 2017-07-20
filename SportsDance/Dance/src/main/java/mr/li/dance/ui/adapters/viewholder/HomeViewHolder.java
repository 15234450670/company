package mr.li.dance.ui.adapters.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import mr.li.dance.R;
import mr.li.dance.models.BaseHomeItem;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DanceViewHolder;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.ShareUtils;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-推荐页 直播，热门视频，赛事咨询，精彩瞬间等item 的控制类，
 * 修订历史:
 */

public class HomeViewHolder extends RecyclerView.ViewHolder {
    DanceViewHolder danceViewHolder;
    Context mContext;
    ShareUtils shareUtils;

    public HomeViewHolder(Context context, View view) {
        super(view);
        shareUtils = new ShareUtils((Activity) context);
        mContext = context;
        danceViewHolder = new DanceViewHolder(mContext, view);
    }

    public void setViewDatas(final BaseHomeItem homeListItemInfo) {
        int type = homeListItemInfo.getType();
        danceViewHolder.setClickListener(R.id.share_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toShre(homeListItemInfo);
            }
        });
        switch (type) {
            case 10101://直播
                bindZhibo(homeListItemInfo);
                break;
            case 10102://点播
                bindVideo(homeListItemInfo);
                break;
            case 10103://赛事资讯
                bindInfomation(homeListItemInfo);
                break;
            case 10104://赛事相册
                bindImageInfo(homeListItemInfo);
                break;
            case 10105://赛事
                bindMatch(homeListItemInfo);
                break;
            case 10106://外联
                bindOther(homeListItemInfo);
                break;
            default:
                break;
        }

    }

    private void toShre(BaseHomeItem homeListItemInfo) {
        int type = homeListItemInfo.getType();
        String shareUrl = "";
        String mShareContent = "";
        String countID = "";
        switch (type) {
            case 10101://直播
                shareUrl = String.format(AppConfigs.SHARELIVE, homeListItemInfo.getId());
                mShareContent = homeListItemInfo.getCompete_name();
                countID = AppConfigs.CLICK_EVENT_19;
                break;
            case 10102://点播
                shareUrl = String.format(AppConfigs.SHAREMOV, homeListItemInfo.getId());
                mShareContent = homeListItemInfo.getCompete_name();
                countID = AppConfigs.CLICK_EVENT_18;
                break;
            case 10103://赛事资讯
                shareUrl = String.format(AppConfigs.ZixunShareUrl, String.valueOf(homeListItemInfo.getId()));
                mShareContent = homeListItemInfo.getTitle();
                countID = AppConfigs.CLICK_EVENT_21;
                break;
            case 10104://赛事相册
                shareUrl = String.format(AppConfigs.SHAREPHOTOURL, homeListItemInfo.getId());
                mShareContent = homeListItemInfo.getCompete_name();
                countID = AppConfigs.CLICK_EVENT_19;
                break;
            case 10105://赛事
                shareUrl = String.format(AppConfigs.SHAREGAME, homeListItemInfo.getId());
                mShareContent = homeListItemInfo.getCompete_name();
                countID = AppConfigs.CLICK_EVENT_22;
                break;
            case 10106://外联
                shareUrl = homeListItemInfo.getUrl();
                if (MyStrUtil.isEmpty(shareUrl)) {
                    return;
                }
                mShareContent = homeListItemInfo.getTitle();
                countID = AppConfigs.CLICK_EVENT_23;
                break;
            default:
                break;
        }
        shareUtils.showShareDilaog(countID, shareUrl, mShareContent);
    }


    private void bindZhibo(BaseHomeItem zhibo) {
        danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, zhibo.getPicture_app(), R.drawable.default_video);
        danceViewHolder.setText(R.id.name, zhibo.getCompete_name());
        danceViewHolder.setText(R.id.time_tv, zhibo.getStart_time());
        danceViewHolder.setViewVisibility(R.id.typeicon_tv, View.VISIBLE);
        danceViewHolder.setImageResDrawable(R.id.typeicon_tv, R.drawable.home_icon_008, R.drawable.home_icon_008);

        danceViewHolder.setViewVisibility(R.id.picnum_tv, View.INVISIBLE);
    }

    private void bindVideo(BaseHomeItem video) {
        danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, video.getPicture_app(), R.drawable.default_video);
        danceViewHolder.setText(R.id.name, video.getCompete_name());
        danceViewHolder.setText(R.id.time_tv, video.getStart_time());
        danceViewHolder.setViewVisibility(R.id.typeicon_tv, View.VISIBLE);
        danceViewHolder.setImageResDrawable(R.id.typeicon_tv, R.drawable.home_icon_005, R.drawable.home_icon_005);

        danceViewHolder.setViewVisibility(R.id.picnum_tv, View.INVISIBLE);
    }

    private void bindInfomation(BaseHomeItem baseHomeItem) {
        danceViewHolder.setText(R.id.name, baseHomeItem.getTitle());
        danceViewHolder.setText(R.id.time_tv, baseHomeItem.getStart_time());
        danceViewHolder.setViewVisibility(R.id.typeicon_tv, View.INVISIBLE);
        danceViewHolder.setText(R.id.from_tv, "来源: " + baseHomeItem.getWriter());
        danceViewHolder.setViewVisibility(R.id.picnum_tv, View.INVISIBLE);
        danceViewHolder.setViewVisibility(R.id.top_layout, View.GONE);
        if ("1".equals(baseHomeItem.getImg_num())) {

            danceViewHolder.getTextView(R.id.name).setTextColor(mContext.getResources().getColor(R.color.yeelow_color));
            danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, baseHomeItem.getPicture(), R.drawable.default_video);
        } else {
            danceViewHolder.setRoundImageByUrlOrFilePath(R.id.pic1_iv, baseHomeItem.getPicture(), R.drawable.default_video);
            danceViewHolder.setRoundImageByUrlOrFilePath(R.id.pic2_iv, baseHomeItem.getPicture_2(), R.drawable.default_video);
            danceViewHolder.setRoundImageByUrlOrFilePath(R.id.pic3_iv, baseHomeItem.getPicture_3(), R.drawable.default_video);
        }
    }

    private void bindImageInfo(BaseHomeItem albumInfo) {
        danceViewHolder.setViewVisibility(R.id.typeicon_tv, View.INVISIBLE);
        danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, albumInfo.getPicture_app(), R.drawable.default_video);
        danceViewHolder.setText(R.id.name, albumInfo.getCompete_name());
        danceViewHolder.setText(R.id.time_tv, albumInfo.getStart_time());
        danceViewHolder.setImageResDrawable(R.id.typeicon_tv, R.drawable.home_icon_005, R.drawable.home_icon_005);
        danceViewHolder.setText(R.id.type_tv, "精彩瞬间");
        danceViewHolder.setText(R.id.num_tv, "共" + albumInfo.getPhotos());
        danceViewHolder.setViewVisibility(R.id.picnum_tv, View.VISIBLE);
    }


    private void bindMatch(BaseHomeItem baseHomeItem) {
        danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, baseHomeItem.getPicture_app(), R.drawable.default_video);
        danceViewHolder.setText(R.id.name, baseHomeItem.getCompete_name());
        danceViewHolder.setText(R.id.starttime, "结束时间: " + baseHomeItem.getStart_time());
        danceViewHolder.setText(R.id.endtime, "结束时间: " + baseHomeItem.getEnd_time());
        switch (baseHomeItem.getCompete_type()) {
            case 10001:
                danceViewHolder.setText(R.id.level_tv, "WDSF");
                break;
            case 10002:
                danceViewHolder.setText(R.id.level_tv, "CDSF");
                break;
            case 10003:
                danceViewHolder.setText(R.id.level_tv, "地方赛事");
                break;
        }

    }

    private void bindOther(BaseHomeItem albumInfo) {
        danceViewHolder.setViewVisibility(R.id.typeicon_tv, View.INVISIBLE);
        danceViewHolder.setViewVisibility(R.id.picnum_tv, View.INVISIBLE);
        danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, albumInfo.getPicture_app(), R.drawable.default_video);
        danceViewHolder.setText(R.id.name, albumInfo.getTitle());
        danceViewHolder.setText(R.id.time_tv, albumInfo.getStart_time());
        String shareUrl = albumInfo.getUrl();
        if (MyStrUtil.isEmpty(shareUrl)) {
            danceViewHolder.setViewVisibility(R.id.share_layout, View.INVISIBLE);
        } else {
            danceViewHolder.setViewVisibility(R.id.share_layout, View.VISIBLE);
        }

    }
}
