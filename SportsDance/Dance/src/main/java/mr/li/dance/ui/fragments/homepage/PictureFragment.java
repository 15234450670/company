package mr.li.dance.ui.fragments.homepage;

import android.support.v7.widget.RecyclerView;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.HomeAlbumResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.ui.activitys.album.AlbumActivity;
import mr.li.dance.ui.adapters.PicPageAdapter;
import mr.li.dance.ui.fragments.BaseListFragment;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页- 图片页面
 * 修订历史:
 */
public class PictureFragment extends BaseListFragment<AlbumInfo> {
    PicPageAdapter mPageAdapter;
    private final int requestCode = 0x001;

    @Override
    public void initData() {
        refresh();
    }

    @Override
    public void initViews() {
        super.initViews();
        danceViewHolder.setText(R.id.title_tv, "图片");
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_list_layout;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mPageAdapter = new PicPageAdapter(getActivity());
        mPageAdapter.setItemClickListener(this);
        return mPageAdapter;
    }

    @Override
    public void itemClick(int position, AlbumInfo value) {
        AlbumActivity.lunch(this, value.getId(),value.getClass_name());
    }

    @Override
    public void refresh() {
        super.refresh();
        Request<String> request = ParameterUtils.getSingleton().getHomeAlbumMap(1);
        request(AppConfigs.home_album, request, false);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        Request<String> request = ParameterUtils.getSingleton().getHomeAlbumMapFromServer(mPageAdapter.getNextPage());
        request(AppConfigs.home_album, request, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        HomeAlbumResponse reponseResult = JsonMananger.getReponseResult(response, HomeAlbumResponse.class);
        mPageAdapter.addList(isRefresh, reponseResult.getData());

    }
}
