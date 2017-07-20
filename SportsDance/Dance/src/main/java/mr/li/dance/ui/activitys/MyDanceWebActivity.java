package mr.li.dance.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.WebResponse;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.widget.DanceWebView;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.ShareUtils;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: H5显示页面
 * 修订历史:
 */

public class MyDanceWebActivity extends BaseActivity {
    public static final int ZIXUNTYPE = 0;//0 资讯
    public static final int ABOUTTYPE = 1;//1关于我们
    public static final int UPLOADPIC = 2;//我也想出现这里
    public static final int MATCHOTHER1 = 31;//赛事的 赛事章程
    public static final int MATCHOTHER2 = 32;//赛事的 赛事设项目
    public static final int MATCHOTHER3 = 33;//赛事的 赛程表
    public static final int KAOJI = 4;//考级
    public static final int OTHERTYPE = 5;//外联


    private String titleName;
    private int mWebType;
    private int wailianId;
    private boolean showShare;
    private String mCountId;//分享统计Id

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

        WebSettings webSettings = mWebView.getSettings();
        
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        //        if (dm.densityDpi > 240 ) {
//            webSettings.setDefaultFontSize(20); //可以取1-72之间的任意值，默认16
//        }
//        webSettings.setTextSize(WebSettings.TextSize.LARGER); //可


        if (Build.VERSION.SDK_INT > 11) {
            webSettings.setDisplayZoomControls(false);
        }
        webSettings.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new RongWebChromeClient());

        if (showShare) {
            setRightImage(R.drawable.share_icon_001);
        }
        loadUrl();
    }

    private void loadUrl() {

        switch (mWebType) {
            case ABOUTTYPE:
                Request<String> aboutRequest = ParameterUtils.getSingleton().getAboutUsMap();
                request(AppConfigs.webcode, aboutRequest);
                break;
            case UPLOADPIC:
                Request<String> stringRequest = ParameterUtils.getSingleton().getxcUploadDetailMap();
                request(AppConfigs.webcode, stringRequest);
                break;
            case OTHERTYPE:
                if (wailianId != -1) {
                    updateWaiLian();
                }
                mCountId = AppConfigs.CLICK_EVENT_23;
                mWebView.loadUrl(shareUrl);
                break;
            case ZIXUNTYPE:
                mCountId = AppConfigs.CLICK_EVENT_21;
                mWebView.loadUrl(shareUrl);
                break;
            case KAOJI:
                mCountId = AppConfigs.CLICK_EVENT_24;
                mWebView.loadUrl(shareUrl);
                break;
            case MATCHOTHER1:
                mCountId = AppConfigs.CLICK_EVENT_25;
                mWebView.loadUrl(shareUrl);
                break;
            case MATCHOTHER2:
                mCountId = AppConfigs.CLICK_EVENT_26;
                mWebView.loadUrl(shareUrl);
                break;
            case MATCHOTHER3:
                mCountId = AppConfigs.CLICK_EVENT_27;
                mWebView.loadUrl(shareUrl);
                break;
            default:
                mWebView.loadUrl(shareUrl);
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
        shareUrl = mIntentExtras.getString("url");
        wailianId = mIntentExtras.getInt("wailianid", -1);
        showShare = mIntentExtras.getBoolean("showshare", false);
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

    public static void lunch(Context context, int type, String title, String url, boolean showShare) {
        Intent intent = new Intent(context, MyDanceWebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("webtype", type);
        intent.putExtra("showshare", showShare);
        context.startActivity(intent);
    }

    public static void lunch(Context context, int type, String title, String url, String wailianId) {
        Intent intent = new Intent(context, MyDanceWebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("webtype", type);
        intent.putExtra("wailianid", wailianId);
        intent.putExtra("showshare", true);
        context.startActivity(intent);
    }

    public static void lunch(Context context, int type, String title, String url) {
        lunch(context, type, title, url, false);
    }

    public static void lunch(Context context, int type, String title) {
        lunch(context, type, title, "", false);
    }

    @Override
    public void initDatas() {
        super.initDatas();
    }

    private String shareUrl;


    private void showShareDialog() {
        if (MyStrUtil.isEmpty(titleName)) {
            mShareContent = mTitle.getText().toString();
        } else {
            mShareContent = titleName;
        }
        if (mShareUtils == null) {
            mShareUtils = new ShareUtils(this);
        }
        mShareUtils.showShareDilaog(mCountId,shareUrl, mShareContent);

    }

    ShareUtils mShareUtils;
    String mShareContent;

    private void updateWaiLian() {
        Request<String> stringRequest = ParameterUtils.getSingleton().getHomeWlinkClickMap(wailianId);
        request(AppConfigs.home_WlinkClick, stringRequest);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (AppConfigs.home_WlinkClick != what) {
            WebResponse reponseResult = JsonMananger.getReponseResult(response, WebResponse.class);
            String h5Content = reponseResult.getData();
            mWebView.loadData(h5Content, "text/html; charset=UTF-8", null);//这种写法可以正确解码
        }

    }
}
