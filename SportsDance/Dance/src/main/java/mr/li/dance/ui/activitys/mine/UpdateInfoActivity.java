package mr.li.dance.ui.activitys.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.broadcast.BroadcastManager;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.UserInfo;
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
 * 描述: 个人信息的修改或者录入
 * 修订历史:
 */


public class UpdateInfoActivity extends BaseActivity {
    private String oldContent;
    private String mTitle;
    private UserInfo mUserInfo;
    public static final int updateRealName = 0x011;
    public static final int updateIdCard = 0x022;
    public static final int updateNick = 0x033;
    private int requestCode = 0;
    private boolean needCallBack;

    @Override
    public int getContentViewId() {
        return R.layout.activity_updateinfo;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        oldContent = mIntentExtras.getString("oldcontent");
        mTitle = mIntentExtras.getString("title");
        needCallBack = mIntentExtras.getBoolean("needcallback", false);

        requestCode = mIntentExtras.getInt("requestcode");
    }

    @Override
    public void initDatas() {
        super.initDatas();
        mUserInfo = UserInfoManager.getSingleton().getUserInfo(this);
    }

    @Override
    public void initViews() {
        setTitleAndRightBtn(mTitle, "保存");
        mDanceViewHolder.setText(R.id.update_et, oldContent);
    }

    public static void Lunch(Activity context, String oldContent, String title, int requestCode, boolean needCallBack) {
        Intent intent = new Intent(context, UpdateInfoActivity.class);
        intent.putExtra("oldcontent", oldContent);
        intent.putExtra("title", title);
        intent.putExtra("requestcode", requestCode);
        intent.putExtra("needcallback", needCallBack);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onHeadRightButtonClick(View v) {
        super.onHeadRightButtonClick(v);
        String inputContent = mDanceViewHolder.getTextValue(R.id.update_et);

        if (MyStrUtil.isEmpty(inputContent)) {
            NToast.shortToast(this, "请输入提交的内容");
            return;
        }
        if (needCallBack) {
            Intent intent = new Intent();
            intent.putExtra("inputcontent", inputContent);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            updateUserInfo(inputContent);
        }
    }

    private void updateUserInfo(String inputContent) {
        Request<String> request = null;
        switch (requestCode) {
            case updateRealName:
                request = ParameterUtils.getSingleton().getUpdateRealNameMap(mUserInfo.getUserid(), inputContent);
                break;
            case updateIdCard:
                inputContent = inputContent.toUpperCase();
                request = ParameterUtils.getSingleton().getUpdateCardMap(mUserInfo.getUserid(), inputContent);
                break;
            case updateNick:
                request = ParameterUtils.getSingleton().getUpdateNickMap(mUserInfo.getUserid(), inputContent);
                break;
        }
        if (request != null) {
            request(AppConfigs.user_photo, request, true);
        }
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
        NToast.shortToast(this, stringResponse.getData());
        String inputContent = mDanceViewHolder.getTextValue(R.id.update_et);
        switch (requestCode) {
            case updateRealName:
                UserInfoManager.getSingleton().saveReal_name(this, inputContent);
                break;
            case updateIdCard:
                UserInfoManager.getSingleton().saveId_card(this, inputContent);
                break;
            case updateNick:
                UserInfoManager.getSingleton().saveUsername(this, inputContent);
                break;
        }
        setResult(RESULT_OK);
        finish();
    }
}
