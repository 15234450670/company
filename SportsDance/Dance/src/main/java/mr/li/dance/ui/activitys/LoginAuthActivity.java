package mr.li.dance.ui.activitys;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.dialogs.LoadDialog;

/**
 * 作者: Administrator
 * 时间: 2017/6/8.
 * 功能:
 */

public abstract class LoginAuthActivity extends BaseActivity {


    @Override
    public void initDatas() {
        super.initDatas();
        mWaitDialog = new LoadDialog(this, true, null);

    }

    public class MyUMAuthListener implements UMAuthListener {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(mWaitDialog);

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(mWaitDialog);
        }
    }
}
