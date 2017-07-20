package mr.li.dance.ui.activitys.base;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lecloud.sdk.api.stats.IAppStats;
import com.lecloud.sdk.api.stats.ICdeSetting;
import com.lecloud.sdk.config.LeCloudPlayerConfig;
import com.lecloud.sdk.listener.OnInitCmfListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;
import com.yolanda.nohttp.NoHttp;

import java.util.LinkedHashMap;
import java.util.List;

import mr.li.dance.R;
import mr.li.dance.utils.NLog;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/4/24
 * 描述:
 * 修订历史:
 */

public class DanceApplication extends Application {

    public static boolean cdeInitSuccess;

    static DanceApplication instance;
    private String mDeviceToken;


    public static DanceApplication getInstance() {
        if (instance == null) {
            instance = new DanceApplication();
        }
        return instance;
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLeShi();
        NoHttp.initialize(this); // NoHttp默认初始化。
        CrashReport.initCrashReport(getApplicationContext(), "1f85dd65d8", false);
        initUmengShare();
    }


    private void initUmengShare() {
        MobclickAgent.openActivityDurationTrack(false);

        initPush();
        Config.DEBUG = false;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
    }

    {
        PlatformConfig.setWeixin("wxfe0229c4baf3f158", "5544b56dd13d051cd73d812936fa9836");
        PlatformConfig.setSinaWeibo("1421240435", "eeb8e78b545c490a9d6b3dd5dfadbe60", "https://sns.whalecloud.com/sina2/callback");
        PlatformConfig.setQQZone("1105479969", "DWL3oGEk68hyQsHC");

    }

    private void initLeShi() {
        String processName = getProcessName(this, android.os.Process.myPid());
        if (getApplicationInfo().packageName.equals(processName)) {
            //TODO CrashHandler是一个抓取崩溃log的工具类（可选）
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
                parameters.put(ICdeSetting.HOST_TYPE, LeCloudPlayerConfig.HOST_DEFAULT + "");
                parameters.put(ICdeSetting.LOG_OUTPUT_TYPE, LeCloudPlayerConfig.LOG_LOGCAT + "");
                parameters.put(ICdeSetting.USE_CDE_PORT, false + "");
                parameters.put(ICdeSetting.SCHEME_TYPE, LeCloudPlayerConfig.SCHEME_HTTP + "");
                parameters.put(IAppStats.APP_VERSION_NAME, packageInfo.versionName);
                parameters.put(IAppStats.APP_VERSION_CODE, packageInfo.versionCode + "");
                parameters.put(IAppStats.APP_PACKAGE_NAME, getPackageName());
                parameters.put(IAppStats.APP_NAME, "bcloud_android");
                LeCloudPlayerConfig.setmInitCmfListener(new OnInitCmfListener() {

                    @Override
                    public void onCdeStartSuccess() {
                        //cde启动成功,可以开始播放
                        cdeInitSuccess = true;
                        Log.d("huahua", "onCdeStartSuccess: ");
                    }

                    @Override
                    public void onCdeStartFail() {
                        //cde启动失败,不能正常播放;如果使用remote版本则可能是remote下载失败;
                        //如果使用普通版本,则可能是so文件加载失败导致
                        cdeInitSuccess = false;
                        Log.d("huahua", "onCdeStartFail: ");
                    }

                    @Override
                    public void onCmfCoreInitSuccess() {
                        //不包含cde的播放框架需要处理
                    }

                    @Override
                    public void onCmfCoreInitFail() {
                        //不包含cde的播放框架需要处理
                    }

                    @Override
                    public void onCmfDisconnected() {
                        //cde服务断开,会导致播放失败,重启一次服务
                        try {
                            cdeInitSuccess = false;
                            LeCloudPlayerConfig.init(getApplicationContext(), parameters);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                LeCloudPlayerConfig.init(getApplicationContext(), parameters);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static final String TAG = DanceApplication.class.getName();
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    private Handler handler;

    private void initPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        handler = new Handler();

        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 自定义消息的回调方法
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * 自定义通知栏样式的回调方法
             * */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.getNotification();
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知，参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);


        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                mDeviceToken = deviceToken;
                NLog.i(TAG, "device token: " + deviceToken);
                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                NLog.i(TAG, "register failed: " + s + " " + s1);
                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });

        //此处是完全自定义处理设置，两个例子，任选一种即可

    }

    public String getDeviceToken() {
        return mDeviceToken;
    }

}
