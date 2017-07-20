package mr.li.dance.https.response;

import java.util.ArrayList;

/**
 * Created by Lixuewei on 2017/5/30.
 */

public class UpLoadFileResponse extends BaseResponse {
    private ArrayList<String> data;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
