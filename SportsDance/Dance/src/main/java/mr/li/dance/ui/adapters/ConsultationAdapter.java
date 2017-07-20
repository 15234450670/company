package mr.li.dance.ui.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.https.response.ZiXunIndexResponse;
import mr.li.dance.models.ZiXunInfo;
import mr.li.dance.ui.adapters.viewholder.BaseViewHolder;
import mr.li.dance.utils.MyStrUtil;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页-咨询-列表-适配器
 * 修订历史:
 */
public class ConsultationAdapter extends DanceBaseAdapter {

    Context mContext;
    public static final int TYPE_1 = 0xff01;
    public static final int TYPE_2 = 0xff02;
    private List<ZiXunInfo> mDatas;

    ListViewItemClickListener<ZiXunInfo> mItemClickListener;

    /**
     * 构造器
     *
     * @param
     */
    public ConsultationAdapter(Context context, ListViewItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
        mDatas = new ArrayList<>();
    }


    @Override
    public int getItemCount() {
        if (MyStrUtil.isEmpty(mDatas)) {
            return 0;
        } else {
            return mDatas.size();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType) {
            case TYPE_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation_type1, null);
                return new ConsultationViewHolder(view);
            case TYPE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultation_item_type4, null);
                return new ConsultationViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindType3((ConsultationViewHolder) holder, position);

    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.size() == 0) {
            return -1;
        }
        ZiXunInfo ziXunInfo = mDatas.get(position);
        if ("1".equals(ziXunInfo.getImg_num())) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }
    }

    class ConsultationViewHolder extends BaseViewHolder {
        public TextView nameTv;

        public ConsultationViewHolder(View view) {
            super(mContext, view);
            nameTv = (TextView) itemView.findViewById(R.id.name);
        }
    }

    private void bindType3(ConsultationViewHolder holder, final int position) {
        ZiXunInfo ziXunInfo = mDatas.get(position);
        holder.danceViewHolder.setText(R.id.name, ziXunInfo.getTitle());
        holder.danceViewHolder.setText(R.id.time_tv, ziXunInfo.getInserttime());
        holder.danceViewHolder.setText(R.id.from_tv, "来源: "+ziXunInfo.getWriter());

        if ("1".equals(ziXunInfo.getImg_num())) {
            holder.danceViewHolder.setRoundImageByUrlOrFilePath(R.id.imageView, ziXunInfo.getPicture(), R.drawable.default_video);
        } else {
            holder.danceViewHolder.setRoundImageByUrlOrFilePath(R.id.pic1_iv, ziXunInfo.getPicture(), R.drawable.default_video);
            holder.danceViewHolder.setRoundImageByUrlOrFilePath(R.id.pic2_iv, ziXunInfo.getPicture_2(), R.drawable.default_video);
            holder.danceViewHolder.setRoundImageByUrlOrFilePath(R.id.pic3_iv, ziXunInfo.getPicture_3(), R.drawable.default_video);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.itemClick(position, mDatas.get(position));
            }
        });
    }

    public void refresh(ZiXunIndexResponse response) {
        super.refresh();
        mDatas.clear();
        ArrayList<ZiXunInfo> ziXunInfos = response.getData();
        if (!MyStrUtil.isEmpty(ziXunInfos)) {
            mDatas.addAll(ziXunInfos);
        }
        notifyDataSetChanged();

    }

    public void loadMore(ZiXunIndexResponse indexResponse) {
        ArrayList<ZiXunInfo> ziXunInfos = indexResponse.getData();
        if (!MyStrUtil.isEmpty(ziXunInfos)) {
            mDatas.addAll(ziXunInfos);
            super.loadMore();
        }
        notifyDataSetChanged();
    }
}