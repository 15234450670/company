package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.Video;

/**
 * Created by Lixuewei on 2017/5/28.
 */

public class VideoDetailResponse extends BaseResponse {
    Entity data;

    public Entity getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data = data;
    }

    public class Entity{
        private Video detail;
        private int collection_id;
        private ArrayList<Video> otherList;

        public Video getDetail() {
            return detail;
        }

        public void setDetail(Video detail) {
            this.detail = detail;
        }

        public ArrayList<Video> getOtherList() {
            return otherList;
        }

        public void setOtherList(ArrayList<Video> otherList) {
            this.otherList = otherList;
        }

        public int getCollection_id() {
            return collection_id;
        }

        public void setCollection_id(int collection_id) {
            this.collection_id = collection_id;
        }
    }
}
