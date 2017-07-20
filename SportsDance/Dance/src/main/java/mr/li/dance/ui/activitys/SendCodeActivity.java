package mr.li.dance.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yolanda.nohttp.rest.Request;

import java.util.Map;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.https.response.UserInfoResponse;
import mr.li.dance.models.UserInfo;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.activitys.base.DanceApplication;
import mr.li.dance.ui.dialogs.CommonDialog;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DownTimer;
import mr.li.dance.utils.DownTimerListener;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;
import mr.li.dance.utils.Utils;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 发送验证码
 * 修订历史:
 */

public class SendCodeActivity extends BaseActivity implements View.OnClickListener, DownTimerListener {
    private TextView mGetCodeTv;
    boolean isBright = true;
    private int mCodeType;//-1、手机号注册 0/第三方注册 1 找回密码

    @Override
    public int getContentViewId() {
        return R.layout.activity_registfirststep;
    }

    @Override
    public void initDatas() {
        super.initDatas();
        registFinishBooadCast();
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mCodeType = mIntentExtras.getInt("codetype", -1);
    }

    @Override
    public void initViews() {
        if (mCodeType == -1 || mCodeType == 0) {
            setTitle("设置手机号");
        } else if (mCodeType == 1) {
            setTitle("找回密码");
        }

        mDanceViewHolder.setClickListener(R.id.getcode_tv, this);
        mGetCodeTv = (TextView) mDanceViewHolder.getView(R.id.getcode_tv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextstep_btn:
                checkCode();
                break;
            case R.id.getcode_tv:
                String mobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
                if (MyStrUtil.isEmpty(mobile)) {
                    NToast.shortToast(this, "请输入手机号");
                } else {
                    getCode(mobile);
                }
                break;
        }
    }

    private void checkCode() {
        String mobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
        String code = mDanceViewHolder.getTextValue(R.id.code_tv);
        if (MyStrUtil.isEmpty(code)) {
            NToast.shortToast(this, "请输入验证码");
        } else {
            int type = 1;
            switch (mCodeType) {//-1、手机号注册 0/第三方注册 1 找回密码
                case -1:
                    type = 1;
                    break;
                case 0:
                    type = 2;
                    break;
                case 1:
                    type = 3;
                    break;
            }
            Request<String> request = ParameterUtils.getSingleton().getCheckCodeMap(type, mobile, code);
            request(AppConfigs.passport_register, request, true);
        }
    }


    public static void lunch(Context context, int codetype) {
        Intent intent = new Intent(context, SendCodeActivity.class);
        intent.putExtra("codetype", codetype);
        context.startActivity(intent);
    }

    public static void lunch(Context context, int codetype, String openid, String source, String username, String picture) {
        Intent intent = new Intent(context, SendCodeActivity.class);
        intent.putExtra("codetype", codetype);
        intent.putExtra("openid", openid);
        intent.putExtra("source", source);
        intent.putExtra("picture", picture);
        intent.putExtra("username", username);
        context.startActivity(intent);
    }

    public static void lunch(Context context) {
        lunch(context, -1);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mGetCodeTv.setText(String.valueOf(millisUntilFinished / 1000) + "s");
        mGetCodeTv.setClickable(false);
        mGetCodeTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.code_shape_bg));
        isBright = false;
    }

    @Override
    public void onFinish() {
        mDanceViewHolder.setText(R.id.getcode_tv, "获取验证码");
        mDanceViewHolder.getView(R.id.getcode_tv).setClickable(true);
        mGetCodeTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.pwdsubmit_shape_bg));
        isBright = true;
    }

    private void getCode(String mobile) {
        if (mCodeType == -1) {
            Request<String> request = ParameterUtils.getSingleton().getCodeMap(mobile);
            request(AppConfigs.passport_sendIdentifyCode, request);
        } else if (mCodeType == 1) {
            Request<String> request = ParameterUtils.getSingleton().getFindPwdCodeMap(mobile);
            request(AppConfigs.passport_sendIdentifyCode, request);
        } else {
            Request<String> request = ParameterUtils.getSingleton().getSendIdentifyCode_thirdMap(mobile);
            request(AppConfigs.passport_sendIdentifyCode, request);
        }

    }

    @Override
    public void onSucceed(int what, String response) {
        StringResponse reponseResult = JsonMananger.getReponseResult(response, StringResponse.class);
        super.onSucceed(what, response);
        switch (what) {
            case AppConfigs.passport_sendIdentifyCode:
                NToast.shortToast(this, reponseResult.getData());
                DownTimer downTimer = new DownTimer();
                downTimer.setListener(this);
                downTimer.startDown(60 * 1000);
                break;
            case AppConfigs.passport_register:
                if (mCodeType == 0) {
                    checkMoble();
                } else {
                    String mobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
                    SetPwdActivity.lunch(this, mobile, mCodeType);
                    finish();
                }
                break;
            case AppConfigs.passport_register_third:
                Map<String, Object> objectMap = JsonMananger.JsonStrToMap(reponseResult.getData());
                int type = (Integer) objectMap.get("type");
                if (type == 1) {
                    showCheckMobileDialog();
                } else {
                    String mobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
                    String openid = mIntentExtras.getString("openid");
                    String source = mIntentExtras.getString("source");
                    String picture = mIntentExtras.getString("picture");
                    String username = mIntentExtras.getString("username");

                    SetPwdActivity.lunch(this, mobile, mCodeType, openid, source, picture, username);
                }
                break;
            case AppConfigs.passport_edMobile:
                UserInfoResponse infoResponse = JsonMananger.getReponseResult(response, UserInfoResponse.class);
                UserInfo userInfo = infoResponse.getData();
                UserInfoManager.getSingleton().saveLoginInfo(this, userInfo);
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }


    private void showCheckMobileDialog() {
        String message = "您的手机号已存在，继续将与该账户绑定或者请您更换手机号";
        CommonDialog commonDialog = new CommonDialog(this, message, "更换", "确定");
        commonDialog.setClickListener(new CommonDialog.ClickListener() {
            @Override
            public void toConfirm(String message) {
                bindMobile();
            }

            @Override
            public void toRefause() {
                mDanceViewHolder.setText(R.id.mobile_tv, "");
                mDanceViewHolder.setText(R.id.code_tv, "");
            }
        });
        commonDialog.display();
    }

    private void bindMobile() {
        String openid = mIntentExtras.getString("openid");
        String source = mIntentExtras.getString("source");
        String picture = mIntentExtras.getString("picture");
        String mobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
        String deviceToken = DanceApplication.getInstance().getDeviceToken();

        String version = Utils.getVersionName(this);
        String phone_xh = Utils.getSystemModel();
        Request<String> request = ParameterUtils.getSingleton().getPassportEdMobile(deviceToken, openid, source, mobile, picture, version, phone_xh);
        request(AppConfigs.passport_edMobile, request);
    }


    @Override
    public void onFailed(int what, int responseCode, String response) {
        if (MyStrUtil.isEmpty(response)) {
            super.onFailed(what, responseCode, response);
        } else {
            StringResponse reponseResult = JsonMananger.getReponseResult(response, StringResponse.class);
            NToast.shortToast(this, reponseResult.getData());
        }

    }

    /**
     * 手机号是否绑定
     */
    private void checkMoble() {
        String mobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
        Request<String> request = ParameterUtils.getSingleton().getPassportRegister_thirdMap(mobile);
        request(AppConfigs.passport_register_third, request);
    }
}
