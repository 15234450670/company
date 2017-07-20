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
import mr.li.dance.ui.adapters.ListViewItemClickListener;
import mr.li.dance.ui.adapters.MatchAdatper;
import mr.li.dance.ui.dialogs.YearSelectDialog;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DateUtils;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 赛事搜索页面
 * 修订历史:
 */
public class SearchMatchActivity extends BaseListActivity<Match> {


    private ArrayList<String> groups;
    MatchAdatper mMatchAdatper;
    YearSelectDialog mYearSelectDialog;

    @Override
    public void itemClick(int position, Match value) {
        MatchDetailActivity.lunch(this,value.getId());
    }

    @Override
    public void initDatas() {
        super.initDatas();
        groups = new ArrayList<>();
        DateUtils dateUtils = new DateUtils();
        int currentYear = dateUtils.getmSelYear();
        for (int i = currentYear; i >= 2001; i--) {
            groups.add(String.valueOf(i));
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mDanceViewHolder.setText(R.id.year_tv, groups.get(0));
        mYearSelectDialog = new YearSelectDialog(this, new ListViewItemClickListener<String>() {
            @Override
            public void itemClick(int position, String value) {
                mDanceViewHolder.setText(R.id.year_tv, value);
                String searchContent = mDanceViewHolder.getTextValue(R.id.search_et);
                if(MyStrUtil.isEmpty(searchContent)){
                    refresh();
                }
            }
        });
        setHeadVisibility(View.GONE);
        mDanceViewHolder.setClickListener(R.id.yearselect_layout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentYear = mDanceViewHolder.getTextValue(R.id.year_tv);
                mYearSelectDialog.showWindow(mDanceViewHolder.getView(R.id.yearselect_layout), currentYear, groups, false,0);
            }
        });
        mDanceViewHolder.setClickListener(R.id.search_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                refresh();
            }
        });
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mMatchAdatper = new MatchAdatper(this);
        mMatchAdatper.setItemClickListener(this);
        return mMatchAdatper;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_searchmatch;
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        MatchIndexResponse reponseResult = JsonMananger.getReponseResult(response, MatchIndexResponse.class);
        mMatchAdatper.addList(isRefresh, reponseResult.getData());
    }

    public static void lunch(Context context) {
        context.startActivity(new Intent(context, SearchMatchActivity.class));
    }


    private void getData(int index) {
        Request<String> request = null;
        String data = mDanceViewHolder.getTextValue(R.id.year_tv);
        String content = mDanceViewHolder.getTextValue(R.id.search_et);
        request = ParameterUtils.getSingleton().getSearchMatchMap(data, content, index);
        request(AppConfigs.match_matchSearch, request, false);
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

}
