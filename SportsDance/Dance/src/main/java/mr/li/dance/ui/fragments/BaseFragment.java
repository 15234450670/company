package mr.li.dance.ui.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.rest.Request;

import mr.li.dance.R;
import mr.li.dance.https.CallServer;
import mr.li.dance.https.HttpListener;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.ui.fragments.main.MatchFragment;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.DanceViewHolder;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 所有fragment的基类
 * 修订历史:
 */

public abstract class BaseFragment extends Fragment implements HttpListener {
    protected View mView;
    protected SharedPreferences mSp;
    protected SharedPreferences.Editor mEditor;
    protected DanceViewHolder danceViewHolder;

    protected void setScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//A
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//B
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.home_bg_color));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setScreen();
        mView = inflater.inflate(getContentView(), null, false);
        danceViewHolder = new DanceViewHolder(getActivity(), mView);
        mSp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        mEditor = mSp.edit();
        initData();
        initViews();
        return mView;
    }

    public abstract void initData();

    public abstract void initViews();

    public abstract int getContentView();


    protected String getmUserId() {
        return mSp.getString(AppConfigs.USERID, "");
    }

    @Override
    public void onSucceed(int what, String response) {

    }

    @Override
    public void onFailed(int what, int responseCode, String response) {
        switch (responseCode) {
            case -1000:
                NToast.shortToast(getActivity(), "网络连接错误");
                break;
            case -1001:
                NToast.shortToast(getActivity(), response);
                break;
            default:
                StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
                if (!MyStrUtil.isEmpty(stringResponse)) {
                    NToast.shortToast(getActivity(), stringResponse.getData());
                }
                break;
        }
    }

    protected void request(int what, Request request, boolean showLoginDialog) {
        CallServer.getRequestInstance().add(getActivity(), what, request, this, false, showLoginDialog);
    }
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart("");
    }
}
