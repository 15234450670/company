package mr.li.dance.ui.activitys.mine;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.ui.activitys.SetPwdActivity;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DownTimer;
import mr.li.dance.utils.DownTimerListener;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页-我的-点头像-个人信息-绑定电话
 * 修订历史:
 */


public class UpdateMobileActivity extends BaseActivity implements View.OnClickListener, DownTimerListener {
    private TextView mGetCodeTv;
    boolean isBright = true;


    @Override
    public int getContentViewId() {
        return R.layout.activity_updatemobile;
    }

    @Override
    public void initViews() {
        setTitle("修改手机号");
        mDanceViewHolder.setClickListener(R.id.getcode_tv, this);
        mGetCodeTv = (TextView) mDanceViewHolder.getView(R.id.getcode_tv);
    }

    public static void Lunch(Activity context, String oldContent, int requestCode) {
        Intent intent = new Intent(context, UpdateMobileActivity.class);
        intent.putExtra("oldcontent", oldContent);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getcode_tv:
                String mobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
                if (MyStrUtil.isEmpty(mobile)) {
                    NToast.shortToast(this, "请输入手机号");
                } else {
                    getCode(mobile);
                }
                break;
            case R.id.nextstep_btn:
                changeMoble();
                break;
        }
    }

    private void getCode(String mobile) {
        Request<String> request = ParameterUtils.getSingleton().getCodeMap(mobile);
        request(AppConfigs.passport_sendIdentifyCode, request);

    }

    private void changeMoble() {
        String mobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
        String code = mDanceViewHolder.getTextValue(R.id.code_tv);
        if (MyStrUtil.isEmpty(code)) {
            NToast.shortToast(this, "请输入验证码");
        } else {
            String userId = UserInfoManager.getSingleton().getUserId(this);
            Request<String> request = ParameterUtils.getSingleton().getUpdateMobileMap(userId, mobile, code);
            request(AppConfigs.myInfo_edMobile, request, true);
        }
    }

    @Override
    public void onSucceed(int what, String response) {
        StringResponse reponseResult = JsonMananger.getReponseResult(response, StringResponse.class);
        super.onSucceed(what, response);
        switch (what) {
            case AppConfigs.passport_sendIdentifyCode:
                DownTimer downTimer = new DownTimer();
                downTimer.setListener(this);
                downTimer.startDown(60 * 1000);
                NToast.shortToast(this, reponseResult.getData());
                break;
            case AppConfigs.myInfo_edMobile:
                NToast.shortToast(this, reponseResult.getData());
                String mobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
                UserInfoManager.getSingleton().saveMobile(this, mobile);
                finish();
                break;
        }
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
}
