package mr.li.dance.https;

import android.content.Context;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.tools.MultiValueMap;

/**
 * Created by Lixuewei on 2017/5/30.
 */

public class DownLoadCallServer {
    private static DownLoadCallServer callServer;
    /**
     * 请求队列
     */
    private DownloadQueue requestQueue;

    private DownLoadCallServer() {
        requestQueue = NoHttp.newDownloadQueue();
    }

    /**
     * 请求队列
     */
    public synchronized static DownLoadCallServer getRequestInstance() {
        if (callServer == null)
            callServer = new DownLoadCallServer();
        return callServer;
    }

    public <T> void add(Context context, int what, DownloadRequest downloadRequest, DownloadListener downloadListener) {
        requestQueue.add(what, downloadRequest, downloadListener);
    }

    public void cancelBySign(Object sign) {
        requestQueue.cancelBySign(sign);
    }

}
