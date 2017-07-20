package mr.li.dance.models;

import mr.li.dance.utils.MyStrUtil;

/**
 * 作者: Administrator
 * 时间: 2017/6/8.
 * 功能:
 */

public class UpdateVersion {

    /**
     * is_force : 0
     * url :
     * is_upd : 2
     */

    private int is_force;//是否强制更新
    private String url;
    private String descr;
    private String version;
    private int is_upd;

    public int getIs_force() {
        return is_force;
    }

    public void setIs_force(int is_force) {
        this.is_force = is_force;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIs_upd() {
        return is_upd;
    }

    public void setIs_upd(int is_upd) {
        this.is_upd = is_upd;
    }

    public String getDescr() {
        if (!MyStrUtil.isEmpty(descr)) {
            descr = descr.replace("<br>", "\n");
            descr = descr.replace("-", "\n");
        }
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
