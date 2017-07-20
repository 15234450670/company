package mr.li.dance.ui.fragments.homepage;

import android.support.v7.widget.RecyclerView;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.HomeIndexResponse;
import mr.li.dance.https.response.HomeResponse;
import mr.li.dance.ui.adapters.HomeRecyclerAdapter;
import mr.li.dance.ui.fragments.BaseListFragment;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页- 推荐页面
 * 修订历史:
 */
public class RecommendFragment extends BaseListFragment {
    HomeRecyclerAdapter mAdapter;

    @Override
    public void initData() {
        Request<String> request = ParameterUtils.getSingleton().getHomeIndexMap();
        request(AppConfigs.home_index, request, false);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_list_layout;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new HomeRecyclerAdapter(getActivity());
        return mAdapter;
    }


    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (what == AppConfigs.home_index) {
            HomeResponse homeResponse = JsonMananger.getReponseResult(response, HomeResponse.class);
            mAdapter.refresh(homeResponse);
        } else {
            HomeIndexResponse homeResponse = JsonMananger.getReponseResult(response, HomeIndexResponse.class);
            mAdapter.loadMore(homeResponse);
        }

    }

    @Override
    public void refresh() {
        super.refresh();
        Request<String> request = ParameterUtils.getSingleton().getHomeIndexMap();
        request(AppConfigs.home_index, request, false);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        Request<String> request = ParameterUtils.getSingleton().getHomeIndexPageMap(mAdapter.getNextPage());
        request(AppConfigs.home_index_page, request, false);
    }

    @Override
    public void itemClick(int position, Object value) {

    }
}
