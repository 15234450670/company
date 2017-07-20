package mr.li.dance.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import mr.li.dance.R;
import mr.li.dance.ui.activitys.album.AlbumActivity;
import mr.li.dance.ui.dialogs.ShareDialog;

/**
 * Created by Lixuewei on 2017/6/13.
 */

public class ShareUtils {
    //    ShareAction mShareAction;
    private Activity mActivity;
    UMShareListener umShareListener;
    String shareUrl;
    String mShareContent;
    String desc = "赛事直播，精彩瞬间，体舞资讯，官方查询尽在中国体育舞蹈";
    ShareDialog mShareDialog;

    public ShareUtils(Activity mActivity) {
        this.mActivity = mActivity;
        initShareListener();
        initShareDialog();
    }

    /**
     * @param countId       统计id
     * @param shareUrl
     * @param mShareContent
     */
    public void showShareDilaog(String countId, String shareUrl, String mShareContent) {
        if (!MyStrUtil.isEmpty(countId)) {
            MobclickAgent.onEvent(mActivity, countId);
        }
        this.shareUrl = shareUrl;
        this.mShareContent = mShareContent;
        mShareDialog.dispaly();
    }


    private void initShareListener() {
        umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                //分享开始的回调
            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Toast.makeText(mActivity, "分享成功啦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(mActivity, "分享失败啦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }
        };
    }

    private void toShare(SHARE_MEDIA share_media) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(mShareContent);
        web.setThumb(new UMImage(mActivity, R.mipmap.dancelogo_icon));
        web.setDescription(desc);
        ShareAction shareAction = new ShareAction(mActivity);
        shareAction.setPlatform(share_media).withMedia(web);
        UMShareAPI.get(mActivity).doShare(mActivity, shareAction, umShareListener);
    }

    private void initShareDialog() {
        mShareDialog = new ShareDialog(mActivity, new ShareDialog.ShareClickListener() {
            @Override
            public void selectItem(SnsPlatform snsPlatform) {
                toShare(snsPlatform.mPlatform);
            }
        });
    }

}
