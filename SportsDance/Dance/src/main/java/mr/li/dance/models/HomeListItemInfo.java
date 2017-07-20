package mr.li.dance.models;

import java.util.List;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 主页-首页（推荐，直播，视频，咨询，图片）对象，从后台获取到数据后都转成改对象进行加载
 * 修订历史:
 */

public class HomeListItemInfo {
    private boolean isFirst; //是否是第一个元素（如果是第一个元素显示头部）
    private int type;//1 直播 2 热门视频 3 赛事资讯 4精彩瞬间 5图片 6 热门赛事
    /**
     * id : 654
     * name : Jonathan摩登华尔兹舞表演2
     * picture :
     * inserttime : 2009-07
     */

    private String id;
    private String name;
    private String picture;
    private String picture_2;
    private String picture_3;
    private String inserttime;
    private String img_num;



    private List<String> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getImg_num() {
        return img_num;
    }

    public void setImg_num(String img_num) {
        this.img_num = img_num;
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
}
