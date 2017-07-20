package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.ZhiBoInfo;
import mr.li.dance.models.ZiXunInfo;
import mr.li.dance.models.Video;

/**
 * 作者: Administrator
 * 时间: 2017/5/26.
 * 功能:
 */

public class HomeItemList {
    ArrayList<ZhiBoInfo> match;
    ArrayList<Video> vedio;
    ArrayList<AlbumInfo> img;
    ArrayList<ZiXunInfo> zx;

    public ArrayList<ZhiBoInfo> getMatch() {
        return match;
    }

    public void setMatch(ArrayList<ZhiBoInfo> match) {
        this.match = match;
    }

    public ArrayList<Video> getVedio() {
        return vedio;
    }

    public void setVedio(ArrayList<Video> vedio) {
        this.vedio = vedio;
    }

    public ArrayList<AlbumInfo> getImg() {
        return img;
    }

    public void setImg(ArrayList<AlbumInfo> img) {
        this.img = img;
    }

    public ArrayList<ZiXunInfo> getZx() {
        return zx;
    }

    public void setZx(ArrayList<ZiXunInfo> zx) {
        this.zx = zx;
    }
}
