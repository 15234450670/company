package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.BannerInfo;
import mr.li.dance.models.MathcRecommend;
import mr.li.dance.models.QuickZhiboInfo;
import mr.li.dance.models.ZhiBoInfo;

/**
 * Created by Lixuewei on 2017/5/28.
 */

public class ZhiboDetailResponse extends BaseResponse {
    private Entity data;

    public Entity getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data = data;
    }

    public static class Entity {
        private ZhiBoInfo detail;
        private ArrayList<QuickZhiboInfo> otherList;

        public ZhiBoInfo getDetail() {
            return detail;
        }

        public void setDetail(ZhiBoInfo detail) {
            this.detail = detail;
        }

        public ArrayList<QuickZhiboInfo> getOtherList() {
            return otherList;
        }

        public void setOtherList(ArrayList<QuickZhiboInfo> otherList) {
            this.otherList = otherList;
        }
    }
}
