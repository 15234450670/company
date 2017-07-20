package mr.li.dance.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import mr.li.dance.R;
import mr.li.dance.models.BannerInfo;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.glide.ImageLoaderManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；既支持自动轮播页面也支持手势滑动切换页面
 * 修订历史:
 */

public class SlideShowView extends FrameLayout {
    private String TAG = "SlideShowView"     ;

    //轮播图图片数量
    private final static int IMAGE_COUNT = 4;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = false;

    //跳转监听器
    private BannerClickListener goListener;

    //自定义轮播图的资源
    private List<BannerInfo> mLunBoDatas;
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;
    ScrollListener mScrollListener;
    boolean isRuning;

    public interface ScrollListener {
        void scrollPosition(int position);
    }

    public void setScrollListener(ScrollListener listener) {
        mScrollListener = listener;
    }

    //Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
            if (mScrollListener != null) {
                mScrollListener.scrollPosition(currentItem);
            }
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == currentItem) {
                    ((View) dotViewsList.get(currentItem)).setBackgroundResource(R.drawable.shape_oval_dot_orage);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.shape_oval_dot_dark);
                }
            }
        }

    };

    public SlideShowView(Context context) {
        this(context, null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            this.context = context;
            initData();
            // 一步任务获取图片
            new GetListTask().execute("");
            if (isAutoPlay) {
                startPlay();
            }
        }
    }

    /**
     * 开始轮播图切换
     */
    public void startPlay() {

        isRuning = true;
        currentItem = 0;
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 4, 4, TimeUnit.SECONDS);
        }
    }

    /**
     * 停止轮播图切换
     */
    private void stopPlay() {
        if (null == scheduledExecutorService) {
            return;
        }
        scheduledExecutorService.shutdown();
        scheduledExecutorService = null;
    }

    /**
     * 初始化相关Data
     */
    private void initData() {
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
        mLunBoDatas = new ArrayList<>();
        Log.i(TAG,imageViewsList.size()+"");
    }

    LinearLayout dotLayout;

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        resetUi();
    }

    private void resetUi() {
        dotLayout.removeAllViews();
        // 热点个数与图片特殊相等
        for (int i = 0; i < mLunBoDatas.size(); i++) {
            final ImageView view = new ImageView(context);
            final int index = i;
            if (mScrollListener != null) {
                view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    goListener.itemClick(index);
                }
            });
            imageViewsList.add(view);

            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 8;
            params.rightMargin = 8;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        /////////首次进入的时候初始化点点
        for (int i = 0; i < dotViewsList.size(); i++) {
            if (i == 0) {
                ((View) dotViewsList.get(0)).setBackgroundResource(R.drawable.shape_oval_dot_orage);
            } else {
                ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.shape_oval_dot_dark);
            }
        }
        if(dotViewsList.size() == 1){
            dotViewsList.get(0).setVisibility(INVISIBLE);
        }else {
            if(dotViewsList.size() > 0){
                dotViewsList.get(0).setVisibility(VISIBLE);
            }
        }
        viewPager.setAdapter(new MyPagerAdapter());
        currentItem =0;

        if(dotViewsList.size() > 1){
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == currentItem) {
                    ((View) dotViewsList.get(currentItem)).setBackgroundResource(R.drawable.shape_oval_dot_orage);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.shape_oval_dot_dark);
                }
            }
        }


    }

    public void setOnGolistener(BannerClickListener listener) {
        this.goListener = listener;
//        for (final ImageView imageView : imageViewsList) {
//            imageView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    goListener.itemClick(imageViewsList.indexOf(imageView));
//                }
//            });
//        }
    }


    public interface BannerClickListener{
        void itemClick(int position);
    }
    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            //((ViewPag.er)container).removeView((View)object);
            ((ViewPager) container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewsList.get(position);

            if (!MyStrUtil.isEmpty(mLunBoDatas)) {
                String url = "";
                if(position < mLunBoDatas.size()){
                    url = mLunBoDatas.get(position).getImg();
                }
                ImageLoaderManager.getSingleton().Load(context, url, imageView, R.drawable.default_banner);
            }
            Log.i(TAG,mLunBoDatas.toString()+"*********");
            imageView.setBackgroundResource(R.drawable.default_banner);

            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            Log.i(TAG,imageViewsList.size()+"------------");
            return imageViewsList.size();

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    if (isAutoPlay) {
                        // 当前为最后一张，此时从右向左滑，则切换到第一张
                        if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                            viewPager.setCurrentItem(0);
                        }
                        // 当前为第一张，此时从左向右滑，则切换到最后一张
                        else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                            viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            if (mScrollListener != null) {
                mScrollListener.scrollPosition(currentItem);
            }

            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    ((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.shape_oval_dot_orage);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.shape_oval_dot_dark);
                }
            }
        }

    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                if(mLunBoDatas.size()>currentItem){
                    handler.obtainMessage().sendToTarget();
                }

            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }

    /**
     * 释放资源，在activity结束时请调用
     */
    public void destory() {
        stopPlay();
        destoryBitmaps();
    }


    public void setImageUrls(List<BannerInfo> list) {
        if (isRuning) {
            return;
        }
        stopPlay();
        mLunBoDatas.clear();
        if (null != list) {
            mLunBoDatas.addAll(list);
        }

        if (viewPager == null) {
            return;
        }
        resetUi();
    }

    /**
     * 异步任务,获取数据
     */
    class GetListTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                initUI(context);
            }
        }
    }

}