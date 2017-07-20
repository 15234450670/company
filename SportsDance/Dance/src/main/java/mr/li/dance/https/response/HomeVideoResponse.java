package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.HomeTypeBtn;
import mr.li.dance.models.QuickZhiboInfo;
import mr.li.dance.models.Video;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class HomeVideoResponse extends BaseResponse {
    private Entity data;

    public Entity getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data = data;
    }

    public static class Entity {
        private ArrayList<HomeTypeBtn> db_type;
        private ArrayList<QuickZhiboInfo> dianbo;
        private ArrayList<Video> db_rec;

        public ArrayList<HomeTypeBtn> getDb_type() {
            return db_type;
        }

        public void setDb_type(ArrayList<HomeTypeBtn> db_type) {
            this.db_type = db_type;
        }

        public ArrayList<QuickZhiboInfo> getDianbo() {
            return dianbo;
        }

        public void setDianbo(ArrayList<QuickZhiboInfo> dianbo) {
            this.dianbo = dianbo;
        }

        public ArrayList<Video> getDb_rec() {
            return db_rec;
        }

        public void setDb_rec(ArrayList<Video> db_rec) {
            this.db_rec = db_rec;
        }
    }
}
