package mr.li.dance.ui.fragments.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import mr.li.dance.R;
import mr.li.dance.broadcast.BroadcastManager;
import mr.li.dance.models.UserInfo;
import mr.li.dance.ui.activitys.MyDanceWebActivity;
import mr.li.dance.ui.activitys.LoginActivity;
import mr.li.dance.ui.activitys.mine.MyCollectActivity;
import mr.li.dance.ui.activitys.mine.MyAlbumActivity;
import mr.li.dance.ui.activitys.mine.MyGuanzhuActivity;
import mr.li.dance.ui.activitys.mine.MyMessageActivity;
import mr.li.dance.ui.activitys.mine.SettingActivity;
import mr.li.dance.ui.activitys.mine.SuggestActivity;
import mr.li.dance.ui.activitys.mine.UserInfoActivity;
import mr.li.dance.ui.adapters.MineAdapter;
import mr.li.dance.ui.fragments.BaseListFragment;
import mr.li.dance.ui.widget.MineItemDecoration;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.glide.ImageLoaderManager;
import mr.li.dance.utils.UserInfoManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-我的页面
 * 修订历史:
 */

public class MineFragment extends BaseListFragment {
    MineAdapter mMineAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        BroadcastManager.getInstance(getActivity()).addAction(AppConfigs.updateinfoAction, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshInfo();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViews() {
        initRefreshLayout();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerview.setLayoutManager(layoutManager);
        MineItemDecoration decoration = new MineItemDecoration((int) getResources().getDimension(R.dimen.spacing_30));
        mRecyclerview.addItemDecoration(decoration);
        mRecyclerview.setAdapter(getAdapter());
        danceViewHolder.setClickListener(R.id.message_icon, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserInfoManager.getSingleton().isLoading(getActivity())) {
                    LoginActivity.lunch(MineFragment.this, 0x001);
                } else {
                    MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_10);
                    MyMessageActivity.lunch(getActivity());
                }
            }
        });
        danceViewHolder.setClickListener(R.id.headicon, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserInfoManager.getSingleton().isLoading(getActivity())) {
                    UserInfoActivity.lunch(MineFragment.this);
                } else {
                    MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_15);
                    LoginActivity.lunch(MineFragment.this, 0x001);
                }

            }
        });
        refreshInfo();
    }

    private void refreshInfo() {
        if (null != getActivity() && UserInfoManager.getSingleton().isLoading(getActivity())) {
            UserInfo userInfo = UserInfoManager.getSingleton().getUserInfo(getActivity());
            danceViewHolder.setText(R.id.nick_tv, userInfo.getUsername());
            ImageLoaderManager.getSingleton().LoadCircle(getActivity(), userInfo.getPicture(), danceViewHolder.getImageView(R.id.headicon), R.drawable.icon_mydefault);
            danceViewHolder.setViewVisibility(R.id.message_icon, View.VISIBLE);
            ImageLoaderManager.getSingleton().LoadMoHu(getActivity(), userInfo.getPicture(), danceViewHolder.getImageView(R.id.background_iv), R.drawable.icon_mydefault);
        } else {
            danceViewHolder.setText(R.id.nick_tv, "未登录");
            danceViewHolder.setImageResDrawable(R.id.headicon, R.drawable.icon_mydefault, R.drawable.icon_mydefault);
            danceViewHolder.setViewVisibility(R.id.message_icon, View.INVISIBLE);
            ImageLoaderManager.getSingleton().LoadMoHu(getActivity(), "", danceViewHolder.getImageView(R.id.background_iv), R.drawable.icon_mydefault);
        }
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mMineAdapter = new MineAdapter(getContext());
        mMineAdapter.setItemClickListener(this);
        return mMineAdapter;
    }

    @Override
    public int getContentView() {

        return R.layout.fragment_mine;
    }

    @Override
    public void itemClick(int position, Object value) {
        if (!UserInfoManager.getSingleton().isLoading(getActivity())) {
            LoginActivity.lunch(this, 0x001);
            return;
        }
        switch (position) {
            case 0:
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_14);
                MyCollectActivity.lunch(getActivity(), false);
                break;
            case 1:
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_13);
                MyCollectActivity.lunch(getActivity(), true);
                break;
            case 2:
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_11);
                MyGuanzhuActivity.lunch(getActivity(), 0x004);
                break;
            case 3:
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_12);
                MyAlbumActivity.lunch(getActivity(), 0x005);
                break;
            case 4:
                SuggestActivity.lunch(getActivity());
                break;
            case 5:
                MyDanceWebActivity.lunch(getActivity(), MyDanceWebActivity.ABOUTTYPE, "关于我们");
                break;
            case 6:
                SettingActivity.lunch(getActivity());
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        refreshInfo();
    }
}
