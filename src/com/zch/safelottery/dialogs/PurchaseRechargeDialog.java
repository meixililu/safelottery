package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.AccountChargeActivity;
import com.zch.safelottery.util.GetString;

/**余额不足提示dialog
 * @author Messi
 *
 */
public class PurchaseRechargeDialog extends Dialog{
	private Dialog dialog;
	private Context context;
	private TextView titleTv,messageTv;
	private Button positiveBtn,negativeBtn;
	private String title,message;
	private String positive,negative;
	private String money;
	
	private OnClickListener onClick;
	private int canselsect = -1;
	
	public PurchaseRechargeDialog(Context context) {
		super(context);
		this.context = context;
		
		title = "余额不足";
		message = "您的账户余额不足,请充值";
		positive = "马上充值";
		negative = "修改方案";
	}
	public PurchaseRechargeDialog(Context context,String positive,String negative) {
		super(context);
		this.context = context;
		this.positive = positive;
		this.negative = negative;
	}
	public PurchaseRechargeDialog(Context context,String positive,int canselsect) {
		super(context);
		this.context = context;
		this.positive = positive;
		this.canselsect=canselsect;
	}
	@Override
	public void show() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_style_normal, null);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		
		view.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(onClick !=null)
					onClick.onKey(keyCode, event);
				return false;
			}
		});
		dialog = new Dialog(context, R.style.dialog);
		
		titleTv = (TextView) view.findViewById(R.id.normal_dialog_title);
		messageTv = (TextView) view.findViewById(R.id.normal_dialog_text);
		positiveBtn = (Button) view.findViewById(R.id.normal_dialog_sure);
		negativeBtn = (Button) view.findViewById(R.id.normal_dialog_cancel);
		
		if(canselsect==0){
			negativeBtn.setVisibility(View.GONE);
		}
		
		if(judgeNull(title)!=null){
			titleTv.setText(title);
		}
		if(judgeNull(message)!=null){
			messageTv.setText(message);
		}
		if(judgeNull(positive)!=null){
			positiveBtn.setText(positive);
		}
		if(judgeNull(negative)!=null){
			negativeBtn.setText(negative);
		}
		
		if(judgeNull(money) !=null){
			messageTv.setText("您的账户余额不足,此次购买还需"+money+"元。");
		}
		positiveBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(onClick != null)
					onClick.onClick();
				else{
					Intent intent = new Intent(context,AccountChargeActivity.class);
					context.startActivity(intent);
				}
			}
		});
		
		negativeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(onClick != null)
					onClick.onNegativeClick();
			}
		});
		dialog.setContentView(view);
		dialog.show();
	}
	
	
	public interface OnClickListener{
		public void onClick();
		public void onNegativeClick();
		public void onKey(int keyCode, KeyEvent event);
	}
	
	public void setOnClickListener(OnClickListener onClick){
		this.onClick = onClick;
	}
	
	private String judgeNull(String str){
		if(str != null){
			if(!str.equals(""))
				return str;
		}
		return null;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setPositive(String positive) {
		this.positive = positive;
	}
	public void setNegative(String negative) {
		this.negative = negative;
	}
	public void setMoney(String money) {
		this.money = GetString.df.format(Double.parseDouble(money));
	}
	public void setMoney(double money) {
		this.money = GetString.df.format(money);
	}
	
}
