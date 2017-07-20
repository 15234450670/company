package mr.li.dance.ui.activitys.mine;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.yolanda.nohttp.rest.Request;

import java.util.ArrayList;

import mr.li.dance.R;
import mr.li.dance.broadcast.BroadcastManager;
import mr.li.dance.https.ParameterUtils;
import mr.li.dance.https.response.StringResponse;
import mr.li.dance.https.response.UpLoadFileResponse;
import mr.li.dance.https.response.UserInfoResponse;
import mr.li.dance.models.UserInfo;
import mr.li.dance.ui.activitys.base.BaseActivity;
import mr.li.dance.ui.dialogs.BottomSingleDialog;
import mr.li.dance.utils.AppConfigs;
import mr.li.dance.utils.glide.ImageLoaderManager;
import mr.li.dance.utils.JsonMananger;
import mr.li.dance.utils.NToast;
import mr.li.dance.utils.UserInfoManager;
import mr.li.dance.utils.photo.PhotoUtils;

/**
 * 作者: Lixuewei
 * 版本: 1.0
 * 创建日期: 2017/6/5
 * 描述: 首页-我的-点头像-个人信息
 * 修订历史:
 */


public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    static public final int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private PhotoUtils photoUtils;
    private Uri selectUri;

    @Override
    public int getContentViewId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void getIntentData() {

    }

    @Override
    public void initDatas() {
        super.initDatas();
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = ParameterUtils.getSingleton().getUserInfoMap(userId);
        request(AppConfigs.myInfo_myList, request, true);

    }

    @Override
    public void initViews() {
        setTitle("个人信息");
        setPortraitChangeListener();
    }

    public static void lunch(Fragment context) {
        context.startActivity(new Intent(context.getContext(), UserInfoActivity.class));
    }

    public static void lunch(Activity context) {
        context.startActivity(new Intent(context, UserInfoActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_layout:
                showPicDialog();
                break;
            case R.id.name_layout:
                String oldName = mDanceViewHolder.getTextValue(R.id.name_tv);
                UpdateInfoActivity.Lunch(this, oldName, "姓名", UpdateInfoActivity.updateRealName,false);
                break;
            case R.id.card_layout:
                String oldCard = mDanceViewHolder.getTextValue(R.id.card_tv);
                UpdateInfoActivity.Lunch(this, oldCard, "身份证号", UpdateInfoActivity.updateIdCard,false);
                break;
            case R.id.nick_layout:
                String oldNick = mDanceViewHolder.getTextValue(R.id.nick_tv);
                UpdateInfoActivity.Lunch(this, oldNick, "昵称", UpdateInfoActivity.updateNick,false);
                break;
            case R.id.sex_layout:
                showBottomDialog();
                break;
            case R.id.mobile_layout:
                String oldMobile = mDanceViewHolder.getTextValue(R.id.mobile_tv);
                UpdateMobileActivity.Lunch(this, oldMobile, 0x001);
                break;
        }
    }

    private void showBottomDialog() {
        BottomSingleDialog bottomSingleDialog = new BottomSingleDialog(this, new BottomSingleDialog.DialogClickListener() {
            @Override
            public void selectItem(View view, String value) {
                String sex;
                if ("女".equals(value)) {
                    sex = "F";
                } else {
                    sex = "M";
                }
                String userSex = UserInfoManager.getSingleton().getUserInfo(UserInfoActivity.this).getSex();
                if (!TextUtils.equals(userSex, sex)) {
                    String userId = UserInfoManager.getSingleton().getUserId(UserInfoActivity.this);
                    Request<String> request = ParameterUtils.getSingleton().getUpdateSexMap(userId, sex);
                    request(AppConfigs.myInfo_edSex, request, true);
                }
            }
        });
        bottomSingleDialog.dispaly("男", "女");
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
                    photoUtils.takePicture(UserInfoActivity.this);
                } else {
                    photoUtils.selectPicture(UserInfoActivity.this);
                }
            }
        });
        bottomSingleDialog.dispaly("拍照", "相册");
    }

    private void setPortraitChangeListener() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    selectUri = uri;
                    updateFile(selectUri.getPath());
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

    private String newHeadUrl;

    @Override
    public void onSucceed(int what, String response) {
        super.onSucceed(what, response);
        if (what == AppConfigs.user_photo) {
            UpLoadFileResponse reponseResult = JsonMananger.getReponseResult(response, UpLoadFileResponse.class);
            ArrayList<String> fileUrlList = reponseResult.getData();
            updateHeadIcon(fileUrlList.get(0));
        } else if (what == AppConfigs.myInfo_myList) {
            UserInfoResponse reponseResult = JsonMananger.getReponseResult(response, UserInfoResponse.class);
            UserInfo userInfo = reponseResult.getData();
            UserInfoManager.getSingleton().saveUserInfo(this, userInfo);
            refreshInfo();
        } else if (what == AppConfigs.myInfo_edPicture) {
            StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
            NToast.shortToast(this, stringResponse.getData());
            UserInfoManager.getSingleton().savePicture(this, newHeadUrl);
            refreshInfo();
        } else if (what == AppConfigs.myInfo_edSex) {
            StringResponse stringResponse = JsonMananger.getReponseResult(response, StringResponse.class);
            NToast.shortToast(this, stringResponse.getData());
            String userSex = UserInfoManager.getSingleton().getUserInfo(this).getSex();
            if (TextUtils.equals("F", userSex)) {
                mDanceViewHolder.setText(R.id.sex_tv, "男");
                UserInfoManager.getSingleton().saveSex(this, "M");
            } else {
                mDanceViewHolder.setText(R.id.sex_tv, "女");
                UserInfoManager.getSingleton().saveSex(this, "F");
            }
            refreshInfo();
        }
    }

    private void updateHeadIcon(String picUrl) {
        newHeadUrl = picUrl;
        String userId = UserInfoManager.getSingleton().getUserId(this);
        Request<String> request = ParameterUtils.getSingleton().getUpdateHeadIconMap(userId, picUrl);
        request(AppConfigs.myInfo_edPicture, request, true);
    }

    private void refreshInfo() {
        UserInfo userInfo = UserInfoManager.getSingleton().getUserInfo(this);
        ImageLoaderManager.getSingleton().LoadCircle(this, userInfo.getPicture(), mDanceViewHolder.getImageView(R.id.headicon), R.drawable.default_icon);
        mDanceViewHolder.setText(R.id.name_tv, userInfo.getReal_name());
        mDanceViewHolder.setText(R.id.sex_tv, ("F".equals(userInfo.getSex()) ? "女" : "男"));
        mDanceViewHolder.setText(R.id.card_tv, userInfo.getId_card());
        mDanceViewHolder.setText(R.id.nick_tv, userInfo.getUsername());
        mDanceViewHolder.setText(R.id.mobile_tv, userInfo.getMobile());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                photoUtils.onActivityResult(UserInfoActivity.this, requestCode, resultCode, data);
                break;
            case UpdateInfoActivity.updateIdCard:
            case UpdateInfoActivity.updateNick:
            case UpdateInfoActivity.updateRealName:
                refreshInfo();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(this).destroy(AppConfigs.updateinfoAction);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onHeadLeftButtonClick(View v) {
        onBackPressed();
    }
}
