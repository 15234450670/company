package mr.li.dance.ui.activitys.match;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.constant.StatusCode;
import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lecloud.skin.videoview.live.UIActionLiveVideoView;
import com.yolanda.nohttp.rest.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.Match;
import mr.li.dance.ui.activitys.MyDanceWebActivity;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.widget.VideoLayoutParams;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.ShareUtils;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 赛事详情页面
 * 修订历史:
 */
public class MatchDetailActivity extends BaseActivity implements View.OnClickListener {
    private String mMatchId;
    private String mMatchName;

    //    private String mGuicheng;//竞赛规则
//    private String mShexiang;//比赛设项
//    private String mSaiCheng;//比赛赛程
//    private String mVedioLiveId;
    private IMediaDataVideoView videoView;
    private long beginTime;
    LinkedHashMap<String, String> rateMap = new LinkedHashMap<String, String>();
    @Override
    public int getContentViewId() {
        return R.layout.activity_matchdetail;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mMatchId = mIntentExtras.getString("matchid");
    }

    @Override
    public void initDatas() {
        super.initDatas();
        Request<String> request = ParameterUtils.getSingleton().getmMatchDetailMap(mMatchId);
        request(AppConfigs.match_matchDetail, request, true);
    }

    @Override
    public void initViews() {
        setTitle("赛事详情");

        mDanceViewHolder.setClickListener(R.id.score_layout, this);
        mDanceViewHolder.setClickListener(R.id.matchvideo_layout, this);
        mDanceViewHolder.setClickListener(R.id.wonderfulpic_layout, this);
        mDanceViewHolder.setClickListener(R.id.guize_layout, this);
        mDanceViewHolder.setClickListener(R.id.shexiang_layout, this);
        mDanceViewHolder.setClickListener(R.id.saicheng_layout, this);

    }

    public static void lunch(Context context, String matchId) {
        Intent intent = new Intent(context, MatchDetailActivity.class);
        intent.putExtra("matchid", matchId);
        context.startActivity(intent);
    }


    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
        String dataStr = stringResponse.getData();
        Match match = JsonMananger.getReponseResult(dataStr, Match.class);
        mMatchName = match.getTitle();
        setDataToView(match);

    }

    private void setDataToView(Match match) {
        setRightImage(R.drawable.share_icon_001);
        setTitle(match.getTitle());
        String vedioLiveId = match.getVedio_live_id();
        mDanceViewHolder.setText(R.id.matchname_tv, match.getTitle());
        mDanceViewHolder.setText(R.id.address_tv, match.getAddress());
        if (!MyStrUtil.isEmpty(match.getStart_sign_up()) && !match.getStart_sign_up().startsWith("0")) {
            mDanceViewHolder.setText(R.id.signup_tv, match.getStart_sign_up() + "至" + match.getEnd_sign_up());
        } else {
            mDanceViewHolder.setText(R.id.signup_tv, "");
        }
        mDanceViewHolder.setText(R.id.matchtime_tv, match.getStart_time() + "至" + match.getEnd_time());

        if (MyStrUtil.isEmpty(vedioLiveId) || TextUtils.equals(vedioLiveId, "0")) {

            mDanceViewHolder.setImageByUrlOrFilePath(R.id.video_bg, match.getImg(), R.drawable.default_banner);
        }else{
            mDanceViewHolder.setViewVisibility(R.id.video_bg, View.INVISIBLE);
            startPlayVideo(match);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_layout:
                ScoreGroupActivity.lunch(this, mMatchId);
                break;
            case R.id.guize_layout:
                String guize_url = String.format(AppConfigs.SAISHIShareUrl, String.valueOf(mMatchId), String.valueOf(10701));
                MyDanceWebActivity.lunch(this, MyDanceWebActivity.MATCHOTHER1, "赛事规则", guize_url, true);
                break;
            case R.id.shexiang_layout:
                String shexiang_url = String.format(AppConfigs.SAISHIShareUrl, String.valueOf(mMatchId), String.valueOf(10702));
                MyDanceWebActivity.lunch(this, MyDanceWebActivity.MATCHOTHER2, "赛事设项", shexiang_url, true);
                break;
            case R.id.saicheng_layout:
                String saicheng_url = String.format(AppConfigs.SAISHIShareUrl, String.valueOf(mMatchId), String.valueOf(10703));
                MyDanceWebActivity.lunch(this, MyDanceWebActivity.MATCHOTHER3, "赛程表", saicheng_url, true);
                break;
            case R.id.matchvideo_layout:
                MatchVideoActivity.lunch(this, mMatchId);
                break;
            case R.id.wonderfulpic_layout:
                WonderfulActivity.lunch(this, mMatchId, mMatchName);
                break;
            case R.id.videoContainer:
//                if (!MyStrUtil.isEmpty(mVedioLiveId) && !TextUtils.equals(mVedioLiveId, "0")) {
//                    ZhiBoDetailActivity.lunch(this, mVedioLiveId);
//                }
                break;
        }
    }

    public void onHeadRightButtonClick(View v) {
        showShareDialog();
    }

    ShareUtils mShareUtils;

    private void showShareDialog() {

        if (mShareUtils == null) {
            mShareUtils = new ShareUtils(this);
        }
        String shareUrl = String.format(AppConfigs.SHAREGAME, mMatchId);
        String mShareContent = mMatchName;
        String countId = AppConfigs.CLICK_EVENT_22;
        mShareUtils.showShareDilaog(countId, shareUrl, mShareContent);
    }

    private void startPlayVideo(Match zhiBoInfo){
        videoView = new UIActionLiveVideoView(this);
        setActionLiveParameter(false);
        if (videoView instanceof UIActionLiveVideoView) {
            ((UIActionLiveVideoView) videoView).setVideoAutoPlay(true);
        }
        videoView.setVideoViewListener(mVideoViewListener);
        final RelativeLayout videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoContainer.addView((View) videoView, VideoLayoutParams.computeContainerSize(this, 16, 9));
        Bundle mBundle = new Bundle();
        mBundle.putInt(PlayerParams.KEY_PLAY_MODE, PlayerParams.VALUE_PLAYER_ACTION_LIVE);
        mBundle.putString(PlayerParams.KEY_PLAY_ACTIONID, zhiBoInfo.getActivity_id());
        mBundle.putBoolean(PlayerParams.KEY_PLAY_USEHLS, false);
        mBundle.putString(PlayerParams.KEY_CUID, "");
        mBundle.putString(PlayerParams.KEY_UTOKEN, "");
        mBundle.putString(PlayerParams.KEY_PLAY_CUSTOMERID, "");
        mBundle.putBoolean("pano", true);
        mBundle.putBoolean("hasSkin", true);
        videoView.setDataSource(mBundle);
    }

    VideoViewListener mVideoViewListener = new VideoViewListener() {
        @Override
        public void onStateResult(int event, Bundle bundle) {
            handleVideoInfoEvent(event, bundle);// 处理视频信息事件
            handlePlayerEvent(event, bundle);// 处理播放器事件
            handleLiveEvent(event, bundle);// 处理直播类事件,如果是点播，则这些事件不会回调
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


    /**
     * 处理直播类事件
     */
    private void handleLiveEvent(int state, Bundle bundle) {
    }

    /**
     * 处理视频信息类事件
     */
    private void handleVideoInfoEvent(int state, Bundle bundle) {
    }
    /**
     * 处理播放器本身事件，具体事件可以参见IPlayer类
     */
    private void handlePlayerEvent(int state, Bundle bundle) {
        switch (state) {
            case PlayerEvent.ACTION_LIVE_PLAY_PROTOCOL:
                setActionLiveParameter(bundle.getBoolean(PlayerParams.KEY_PLAY_USEHLS));
                break;
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
                    long startPlayTime = (System.currentTimeMillis() - beginTime);
                    float num = (float) startPlayTime / 1000;
                }
                break;
            default:
                break;
        }
    }
    private void setActionLiveParameter(boolean hls) {
        if (hls) {
            videoView.setCacheWatermark(1000, 100);
            videoView.setMaxDelayTime(50000);
            videoView.setCachePreSize(1000);
            videoView.setCacheMaxSize(40000);
        } else {
            //rtmp
            videoView.setCacheWatermark(500, 100);
            videoView.setMaxDelayTime(1000);
            videoView.setCachePreSize(200);
            videoView.setCacheMaxSize(10000);
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
}
