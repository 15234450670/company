package mr.li.dance.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import mr.li.dance.R;
import mr.li.dance.models.ScoreGroupInfo;
import mr.li.dance.models.ScoreInfo;

/**
 * 作者: Administrator
 * 时间: 2017/6/2.
 * 功能:
 */

public class ScoreFromAdapter extends BaseRecyclerAdapter<ScoreInfo> {
    public ScoreFromAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_scorefrom;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, ScoreInfo item) {
        TextView rankingTv = holder.getTextView(R.id.ranking_tv);
        switch (position) {
            case 0:
                rankingTv.setBackgroundResource(R.drawable.mathc_icon_008);
                rankingTv.setTextColor(Color.parseColor("#9c5200"));
                break;
            case 1:
                rankingTv.setTextColor(Color.parseColor("#254f5b"));
                rankingTv.setBackgroundResource(R.drawable.mathc_icon_0010);
                break;
            case 2:
                rankingTv.setTextColor(Color.parseColor("#643a00"));
                rankingTv.setBackgroundResource(R.drawable.mathc_icon_009);
                break;
            default:
                rankingTv.setTextColor(Color.parseColor("#ffffff"));
                rankingTv.setBackgroundResource(R.drawable.mathc_icon_007);
                break;
        }
        holder.setText(R.id.ranking_tv, item.getResult());
        holder.setText(R.id.daibiaodui_tv, "代表队: " + item.getMan_org());
        holder.setText(R.id.beihao_tv, "背号: " + item.getBackid());
        holder.setText(R.id.member_tv, "参赛选手: " + item.getMan()+" "+item.getWoman());
    }

}
