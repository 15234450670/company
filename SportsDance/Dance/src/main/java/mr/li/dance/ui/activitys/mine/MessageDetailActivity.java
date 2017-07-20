package mr.li.dance.ui.activitys.mine;

import android.content.Context;
import android.content.Intent;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.MyMessageInfo;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.UserInfoManager;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 消息详情页面
 * 修订历史:
 */
public class MessageDetailActivity extends BaseActivity {

    private int msg_id;

    @Override
    public int getContentViewId() {
        return R.layout.activity_messagedetail;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        msg_id = mIntentExtras.getInt("msg_id");
    }

    @Override
    public void initDatas() {
        super.initDatas();
        Request<String> request = ParameterUtils.getSingleton().getMyMessageDetail(msg_id);
        request(AppConfigs.user_myInfo, request, true);
    }

    @Override
    public void initViews() {

    }

    public static void lunch(Context context, int msg_id) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra("msg_id", msg_id);
        context.startActivity(intent);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
        MyMessageInfo myMessageInfo = JsonMananger.getReponseResult(stringResponse.getData(), MyMessageInfo.class);
        setTitle(myMessageInfo.getTitle());
        mDanceViewHolder.setText(R.id.content_tv, myMessageInfo.getContent());
    }
}
