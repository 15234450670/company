package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.Video;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class HomeVideoIndexResponse extends BaseResponse {
    private ArrayList<Video> data;

    public ArrayList<Video> getData() {
        return data;
    }

    public void setData(ArrayList<Video> data) {
        this.data = data;
    }
}
