package mr.li.dance.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yolanda.nohttp.rest.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.WebResponse;
import mr.li.dance.ui.activitys.base.BaseActivity;

import mr.li.dance.ui.widget.DanceWebView;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.ShareUtils;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: H5显示页面
 * 修订历史:
 */

public class DanceWebActivity extends BaseActivity {

    public static final int ZIXUNTYPE = 0;//0 资讯
    public static final int ABOUTTYPE = 1;//1关于我们
    public static final int MATCHOTHER = 2;//赛事的 赛事章程，赛事设项目，赛程表
    public static final int OTHERTYPE = 3;//3直接显示url
    public static final int Type_4 = 4;//4相册页面点“我也想出现这里”

    private String titleName;
    private String detailId;
    private String url;
    private int mWebType;//0 资讯 //1关于我们//2外联//3直接显示url//4相册页面点“我也想出现这里”5//外联
    private String compete_id;//竞赛规则 竞赛设项 赛程表用的
    private String w_page;//竞赛规则 竞赛设项 赛程表用的
    private int wailianId = -1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initViews() {
        setTitle(titleName);
        mWebView = (DanceWebView) findViewById(R.id.rc_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.rc_web_progressbar);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT > 11) {
            mWebView.getSettings().setDisplayZoomControls(false);
        }
        mWebView.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new RongWebChromeClient());

        switch (mWebType) {
            case ZIXUNTYPE:
                Request<String> ziXunRequest = ParameterUtils.getSingleton().getWebMap(detailId);
                request(AppConfigs.webcode, ziXunRequest);
                setRightImage(R.drawable.share_icon_001);
                break;
            case ABOUTTYPE:
                Request<String> aboutRequest = ParameterUtils.getSingleton().getAboutUsMap();
                request(AppConfigs.webcode, aboutRequest);
                break;
            case MATCHOTHER:
                Request<String> matchResuest = ParameterUtils.getSingleton().getMatch_Jsgz_Sx_SCB_Map(compete_id, w_page);
                request(AppConfigs.webcode, matchResuest);
                setRightImage(R.drawable.share_icon_001);
                break;
            case Type_4:
                Request<String> stringRequest = ParameterUtils.getSingleton().getxcUploadDetailMap();
                request(AppConfigs.webcode, stringRequest);

            case OTHERTYPE:
                updateWaiLian();
                mWebView.loadUrl(url);
                break;
        }
    }

    @Override
    public void onHeadRightButtonClick(View v) {
        showShareDialog();
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mWebType = mIntentExtras.getInt("webtype", 0);
        titleName = mIntentExtras.getString("title");
        detailId = mIntentExtras.getString("detailid");
        url = mIntentExtras.getString("url");
        wailianId = mIntentExtras.getInt("wailianid", -1);

        compete_id = mIntentExtras.getString("compete_id");
        w_page = mIntentExtras.getString("w_page");


    }

    private DanceWebView mWebView;
    private ProgressBar mProgressBar;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private class RongWebChromeClient extends WebChromeClient {
        private RongWebChromeClient() {
        }

        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() == View.GONE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                mProgressBar.setProgress(newProgress);
            }

            super.onProgressChanged(view, newProgress);
        }

        public void onReceivedTitle(WebView view, String title) {
            if (MyStrUtil.isEmpty(titleName)) {
                setTitle(title);
            }
        }
    }

//    public static void lunch(Context context, String title, String url) {
//        Intent intent = new Intent(context, DanceWebActivity.class);
//        intent.putExtra("title", title);
//        intent.putExtra("url", url);
//        context.startActivity(intent);
//    }
//
//    public static void lunch(Context context, String detailId, int webType, String title) {
//        Intent intent = new Intent(context, DanceWebActivity.class);
//        intent.putExtra("detailid", detailId);
//        intent.putExtra("webtype", webType);
//        intent.putExtra("title", title);
//        context.startActivity(intent);
//    }
//
//
//    public static void lunch(Context context, String wailianId, int webType, String url, String title) {
//        Intent intent = new Intent(context, DanceWebActivity.class);
//        intent.putExtra("webtype", webType);
//        intent.putExtra("url", url);
//        intent.putExtra("wailianid", wailianId);
//        intent.putExtra("title", title);
//        context.startActivity(intent);
//    }
//
//    public static void lunch(Context context, int webType, String url, String title) {
//        Intent intent = new Intent(context, DanceWebActivity.class);
//        intent.putExtra("webtype", webType);
//        intent.putExtra("url", url);
//        intent.putExtra("title", title);
//        context.startActivity(intent);
//    }
//
//    public static void lunch(Context context, int webType, String compete_id, String w_page, String title) {
//        Intent intent = new Intent(context, DanceWebActivity.class);
//        intent.putExtra("webtype", webType);
//        intent.putExtra("compete_id", compete_id);
//        intent.putExtra("title", title);
//        intent.putExtra("w_page", w_page);
//        context.startActivity(intent);
//    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (AppConfigs.home_WlinkClick != what) {
            WebResponse reponseResult = JsonMananger.getReponseResult(response, WebResponse.class);
            String h5Content = reponseResult.getData();
            mWebView.loadData(h5Content, "text/html; charset=UTF-8", null);//这种写法可以正确解码
        }

    }

    @Override
    public void initDatas() {
        super.initDatas();
    }

    private String shareUrl;
    private UMWeb web;
    private ShareAction mShareAction;

    private void showShareDialog() {
        if (MyStrUtil.isEmpty(mTitle.getText().toString())) {
            NToast.shortToast(this, "未加载完毕，无法进行分享");
            return;
        }
        if (mWebType == ZIXUNTYPE) {
            MobclickAgent.onEvent(this, AppConfigs.CLICK_EVENT_21);
            shareUrl = String.format(AppConfigs.ZixunShareUrl, String.valueOf(detailId));
        } else if (mWebType == MATCHOTHER) {
            MobclickAgent.onEvent(this, AppConfigs.CLICK_EVENT_21);
            shareUrl = String.format(AppConfigs.SAISHIShareUrl, String.valueOf(compete_id), String.valueOf(w_page));
        } else {
            return;
        }
        mShareContent = mTitle.getText().toString();
        MobclickAgent.onEvent(this, AppConfigs.CLICK_EVENT_19);
        if (mShareUtils == null) {
            mShareUtils = new ShareUtils(this);
        }
    }

    ShareUtils mShareUtils;
    String mShareContent;

    private void updateWaiLian() {
        if (wailianId != -1) {
            Request<String> stringRequest = ParameterUtils.getSingleton().getHomeWlinkClickMap(wailianId);
            request(AppConfigs.home_WlinkClick, stringRequest);
        }
    }

}
