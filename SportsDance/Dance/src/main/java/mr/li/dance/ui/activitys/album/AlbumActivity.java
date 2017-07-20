package mr.li.dance.ui.activitys.album;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.AlbumDetailResponse;
import mr.li.dance.https.response.PhotoIndexResponse;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.ui.activitys.MyDanceWebActivity;
import mr.li.dance.ui.activitys.LoginActivity;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.activitys.mine.MyCollectActivity;
import mr.li.dance.ui.adapters.AlbumAdapter;
import mr.li.dance.ui.widget.SpacesItemDecoration;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.ShareUtils;
import mr.li.dance.utils.glide.ImageLoaderManager;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页-图片-相册详情页面
 * 修订历史:
 */

public class AlbumActivity extends BaseListActivity<AlbumInfo> {
    AlbumAdapter mAlbumAdapter;
    private String mAlbumUserId;
    private String shareUrl;
    boolean isCollected;
    boolean isfromCollectPage;
    private String mShareContent;

    @Override
    public void initViews() {
        setTitle("相册");
        initRefreshLayout();
        mRefreshLayout.setEnableLoadmore(false);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) getResources().getDimension(R.dimen.spacing_20));
        mRecyclerview.addItemDecoration(decoration);
        mRecyclerview.setAdapter(getAdapter());
        //相册右上角按钮
        setRightImage(R.drawable.collect_icon, R.drawable.share_icon_001);

        mDanceViewHolder.setClickListener(R.id.headicon, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAlbumActivity.lunch(AlbumActivity.this, mAlbumUserId);
            }
        });
        mDanceViewHolder.setClickListener(R.id.touploadmyablue_layout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDanceWebActivity.lunch(AlbumActivity.this,MyDanceWebActivity.UPLOADPIC,"相册上传详情");

            }
        });


        refresh();
    }

    @Override
    public void itemClick(int position, AlbumInfo value) {
        List<AlbumInfo> list = mAlbumAdapter.getmList();
        ArrayList<String> urlList = new ArrayList<>();
        for (AlbumInfo albumInfo : list) {
            urlList.add(albumInfo.getPhoto());
        }
        ImageShowActivity.lunch(this, urlList, position);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAlbumAdapter = new AlbumAdapter(this);
        mAlbumAdapter.setItemClickListener(this);
        return mAlbumAdapter;
    }

    private String mId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_album;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mId = mIntentExtras.getString("id");
        mShareContent = mIntentExtras.getString("sharecontent");
    }

    public static void lunch(Fragment fragment, String id, String shareContent) {
        Intent intent = new Intent(fragment.getActivity(), AlbumActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("sharecontent", shareContent);
        fragment.startActivity(intent);
    }

    public static void lunch(Context context, String id, String shareContent) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("sharecontent", shareContent);
        context.startActivity(intent);
    }

    public static void lunch(Context context, String id, String shareContent, boolean isfromCollectPage) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("sharecontent", shareContent);
        intent.putExtra("isfromcollectpage", isfromCollectPage);
        context.startActivity(intent);
    }


    @Override
    public void refresh() {
        super.refresh();
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = ParameterUtils.getSingleton().getAlbumDetailMap(userId, mId);
        request(AppConfigs.home_album, request, false);
    }

    @Override
    public void loadMore() {
        super.loadMore();
//        Request<String> request = ParameterUtils.getSingleton().getPhotoDetailMap(mId, mAlbumAdapter.getNextPage());
//        request(AppConfigs.home_photoDetail, request, false);
    }


    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (what == AppConfigs.home_album) {
            AlbumDetailResponse reponseResult = JsonMananger.getReponseResult(response, AlbumDetailResponse.class);
            mAlbumUserId = reponseResult.getData().getUserid();
            mAlbumAdapter.addList(isRefresh, reponseResult.getData().getPhotoDetail());
            mDanceViewHolder.setText(R.id.name_tv, reponseResult.getData().getCreate_man());
            String headIcon = reponseResult.getData().getPicture();
            ImageLoaderManager.getSingleton().LoadCircle(this, headIcon, (ImageView) mDanceViewHolder.getView(R.id.headicon), R.drawable.icon_mydefault);
            ImageLoaderManager.getSingleton().LoadMoHu(this, headIcon, mDanceViewHolder.getImageView(R.id.background_iv), R.drawable.icon_mydefault);
            isCollected = (0 != reponseResult.getData().getCollection_id());

            shareUrl = String.format(AppConfigs.SHAREPHOTOURL, mId);
            if (isCollected) {
                mRightIv.setImageResource(R.drawable.collect_icon_002);
            } else {
                mRightIv.setImageResource(R.drawable.collect_icon);
            }
        } else if (AppConfigs.home_photoDetail == what) {
            PhotoIndexResponse reponseResult = JsonMananger.getReponseResult(response, PhotoIndexResponse.class);
            mAlbumAdapter.addList(false, reponseResult.getData());
        } else {
            StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
            NToast.shortToast(this, stringResponse.getData());
            isCollected = !isCollected;
            if (isCollected) {
                mRightIv.setImageResource(R.drawable.collect_icon_002);
            } else {
                mRightIv.setImageResource(R.drawable.collect_icon);
            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mId = intent.getStringExtra("id");
        refresh();
    }

    @Override
    public void onHeadRightButtonClick(View v) {
        if (!UserInfoManager.getSingleton().isLoading(this)) {
            LoginActivity.lunch(this, 0x001);
        } else {
            String userId = UserInfoManager.getSingleton().getUserId(this);
            int operation = isCollected ? 1 : 2;
            Request<String> request = ParameterUtils.getSingleton().getCollectionMap(userId, mId, 10601, operation);
            request(AppConfigs.user_collection, request, false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 0x001) {
            refresh();
        }
    }

    public void onHeadRightButtonClick2(View v) {
        showShareDialog();
    }

    ShareUtils mShareUtils;

    private void showShareDialog() {
        if (mShareUtils == null) {
            mShareUtils = new ShareUtils(this);
        }
        mShareUtils.showShareDilaog(AppConfigs.CLICK_EVENT_20,shareUrl, mShareContent);
    }


    @Override
    public void onBackPressed() {
        if (isfromCollectPage && !isCollected) {
            MyCollectActivity.lunch(this, true, mId);
            finish();
        } else {
            super.onBackPressed();
        }

    }


}
