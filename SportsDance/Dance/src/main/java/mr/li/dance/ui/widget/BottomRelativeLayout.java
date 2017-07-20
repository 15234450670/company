package mr.li.dance.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mr.li.dance.R;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页底部按钮组件
 * 修订历史:
 */

public class BottomRelativeLayout extends RelativeLayout {

    private Integer defaultIconResId;
    private Integer hoverIconResId;
    public BottomRelativeLayout(Context context) {
        super(context);
    }

    public BottomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs,
                R.styleable.BottomRelativeLayout);
        defaultIconResId = typeArray.getResourceId(R.styleable.BottomRelativeLayout_default_icon, R.drawable.home_default_icon);
        hoverIconResId = typeArray.getResourceId(R.styleable.BottomRelativeLayout_hover_icon, R.drawable.home_default_icon);
        typeArray.recycle();
    }

    public void setClicked(boolean clicked) {
        ImageView iconIv = (ImageView) getChildAt(0);
        TextView textTv = (TextView) getChildAt(1);
        if (clicked) {
            iconIv.setImageResource(hoverIconResId);
            textTv.setTextColor(getResources().getColor(R.color.bottom_text_hover_color));
        } else {
            iconIv.setImageResource(defaultIconResId);
            textTv.setTextColor(getResources().getColor(R.color.bottom_text_default_color));
        }
    }
}
