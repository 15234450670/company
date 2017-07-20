package mr.li.dance.models;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class BaseHomeItem {
    private String id;//该条数据的id
    private int type;//10101 直播 10102 点播 10103 赛事资讯 10104相册 10105图片 10106 外联
    private String compete_id;//赛事id
    private String compete_name;//赛事名称
    private String picture_app;//赛事图片
    private String start_time;//赛事时间
    private int compete_type;//赛事类型
    private String end_time;//结束时间
    /////////////////////////////////////////////////////
    private String title;//资讯标题
    private String img_num;//图片数量
    private String picture;//第一张图片
    private String picture_2;//第二张图片
    private String picture_3;////第三张图片
    private String writer;////来源
    ////////////////////////////////////////////////////
    private String photos;//图片数量
    ////////////////////////////////////////////////////
    private String url;//外联的url

    private String activity_id;//直播主题ID
    private String video_unique;//点播标识

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompete_id() {
        return compete_id;
    }

    public void setCompete_id(String compete_id) {
        this.compete_id = compete_id;
    }

    public String getCompete_name() {
        return compete_name;
    }

    public void setCompete_name(String compete_name) {
        this.compete_name = compete_name;
    }

    public String getPicture_app() {
        return picture_app;
    }

    public void setPicture_app(String picture_app) {
        this.picture_app = picture_app;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_num() {
        return img_num;
    }

    public void setImg_num(String img_num) {
        this.img_num = img_num;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture_2() {
        return picture_2;
    }

    public void setPicture_2(String picture_2) {
        this.picture_2 = picture_2;
    }

    public String getPicture_3() {
        return picture_3;
    }

    public void setPicture_3(String picture_3) {
        this.picture_3 = picture_3;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCompete_type() {
        return compete_type;
    }

    public void setCompete_type(int compete_type) {
        this.compete_type = compete_type;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getVideo_unique() {
        return video_unique;
    }

    public void setVideo_unique(String video_unique) {

        this.video_unique = video_unique;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
