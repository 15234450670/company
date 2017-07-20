package mr.li.dance.ui.otherload;

import android.app.Activity;
import android.content.Context;
import android.text.NoCopySpan;
import android.util.Log;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: Administrator
 * 时间: 2017/5/31.
 * 功能:
 */

public class OtherLoadManager {
    Activity mContext;
    public BaseUiListener mBaseUiListener;
    private UserInfo mUserInfo;
    private Tencent mTencent;
    public IUiListener mIUiListener;

    public OtherLoadManager(Activity mContext, BaseUiListener listener) {
        this.mContext = mContext;
        mBaseUiListener = listener;
        mTencent = Tencent.createInstance("1106090579", mContext.getApplicationContext());
        initQQLoadListner();
    }

    private void initQQLoadListner() {
        mIUiListener = new IUiListener() {
            @Override
            public void onComplete(Object response) {
                JSONObject obj = (JSONObject) response;
                try {
                    String openID = obj.getString("openid");
                    String accessToken = obj.getString("access_token");
                    String expires = obj.getString("expires_in");
                    mTencent.setOpenId(openID);
                    mTencent.setAccessToken(accessToken, expires);
                    QQToken qqToken = mTencent.getQQToken();
                    mUserInfo = new UserInfo(mContext.getApplicationContext(), qqToken);
                    mUserInfo.getUserInfo(new IUiListener() {
                        @Override
                        public void onComplete(Object response) {
                            Log.e("OtherLoadManager", "登录成功" + response.toString());
                        }

                        @Override
                        public void onError(UiError uiError) {
                            Log.e("OtherLoadManager", "登录失败" + uiError.toString());
                        }

                        @Override
                        public void onCancel() {
                            Log.e("OtherLoadManager", "登录取消");

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Log.e("OtherLoadManager", "登录取消");
            }

            @Override
            public void onCancel() {

            }

        };
    }

    public void loadOfQQ() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(mContext, "all", mIUiListener);
        }
    }


}
