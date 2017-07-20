package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.BaseHomeItem;
import mr.li.dance.models.HomeListItemInfo;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class HomeIndexResponse extends BaseResponse {

    private ArrayList<BaseHomeItem> data;

    public ArrayList<BaseHomeItem> getData() {
        return data;
    }

    public void setData(ArrayList<BaseHomeItem> data) {
        this.data = data;
    }
}
