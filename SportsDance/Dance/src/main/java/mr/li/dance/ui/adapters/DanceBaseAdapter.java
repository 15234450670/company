package mr.li.dance.ui.adapters;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Lixuewei on 2017/6/8.
 */

public abstract class DanceBaseAdapter extends RecyclerView.Adapter {
    public int currentPage = 1;

    public int getNextPage() {
        return currentPage + 1;
    }

    public int getInitPage(){
        currentPage = 1;
        return currentPage;
    }

    public void refresh(){
        currentPage = 1;
    }
    public void loadMore(){
        currentPage++;
    }
}
