package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.AlbumInfo;

/**
 * Created by Lixuewei on 2017/5/29.
 */

public class AlbumDetailResponse extends BaseResponse {


    private Entity data;

    public Entity getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data = data;
    }

    public class Entity {
        private String userid;
        private String picture;
        private int is_attention;
        private String username;
        private String create_man;
        private int collection_id;
        private ArrayList<AlbumInfo> photoDetail;
        private ArrayList<AlbumInfo> albun;

        public int getCollection_id() {
            return collection_id;
        }

        public void setCollection_id(int collection_id) {
            this.collection_id = collection_id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public ArrayList<AlbumInfo> getPhotoDetail() {
            return photoDetail;
        }

        public void setPhotoDetail(ArrayList<AlbumInfo> photoDetail) {
            this.photoDetail = photoDetail;
        }

        public int getIs_attention() {
            return is_attention;
        }

        public void setIs_attention(int is_attention) {
            this.is_attention = is_attention;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public ArrayList<AlbumInfo> getAlbun() {
            return albun;
        }

        public void setAlbun(ArrayList<AlbumInfo> albun) {
            this.albun = albun;
        }

        public String getCreate_man() {
            return create_man;
        }

        public void setCreate_man(String create_man) {
            this.create_man = create_man;
        }
    }

}
