package mr.li.dance.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者: Administrator
 * 时间: 2017/5/23.
 * 功能:
 */

public class MineItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public MineItemDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left=space/2;
        outRect.right=space/2;
        outRect.bottom=space/2;
        outRect.top=space/2;

    }
}
