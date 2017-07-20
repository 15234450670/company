package mr.li.dance.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mr.li.dance.utils.glide.ImageLoaderManager;

/**
 * 作者: Administrator
 * 时间: 2017/5/16.
 * 功能:
 */

public class DanceViewHolder {
    private SparseArray<View> mViews;//集合类，layout里包含的View,以view的id作为key，value是view对象
    private View itemView;
    private Context mContext;

    public DanceViewHolder(Context context, View mainView) {
        this.itemView = mainView;
        this.mContext = context;
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

    public void setViewVisibility(int viewId, int visibility) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
        }
        if (view != null) {
            mViews.put(viewId, view);
            view.setVisibility(visibility);
        }
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

    public void setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        if (view != null) {
            view.setText(value);
        }
    }

    public void setTextColor(int viewId, int color) {
        TextView view = findViewById(viewId);
        view.setTextColor(mContext.getResources().getColor(color));

    }

    public void setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
    }

    public void setImageResDrawable(int viewId, int resId, int defaultId) {
        ImageView view = getImageView(viewId);
        if (null != view) {
            ImageLoaderManager.getSingleton().Load(mContext, resId, view, defaultId);
        }

    }

    public void setImageByUrlOrFilePath(int viewId, String urlOrFilePath, int defaultId) {
        ImageView view = getImageView(viewId);
        if (view != null) {
            ImageLoaderManager.getSingleton().Load(mContext, urlOrFilePath, view, defaultId);
        }
    }

    public void setRoundImageByUrlOrFilePath(int viewId, String urlOrFilePath, int defaultId) {
        ImageView view = getImageView(viewId);
        if (view != null) {
            ImageLoaderManager.getSingleton().LoadRound(mContext, urlOrFilePath, view, defaultId);
        }
    }

    public void setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        if (null != view) {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(listener);
        }
    }

    public String getTextValue(int viewId) {
        TextView view = findViewById(viewId);
        if (null == view) {
            return "";
        } else {
            return view.getText().toString();
        }

    }
}
