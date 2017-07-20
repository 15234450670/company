package mr.li.dance.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mr.li.dance.R;


public class UpdateApkDialog extends Dialog implements View.OnClickListener {
    private ClickListener mListener;
    private boolean canColose;

    public interface ClickListener {
        public void toConfirm();

        public void toRefause();
    }

    public void setClickListener(ClickListener clickListener) {
        mListener = clickListener;
    }

    public UpdateApkDialog(Context context, String version, String desc, boolean canColose, String leftText, String rightText) {
        super(context, R.style.BottomDialogStyleBottom);
        this.canColose = canColose;
        this.setCancelable(canColose);
        this.setCanceledOnTouchOutside(canColose);
        init(version, desc, leftText, rightText);
    }


    private void init(String version, String desc, String lefttext, String righttext) {
        setContentView(R.layout.dialog_updateapk);

        TextView cancelBtn = (TextView) findViewById(R.id.cancelbtn);
        TextView confirm_btn = (TextView) findViewById(R.id.confirm_btn);
        TextView messagetext = (TextView) findViewById(R.id.content_tv);
        TextView version_tv = (TextView) findViewById(R.id.version_tv);
        messagetext.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (!canColose) {
            cancelBtn.setVisibility(View.GONE);
        }
        messagetext.setText(desc);
        cancelBtn.setText(lefttext);
        confirm_btn.setText(righttext);
        version_tv.setText(version);

        confirm_btn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }


    public void display() {
        this.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (canColose) {
            dismiss();
        }
        switch (view.getId()) {
            case R.id.confirm_btn:
                mListener.toConfirm();
                break;
            case R.id.cancelbtn:
                mListener.toRefause();
                break;
            default:
                break;
        }
    }
}
