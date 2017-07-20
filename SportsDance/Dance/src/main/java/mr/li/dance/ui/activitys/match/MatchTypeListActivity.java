package mr.li.dance.ui.activitys.match;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import java.util.ArrayList;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.MatchIndexResponse;
import mr.li.dance.models.Match;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.BaseRecyclerAdapter;
import mr.li.dance.ui.adapters.ListViewItemClickListener;
import mr.li.dance.ui.adapters.MatchAdatper;
import mr.li.dance.ui.dialogs.YearSelectDialog;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DateUtils;
import mr.li.dance.utils.DensityUtil;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 分类查询赛事列表页面
 * 修订历史:
 */
public class MatchTypeListActivity extends BaseListActivity<Match> {


    private ArrayList<String> mYears;

    MatchAdatper mMatchAdatper;
    private int mType;
    YearSelectDialog mYearSelectDialog;

    @Override
    public void itemClick(int position, Match value) {
        MatchDetailActivity.lunch(this,value.getId());
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mType = mIntentExtras.getInt("type");
    }

    @Override
    public void initDatas() {
        super.initDatas();
        mYears = new ArrayList<>();
        DateUtils dateUtils = new DateUtils();
        int currentYear = dateUtils.getmSelYear();
        for (int i = currentYear; i >= 2001; i--) {
            mYears.add(String.valueOf(i));
        }

    }

    @Override
    public void initViews() {
        setHeadVisibility(View.GONE);
        super.initViews();
        mDanceViewHolder.setText(R.id.year_tv, mYears.get(0));
        switch (mType) {
            case 10001:
                mDanceViewHolder.setText(R.id.tv_title, "WDSF");
                break;
            case 10002:
                mDanceViewHolder.setText(R.id.tv_title, "CDSF");
                break;
            case 10003:
                mDanceViewHolder.setText(R.id.tv_title, "地方赛事");
                break;
        }
        mYearSelectDialog = new YearSelectDialog(this, new ListViewItemClickListener<String>() {
            @Override
            public void itemClick(int position, String value) {
                mDanceViewHolder.setText(R.id.year_tv, value);
                String searchContent = mDanceViewHolder.getTextValue(R.id.search_et);
                if (MyStrUtil.isEmpty(searchContent)) {
                    refresh();
                }
            }
        });

        mDanceViewHolder.setClickListener(R.id.search_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                refresh();
            }
        });
        refresh();
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        switch (mType) {
            case 10001:
                mMatchAdatper = new MatchAdatper(this,R.drawable.wdsf_icon);
                break;
            case 10002:
                mMatchAdatper = new MatchAdatper(this,R.drawable.cdsf_icon);
                break;
            case 10003:
                mMatchAdatper = new MatchAdatper(this,R.drawable.addmathc_icon);
                break;
        }

        mMatchAdatper.setItemClickListener(this);
        return mMatchAdatper;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_matchtype;
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        MatchIndexResponse reponseResult = JsonMananger.getReponseResult(response, MatchIndexResponse.class);
        mMatchAdatper.addList(isRefresh, reponseResult.getData());
    }

    public static void lunch(Context context, int type) {
        Intent intent = new Intent(context, MatchTypeListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }


    private void getData(int index) {
        Request<String> request = null;
        String data = mDanceViewHolder.getTextValue(R.id.year_tv);
        request = ParameterUtils.getSingleton().getmMatchFenleiMap(data, mType, index);
        request(AppConfigs.match_matchFenlei, request, false);
    }

    @Override
    public void refresh() {
        super.refresh();
        getData(1);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        getData(mMatchAdatper.getNextPage());
    }

    @Override
    public void onHeadRightButtonClick(View v) {
        super.onHeadRightButtonClick(v);
        String currentYear = mDanceViewHolder.getTextValue(R.id.year_tv);
        int offx = DensityUtil.dp2px(this, getResources().getDimension(R.dimen.spacing_46)) * (-1);
        mYearSelectDialog.showWindow(mDanceViewHolder.getView(R.id.year_tv), currentYear, mYears, true, offx);
    }
}
