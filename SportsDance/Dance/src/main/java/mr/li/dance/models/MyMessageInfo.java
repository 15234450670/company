package mr.li.dance.models;

/**
 * 作者: Administrator
 * 时间: 2017/6/7.
 * 功能:
 */

public class MyMessageInfo {

    /**
     * title : 什么鬼啊
     * create_time : 2017-06-05
     */

    private int id;
    private String title;
    private String create_time;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
