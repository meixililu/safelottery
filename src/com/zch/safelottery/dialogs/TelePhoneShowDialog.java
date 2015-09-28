package com.zch.safelottery.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.activity.HelpDetailActivity;
import com.zch.safelottery.activity.LoginActivity;
import com.zch.safelottery.activity.QuestionActivity;
import com.zch.safelottery.activity.UserHomeActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.GetString;

/**
 * 自定义退出dialog
 * @author Messi
 */
public class TelePhoneShowDialog extends Dialog {

	private FrameLayout phone_btn, cancle_btn;
	private View phone_show_question1, phone_show_question2, phone_show_question3, 
							phone_show_question4, phone_show_question5;
	private Context context;

	public TelePhoneShowDialog(Context context) {
		super(context,R.style.dialog);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialog_phone_show);
	    this.getWindow().setLayout(SystemInfo.width, SystemInfo.height);
	    init();
	}

	public void init() {
		phone_btn = (FrameLayout) findViewById(R.id.normal_dialog_phone);
		cancle_btn = (FrameLayout) findViewById(R.id.normal_dialog_cancel);
		phone_show_question1 = (View) findViewById(R.id.phone_show_question1);
		phone_show_question2 = (View) findViewById(R.id.phone_show_question2);
		phone_show_question3 = (View) findViewById(R.id.phone_show_question3);
		phone_show_question4 = (View) findViewById(R.id.phone_show_question4);
		phone_show_question5 = (View) findViewById(R.id.phone_show_question5);
		phone_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					StatService.onEvent(context.getApplicationContext(), "account-call", "个人中心-拨打电话", 1);
					Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse(Settings.ROBE_PHONE));
					context.startActivity(phoneIntent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		cancle_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toKefuCenter();
//				TelePhoneShowDialog.this.dismiss();
			}
		});
		phone_show_question1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, HelpDetailActivity.class);
				intent.putExtra("kind", 27);
				intent.putExtra(Settings.WEBTAGLOCATION, "question01");
				context.startActivity(intent);
			}
		});
		phone_show_question2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, HelpDetailActivity.class);
				intent.putExtra("kind", 27);
				intent.putExtra(Settings.WEBTAGLOCATION, "question02");
				context.startActivity(intent);
			}
		});
		phone_show_question3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, HelpDetailActivity.class);
				intent.putExtra("kind", 27);
				intent.putExtra(Settings.WEBTAGLOCATION, "question03");
				context.startActivity(intent);
			}
		});
		phone_show_question4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, HelpDetailActivity.class);
				intent.putExtra("kind", 27);
				intent.putExtra(Settings.WEBTAGLOCATION, "question04");
				context.startActivity(intent);
			}
		});
		phone_show_question5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, HelpDetailActivity.class);
				intent.putExtra("kind", 27);
				intent.putExtra(Settings.WEBTAGLOCATION, "question05");
				context.startActivity(intent);
			}
		});
	}
	
	//在线客服
	private void toKefuCenter(){
		if (GetString.isLogin) {
			Intent intent = new Intent(context, QuestionActivity.class);
			context.startActivity(intent);
		} else {
			Intent intent = new Intent(context, LoginActivity.class);
			intent.putExtra("from", 9);
			intent.putExtra(Settings.TOCLASS, QuestionActivity.class);
			context.startActivity(intent);
		}
		StatService.onEvent(context, "account-online-service", "个人中心-在线客服", 1);
	}
}
