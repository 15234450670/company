package mr.li.dance.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yolanda.nohttp.error.ServerError;

import mr.li.dance.broadcast.BroadcastManager;
import mr.li.dance.models.UserInfo;

/**
 * 作者: Administrator
 * 时间: 2017/5/27.
 * 功能:
 */

public class UserInfoManager {
    private volatile static UserInfoManager singleton;
    private UserInfo mUserInfo;

    private UserInfoManager() {
    }

    public static UserInfoManager getSingleton() {
        if (singleton == null) {
            synchronized (UserInfoManager.class) {
                if (singleton == null) {
                    singleton = new UserInfoManager();
                }
            }
        }
        return singleton;
    }

    public boolean isLoading(Context context) {
        if (mUserInfo == null) {
            mUserInfo = getUserInfo(context);
        }
        return !MyStrUtil.isEmpty(mUserInfo);
    }


    public String getUserId(Context context) {
        if (mUserInfo == null) {
            mUserInfo = getUserInfo(context);
        }
        if(null == mUserInfo){
            return "";
        }else{
            return mUserInfo.getUserid();
        }

    }


    public UserInfo getUserInfo(Context context) {
        if (mUserInfo == null) {
            SharedPreferences mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
            String userJson = mSp.getString(AppConfigs.USERINFOJSON, "");
            if (MyStrUtil.isEmpty(userJson)) {
                return null;
            }
            mUserInfo = JsonMananger.getReponseResult(userJson, UserInfo.class);
        }
        return mUserInfo;
    }

    public void saveUserInfo(Context context, UserInfo userInfo) {
        if (MyStrUtil.isEmpty(userInfo.getUserid())) {
            userInfo.setUserid(mUserInfo.getUserid());
        }
        SharedPreferences mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSp.edit();
        String userJson = "";
        try {
            userJson = JsonMananger.beanToJson(userInfo);
        } catch (ServerError serverError) {
            serverError.printStackTrace();
        }
        editor.putString(AppConfigs.USERINFOJSON, userJson).commit();
        mUserInfo = null;
        getUserInfo(context);
        BroadcastManager.getInstance(context).sendBroadcast(AppConfigs.updateinfoAction);
    }

    public void clearInfo(Context context) {
        mUserInfo = null;
        SharedPreferences mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(AppConfigs.USERINFOJSON, "");
        editor.putString(AppConfigs.LOGING_PASSWORD, "");
        editor.putString(AppConfigs.LOGING_NAME, "");
        editor.commit();
        BroadcastManager.getInstance(context).sendBroadcast(AppConfigs.updateinfoAction);
    }

    public void saveLoginInfo(Context context, String loginName, String loginPwd) {
        mUserInfo = null;
        SharedPreferences mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(AppConfigs.LOGING_NAME, loginName);
        editor.putString(AppConfigs.LOGING_PASSWORD, loginPwd);
        editor.commit();
    }

    public void saveLoginInfo(Context context, UserInfo userInfo) {
        saveUserInfo(context, userInfo);
    }

    public String getLoginName(Context context) {
        SharedPreferences mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return mSp.getString(AppConfigs.LOGING_NAME, "");
    }

    public String getLoginPwd(Context context) {
        SharedPreferences mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return mSp.getString(AppConfigs.LOGING_PASSWORD, "");
    }


    public void saveUsername(Context context, String username) {
        mUserInfo.setUsername(username);
        saveUserInfo(context, mUserInfo);

    }

    public void saveMobile(Context context, String mobile) {
        mUserInfo.setMobile(mobile);
        saveUserInfo(context, mUserInfo);
    }

    public void savePicture(Context context, String picture) {
        mUserInfo.setPicture(picture);
        saveUserInfo(context, mUserInfo);
    }

    public void saveReal_name(Context context, String real_name) {
        mUserInfo.setReal_name(real_name);
        saveUserInfo(context, mUserInfo);
    }

    public void saveSex(Context context, String sex) {
        mUserInfo.setSex(sex);
        saveUserInfo(context, mUserInfo);
    }

    public void saveId_card(Context context, String id_card) {
        mUserInfo.setId_card(id_card);
        saveUserInfo(context, mUserInfo);
    }


}
