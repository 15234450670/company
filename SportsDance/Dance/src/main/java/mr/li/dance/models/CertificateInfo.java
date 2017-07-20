package mr.li.dance.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lixuewei on 2017/6/4.
 */

public class CertificateInfo implements Parcelable {

    /**
     * name : 刘思怡
     * gender : 女
     * birthday : 2004-01-21
     * photopath : http://exam.cdsf.org.cn/Images/Student/246F966E86994C4F874077DD68F6B190_s.jpg?times=2017050910575119
     * idnumber : 110109200401212528
     * levelnumber : 110109200401212528
     * artlevel : 五级
     * examresult : 及格
     * orgname : 北京市健美操体育舞蹈协会
     * examroom : 北京市昌平区枫丹西路1号北京国际温泉体育健身中心
     * examtime : 2017-05-16
     * examinername : 沈毅
     * areaname : 北京市
     * typename : 拉丁舞
     * examnumber : L110101201765001
     */

    private String name;
    private String gender;
    private String birthday;
    private String photopath;
    private String idnumber;
    private String levelnumber;
    private String artlevel;
    private String examresult;
    private String orgname;
    private String examroom;
    private String examtime;
    private String examinername;
    private String areaname;
    private String typename;
    private String examnumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getLevelnumber() {
        return levelnumber;
    }

    public void setLevelnumber(String levelnumber) {
        this.levelnumber = levelnumber;
    }

    public String getArtlevel() {
        return artlevel;
    }

    public void setArtlevel(String artlevel) {
        this.artlevel = artlevel;
    }

    public String getExamresult() {
        return examresult;
    }

    public void setExamresult(String examresult) {
        this.examresult = examresult;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getExamroom() {
        return examroom;
    }

    public void setExamroom(String examroom) {
        this.examroom = examroom;
    }

    public String getExamtime() {
        return examtime;
    }

    public void setExamtime(String examtime) {
        this.examtime = examtime;
    }

    public String getExaminername() {
        return examinername;
    }

    public void setExaminername(String examinername) {
        this.examinername = examinername;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getExamnumber() {
        return examnumber;
    }

    public void setExamnumber(String examnumber) {
        this.examnumber = examnumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.photopath);
        dest.writeString(this.idnumber);
        dest.writeString(this.levelnumber);
        dest.writeString(this.artlevel);
        dest.writeString(this.examresult);
        dest.writeString(this.orgname);
        dest.writeString(this.examroom);
        dest.writeString(this.examtime);
        dest.writeString(this.examinername);
        dest.writeString(this.areaname);
        dest.writeString(this.typename);
        dest.writeString(this.examnumber);
    }

    public CertificateInfo() {
    }

    protected CertificateInfo(Parcel in) {
        this.name = in.readString();
        this.gender = in.readString();
        this.birthday = in.readString();
        this.photopath = in.readString();
        this.idnumber = in.readString();
        this.levelnumber = in.readString();
        this.artlevel = in.readString();
        this.examresult = in.readString();
        this.orgname = in.readString();
        this.examroom = in.readString();
        this.examtime = in.readString();
        this.examinername = in.readString();
        this.areaname = in.readString();
        this.typename = in.readString();
        this.examnumber = in.readString();
    }

    public static final Parcelable.Creator<CertificateInfo> CREATOR = new Parcelable.Creator<CertificateInfo>() {
        @Override
        public CertificateInfo createFromParcel(Parcel source) {
            return new CertificateInfo(source);
        }

        @Override
        public CertificateInfo[] newArray(int size) {
            return new CertificateInfo[size];
        }
    };
}
