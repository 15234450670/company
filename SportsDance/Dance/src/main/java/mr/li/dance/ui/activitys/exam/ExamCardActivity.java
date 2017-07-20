package mr.li.dance.ui.activitys.exam;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.CertificateInfo;
import mr.li.dance.ui.activitys.base.BaseListActivity;
import mr.li.dance.ui.adapters.ExamCardAdapter;
import mr.li.dance.ui.adapters.LvLiAdapter;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 准考证展示页面
 * 修订历史:
 */

public class ExamCardActivity extends BaseListActivity implements View.OnClickListener {
    ExamCardAdapter mExamCardAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.list_layout;
    }

    @Override
    public void initDatas() {
        super.initDatas();
        refresh();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("准考证");
        mRefreshLayout.setEnableLoadmore(false);
        super.initViews();

    }

    @Override
    public void itemClick(int position, Object value) {

    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mExamCardAdapter = new ExamCardAdapter(this);
        return mExamCardAdapter;
    }

    public static void lunch(Context context) {
        context.startActivity(new Intent(context, ExamCardActivity.class));
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public void refresh() {
        super.refresh();
        if(!UserInfoManager.getSingleton().isLoading(this)){
            return;
        }
        String idn = UserInfoManager.getSingleton().getUserInfo(this).getId_card();
        String name = UserInfoManager.getSingleton().getUserInfo(this).getReal_name();
        int type = 2;
        Request<String> request = ParameterUtils.getSingleton().getKaojiCertificateMap(idn, name, type);
        request(AppConfigs.home_dianbo, request, true);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
        String dateStr = stringResponse.getData();
        List<CertificateInfo> list = JsonMananger.jsonToList(dateStr, CertificateInfo.class);
        mExamCardAdapter.addList(true, list);

    }

}
