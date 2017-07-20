package mr.li.dance.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.tencent.tauth.AuthActivity;


import mr.li.dance.R;


/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/5/16
 * 描述: 启动页面
 * 修订历史:
 */
public class SplashActivity extends Activity {
    private android.os.Handler handler = new android.os.Handler();

    public void initDatas() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMain();
            }
        }, 1500);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initDatas();
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

}
