package mr.li.dance.https.response;

import mr.li.dance.models.UserInfo;

/**
 * 作者: Administrator
 * 时间: 2017/5/27.
 * 功能:
 */

public class UserInfoResponse extends BaseResponse {
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
