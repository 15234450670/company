package mr.li.dance.models;

/**
 * Created by Lixuewei on 2017/5/26.
 */

public class QuickZhiboInfo {

    /**
     * id : 25
     * video :
     * picture : http://store.cdsf.org.cn/picture//463/0/0/3a2540776d96235f83ad54bf88b29149.jpg
     * brief : “锦鲲杯”2017年（南京站）5月27日赛事
     */

    private String id;
    private String video;
    private String picture;
    private String brief;
    private String activity_id;
    private boolean isPlaying;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }
}
