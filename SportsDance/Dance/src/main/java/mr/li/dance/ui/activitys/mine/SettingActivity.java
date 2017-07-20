package mr.li.dance.ui.activitys.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import mr.li.dance.R;
import mr.li.dance.broadcast.BroadcastManager;
import mr.li.dance.ui.activitys.LoginActivity;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;
import mr.li.dance.utils.Utils;
import mr.li.dance.utils.glide.GlideCatchUtil;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页-我的-设置
 * 修订历史:
 */


public class SettingActivity extends BaseActivity implements View.OnClickListener {


    @Override
    public int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initViews() {
        setTitle("设置");
        mDanceViewHolder.setText(R.id.vername_tv, "V "+Utils.getVersionName(this));
    }

    public static void lunch(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    public void initDatas() {
        super.initDatas();
        registFinishBooadCast();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_layout:
                String  cacheSize = GlideCatchUtil.getInstance().getCacheSize();
                GlideCatchUtil.getInstance().cleanCatchDisk();
                GlideCatchUtil.getInstance().clearCacheDiskSelf();
                GlideCatchUtil.getInstance().clearCacheMemory();
                NToast.shortToast(this,"成功清理了"+cacheSize+"缓存文件");
                break;
            case R.id.updatepwd_layout:
                UpdatePwdActivity.lunch(this);
                break;
        }
    }

    public void toQuit(View view) {
        MobclickAgent.onEvent(this, AppConfigs.CLICK_EVENT_16);
        UserInfoManager.getSingleton().clearInfo(this);
        LoginActivity.lunch(this);
        finish();
    }
}
