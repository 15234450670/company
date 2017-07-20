package mr.li.dance.https;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/22
 * 描述: 网络请求的监听
 * 修订历史:
 */
public interface HttpListener {
	/**
	 * 请求失败
	 */
	void onSucceed(int what, String response);

	/**
	 * 请求成功
	 */
	void onFailed(int what, int responseCode,String response);
}
