package mr.li.dance.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.activitys.mine.UpdateInfoActivity;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 完善个人信息页面
 * 修订历史:
 */

public class PerfectInfoActivity extends BaseActivity implements View.OnClickListener {
    private String mUserId;
    private boolean isFromRegist;

    @Override
    public int getContentViewId() {
        return R.layout.activity_perfectinfo;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mUserId = mIntentExtras.getString("userid");
        isFromRegist = mIntentExtras.getBoolean("isfromregist", false);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        registFinishBooadCast();
    }

    @Override
    public void initViews() {
        if (isFromRegist) {
            setTitleAndRightBtn("完善资料", "跳过");
        } else {
            setTitle("完善资料");
        }

    }

    public static void lunch(Context context, String userId) {
        Intent intent = new Intent(context, PerfectInfoActivity.class);
        intent.putExtra("userid", userId);
        intent.putExtra("isfromregist", true);
        context.startActivity(intent);
    }

    public static void lunch(Fragment context, String userId, int requestCode) {
        Intent intent = new Intent(context.getActivity(), PerfectInfoActivity.class);
        intent.putExtra("userid", userId);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.name_layout:
                String oldName = mDanceViewHolder.getTextValue(R.id.name_tv);
                UpdateInfoActivity.Lunch(this, oldName, "姓名", 0x001, true);
                break;
            case R.id.card_layout:
                String oldCard = mDanceViewHolder.getTextValue(R.id.nick_tv);
                UpdateInfoActivity.Lunch(this, oldCard, "身份证号", 0x002, true);
                break;
            case R.id.submit_btn:
                subimt();
                break;
        }
    }

    private void subimt() {
        String name = mDanceViewHolder.getTextValue(R.id.name_tv);
        String card = mDanceViewHolder.getTextValue(R.id.card_tv);

        if (MyStrUtil.isEmpty(name)) {
            NToast.shortToast(this, "请输入姓名");
            return;
        }
        if (MyStrUtil.isEmpty(card)) {
            NToast.shortToast(this, "请再次输入身份证号");
            return;
        }
        Request<String> request = ParameterUtils.getSingleton().getReplenishInfoMap(mUserId, name, card);
        request(AppConfigs.passport_nickname, request, true);
    }

    @Override
    public void onSucceed(int what, String responseStr) {
        super.onSucceed(what, responseStr);
        StringResponse response = JsonMananger.getReponseResult(responseStr, StringResponse.class);
        NToast.shortToast(this, response.getData());

        String name = mDanceViewHolder.getTextValue(R.id.name_tv);
        String card = mDanceViewHolder.getTextValue(R.id.card_tv);

        UserInfoManager.getSingleton().saveReal_name(this, name);
        UserInfoManager.getSingleton().saveId_card(this, card);

        if (isFromRegist) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onHeadRightButtonClick(View v) {
        super.onHeadRightButtonClick(v);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        String inputcontent = data.getStringExtra("inputcontent");
        if (requestCode == 0x001) {
            mDanceViewHolder.setText(R.id.name_tv, inputcontent);
        } else {
            mDanceViewHolder.setText(R.id.card_tv, inputcontent);
        }
    }
}
