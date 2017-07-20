package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.QuickZhiboInfo;
import mr.li.dance.models.ZhiBoInfo;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class HomeZhiBoResponse extends BaseResponse {
    private Entity data;

    public Entity getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data = data;
    }

    public static class Entity {
        private ArrayList<ZhiBoInfo> zbRecList;
        private ArrayList<QuickZhiboInfo> kszb;

        public ArrayList<ZhiBoInfo> getZbRecList() {
            return zbRecList;
        }

        public void setZbRecList(ArrayList<ZhiBoInfo> zbRecList) {
            this.zbRecList = zbRecList;
        }

        public ArrayList<QuickZhiboInfo> getKszb() {
            return kszb;
        }

        public void setKszb(ArrayList<QuickZhiboInfo> kszb) {
            this.kszb = kszb;
        }
    }
}
