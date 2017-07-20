
package mr.li.dance.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import mr.li.dance.R;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.fragments.SearchFragment;
import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页搜索页面
 * 修订历史:
 */


public class SearchActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    FragmentManager mFragmentManager;
    SearchFragment mCurrentFragment;
    SearchFragment mZhiboFragment;
    SearchFragment mVideoFragment;
    SearchFragment mZiXunFragment;
    SearchFragment mPicFragment;
    RadioGroup mTitleRg;

    @Override
    public int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    public void initDatas() {
        super.initDatas();
        mFragmentManager = getSupportFragmentManager();
        mZhiboFragment = new SearchFragment();
        Bundle zhiBoBundle = new Bundle();
        zhiBoBundle.putString("type", "video_live");
        mZhiboFragment.setArguments(zhiBoBundle);


        mVideoFragment = new SearchFragment();
        Bundle videoBundle = new Bundle();
        videoBundle.putString("type", "video");
        mVideoFragment.setArguments(videoBundle);

        mZiXunFragment = new SearchFragment();
        Bundle ziXunBundle = new Bundle();
        ziXunBundle.putString("type", "article");
        mZiXunFragment.setArguments(ziXunBundle);
        mPicFragment = new SearchFragment();
        Bundle picBundle = new Bundle();
        picBundle.putString("type", "photo_class");
        mPicFragment.setArguments(picBundle);

        mCurrentFragment = mZhiboFragment;
    }

    @Override
    public void initViews() {

        setHeadVisibility(View.GONE);
        mTitleRg = (RadioGroup) mDanceViewHolder.getView(R.id.title_rg);
        mTitleRg.setOnCheckedChangeListener(this);
        mDanceViewHolder.setClickListener(R.id.search_btn, this);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.content_fl, mZhiboFragment).commitAllowingStateLoss();
    }

    String type = "video_live";

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(mCurrentFragment);
        switch (checkedId) {
            case R.id.directseeding_rb:
                if (!mZhiboFragment.isAdded()) {
                    transaction.add(R.id.content_fl, mZhiboFragment);
                }
                type = "video_live";
                mCurrentFragment = mZhiboFragment;
                break;
            case R.id.video_rb:
                if (!mVideoFragment.isAdded()) {
                    transaction.add(R.id.content_fl, mVideoFragment);
                }
                mCurrentFragment = mVideoFragment;
                type = "video";
                break;
            case R.id.consultation_rb:
                if (!mZiXunFragment.isAdded()) {
                    transaction.add(R.id.content_fl, mZiXunFragment);
                }
                type = "article";
                mCurrentFragment = mZiXunFragment;
                break;
            case R.id.picture_rb:
                if (!mPicFragment.isAdded()) {
                    transaction.add(R.id.content_fl, mPicFragment);
                }
                type = "photo_class";
                mCurrentFragment = mPicFragment;
                break;
        }
        transaction.show(mCurrentFragment);
        transaction.commitAllowingStateLoss();
        String content = mDanceViewHolder.getTextValue(R.id.search_et);
        if (!MyStrUtil.isEmpty(content)) {
            mCurrentFragment.refresh(content, type);
        }
    }


    @Override
    public void onClick(View v) {
        String content = mDanceViewHolder.getTextValue(R.id.search_et);
        if (!MyStrUtil.isEmpty(content)) {
            mCurrentFragment.refresh(content, type);
        }
    }

    public static void lunch(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }
}
