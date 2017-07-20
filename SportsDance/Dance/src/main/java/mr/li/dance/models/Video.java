package mr.li.dance.models;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class Video extends BaseHomeItem {

    /**
     * id :
     * name :
     * picture :
     * inserttime :
     */

    private String name;
    private String picture;
    private String inserttime;
    private String title;
    private String start_time;
    private String video;
    private String compete_name;
    private String video_unique;
    /**
     * album_id :
     * album_img :
     * album_name :
     */

    private String album_id;
    private String album_img;
    private String album_name;
    private String img_fm;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCompete_name() {
        return compete_name;
    }

    public void setCompete_name(String compete_name) {
        this.compete_name = compete_name;
    }

    public String getVideo_unique() {
        return video_unique;
    }

    public void setVideo_unique(String video_unique) {
        this.video_unique = video_unique;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_img() {
        return album_img;
    }

    public void setAlbum_img(String album_img) {
        this.album_img = album_img;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getImg_fm() {
        return img_fm;
    }

    public void setImg_fm(String img_fm) {
        this.img_fm = img_fm;
    }
}
