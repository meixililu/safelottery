package com.zch.safelottery.result.parser;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.RecordMatchListBean;
import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.jingcai.JZActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LotteryId;
import com.zch.safelottery.util.ScreenUtil;
import com.zch.safelottery.util.ViewUtils;

public class BDJCLotteryParser {
	// BDJC
	public static LinearLayout getBDJCView(Context context, RecordMatchListBean bean, String lid, String method) {

		LinearLayout layout = null;
		try {
			// 初始化所有控件
			layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.jc_resutl_item, null);
			TextView changci = (TextView) layout.findViewById(R.id.changci);
			TextView duizhen = (TextView) layout.findViewById(R.id.duizhen);
			TextView rangqiu_fen = (TextView) layout.findViewById(R.id.rangqiu_fen);
			TextView caiguo = (TextView) layout.findViewById(R.id.caiguo);
			View danma_layout = (View) layout.findViewById(R.id.danma_layout);
			CheckBox danma = (CheckBox) layout.findViewById(R.id.danma);
			AutoWrapView touzhu = (AutoWrapView) layout.findViewById(R.id.touzhu);

			if (lid.equals(LotteryId.JCLQ)) {
				getJCLQView(context, changci, duizhen, rangqiu_fen, caiguo, danma_layout, danma, touzhu, method, bean);
			} else if (lid.equals(LotteryId.JCZQ)) {
				getJCZQView(context, changci, duizhen, rangqiu_fen, caiguo, danma_layout, danma, touzhu, method, bean);
			}
			// else if(lid.equals(LotteryId.BJDC)){
			// getBJDCView(context,changci,duizhen,rangqiu_fen,caiguo,danma_layout,danma,touzhu,method,bean);
			// }
			else if (lid.equals(LotteryId.CGJ)) {
				getJZGJView(context, changci, duizhen, rangqiu_fen, caiguo, danma_layout, danma, touzhu, method, bean, lid);
			}
			LinearLayout.LayoutParams layout_pa = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			layout_pa.topMargin = 4;
			layout.setLayoutParams(layout_pa);
			return layout;
		} catch (Exception e) {
			if (Settings.DEBUG) {
				Log.d(Settings.TAG, "-error_msg---BDJCLotteryParser---getBDJCView(获取北单竞彩兑奖界面失败)---Exception");
				e.printStackTrace();
			}
			return layout;
		}
	}

	// 竞彩篮球
	public static void getJCLQView(Context context, TextView changci, TextView duizhen, TextView rangqiu_fen, TextView caiguo, View danma_layout, CheckBox danma, AutoWrapView touzhu, String method,
			RecordMatchListBean bean) {
		if (method.equals("01")) {// 胜负
			changci.setText(bean.getMatchNo());
			String mainteamscore = "";
			String guestteamscore = "";
			if (bean.getMainTeamScore() != null) {
				mainteamscore = bean.getMainTeamScore();
				guestteamscore = bean.getGuestTeamScore();
				duizhen.setText(bean.getMainTeam() + "(主)" +mainteamscore +":"+ guestteamscore+ bean.getGuestTeam());
			} else {
				duizhen.setText(bean.getMainTeam() + "(主)" + "VS" + bean.getGuestTeam());
			}
			rangqiu_fen.setText(bean.getResult());
			rangqiu_fen.setTextColor(context.getResources().getColor(R.color.content_txt_red));
			caiguo.setVisibility(View.GONE);
			if (bean.getDan().equals("1")) {
				danma.setChecked(true);
			} else {
				danma.setVisibility(View.GONE);
			}
			compareResult(context, touzhu, bean.getResult(), bean.getNumber(), method);
		} else if (method.equals("02")) {// 让分胜负
			changci.setText(bean.getMatchNo());
			String mainteamscore = "";
			String guestteamscore = "";
			if (bean.getMainTeamScore() != null) {
				mainteamscore = bean.getMainTeamScore();
				guestteamscore = bean.getGuestTeamScore();
				duizhen.setText(bean.getMainTeam() + "(主)" +mainteamscore +":"+ guestteamscore+ bean.getGuestTeam());
			} else {
				duizhen.setText(bean.getMainTeam() + "(主)" + "VS" + bean.getGuestTeam());
			}
			rangqiu_fen.setText(bean.getLetBall());
			caiguo.setText(bean.getResult());
			if (bean.getDan().equals("1")) {
				danma.setChecked(true);
			} else {
				danma.setVisibility(View.GONE);
			}
			compareResult(context, touzhu, bean.getResult(), bean.getNumber(), method);
		} else if (method.equals("03")) {// 胜分差
			changci.setText(bean.getMatchNo());
			String mainteamscore = "";
			String guestteamscore = "";
			if (bean.getMainTeamScore() != null) {
				mainteamscore = bean.getMainTeamScore();
				guestteamscore = bean.getGuestTeamScore();
				duizhen.setText(bean.getMainTeam() + "(主)" +mainteamscore +":"+ guestteamscore+ bean.getGuestTeam());
			} else {
				duizhen.setText(bean.getMainTeam() + "(主)" + "VS" + bean.getGuestTeam());
			}
			rangqiu_fen.setText(bean.getResult());
			caiguo.setVisibility(View.GONE);
			if (bean.getDan().equals("1")) {
				danma.setChecked(true);
			} else {
				danma.setVisibility(View.GONE);
			}
			compareResult(context, touzhu, bean.getResult(), bean.getNumber(), method);
		} else if (method.equals("04")) {// 大小分
			changci.setText(bean.getMatchNo());
			String mainteamscore = "";
			String guestteamscore = "";
			if (bean.getMainTeamScore() != null) {
				mainteamscore = bean.getMainTeamScore();
				guestteamscore = bean.getGuestTeamScore();
				duizhen.setText(bean.getMainTeam() + "(主)" +mainteamscore +":"+ guestteamscore+ bean.getGuestTeam());
			} else {
				duizhen.setText(bean.getMainTeam() + "(主)" + "VS" + bean.getGuestTeam());
			}
			rangqiu_fen.setText(bean.getPreCast());
			caiguo.setText(bean.getResult());
			if (bean.getDan().equals("1")) {
				danma.setChecked(true);
			} else {
				danma.setVisibility(View.GONE);
			}
			compareResult(context, touzhu, bean.getResult(), bean.getNumber(), method);
		}

	}

	// 竞彩篮球
	public static void compareResult(Context context, AutoWrapView autoView, String result, String scheme, String method) {
		String[] scheme_array = scheme.split(",");
		if (method.equals("01")) {
			int len = scheme_array.length;
			for (int i = 0; i < len; i++) {
				if (i >= 1) {
					TextView split = ViewUtils.Bla_scheme_For_JC(context, ",", "split", "blue");
					autoView.addView(split);
				}
				if (ifContains(scheme_array[i],result)) {
					TextView txt1 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "red");
					autoView.addView(txt1);
				} else {
					TextView txt2 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "blue");
					autoView.addView(txt2);
				}
			}
		} else if (method.equals("02")) {
			int len = scheme_array.length;
			for (int i = 0; i < len; i++) {
				if (i >= 1) {
					TextView split = ViewUtils.Bla_scheme_For_JC(context, ",", "split", "blue");
					autoView.addView(split);
				}
				if (ifContains(scheme_array[i],result)) {
					TextView txt1 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "red");
					autoView.addView(txt1);
				} else {
					TextView txt2 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "blue");
					autoView.addView(txt2);
				}
			}
		} else if (method.equals("03")) {
			for (int i = 0; i < scheme_array.length; i++) {
				if (i >= 1) {
					TextView split = ViewUtils.Bla_scheme_For_JC(context, ",", "split", "blue");
					autoView.addView(split);
				}
				if (ifContains(scheme_array[i],result)) {
					TextView txt1 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "red");
					autoView.addView(txt1);
				} else {
					TextView txt2 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "blue");
					autoView.addView(txt2);
				}
			}
		} else if (method.equals("04")) {
			for (int i = 0; i < scheme_array.length; i++) {
				if (i >= 1) {
					TextView split = ViewUtils.Bla_scheme_For_JC(context, ",", "split", "blue");
					autoView.addView(split);
				}
				if (ifContains(scheme_array[i],result)) {
					TextView txt1 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "red");
					autoView.addView(txt1);
				} else {
					TextView txt2 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "blue");
					autoView.addView(txt2);
				}
			}
		}

	}
	
	// 竞彩足球
	public static void getJCZQView(Context context, TextView changci, TextView duizhen, TextView rangqiu_fen, TextView caiguo, View danma_layout, CheckBox danma, AutoWrapView touzhu, String method,
			RecordMatchListBean bean) {
		if (method.equals(JZActivity.WF_RQSPF) || method.equals(JZActivity.WF_SPF)) {// 让球胜平负
			changci.setText(bean.getMatchNo());
			
			String mainteamscore = "";
			String guestteamscore = "";
			if (bean.getMainTeamScore() != null) {
				mainteamscore = bean.getMainTeamScore();
				guestteamscore = bean.getGuestTeamScore();
				duizhen.setText(bean.getMainTeam() + "(主)" +mainteamscore +":"+ guestteamscore+ bean.getGuestTeam());
			} else {
				duizhen.setText(bean.getMainTeam() + "(主)" + "VS" + bean.getGuestTeam());
			}
			rangqiu_fen.setText(bean.getLetBall());
			caiguo.setText(bean.getResult());
			if (bean.getDan().equals("1")) {
				danma.setChecked(true);
			} else {
				danma.setVisibility(View.GONE);
			}
			compareJCZQResult(context, touzhu, bean.getResult(), bean.getNumber(), method);
		} else if (method.equals("02")) {// 总进球数
			changci.setText(bean.getMatchNo());
			String mainteamscore = "";
			String guestteamscore = "";
			if (bean.getMainTeamScore() != null) {
				mainteamscore = bean.getMainTeamScore();
				guestteamscore = bean.getGuestTeamScore();
				duizhen.setText(bean.getMainTeam() + "(主)" +mainteamscore +":"+ guestteamscore+ bean.getGuestTeam());
			} else {
				duizhen.setText(bean.getMainTeam() + "(主)" + "VS" + bean.getGuestTeam());
			}
			String temp = bean.getResult();
			if (temp.equals("7")) {
				temp = "彩果:7+";
			} else if (temp.equals("")) {
				temp = "-";
			} else {
				temp = "彩果:" + temp;
			}
			rangqiu_fen.setText(temp);
			rangqiu_fen.setTextColor(context.getResources().getColor(R.color.content_txt_red));
			caiguo.setVisibility(View.GONE);
			if (bean.getDan().equals("1")) {
				danma.setChecked(true);
			} else {
				danma.setVisibility(View.GONE);
			}
			compareJCZQResult(context, touzhu, bean.getResult(), bean.getNumber(), method);
		} else if (method.equals("03")) {// 半场胜平负
			changci.setText(bean.getMatchNo());
			String mainteamscore = "";
			String guestteamscore = "";
			if (bean.getMainTeamScore() != null) {
				mainteamscore = bean.getMainTeamScore();
				guestteamscore = bean.getGuestTeamScore();
				duizhen.setText(bean.getMainTeam() + "(主)" +mainteamscore +":"+ guestteamscore+ bean.getGuestTeam());
			} else {
				duizhen.setText(bean.getMainTeam() + "(主)" + "VS" + bean.getGuestTeam());
			}
			rangqiu_fen.setText(bean.getResult());
			rangqiu_fen.setTextColor(context.getResources().getColor(R.color.content_txt_red));
			caiguo.setVisibility(View.GONE);
			if (bean.getDan().equals("1")) {
				danma.setChecked(true);
			} else {
				danma.setVisibility(View.GONE);
			}
			compareJCZQResult(context, touzhu, bean.getResult(), bean.getNumber(), method);
		} else if (method.equals("04")) {// 比分
			changci.setText(bean.getMatchNo());
			String mainteamscore = "";
			String guestteamscore = "";
			if (bean.getMainTeamScore() != null) {
				mainteamscore = bean.getMainTeamScore();
				guestteamscore = bean.getGuestTeamScore();
				duizhen.setText(bean.getMainTeam() + "(主)" +mainteamscore +":"+ guestteamscore+ bean.getGuestTeam());
			} else {
				duizhen.setText(bean.getMainTeam() + "(主)" + "VS" + bean.getGuestTeam());
			}
			String temp = bean.getResult();
			 if (TextUtils.isEmpty(temp)) {
					temp = "-";
			 }else if (!temp.equals("-")) {
				temp = "彩果:" + temp;
			}
			
			rangqiu_fen.setText(temp);
			rangqiu_fen.setTextColor(context.getResources().getColor(R.color.content_txt_red));
			caiguo.setVisibility(View.GONE);
			if (bean.getDan().equals("1")) {
				danma.setChecked(true);
			} else {
				danma.setVisibility(View.GONE);
			}
			compareJCZQResult(context, touzhu, bean.getResult(), bean.getNumber(), method);
		}

	}

	// 竞彩足球
	public static void compareJCZQResult(Context context, AutoWrapView autoView, String result, String scheme, String method) {
		String[] scheme_array = scheme.split(",");
		if (method.equals(JZActivity.WF_RQSPF) || method.equals(JZActivity.WF_SPF)) {
			for (int i = 0; i < scheme_array.length; i++) {
				if (i >= 1) {
					TextView split = ViewUtils.Bla_scheme_For_JC(context, ",", "split", "blue");
					autoView.addView(split);
				}
				if (ifContains(scheme_array[i],result)) {
					TextView txt1 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "red");
					autoView.addView(txt1);
				} else {
					TextView txt2 = ViewUtils.Bla_scheme_For_JC(context, scheme_array[i], "", "blue");
					autoView.addView(txt2);
				}
			}
		} else if (method.equals("02")) {
			for (int i = 0; i < scheme_array.length; i++) {
				if (i >= 1) {
					TextView split = ViewUtils.Bla_scheme_For_JC(context, ",", "split", "blue");
					autoView.addView(split);
				}
				String temp = scheme_array[i];
				if (ifContains( scheme_array[i],result)) {
					TextView txt1 = ViewUtils.Bla_scheme_For_JC(context,temp,"", "red");
					autoView.addView(txt1);
				} else {
					TextView txt2 = ViewUtils.Bla_scheme_For_JC(context,temp, "", "blue");
					autoView.addView(txt2);
				}
			}
		} else if (method.equals("03")) {
			for (int i = 0; i < scheme_array.length; i++) {
				if (i >= 1) {
					TextView split = ViewUtils.Bla_scheme_For_JC(context, ",", "split", "blue");
					autoView.addView(split);
				}
				if (ifContains(scheme_array[i],result)) {
					TextView txt1 = ViewUtils.Bla_scheme_For_JC(context,scheme_array[i], "", "red");
					autoView.addView(txt1);
				} else {
					TextView txt2 = ViewUtils.Bla_scheme_For_JC(context,scheme_array[i], "", "blue");
					autoView.addView(txt2);
				}
			}
		} else if (method.equals("04")) {
			for (int i = 0; i < scheme_array.length; i++) {
				if (i >= 1) {
					TextView split = ViewUtils.Bla_scheme_For_JC(context, ",", "split", "blue");
					autoView.addView(split);
				}
				if (ifContains(scheme_array[i],result)) {
					TextView txt1 = ViewUtils.Bla_scheme_For_JC(context,scheme_array[i], "", "red");
					autoView.addView(txt1);
				} else {
					TextView txt2 = ViewUtils.Bla_scheme_For_JC(context,scheme_array[i], "", "blue");
					autoView.addView(txt2);
				}
			}
		}

	}
	
	public static boolean ifContains(String scheme,String result){
		if(!TextUtils.isEmpty(result)){
			if(scheme.contains("(")){
				String sch[]=scheme.split("\\(");
				if(sch[0].contains(result)){
					return true;
				}
			}
		}
		return false;
	}

	// //北京单场
	// public static void getBJDCView(Context context,TextView changci,TextView
	// duizhen,TextView rangqiu_fen,TextView caiguo,
	// View danma_layout,CheckBox danma,AutoWrapView touzhu,String
	// method,RecordMatchListBean bean){
	// if(method.equals("01")){//让球胜平负
	// changci.setText( bean.getId() );
	// duizhen.setText( bean.getHome_team()+"(主)"+ bean.getScore()
	// +bean.getGuest_team() );
	// rangqiu_fen.setText( bean.getRangqiu_fen() );
	// caiguo.setText( getJCZQResultCH(bean.getResult()) );
	// if(bean.isHasDan()){
	// danma.setVisibility(View.VISIBLE);
	// danma.setChecked( bean.isHasDan() );
	// }else{
	// danma.setVisibility(View.GONE);
	// }
	// compareBJDCResult(context, touzhu, bean.getResult(), bean.getScheme(),
	// method, bean.getResult_sp());
	// }else if(method.equals("03")){//总进球数
	// changci.setText( bean.getId() );
	// duizhen.setText( bean.getHome_team()+"(主)"+ bean.getScore()
	// +bean.getGuest_team() );
	//
	// String temp = bean.getResult();
	// if(temp.equals("7")){
	// temp = "彩果:7+";
	// }else if(temp.equals("")){
	// temp = "-";
	// }else{
	// temp = "彩果:"+ temp;
	// }
	//
	// rangqiu_fen.setText( temp );
	// rangqiu_fen.setTextColor(context.getResources().getColor(R.color.content_txt_red));
	// caiguo.setVisibility(View.GONE);
	// if(bean.isHasDan()){
	// danma.setVisibility(View.VISIBLE);
	// danma.setChecked( bean.isHasDan() );
	// }else{
	// danma.setVisibility(View.GONE);
	// }
	// compareBJDCResult(context, touzhu, bean.getResult(), bean.getScheme(),
	// method, bean.getResult_sp());
	// }else if(method.equals("05")){//单场比分
	// changci.setText( bean.getId() );
	// duizhen.setText( bean.getHome_team()+"(主)"+ bean.getScore()
	// +bean.getGuest_team());
	//
	// String temp = getBDScoreCH(bean.getResult());
	// if(!temp.equals("-")){
	// temp = "彩果:"+ temp;
	// }
	//
	// rangqiu_fen.setText( temp );
	// caiguo.setVisibility(View.GONE);
	// if(bean.isHasDan()){
	// danma.setVisibility(View.VISIBLE);
	// danma.setChecked( bean.isHasDan() );
	// }else{
	// danma.setVisibility(View.GONE);
	// }
	// compareBJDCResult(context, touzhu, bean.getResult(), bean.getScheme(),
	// method, bean.getResult_sp());
	// }
	//
	// }
	//
	// //北京单场
	// public static void compareBJDCResult(Context context, AutoWrapView
	// autoView, String result, String scheme,String method ,String sp){
	// String[] scheme_array = scheme.split(",");
	// boolean hasSp = false;
	// if(!sp.contains("n") && !sp.equals("")){
	// hasSp = true;
	// }
	// String[] sps = sp.split("#");
	//
	// if(method.equals("01")){
	// for(int i=0; i<scheme_array.length; i++){
	// if(i >= 1){
	// TextView split = ViewUtils.Bla_scheme_For_JC(context,",","split","blue");
	// autoView.addView(split);
	// }
	// String temp_sp = hasSp ? "("+sps[i]+")" : "";
	// if(scheme_array[i].equals(result)){
	// TextView txt1 =
	// ViewUtils.Bla_scheme_For_JC(context,(getJCZQResultCH(scheme_array[i])+temp_sp),"","red");
	// autoView.addView(txt1);
	// }else{
	// TextView txt2 =
	// ViewUtils.Bla_scheme_For_JC(context,(getJCZQResultCH(scheme_array[i])+temp_sp),"","blue");
	// autoView.addView(txt2);
	// }
	// }
	// }else if(method.equals("03")){
	// for(int i=0; i<scheme_array.length; i++){
	// if(i >= 1){
	// TextView split = ViewUtils.Bla_scheme_For_JC(context,",","split","blue");
	// autoView.addView(split);
	// }
	// String temp_sp = hasSp ? "("+sps[i]+")" : "";
	//
	// String temp = scheme_array[i];
	// if(temp.equals("7")) temp = "7+";
	// if(result.equals("7")) result = "7+";
	//
	// if(temp.equals(result)){
	// TextView txt1 =
	// ViewUtils.Bla_scheme_For_JC(context,((temp)+temp_sp),"","red");
	// autoView.addView(txt1);
	// }else{
	// TextView txt2 =
	// ViewUtils.Bla_scheme_For_JC(context,((temp)+temp_sp),"","blue");
	// autoView.addView(txt2);
	// }
	// }
	// }else if(method.equals("05")){
	// for(int i=0; i<scheme_array.length; i++){
	// if(i >= 1){
	// TextView split = ViewUtils.Bla_scheme_For_JC(context,",","split","blue");
	// autoView.addView(split);
	// }
	// String temp_sp = hasSp ? "("+sps[i]+")" : "";
	// if(scheme_array[i].equals(result)){
	// TextView txt1 =
	// ViewUtils.Bla_scheme_For_JC(context,(getBDScoreCH(scheme_array[i])+temp_sp),"","red");
	// autoView.addView(txt1);
	// }else{
	// TextView txt2 =
	// ViewUtils.Bla_scheme_For_JC(context,(getBDScoreCH(scheme_array[i])+temp_sp),"","blue");
	// autoView.addView(txt2);
	// }
	// }
	// }
	// }

	// 冠军，冠亚军
	public static void getJZGJView(Context context, TextView changci, TextView duizhen, TextView rangqiu_fen, TextView caiguo, View danma_layout, CheckBox danma, AutoWrapView touzhu, String method,
			RecordMatchListBean bean, String lid) {
		boolean isGj = lid.equals(LotteryId.CGJ);
		String changciString = (isGj ? "冠军" : "冠亚军") + bean.getMatchNo();
		changci.setText(changciString);
		// String SchemeName = isGj ? bean.getHome_team() :
		// bean.getHome_team()+" • "+bean.getGuest_team();
		duizhen.setPadding(ScreenUtil.dip2px(context, 10), 0, 0, 0);
		// duizhen.setText( SchemeName );

		String preCast = bean.getPreCast();
		String resultId = "";
		String resultName = "";
		if (!TextUtils.isEmpty(preCast)) {
			String[] resultStrings = preCast.split("#");
			int len = resultStrings.length;
			if (len == 2) {
				resultId = resultStrings[0];
				resultName = resultStrings[1];
			}
		}
		rangqiu_fen.setText(resultName);
		caiguo.setVisibility(View.GONE);
		danma.setVisibility(View.GONE);
		compareJZGJResult(context, touzhu, resultId, "", isGj, bean.getMatchNo(), "");
	}

	// 冠军，冠亚军结果比对
	public static void compareJZGJResult(Context context, AutoWrapView autoView, String resultId, String sp, boolean isGj, String SchemeId, String SchemeName) {
		boolean hasSp = false;
		if (!sp.contains("n") && !sp.equals("") && isNum(sp)) {
			hasSp = true;
		}
		String temp_sp = hasSp ? "(" + sp + ")" : "";
		if (resultId.equals(SchemeId)) {
			TextView txt1 = ViewUtils.Bla_scheme_For_JC(context, (SchemeName + temp_sp), "", "red");
			autoView.addView(txt1);
		} else {
			TextView txt2 = ViewUtils.Bla_scheme_For_JC(context, (SchemeName + temp_sp), "", "blue");
			autoView.addView(txt2);
		}
	}

	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	public static String getSFCName(String code) {
		if (code.equals("01")) {
			return "胜1-5分";
		} else if (code.equals("02")) {
			return "胜6-10分";
		} else if (code.equals("03")) {
			return "胜11-15分";
		} else if (code.equals("04")) {
			return "胜16-20分";
		} else if (code.equals("05")) {
			return "胜21-25分";
		} else if (code.equals("06")) {
			return "胜26分以上";
		} else if (code.equals("11")) {
			return "负1-5分";
		} else if (code.equals("12")) {
			return "负6-10分";
		} else if (code.equals("13")) {
			return "负11-15分";
		} else if (code.equals("14")) {
			return "负16-20分";
		} else if (code.equals("15")) {
			return "负21-25分";
		} else if (code.equals("16")) {
			return "负26分以上";
		} else {
			return "-";
		}
	}

	// 篮球结果转换
	public static String getJCLQ_DXResultCH(String code) {
		if (code.equals("1")) {
			return "大分";
		} else if (code.equals("2")) {
			return "小分";
		} else {
			return "-";
		}
	}

//	private static String getBDScoreCH(String bf) {
//		if (bf.equals("10")) {
//			return "1:0";
//		} else if (bf.equals("20")) {
//			return "2:0";
//		} else if (bf.equals("21")) {
//			return "2:1";
//		} else if (bf.equals("30")) {
//			return "3:0";
//		} else if (bf.equals("31")) {
//			return "3:1";
//		} else if (bf.equals("32")) {
//			return "3:2";
//		} else if (bf.equals("40")) {
//			return "4:0";
//		} else if (bf.equals("41")) {
//			return "4:1";
//		} else if (bf.equals("42")) {
//			return "4:2";
//		} else if (bf.equals("50")) {
//			return "5:0";
//		} else if (bf.equals("51")) {
//			return "5:1";
//		} else if (bf.equals("52")) {
//			return "5:2";
//		} else if (bf.equals("90")) {
//			return "胜其他";
//		} else if (bf.equals("00")) {
//			return "0:0";
//		} else if (bf.equals("11")) {
//			return "1:1";
//		} else if (bf.equals("22")) {
//			return "2:2";
//		} else if (bf.equals("33")) {
//			return "3:3";
//		} else if (bf.equals("99")) {
//			return "平其他";
//		} else if (bf.equals("01")) {
//			return "0:1";
//		} else if (bf.equals("02")) {
//			return "0:2";
//		} else if (bf.equals("12")) {
//			return "1:2";
//		} else if (bf.equals("03")) {
//			return "0:3";
//		} else if (bf.equals("13")) {
//			return "1:3";
//		} else if (bf.equals("23")) {
//			return "2:3";
//		} else if (bf.equals("04")) {
//			return "0:4";
//		} else if (bf.equals("14")) {
//			return "1:4";
//		} else if (bf.equals("24")) {
//			return "2:4";
//		} else if (bf.equals("05")) {
//			return "0:5";
//		} else if (bf.equals("15")) {
//			return "1:5";
//		} else if (bf.equals("25")) {
//			return "2:5";
//		} else if (bf.equals("09")) {
//			return "负其他";
//		}
//		return "-";
//	}
}
