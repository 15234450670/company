package mr.li.dance.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import mr.li.dance.R;


public class CommonDialog extends Dialog implements View.OnClickListener{
	private ClickListener mListener;
	private String message;
	private String rightText;
	public interface ClickListener {
		public void toConfirm(String message);
		public void toRefause();
	}

	public void setClickListener(ClickListener clickListener){
		mListener = clickListener;
	}
	public CommonDialog(Context context, String txt, String leftText, String rightText) {
		super(context, R.style.BottomDialogStyleBottom);
		init(context, txt,leftText,rightText);
	}


	private void init(final Context context, String txt,String lefttext,String righttext) {
		setContentView(R.layout.dialog_calltel);
		message = txt;
		TextView cancelBtn = (TextView) findViewById(R.id.cancelbtn);
		TextView callbtn = (TextView) findViewById(R.id.callbtn);
		TextView messagetext = (TextView) findViewById(R.id.message);
		ImageView deleteicon = (ImageView) findViewById(R.id.deleteicon);
	
		messagetext.setText(message);
		cancelBtn.setText(lefttext);
		callbtn.setText(righttext);
		
		callbtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		deleteicon.setOnClickListener(this);
	}
	public void display(String message,boolean showCancelIcon){
		this.show();
	}
	public void display(String message){
		TextView messagetext = (TextView) findViewById(R.id.message);
		messagetext.setText(message);
		this.show();
	}
	public void display(){
		this.show();
	}
	public void display(boolean showCancelIcon){
		ImageView deleteicon = (ImageView) findViewById(R.id.deleteicon);
		deleteicon.setVisibility(View.VISIBLE);
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
		dismiss();
		switch (view.getId()) {
		case R.id.callbtn:
			mListener.toConfirm(message);
			break;
		case R.id.cancelbtn:
			mListener.toRefause();
			break;
		default:
			break;
		}
	}
}
