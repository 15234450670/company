package mr.li.dance.ui.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;

import mr.li.dance.R;


public class ShareDialog extends Dialog implements View.OnClickListener {
    Context context;
    ShareClickListener dialogClickListener;

    public ShareDialog(Context context, ShareClickListener clickListener) {
        super(context, R.style.BottomDialogStyleBottom);
        this.context = context;
        dialogClickListener = clickListener;
    }

    public ShareDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_share);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void dispaly() {
        this.show();
        LinearLayout qq_layout = (LinearLayout) findViewById(R.id.qq_layout);
        LinearLayout wx_layout = (LinearLayout) findViewById(R.id.wx_layout);
        LinearLayout wx2_layout = (LinearLayout) findViewById(R.id.wx2_layout);
        LinearLayout wb_layout = (LinearLayout) findViewById(R.id.wb_layout);
        View cancel_tv = (View) findViewById(R.id.cancel_tv);

        qq_layout.setOnClickListener(this);
        wx_layout.setOnClickListener(this);
        wx2_layout.setOnClickListener(this);
        wb_layout.setOnClickListener(this);
        cancel_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.dismiss();
        switch (view.getId()) {
            case R.id.qq_layout:
                dialogClickListener.selectItem(SHARE_MEDIA.QQ.toSnsPlatform());
                break;
            case R.id.wx_layout:
                dialogClickListener.selectItem(SHARE_MEDIA.WEIXIN.toSnsPlatform());
                break;
            case R.id.wx2_layout:
                dialogClickListener.selectItem(SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform());
                break;
            case R.id.wb_layout:
                dialogClickListener.selectItem(SHARE_MEDIA.SINA.toSnsPlatform());
                break;
            default:
                break;
        }
        this.dismiss();
    }

    public interface ShareClickListener {
        public void selectItem(SnsPlatform snsPlatform);
    }
}
