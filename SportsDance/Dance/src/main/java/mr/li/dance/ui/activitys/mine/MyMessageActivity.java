package mr.li.dance.ui.activitys.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.yolanda.nohttp.rest.Request;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.MyMessageInfo;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.MyMessageAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页-我的-我的消息
 * 修订历史:
 */

public class MyMessageActivity extends BaseListActivity<MyMessageInfo> {
    MyMessageAdapter mMyMessageAdapter;


    @Override
    public void initDatas() {
        super.initDatas();
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = ParameterUtils.getSingleton().getMyMessage(userId, 1);
        request(AppConfigs.user_myInfo, request, true);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("我的消息");
    }

    @Override
    public void itemClick(int position, MyMessageInfo value) {
        MessageDetailActivity.lunch(this,value.getId());
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mMyMessageAdapter = new MyMessageAdapter(this);
        mMyMessageAdapter.setItemClickListener(this);
        return mMyMessageAdapter;
    }

    @Override
    public int getContentViewId() {
        return R.layout.list_layout;
    }

    @Override
    public void getIntentData() {

    }

    public static void lunch(Context context) {
        context.startActivity(new Intent(context, MyMessageActivity.class));
    }

    @Override
    public void refresh() {
        super.refresh();
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = ParameterUtils.getSingleton().getMyMessage(userId, 1);
        request(AppConfigs.user_myInfo, request, false);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = ParameterUtils.getSingleton().getMyMessage(userId, mMyMessageAdapter.getNextPage());
        request(AppConfigs.user_myInfo, request, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
        List<MyMessageInfo> list = JsonMananger.jsonToList(stringResponse.getData(), MyMessageInfo.class);
        mMyMessageAdapter.addList(isRefresh, list);
    }
}
