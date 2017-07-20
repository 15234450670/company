package mr.li.dance.ui.activitys.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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
import mr.li.dance.https.response.MyGuanzhuListResponse;
import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.UserInfo;
import mr.li.dance.ui.activitys.SwipeListActivity;
import mr.li.dance.ui.activitys.album.UserAlbumActivity;
import mr.li.dance.ui.adapters.MyGuanzhuAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页-我的-我的关注
 * 修订历史:
 */


public class MyGuanzhuActivity extends SwipeListActivity<UserInfo> {
    MyGuanzhuAdapter mAdapter;
    UserInfo mCancelUserInfo;

    @Override
    public void itemClick(int position, UserInfo value) {
        UserAlbumActivity.lunch(this, value.getUserid());
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new MyGuanzhuAdapter(this, this);
        return mAdapter;
    }

    @Override
    public int getContentViewId() {
        return R.layout.swipelist_layout;
    }

    @Override
    public void initViews() {
        super.initViews();
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) mRecyclerview;
        mRefreshLayout.setEnableLoadmore(false);
        setTitle("我的关注");
        mDanceViewHolder.getImageView(R.id.nodate_icon).setImageResource(R.drawable.no_collect_album);

        mDanceViewHolder.setText(R.id.nodate_desc, "暂无关注");
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
        Request<String> request = ParameterUtils.getSingleton().getMyAttentionMap(userId);
        request(AppConfigs.user_attention, request);
    }

    public static void lunch(Activity context, int requestCode) {
        Intent intent = new Intent(context, MyGuanzhuActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (AppConfigs.user_attentionOperation == what) {
            mAdapter.removePosition(mCancelUserInfo);
        } else {
            MyGuanzhuListResponse reponseResult = JsonMananger.getReponseResult(response, MyGuanzhuListResponse.class);
            List list = reponseResult.getData();
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

    @Override
    public OnSwipeMenuItemClickListener getSwipeMenuItemClickListener() {
        OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                closeable.smoothCloseMenu();// 关闭被点击的菜单。
                if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                    UserInfo userInfo = mAdapter.getItem(adapterPosition);
                    cancelGuanZhu(userInfo);
                }
            }
        };
        return menuItemClickListener;
    }

    private void cancelGuanZhu(UserInfo userInfo) {
        mCancelUserInfo = userInfo;
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = ParameterUtils.getSingleton().getAttentionOperationMap(userId, userInfo.getUserid(), 1);
        request(AppConfigs.user_attentionOperation, request, true);
    }

    public SwipeMenuCreator getSwipeMenuCreator() {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int width = getResources().getDimensionPixelSize(R.dimen.spacing_160);
                int height = getResources().getDimensionPixelSize(R.dimen.spacing_112);
                int textsize = getResources().getDimensionPixelSize(R.dimen.textsize16);
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
