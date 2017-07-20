package mr.li.dance.ui.activitys.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.yolanda.nohttp.rest.Request;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.HomeVideoIndexResponse;
import mr.li.dance.models.BaseItemAdapterType;
import mr.li.dance.models.Video;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.BaseItemAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 视频列表页面
 * 修订历史:
 */


public class VideoListActivity extends BaseListActivity<Video> {
    BaseItemAdapter mAdapter;
    String mTypeName;
    String mTypeId;

    @Override
    public void itemClick(int position, Video value) {
        VideoDetailActivity.lunch(this, value.getId());

    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle(mTypeName);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new BaseItemAdapter(this, BaseItemAdapterType.CommentType);
        mAdapter.setItemClickListener(this);
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
        Request<String> request = ParameterUtils.getSingleton().getVideoListMap(1, mTypeId);
        request(AppConfigs.home_dianboList, request, false);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        Request<String> request = ParameterUtils.getSingleton().getVideoListMap(mAdapter.getNextPage(), mTypeId);
        request(AppConfigs.home_dianboList, request, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        HomeVideoIndexResponse reponseResult = JsonMananger.getReponseResult(response, HomeVideoIndexResponse.class);
        if (isRefresh) {
            mAdapter.refresh(reponseResult.getData());
        } else {
            mAdapter.loadMore(reponseResult.getData());
        }
    }

    public static void lunch(Context context, String typename,String app_type_id) {
        Intent intent = new Intent(context, VideoListActivity.class);
        intent.putExtra("typename", typename);
        intent.putExtra("typeid", app_type_id);
        context.startActivity(intent);
    }
}
