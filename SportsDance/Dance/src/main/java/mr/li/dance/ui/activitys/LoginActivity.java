package mr.li.dance.ui.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.SocializeUtils;
import com.yolanda.nohttp.rest.Request;

import java.util.Map;

import mr.li.dance.R;
import mr.li.dance.broadcast.BroadcastManager;
import mr.li.dance.https.CallServer;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.https.response.UserInfoResponse;
import mr.li.dance.models.UserInfo;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.dialogs.LoadDialog;
import mr.li.dance.ui.otherload.BaseUiListener;
import mr.li.dance.ui.otherload.OtherLoadManager;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DialogWithYesOrNoUtils;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.NLog;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;
import mr.li.dance.utils.Utils;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 登录页面
 * 修订历史:
 */


public class LoginActivity extends LoginAuthActivity implements View.OnClickListener {
    private int requestCode = -1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        if (mIntentExtras != null) {
            requestCode = mIntentExtras.getInt("requestcode");
        }
    }


    OtherLoadManager otherLoadManager;

    private void initUmeng(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUmeng(savedInstanceState);
    }

    @Override
    public void initViews() {

        setTitle("中国体育舞蹈");
        String loginName = UserInfoManager.getSingleton().getLoginName(this);
        mDanceViewHolder.setText(R.id.login_name, loginName);
        String passWord = UserInfoManager.getSingleton().getLoginPwd(this);
        mDanceViewHolder.setText(R.id.login_password, passWord);
        mDanceViewHolder.setClickListener(R.id.wx_iv, this);
        mDanceViewHolder.setClickListener(R.id.qq_iv, this);
        mDanceViewHolder.setClickListener(R.id.wb_iv, this);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        registFinishBooadCast();
    }

    public void toRegist(View view) {
        SendCodeActivity.lunch(this);
    }

    public void toForgetPwd(View view) {
        SendCodeActivity.lunch(this, 1);
    }

    public void toLogin(View view) {
        String phone = mDanceViewHolder.getTextValue(R.id.login_name);
        String passWord = mDanceViewHolder.getTextValue(R.id.login_password);
        if (TextUtils.isEmpty(phone)) {
            NToast.shortToast(this, "手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            NToast.shortToast(this, "密码不能为空");
            return;
        }
        closeKeyboard();
        String phone_xh = Utils.getSystemModel();
        Request<String> request = ParameterUtils.getSingleton().getLoginMap(Utils.getVersionName(this), phone, passWord,phone_xh);
        CallServer.getRequestInstance().add(this, AppConfigs.login_loginMob, request, this, false, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 0x001) {
            String loginName = data.getStringExtra("loginname");
            mDanceViewHolder.setText(R.id.login_name, loginName);
            String loginPwd = data.getStringExtra("loginpwd");
            mDanceViewHolder.setText(R.id.login_password, loginPwd);
        }
    }


    public static void lunch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void lunch(Fragment context, int requestCode) {
        Intent intent = new Intent(context.getActivity(), LoginActivity.class);
        intent.putExtra("requestcode", requestCode);
        context.startActivityForResult(intent, requestCode);
    }

    public static void lunch(Activity context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("requestcode", requestCode);
        context.startActivityForResult(intent, requestCode);
    }

    SnsPlatform mSnsPlatform;

    @Override
    public void onClick(View view) {
        mSnsPlatform = null;
        switch (view.getId()) {
            case R.id.wx_iv:
                mSnsPlatform = SHARE_MEDIA.WEIXIN.toSnsPlatform();
                break;
            case R.id.qq_iv:
                mSnsPlatform = SHARE_MEDIA.QQ.toSnsPlatform();
                break;
            case R.id.wb_iv:
                mSnsPlatform = SHARE_MEDIA.SINA.toSnsPlatform();
                break;
        }
        if (null != mSnsPlatform) {
            mWaitDialog.show();
            UMShareAPI.get(this).doOauthVerify(this, mSnsPlatform.mPlatform, new MyUMAuthListener() {
                @Override
                public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        NLog.i("UMAuthListener", "key= " + entry.getKey() + " and value= " + entry.getValue());
                    }
                    getUserInfo();
                }
            });

        }

    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (what == AppConfigs.passport_isOpenid) {
            StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
            UserInfo userInfo = JsonMananger.getReponseResult(stringResponse.getData(), UserInfo.class);
            checkIsLogin(userInfo);
        } else {
            UserInfoResponse infoResponse = JsonMananger.getReponseResult(response, UserInfoResponse.class);
            UserInfo userInfo = infoResponse.getData();
            String loginName = mDanceViewHolder.getTextValue(R.id.login_name);
            String loginPwd = mDanceViewHolder.getTextValue(R.id.login_password);
            UserInfoManager.getSingleton().saveLoginInfo(this, loginName, loginPwd);
            UserInfoManager.getSingleton().saveLoginInfo(this, userInfo);
            if (requestCode == -1) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private String openid;
    private String source;

    private void checkIsLogin(UserInfo userInfo) {
        if (TextUtils.equals(userInfo.getIs_login(), "yes")) {
            UserInfoManager.getSingleton().saveLoginInfo(this, userInfo);
            if (requestCode == -1) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                setResult(RESULT_OK);
                finish();
            }
        } else {
            SendCodeActivity.lunch(this, 0, openid, source, mThirdUserName, mThirdUsericon);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    private void otherLogin(String openid, String source) {
        String phone_xh = Utils.getSystemModel();
        Request<String> request = ParameterUtils.getSingleton().getPassportIsOpenId(openid, source, Utils.getVersionName(this), phone_xh);
        CallServer.getRequestInstance().add(this, AppConfigs.passport_isOpenid, request, this, true, false);
    }


    private String mThirdUserName;
    private String mThirdUsericon;

    private void getUserInfo() {
        UMShareAPI.get(this).getPlatformInfo(this, mSnsPlatform.mPlatform, new MyUMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> data) {
                mThirdUserName = data.get("name");
                mThirdUsericon = data.get("iconurl");
                if (share_media == SHARE_MEDIA.QQ) {
                    openid = data.get("uid");
                    source = "10402";
                } else if (share_media == SHARE_MEDIA.WEIXIN) {
                    openid = data.get("uid");
                    source = "10404";
                } else {
                    openid = data.get("uid");
                    source = "10403";
                }
                deleteOauth();
            }
        });
    }


    private void deleteOauth() {
        UMShareAPI.get(LoginActivity.this).deleteOauth(LoginActivity.this, mSnsPlatform.mPlatform, new MyUMAuthListener(){
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                otherLogin(openid, source);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                otherLogin(openid, source);
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {

                otherLogin(openid, source);
            }
        });
    }

}
