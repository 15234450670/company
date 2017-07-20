package mr.li.dance.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import mr.li.dance.R;


/**
 * Created by AMing on 15/11/26.
 * Company RongCloud
 */
public class DialogWithYesOrNoUtils {

    private static DialogWithYesOrNoUtils instance = null;

    public static DialogWithYesOrNoUtils getInstance() {
        if (instance == null) {
            instance = new DialogWithYesOrNoUtils();
        }
        return instance;
    }

    private DialogWithYesOrNoUtils() {
    }

    public void showDialog(Context context, String titleInfo, final DialogCallBack callBack) {
        showDialog(context, titleInfo, callBack, true);
    }

    public void showDialog(Context context, String titleInfo, final DialogCallBack callBack, boolean cancel) {

        AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
        alterDialog.setMessage(titleInfo);
        alterDialog.setCancelable(true);

        alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.executeEvent();
            }
        });
        if (cancel) {
            alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        alterDialog.setCancelable(cancel);
        alterDialog.show();
    }

    public void showDialog(Context context, String titleInfo, final DialogCallBack callBack, boolean cancel, String leftText, String rightText) {

        AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
        alterDialog.setMessage(titleInfo);
        alterDialog.setCancelable(true);

        alterDialog.setPositiveButton(rightText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.executeEvent();
            }
        });
        if (cancel) {
            alterDialog.setNegativeButton(leftText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        alterDialog.setCancelable(cancel);
        alterDialog.show();
    }



    public interface DialogCallBack {
        void executeEvent();

        void executeEditEvent(String editText);
    }

}
