package mr.li.dance.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mr.li.dance.utils.glide.ImageLoaderManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: recycleView 的页面元素加载空值类
 * 修订历史:
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;//集合类，layout里包含的View,以view的id作为key，value是view对象
    private Context mContext;//上下文对象

    public RecyclerViewHolder(Context ctx, View itemView) {
        super(itemView);
        mContext = ctx;
        mViews = new SparseArray<View>();
    }

    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    public void setVisibility(int viewId,int visibility){
        View view = findViewById(viewId);
        if(view != null){
            view.setVisibility(visibility);
        }
    }

    public RecyclerViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        if(null == view){
            return this;
        }
        view.setText(value);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    public RecyclerViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public void setImageResDrawable(int viewId, int resId) {
        ImageView view = getImageView(viewId);
        view.setImageResource(resId);
    }
    public void setImageByUrlOrFilePath(int viewId, String urlOrFilePath, int defaultId) {
        ImageView view = getImageView(viewId);
        if(view != null){
            ImageLoaderManager.getSingleton().Load(mContext, urlOrFilePath, view, defaultId);
        }
    }
    public void setRoundImageByUrlOrFilePath(int viewId, String urlOrFilePath, int defaultId) {
        ImageView view = getImageView(viewId);
        if(view != null){
            ImageLoaderManager.getSingleton().LoadRound(mContext, urlOrFilePath, view, defaultId);
        }
    }
    public RecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        if (null == view) {
            return this;
        }
        view.setOnClickListener(listener);
        return this;
    }
}