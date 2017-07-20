package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.BannerInfo;
import mr.li.dance.models.ZiXunInfo;
import mr.li.dance.models.HomeTypeBtn;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class HomeZxResponse extends BaseResponse {
    private Entity data;

    public Entity getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data = data;
    }

    public static class Entity {
        private ArrayList<BannerInfo> banner;
        private ArrayList<HomeTypeBtn> zx_type;
        private ArrayList<ZiXunInfo> zxRec;

        public ArrayList<BannerInfo> getBanner() {
            return banner;
        }

        public void setBanner(ArrayList<BannerInfo> banner) {
            this.banner = banner;
        }

        public ArrayList<HomeTypeBtn> getZx_type() {
            return zx_type;
        }

        public void setZx_type(ArrayList<HomeTypeBtn> zx_type) {
            this.zx_type = zx_type;
        }

        public ArrayList<ZiXunInfo> getZxRec() {
            return zxRec;
        }

        public void setZxRec(ArrayList<ZiXunInfo> zxRec) {
            this.zxRec = zxRec;
        }
    }
}
