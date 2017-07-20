package mr.li.dance.ui.activitys.match;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.MatchIndexResponse;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.Match;
import mr.li.dance.ui.activitys.album.ImageShowActivity;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.AlbumAdapter;
import mr.li.dance.ui.adapters.ListViewItemClickListener;
import mr.li.dance.ui.adapters.MatchAdatper;
import mr.li.dance.ui.dialogs.YearSelectDialog;
import mr.li.dance.ui.widget.SpacesItemDecoration;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DateUtils;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 赛事图片按背号搜索页面
 * 修订历史:
 */
public class SearchMatchPicActivity extends BaseListActivity<AlbumInfo> {

    AlbumAdapter mAlbumAdapter;

    private String mCompleteId;

    @Override
    public void itemClick(int position, AlbumInfo value) {
        ArrayList<String> urls = new ArrayList<String>();
        List<AlbumInfo> list = mAlbumAdapter.getmList();
        for (AlbumInfo albumInfo : list) {
            urls.add(albumInfo.getPhoto());
        }
        ImageShowActivity.lunch(this, urls,position+1);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mCompleteId = mIntentExtras.getString("completeid");
    }

    @Override
    public void initDatas() {
        super.initDatas();

    }

    @Override
    public void initViews() {
        setHeadVisibility(View.GONE);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) getResources().getDimension(R.dimen.spacing_20));
        mRecyclerview.addItemDecoration(decoration);
        mRecyclerview.setAdapter(getAdapter());
        mDanceViewHolder.setClickListener(R.id.search_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                refresh();
            }
        });
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAlbumAdapter = new AlbumAdapter(this);
        mAlbumAdapter.setItemClickListener(this);
        return mAlbumAdapter;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_searchmatchpic;
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse reponseResult = JsonMananger.getReponseResult(response, StringResponse.class);
        List<AlbumInfo> list = JsonMananger.jsonToList(reponseResult.getData(), AlbumInfo.class);
        mAlbumAdapter.addList(isRefresh, list);
    }

    public static void lunch(Context context, String completeid) {
        Intent intent = new Intent(context, SearchMatchPicActivity.class);
        intent.putExtra("completeid", completeid);
        context.startActivity(intent);
    }


    private void getData() {
        Request<String> request = null;
        String beihao = mDanceViewHolder.getTextValue(R.id.search_et);
        request = ParameterUtils.getSingleton().getPhotoSearchMap(beihao, mCompleteId);
        request(AppConfigs.match_photoSearch, request, false);
    }

    @Override
    public void refresh() {
        super.refresh();
        getData();
    }

    @Override
    public void loadMore() {
        super.loadMore();
        getData();
    }

}
