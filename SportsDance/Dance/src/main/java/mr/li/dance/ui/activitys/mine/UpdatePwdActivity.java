package mr.li.dance.ui.activitys.mine;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.broadcast.BroadcastManager;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.UserInfo;
import mr.li.dance.ui.activitys.LoginActivity;
import mr.li.dance.ui.activitys.MainActivity;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页-我的-设置-修改密码
 * 修订历史:
 */


public class UpdatePwdActivity extends BaseActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_updatepwd;
    }

    @Override
    public void initViews() {
        setTitle("修改密码");
    }

    public static void lunch(Context context) {
        context.startActivity(new Intent(context, UpdatePwdActivity.class));
    }

    public void toSubmit(View view) {
        String oldPwd = mDanceViewHolder.getTextValue(R.id.oldpwd_tv);
        String newPwd = mDanceViewHolder.getTextValue(R.id.newpwd_tv);
        String repeatPwd = mDanceViewHolder.getTextValue(R.id.repeatpwd_tv);

        if (MyStrUtil.isEmpty(oldPwd)) {
            NToast.shortToast(this, "请输入旧密码");
            return;
        }
        if (MyStrUtil.isEmpty(newPwd)) {
            NToast.shortToast(this, "请输入新密码");
            return;
        }
        if (MyStrUtil.isEmpty(repeatPwd)) {
            NToast.shortToast(this, "请再次输入新密码");
            return;
        }

        if (!TextUtils.equals(newPwd, repeatPwd)) {
            NToast.shortToast(this, "俩次输如新密码不同");
            return;
        }

        if (TextUtils.equals(oldPwd, repeatPwd)) {
            NToast.shortToast(this, "新密码与旧密码相同");
            return;
        }
        UserInfo userInfo = UserInfoManager.getSingleton().getUserInfo(this);
        Request<String> request = ParameterUtils.getSingleton().getUpdatePwdMap(userInfo.getMobile(), oldPwd, newPwd, repeatPwd);
        request(AppConfigs.myInfo_retrieve, request, true);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
        if (null == stringResponse) {

        }
        NToast.shortToast(this, stringResponse.getData());
        MainActivity.lunch(this);
//        BroadcastManager.getInstance(this).sendBroadcast(AppConfigs.finishactivityAction);
//        LoginActivity.lunch(this);
        finish();
    }
}
