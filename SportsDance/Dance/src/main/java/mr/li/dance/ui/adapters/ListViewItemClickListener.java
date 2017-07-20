package mr.li.dance.ui.adapters;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: recycleView 的点击监听
 * 修订历史:
 */
public interface ListViewItemClickListener<T> {
    void itemClick(int position, T value);
}
