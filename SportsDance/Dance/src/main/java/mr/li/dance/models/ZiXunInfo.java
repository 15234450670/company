package mr.li.dance.models;

import mr.li.dance.ui.activitys.mine.MyAlbumActivity;
import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class ZiXunInfo extends BaseHomeItem {

    /**
     * id :
     * name :
     * picture :
     * inserttime :
     */

    private String name;
    private String picture;
    private String picture_2;
    private String picture_3;
    private String inserttime;
    private String img_num;
    private String title;
    private String start_time;
    private String writer;////来源

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
        if (MyStrUtil.isEmpty(inserttime)) {
            return start_time;
        } else {
            return inserttime;
        }
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getWriter() {
        return writer;
    }

    @Override
    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getStart_time() {
        if (MyStrUtil.isEmpty(start_time)) {
            return inserttime;
        } else {
            return start_time;
        }

    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
}
