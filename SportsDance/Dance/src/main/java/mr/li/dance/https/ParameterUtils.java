package mr.li.dance.https;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import android.util.Log;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;

import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.NLog;
import mr.li.dance.utils.glide.ImageLoaderManager;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 请求合成类,负责组成所有接口的请求参数
 * 修订历史:
 */
public class ParameterUtils {
    private volatile static ParameterUtils singleton;

    private ParameterUtils() {
    }

    public static ParameterUtils getSingleton() {
        if (singleton == null) {
            synchronized (ImageLoaderManager.class) {
                if (singleton == null) {
                    singleton = new ParameterUtils();
                }
            }
        }
        return singleton;
    }

    private Request<String> getBaseRequestForPost(String childUrl) {
        String url = new StringBuilder(AppConfigs.getDomainUrl()).append(childUrl).toString();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        NLog.i("ParameterUtils", "url == " + url);
        return request;
    }

    private Request<String> getBaseRequestCacheForPost(String childUrl) {
        Request<String> request = getBaseRequestForPost(childUrl);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        return request;
    }
    /**
     * 获取验证码
     *
     * @param mobile
     * @return
     */
    public Request<String> getCodeMap(String mobile) {
        Request<String> request = getBaseRequestForPost("/passport.sendIdentifyCode");
        request.add("mobile", mobile);
        return request;
    }

    /**
     * 找回密码发送验证码
     *
     * @param mobile
     * @return
     */
    public Request<String> getFindPwdCodeMap(String mobile) {
        Request<String> request = getBaseRequestForPost("/login.sendIdentifyCode");
        request.add("mobile", mobile);
        return request;
    }
    /**
     * 第三方登录获取验证码
     *
     * @param mobile
     * @return
     */
    public Request<String> getSendIdentifyCode_thirdMap(String mobile) {
        Request<String> request = getBaseRequestForPost("/passport.sendIdentifyCode_third");
        request.add("mobile", mobile);
        return request;
    }
    /**
     * 修改密码
     *
     * @param mobile
     * @param old_password
     * @param password
     * @param password_y
     * @return
     */
    public Request<String> getUpdatePwdMap(String mobile, String old_password, String password, String password_y) {
        Request<String> request = getBaseRequestForPost("/myInfo.retrieve");
        request.add("mobile", mobile);
        request.add("old_password", old_password);
        request.add("password", password);
        request.add("password_y", password_y);
        return request;
    }

    /**
     * 验证验证码
     *
     * @param mobile
     * @param identifyingCode
     * @return
     */
    public Request<String> getCheckCodeMap(int type,String mobile, String identifyingCode) {
        Request<String> request = getBaseRequestForPost("/passport.register");
        request.add("type", type);
        request.add("mobile", mobile);
        request.add("identifyingCode", identifyingCode);
        return request;
    }

    /**
     * 补充资料
     *
     * @param userid
     * @param real_name
     * @param id_card
     * @return
     */
    public Request<String> getReplenishInfoMap(String userid, String real_name, String id_card) {
        Request<String> request = getBaseRequestForPost("/passport.replenish");
        request.add("userid", userid);
        request.add("real_name", real_name);
        request.add("id_card", id_card);
        return request;
    }

    /**
     * 忘记密码
     *
     * @param mobile
     * @param password
     * @return
     */
    public Request<String> getFindBackPwdMap(String mobile, String password) {
        Request<String> request = getBaseRequestForPost("/login.retrieve");
        request.add("mobile", mobile);
        request.add("password", password);
        request.add("password_y", password);
        return request;
    }

    /**
     * 设置头像和昵称和密码
     *
     * @param mobile
     * @param password
     * @param picture
     * @param username
     * @return
     */
    public Request<String> getSetNickNameInfoMap(String device_token,String mobile, String password, String picture, String username,String version,String phone_xh) {
        Request<String> request = getBaseRequestForPost("/passport.nickname");
        request.add("mobile", mobile);
        request.add("password", password);
        request.add("picture", picture);
        request.add("username", username);
        request.add("device_token", device_token);
        request.add("version", version);
        request.add("phone_xh", phone_xh);
        return request;
    }

    /**
     * 登录
     *
     * @param versions
     * @param mobile
     * @param password
     * @return
     */
    public Request<String> getLoginMap(String version, String mobile, String password,String phone_xh) {
        Request<String> request = getBaseRequestForPost("/login.loginMob");
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        request.add("version", version);
        request.add("mobile_type", "Android");
        request.add("mobile", mobile);
        request.add("password", password);
        request.add("phone_xh", phone_xh);
        return request;
    }

    /**
     * 推荐页面获取
     *
     * @return
     */
    public Request<String> getHomeIndexMap() {
        Request<String> request = getBaseRequestCacheForPost("/home.index");
        return request;
    }
    /**
     * 推荐页面分页获取
     *
     * @param indexPage
     * @return
     */
    public Request<String> getHomeIndexPageMap(int indexPage) {
        Request<String> request = getBaseRequestForPost("/home.indexPage");
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }

    /**
     * 获取首页直播
     *
     * @return
     */
    public Request<String> getHomeZhiboMap() {
        Request<String> request = getBaseRequestCacheForPost("/home.zhibo");

        return request;
    }
    public Request<String> getHomeZhiboMapFromServer() {
        Request<String> request = getBaseRequestForPost("/home.zhibo");

        return request;
    }
    /**
     * 首页分页获取直播
     *
     * @param indexPage
     * @return
     */
    public Request<String> getHomeZhiboPageMap(int indexPage) {
        Request<String> request = getBaseRequestForPost("/home.zhiboPage");
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }

    /**
     * 直播详情
     *
     * @param id
     * @return
     */
    public Request<String> getHZhiboDetailMap(String id) {
        Request<String> request = getBaseRequestForPost("/home.zhiboDetailL");

        request.add("id", id);
        return request;
    }

    /**
     * 视频详情
     *
     * @param id
     * @return
     */
    public Request<String> getVideoDetailMap(String userId, String id) {
        Request<String> request = getBaseRequestForPost("/home.dianboDetailL");
        request.add("id", id);
        request.add("userid", userId);
        return request;
    }

    /**
     * 首页获取点播（视频）
     *
     * @return
     */
    public Request<String> getHomeDianboMap() {
        Request<String> request = getBaseRequestCacheForPost("/home.dianbo");
        return request;
    }
    public Request<String> getHomeDianboMapFromServer() {
        Request<String> request = getBaseRequestForPost("/home.dianbo");
        return request;
    }
    /**
     * 首页分页获取点播（视频）
     *
     * @return
     */
    public Request<String> getHomeDianboPageMap(int indexPage) {
        Request<String> request = getBaseRequestForPost("/home.dianboPage");
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }

    /**
     * 获取视频分类列表
     *
     * @param indexPage
     * @param app_type_id
     * @return
     */
    public Request<String> getVideoListMap(int indexPage, String app_type_id) {
        Request<String> request = getBaseRequestForPost("/home.dianboList");
        request.add("app_type_id", app_type_id);
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }

    /**
     * 首页获取资讯
     *
     * @return
     */
    public Request<String> getHomeZxMap() {
        Request<String> request = getBaseRequestCacheForPost("/home.zx");

        return request;
    }
    public Request<String> getHomeZxMapFromServer() {
        Request<String> request = getBaseRequestForPost("/home.zx");

        return request;
    }
    /**
     * 首页分页获取资讯
     *
     * @param indexPage
     * @return
     */
    public Request<String> getHomeZxPageMap(int indexPage) {
        Request<String> request = getBaseRequestForPost("/home.zxPage");
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }

    private void LogPage(int indexPage){
        NLog.i("ParameterUtils","page == "+indexPage);
    }

    /**
     * 获取分类资讯列表
     *
     * @param indexPage
     * @param
     * @return
     */
    public Request<String> getZixunListMap(int indexPage, String app_type_id) {
        Request<String> request = getBaseRequestForPost("/home.zxList");
        request.add("app_type_id", app_type_id);
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }

    /**
     * 首页获取图片
     *
     * @return
     */
    public Request<String> getHomeAlbumMap(int indexPage) {
        Request<String> request = getBaseRequestCacheForPost("/home.album");
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }
    public Request<String> getHomeAlbumMapFromServer(int indexPage) {
        Request<String> request = getBaseRequestForPost("/home.album");

        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }
    public Request<String> getHomeSearchMap(String type, String content, int page) {
        Request<String> request = getBaseRequestForPost("/home.Search");
        request.add("type", type);
        request.add("content", content);
        request.add("page", page);

        LogPage(page);
        return request;
    }

    /**
     * 获取相册详情
     *
     * @param id
     * @return
     */
    public Request<String> getAlbumDetailMap(String userid, String id) {
        Request<String> request = getBaseRequestForPost("/home.albumDetail");
        request.add("userid", userid);
        request.add("id", id);
        return request;
    }

    /**
     * 分页获取相册内图片
     *
     * @param id
     * @param indexPage
     * @return
     */
    public Request<String> getPhotoDetailMap(String id, int indexPage) {
        Request<String> request = getBaseRequestForPost("/home.photoDetail");
        request.add("id", id);
        request.add("page", indexPage);

        LogPage(indexPage);
        return request;
    }

    public Request<String> getCollectionMap(String userid, String thisid, int xc_video, int operation) {
        Request<String> request = getBaseRequestForPost("/home.collection");
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        request.add("userid", userid);
        request.add("thisid", thisid);
        request.add("xc_video", xc_video);
        request.add("operation", operation);

        return request;
    }

    /**
     * 个人相册页面
     *
     * @param userid
     * @param attention_userid
     * @return
     */
    public Request<String> getPersonalListMap(String userid, String attention_userid) {
        Request<String> request = getBaseRequestCacheForPost("/home.personalList");
        request.add("userid", userid);
        request.add("attention_userid", attention_userid);
        return request;
    }

    /**
     * 个人相册页面分页获取相册
     *
     * @param attention_userid
     * @param indexPage
     * @return
     */
    public Request<String> getPersonalListPageMap(String attention_userid, int indexPage) {
        Request<String> request = getBaseRequestForPost("/home.personalListPage");
        request.add("attention_userid", attention_userid);
        request.add("page", indexPage);

        LogPage(indexPage);
        return request;
    }

    /**
     * 关注操作
     *
     * @param userid
     * @param attention_userid
     * @param attention
     * @return
     */
    public Request<String> getAttentionOperationMap(String userid, String attention_userid, int attention) {
        Request<String> request = getBaseRequestForPost("/user.attentionOperation");
        request.add("userid", userid);
        request.add("attention_userid", attention_userid);
        request.add("attention", attention);
        return request;
    }

    /**
     * 获取赛事首页
     *
     * @return
     */
    public Request<String> getMatchMap() {
        Request<String> request = getBaseRequestCacheForPost("/match.index");

        return request;
    }
    public Request<String> getMatchMapFromServer() {
        Request<String> request = getBaseRequestForPost("/match.index");

        return request;
    }
    /**
     * 赛事首页分页获取
     *
     * @param indexPage
     * @return
     */
    public Request<String> getMatchIndexPageMap(int indexPage) {
        Request<String> request = getBaseRequestForPost("/match.indexList");
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }

    /**
     * 赛事搜索的接口
     *
     * @param date
     * @param content
     * @param indexPage
     * @return
     */
    public Request<String> getSearchMatchMap(String date, String content, int indexPage) {
        Request<String> request = getBaseRequestForPost("/match.matchSearch");
        request.add("date", date);
        request.add("content", content);
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }

    public Request<String> getPhotoSearchMap(String beihao, String compete_id) {
        Request<String> request = getBaseRequestForPost("/match.photoSearch");
        request.add("beihao", beihao);
        request.add("compete_id", compete_id);
        return request;
    }

    /**
     * 赛事分类接口
     *
     * @param date
     * @param indexPage
     * @return
     */
    public Request<String> getmMatchFenleiMap(String date, int type, int indexPage) {
        Request<String> request = getBaseRequestForPost("/match.matchFenlei");
        request.add("date", date);
        request.add("type", type);
        request.add("page", indexPage);

        LogPage(indexPage);
        return request;
    }

    /**
     * 赛事详情
     *
     * @param id
     * @return
     */
    public Request<String> getmMatchDetailMap(String id) {
        Request<String> request = getBaseRequestForPost("/match.matchDetail");
        request.add("id", id);
        return request;
    }

    /**
     * 成绩查询
     *
     * @param id
     * @return
     */
    public Request<String> getmScoreQueryMap(String id, int indexPage) {
        Request<String> request = getBaseRequestForPost("/match.scoreQuery");
        request.add("id", id);
        request.add("page", indexPage);
        LogPage(indexPage);
        return request;
    }

    /**
     * 成绩查询名次的接口
     *
     * @param group_name
     * @param page
     * @return
     */
    public Request<String> getmscoreQueryMMap(String matchId, String group_name, int page) {
        Request<String> request = getBaseRequestForPost("/match.scoreQueryM?page=" + page);
        request.add("group_name", group_name);
        request.add("page", page);
        request.add("id", matchId);
        LogPage(page);
        return request;
    }

    /**
     * 赛事视频的接口
     *
     * @return
     */
    public Request<String> getMatchVedioMap(String id) {
        Request<String> request = getBaseRequestCacheForPost("/match.matchVedio");
        request.add("id", id);
        return request;
    }

    /**
     * 赛事视频列表的接口
     *
     * @param id
     * @param page
     * @return
     */
    public Request<String> getMatchVedioListMap(String id, int page) {
        Request<String> request = getBaseRequestForPost("/match.matchVedioList");
        request.add("id", id);
        request.add("page", page);
        LogPage(page);
        return request;
    }

    /**
     * 精彩图片
     *
     * @param title
     * @return
     */
    public Request<String> getWonderfulPicListMap(String id, String title) {
        Request<String> request = getBaseRequestForPost("/match.jingcaiPhoto");
        request.add("id", id);
        request.add("title", title);
        return request;
    }

    /**
     * 直播视频详情的接口
     *
     * @param title
     * @param page
     * @return
     */
    public Request<String> getMatchPhotoListMap(String title, int page) {
        Request<String> request = getBaseRequestForPost("/match.matchPhotoList");
        request.add("title", title);
        request.add("page", page);
        LogPage(page);
        return request;
    }

    private void log(HashMap<String, Object> requestMap) {
        for (Entry<String, Object> entry : requestMap.entrySet()) {
            NLog.i("ParameterUtils", entry.getKey() + "--->" + entry.getValue());
        }
    }

    public Request<String> getWebMap(String id) {
        Request<String> request = getBaseRequestForPost("/home.zxDetail");
        request.add("id", id);
        return request;
    }

    /**
     * 关于我们
     *
     * @return
     */
    public Request<String> getAboutUsMap() {
        Request<String> request = getBaseRequestForPost("/user.aboutUs");
        return request;
    }
    public Request<String> getxcUploadDetailMap() {
        Request<String> request = getBaseRequestForPost("/user.xcUploadDetail");
        return request;
    }
    public Request<String> getMatch_Jsgz_Sx_SCB_Map(String compete_id,String w_page) {
        Request<String> request = getBaseRequestForPost("/match.graphicDetails");
        request.add("compete_id",compete_id);
        request.add("w_page",w_page);
        return request;
    }
    /**
     * 意见反馈
     *
     * @param userid
     * @return
     */
    public Request<String> getUserOpinionMap(String userid) {
        Request<String> request = getBaseRequestForPost("/user.opinion");
        request.add("userid", userid);
        return request;
    }

    /**
     * 发送问题反馈
     *
     * @param username
     * @return
     */
    public Request<String> getSendOpinionMap(String username, String userid, String content) {
        Request<String> request = getBaseRequestForPost("/user.sendOpinion");
        request.add("username", username);
        request.add("userid", userid);
        request.add("content", content);
        return request;
    }

    /**
     * 我的相册
     *
     * @param userid
     * @return
     */
    public Request<String> getMyAlbumMap(String userid) {
        Request<String> request = getBaseRequestForPost("/user.myAlbun");
        request.add("userid", userid);
        return request;
    }

    /**
     * 我的关注
     *
     * @param userid
     * @return
     */
    public Request<String> getMyAttentionMap(String userid) {
        Request<String> request = getBaseRequestForPost("/user.attention");
        request.add("userid", userid);
        return request;
    }

    /**
     * 我的收藏
     *
     * @param userid
     * @param xc_video // 相册的收藏：   10601 // 视频的收藏：   10602
     * @param page
     * @return
     */
    public Request<String> getCollectionListMap(String userid, int xc_video, int page) {
        Request<String> request = getBaseRequestForPost("/user.collectionList");
        request.add("userid", userid);
        request.add("xc_video", xc_video);
        request.add("page", page);
        LogPage(page);
        return request;
    }

    /**
     * 版本控制
     *
     * @param version
     * @return
     */
    public Request<String> getVersionMap(String version) {
        Request<String> request = getBaseRequestForPost("/user.version");
        request.add("type", 1);
        request.add("version", version);
        return request;
    }

    /**
     * 上传头像
     *
     * @param filepath
     * @return
     */
    public Request<String> getUpdateFileMap(String filepath) {
        Request<String> request = getBaseRequestForPost("/user.photo");
        request.add("picture", new File(filepath));
        return request;
    }

    public Request<String> getUpdateHeadIconMap(String userid, String new_picture) {
        Request<String> request = getBaseRequestForPost("/myInfo.edPicture");
        request.add("new_picture", new_picture);
        request.add("userid", userid);
        return request;
    }

    public Request<String> getUserInfoMap(String userId) {
        Request<String> request = getBaseRequestForPost("/myInfo.myList");
        request.add("userid", userId);
        return request;
    }

    public Request<String> getUpdateRealNameMap(String userId, String realName) {
        Request<String> request = getBaseRequestForPost("/myInfo.edName");
        request.add("userid", userId);
        request.add("new_real_name", realName);
        return request;
    }

    public Request<String> getUpdateCardMap(String userId, String new_id_card) {
        Request<String> request = getBaseRequestForPost("/myInfo.edIdCard");
        request.add("userid", userId);
        request.add("new_id_card", new_id_card);
        return request;
    }

    public Request<String> getUpdateSexMap(String userId, String new_sex) {
        Request<String> request = getBaseRequestForPost("/myInfo.edSex");
        request.add("userid", userId);
        request.add("new_sex", new_sex);
        return request;
    }

    public Request<String> getUpdateNickMap(String userId, String new_sex) {
        Request<String> request = getBaseRequestForPost("/myInfo.edUname");
        request.add("userid", userId);
        request.add("new_username", new_sex);
        return request;
    }

    public Request<String> getUpdateMobileMap(String userId, String mobile, String identifyingCode) {
        Request<String> request = getBaseRequestForPost("/myInfo.edMobile");
        request.add("userid", userId);
        request.add("mobile", mobile);
        request.add("identifyingCode", identifyingCode);
        return request;
    }

    public Request<String> getPassportRegister_thirdMap(String mobile) {
        Request<String> request = getBaseRequestForPost("/passport.register_third");
        request.add("mobile", mobile);
        return request;
    }

    /**
     * 考级证书查询
     *
     * @param idn  身份证号
     * @param name 姓名
     * @param type 类型 1：已考 2：全部  3：未考
     * @return
     */
    public Request<String> getKaojiCertificateMap(String idn, String name, int type) {
        Request<String> request = getBaseRequestForPost("/kaoji.certificate");
        request.add("idn", idn);
        request.add("name", name);
        request.add("type", type);
        return request;
    }
    public Request<String> getMyMessage(String userid, int page) {
        Request<String> request = getBaseRequestForPost("/user.myInfo");
        request.add("userid", userid);
        request.add("page", page);
        LogPage(page);
        return request;
    }
    public Request<String> getMyMessageDetail(int mes_id) {
        Request<String> request = getBaseRequestForPost("/user.myInfoDetail");
        request.add("mes_id", mes_id);
        return request;
    }
//    openid ：uid (新浪的uid ，微信的openid ，qq的openid)
//    source  登录方式 10402.qq   10403.微博  10404.微信
//    mobile_type  手机类型  1.Android  2.ios
//    varsion  App版本号
//    phone_xh  手机型号
    public Request<String> getPassportIsOpenId(String openid,String source,String version,String phone_xh) {
        Request<String> request = getBaseRequestForPost("/passport.isOpenid");
        request.add("openid", openid);
        request.add("source", source);
        request.add("mobile_type", 1);
        request.add("version", version);
        request.add("phone_xh", phone_xh);
        String parmars = "openid = "+openid+" source="+source+" mobile_type="+1+" varsion="+version+" phone_xh="+phone_xh;
        NLog.i("ParameterUtils","parmars == "+parmars);
        return request;
    }
    public Request<String> getPassportEdMobile(String device_token,String openid,String source,String mobile,String picture,String version,String phone_xh) {
        Request<String> request = getBaseRequestForPost("/passport.edMobile");
        request.add("openid", openid);
        request.add("source", source);
        request.add("mobile", mobile);
        request.add("picture", picture);
        request.add("device_token", device_token);
        request.add("version", version);
        request.add("phone_xh", phone_xh);
        request.add("mobile_type", 1);
        String parmars = "openid = "+openid+" source="+source+" mobile="+mobile;
        NLog.i("ParameterUtils","parmars == "+parmars);
        return request;
    }

    public Request<String> getPassportPassword(String device_token,String openid,String mobile,String source,String username,
                                               String picture,String password,String password_y) {
        Request<String> request = getBaseRequestForPost("/passport.password");
        request.add("device_token", device_token);
        request.add("openid", openid);
        request.add("mobile", mobile);
        request.add("source", source);
        request.add("username", username);
        request.add("picture", picture);
        request.add("password", password);
        request.add("password_y", password_y);
        request.add("is_equipment", 1);

        String parmars = "openid = "+openid+" source="+source+" mobile="+mobile;
        NLog.i("ParameterUtils","parmars == "+parmars);
        return request;
    }

    public Request<String> getHomeWlinkClickMap(int id) {
        Request<String> request = getBaseRequestForPost("/home.WlinkClick");
        request.add("id", id);
        return request;
    }
}
