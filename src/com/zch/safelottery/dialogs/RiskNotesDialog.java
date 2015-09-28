package com.zch.safelottery.dialogs;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zch.safelottery.R;

/**
 * 验证户籍所在地
 * @author Jiang
 *
 */
public class RiskNotesDialog extends Dialog {
	private Context mContext;
	private OnClickListener mClick;
	
	public RiskNotesDialog(Context context) {
		super(context, R.style.dialog);
		this.mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_risk_notes);
		
		final EditText ed = (EditText) findViewById(R.id.dialog_risk_notes_edx);
		
		Button btn = (Button) findViewById(R.id.dialog_password_confirm);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mClick != null){
					if(!TextUtils.isEmpty(ed.getText().toString().trim())){
						mClick.onClick(ed.getText().toString().trim());
						RiskNotesDialog.this.dismiss();
					}else{
						Toast.makeText(mContext, "户籍所在地不能为空", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	public interface OnClickListener{
		public void onClick(String str);
	}
	
	public void setOnClickListener(OnClickListener mClick){
		this.mClick = mClick;
	}
}
