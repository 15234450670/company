package com.lecloud.skin.videoview.vod;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lecloud.skin.R;
import com.lecloud.skin.imageload.SmartImageTask;
import com.lecloud.skin.imageload.SmartImageView;
import com.lecloud.skin.ui.utils.PxUtils;
import com.lecloud.skin.ui.utils.ScreenUtils;

public class ShowAdPicUtils {
    private Context mContext;
    private SmartImageView adPic;
    private ImageView closePic;
    private LayoutInflater mInflater;
    private FrameLayout mContentView;
    private View view;
    private float scale;


    public void setmAdPicStatusListener(AdPicStatusListener mAdPicStatusListener) {
        this.mAdPicStatusListener = mAdPicStatusListener;
    }


    public void setmVideoPauseListener(VideoPauseListener mVideoPauseListener) {
        this.mVideoPauseListener = mVideoPauseListener;
    }

    private VideoPauseListener mVideoPauseListener;
    private AdPicStatusListener mAdPicStatusListener;

    public ShowAdPicUtils(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public FrameLayout showAdPic(String url) {
        if (view == null) {
            view = mInflater.inflate(R.layout.pause_ad_layout, null);
            mContentView = (FrameLayout) view.findViewById(R.id.show_ad);
            adPic = (SmartImageView) view.findViewById(R.id.ad_pic);
            closePic = (ImageView) view.findViewById(R.id.close_ad);
        }

        if (mVideoPauseListener != null && mVideoPauseListener.isVideoPause()) {

            adPic.setImageUrl(url, null, null, new SmartImageTask.OnCompleteListener() {
                @Override
                public void onComplete() {

                }

                @Override
                public void onComplete(Bitmap bitmap) {
                    if (mVideoPauseListener != null && mVideoPauseListener.isVideoPause()) {
                        scale = (float) bitmap.getHeight() / bitmap.getWidth();
                        changeAdPicSize();
                    }
                }
            });

            adPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdPicStatusListener != null) {
                        mAdPicStatusListener.onAdPicClicked();
                    }
                }
            });
            closePic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    closeAdPic();
                }
            });
            if (mAdPicStatusListener != null) {
                mAdPicStatusListener.onAdPicStarted();
            }
        }
        return mContentView;
    }

    public void changeAdPicSize() {
        if (adPic != null) {
//            Log.d("huahua", "changeAdPicSize:11" + scale);
            int contentWidth;
            int contentHeight;
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            if (ScreenUtils.getOrientation(mContext) == Configuration.ORIENTATION_PORTRAIT) {
                contentWidth = PxUtils.dip2px(mContext, 300);/*200，150 w 280，210*/
                contentHeight = PxUtils.dip2px(mContext, 225);
            } else {
                contentWidth = PxUtils.dip2px(mContext, 420);
                contentHeight = PxUtils.dip2px(mContext, 315);
            }
            if (((float) contentHeight / contentWidth) > scale) {
                lp.width = contentWidth;
                lp.height = (int) (lp.width * scale);
            } else {
                lp.height = contentHeight;
                lp.width = (int) (lp.height / scale);
            }
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rl.addRule(RelativeLayout.CENTER_IN_PARENT);
            adPic.setLayoutParams(lp);
            mContentView.setLayoutParams(rl);
            if (mContentView.getVisibility() == View.GONE) {
                mContentView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void closeAdPic() {
        if (mContentView.getVisibility() == View.VISIBLE) {
            mContentView.setVisibility(View.GONE);
            if (mAdPicStatusListener != null) {
                mAdPicStatusListener.onAdPicClosed();
            }
        }
    }


    public interface VideoPauseListener {
        boolean isVideoPause();
    }

    public interface AdPicStatusListener {
        void onAdPicClicked();

        void onAdPicClosed();

        void onAdPicStarted();
    }

}
