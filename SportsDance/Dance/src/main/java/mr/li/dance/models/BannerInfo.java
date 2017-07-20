package mr.li.dance.models;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: Banner对象
 * 修订历史:
 */
public class BannerInfo {

    private String id;

    private String img;
    private int type;
    private String title;
    private String litpic;
    private String url;
    private int picResourseId;
    private String number;
    /**
     * img :
     * type :
     */

    public BannerInfo(){

    }

    public BannerInfo(String url){
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPicResourseId() {
        return picResourseId;
    }

    public void setPicResourseId(int picResourseId) {
        this.picResourseId = picResourseId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }
}
