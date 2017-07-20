package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.UserInfo;

/**
 * 作者: Administrator
 * 时间: 2017/5/27.
 * 功能:
 */

public class MyGuanzhuListResponse extends BaseResponse {
    private ArrayList<UserInfo> data;

    public ArrayList<UserInfo> getData() {
        return data;
    }

    public void setData(ArrayList<UserInfo> data) {
        this.data = data;
    }
}
