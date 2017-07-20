package mr.li.dance.ui.activitys.match;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.MatchPicResponse;
import mr.li.dance.https.response.MatchVideoResponse;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.Match;
import mr.li.dance.models.Video;
import mr.li.dance.ui.activitys.album.AlbumActivity;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.activitys.video.VideoDetailActivity;
import mr.li.dance.ui.adapters.MatchVideoAdapter;
import mr.li.dance.ui.adapters.WonderfulPicAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 精彩图片
 * 修订历史:
 */
public class WonderfulActivity extends BaseListActivity<AlbumInfo> {
    WonderfulPicAdapter mWonderfulPicAdapter;
    private String mMatchId;
    private String mTitle;

    @Override
    public int getContentViewId() {
        return R.layout.list_layout;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mMatchId = mIntentExtras.getString("matchid");
        mTitle = mIntentExtras.getString("title");
    }

    @Override
    public void initDatas() {
        super.initDatas();
        refresh();
    }

    @Override
    public void initViews() {
        super.initViews();
        mRefreshLayout.setEnableLoadmore(false);
        setTitle("赛事图片");
        setRightImage(R.drawable.home_icon_007);
    }

    @Override
    public void itemClick(int position, AlbumInfo value) {
        if (value != null) {
            AlbumActivity.lunch(this, value.getId(),value.getClass_name());
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        Request<String> request = ParameterUtils.getSingleton().getWonderfulPicListMap(mMatchId, mTitle);
        request(AppConfigs.match_jingcaiPhoto, request, false);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mWonderfulPicAdapter = new WonderfulPicAdapter(this);
        mWonderfulPicAdapter.setItemClickListener(this);
        return mWonderfulPicAdapter;
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
        String dataStr = stringResponse.getData();
        if (what == AppConfigs.match_jingcaiPhoto) {
            MatchPicResponse videoResponse = JsonMananger.getReponseResult(dataStr, MatchPicResponse.class);
            Match match = new Match();
            match.setType(videoResponse.getType());
            match.setTitle(videoResponse.getTitle());
            match.setAddress(videoResponse.getAddress());
            mWonderfulPicAdapter.setmMatchInfo(match);
            mWonderfulPicAdapter.addList(isRefresh, videoResponse.getAlbum());
        } else {
//            List<Video> list = JsonMananger.jsonToList(dataStr, Video.class);
//            mWonderfulPicAdapter.addList(isRefresh, list);
        }

    }


    public static void lunch(Context context, String mMatchId, String mathcName) {
        Intent intent = new Intent(context, WonderfulActivity.class);
        intent.putExtra("matchid", mMatchId);
        intent.putExtra("title", mathcName);
        context.startActivity(intent);
    }

    @Override
    public void onHeadRightButtonClick(View v) {
        super.onHeadRightButtonClick(v);
        SearchMatchPicActivity.lunch(this, mMatchId);
    }
}
