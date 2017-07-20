package mr.li.dance.https.response;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mr.li.dance.models.BannerInfo;
import mr.li.dance.models.Match;

/**
 * Created by Lixuewei on 2017/5/29.
 */

public class MatchIndexResponse extends BaseResponse {
    private ArrayList<Match> data;

    public ArrayList<Match> getData() {
        return data;
    }

    public void setData(ArrayList<Match> data) {
        this.data = data;
    }
}
