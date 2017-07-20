package mr.li.dance.ui.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mr.li.dance.R;


public class BottomSingleDialog extends Dialog implements View.OnClickListener {
    Context context;
    DialogClickListener dialogClickListener;

    public BottomSingleDialog(Context context, DialogClickListener clickListener) {
        super(context, R.style.BottomDialogStyleBottom);
        this.context = context;
        dialogClickListener = clickListener;
    }

    public BottomSingleDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_bottom);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void dispaly() {
        this.show();
        TextView itemone = (TextView) findViewById(R.id.itemone);
        TextView itemtwo = (TextView) findViewById(R.id.itemtwo);
        TextView cancelBtn = (TextView) findViewById(R.id.cancelbtn);

        itemone.setOnClickListener(this);
        itemtwo.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    public void dispaly(String itemOneStr,String itemTwoStr) {
        this.show();
        TextView itemone = (TextView) findViewById(R.id.itemone);
        TextView itemtwo = (TextView) findViewById(R.id.itemtwo);
        TextView cancelBtn = (TextView) findViewById(R.id.cancelbtn);
        itemone.setText(itemOneStr);
        itemtwo.setText(itemTwoStr);
        itemone.setOnClickListener(this);
        itemtwo.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        this.dismiss();
        switch (view.getId()) {
            case R.id.itemone:
                TextView itemone = (TextView) findViewById(R.id.itemone);
                dialogClickListener.selectItem(view, itemone.getText().toString());
                break;
            case R.id.itemtwo:
                TextView itemtwo = (TextView) findViewById(R.id.itemtwo);
                dialogClickListener.selectItem(view, itemtwo.getText().toString());
                break;
            case R.id.cancelbtn:
                this.dismiss();
                break;

            default:
                break;
        }
    }

    public interface DialogClickListener {
        public void selectItem(View view, String value);

    }
}
