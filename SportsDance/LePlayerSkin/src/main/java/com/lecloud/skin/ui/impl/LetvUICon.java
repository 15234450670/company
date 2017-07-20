package com.lecloud.skin.ui.impl;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lecloud.sdk.api.md.entity.action.CoverConfig;
import com.lecloud.skin.ui.ILetvUICon;
import com.lecloud.skin.ui.LetvUIListener;
import com.lecloud.skin.ui.base.BaseMediaController;
import com.lecloud.skin.ui.orientation.OrientationSensorUtils;
import com.lecloud.skin.ui.utils.GestureControl;
import com.lecloud.skin.ui.utils.ScreenUtils;
import com.lecloud.skin.ui.view.V4TopTitleView;
import com.lecloud.skin.ui.view.VideoLoading;
import com.lecloud.skin.ui.view.VideoNoticeView;
import com.lecloud.skin.ui.view.WaterMarkView;

import java.lang.ref.WeakReference;
import java.util.List;

public class LetvUICon extends FrameLayout implements ILetvUICon {
//	public LetvUIListener letvUIListener;

    /**
     * lockFlag 为false 表示不加锁
     */
    protected boolean lockFlag = false;
    protected ImageView videoLock;
    private static final long DELAY_HIDE = 8 * 1000;
    protected RelativeLayout rlSkin;
    protected boolean mRlSkinHide = false;
    private boolean mLandScape = false;
    protected BaseMediaController mLargeMediaController;//V4LargeMediaController
    protected BaseMediaController mSmallMediaController;//V4LargeMediaController
    protected V4TopTitleView mV4TopTitleView;//V4LargeMediaController
    protected GestureControl mGestureControl;
    protected boolean canGesture = true;
    protected OrientationSensorUtils mOrientationSensorUtils;
    protected LetvUIListener mLetvUIListener;
    protected WaterMarkView waterMarkView;
    protected VideoLoading mVideoLoading;
    protected VideoNoticeView mNoticeView;
    //是否开启重力感应
    private boolean mUseGravitySensor = true;
    //布局容器的宽和高
    private int width = -1;
    private int height = -1;

    private int mRequestedOrientation = -1;

    static class MyHandler extends Handler {
        WeakReference<LetvUICon> mReference;

        MyHandler(LetvUICon uiCon) {
            mReference = new WeakReference<>(uiCon);
        }

        @Override
        public void handleMessage(Message msg) {
            LetvUICon uiCon = mReference.get();
            if (uiCon == null) {
                return;
            }
            if (uiCon.lockFlag) {
                return;
            }
            switch (msg.what) {
                case OrientationSensorUtils.ORIENTATION_8:// 反横屏
                    if (uiCon.mLetvUIListener != null) {
                        uiCon.mLetvUIListener.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    }
                    break;
                case OrientationSensorUtils.ORIENTATION_9:// 反竖屏
                    if (uiCon.mLetvUIListener != null) {
                        uiCon.mLetvUIListener.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    }
                    break;
                case OrientationSensorUtils.ORIENTATION_0:// 正横屏
                    if (uiCon.mLetvUIListener != null) {
                        uiCon.mLetvUIListener.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    break;
                case OrientationSensorUtils.ORIENTATION_1:// 正竖屏
                    if (uiCon.mLetvUIListener != null) {
                        uiCon.mLetvUIListener.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    MyHandler mHandler = new MyHandler(this);
    HideHandler mHideHandler = new HideHandler(this);

    static class HideHandler extends Handler {
        WeakReference<LetvUICon> mReference;

        HideHandler(LetvUICon con) {
            mReference = new WeakReference<LetvUICon>(con);
        }

        @Override
        public void handleMessage(Message msg) {
            LetvUICon uiCon = mReference.get();
            if (uiCon == null) {
                return;
            }
            uiCon.hide();
            super.handleMessage(msg);
        }
    }

    //    private Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            hide();
//        }
//    };
    private boolean isLive;

    public LetvUICon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        show();
    }

    public LetvUICon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        show();
    }

    public LetvUICon(Context context) {
        super(context);
        init(context);
        show();
    }

    protected void initWaterMarkView(Context context) {
        waterMarkView = new WaterMarkView(context);
        addView(waterMarkView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    protected void initVideoLoadingView(Context context) {
        mVideoLoading = new VideoLoading(context);
        addView(mVideoLoading, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    protected void initVideoNoticeView(Context context) {
        mNoticeView = new VideoNoticeView(context);
        mNoticeView.setIsLive(isLive);
        addView(mNoticeView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mNoticeView.setVisibility(View.GONE);
    }

    protected void setIsLive(boolean isLive) {
        this.isLive = isLive;
        if (mNoticeView != null) {
            mNoticeView.setIsLive(isLive);
        }
    }

    protected void init(Context context) {
        initWaterMarkView(context);
        initVideoLoadingView(context);
        initVideoNoticeView(context);
    }

    @Override
    protected void onAttachedToWindow() {
        if (mOrientationSensorUtils == null) {
            mOrientationSensorUtils = new OrientationSensorUtils((Activity) getContext(), mHandler);
            mLargeMediaController.setOrientationSensorUtils(mOrientationSensorUtils);
            mSmallMediaController.setOrientationSensorUtils(mOrientationSensorUtils);
            mV4TopTitleView.setOrientationSensorUtils(mOrientationSensorUtils);
        }
        if (!mUseGravitySensor) {
            return;
        }
        mOrientationSensorUtils.onResume();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mOrientationSensorUtils != null && mUseGravitySensor) {
            mOrientationSensorUtils.onPause();
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    @Override
    public void setPlayState(boolean isPlayState) {
        if (mLargeMediaController != null) {
            mLargeMediaController.setPlayState(isPlayState);
        }
        if (mSmallMediaController != null) {
            mSmallMediaController.setPlayState(isPlayState);
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation, View view) {
        //如果锁屏,不能切换横竖屏
        if (lockFlag) {
            return;
        }
        if (width == -1 || height == -1) {
            width = view.getLayoutParams().width;
            height = view.getLayoutParams().height;
        }
        mRequestedOrientation = requestedOrientation;
        if (requestedOrientation == ILetvUICon.SCREEN_LANDSCAPE) {
            changeScreenLandscape(view);
        } else {
            changeScreenPortrait(view);
        }
//		canGesture(canGesture);
    }

    @Override
    public int getRequestedOrientation() {
        return mRequestedOrientation;
    }

    @Override
    public void changeScreenLandscape(View view) {
        mLandScape = true;
        view.getLayoutParams().height = ScreenUtils.getHeight(getContext());
        view.getLayoutParams().width = ScreenUtils.getWidth(getContext()) + ScreenUtils.getNavigationBarHeight(getContext());
        ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mLargeMediaController.setVisibility(VISIBLE);
        mV4TopTitleView.setVisibility(VISIBLE);
        mV4TopTitleView.showTopRightView(true);
        videoLock.setVisibility(VISIBLE);
        mSmallMediaController.setVisibility(GONE);
        ScreenUtils.showFullScreen((Activity) getContext(), true);
        attachGestureController(this, false);
    }


    public void changeScreenPortrait(View view) {
        mLandScape = false;
        ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view.getLayoutParams().height = height;
        view.getLayoutParams().width = width;
        mLargeMediaController.setVisibility(GONE);
        mV4TopTitleView.setVisibility(VISIBLE);
        mV4TopTitleView.showTopRightView(false);
        videoLock.setVisibility(GONE);
        mSmallMediaController.setVisibility(VISIBLE);
        ScreenUtils.showFullScreen((Activity) getContext(), false);
        attachGestureController(this, true);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setRateTypeItems(List<String> ratetypes, String definition) {
        mLargeMediaController.setRateTypeItems(ratetypes, definition);
    }

    @Override
    public void hide() {
        mRlSkinHide = true;
        mSmallMediaController.setVisibility(GONE);
        mLargeMediaController.setVisibility(GONE);
        mV4TopTitleView.setVisibility(GONE);
        if (lockFlag) {
            if (mGestureControl != null) {
                mGestureControl.cancelTouchable(true);
            }
        }
    }

    @Override
    public void show() {
        mRlSkinHide = false;
        if (mLandScape) {
            canGesture(canGesture);
            mLargeMediaController.setVisibility(VISIBLE);
        } else {
            mSmallMediaController.setVisibility(VISIBLE);
        }
        mV4TopTitleView.setVisibility(VISIBLE);
//        removeCallbacks(mRunnable);
        mHideHandler.removeMessages(0);
        mHideHandler.sendEmptyMessageDelayed(0, DELAY_HIDE);
//        postDelayed(mRunnable, DELAY_HIDE);
        if (mV4TopTitleView != null) {
            mV4TopTitleView.setState();
        }
    }


    protected void attachGestureController(View view, boolean cancel) {
        if (!canGesture) {
            return;
        }
        if (mGestureControl == null) {
            mGestureControl = new GestureControl(getContext(), view);
            mGestureControl.setOnGestureControlListener(new GestureControl.GestureControlListener() {

                @Override
                public void onTouchUp() {
                    touchUp();
                }

                @Override
                public void onSeekTo(int seekGap) {
                    seekTo(seekGap);
                }
            });
        }
        mGestureControl.cancelTouchable(cancel);
//        TODO
//        if (largeController != null && largeController.getSeekbar() != null) {
//            largeController.getSeekbar().startTrackingTouch();
//            largeController.getSeekbar().setProgress(bundle.getInt(GestureControl.GESTURE_CONTROL_SEEK_GAP));
//            if (mGestureControl.mSeekToPopWindow != null) {
//                mGestureControl.mSeekToPopWindow.setProgress(largeController.getSeekbar().getPlayerProgress(), largeController.getSeekbar().getPlayerDuration());
//            }
//        }
    }

    protected void seekTo(int seekGap) {

    }

    protected void touchUp() {

    }

    @Override
    public void canGesture(boolean flag) {
        canGesture = flag;
        if (mGestureControl != null) {
            mGestureControl.cancelTouchable(!flag);
        }
    }

    @Override
    public void setLetvUIListener(LetvUIListener mLetvUIListener) {
        this.mLetvUIListener = mLetvUIListener;
    }

    @Override
    public void isPano(boolean pano) {
        canGesture(!pano);
        mLargeMediaController.isPano(pano);
    }

    public void addWaterMark(CoverConfig coverConfig) {
        if (coverConfig != null && coverConfig.getWaterMarks() != null && coverConfig.getWaterMarks().size() > 0) {
            waterMarkView.setWaterMarks(coverConfig.getWaterMarks());
            //等到第一帧渲染的时候开始显示
//            waterMarkView.show();
        }
    }

    @Override
    public void showLoadingAnimation() {
        mVideoLoading.showLoadingAnimation();
    }

    @Override
    public void showLoadingPic(CoverConfig coverConfig) {
        if (coverConfig != null && coverConfig.getLoadingConfig() != null && coverConfig.getLoadingConfig().getPicUrl() != null) {
            mVideoLoading.setLoadingUrl(coverConfig.getLoadingConfig().getPicUrl());
        }
    }

    @Override
    public void hideLoading() {
        mVideoLoading.hide();
    }

    @Override
    public void processMediaState(int event, Bundle bundle) {
        mNoticeView.processMediaState(event, bundle);

    }

    @Override
    public void processPlayerState(int event, Bundle bundle) {
        mNoticeView.processPlayerState(event, bundle);
    }

    @Override
    public void hideWaterMark() {
        if (waterMarkView != null) {
            waterMarkView.hide();
        }
    }

    @Override
    public boolean isShowLoading() {
        return mVideoLoading.isShown();
    }

    @Override
    public void showLoadingProgress() {
        mVideoLoading.showLoadingProgress();
    }

    @Override
    public void showWaterMark() {
        if (waterMarkView != null) {
            waterMarkView.show();
        }
    }

    @Override
    public void setRePlayListener(VideoNoticeView.IReplayListener l) {
        if (mNoticeView != null) {
            mNoticeView.setRePlayListener(l);
        }
    }

    @Override
    public void processActionStatus(int state) {
        if (mNoticeView != null)
            mNoticeView.processActionStatus(state);

    }

    @Override
    public void processLiveStatus(int state) {
        if (mNoticeView != null)
            mNoticeView.processLiveStatus(state);
    }

    /**
     * 同步进度条
     *
     * @param position
     */
    public void syncSeekProgress(int position) {
        if (mLargeMediaController != null) {
            mLargeMediaController.setCurrentPosition(position);
        }
        if (mSmallMediaController != null) {
            mSmallMediaController.setCurrentPosition(position);
        }
    }

    @Override
    public void setTitle(String title) {
        // TODO Auto-generated method stub

    }

    /**
     * 判断是否全屏
     *
     * @return
     */
    public boolean isFullScreen() {
        if (mLargeMediaController != null && mLargeMediaController.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setGravitySensor(boolean useGSensor) {
        mUseGravitySensor = useGSensor;
    }

    @Override
    public boolean showErrorTip() {
        if (mNoticeView != null) {
            return mNoticeView.getVisibility() == VISIBLE ? true : false;
        }
        return false;
    }

    @Override
    public void setSeekable(boolean seekable) {
//        mSeekable = seekable;
    }

    @Override
    public boolean isLockScreen() {
        return lockFlag;
    }

    @Override
    public void setVrDisplayMode(boolean vrDisplayMode) {
        if (mLargeMediaController != null) {
            mLargeMediaController.setVrMode(vrDisplayMode);
        }
    }

    @Override
    public void isOpenGyroMode(boolean gyroMode) {
        if (mLargeMediaController != null) {
            mLargeMediaController.setGyroMode(gyroMode);
        }
    }

}
