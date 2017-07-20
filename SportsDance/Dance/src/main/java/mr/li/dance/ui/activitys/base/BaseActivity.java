package mr.li.dance.ui.activitys.base;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;
import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.rest.Request;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.broadcast.BroadcastManager;
import mr.li.dance.https.CallServer;
import mr.li.dance.https.HttpListener;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.UpdateVersion;
import mr.li.dance.ui.activitys.DownLoadApkActivity;
import mr.li.dance.ui.activitys.MainActivity;
import mr.li.dance.ui.dialogs.LoadDialog;
import mr.li.dance.ui.dialogs.UpdateApkDialog;
import mr.li.dance.utils.AndroidBug54971Workaround;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DanceViewHolder;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.Utils;
import mr.li.dance.utils.glide.ImageLoaderManager;
import mr.li.dance.utils.updater.Updater;
import mr.li.dance.utils.updater.UpdaterConfig;
import mr.li.dance.utils.updater.UpdaterUtils;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 所有activity 的基类
 * 修订历史:
 */
public abstract class BaseActivity extends FragmentActivity implements HttpListener {

    protected Context mContext;
    private ViewFlipper mContentView;
    protected RelativeLayout mHeadLayout;
    protected ImageView mLeftIv;
    protected ImageView mRightIv;
    protected ImageView mRightIv2;
    protected TextView mTitle;
    protected TextView mHeadRightText;
    private Drawable mBtnBackDrawable;
    protected View mLeftLayout;
    protected View mRightLayout;
    protected CallServer mCallServer;
    protected SharedPreferences mSp;
    protected SharedPreferences.Editor mEditor;
    protected LoadDialog mWaitDialog;
    protected DanceViewHolder mDanceViewHolder;
    public TwinklingRefreshLayout mRefreshLayout;
    public Bundle mIntentExtras;

    protected void setScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//A
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//B
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.home_bg_color));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreen();
        super.setContentView(R.layout.layout_base);
        initTitleView();
        setContentView(getContentViewId());
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        mContext = this;
        mCallServer = CallServer.getRequestInstance();
        getIntentData();
        initDatas();
        initViews();
        initRefreshLayout();
    }

    public void initRefreshLayout() {
        mRefreshLayout = (TwinklingRefreshLayout) mDanceViewHolder.getView(R.id.refresh);
        if (mRefreshLayout == null) {
            return;
        }
        BezierLayout headerView = new BezierLayout(this);
        mRefreshLayout.setHeaderView(headerView);
        mRefreshLayout.setMaxHeadHeight(140);
        mRefreshLayout.setPureScrollModeOn();//只显示页面回弹效果
        mRefreshLayout.setOverScrollBottomShow(false);
    }

    // 初始化公共头部
    private void initTitleView() {
        mContentView = (ViewFlipper) super.findViewById(R.id.layout_container);
        mHeadLayout = (RelativeLayout) super.findViewById(R.id.layout_title);
        mHeadRightText = (TextView) findViewById(R.id.text_right);
        mLeftIv = (ImageView) super.findViewById(R.id.btn_left);
        mRightIv = (ImageView) super.findViewById(R.id.image_right);
        mRightIv2 = (ImageView) super.findViewById(R.id.image_right2);
        mTitle = (TextView) super.findViewById(R.id.tv_title);
        mBtnBackDrawable = getResources().getDrawable(R.drawable.back_icon);
        mBtnBackDrawable.setBounds(0, 0, mBtnBackDrawable.getMinimumWidth(),
                mBtnBackDrawable.getMinimumHeight());
        mLeftLayout = findViewById(R.id.left_layout);
        mRightLayout = findViewById(R.id.right_layout);
    }


    public abstract int getContentViewId();

    public void getIntentData() {
        mIntentExtras = getIntent().getExtras();
    }


    public abstract void initViews();

    public void initDatas() {
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        mEditor = mSp.edit();

    }

    ;

    protected String getmUserId() {
        return mSp.getString(AppConfigs.USERID, "");
    }

    @Override
    public void setContentView(View view) {
        mDanceViewHolder = new DanceViewHolder(this, view);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        mContentView.addView(view, lp);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    protected void setRightImage(int iamgeResId) {
        setRightImage(iamgeResId, -1);
        mRightLayout.setVisibility(View.VISIBLE);
        mRightIv.setVisibility(View.VISIBLE);
        setHeadRightButtonVisibility(View.GONE);
        mRightIv.setImageResource(iamgeResId);
    }

    protected void setRightTextAndImage(String righttext, int iamgeResId) {
        mRightLayout.setVisibility(View.VISIBLE);
        mRightIv.setVisibility(View.VISIBLE);
        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText(righttext);
        mRightIv.setImageResource(iamgeResId);
    }

    protected void setRightImage(int iamgeResId, int iamgeResId2) {
        mRightLayout.setVisibility(View.VISIBLE);
        mRightIv.setVisibility(View.VISIBLE);
        setHeadRightButtonVisibility(View.GONE);
        mRightIv.setImageResource(iamgeResId);
        if (iamgeResId2 != -1) {
            mRightIv2.setVisibility(View.VISIBLE);
            setHeadRightButtonVisibility(View.GONE);
            mRightIv2.setImageResource(iamgeResId2);
        }
    }

    /**
     * 设置头部是否可见
     *
     * @param visibility
     */
    public void setHeadVisibility(int visibility) {
        mHeadLayout.setVisibility(visibility);
    }

    /**
     * 设置左边是否可见
     *
     * @param visibility
     */
    public void setHeadLeftButtonVisibility(int visibility) {
        mLeftLayout.setVisibility(visibility);
    }

    /**
     * 设置右边是否可见
     *
     * @param visibility
     */
    public void setHeadRightButtonVisibility(int visibility) {
        mHeadRightText.setVisibility(visibility);
    }

    public void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(int titleId) {
        setTitle(getString(titleId), false);
    }

    /**
     * 设置标题
     */
    public void setTitle(int titleId, boolean flag) {
        setTitle(getString(titleId), flag);
    }


    /**
     * 设置标题
     */
    public void setTitle(String title) {
        setTitle(title, false);
    }


    public void setTitleAndRightBtn(String title, String btnName) {
        setTitle(title, false);
        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText(btnName);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title, boolean flag) {
        mTitle.setText(title);
    }

    /**
     * 点击左按钮
     */
    public void onHeadLeftButtonClick(View v) {
        finish();
    }

    /**
     * 点击右按钮
     */
    public void onHeadRightButtonClick(View v) {

    }

    /**
     * 发送请求（需要检查网络）
     *
     * @param requestCode 请求码
     */
    public void request(int requestCode, Request request) {
        request.setCancelSign(this);
        request(requestCode, request, false);
    }

    public void request(int requestCode, Request request, boolean showLoading) {
        request.setCancelSign(this);
        if (mCallServer != null) {
            mCallServer.add(this, requestCode, request, this, false, showLoading);
        }
    }

    /**
     * 取消所有请求
     */
    public void cancelRequest() {
        if (mCallServer != null) {
            mCallServer.cancelBySign(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        closeKeyboard();
        cancelRequest();
        BroadcastManager.getInstance(mContext).destroy(AppConfigs.finishactivityAction);
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    public <T> T getReponseResult(String resultStr, Class<T> cls) {
        T t = null;
        try {
            t = JsonMananger.jsonToBean(resultStr, cls);
        } catch (ServerError serverError) {
            serverError.printStackTrace();
        }
        return t;
    }

    @Override
    public void onFailed(int what, int responseCode, String response) {
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
        switch (responseCode) {
            case -1000:
                NToast.shortToast(this, "网络连接错误");
                break;
            case -1001:
                NToast.shortToast(this, response);
                break;
            default:
                StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
                if (stringResponse != null) {
                    NToast.shortToast(this, stringResponse.getData());
                }
                break;
        }
    }

    public void registFinishBooadCast() {
        BroadcastManager.getInstance(mContext).addAction(AppConfigs.finishactivityAction, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (wasBackground ) {
            if(updateApkDialog == null || !updateApkDialog.isShowing()){
                checkVersion();
            }
        }
        wasBackground = false;
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (isApplicationBroughtToBackground()) {
            wasBackground = true;
        }
    }

    public boolean wasBackground = false;    //声明一个布尔变量,记录当前的活动背景

    private boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }

    private void checkVersion() {
        HttpListener httpListener = new HttpListener() {
            @Override
            public void onSucceed(int what, String response) {
                StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
                String updateInfomation = stringResponse.getData();
                if (!MyStrUtil.isEmpty(updateInfomation)) {
                    UpdateVersion updateVersion = JsonMananger.getReponseResult(updateInfomation, UpdateVersion.class);
                    String downUrl = updateVersion.getUrl();
                    boolean hasNewVersion = updateVersion.getIs_upd() == 1;
                    boolean isForceUpdate = updateVersion.getIs_force() == 1;

                    if (hasNewVersion && !MyStrUtil.isEmpty(downUrl)) {
                        String version = "V " + updateVersion.getVersion();
                        String desc = updateVersion.getDescr();
                        showDownDialog(version, desc, downUrl, isForceUpdate);
                    }
                }
            }

            @Override
            public void onFailed(int what, int responseCode, String response) {

            }
        };

        Request<String> request = ParameterUtils.getSingleton().getVersionMap(Utils.getVersionName(this));
        CallServer.getRequestInstance().add(this, AppConfigs.user_version, request, httpListener, true, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }


    }

    UpdateApkDialog updateApkDialog;

    private void showDownDialog(String version, String desc, final String downUrl, final boolean isForce) {
        updateApkDialog = new UpdateApkDialog(this, version, desc, !isForce, "关闭", "下载");
        updateApkDialog.setClickListener(new UpdateApkDialog.ClickListener() {
            @Override
            public void toConfirm() {
                if (downUrl.startsWith("http://") || downUrl.startsWith("HTTP://") || downUrl.startsWith("http://") || downUrl.startsWith("HTTPS://")) {
                    if (isForce) {
                        DownLoadApkActivity.lunch(BaseActivity.this, downUrl, isForce);
                    } else {
                        downLoad(downUrl);
                    }
                } else {
                    NToast.shortToast(BaseActivity.this, "下载地址错误");
                }


            }

            @Override
            public void toRefause() {

            }
        });
        updateApkDialog.display();
    }

    private void downLoad(String downUrl) {
        if (!canDownloadState()) {
            UpdaterUtils.showDownloadSetting(this);
        } else {
            UpdaterConfig config = new UpdaterConfig.Builder(this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setDescription(getString(R.string.system_download_description))
                    .setFileUrl(downUrl)
                    .setCanMediaScanner(true)
                    .build();
            Updater.get().showLog(true).download(config);
        }
    }

    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
