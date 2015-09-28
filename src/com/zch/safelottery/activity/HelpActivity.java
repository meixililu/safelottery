package com.zch.safelottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LotteryId;
/*
 * 购彩帮助
 */
public class HelpActivity extends ZCHBaseActivity {

	private LinearLayout daitou;
	private LinearLayout hemai;
	private LinearLayout account;
	private LinearLayout chargeOrTake;
	private LinearLayout duijiang;
	private LinearLayout faq;
	private LinearLayout ssq;
	private LinearLayout dlt;
	private LinearLayout x3d;
	private LinearLayout k3;
	private LinearLayout k3x;
	private LinearLayout x11;
	private LinearLayout n11;
	private LinearLayout gd11;
	private LinearLayout sscCq;
	private LinearLayout sscJx;
	private LinearLayout kl8;
	private LinearLayout klnc;
	private LinearLayout pl3;
	private LinearLayout pl5;
	private LinearLayout qxc;
	private LinearLayout qlc;
	private LinearLayout pks;
	private LinearLayout chang14;
	private LinearLayout renxuan9;
	private LinearLayout jingzu;
	private LinearLayout jinglan;
	private LinearLayout beidan;
	private LinearLayout guanjun;
	private LinearLayout guanyajun;

	private String lid;
	private int kind;
	
	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			Intent intent = new Intent(HelpActivity.this, HelpDetailActivity.class);
			if (v.getId() == R.id.help_daitou) {
				kind = 1;
				lid = null;
			} else if (v.getId() == R.id.help_hemai) {
				kind = 2;
				lid = null;
			} else if (v.getId() == R.id.help_account) {
				kind = 3;
				lid = null;
			} else if (v.getId() == R.id.help_charge) {
				kind = 4;
				lid = null;
			} else if (v.getId() == R.id.help_duijiang) {
				kind = 5;
				lid = null;
			} else if (v.getId() == R.id.help_ssq) {
				kind = 6;
				lid = LotteryId.SSQ;
			} else if (v.getId() == R.id.help_dlt) {
				kind = 7;
				lid = LotteryId.DLT;
			} else if (v.getId() == R.id.help_3d) {
				kind = 8;
				lid = LotteryId.FC;
			} else if (v.getId() == R.id.help_pl3) {
				kind = 9;
				lid = LotteryId.PL3;
			} else if (v.getId() == R.id.help_k3) {
				kind = 31;
				lid = LotteryId.K3;
			} else if (v.getId() == R.id.help_k3x) {
				kind = 32;
				lid = LotteryId.K3X;
			} else if (v.getId() == R.id.help_11x5) {
				kind = 10;
				lid = LotteryId.SYXW;
			} else if (v.getId() == R.id.help_n11x5) {
				kind = 101;
				lid = LotteryId.NSYXW;
			} else if (v.getId() == R.id.help_gd11x5) {
				kind = 105;
				lid = LotteryId.GDSYXW;
			} else if (v.getId() == R.id.help_ssc_cq) {
				kind = 1101;
				lid = LotteryId.SSCCQ;
			} else if (v.getId() == R.id.help_ssc_jx) {
				kind = 1102;
				lid = LotteryId.SSCJX;
			} else if (v.getId() == R.id.help_kl8) {
				kind = 12;
				lid = null; //LotteryId.KL8;
			} else if (v.getId() == R.id.help_klnc) {
				kind = 13;
				lid = null; //LotteryId.XYNC;
//			} else if (v.getId() == R.id.help_klnc) { //14 中彩汇用户服务协议
//				kind = 14;
//
//				Intent intent = new Intent(HelpActivity.this, HelpDetailActivity.class);
//				intent.putExtra("kind", 13);
//				startActivity(intent);
			} else if (v.getId() == R.id.help_pl5_howto) {
				kind = 15;
				lid = LotteryId.PL5;
			} else if (v.getId() == R.id.help_qxc_howto) {
				kind = 16;
				lid = LotteryId.QXC;
			} else if (v.getId() == R.id.help_qlc_howto) {
				kind = 17;
				lid = LotteryId.QLC;
			} else if (v.getId() == R.id.help_pks_howto) {
				kind = 18;
				lid = null; //LotteryId.PKS;
			} else if (v.getId() == R.id.help_chang14) {
				kind = 19;
				lid = LotteryId.SFC;
			} else if (v.getId() == R.id.help_renxuan9) {
				kind = 20;
				lid = LotteryId.RX9;
			} else if (v.getId() == R.id.help_jingzu) {
				kind = 21;
				lid = LotteryId.JCZQ;
			} else if (v.getId() == R.id.help_jinglan) {
				kind = 22;
				lid = LotteryId.JCLQ;
			} else if (v.getId() == R.id.help_beidan) {
				kind = 23;
				lid = null; //LotteryId.BJDC;
			} else if (v.getId() == R.id.help_guanjun) {
				kind = 24;
				lid = null;
			} else if (v.getId() == R.id.help_guanyajun) {
				kind = 25;
				lid = null;
			} else if (v.getId() == R.id.help_faq) {
				kind = 27;//26是保底
				lid = null;
			}
			intent.putExtra("kind", kind);
			if(!TextUtils.isEmpty(lid))
				intent.putExtra(Settings.BUNDLE, LotteryId.getBundle(lid));
			
			startActivity(intent);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help);

		initViews();
	}

	private void initViews() {

		daitou = (LinearLayout) findViewById(R.id.help_daitou);
		hemai = (LinearLayout) findViewById(R.id.help_hemai);
		account = (LinearLayout) findViewById(R.id.help_account);
		chargeOrTake = (LinearLayout) findViewById(R.id.help_charge);
		duijiang = (LinearLayout) findViewById(R.id.help_duijiang);
		faq = (LinearLayout) findViewById(R.id.help_faq);
		ssq = (LinearLayout) findViewById(R.id.help_ssq);
		dlt = (LinearLayout) findViewById(R.id.help_dlt);
		x3d = (LinearLayout) findViewById(R.id.help_3d);
		pl3 = (LinearLayout) findViewById(R.id.help_pl3);
		k3 = (LinearLayout) findViewById(R.id.help_k3);
		k3x = (LinearLayout) findViewById(R.id.help_k3x);
		x11 = (LinearLayout) findViewById(R.id.help_11x5);
		n11 = (LinearLayout) findViewById(R.id.help_n11x5);
		gd11 = (LinearLayout) findViewById(R.id.help_gd11x5);
		sscCq = (LinearLayout) findViewById(R.id.help_ssc_cq);
		sscJx = (LinearLayout) findViewById(R.id.help_ssc_jx);
		kl8 = (LinearLayout) findViewById(R.id.help_kl8);
		klnc = (LinearLayout) findViewById(R.id.help_klnc);
		pl5 = (LinearLayout) findViewById(R.id.help_pl5_howto);
		qxc = (LinearLayout) findViewById(R.id.help_qxc_howto);
		qlc = (LinearLayout) findViewById(R.id.help_qlc_howto);
		pks = (LinearLayout) findViewById(R.id.help_pks_howto);
		chang14 = (LinearLayout) findViewById(R.id.help_chang14);
		renxuan9 = (LinearLayout) findViewById(R.id.help_renxuan9);
		jingzu = (LinearLayout) findViewById(R.id.help_jingzu);
		jinglan = (LinearLayout) findViewById(R.id.help_jinglan);
		beidan = (LinearLayout) findViewById(R.id.help_beidan);
		guanjun = (LinearLayout) findViewById(R.id.help_guanjun);
		guanyajun = (LinearLayout) findViewById(R.id.help_guanyajun);

		
		daitou.setOnClickListener(onClickListener);
		hemai.setOnClickListener(onClickListener);
		account.setOnClickListener(onClickListener);
		chargeOrTake.setOnClickListener(onClickListener);
		faq.setOnClickListener(onClickListener);
		ssq.setOnClickListener(onClickListener);
		dlt.setOnClickListener(onClickListener);
		x3d.setOnClickListener(onClickListener);
		pl3.setOnClickListener(onClickListener);
		k3.setOnClickListener(onClickListener);
		k3x.setOnClickListener(onClickListener);
		x11.setOnClickListener(onClickListener);
		n11.setOnClickListener(onClickListener);
		gd11.setOnClickListener(onClickListener);
		sscCq.setOnClickListener(onClickListener);
		sscJx.setOnClickListener(onClickListener);
		kl8.setOnClickListener(onClickListener);
		klnc.setOnClickListener(onClickListener);
		pl5.setOnClickListener(onClickListener);
		qxc.setOnClickListener(onClickListener);
		qlc.setOnClickListener(onClickListener);
		pks.setOnClickListener(onClickListener);
		chang14.setOnClickListener(onClickListener);
		renxuan9.setOnClickListener(onClickListener);
		jingzu.setOnClickListener(onClickListener);
		jinglan.setOnClickListener(onClickListener);
		beidan.setOnClickListener(onClickListener);
		duijiang.setOnClickListener(onClickListener);
		guanjun.setOnClickListener(onClickListener);
		guanyajun.setOnClickListener(onClickListener);
	}
}
