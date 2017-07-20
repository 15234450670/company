package mr.li.dance.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/7/20.
 */
public class FixationViewPaper extends ViewPager {

    public FixationViewPaper(Context context) {
        super(context);
    }

    public FixationViewPaper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = childWidthSize*2/5;
        //高度和宽度一样
        heightMeasureSpec  = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
