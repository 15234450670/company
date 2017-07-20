package mr.li.dance.ui.activitys.exam;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import mr.li.dance.R;
import mr.li.dance.models.CertificateInfo;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.LvLiAdapter;
import mr.li.dance.ui.widget.FullyLinearLayoutManager;
import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 证书查询结果显示页面
 * 修订历史:
 */

public class CertificateReusltActivity extends BaseListActivity implements View.OnClickListener {
    LvLiAdapter mLvLiAdapter;
    ArrayList<CertificateInfo> dataList;

    @Override
    public int getContentViewId() {
        return R.layout.list_layout;
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("证书查询");
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(false);
        if (!MyStrUtil.isEmpty(dataList)) {
            mLvLiAdapter.addList(dataList);
            mRightLayout.setVisibility(View.VISIBLE);
        }else{
            mRightLayout.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        dataList = mIntentExtras.getParcelableArrayList("datalist");
    }

    @Override
    public void itemClick(int position, Object value) {

    }



    @Override
    public RecyclerView.Adapter getAdapter() {
        mLvLiAdapter = new LvLiAdapter(this);
        return mLvLiAdapter;
    }

    public static void lunch(Context context, ArrayList<CertificateInfo> dataList) {
        Intent intent = new Intent(context, CertificateReusltActivity.class);
        intent.putExtra("datalist", dataList);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
