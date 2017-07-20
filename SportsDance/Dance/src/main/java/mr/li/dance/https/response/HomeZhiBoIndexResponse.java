package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.ZhiBoInfo;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class HomeZhiBoIndexResponse extends BaseResponse {
    private ArrayList<ZhiBoInfo> data;

    public ArrayList<ZhiBoInfo> getData() {
        return data;
    }

    public void setData(ArrayList<ZhiBoInfo> data) {
        this.data = data;
    }
}
