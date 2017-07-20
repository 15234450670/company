package mr.li.dance.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;

import java.io.File;

import mr.li.dance.R;
import mr.li.dance.https.CallServer;
import mr.li.dance.https.DownLoadCallServer;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.utils.NLog;
import mr.li.dance.utils.NToast;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: apk文件下载页面
 * 修订历史:
 */


public class DownLoadApkActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_downapk;
    }

    private String downUrl;
    private boolean isForce;
    private ProgressBar pb_progressbar;
    private TextView progress_count;


    @Override
    public void getIntentData() {
        downUrl = getIntent().getStringExtra("downUrl");
        isForce = getIntent().getBooleanExtra("isForce", false);
    }

    @Override
    public void initViews() {
        setHeadVisibility(View.GONE);
        setTitle("正在下载");
        pb_progressbar = (ProgressBar) findViewById(R.id.pb_progressbar);
        progress_count = (TextView) findViewById(R.id.progress_count);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        String rootPath;
        String downFileName = "dance.apk";
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
        DownLoadCallServer downLoadCallServer = DownLoadCallServer.getRequestInstance();

        downUrl = "http://pboss.im2x.com/client/haotuandui_4.3.0.apk";
        File file = new File(rootPath + downFileName);
        if (file.exists()) {
            file.delete();
        }
        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(downUrl, rootPath, downFileName, true, false);
        downLoadCallServer.add(this, 0, downloadRequest, new DownloadListener() {
            @Override
            public void onDownloadError(int i, Exception e) {
                NToast.shortToast(DownLoadApkActivity.this, "更新失败");
            }

            @Override
            public void onStart(int what, boolean resume, long preLenght, Headers header, long count) {

            }

            @Override
            public void onProgress(int what, int progress, long downCount) {
                // 更新下载进度
                NLog.i("DownLoadApkActivity", "progress = " + progress + " downCount = " + downCount);
                pb_progressbar.setProgress(progress);
                progress_count.setText(progress+"%");
            }

            @Override
            public void onFinish(int what, String filePath) {
                openFile(new File(filePath));
            }

            @Override
            public void onCancel(int what) {

            }
        });
    }

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }


    public static void lunch(Context context, String downUrl, boolean isForce) {
        Intent intent = new Intent(context, DownLoadApkActivity.class);
        intent.putExtra("downUrl", downUrl);
        intent.putExtra("isForce", isForce);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isForce && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        DownLoadCallServer.getRequestInstance().cancelBySign(this);
        return super.onKeyDown(keyCode, event);
    }
}
