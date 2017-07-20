package mr.li.dance.ui.activitys.album;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import mr.li.dance.R;
import mr.li.dance.https.DownLoadCallServer;
import mr.li.dance.ui.activitys.DownLoadApkActivity;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.adapters.ShowImageAdapter;
import mr.li.dance.utils.NToast;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 图片展示页面
 * 修订历史:
 */
public class ImageShowActivity extends BaseActivity {

    private ArrayList<String> imageUrls = new ArrayList<String>();
    private ViewPager mViewPager;
    private int mCurrentPosition;


    @Override
    public int getContentViewId() {
        return R.layout.activity_imageshow;
    }

    @Override
    public void getIntentData() {

    }

    @Override
    public void initDatas() {
        super.initDatas();
        imageUrls = getIntent().getStringArrayListExtra("urls");
        mCurrentPosition = getIntent().getIntExtra("position", 0);
        boolean isfromfile = getIntent().getBooleanExtra("isfromfile", false);
        mShowImageAdapter = new ShowImageAdapter(this, imageUrls, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }, isfromfile);
    }

    ProgressBar pb_progressbar;

    @Override
    public void initViews() {
        setHeadVisibility(View.GONE);

        pb_progressbar = (ProgressBar) findViewById(R.id.pb_progressbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int currentPosition) {
                mDanceViewHolder.setText(R.id.picindex_tv, (currentPosition + 1) + "/" + imageUrls.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        mViewPager.setAdapter(mShowImageAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mDanceViewHolder.setText(R.id.picindex_tv, (mCurrentPosition+1) + "/" + imageUrls.size());
    }


    ShowImageAdapter mShowImageAdapter;


    public void toLogin(View view) {

    }

    public static void lunch(Context context, ArrayList<String> urls,int position) {
        Intent intent = new Intent(context, ImageShowActivity.class);
        intent.putExtra("urls", urls);
        intent.putExtra("position", position);
        intent.putExtra("isfromfile", true);
        context.startActivity(intent);
    }

    @Override
    public void onHeadRightButtonClick(View v) {
        String rootPath;
        int position = mViewPager.getCurrentItem();
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
        DownLoadCallServer downLoadCallServer = DownLoadCallServer.getRequestInstance();

        String downUrl = imageUrls.get(position);
        final String downFileName = downUrl.substring(downUrl.lastIndexOf("/") + 1);

        File file = new File(rootPath + downFileName);
        if (file.exists()) {
            file.delete();
        }
        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(downUrl, rootPath, downFileName, true, false);
        downLoadCallServer.add(this, 0, downloadRequest, new DownloadListener() {
            @Override
            public void onDownloadError(int i, Exception e) {
                NToast.shortToast(ImageShowActivity.this, "保存失败");
            }

            @Override
            public void onStart(int what, boolean resume, long preLenght, Headers header, long count) {
                pb_progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgress(int what, int progress, long downCount) {
                // 更新下载进度
                pb_progressbar.setProgress(progress);
            }

            @Override
            public void onFinish(int what, String filePath) {
                try {
                    MediaStore.Images.Media.insertImage(ImageShowActivity.this.getContentResolver(),
                            filePath, downFileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath)));
                NToast.shortToast(ImageShowActivity.this, "保存成功");
                pb_progressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancel(int what) {

            }
        });
    }


}
