package com.zch.safelottery.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
public class ChargePhoneCallSuccessActivity extends ZCHBaseActivity {
	private Button  closebutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.charge_phonecall_success);
			initViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initViews() {
		closebutton=(Button)findViewById(R.id.charge_phonecall_success_finish_btn);
		closebutton.setOnClickListener(onClickListener);
		closebutton.setClickable(false);
		timer.start();
	}

	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (v.getId() == R.id.charge_phonecall_success_finish_btn) {
				finish();
				timer.cancel();
			}
		}
	};
	
	CountDownTimer timer = new CountDownTimer(10000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			closebutton.setText("关闭    " +millisUntilFinished / 1000 + "秒后可点击");
		}
		
		@Override
		public void onFinish() {
			closebutton.setClickable(true);
			closebutton.setText("关闭");
			closebutton.setTextColor(ChargePhoneCallSuccessActivity.this.getResources().getColor(R.color.black));
		}
	};
}
