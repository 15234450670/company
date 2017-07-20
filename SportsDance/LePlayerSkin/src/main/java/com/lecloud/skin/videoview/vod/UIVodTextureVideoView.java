package com.lecloud.skin.videoview.vod;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.view.Surface;
import android.view.TextureView;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;

/**
 * Created by gaolinhua on 17/3/15.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class UIVodTextureVideoView extends UIVodVideoView {
    BaseTextureView surfaceView;

    public UIVodTextureVideoView(Context context) {
        super(context);
    }

    @Override
    protected void prepareVideoSurface() {
        surfaceView = new BaseTextureView(context);
        surfaceView.setSurfaceTextureListener(mTextureListener);
        setVideoView(surfaceView);
    }

    private void setVideoView(BaseTextureView surfaceView) {
        stopAndRelease();
        videoContiner.removeAllViews();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        videoContiner.addView(surfaceView, params);
    }

    TextureView.SurfaceTextureListener mTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            if (player != null) {
                setTextViewVisible(true);
                player.setDisplay(new Surface(surface));
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            setTextViewVisible(false);
            stopAndRelease();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private void setTextViewVisible(boolean visible) {
        Class cls = null;
        try {
            cls = Class.forName("com.lecloud.sdk.videoview.base.BaseVideoView");
            Field field = cls.getDeclaredField("isSurfaceVisible");
            field.setAccessible(true);//设置允许访问
            field.set(cls.newInstance(), visible);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
