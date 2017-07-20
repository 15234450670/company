package mr.li.dance.https.response;

/**
 * Created by Lixuewei on 2017/5/30.
 */

public class VersionReponse extends BaseResponse {
    private Entity data;

    public Entity getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data = data;
    }

    public class Entity{

        /**
         * is_force : 1
         * url : a
         */

        private int is_force;
        private String url;

        public int getIs_force() {
            return is_force;
        }

        public void setIs_force(int is_force) {
            this.is_force = is_force;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
