package com.lecloud.skin.videoview.vod;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lecloud.sdk.api.ad.constant.ADPlayerParams;
import com.lecloud.sdk.api.md.entity.vod.VideoHolder;
import com.lecloud.sdk.config.LeCloudPlayerConfig;
import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.constant.StatusCode;
import com.lecloud.sdk.pano.base.BasePanoSurfaceView;
import com.lecloud.sdk.player.IAdPlayer;
import com.lecloud.sdk.player.IMediaDataPlayer;
import com.lecloud.sdk.player.IPlayer;
import com.lecloud.sdk.player.IVodPlayer;
import com.lecloud.sdk.utils.NetworkUtils;
import com.lecloud.sdk.videoview.vod.VodVideoView;
import com.lecloud.skin.R;
import com.lecloud.skin.ui.ILetvUICon;
import com.lecloud.skin.ui.ILetvVodUICon;
import com.lecloud.skin.ui.LetvVodUIListener;
import com.lecloud.skin.ui.impl.LetvVodUICon;
import com.lecloud.skin.ui.utils.ScreenUtils;
import com.lecloud.skin.ui.utils.timer.IChange;
import com.lecloud.skin.ui.utils.timer.LeTimerManager;
import com.lecloud.skin.ui.view.VideoNoticeView;
import com.letv.ads.bean.AdElementMime;
import com.letv.ads.constant.AdInfoConstant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UIVodVideoView extends VodVideoView {
    public static final String TAG = "UIVodVideoView";
    protected ILetvVodUICon letvVodUICon;
    protected LeTimerManager leTimerManager;
    TextView timeTextView;
    private long lastPosition;
    private LinkedHashMap<String, String> vtypeList;
    //是否正在seeking
    private boolean isSeeking = false;
    private static final int TRY_LOOK_END_MSG = 1;
    private boolean isNeedBuy;
    private boolean showLookEnd;

    MsgHandler msgHandler = new MsgHandler(this);
    private boolean mLoadingVideo;
    private ShowAdPicUtils mInstance;
    /**ssp start*/
    //    private AdStatusManager mAdStatusManager;
    /**
     * ssp end
     */
    FrameLayout mAdPauseView;
    private int mPortraitWidth;
    private int mPortraitHeight;
    private int mLandscapeWidth;
    private int mLandscapeHeight;

    static class MsgHandler extends Handler {
        WeakReference<UIVodVideoView> mReference;

        MsgHandler(UIVodVideoView viewWeakReference) {
            mReference = new WeakReference<UIVodVideoView>(viewWeakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            UIVodVideoView vodVideoView = mReference.get();
            if (vodVideoView == null) {
                return;
            }
            switch (msg.what) {
                case TRY_LOOK_END_MSG:
                    Toast.makeText(vodVideoView.context, "试看结束", Toast.LENGTH_LONG).show();
                    //调用下面方法，可以直接停止播放
                    vodVideoView.stopAndRelease();
                    vodVideoView.playCompletion();
                    vodVideoView.isNeedBuy = false;
                    vodVideoView.letvVodUICon.hideLoading();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public UIVodVideoView(Context context) {
        super(context);
        initUICon(context);
    }

    @Override
    public void setSurface(Surface surface) {
        if (!NetworkUtils.hasConnect(context)) {
            return;
        }
        player.setDisplay(surface);
    }

    @Override
    public void onSingleTapUp(MotionEvent e) {
        letvVodUICon.performClick();
    }

    @Override
    public void onNotSupport(int mode) {
        Toast.makeText(context, "not support current mode " + mode, Toast.LENGTH_LONG).show();
    }

    public LeTimerManager getLeTimerManager(long delaymillts) {
        if (leTimerManager == null) {
            leTimerManager = new LeTimerManager(new IChange() {

                @Override
                public void onChange() {

                    if (letvVodUICon != null && player != null) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                if (tryLookEnd()) {
                                    if (msgHandler != null && !showLookEnd) {
                                        showLookEnd = true;
                                        msgHandler.sendEmptyMessage(TRY_LOOK_END_MSG);
                                    }
                                }

                                letvVodUICon.setPlayState(player.isPlaying());
                                letvVodUICon.setDuration(player.getDuration());
                                if (!isSeeking && player.getCurrentPosition() <= player.getDuration()) {
                                    if (player.getCurrentPosition() == 0) {
                                        letvVodUICon.setCurrentPosition(lastPosition);
                                    } else {
                                        letvVodUICon.setCurrentPosition(player.getCurrentPosition());
                                    }
                                }
                            }
                        });
                    }
                }
            }, delaymillts);
        }
        return leTimerManager;
    }

    //是否是试看视频
    private boolean isNeedBuy() {
        if (getNeedbuy() == 1 && getTryLookTime() > 0) {
            return true;
        }
        return false;
    }

    //是否试看结束
    private boolean tryLookEnd() {
        long pos = player.getCurrentPosition();
        if (isNeedBuy() && (pos / 1000 >= getTryLookTime())) {
            return true;
        }
        return false;
    }

    private void stopTimer() {
        if (leTimerManager != null) {
            leTimerManager.stop();
            leTimerManager = null;
        }
    }

    private void startTimer() {
        if (leTimerManager == null) {
            getLeTimerManager(500);
        }
        if (leTimerManager != null) {
            leTimerManager.start();
        }
    }

    protected void setEnableSensor(ILetvVodUICon letvVodUICon) {

    }

    private void initUICon(final Context context) {
        letvVodUICon = new LetvVodUICon(context);
        letvVodUICon.setPlayState(false);
        letvVodUICon.showLoadingAnimation();
        setEnableSensor(letvVodUICon);
//        letvVodUICon.setGravitySensor(false);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(letvVodUICon.getView(), params);
        letvVodUICon.setRePlayListener(new VideoNoticeView.IReplayListener() {

            @Override
            public Bundle getReportParams() {
                return ((IMediaDataPlayer) player).getReportParams();
            }

            @Override
            public void onRePlay() {
                if (!mVideoAutoPlay) {
                    mVideoAutoPlay = true;
                    setVideoAutoPlay(mVideoAutoPlay);
                }
                setLastPostion();
                player.retry();
            }
        });
        letvVodUICon.setLetvVodUIListener(new LetvVodUIListener() {

            @Override
            public void setRequestedOrientation(int requestedOrientation) {
                if (context instanceof Activity) {
                    ((Activity) context).setRequestedOrientation(requestedOrientation);
                }
            }

            @Override
            public void resetPlay() {
                // LeLog.dPrint(TAG, "--------resetPlay");
            }

            @Override
            public void onSetDefination(int positon) {
                stopTimer();
                letvVodUICon.showLoadingProgress();
                if (vtypeList != null && vtypeList.size() > 0) {
                    dismissAdPic();
                    setLastPostion();
                    ((IMediaDataPlayer) player).setDataSourceByRate(new ArrayList<String>(vtypeList.keySet()).get(positon));
                }
            }

            @Override
            public void onSeekTo(float sec) {
                long msec = (long) Math.floor((sec * player.getDuration()));
                if (player != null) {
                    dismissAdPic();
                    player.seekTo(msec);
                    if (isComplete()) {
                        player.retry();
                    } else if (!player.isPlaying()) {
                        player.start();
                    }
                    ((LetvVodUICon) letvVodUICon).syncSeekProgress((int) msec);
                }
            }

            @Override
            public void onClickPlay() {
                if (!mVideoAutoPlay) {
                    if (player instanceof IVodPlayer) {
                        ((IVodPlayer) player).prepareVideoPlay();
                    }
                    mVideoAutoPlay = true;
                    setVideoAutoPlay(mVideoAutoPlay);
                }
                if (player.isPlaying()) {
                    if (mLoadingVideo) {
                        return;
                    }
                    mVideoPlaying = false;
                    player.pause();
                    releaseAudioFocus();
                    if (LeCloudPlayerConfig.getInstance().getAdType().equals("ssp")) {
                        ((IVodPlayer) player).requestAdData(AdInfoConstant.AdZoneType.PAUSE + "");
                    }
                } else if (isComplete() || showLookEnd) {
                    dismissAdPic();
                    player.retry();
                    showLookEnd = false;
                } else {
					mVideoPlaying = true;
                    requestAudioFocus();
                    player.resume();
                    dismissAdPic();
                }
            }

            @Override
            public void onUIEvent(int event, Bundle bundle) {
                // TODO Auto-generated method stub

            }

            @Override
            public int onSwitchPanoControllMode(int controllMode) {
                if (mPanoEvent != null) {
                    return mPanoEvent.switchControllMode(controllMode);
                }
                return BasePanoSurfaceView.CONTROLL_MODE_TOUCH;
            }

            @Override
            public int onSwitchPanoDisplayMode(int displayMode) {
                if (mPanoEvent != null) {
                    return mPanoEvent.switchDisplayMode(displayMode);
                }
                return BasePanoSurfaceView.DISPLAY_MODE_NORMAL;
            }

            @Override
            public void onProgressChanged(int progress) {
                // TODO Auto-generated method stub
//				((LetvVodUICon) letvVodUICon).syncSeekProgress(progress);
            }

            @Override
            public void onStartSeek() {
                // TODO Auto-generated method stub
                isSeeking = true;
            }

            @Override
            public void onEndSeek() {
                isSeeking = false;
            }

        });
        letvVodUICon.setVrDisplayMode(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE) {
            mPortraitWidth = h;
            mPortraitHeight = h * 9 / 16;
            mLandscapeWidth = w;
            mLandscapeHeight = h;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (letvVodUICon != null && letvVodUICon.isLockScreen()) {
            if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
                getLayoutParams().width = mPortraitWidth;
                getLayoutParams().height = mPortraitHeight;
            } else {
                getLayoutParams().width = mLandscapeWidth;
                getLayoutParams().height = mLandscapeHeight;
            }
            return;
        }
        if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
            letvVodUICon.setRequestedOrientation(ILetvUICon.SCREEN_PORTRAIT, UIVodVideoView.this);
        } else {
            letvVodUICon.setRequestedOrientation(ILetvUICon.SCREEN_LANDSCAPE, UIVodVideoView.this);
        }
        if (hasPanoView()) {
            letvVodUICon.setVrDisplayMode(false);
            letvVodUICon.isOpenGyroMode(false);
        }
        super.onConfigurationChanged(newConfig);
        if (mAdPauseView != null && mAdPauseView.isShown()) {
            changeAdViewSize();
        }
    }


    @Override
    protected void onInterceptVodMediaDataSuccess(int event, Bundle bundle) {
        super.onInterceptVodMediaDataSuccess(event, bundle);
        VideoHolder videoHolder = bundle.getParcelable(PlayerParams.KEY_RESULT_DATA);
        vtypeList = videoHolder.getVtypes();
        String title = videoHolder.getTitle();
        if (!TextUtils.isEmpty(title)) {
            letvVodUICon.setTitle(title);
        }
        String currentDefiniton = vtypeList.get(onInterceptSelectDefiniton(vtypeList, videoHolder.getDefaultVtype()));
        List<String> ratetypes = new ArrayList<String>(videoHolder.getVtypes().values());
        letvVodUICon.setRateTypeItems(ratetypes, currentDefiniton);
        letvVodUICon.addWaterMark(((VideoHolder) bundle.getParcelable(PlayerParams.KEY_RESULT_DATA)).getCoverConfig());
        letvVodUICon.showLoadingPic(((VideoHolder) bundle.getParcelable(PlayerParams.KEY_RESULT_DATA)).getCoverConfig());
    }

    @Override
    protected void onInterceptMediaDataError(int event, Bundle bundle) {
        super.onInterceptMediaDataError(event, bundle);
        letvVodUICon.hideLoading();
        letvVodUICon.hideWaterMark();
        letvVodUICon.processMediaState(event, bundle);
    }

    private void playCompletion() {
        //btn pause
        letvVodUICon.setPlayState(false);
        //update progress
        if (letvVodUICon != null && player != null) {
            letvVodUICon.setDuration(player.getDuration());
            letvVodUICon.setCurrentPosition(player.getCurrentPosition());
        }
        lastPosition = 0;
        stopTimer();
    }

    @Override
    protected void prepareVideoSurface() {
        if (hasPanoView() && mPanoEvent != null) {
            surfaceView = mPanoEvent.getPanoVideoView();
            mPanoEvent.setPlayerPropertyListener();
            setVideoView(surfaceView);
            mPanoEvent.init();
            letvVodUICon.canGesture(false);
            letvVodUICon.isPano(true);
            ((LetvVodUICon) letvVodUICon).setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ((BasePanoSurfaceView) surfaceView).onPanoTouch(v, event);
                    return true;
                }
            });
        } else {
            super.prepareVideoSurface();
        }
    }

    @Override
    protected void notifyPlayerEvent(int event, Bundle bundle) {
        super.notifyPlayerEvent(event, bundle);
        letvVodUICon.processPlayerState(event, bundle);
        switch (event) {
            case PlayerEvent.PLAY_COMPLETION://202
                showLookEnd = false;
                isNeedBuy = false;
                mLoadingVideo = false;
                mVideoPlaying = false;
                playCompletion();
                break;
            case PlayerEvent.PLAY_INFO:
                int code = bundle.getInt(PlayerParams.KEY_RESULT_STATUS_CODE);
                if (code == StatusCode.PLAY_INFO_VIDEO_RENDERING_START) {
                    startTimer();
                }

                switch (code) {
                    case StatusCode.PLAY_INFO_BUFFERING_START://500004
                        if (NetworkUtils.hasConnect(context) && !letvVodUICon.isShowLoading()) {
                            letvVodUICon.showLoadingProgress();
                        }
                        mLoadingVideo = true;
                        break;
                    case StatusCode.PLAY_INFO_BUFFERING_END://500005
                        letvVodUICon.hideLoading();
                        mLoadingVideo = false;
                        break;
                    case StatusCode.PLAY_INFO_VIDEO_RENDERING_START://500006
                        mVideoPlaying = true;
                        letvVodUICon.showWaterMark();
                        letvVodUICon.hideLoading();
                        mLoadingVideo = false;
                        if (isNeedBuy() && !isNeedBuy) {
                            isNeedBuy = true;
                            Toast.makeText(context, "该片为试看片,试看时长" + getTryLookTime() + "秒..", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case PlayerEvent.PLAY_PREPARED: {
                letvVodUICon.setPlayState(true);
                if (NetworkUtils.hasConnect(context) && !letvVodUICon.isShowLoading()) {
                    letvVodUICon.showLoadingProgress();
                }
                break;
            }
            case PlayerEvent.PLAY_INIT:
                if (lastPosition > 0) {
                    player.seekToLastPostion(lastPosition);
                    letvVodUICon.setDuration(player.getDuration());
                    letvVodUICon.setCurrentPosition(lastPosition);
                }
                break;
            case PlayerEvent.PLAY_SEEK_COMPLETE: {//209
                setLastPostion();
                isSeeking = false;
                break;
            }
            case PlayerEvent.PLAY_ERROR://205
                removeView(timeTextView);
                letvVodUICon.getView().setVisibility(VISIBLE);
                letvVodUICon.hideLoading();
                letvVodUICon.hideWaterMark();
                break;
            default:
                break;
        }
    }


    private void showAdPic(AdElementMime adElementMime) {
        /**ssp start*/
//        mInstance = new ShowAdPicUtils(context.getApplicationContext());
//        if (adElementMime != null && !TextUtils.isEmpty(adElementMime.mediaFileUrl)) {
//            mAdStatusManager = new AdStatusManager(context.getApplicationContext(), adElementMime);
//            mInstance.setmAdPicStatusListener(adPicStatusListener);
//        mInstance.setmVideoPauseListener(mVideoPauseListener);
//            mAdPauseView = mInstance.showAdPic(adElementMime.mediaFileUrl);
//            if (!mAdPauseView.isShown() && videoPause()) {
//                removeView(mAdPauseView);
//                addView(mAdPauseView);
//                bringChildToFront(mAdPauseView);
//            }
//        }
        /**ssp end*/
    }

    ShowAdPicUtils.VideoPauseListener mVideoPauseListener = new ShowAdPicUtils.VideoPauseListener() {
        @Override
        public boolean isVideoPause() {
            return videoPause();
        }
    };

    private boolean videoPause() {
        return !(player != null && player.isPlaying());
    }

    ShowAdPicUtils.AdPicStatusListener adPicStatusListener = new ShowAdPicUtils.AdPicStatusListener() {
        @Override
        public void onAdPicClicked() {
//            if (mAdStatusManager != null) {
//                mAdStatusManager.onAdClicked();
//            }
        }

        @Override
        public void onAdPicClosed() {
//            if (mAdStatusManager != null) {
//                mAdStatusManager.onAdClosedClicked();
//            }
        }

        @Override
        public void onAdPicStarted() {
//            if (mAdStatusManager != null) {
//                mAdStatusManager.onAdPlayStarted();
//            }
        }
    };

    private void dismissAdPic() {
        if (mAdPauseView != null && mAdPauseView.isShown()) {
            removeView(mAdPauseView);
            mAdPauseView = null;
        }
        if (mInstance != null) {
            mInstance.closeAdPic();
        }
    }

    private void changeAdViewSize() {
        if (mInstance != null) {
            mInstance.changeAdPicSize();
        }
    }

    @TargetApi(11)
    @Override
    protected void onInterceptAdEvent(int code, Bundle bundle) {

        switch (code) {
            case PlayerEvent.AD_ELEMENT_RESULT:
                if (bundle != null && videoPause()) {
                    AdElementMime mAdElementMime = (AdElementMime) bundle.getSerializable(ADPlayerParams.KEY_AD_ELEMENTS);
                    showAdPic(mAdElementMime);
                }
                break;
            case PlayerEvent.AD_START:
                letvVodUICon.processPlayerState(code, bundle);
                letvVodUICon.getView().setVisibility(GONE);
                letvVodUICon.hideLoading();
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                if (timeTextView == null) {
                    timeTextView = new TextView(context);
                    timeTextView.setBackgroundColor(Color.BLACK);
                    timeTextView.setAlpha(0.7f);
                    timeTextView.setTextColor(Color.WHITE);
                    timeTextView.setPadding(20, 20, 20, 20);
                }
                if (!timeTextView.isShown()) {
                    removeView(timeTextView);//添加前先移除
                    addView(timeTextView, lp);
                    bringChildToFront(timeTextView);
                }
                break;
            case PlayerEvent.AD_BUFFERING_START:
                if (!NetworkUtils.hasConnect(context)) {
                    letvVodUICon.processPlayerState(code, bundle);
                    letvVodUICon.getView().setVisibility(VISIBLE);
                }
                break;
            case PlayerEvent.AD_BUFFERING_END:
                letvVodUICon.getView().setVisibility(GONE);
                letvVodUICon.processPlayerState(code, bundle);
                break;
            case PlayerEvent.AD_ERROR:
                if (!NetworkUtils.hasConnect(context)) {
                    letvVodUICon.processPlayerState(code, bundle);
                }
            case IAdPlayer.AD_PLAY_ERROR:
            case PlayerEvent.AD_COMPLETE:
                removeView(timeTextView);
                letvVodUICon.getView().setVisibility(VISIBLE);
//                by heyuekuai 2016/09/19 fix bug ad play complete not show loading pic
//                letvVodUICon.hideLoading();
//                letvVodUICon.hideWaterMark();
                break;
            case PlayerEvent.AD_PROGRESS:
                timeTextView.setText(getContext().getResources().getString(R.string.ad) + bundle.getInt(IAdPlayer.AD_TIME) + "s");
                break;
            default:
                break;
        }
        super.onInterceptAdEvent(code, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
        isNeedBuy = false;
        showLookEnd = false;
        dismissAdPic();
    }

    @Override
    public void onPause() {
        super.onPause();
        setLastPostion();
        letvVodUICon.hideWaterMark();
    }

    @Override
    public void setDataSource(Bundle bundle) {
        super.setDataSource(bundle);
        setVideoOrientation();
    }

    @Override
    public void setDataSource(String playUrl) {
        if (isPanorama) {
            mIsPanoView = "1";
        }
        setVideoOrientation();
        super.setDataSource(playUrl);
    }

    private void setVideoOrientation() {
        if (isFirstPlay) {
            int orientation = letvVodUICon.getRequestedOrientation();
            if (orientation == 0) {
                letvVodUICon.setRequestedOrientation(ILetvUICon.SCREEN_LANDSCAPE, this);
            } else {
                letvVodUICon.setRequestedOrientation(ILetvUICon.SCREEN_PORTRAIT, this);
            }
            isFirstPlay = false;
        }
    }

    @Override
    public void onResume() {
        dismissAdPic();
        super.onResume();
    }

    public boolean isComplete() {
        //TODO
        if (player != null) {
            return (player.getStatus() == IPlayer.PLAYER_STATUS_EOS) || (player.getStatus() == -1);
        }
        return false;
    }

    private void setLastPostion() {
        if (player == null || player.getCurrentPosition() == 0) {
            return;
        }
        lastPosition = player.getCurrentPosition();
    }

    @Override
    public void resetPlayer() {
        super.resetPlayer();
        isNeedBuy = false;
        showLookEnd = false;
        isFirstPlay = true;
        stopTimer();
        if (timeTextView != null) {
            timeTextView.setText("");
            removeView(timeTextView);
        }
        lastPosition = 0;
        vtypeList = null;
        isSeeking = false;
    }
}
