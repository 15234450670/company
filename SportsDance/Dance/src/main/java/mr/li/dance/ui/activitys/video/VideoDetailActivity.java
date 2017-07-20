package mr.li.dance.ui.activitys.video;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.constant.StatusCode;
import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lecloud.skin.videoview.vod.UIVodVideoView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yolanda.nohttp.rest.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.https.response.VideoDetailResponse;
import mr.li.dance.models.BaseItemAdapterType;
import mr.li.dance.models.QuickZhiboInfo;
import mr.li.dance.models.Video;
import mr.li.dance.ui.activitys.LoginActivity;
import mr.li.dance.ui.activitys.album.AlbumActivity;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.activitys.mine.MyCollectActivity;
import mr.li.dance.ui.adapters.BaseItemAdapter;
import mr.li.dance.ui.dialogs.ShareDialog;
import mr.li.dance.ui.widget.VideoLayoutParams;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.ShareUtils;
import mr.li.dance.utils.UserInfoManager;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 视频详情页面
 * 修订历史:
 */

public class VideoDetailActivity extends BaseListActivity {
    BaseItemAdapter mAdapter;
    private IMediaDataVideoView videoView;
    private String mItemId;
    boolean isCollected;
    boolean isFromCollectpage = false;
    private String shareUrl;
    private String mShareContent;
    LinkedHashMap<String, String> rateMap = new LinkedHashMap<String, String>();
    VideoViewListener mVideoViewListener = new VideoViewListener() {


        @Override
        public void onStateResult(int event, Bundle bundle) {
            handleVideoInfoEvent(event, bundle);// 处理视频信息事件
            handlePlayerEvent(event, bundle);// 处理播放器事件
        }

        @Override
        public String onGetVideoRateList(LinkedHashMap<String, String> map) {
            rateMap = map;
            for (Map.Entry<String, String> rates : map.entrySet()) {
                if (rates.getValue().equals("高清")) {
                    return rates.getKey();
                }
            }
            return "";
        }
    };


    @Override
    public void itemClick(int position, Object value) {
        Video currentInfo = (Video) value;
        mItemId = currentInfo.getId();
        initDatas();
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new BaseItemAdapter(mContext, BaseItemAdapterType.CommentType);
        mAdapter.setItemClickListener(this);
        return mAdapter;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_videodetail;
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("视频详情");

        mDanceViewHolder.setText(R.id.type_tv, "赛事相关视频");
        mRefreshLayout.setEnableLoadmore(false);
        videoView = new UIVodVideoView(this);
        ((UIVodVideoView) videoView).setVideoAutoPlay(true);
        videoView.setVideoViewListener(mVideoViewListener);
        final RelativeLayout videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoContainer.addView((View) videoView, VideoLayoutParams.computeContainerSize(this, 16, 9));
    }

    @Override
    public void initDatas() {
        super.initDatas();
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = ParameterUtils.getSingleton().getVideoDetailMap(userId, mItemId);
        request(AppConfigs.home_dianboDetailL, request, true);
    }

    private void setVideoDetail(Video video) {
        setRightImage(R.drawable.collect_icon, R.drawable.share_icon_001);
        videoView.resetPlayer();
        mDanceViewHolder.setText(R.id.matchname_tv, video.getName());
        String Compete_name = video.getCompete_name();
        mShareContent = video.getName();
        if (MyStrUtil.isEmpty(Compete_name)) {
            mDanceViewHolder.setViewVisibility(R.id.brief_tv, View.GONE);
        } else {
            mDanceViewHolder.setText(R.id.brief_tv, video.getCompete_name());
        }
        mDanceViewHolder.setText(R.id.type_tv, "赛事相关视频");
        Bundle mBundle = new Bundle();
        mBundle.putString(PlayerParams.KEY_PLAY_UUID, AppConfigs.KEY_PLAY_UUID);
        mBundle.putString(PlayerParams.KEY_PLAY_VUID, video.getVideo_unique());
        mBundle.putString(PlayerParams.KEY_PLAY_PU, AppConfigs.KEY_PLAY_PU);
        shareUrl = String.format(AppConfigs.SHAREMOV, mItemId);
        videoView.setPanorama(true);
        videoView.setDataSource(mBundle);
    }


    @Override
    public void getIntentData() {
        super.getIntentData();
        mItemId = mIntentExtras.getString("itemid");
        isFromCollectpage = mIntentExtras.getBoolean("isfromcollectpage");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.onDestroy();
            videoView.setVideoViewListener(null);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
            setHeadVisibility(View.GONE);
        } else {
            setHeadVisibility(View.VISIBLE);
        }
        if (videoView != null) {
            videoView.onConfigurationChanged(newConfig);
        }
    }

    /**
     * 处理播放器本身事件，具体事件可以参见IPlayer类
     */
    private void handlePlayerEvent(int state, Bundle bundle) {
        switch (state) {
            case PlayerEvent.PLAY_VIDEOSIZE_CHANGED:
                /**
                 * 获取到视频的宽高的时候，此时可以通过视频的宽高计算出比例，进而设置视频view的显示大小。
                 * 如果不按照视频的比例进行显示的话，(以surfaceView为例子)内容会填充整个surfaceView。
                 * 意味着你的surfaceView显示的内容有可能是拉伸的
                 */
                break;

            case PlayerEvent.PLAY_PREPARED:
                // 播放器准备完成，此刻调用start()就可以进行播放了
                if (videoView != null) {
                    videoView.onStart();
                }
                break;
            case PlayerEvent.PLAY_INFO:
                int code = bundle.getInt(PlayerParams.KEY_RESULT_STATUS_CODE);
                if (code == StatusCode.PLAY_INFO_VIDEO_RENDERING_START) {
                    //播放第一帧
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理视频信息类事件
     */
    private void handleVideoInfoEvent(int state, Bundle bundle) {
    }


    @Override
    public void onSucceed(int what, String responseStr) {
        super.onSucceed(what, responseStr);
        if (AppConfigs.home_dianboDetailL == what) {
            VideoDetailResponse detailResponse = JsonMananger.getReponseResult(responseStr, VideoDetailResponse.class);
            isCollected = (0 != detailResponse.getData().getCollection_id());
            mAdapter.refresh(detailResponse.getData().getOtherList());
            setVideoDetail(detailResponse.getData().getDetail());
        } else {
            StringResponse stringResponse = JsonMananger.getReponseResult(responseStr, StringResponse.class);
            NToast.shortToast(this, stringResponse.getData());
            isCollected = !isCollected;
        }
        if (isCollected) {
            mRightIv.setImageResource(R.drawable.collect_icon_002);
        } else {
            mRightIv.setImageResource(R.drawable.collect_icon);
        }
    }

    public static void lunch(Context context, String id) {
        lunch(context, id, false);

    }

    public static void lunch(Context context, String id, boolean isfromCollectPage) {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("itemid", id);
        intent.putExtra("isfromcollectpage", isfromCollectPage);
        context.startActivity(intent);
    }

    @Override
    public void refresh() {
        super.refresh();
        initDatas();
    }

    @Override
    public void onHeadRightButtonClick(View v) {
        if (!UserInfoManager.getSingleton().isLoading(this)) {
            LoginActivity.lunch(this, 0x001);
        } else {
            String userId = UserInfoManager.getSingleton().getUserId(this);
            int operation = isCollected ? 1 : 2;
            Request<String> request = ParameterUtils.getSingleton().getCollectionMap(userId, mItemId, 10602, operation);
            request(AppConfigs.user_collection, request, false);
        }
    }

    @Override
    public void onBackPressed() {
        if (isFromCollectpage && !isCollected) {
            MyCollectActivity.lunch(this, true, mItemId);
            finish();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onHeadLeftButtonClick(View v) {
        onBackPressed();
    }

    public void onHeadRightButtonClick2(View v) {
        showShareDialog();
    }

    ShareUtils mShareUtils;

    private void showShareDialog() {

        if (mShareUtils == null) {
            mShareUtils = new ShareUtils(this);
        }
        mShareUtils.showShareDilaog(AppConfigs.CLICK_EVENT_18, shareUrl, mShareContent);
    }


}
