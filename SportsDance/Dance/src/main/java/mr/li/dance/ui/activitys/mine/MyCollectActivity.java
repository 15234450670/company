package mr.li.dance.ui.activitys.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yolanda.nohttp.rest.Request;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.HomeAlbumResponse;
import mr.li.dance.https.response.HomeVideoIndexResponse;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.BaseHomeItem;
import mr.li.dance.ui.activitys.SwipeListActivity;
import mr.li.dance.ui.activitys.album.AlbumActivity;
import mr.li.dance.ui.activitys.video.VideoDetailActivity;
import mr.li.dance.ui.adapters.CollectAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/1
 * 描述: 我的收藏页面（收藏的相册，收藏的视频）
 * 修订历史:
 */

public class MyCollectActivity extends SwipeListActivity<BaseHomeItem> {
    CollectAdapter mAdapter;
    private BaseHomeItem mDelItem;
    private boolean isAlbum;

    @Override
    public void itemClick(int position, BaseHomeItem value) {
        if (isAlbum) {
            AlbumInfo albumInfo = (AlbumInfo) value;
            AlbumActivity.lunch(this, value.getId(), albumInfo.getClass_name(), true);
        } else {
            VideoDetailActivity.lunch(this, value.getId(), true);
        }
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        isAlbum = mIntentExtras.getBoolean("isalbum");
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new CollectAdapter(this, isAlbum, this);
        return mAdapter;
    }

    @Override
    public int getContentViewId() {
        return R.layout.swipelist_layout;
    }

    @Override
    public void initViews() {
        super.initViews();
        if (isAlbum) {
            setTitle("收藏的相册");
            mDanceViewHolder.getImageView(R.id.nodate_icon).setImageResource(R.drawable.no_collect_videolsit);

            mDanceViewHolder.setText(R.id.nodate_desc, "您还没有收藏相册");
        } else {
            mDanceViewHolder.getImageView(R.id.nodate_icon).setImageResource(R.drawable.no_collect_videolsit);
            mDanceViewHolder.setText(R.id.nodate_desc, "您还没有收藏视频");
            setTitle("收藏的视频");
        }

    }

    public static void lunch(Activity context, boolean isAlbum) {
        Intent intent = new Intent(context, MyCollectActivity.class);
        intent.putExtra("isalbum", isAlbum);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String deleteId = intent.getStringExtra("delectId");
        mAdapter.removeByID(deleteId);
        if (mAdapter.getItemCount() == 0) {
            mDanceViewHolder.setViewVisibility(R.id.nodatalayout, View.VISIBLE);
        } else {
            mDanceViewHolder.setViewVisibility(R.id.nodatalayout, View.INVISIBLE);
        }
    }

    public static void lunch(Activity context, boolean isAlbum, String delectId) {
        Intent intent = new Intent(context, MyCollectActivity.class);
        intent.putExtra("isalbum", isAlbum);
        intent.putExtra("delectId", delectId);
        context.startActivity(intent);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        refresh();
    }


    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (AppConfigs.user_collection == what) {
            StringResponse reponseResult = JsonMananger.getReponseResult(response, StringResponse.class);
            NToast.shortToast(this, reponseResult.getData());
            mAdapter.removePosition(mDelItem);

        } else {
            List list;
            if (isAlbum) {
                HomeAlbumResponse reponseResult = JsonMananger.getReponseResult(response, HomeAlbumResponse.class);
                list = reponseResult.getData();
            } else {
                HomeVideoIndexResponse reponseResult = JsonMananger.getReponseResult(response, HomeVideoIndexResponse.class);
                list = reponseResult.getData();
            }
            if (!MyStrUtil.isEmpty(list)) {
                mAdapter.addList(isRefresh, list);
            }
        }
        if (mAdapter.getItemCount() == 0) {
            mDanceViewHolder.setViewVisibility(R.id.nodatalayout, View.VISIBLE);
        } else {
            mDanceViewHolder.setViewVisibility(R.id.nodatalayout, View.INVISIBLE);
        }


    }

    private void getData(int index) {
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = null;
        if (isAlbum) {
            request = ParameterUtils.getSingleton().getCollectionListMap(userId, 10601, index);
        } else {
            request = ParameterUtils.getSingleton().getCollectionListMap(userId, 10602, index);
        }
        request(AppConfigs.home_collectionList, request, false);
    }

    @Override
    public void refresh() {
        super.refresh();
        getData(1);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        getData(mAdapter.getNextPage());
    }

    @Override
    public OnSwipeMenuItemClickListener getSwipeMenuItemClickListener() {
        OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                closeable.smoothCloseMenu();// 关闭被点击的菜单。
                if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                    BaseHomeItem homeItem = mAdapter.getItem(adapterPosition);
                    cancelCollect(homeItem);
                }
            }
        };
        return menuItemClickListener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void cancelCollect(BaseHomeItem item) {
        mDelItem = item;
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = null;
        if (isAlbum) {
            request = ParameterUtils.getSingleton().getCollectionMap(userId, item.getId(), 10601, 2);
        } else {
            request = ParameterUtils.getSingleton().getCollectionMap(userId, item.getId(), 10602, 2);
        }
        request(AppConfigs.user_collection, request, false);
    }

    public SwipeMenuCreator getSwipeMenuCreator() {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int width = getResources().getDimensionPixelSize(R.dimen.spacing_160);
                int height = getResources().getDimensionPixelSize(R.dimen.spacing_199);
                int textsize = getResources().getDimensionPixelSize(R.dimen.textsize12);
                // 添加右侧的，如果不添加，则右侧不会出现菜单。
                {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                            .setBackgroundDrawable(R.drawable.selector_red)
                            .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                            .setTextColor(Color.WHITE)
                            .setTextSize(textsize)
                            .setWidth(width)
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
                }
            }
        };
        return swipeMenuCreator;
    }


}
