package mr.li.dance.https.response;

import java.util.ArrayList;

import mr.li.dance.models.AlbumInfo;
import mr.li.dance.models.Video;

/**
 * Created by Lixuewei on 2017/6/3.
 */

public class MatchPicResponse {

    /**
     * id :
     * title :
     * address :
     * type :
     */

    private String id;
    private String title;
    private String address;
    private int type;
    private ArrayList<AlbumInfo> album;


    public ArrayList<AlbumInfo> getAlbum() {
        return album;
    }

    public void setAlbum(ArrayList<AlbumInfo> album) {
        this.album = album;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
