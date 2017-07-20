package mr.li.dance.ui.fragments.homepage;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.HomeZhiBoIndexResponse;
import mr.li.dance.https.response.HomeZhiBoResponse;
import mr.li.dance.models.ZhiBoInfo;
import mr.li.dance.ui.activitys.base.DanceApplication;
import mr.li.dance.ui.activitys.video.ZhiBoDetailActivity;
import mr.li.dance.ui.adapters.DirectseedingAdapter;
import mr.li.dance.ui.fragments.BaseListFragment;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-直播页面
 * 修订历史:
 */
public class DirectseedingFragment extends BaseListFragment {
    DirectseedingAdapter mAdapter;

    @Override
    public void initViews() {
        super.initViews();
        initRefreshLayout();
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new DirectseedingAdapter(getActivity(), this);
        return mAdapter;
    }

    @Override
    public int getContentView() {
        return R.layout.list_layout;
    }

    @Override
    public void initData() {
        Request<String> request = ParameterUtils.getSingleton().getHomeZhiboMap();
        request(AppConfigs.home_zhibo, request, false);
    }

    @Override
    public void refresh() {
        super.refresh();
        Request<String> request = ParameterUtils.getSingleton().getHomeZhiboMapFromServer();
        request(AppConfigs.home_zhibo, request, false);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        Request<String> request = ParameterUtils.getSingleton().getHomeZhiboPageMap(mAdapter.getNextPage());
        request(AppConfigs.home_zhiboPage, request, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (what == AppConfigs.home_zhibo) {
            HomeZhiBoResponse homeResponse = JsonMananger.getReponseResult(response, HomeZhiBoResponse.class);
            mAdapter.refresh(homeResponse);
        } else {
            HomeZhiBoIndexResponse homeResponse = JsonMananger.getReponseResult(response, HomeZhiBoIndexResponse.class);
            mAdapter.loadMore(homeResponse);
        }

    }

    @Override
    public void itemClick(int position, Object value) {
        startLeCloudActionLive((ZhiBoInfo) value);
    }

    private void startLeCloudActionLive(ZhiBoInfo zhiBoInfo) {
        if (!DanceApplication.cdeInitSuccess) {
            Toast.makeText(getActivity(), "CDE未初始化完成,不能播放...", Toast.LENGTH_SHORT).show();
            return;
        }

        ZhiBoDetailActivity.lunch(getActivity(), zhiBoInfo.getId());
    }
}
