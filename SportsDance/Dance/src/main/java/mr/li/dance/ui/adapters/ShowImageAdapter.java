package mr.li.dance.ui.adapters;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.util.ArrayList;

import mr.li.dance.R;
import mr.li.dance.ui.widget.photoview.EasePhotoView;
import mr.li.dance.ui.widget.photoview.PhotoViewAttacher;
import mr.li.dance.utils.glide.ImageLoaderManager;

public class ShowImageAdapter extends PagerAdapter {

    private ArrayList<View> mList;
    private ArrayList<String> mUrlList;
    //	private OnLongClickListener mlongClickListener;
    ImageLoaderManager mImageLoader;
    Context mContext;
    View.OnClickListener mClickListener;
    boolean isFromFile;

    public ShowImageAdapter(Context context, ArrayList<String> urllist, View.OnClickListener clickListener, boolean isFromFile) {
        mList = new ArrayList<View>();
        this.isFromFile = isFromFile;
        mUrlList = urllist;
        mContext = context;
        mImageLoader = ImageLoaderManager.getSingleton();
        mClickListener = clickListener;
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        initImages(context, mParams);
    }


    private void initImages(Context context, LinearLayout.LayoutParams mParams) {
        for (int i = 0; i < mUrlList.size(); i++) {
            EasePhotoView mImageView = new EasePhotoView(context);
            mImageView.setLayoutParams(mParams);
//            mImageView.setScaleType(ScaleType.FIT_CENTER);
            mList.add(mImageView);
        }
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(mList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        try {
            container.addView(mList.get(position));
        } catch (Exception e) {
        }
        final EasePhotoView mImageView = (EasePhotoView) mList.get(position);
        mImageLoader.Load(mContext, mUrlList.get(position), mImageView, R.drawable.default_banner);

        mImageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                mClickListener.onClick(view);
            }
        });
        return mList.get(position);
    }

}
