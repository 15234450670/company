package mr.li.dance.ui.fragments.main;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;


import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.CallServer;
import mr.li.dance.https.HttpListener;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.HomeResponse;
import mr.li.dance.ui.activitys.SearchActivity;
import mr.li.dance.ui.fragments.BaseFragment;
import mr.li.dance.ui.fragments.homepage.ConsultationFragment;
import mr.li.dance.ui.fragments.homepage.DirectseedingFragment;
import mr.li.dance.ui.fragments.homepage.PictureFragment;
import mr.li.dance.ui.fragments.homepage.RecommendFragment;
import mr.li.dance.ui.fragments.homepage.VideoFragment;
import mr.li.dance.utils.AndroidBug54971Workaround;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页 （此类加载同homeapge包中的所有子fragment）
 * 修订历史:
 */
public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    ViewPager mViewPaper;
    FragmentPagerAdapter mPagerAdapter;
    private RadioGroup mTitleRg;
    private List<Fragment> mFragment = new ArrayList<>();

    @Override
    public void initData() {
        setScreen();
//        AndroidBug54971Workaround.assistActivity(danceViewHolder.getView(R.id.home_viewpager));
        mFragment.add(new RecommendFragment());
        mFragment.add(new DirectseedingFragment());
        mFragment.add(new VideoFragment());
        mFragment.add(new ConsultationFragment());
        mFragment.add(new PictureFragment());
        FragmentManager mFragmentManager = getChildFragmentManager();
        mPagerAdapter = new FragmentPagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };

    }

    @Override
    public void initViews() {
        mViewPaper = (ViewPager) danceViewHolder.getView(R.id.home_viewpager);
        mViewPaper.setAdapter(mPagerAdapter);
        mViewPaper.setOffscreenPageLimit(3);
        mViewPaper.setOnPageChangeListener(this);

        mTitleRg = (RadioGroup) danceViewHolder.getView(R.id.title_rg);
        mTitleRg.setOnCheckedChangeListener(this);

        danceViewHolder.setClickListener(R.id.search_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_6);
                SearchActivity.lunch(getActivity());
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mTitleRg.check(R.id.recommend_rb);
                break;
            case 1:
                mTitleRg.check(R.id.directseeding_rb);
                break;
            case 2:
                mTitleRg.check(R.id.video_rb);
                break;
            case 3:
                mTitleRg.check(R.id.consultation_rb);
                break;
            case 4:
                mTitleRg.check(R.id.picture_rb);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


        switch (checkedId) {
            case R.id.recommend_rb:
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_1);
                mViewPaper.setCurrentItem(0);
                break;
            case R.id.directseeding_rb:
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_2);
                mViewPaper.setCurrentItem(1);
                break;
            case R.id.video_rb:
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_3);
                mViewPaper.setCurrentItem(2);
                break;
            case R.id.consultation_rb:
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_4);
                mViewPaper.setCurrentItem(3);
                break;
            case R.id.picture_rb:
                MobclickAgent.onEvent(getActivity(), AppConfigs.CLICK_EVENT_5);
                mViewPaper.setCurrentItem(4);
                break;
        }
    }

    @Override
    public void onSucceed(int what, String response) {

    }
}
