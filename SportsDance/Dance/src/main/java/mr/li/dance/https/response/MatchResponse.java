package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.BannerInfo;
import mr.li.dance.models.Match;
import mr.li.dance.models.MathcRecommend;

/**
 * Created by Lixuewei on 2017/5/29.
 */

public class MatchResponse extends BaseResponse {
    private Entity data;

    public Entity getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data = data;
    }

    public class Entity {
        private ArrayList<BannerInfo> banner;
        private ArrayList<Match> zbMatch;
        private ArrayList<Match> hotMatch;

        public ArrayList<BannerInfo> getBanner() {
            return banner;
        }

        public void setBanner(ArrayList<BannerInfo> banner) {
            this.banner = banner;
        }

        public ArrayList<Match> getZbMatch() {
            return zbMatch;
        }

        public void setZbMatch(ArrayList<Match> zbMatch) {
            this.zbMatch = zbMatch;
        }

        public ArrayList<Match> getHotMatch() {
            return hotMatch;
        }

        public void setHotMatch(ArrayList<Match> hotMatch) {
            this.hotMatch = hotMatch;
        }
    }
}
