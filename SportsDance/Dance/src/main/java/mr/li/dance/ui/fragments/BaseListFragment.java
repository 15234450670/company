package mr.li.dance.ui.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;

import mr.li.dance.R;
import mr.li.dance.ui.adapters.ListViewItemClickListener;
import mr.li.dance.ui.widget.FullyLinearLayoutManager;
import mr.li.dance.utils.DanceViewHolder;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 所有带recycleView的fragment 的基类
 * 修订历史:
 */

public abstract class BaseListFragment<T> extends BaseFragment implements ListViewItemClickListener<T> {
    protected RecyclerView mRecyclerview;
    public TwinklingRefreshLayout mRefreshLayout;
    protected boolean isRefresh = true;
    RecyclerView.Adapter mAdapter;


    public void initViews() {
        initRefreshLayout();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(layoutManager);
        mAdapter = getAdapter();
        if (null != mAdapter) {
            mRecyclerview.setAdapter(mAdapter);
        }
    }
    public void initRefreshLayout() {
        mRecyclerview = (RecyclerView) danceViewHolder.getView(R.id.recyclerview);
        mRefreshLayout = (TwinklingRefreshLayout) danceViewHolder.getView(R.id.refresh);

        if (mRefreshLayout == null) {
            return;
        }
        BezierLayout headerView = new BezierLayout(getActivity());
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
        if (mRefreshLayout == null) {
            return;
        }
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
