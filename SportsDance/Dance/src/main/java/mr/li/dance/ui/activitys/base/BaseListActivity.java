package mr.li.dance.ui.activitys.base;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;

import mr.li.dance.R;
import mr.li.dance.ui.adapters.ListViewItemClickListener;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 所有带RecycleView的 activity 的基类 此类集成BaseActivity
 * 修订历史:
 */

public abstract class BaseListActivity<T> extends BaseActivity implements ListViewItemClickListener<T> {
//    protected int pageNo = 1;
    protected RecyclerView mRecyclerview;
    protected boolean isRefresh;
    public TwinklingRefreshLayout mRefreshLayout;

    @Override
    public void initViews() {
        initRefreshLayout();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setAdapter(getAdapter());
    }

    @Override
    public void initRefreshLayout() {
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        mRefreshLayout = (TwinklingRefreshLayout) mDanceViewHolder.getView(R.id.refresh);
        if (mRefreshLayout == null) {
            return;
        }
        BezierLayout headerView = new BezierLayout(this);
        mRefreshLayout.setHeaderView(headerView);
        mRefreshLayout.setMaxHeadHeight(140);
        mRefreshLayout.setOverScrollBottomShow(false);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore();
                    }
                }, 2000);
            }
        });
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerview.setAdapter(adapter);
    }

    public abstract RecyclerView.Adapter getAdapter();

    public void refresh() {
        this.isRefresh = true;

    }

    public void loadMore() {
        this.isRefresh = false;
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (isRefresh) {
            mRefreshLayout.finishRefreshing();
        } else {
            mRefreshLayout.finishLoadmore();
        }

    }

    @Override
    public void onFailed(int what, int responseCode, String response) {
        super.onFailed(what, responseCode, response);
        if (mRefreshLayout == null) {
            return;
        }
        if (isRefresh) {
            mRefreshLayout.finishRefreshing();
        } else {
            mRefreshLayout.finishLoadmore();
        }
    }
}
