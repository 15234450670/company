package mr.li.dance.ui.activitys;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import java.util.ArrayList;

import mr.li.dance.R;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.UpLoadFileResponse;
import mr.li.dance.https.response.UserInfoResponse;
import mr.li.dance.models.UserInfo;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.activitys.base.DanceApplication;
import mr.li.dance.ui.activitys.mine.UpdateInfoActivity;
import mr.li.dance.ui.dialogs.BottomSingleDialog;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.UserInfoManager;
import mr.li.dance.utils.Utils;
import mr.li.dance.utils.glide.ImageLoaderManager;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.MyStrUtil;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.photo.PhotoUtils;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 注册流程 设置头像和昵称
 * 修订历史:
 */


public class SetHeadNickActivity extends BaseActivity implements View.OnClickListener {
    private String mPwd;
    private String mMobile;
    static public final int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private PhotoUtils photoUtils;
    private Uri selectUri;
    private String newHeadUrl;

    @Override
    public int getContentViewId() {
        return R.layout.activity_setheadnick;
    }

    @Override
    public void initDatas() {
        super.initDatas();
        registFinishBooadCast();
        setPortraitChangeListener();
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mPwd = mIntentExtras.getString("pwd");
        mMobile = mIntentExtras.getString("mobile");
    }

    @Override
    public void initViews() {
        setTitle("个人信息");
    }

    private void setPortraitChangeListener() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    selectUri = uri;
                    ImageLoaderManager.getSingleton().LoadCircle(SetHeadNickActivity.this, uri.getPath(), mDanceViewHolder.getImageView(R.id.headicon), R.drawable.default_icon);
                }
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    public void updateFile(String filePath) {
        Request<String> request = ParameterUtils.getSingleton().getUpdateFileMap(filePath);
        request(AppConfigs.user_photo, request, true);
    }

    public static void lunch(Context context, String mobile, String pwd) {
        Intent intent = new Intent(context, SetHeadNickActivity.class);
        intent.putExtra("mobile", mobile);
        intent.putExtra("pwd", pwd);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.name_layout:
                String oldName = mDanceViewHolder.getTextValue(R.id.name_tv);
                UpdateInfoActivity.Lunch(this, oldName, "昵称", 0x001, true);
                break;
            case R.id.head_layout:
                showPicDialog();
                break;
            case R.id.submit_btn:
                String nick = mDanceViewHolder.getTextValue(R.id.nick_tv);
                if (MyStrUtil.isEmpty(nick)) {
                    NToast.shortToast(this, "请设置昵称");
                } else {
                    if (null != selectUri && !MyStrUtil.isEmpty(selectUri.getPath())) {
                        updateFile(selectUri.getPath());
                    } else {
                        subimt();
                    }
                }
                break;
        }
    }


    private void subimt() {
        String nick = mDanceViewHolder.getTextValue(R.id.nick_tv);
        if (MyStrUtil.isEmpty(nick)) {
            NToast.shortToast(this, "请设置昵称");
        } else {
            String deviceToken = DanceApplication.getInstance().getDeviceToken();
            String version = Utils.getVersionName(this);
            String phone_xh = Utils.getSystemModel();
            Request<String> request = ParameterUtils.getSingleton().getSetNickNameInfoMap(deviceToken, mMobile, mPwd, newHeadUrl, nick,version,phone_xh);
            request(AppConfigs.passport_nickname, request, true);
        }

    }

    @Override
    public void onSucceed(int what, String responseStr) {
        super.onSucceed(what, responseStr);
        if (what == AppConfigs.user_photo) {
            UpLoadFileResponse reponseResult = JsonMananger.getReponseResult(responseStr, UpLoadFileResponse.class);
            ArrayList<String> fileUrlList = reponseResult.getData();
            if (!MyStrUtil.isEmpty(fileUrlList)) {
                newHeadUrl = fileUrlList.get(0);
                subimt();
            }else{
                NToast.shortToast(this,"头像上传失败");
            }
        } else {
            UserInfoResponse response = JsonMananger.getReponseResult(responseStr, UserInfoResponse.class);
            UserInfo userInfo = response.getData();
            String nick = mDanceViewHolder.getTextValue(R.id.nick_tv);
            userInfo.setUsername(nick);
            if (!MyStrUtil.isEmpty(newHeadUrl)) {
                userInfo.setPicture(newHeadUrl);
            }
            UserInfoManager.getSingleton().saveUserInfo(this, userInfo);
            PerfectInfoActivity.lunch(this, userInfo.getUserid());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 0x001) {
            String nick = data.getStringExtra("inputcontent");
            mDanceViewHolder.setText(R.id.nick_tv, nick);
        }
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                photoUtils.onActivityResult(SetHeadNickActivity.this, requestCode, resultCode, data);
                break;

        }
    }


    @TargetApi(23)
    private void showPicDialog() {
        BottomSingleDialog bottomSingleDialog = new BottomSingleDialog(this, new BottomSingleDialog.DialogClickListener() {
            @Override
            public void selectItem(View view, String value) {
                if ("拍照".equals(value)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkPermission = checkSelfPermission(Manifest.permission.CAMERA);
                        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                            } else {
                                new AlertDialog.Builder(mContext)
                                        .setMessage("您需要在设置里打开相机权限。")
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .create().show();
                            }
                            return;
                        }
                    }
                    photoUtils.takePicture(SetHeadNickActivity.this);
                } else {
                    photoUtils.selectPicture(SetHeadNickActivity.this);
                }
            }
        });
        bottomSingleDialog.dispaly("拍照", "相册");
    }

    @Override
    public void onHeadRightButtonClick(View v) {
        super.onHeadRightButtonClick(v);
        startActivity(new Intent(this, MainActivity.class));
    }


}
