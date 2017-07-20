package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.SuggestInfo;

/**
 * 作者: Administrator
 * 时间: 2017/5/27.
 * 功能:
 */

public class SuggestResponse extends BaseResponse {
    private ArrayList<SuggestInfo> data;

    public ArrayList<SuggestInfo> getData() {
        return data;
    }

    public void setData(ArrayList<SuggestInfo> data) {
        this.data = data;
    }
}
