package mr.li.dance.ui.activitys.match;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.yolanda.nohttp.rest.Request;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.ScoreGroupInfo;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.ScoreGroupAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 成绩查询列表页面
 * 修订历史:
 */
public class ScoreGroupActivity extends BaseListActivity<ScoreGroupInfo> {
    ScoreGroupAdapter mScorefromAdapter;
    private String mMatchId;


    @Override
    public int getContentViewId() {
        return R.layout.activity_groupscore;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mMatchId = mIntentExtras.getString("matchid");
    }


    @Override
    public void initViews() {
        super.initViews();
        setTitle("成绩单");
        Request<String> request = ParameterUtils.getSingleton().getmScoreQueryMap(mMatchId, 1);
        request(AppConfigs.match_scoreQuery, request, true);
    }

    @Override
    public void itemClick(int position, ScoreGroupInfo value) {
        ScoreFromActivity.lunch(this, mMatchId, value.getGroup_name());
    }

    private void getData(int index) {
        Request<String> request = ParameterUtils.getSingleton().getmScoreQueryMap(mMatchId, index);
        request(AppConfigs.match_scoreQuery, request, false);
    }

    @Override
    public void refresh() {
        super.refresh();
        getData(1);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        getData(mScorefromAdapter.getNextPage());
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mScorefromAdapter = new ScoreGroupAdapter(this);
        mScorefromAdapter.setItemClickListener(this);
        return mScorefromAdapter;
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
        String dataStr = stringResponse.getData();
        List<ScoreGroupInfo> list = JsonMananger.jsonToList(dataStr, ScoreGroupInfo.class);
        mScorefromAdapter.addList(isRefresh, list);
    }


    public static void lunch(Context context, String matchId) {
        Intent intent = new Intent(context, ScoreGroupActivity.class);
        intent.putExtra("matchid", matchId);
        context.startActivity(intent);
    }

}
