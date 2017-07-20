package mr.li.dance.ui.activitys.exam;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.models.CertificateInfo;
import mr.li.dance.ui.activitys.MyDanceWebActivity;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 证书查询输入姓名和身份证号码页面
 * 修订历史:
 */
public class QueryCertificateActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentViewId() {
        return R.layout.activity_querycertificate;
    }

    @Override
    public void initViews() {
        setTitle("证书查询");
        mDanceViewHolder.setClickListener(R.id.explain_layout, this);
        mDanceViewHolder.setClickListener(R.id.submit_btn, this);
    }

    public static void lunch(Context context) {
        context.startActivity(new Intent(context, QueryCertificateActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.explain_layout:
                String url = AppConfigs.KAOJIEXPLAIN;
                MyDanceWebActivity.lunch(this, MyDanceWebActivity.KAOJI, "考级说明", url, true);
                break;
            case R.id.submit_btn:
                query();
                break;
        }
    }

    private void query() {
        String name = mDanceViewHolder.getTextValue(R.id.name_tv);
        String idn = mDanceViewHolder.getTextValue(R.id.card_tv);
        if (MyStrUtil.isEmpty(name)) {
            NToast.shortToast(this, "请输入查询的姓名");
            return;
        }
        if (MyStrUtil.isEmpty(idn)) {
            NToast.shortToast(this, "请输入查询的身份证号");
            return;
        }
        int type = 1;
        Request<String> request = ParameterUtils.getSingleton().getKaojiCertificateMap(idn, name, type);
        request(AppConfigs.kaoji_certificate, request, true);
    }

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
        String dateStr = stringResponse.getData();
        List<CertificateInfo> list = JsonMananger.jsonToList(dateStr, CertificateInfo.class);
        ArrayList<CertificateInfo> dataList = new ArrayList<>();
        dataList.addAll(list);
        CertificateReusltActivity.lunch(this, dataList);

    }

}
