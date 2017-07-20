package mr.li.dance.models;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class Match extends BaseHomeItem {

    /**
     * id :
     * name :
     * picture :
     * start_date :
     */
    private String name;
    private String title;
    private String picture;
    private String start_date;
    private String end_date;
    private String start_time;
    private String address;
    private String picture_app;
    private int type;
    /**
     * img :
     * end_time :
     * start_sign_up :
     * end_sign_up :
     * vedio_live_id :
     * activity_id :
     * saicheng :
     * shexiang :
     * guicheng :
     */

    private String img;
    private String end_time;
    private String start_sign_up;
    private String end_sign_up;
    private String vedio_live_id;
    private String activity_id;
    private String saicheng;
    private String shexiang;
    private String guicheng;
    private int itemType;


    private boolean isFirst;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
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

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_sign_up() {
        return start_sign_up;
    }

    public void setStart_sign_up(String start_sign_up) {
        this.start_sign_up = start_sign_up;
    }

    public String getEnd_sign_up() {
        return end_sign_up;
    }

    public void setEnd_sign_up(String end_sign_up) {
        this.end_sign_up = end_sign_up;
    }

    public String getVedio_live_id() {
        return vedio_live_id;
    }

    public void setVedio_live_id(String vedio_live_id) {
        this.vedio_live_id = vedio_live_id;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getSaicheng() {
        return saicheng;
    }

    public void setSaicheng(String saicheng) {
        this.saicheng = saicheng;
    }

    public String getShexiang() {
        return shexiang;
    }

    public void setShexiang(String shexiang) {
        this.shexiang = shexiang;
    }

    public String getGuicheng() {
        return guicheng;
    }

    public void setGuicheng(String guicheng) {
        this.guicheng = guicheng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture_app() {
        return picture_app;
    }

    public void setPicture_app(String picture_app) {
        this.picture_app = picture_app;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
