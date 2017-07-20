package mr.li.dance.models;

import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class AlbumInfo extends BaseHomeItem {


    /**
     * id : 1482
     * class_name : 2016深圳站
     * picture : http://store.cdsf.org.cn/picture/./10.
     * img_fm : 1
     * inserttime : 2017-05
     */


    private String class_name;
    private String picture;
    private String img_fm;
    private String inserttime;
    private String photo;
    /**
     * compete_name :
     * photos : 10
     * start_time : 2017-05
     */

    private String compete_name;
    private String photos;
    private String start_time;
    private String picture_src;
    private String username;

    private int width;
    private int height;


    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getPicture_src() {
        if (MyStrUtil.isEmpty(picture_src)) {
            return picture;
        } else {
            return picture_src;
        }
    }
    public void setPicture_src(String picture_src) {
        this.picture_src = picture_src;
    }

    public String getPicture() {
        if (!MyStrUtil.isEmpty(picture)&& picture.startsWith("http")) {
            return picture;
        } else if(!MyStrUtil.isEmpty(img_fm) && img_fm.startsWith("http")){
            return img_fm;
        }else{
            return picture_src;
        }
    }

    public String getHomePicNum(){
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getImg_fm() {
        return getPicture();
    }

    public void setImg_fm(String img_fm) {
        this.img_fm = img_fm;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public String getCompete_name() {
        if (MyStrUtil.isEmpty(compete_name)) {
            return class_name;
        } else {
            return compete_name;
        }
    }

    public String getCollectCompete_name(){
        return compete_name;
    }

    public void setCompete_name(String compete_name) {
        this.compete_name = compete_name;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
