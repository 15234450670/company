package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.ZiXunInfo;

/**
 * Created by Lixuewei on 2017/5/26.
 */

public class ZiXunIndexResponse extends BaseResponse {
    private ArrayList<ZiXunInfo> data;

    public ArrayList<ZiXunInfo> getData() {
        return data;
    }

    public void setData(ArrayList<ZiXunInfo> data) {
        this.data = data;
    }
}
