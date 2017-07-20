package com.lecloud.skin.videoview.vod;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by gaolinhua on 17/3/15.
 */

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class BaseTextureView extends TextureView {
    public static final int DISPLAY_MODE_SMART_PARENT = 0;
    public static final int DISPLAY_MODE_SCALE_ZOOM = 1;
    public static final int DISPLAY_MODE_MATCH_PARENT = 2;
    public static final int DISPLAY_MODE_ORIGIN_SIZE = 3;
    public static final int DISPLAY_MODE_16_9 = 4;
    public static final int DISPLAY_MODE_4_3 = 5;
    public static final int DISPLAY_MODE_HEIGHT_MATCH = 6;
    private int mDisplayMode = DISPLAY_MODE_SMART_PARENT;

    private int mVideoWidth;
    private int mVideoHeight;

    public BaseTextureView(Context context) {
        super(context);
    }

    public void onVideoSizeChanged(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            resetSize();
        }
    }

    private void resetSize() {
        this.requestLayout();
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            switch (this.mDisplayMode) {
                case DISPLAY_MODE_SMART_PARENT: // 智能全屏(相对父布局)
                    if (this.mVideoWidth * heightMeasureSpec > this.mVideoHeight * widthMeasureSpec) {
                        if (this.mVideoWidth * height > width * this.mVideoHeight) {
                            height = width * this.mVideoHeight / this.mVideoWidth;
                        }
                    }
                    break;
                case DISPLAY_MODE_SCALE_ZOOM: // 比例缩放
                    if (this.mVideoWidth * height > width * this.mVideoHeight) {
                        height = width * this.mVideoHeight / this.mVideoWidth;
                    } else if (this.mVideoWidth * height < width * this.mVideoHeight) {
                        width = height * this.mVideoWidth / this.mVideoHeight;
                    }
                    break;
                case DISPLAY_MODE_MATCH_PARENT: // 全屏拉伸(相对父布局)
                    break;
                case DISPLAY_MODE_ORIGIN_SIZE:// 原始尺寸
                    width = this.mVideoWidth;
                    height = this.mVideoHeight;
                    break;
                case DISPLAY_MODE_16_9:
                    if (width * 9 < 16 * height) {
                        height = width * 9 / 16;
                    } else if (width * 9 > 16 * height) {
                        width = height * 16 / 9;
                    }
                    break;
                case DISPLAY_MODE_4_3:
                    if (width * 3 < 4 * height) {
                        height = width * 3 / 4;
                    } else if (width * 3 > 4 * height) {
                        width = height * 4 / 3;
                    }
                    break;
                case DISPLAY_MODE_HEIGHT_MATCH:
                    width = this.mVideoWidth * height / this.mVideoHeight;
                    break;
            }
        }
        Log.d("BaseTextureView", String.format("onMeasure.  measure size(%sx%s)", this.mVideoWidth, this.mVideoHeight));
        this.setMeasuredDimension(width, height);
    }

    public int getDisplayMode() {
        return mDisplayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.mDisplayMode = displayMode;
    }
}
