package mr.li.dance.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import mr.li.dance.R;
import mr.li.dance.models.CertificateInfo;
import mr.li.dance.utils.MyStrUtil;

/**
 * Created by Lixuewei on 2017/6/4.
 */

public class LvLiAdapter extends BaseRecyclerAdapter<CertificateInfo> {
    public LvLiAdapter(Context ctx) {
        super(ctx);
    }

    private final int TYPE_1 = 0x001;
    private final int TYPE_2 = 0x002;

    @Override
    public int getItemCount() {
        if (MyStrUtil.isEmpty(mData)) {
            return 0;
        } else {
            return super.getItemCount() + 1;
        }
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == TYPE_1) {
            return R.layout.item_exam_up;
        } else {
            return R.layout.item_lvli;
        }

    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, CertificateInfo item) {
        if (mData.size() == 0) {
            return;
        }
        if (position == 0) {
            bindTitle(holder, mData.get(0));
        } else {
            bindLvLi(holder, mData.get(position - 1));
        }
    }

    private void bindTitle(RecyclerViewHolder holder, CertificateInfo info) {
        holder.setImageByUrlOrFilePath(R.id.headicon_iv, info.getPhotopath(), R.drawable.kaoji_defaulticon);
        holder.setText(R.id.name_tv, info.getName());
        holder.setText(R.id.sex_tv, info.getGender());
        holder.setText(R.id.shengri_tv, info.getBirthday());
        String idcard = info.getIdnumber();
        int lenght = idcard.length();
        if (lenght > 10) {
            String startStr = idcard.substring(0, 5);
            String endStr = idcard.substring(lenght - 5, lenght);
            holder.setText(R.id.shenfenzheng_tv, startStr + "********" + endStr);
        } else {
            holder.setText(R.id.shenfenzheng_tv, idcard);
        }
        holder.setText(R.id.zhengshu_tv, info.getLevelnumber());
    }

    private void bindLvLi(RecyclerViewHolder holder, CertificateInfo info) {
        holder.setText(R.id.level_tv, info.getArtlevel());
        holder.setText(R.id.examtime_tv, "考试时间: " + info.getExamtime());
        holder.setText(R.id.danwei_tv, "考试承办单位: " + info.getOrgname());
        holder.setText(R.id.kaoguan_tv, "考官: " + info.getExaminername());
        holder.setText(R.id.examresult_tv, info.getExamresult());
        holder.setText(R.id.examresult_tv, "合格");
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }
    }
}
