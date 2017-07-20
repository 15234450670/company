package mr.li.dance.https.response;

/**
 * Created by Lixuewei on 2017/6/2.
 */

public class AttentionResponse extends BaseResponse {
    private  Entity data;

    public void setData(Entity data) {
        this.data = data;
    }

    public Entity getData() {
        return data;
    }

    public class Entity{

        /**
         * message : 关注成功
         * is_attention : 1
         */

        private String message;
        private int is_attention;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getIs_attention() {
            return is_attention;
        }

        public void setIs_attention(int is_attention) {
            this.is_attention = is_attention;
        }
    }

}
