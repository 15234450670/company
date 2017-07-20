package mr.li.dance.ui.activitys;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import mr.li.dance.R;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.ListViewItemClickListener;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 所有带侧滑显示删除RecycleView的 activity 的基类 此类集成BaseActivity
 * 修订历史:
 */

public abstract class SwipeListActivity<T> extends BaseListActivity<T> {
    protected SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    protected OnSwipeMenuItemClickListener menuItemClickListener;

    @Override
    public void initDatas() {
        super.initDatas();
        menuItemClickListener = getSwipeMenuItemClickListener();
    }

    public abstract OnSwipeMenuItemClickListener getSwipeMenuItemClickListener();
    public abstract SwipeMenuCreator getSwipeMenuCreator();

    @Override
    public void initViews() {
        initRefreshLayout();
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recyclerview);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSwipeMenuRecyclerView.setLayoutManager(layoutManager);

        mSwipeMenuRecyclerView.setSwipeMenuCreator(getSwipeMenuCreator());
        // 设置菜单Item点击监听。
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
        mSwipeMenuRecyclerView.setAdapter(getAdapter());

    }



}
