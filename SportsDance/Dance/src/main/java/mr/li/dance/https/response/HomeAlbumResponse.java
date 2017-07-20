package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.AlbumInfo;

/**
 * Created by Lixuewei on 2017/5/26.
 */

public class HomeAlbumResponse extends BaseResponse {
    private ArrayList<AlbumInfo> data;

    public ArrayList<AlbumInfo> getData() {
        return data;
    }

    public void setData(ArrayList<AlbumInfo> data) {
        this.data = data;
    }
}
