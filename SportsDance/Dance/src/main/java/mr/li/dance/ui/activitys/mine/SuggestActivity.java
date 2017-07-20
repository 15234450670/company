package mr.li.dance.ui.activitys.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import java.util.Collections;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.https.response.SuggestResponse;
import mr.li.dance.models.SuggestInfo;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.SuggestAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页-我的-意见反馈
 * 修订历史:
 */


public class SuggestActivity extends BaseListActivity implements View.OnClickListener {
    SuggestAdapter mSuggestAdapter;


    @Override
    public void initViews() {
        super.initViews();
        mRefreshLayout.setEnableLoadmore(false);
        setTitle("意见反馈");
        mDanceViewHolder.setClickListener(R.id.submit_btn, this);
    }

    @Override
    public void refresh() {
        super.refresh();
        String userId = UserInfoManager.getSingleton().getUserInfo(this).getUserid();
        Request<String> request = ParameterUtils.getSingleton().getUserOpinionMap(userId);
        request(AppConfigs.user_opinion, request);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        String userId = UserInfoManager.getSingleton().getUserInfo(this).getUserid();
        Request<String> request = ParameterUtils.getSingleton().getUserOpinionMap(userId);
        request(AppConfigs.user_opinion, request, true);
    }

    @Override
    public void itemClick(int position, Object value) {

    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mSuggestAdapter = new SuggestAdapter(this);
        return mSuggestAdapter;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_suggest;
    }

    public static void lunch(Context context) {
        context.startActivity(new Intent(context, SuggestActivity.class));
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (what == AppConfigs.user_opinion) {
            SuggestResponse suggestResponse = JsonMananger.getReponseResult(response, SuggestResponse.class);
            List list = suggestResponse.getData();
            if (!MyStrUtil.isEmpty(list)) {
                Collections.reverse(list); // 倒序排列
            }
            mSuggestAdapter.addList(true, list);
        } else {
            closeKeyboard();
            StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
            NToast.shortToast(this, stringResponse.getData());
            SuggestInfo suggestInfo = new SuggestInfo();
            String content = mDanceViewHolder.getTextValue(R.id.input_et);
            suggestInfo.setProblem(content);
            mSuggestAdapter.add(suggestInfo);
            mDanceViewHolder.setText(R.id.input_et, "");
        }
        mRecyclerview.getLayoutManager().scrollToPosition(mSuggestAdapter.getItemCount() - 1);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_btn) {
            submit();
        }
    }

    private void submit() {
        String content = mDanceViewHolder.getTextValue(R.id.input_et);
        if (MyStrUtil.isEmpty(content)) {
            NToast.shortToast(this, "请输入反馈内容");
        } else {
            String userName = UserInfoManager.getSingleton().getUserInfo(this).getUsername();
            String userId = UserInfoManager.getSingleton().getUserInfo(this).getUserid();
            Request<String> request = ParameterUtils.getSingleton().getSendOpinionMap(userName, userId, content);
            request(AppConfigs.user_sendOpinion, request, true);
        }
    }
}
