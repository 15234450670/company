package mr.li.dance.ui.fragments.homepage;

import android.support.v7.widget.RecyclerView;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.HomeZxResponse;
import mr.li.dance.https.response.ZiXunIndexResponse;
import mr.li.dance.models.ZiXunInfo;
import mr.li.dance.ui.activitys.MyDanceWebActivity;

import mr.li.dance.ui.adapters.ConsultationPageAdapter;
import mr.li.dance.ui.fragments.BaseListFragment;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-咨询页面
 * 修订历史:
 */

public class ConsultationFragment extends BaseListFragment {
    ConsultationPageAdapter mPageAdapter;

    @Override
    public void initViews() {
        super.initViews();
        danceViewHolder.setText(R.id.title_tv, "咨询");

    }

    @Override
    public int getContentView() {
        return R.layout.fragment_list_layout;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mPageAdapter = new ConsultationPageAdapter(getActivity(), this);
        return mPageAdapter;
    }

    @Override
    public void initData() {
        Request<String> request = ParameterUtils.getSingleton().getHomeZxMap();
        request(AppConfigs.home_zx, request, false);

    }

    @Override
    public void refresh() {
        super.refresh();
        Request<String> request = ParameterUtils.getSingleton().getHomeZxMap();
        request(AppConfigs.home_zx, request, false);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        Request<String> request = ParameterUtils.getSingleton().getHomeZxPageMap(mPageAdapter.getNextPage());
        request(AppConfigs.home_zxPage, request, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (what == AppConfigs.home_zx) {
            HomeZxResponse reponseResult = JsonMananger.getReponseResult(response, HomeZxResponse.class);
            mPageAdapter.refresh(reponseResult);
        } else {
            ZiXunIndexResponse indexResponse = JsonMananger.getReponseResult(response, ZiXunIndexResponse.class);
            mPageAdapter.loadMore(indexResponse);
        }

    }

    @Override
    public void itemClick(int position, Object value) {
        ZiXunInfo ziXunInfo = (ZiXunInfo) value;
        String url = String.format(AppConfigs.ZixunShareUrl, String.valueOf(ziXunInfo.getId()));
        MyDanceWebActivity.lunch(getActivity(), MyDanceWebActivity.ZIXUNTYPE, ziXunInfo.getTitle(),url,  true);
    }

}
