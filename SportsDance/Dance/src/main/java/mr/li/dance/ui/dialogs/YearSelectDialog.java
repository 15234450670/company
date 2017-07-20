package mr.li.dance.ui.dialogs;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.List;

import mr.li.dance.R;
import mr.li.dance.ui.adapters.BaseRecyclerAdapter;
import mr.li.dance.ui.adapters.ListViewItemClickListener;
import mr.li.dance.ui.adapters.RecyclerViewHolder;

/**
 * 作者: Administrator
 * 时间: 2017/6/2.
 * 功能:
 */

public class YearSelectDialog {
    private PopupWindow popupWindow;
    private View view;
    RecyclerView popRecyclerView;
    Context mContext;
    PopYearAdatper baseRecyclerAdapter;
    private String mCurrentItem;
    ListViewItemClickListener mItemClickListener;

    public YearSelectDialog(Context context, ListViewItemClickListener clickListener) {
        mContext = context;
        mItemClickListener = clickListener;
    }

    public void showWindow(View parent, String currentYear, List<String> years, boolean showSanjiaoRight,int offx) {
        mCurrentItem = currentYear;

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.popwindow_year, null);
            View sanjiaoView = view.findViewById(R.id.sanjiao_icon);
            if (showSanjiaoRight) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.RIGHT;
                sanjiaoView.setLayoutParams(lp);
            }
            popRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            popRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            baseRecyclerAdapter = new PopYearAdatper(mContext);
            baseRecyclerAdapter.setItemClickListener(new ListViewItemClickListener<String>() {
                @Override
                public void itemClick(int position, String value) {
                    popupWindow.dismiss();
                    mItemClickListener.itemClick(position, value);
                }
            });
            popRecyclerView.setAdapter(baseRecyclerAdapter);
            popupWindow = new PopupWindow(view, 360, 600);
            baseRecyclerAdapter.addList(years);
        }
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        popupWindow.showAsDropDown(parent,offx,-40);

        baseRecyclerAdapter.notifyDataSetChanged();
    }

    class PopYearAdatper extends BaseRecyclerAdapter<String> {
        public PopYearAdatper(Context ctx) {
            super(ctx);
        }

        @Override
        public int getItemLayoutId(int viewType) {
            return R.layout.item_year;
        }

        @Override
        public void bindData(RecyclerViewHolder holder, int position, String item) {
            if (item.equals(mCurrentItem)) {
                if (position == 0) {
                    holder.itemView.setBackgroundResource(R.drawable.popwindow_year_up_bg2);
                    holder.setVisibility(R.id.spit_line, View.VISIBLE);
                } else if (position == mData.size() - 1) {
                    holder.setVisibility(R.id.spit_line, View.GONE);
                    holder.itemView.setBackgroundResource(R.drawable.popwindow_year_down_bg2);
                } else {
                    holder.itemView.setBackgroundResource(R.color.white);
                    holder.setVisibility(R.id.spit_line, View.VISIBLE);
                }
                holder.getTextView(R.id.year_tv).setTextColor(mContext.getResources().getColor(R.color.home_bg_color));
            } else {
                if (position == mData.size() - 1) {
                    holder.setVisibility(R.id.spit_line, View.GONE);
                } else {
                    holder.setVisibility(R.id.spit_line, View.VISIBLE);
                }
                holder.itemView.setBackgroundResource(R.color.transparent);
                holder.getTextView(R.id.year_tv).setTextColor(mContext.getResources().getColor(R.color.white));
            }
            holder.setText(R.id.year_tv, item);
        }
    }
}
