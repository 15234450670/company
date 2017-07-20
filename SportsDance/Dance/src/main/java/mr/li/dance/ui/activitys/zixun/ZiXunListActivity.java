package mr.li.dance.ui.activitys.zixun;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.ZiXunIndexResponse;
import mr.li.dance.models.ZiXunInfo;
import mr.li.dance.ui.activitys.MyDanceWebActivity;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.ConsultationAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 资讯列表
 * 修订历史:
 */


public class ZiXunListActivity extends BaseListActivity<ZiXunInfo> {
    ConsultationAdapter mAdapter;
    String mTypeName;
    String mTypeId;

    @Override
    public void itemClick(int position, ZiXunInfo xunInfo) {

        String title = xunInfo.getName();
        if (MyStrUtil.isEmpty(title)) {
            title = xunInfo.getTitle();
        }
        String url = String.format(AppConfigs.ZixunShareUrl, String.valueOf(xunInfo.getId()));
        MyDanceWebActivity.lunch(this, MyDanceWebActivity.ZIXUNTYPE, title,url, true);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle(mTypeName);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new ConsultationAdapter(this, this);
        return mAdapter;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mTypeName = mIntentExtras.getString("typename");
        mTypeId = mIntentExtras.getString("typeid");
    }

    @Override
    public int getContentViewId() {
        return R.layout.list_layout;
    }

    @Override
    public void initDatas() {
        super.initDatas();
        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        Request<String> request = ParameterUtils.getSingleton().getZixunListMap(1, mTypeId);
        request(AppConfigs.home_dianboList, request, false);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        Request<String> request = ParameterUtils.getSingleton().getZixunListMap(mAdapter.getNextPage(), mTypeId);
        request(AppConfigs.home_dianboList, request, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        ZiXunIndexResponse reponseResult = JsonMananger.getReponseResult(response, ZiXunIndexResponse.class);
        if (isRefresh) {
            mAdapter.refresh(reponseResult);
        } else {
            mAdapter.loadMore(reponseResult);
        }
    }

    public static void lunch(Context context, String typename, String typeid) {
        Intent intent = new Intent(context, ZiXunListActivity.class);
        intent.putExtra("typename", typename);
        intent.putExtra("typeid", typeid);
        context.startActivity(intent);
    }

}
