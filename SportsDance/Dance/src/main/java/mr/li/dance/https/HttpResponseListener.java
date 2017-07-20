package mr.li.dance.https;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import mr.li.dance.https.response.BaseResponse;
import mr.li.dance.ui.dialogs.LoadDialog;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.NLog;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 网络请求回调
 * 修订历史:
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    /**
     * Dialog
     */
    private LoadDialog mWaitDialog;

    /**
     * 当前请求
     */
    private Request<T> mRequest;

    /**
     * 结果回调
     */
    private HttpListener callback;

    /**
     * 是否显示dialog
     */
    private Context context;
    private boolean isLoading;

    /**
     * @param context      context用来实例化dialog
     * @param request      请求对象
     * @param httpCallback 回调对象
     * @param canCancel    是否允许用户取消请求
     * @param isLoading    是否显示dialog
     */
    public HttpResponseListener(Context context, Request<T> request, HttpListener httpCallback, boolean canCancel, boolean isLoading) {
        this.context = context;
        this.mRequest = request;
        if (isLoading) {// 需要显示dialog
            mWaitDialog = new LoadDialog(context, true, null);
            mWaitDialog.setCancelable(canCancel);
            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();// dialog被用户关闭时, 取消当前请求
                }
            });
        }
        this.callback = httpCallback;
        this.isLoading = isLoading;
    }

    /**
     * 开始请求, 这里显示一个dialog
     */
    @Override
    public void onStart(int what) {


        if (mWaitDialog != null && !mWaitDialog.isShowing()) {
            Activity activity = mWaitDialog.getOwnerActivity();
            if (activity != null) {
                mWaitDialog.show();
            }
        }


    }

    /**
     * 结束请求, 这里关闭dialog
     */
    @Override
    public void onFinish(int what) {
        if (mWaitDialog != null && mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }

    /**
     * 成功回调
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        if (callback != null) {
            String responseResult = (String) response.get();
            NLog.i("HttpResponseListener", "responseResult=" + responseResult);
            if (!TextUtils.isEmpty(responseResult)) {
                responseResult = responseResult.replace("\uFEFF", "").trim();
                NLog.i("HttpResponseListener", "responseResult.trim()=" + responseResult);
                BaseResponse baseResponse = JsonMananger.getReponseResult(responseResult, BaseResponse.class);
                if (baseResponse != null) {
                    if (baseResponse.getErrorCode() == 200) {
                        callback.onSucceed(what, responseResult);
                    } else {
                        callback.onFailed(what, baseResponse.getErrorCode(), responseResult);
                    }
                } else {
                    callback.onFailed(what, -1001, "服务器数据错误");
                }
            } else {
                callback.onFailed(what, -1001, "服务器数据错误");
            }

        }
    }

    private boolean resultIsString(int what) {
        if (what == AppConfigs.SENDMSG_REQUEST_CODE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 失败回调
     */
    @Override
    public void onFailed(int what, Response<T> response) {
        if (callback != null)
            callback.onFailed(what, -1000, "服务器数据错误");
    }
}
