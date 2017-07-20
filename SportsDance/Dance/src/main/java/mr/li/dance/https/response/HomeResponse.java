package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.BannerInfo;
import mr.li.dance.models.BaseHomeItem;
import mr.li.dance.models.MathcRecommend;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class HomeResponse extends BaseResponse {
    HomeEntity data;

    public HomeEntity getData() {
        return data;
    }

    public void setData(HomeEntity data) {
        this.data = data;
    }

    public static class HomeEntity {
        private ArrayList<BannerInfo> banner;
        private ArrayList<BaseHomeItem> indexRec;
        private ArrayList<MathcRecommend> match_recommend;

        public ArrayList<BannerInfo> getBanner() {
            return banner;
        }

        public void setBanner(ArrayList<BannerInfo> banner) {
            this.banner = banner;
        }

        public ArrayList<BaseHomeItem> getIndexRec() {
            return indexRec;
        }

        public void setIndexRec(ArrayList<BaseHomeItem> indexRec) {
            this.indexRec = indexRec;
        }

        public ArrayList<MathcRecommend> getMatch_recommend() {
            return match_recommend;
        }

        public void setMatch_recommend(ArrayList<MathcRecommend> match_recommend) {
            this.match_recommend = match_recommend;
        }
    }


}
