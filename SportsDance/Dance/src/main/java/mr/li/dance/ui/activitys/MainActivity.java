package mr.li.dance.ui.activitys;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.yolanda.nohttp.rest.Request;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.broadcast.BroadcastManager;
import mr.li.dance.https.CallServer;
import mr.li.dance.https.HttpListener;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.UpdateVersion;
import mr.li.dance.ui.dialogs.UpdateApkDialog;
import mr.li.dance.ui.fragments.BaseFragment;
import mr.li.dance.ui.fragments.main.ExaminationFragment;
import mr.li.dance.ui.fragments.main.HomeFragment;
import mr.li.dance.ui.fragments.main.MatchFragment;
import mr.li.dance.ui.fragments.main.MineFragment;
import mr.li.dance.ui.widget.BottomRelativeLayout;
import mr.li.dance.utils.AndroidBug54971Workaround;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DialogWithYesOrNoUtils;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;
import mr.li.dance.utils.Utils;
import mr.li.dance.utils.updater.Updater;
import mr.li.dance.utils.updater.UpdaterConfig;
import mr.li.dance.utils.updater.UpdaterUtils;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/16
 * 描述: 主页承载页面
 * 修订历史:
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener, HttpListener {
    FragmentManager mFragmentManager;
    BaseFragment mCurrentFragment, mHomePageFragment, mMathcFragment, mMineFragment;
    ExaminationFragment mExaminationFragment;
    private static boolean isExit = false;

    private PushAgent mPushAgent;

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
        setContentView(getContentViewId());
        initDatas();
        initViews();
        mPushAgent = PushAgent.getInstance(getApplicationContext());
        mPushAgent.onAppStart();
        MobclickAgent.onEvent(this, AppConfigs.CLICK_EVENT_17);
    }


    public int getContentViewId() {
        return R.layout.activity_main;
    }


    public void initDatas() {
        BroadcastManager.getInstance(this).sendBroadcast(AppConfigs.finishactivityAction);
        BroadcastManager.getInstance(this).addAction(AppConfigs.finishactivityAction, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        });
        mFragmentManager = getSupportFragmentManager();
        mHomePageFragment = new HomeFragment();
        mMathcFragment = new MatchFragment();
        mExaminationFragment = new ExaminationFragment();
        mMineFragment = new MineFragment();
    }


    private BottomRelativeLayout home_layout, match_layout, examination_layout, mine_layout;


    public void initViews() {
        home_layout = (BottomRelativeLayout) findViewById(R.id.homepage_layout);
        match_layout = (BottomRelativeLayout) findViewById(R.id.match_layout);
        examination_layout = (BottomRelativeLayout) findViewById(R.id.examination_layout);
        mine_layout = (BottomRelativeLayout) findViewById(R.id.mine_layout);

        home_layout.setOnClickListener(this);
        match_layout.setOnClickListener(this);
        examination_layout.setOnClickListener(this);
        mine_layout.setOnClickListener(this);
        home_layout.setClicked(true);

        mCurrentFragment = mHomePageFragment;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.content_fl, mHomePageFragment).show(mHomePageFragment);
        transaction.commit();
        checkVersion();

    }


    @Override
    public void onClick(View v) {
        changeTextViewColor();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(mCurrentFragment);
        switch (v.getId()) {
            case R.id.homepage_layout:
                MobclickAgent.onEvent(this, AppConfigs.CLICK_EVENT_17);
                if (!mHomePageFragment.isAdded()) {
                    transaction.add(R.id.content_fl, mHomePageFragment);
                }
                mCurrentFragment = mHomePageFragment;
                home_layout.setClicked(true);
                break;
            case R.id.match_layout:
                if (!mMathcFragment.isAdded()) {
                    transaction.add(R.id.content_fl, mMathcFragment);
                }
                MobclickAgent.onEvent(this, AppConfigs.CLICK_EVENT_7);
                mCurrentFragment = mMathcFragment;
                match_layout.setClicked(true);
                break;
            case R.id.examination_layout:

                if (!UserInfoManager.getSingleton().isLoading(this)) {
                    LoginActivity.lunch(this, 0x011);
                    return;
                } else {
                    if (!mExaminationFragment.isAdded()) {
                        transaction.add(R.id.content_fl, mExaminationFragment);
                    }
                    MobclickAgent.onEvent(this, AppConfigs.CLICK_EVENT_8);
                    mCurrentFragment = mExaminationFragment;
                    examination_layout.setClicked(true);
                    break;
                }
            case R.id.mine_layout:
                MobclickAgent.onEvent(this, AppConfigs.CLICK_EVENT_9);
                if (!mMineFragment.isAdded()) {
                    transaction.add(R.id.content_fl, mMineFragment);
                }
                mCurrentFragment = mMineFragment;
                mine_layout.setClicked(true);
                break;
        }
        transaction.show(mCurrentFragment);
        transaction.commit();
    }

    private void changeTextViewColor() {
        home_layout.setClicked(false);
        match_layout.setClicked(false);
        examination_layout.setClicked(false);
        mine_layout.setClicked(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    public static void lunch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    private void checkVersion() {
        Request<String> request = ParameterUtils.getSingleton().getVersionMap(Utils.getVersionName(this));
        CallServer.getRequestInstance().add(this, AppConfigs.user_version, request, this, true, false);
    }

    @Override
    public void onFailed(int what, int responseCode, String response) {

    }

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        BroadcastManager.getInstance(this).sendBroadcast(AppConfigs.updateinfoAction);
    }

    UpdateApkDialog updateApkDialog;

    private void showDownDialog(String version, String desc, final String downUrl, final boolean isForce) {

        updateApkDialog = new UpdateApkDialog(this, version, desc, !isForce, "关闭", "下载");
        updateApkDialog.setClickListener(new UpdateApkDialog.ClickListener() {
            @Override
            public void toConfirm() {
                if (downUrl.startsWith("http://") || downUrl.startsWith("HTTP://") || downUrl.startsWith("http://") || downUrl.startsWith("HTTPS://")) {
                    if (isForce) {
                        DownLoadApkActivity.lunch(MainActivity.this, downUrl, isForce);
                    } else {
                        downLoad(downUrl);
                    }
                } else {
                    NToast.shortToast(MainActivity.this, "下载地址错误");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        if (resultCode == 0x011) {
            examination_layout.callOnClick();
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (wasBackground) {
            if (updateApkDialog == null || !updateApkDialog.isShowing()) {
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

    public boolean wasBackground = false;    //声明一个布尔变量,记录当前的活动背景


}
