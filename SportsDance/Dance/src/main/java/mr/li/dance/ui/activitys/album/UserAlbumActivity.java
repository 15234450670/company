package mr.li.dance.ui.activitys.album;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.AlbumDetailResponse;
import mr.li.dance.https.response.AttentionResponse;
import mr.li.dance.https.response.PhotoIndexResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.ui.activitys.LoginActivity;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.UserAlbumAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页—图片-相册详情-个人名片
 * 修订历史:
 */

public class UserAlbumActivity extends BaseListActivity<AlbumInfo> {
    UserAlbumAdapter mAlbumAdapter;
    private String mAlbumUserId;
    private boolean isAttentioned;
    private boolean isNotSelfAlbum;

    @Override
    public void itemClick(int position, AlbumInfo value) {
        if (position == 0) {
            toGuanZhu();
        } else {
            AlbumActivity.lunch(this, value.getId(), value.getClass_name());
        }
    }

    @Override
    public void initDatas() {
        super.initDatas();
        String userId = UserInfoManager.getSingleton().getUserId(this);
        isNotSelfAlbum = !userId.equals(mAlbumUserId);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("相册名称");
        refresh();
    }


    @Override
    public RecyclerView.Adapter getAdapter() {
        mAlbumAdapter = new UserAlbumAdapter(this, isNotSelfAlbum);
        mAlbumAdapter.setItemClickListener(this);
        return mAlbumAdapter;
    }

    @Override
    public int getContentViewId() {
        return R.layout.list_layout;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mAlbumUserId = mIntentExtras.getString("albumuserid");
    }

    public static void lunch(Context context, String albumUserId) {
        Intent intent = new Intent(context, UserAlbumActivity.class);
        intent.putExtra("albumuserid", albumUserId);
        context.startActivity(intent);
    }

    @Override
    public void refresh() {
        super.refresh();
        String loginId = "";
        if (UserInfoManager.getSingleton().isLoading(this)) {
            loginId = UserInfoManager.getSingleton().getUserId(this);
        }
        Request<String> request = ParameterUtils.getSingleton().getPersonalListMap(loginId, mAlbumUserId);
        request(AppConfigs.home_album, request, false);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        Request<String> request = ParameterUtils.getSingleton().getPersonalListPageMap(mAlbumUserId, mAlbumAdapter.getNextPage());
        request(AppConfigs.home_photoDetail, request, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (what == AppConfigs.home_album) {
            AlbumDetailResponse reponseResult = JsonMananger.getReponseResult(response, AlbumDetailResponse.class);
            isAttentioned = reponseResult.getData().getIs_attention() == 1;
            mAlbumAdapter.setAttentioned(isAttentioned);
            mAlbumAdapter.setmUserName(reponseResult.getData().getUsername(), reponseResult.getData().getPicture());
            mAlbumAdapter.addList(isRefresh, reponseResult.getData().getAlbun());

        } else if (what == AppConfigs.home_photoDetail) {
            PhotoIndexResponse reponseResult = JsonMananger.getReponseResult(response, PhotoIndexResponse.class);
            mAlbumAdapter.addList(false, reponseResult.getData());
        } else {
            AttentionResponse reponseResult = JsonMananger.getReponseResult(response, AttentionResponse.class);
            NToast.shortToast(this, reponseResult.getData().getMessage());
            int is_attention = reponseResult.getData().getIs_attention();
            isAttentioned = is_attention == 1;
            mAlbumAdapter.setAttentioned(isAttentioned);

            mAlbumAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        refresh();
    }

    private void toGuanZhu() {
        if (!UserInfoManager.getSingleton().isLoading(this)) {
            LoginActivity.lunch(this, 0x001);
        } else {
            String userId = UserInfoManager.getSingleton().getUserId(this);
            int attentionType = isAttentioned ? 1 : 2;
            Request<String> request = ParameterUtils.getSingleton().getAttentionOperationMap(userId, mAlbumUserId, attentionType);
            request(AppConfigs.user_attentionOperation, request, true);
        }
    }

}
