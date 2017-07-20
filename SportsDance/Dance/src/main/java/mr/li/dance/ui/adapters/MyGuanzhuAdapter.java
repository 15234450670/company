package mr.li.dance.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.models.UserInfo;
import mr.li.dance.utils.glide.ImageLoaderManager;
import mr.li.dance.utils.MyStrUtil;

/**
 * Created by Lixuewei on 2017/5/30.
 */

public class MyGuanzhuAdapter extends SwipeMenuAdapter<RecyclerViewHolder> {
    Context mContext;
    private ArrayList<UserInfo> mDatas;
    ListViewItemClickListener<UserInfo> mItemClickListener;
    public MyGuanzhuAdapter(Context context,ListViewItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
        mDatas = new ArrayList<>();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mytuanzhu, parent, false);
    }

    @Override
    public RecyclerViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        DefaultViewHolder viewHolder = new DefaultViewHolder(mContext, realContentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        ImageLoaderManager.getSingleton().LoadCircle(mContext, mDatas.get(position).getPicture_src(), holder.getImageView(R.id.icon), R.drawable.default_icon);
        holder.setText(R.id.content_tv, mDatas.get(position).getUsername());
        holder.setClickListener(R.id.content_tv,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.itemClick(position,mDatas.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class DefaultViewHolder extends RecyclerViewHolder {
        public DefaultViewHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    public void addList(boolean isRefresh, List list) {
        if (isRefresh) {
            mDatas.clear();
            currentPage = 0;
        }
        if (!MyStrUtil.isEmpty(list)) {
            mDatas.addAll(list);
            currentPage++;
            notifyDataSetChanged();
        }
    }

    public UserInfo getItem(int position) {
        return mDatas.get(position);
    }

    public void removePosition(UserInfo userInfo) {
        mDatas.remove(userInfo);
        notifyDataSetChanged();
    }
    public int currentPage=0;
    public int getNextPage(){
        return currentPage+1;
    }
}
