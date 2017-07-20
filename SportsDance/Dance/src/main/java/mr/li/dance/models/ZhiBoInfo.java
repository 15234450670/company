package mr.li.dance.models;

import java.util.ArrayList;

import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class ZhiBoInfo extends BaseHomeItem {
    /**
     * id :
     * video :
     * picture :
     * brief :
     */

    private String video;
    private String name;
    private String picture;
    private String brief;
    private String start_time;
    private String starttime;
    private String begin_time;
    private String end_time;
    private String start_date;
    private String activity_id;
    private String compete_trailer;
    private String match_id;

    /**
     * start_time :
     */

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBrief() {
        if(!MyStrUtil.isEmpty(brief)){
            brief = brief.replace("<br>","\n");
            brief = brief.replace("-","\n");
        }
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;

    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getCompete_trailer() {
        return compete_trailer;
    }

    public void setCompete_trailer(String compete_trailer) {
        this.compete_trailer = compete_trailer;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }
}
