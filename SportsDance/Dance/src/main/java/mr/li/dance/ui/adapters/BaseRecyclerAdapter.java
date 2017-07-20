package mr.li.dance.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: RecyleView所有适配器的基本
 * 修订历史:
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    public int currentPage=0;
    protected final List<T> mData;
    protected final Context mContext;
    protected LayoutInflater mInflater;
    protected ListViewItemClickListener mClickListener;
    protected OnItemLongClickListener mLongClickListener;

    public BaseRecyclerAdapter(Context ctx) {
        mData = new ArrayList<T>();
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
    }
    public BaseRecyclerAdapter(Context ctx,ArrayList<T> datas) {
        mData = new ArrayList<T>();
        mData.addAll(datas);
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerViewHolder holder = new RecyclerViewHolder(mContext,
                mInflater.inflate(getItemLayoutId(viewType), parent, false));
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mClickListener.itemClick(position, getmItem(position));
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mLongClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (mData.size() != 0) {
            if (mData.size() > position) {
                bindData(holder, position, mData.get(position));
            } else {
                bindData(holder, position, null);
            }

        } else {
            bindData(holder, position, null);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void add(T item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setItemClickListener(ListViewItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    public void addList(List<T> list) {
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(boolean isRefresh, List<T> list) {
        if (isRefresh) {
            currentPage = 0;
            mData.clear();
        }
        if (!MyStrUtil.isEmpty(list)) {
            this.mData.addAll(list);
            currentPage++;
        }
        notifyDataSetChanged();
    }



    public List<T> getmList() {
        return mData;
    }

    public T getmItem(int position) {
        if (mData == null || mData.size() == 0 || mData.size() != getItemCount()) {
            return null;
        }
        return mData.get(position);
    }
    public abstract int getItemLayoutId(int viewType);

    public abstract void bindData(RecyclerViewHolder holder, int position, T item);

    public interface OnItemLongClickListener {
        public void onItemLongClick(View itemView, int pos);
    }

    public int getNextPage(){
        return currentPage+1;
    }

}