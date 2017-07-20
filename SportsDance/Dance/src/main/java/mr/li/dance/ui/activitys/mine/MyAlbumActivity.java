package mr.li.dance.ui.activitys.mine;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.HomeAlbumResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.BaseItemAdapterType;
import mr.li.dance.ui.activitys.album.AlbumActivity;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.BaseItemAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页-我的-我的相册
 * 修订历史:
 */

public class MyAlbumActivity extends BaseListActivity<AlbumInfo> {
    BaseItemAdapter mItemAdapter;

    @Override
    public void itemClick(int position, AlbumInfo value) {
        AlbumActivity.lunch(this, value.getId(),value.getClass_name());
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mItemAdapter = new BaseItemAdapter(this, BaseItemAdapterType.CommentType);
        mItemAdapter.setItemClickListener(this);
        return mItemAdapter;
    }

    @Override
    public int getContentViewId() {
        return R.layout.list_myalbum;
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("我的相册");
        mRefreshLayout.setEnableLoadmore(false);
    }

    public static void lunch(Activity context, int requestCode) {
        Intent intent = new Intent(context, MyAlbumActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = ParameterUtils.getSingleton().getMyAlbumMap(userId);
        request(AppConfigs.user_myAlbun, request);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        HomeAlbumResponse albumResponse = JsonMananger.getReponseResult(response, HomeAlbumResponse.class);
        List list = albumResponse.getData();
        if (!MyStrUtil.isEmpty(list)) {
            mItemAdapter.addList(true, list);
        }
        if(mItemAdapter.getItemCount() == 0){
            mDanceViewHolder.setViewVisibility(R.id.nodatalayout, View.VISIBLE);
        }else{
            mDanceViewHolder.setViewVisibility(R.id.nodatalayout, View.GONE);
        }
    }
}
