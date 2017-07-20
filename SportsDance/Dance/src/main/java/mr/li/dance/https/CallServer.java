package mr.li.dance.https;

import android.content.Context;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.tools.MultiValueMap;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 网络请求控制器
 * 修订历史:
 */
public class CallServer {
	private static CallServer callServer;
	/**
	 * 请求队列
	 */
	private RequestQueue requestQueue;

	private CallServer() {
		requestQueue = NoHttp.newRequestQueue();
	}

	/**
	 * 请求队列
	 */
	public synchronized static CallServer getRequestInstance() {
		if (callServer == null)
			callServer = new CallServer();
		return callServer;
	}

	/**
	 * 添加一个请求到请求队列
	 *
	 * @param context   context用来实例化dialog
	 * @param what      用来标志请求，在回调方法中会返回这个what，类似handler的what
	 * @param request   请求对象
	 * @param callback  结果回调对象
	 * @param canCancel 是否允许用户取消请求
	 * @param isLoading 是否显示dialog
	 */
	public <T> void add(Context context, int what, Request<T> request, HttpListener callback, boolean canCancel, boolean isLoading) {
		MultiValueMap<String, Object> map = request.getParamKeyValues();
		requestQueue.add(what, request, new HttpResponseListener<T>(context,
				request, callback, canCancel, isLoading));
	}

	/**
	 * 取消这个sign标记的所有请求
	 */
	public void cancelBySign(Object sign) {
		requestQueue.cancelBySign(sign);
	}

	/**
	 * 取消队列中所有请求
	 */
	public void cancelAll() {
		requestQueue.cancelAll();
	}

	/**
	 * 退出app时停止所有请求
	 */
	public void stopAll() {
		requestQueue.stop();
	}


}
