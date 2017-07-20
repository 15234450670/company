package mr.li.dance.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.https.response.UserInfoResponse;
import mr.li.dance.models.UserInfo;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.activitys.base.DanceApplication;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 注册流程设置密码
 * 修订历史:
 */


public class SetPwdActivity extends BaseActivity implements View.OnClickListener {

    private String mMobile;
    private int setType;//-1 设置登录密码 1 忘记密码后设置密码

    @Override
    public int getContentViewId() {
        return R.layout.activity_registsecondstep;
    }

    @Override
    public void initDatas() {
        super.initDatas();
        registFinishBooadCast();
    }

    @Override
    public void initViews() {
        setTitle("设置登录密码");
    }

    public static void lunch(Context context, String mobile, int setType) {
        Intent intent = new Intent(context, SetPwdActivity.class);
        intent.putExtra("mobile", mobile);
        intent.putExtra("settype", setType);
        context.startActivity(intent);
    }

    public static void lunch(Context context, String mobile, int setType, String openid, String source, String picture, String username) {
        Intent intent = new Intent(context, SetPwdActivity.class);
        intent.putExtra("mobile", mobile);
        intent.putExtra("settype", setType);
        intent.putExtra("openid", openid);
        intent.putExtra("source", source);
        intent.putExtra("username", username);
        intent.putExtra("picture", picture);
        context.startActivity(intent);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mMobile = mIntentExtras.getString("mobile");
        setType = mIntentExtras.getInt("settype", -1);
    }

    @Override
    public void onClick(View view) {
        String pwd = mDanceViewHolder.getTextValue(R.id.pwd_tv);
        String repeatpwd = mDanceViewHolder.getTextValue(R.id.repeatpwd_tv);
        if (MyStrUtil.isEmpty(pwd)) {
            NToast.shortToast(this, "请输入密码");
            return;
        }
        if (MyStrUtil.isEmpty(repeatpwd)) {
            NToast.shortToast(this, "请再次输入密码");
            return;
        }
        if (!TextUtils.equals(pwd, repeatpwd)) {
            NToast.shortToast(this, "俩次密码输入不一致");
        } else {
            if (setType == 0) {
                setThirdPwd();
            } else if (setType == -1) {
                SetHeadNickActivity.lunch(this, mMobile, pwd);
            } else {
                setPwd(pwd);
            }
        }

    }

    private void setPwd(String pwd) {
        Request<String> request = ParameterUtils.getSingleton().getFindBackPwdMap(mMobile, pwd);
        request(AppConfigs.login_retrieve, request, true);
    }

    private void setThirdPwd() {
        String openid = mIntentExtras.getString("openid");
        String mobile = mIntentExtras.getString("mobile");
        String source = mIntentExtras.getString("source");
        String username = mIntentExtras.getString("username");
        String picture = mIntentExtras.getString("picture");
        String pwd = mDanceViewHolder.getTextValue(R.id.pwd_tv);
        String repeatpwd = mDanceViewHolder.getTextValue(R.id.repeatpwd_tv);
        String deviceToken = DanceApplication.getInstance().getDeviceToken();
        Request<String> request = ParameterUtils.getSingleton().getPassportPassword(deviceToken,openid, mobile, source, username, picture, pwd, repeatpwd);
        request(AppConfigs.passport_password, request, true);
    }

    @Override
    public void onSucceed(int what, String response) {
        if (what == AppConfigs.login_retrieve) {
            StringResponse reponseResult = JsonMananger.getReponseResult(response, StringResponse.class);
            NToast.shortToast(this, reponseResult.getData());
            finish();
        } else {
            UserInfoResponse infoResponse = JsonMananger.getReponseResult(response, UserInfoResponse.class);
            UserInfo userInfo = infoResponse.getData();
            UserInfoManager.getSingleton().saveLoginInfo(this, userInfo);
            PerfectInfoActivity.lunch(this, userInfo.getUserid());
        }

    }
}
