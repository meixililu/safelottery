package com.zch.safelottery.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.RecordMainIssueListBean;
import com.zch.safelottery.bean.RecordMatchListBean;
import com.zch.safelottery.bean.RecordNumberInforBean;
import com.zch.safelottery.bean.RecordSubGameListBean;
import com.zch.safelottery.custom_control.AutoWrapView;
import com.zch.safelottery.result.parser.BDJCLotteryParser;
import com.zch.safelottery.result.parser.CZ14LotteryParser;
import com.zch.safelottery.result.parser.DltLotteryParser;
import com.zch.safelottery.result.parser.K3LotteryParser;
import com.zch.safelottery.result.parser.K3XLotteryParser;
import com.zch.safelottery.result.parser.Kl8LotteryParser;
import com.zch.safelottery.result.parser.PksLotteryParser;
import com.zch.safelottery.result.parser.Pl3LotteryParser;
import com.zch.safelottery.result.parser.Pl5LotteryParser;
import com.zch.safelottery.result.parser.QlcLotteryParser;
import com.zch.safelottery.result.parser.SscCqLotteryParser;
import com.zch.safelottery.result.parser.SscJxLotteryParser;
import com.zch.safelottery.result.parser.SsqLotteryParser;
import com.zch.safelottery.result.parser.SyxwLotteryParser;
import com.zch.safelottery.result.parser.XyncLotteryParser;
import com.zch.safelottery.setttings.Settings;

public class LotteryResultUtils {
	/**
	 * 获取方案view
	 * 
	 * @param context
	 * @param parent
	 * @param lid
	 *            彩票id
	 * @param scheme
	 *            方案
	 * @param results
	 *            null为未开奖，非null为开奖
	 * @param isShowAll
	 *            是否显示所有
	 * @param playMethod
	 *            玩法，兑奖使用
	 */
	public static void getLotteryResultView(Context context, LinearLayout parent, String lid, List<RecordNumberInforBean> scheme, RecordMainIssueListBean recordMainIssueListBean, boolean isShowAll,
			boolean isBonus) throws Exception{
		parent.removeAllViews();
		int count = 5;
		int size = scheme.size();
		if (isShowAll) {
			count = size;
		} else {
			if (size < 5) {
				count = size;
			}
		}
		String result[] = null;
		String sortResult[]=null;
		ArrayList<String> resultList = new ArrayList<String>();
		String bonusNumber = recordMainIssueListBean.getBonusNumber();
		if (!TextUtils.isEmpty(bonusNumber) && !bonusNumber.equals("-")) {
			if(bonusNumber.contains("#") && bonusNumber.contains(",")){
				String[] temp = bonusNumber.split("#");
				for(int i=0,len = temp.length; i<len; i++){
					String[] balls = temp[i].split(",");
					for(int j=0,ballLen=balls.length; j<ballLen; j++){
						resultList.add(balls[j]);
					}
				}
			}else if(!bonusNumber.contains("#") && bonusNumber.contains(",")){
				String[] balls = bonusNumber.split(",");
				for(int j=0,ballLen=balls.length; j<ballLen; j++){
					resultList.add(balls[j]);
				}
			}
			int listSize = resultList.size();
			result = new String[listSize];
			for(int n=0; n<listSize; n++){
				result[n] = resultList.get(n);
			}
			//开奖结果排序
			Collections.sort(resultList);
			sortResult=new String[listSize];
			for(int n=0; n<listSize; n++){
				sortResult[n] = resultList.get(n);
			}
		}

		if (result != null) {
			if (isBonus) {
				getLotteryResultViewKnow(context, parent, lid, result);
			} else {
				for (int i = 0; i < count; i++) {
					parent.addView(getLotteryResultViewSingle(context, lid, scheme.get(i).getNumber(), result, scheme.get(i).getPlayCode(),scheme.get(i).getPollCode(),sortResult));
					if (!isShowAll && i <(count-1)) {
						parent.addView(ViewUtils.getDashed(context));
					} else if (isShowAll && i < (count - 1)) {
						parent.addView(ViewUtils.getDashed(context));
					}
				}
			}
		} else {
			for (int i = 0; i < count; i++) {
				RecordNumberInforBean bean = scheme.get(i);
				if (bean != null) {
					LogUtil.DefalutLog("RecordNumberInforBean:" + bean.toString());
					parent.addView(getLotteryResultUnkonwViewSingle(context, lid, bean.getNumber(), bean.getPlayCode(),bean.getPollCode()));
				}
				if (!isShowAll &&i <(count-1)) {
					parent.addView(ViewUtils.getDashed(context));
				} else if (isShowAll && i < (count - 1)) {
					parent.addView(ViewUtils.getDashed(context));
				}
			}
		}
	}

	/**
	 * 获取方案view，开奖结果，传入开奖字符数组
	 * 
	 * @param context
	 * @param parent
	 * @param lid
	 *            彩票id
	 * @param results
	 *            开奖字符数组
	 */
	public static void getLotteryResultViewKnow(Context context, LinearLayout parent, String lid, String[] results) {
		parent.removeAllViews();
		if (lid.equals(LotteryId.SSQ)) {
			parent.addView(SsqLotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.SYXW) || lid.equals(LotteryId.NSYXW) || lid.equals(LotteryId.GDSYXW)) {
			parent.addView(SyxwLotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.DLT)) {
			parent.addView(DltLotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.K3 )) {
			parent.addView(K3LotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.K3X)) {
			parent.addView(K3XLotteryParser.getLotteryResultView(context, lid, results));
		}else if (lid.equals(LotteryId.KL8)) {
			parent.addView(Kl8LotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.SSCCQ)) {
			parent.addView(SscCqLotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.SSCJX)) {
			parent.addView(SscJxLotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.XYNC)) {
			parent.addView(XyncLotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.QLC)) {
			parent.addView(QlcLotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.PKS)) {
			parent.addView(PksLotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.PL5)) {
			parent.addView(Pl5LotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.QXC)) {
			parent.addView(Pl5LotteryParser.getLotteryResultView(context, lid, results));
		} else if (lid.equals(LotteryId.SFC) || lid.equals(LotteryId.RX9)) {
			parent.addView(CZ14LotteryParser.getLotteryResultView(context, lid, results));
		} else {
			parent.addView(SyxwLotteryParser.getLotteryResultView(context, lid, results));
		}
	}

	/**
	 * 获取方案view，开奖结果，传入开奖字符串
	 * 
	 * @param context
	 * @param parent
	 * @param lid
	 *            彩票id
	 * @param results
	 *            开奖字符串
	 */
	public static void getLotteryResultViewKnow(Context context, LinearLayout parent, String lid, String results) {
		String[] res = getResultStringSet(lid, results);
		getLotteryResultViewKnow(context, parent, lid, res);
	}

	// 已开奖
	public static AutoWrapView getLotteryResultViewSingle(Context context, String lid, String userData, String[] results, String playMethod,String pollCode,String[] sortResult) {
		AutoWrapView linearLayout = ViewUtils.Bla_scheme_linearlayout(context);
		if (lid.equals(LotteryId.SSQ)) {
			SsqLotteryParser.getkonwResultView(context, linearLayout, userData, results);
		} else if (lid.equals(LotteryId.DLT)) {
			DltLotteryParser.getkonwResultView(context, linearLayout, userData, results);
		} else if (lid.equals(LotteryId.K3 )) {
			K3LotteryParser.getkonwResultView(context, linearLayout, userData, sortResult,playMethod,pollCode);
		} else if ( lid.equals(LotteryId.K3X)) {
			K3XLotteryParser.getkonwResultView(context, linearLayout, userData, sortResult,playMethod,pollCode);
		}else if (lid.equals(LotteryId.SYXW) || lid.equals(LotteryId.NSYXW) || lid.equals(LotteryId.GDSYXW)) {
			SyxwLotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.KL8)) {
			Kl8LotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.SSCCQ) ) {
			SscCqLotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.SSCJX) ) {
			SscJxLotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.PL3) || lid.equals(LotteryId.FC)) {
			Pl3LotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.XYNC)) {
			XyncLotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.QLC)) {
			QlcLotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.PKS)) {
			PksLotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.PL5)) {
			Pl5LotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.QXC)) {
			Pl5LotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else if (lid.equals(LotteryId.SFC) || lid.equals(LotteryId.RX9)) {
			CZ14LotteryParser.getkonwResultView(context, linearLayout, userData, results, playMethod);
		} else {
			HashMap<String, String[]> map = getStringSet(lid, userData);
			String[] redball = map.get("red");
			for (int i = 0; i < redball.length; i++) {

				if (results[i].equals(redball[i])) {
					TextView btn = ViewUtils.Bla_scheme_ball(context, (redball[i]), "red", "");
					linearLayout.addView(btn);
				} else {
					TextView btn = ViewUtils.Bla_scheme_ball(context, (redball[i]), "", "red");
					linearLayout.addView(btn);
				}
			}
		}

		return linearLayout;
	}

	public static String doubleString(String text) {
		if (text == null || text.equals("")) {
			return "";
		}
		if(text.contains("(") || text.contains(")")){
			return text;
		}
		return text.length() >= 2 ? text : "0" + text;
	}

	// 未开奖
	/**
	 * @param context
	 * @param lid 彩种ID
	 * @param result 投注号码
	 * @param playMethod 玩法
	 * @return
	 */
	public static View getLotteryResultUnkonwViewSingle(Context context, String lid, String result, String playMethod,String pollCode) {
		AutoWrapView linearLayout = ViewUtils.Bla_scheme_linearlayout(context);

		if (lid.equals(LotteryId.SSQ)) {
			SsqLotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.DLT)) {
			DltLotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.K3)) {
			LinearLayout linearLayout2 = new LinearLayout(context);
			K3LotteryParser.getUnkonwResultView(context, linearLayout2, result, playMethod,pollCode);
			return linearLayout2;
		} else if (lid.equals(LotteryId.K3X)) {
			LinearLayout linearLayout2 = new LinearLayout(context);
			K3XLotteryParser.getUnkonwResultView(context, linearLayout2, result, playMethod,pollCode);
			return linearLayout2;
		}else if (lid.equals(LotteryId.SYXW) || lid.equals(LotteryId.NSYXW) || lid.equals(LotteryId.GDSYXW)) {
			SyxwLotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.KL8)) {
			Kl8LotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.SSCCQ) || lid.equals(LotteryId.SSCJX)) {
			SscCqLotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.PL3) || lid.equals(LotteryId.FC)) {
			Pl3LotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.XYNC)) {
			XyncLotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.QLC)) {
			QlcLotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.PKS)) {
			PksLotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.PL5)) {
			Pl5LotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.QXC)) {
			Pl5LotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else if (lid.equals(LotteryId.SFC) || lid.equals(LotteryId.RX9)) {
			CZ14LotteryParser.getUnkonwResultView(context, linearLayout, result, playMethod);
		} else {
			HashMap<String, String[]> map = getStringSet(lid, result);
			String[] redball = map.get("red");
			for (int i = 0; i < redball.length; i++) {
				TextView btn = ViewUtils.Bla_scheme_ball(context, (redball[i]), "", "red");
				linearLayout.addView(btn);
			}
		}

		return linearLayout;
	}

	/**
	 * 彩票一注，将字符串转化为字符数据
	 * 
	 * @param lid
	 * @param result
	 * @return
	 */
	public static HashMap<String, String[]> getStringSet(String lid, String result) {
		if (!result.contains("-") && !result.equals("")) {
			HashMap<String, String[]> ball = new HashMap<String, String[]>();
			String[] red = result.split(",");
			ball.put("red", red);
			return ball;
		} else {
			return null;
		}
	}

	// 解析开奖结果
	public static String[] getResultStringSet(String lid, String result) {
		if (!result.contains("-") && !result.equals("")) {
			if (lid.equals(LotteryId.SSQ)) {
				String[] ball = { "", "", "", "", "", "", "" };
				String[] front;
				String[] temp = result.split("#");
				if (temp.length == 2) {
					front = temp[0].toString().split(",");
					for (int i = 0; i < front.length; i++) {
						ball[i] = front[i];
					}
					ball[6] = temp[1];
				}
				return ball;
			} else if (lid.equals(LotteryId.QLC)) {
				String[] ball = { "", "", "", "", "", "", "", "" };
				String[] front;
				String[] temp = result.split("#");
				if (temp.length == 2) {
					front = temp[0].toString().split(",");
					for (int i = 0; i < front.length; i++) {
						ball[i] = front[i];
					}
					ball[7] = temp[1];
				} else {
					front = temp[0].toString().split(",");
					for (int i = 0; i < front.length; i++) {
						ball[i] = front[i];
					}
				}
				return ball;
			} else if (lid.equals(LotteryId.DLT)) {
				String[] ball = { "", "", "", "", "", "", "" };
				String[] red, blue;
				String[] temp = result.split("#");
				if (temp.length == 2) {
					red = temp[0].toString().split(",");
					for (int i = 0; i < red.length; i++) {
						ball[i] = red[i];
					}
					blue = temp[1].toString().split(",");
					ball[5] = blue[0];
					ball[6] = blue[1];
				}
				return ball;
			} else if (lid.equals(LotteryId.KL8)) {
				String[] ball = new String[21];
				String[] front;
				String[] temp = result.split("#");
				if (temp.length == 2) {
					front = temp[0].toString().split(",");
					for (int i = 0; i < front.length; i++) {
						ball[i] = front[i];
					}
					ball[20] = temp[1];
				}
				return ball;
			} else if (lid.equals(LotteryId.SSQ)) {
				String[] ball = { "", "", "", "", "", "", "", "" };
				String[] front;
				String[] temp = result.split("#");
				if (temp.length == 2) {
					front = temp[0].toString().split(",");
					for (int i = 0; i < front.length; i++) {
						ball[i] = front[i];
					}
					ball[7] = temp[1];
				}
				return ball;
			} else if (lid.equals(LotteryId.K3) || lid.equals(LotteryId.K3X)) {
			    String[] ball=new String[]{result};
				return ball;
			} else {
				return result.split(",");
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取方案view
	 * 
	 * @param context
	 * @param parent
	 * @param lid
	 *            彩票id
	 * @param result_list
	 *            方案内同
	 * @param isShowDan
	 *            是否显示胆图标
	 */
	public static void getJCResultView(Context context, LinearLayout parent, String lid, List<RecordMatchListBean> result_list, String method) {
		try {
				for (RecordMatchListBean bean : result_list) {
					parent.addView(BDJCLotteryParser.getBDJCView(context, bean, lid,method));
				}
		} catch (Exception e) {
			if (Settings.DEBUG)
				Log.d(Settings.TAG, "-error_msg---LotteryResultUtils---getCZJCResultView---Exception");
		}

	}

	/**
	 * 获取方案view
	 * 
	 * @param context
	 * @param parent
	 * @param lid
	 *            彩票id
	 * @param result_list
	 *            方案内同
	 * @param isShowDan
	 *            是否显示胆图标
	 */
	public static void getCZResultView(Context context, LinearLayout parent, String lid, List<RecordSubGameListBean> result_list, boolean isShowDan, String method) {
		try {
				parent.removeAllViews();
				int i = 0;
				for (RecordSubGameListBean bean : result_list) {
					if (i > 0) {
						parent.addView(ViewUtils.getDashed(context));
					}
					parent.addView(getViewByCZBean(context, bean, i++, lid, isShowDan, method));
				}
		} catch (Exception e) {
			if (Settings.DEBUG)
				Log.d(Settings.TAG, "-error_msg---LotteryResultUtils---getCZJCResultView---Exception");
			e.printStackTrace();
		}

	}

	public static LinearLayout getViewByCZBean(Context context, RecordSubGameListBean bean, int position, String lid, boolean isShowDan, String method) {
		LinearLayout layout = null;
		try {
			layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.cz_has_dan_listview_item, null);
			TextView changci = (TextView) layout.findViewById(R.id.changci);
			TextView duizhang = (TextView) layout.findViewById(R.id.duizhang);
			TextView caiguo = (TextView) layout.findViewById(R.id.caiguo);
			LinearLayout touzhu = (LinearLayout) layout.findViewById(R.id.touzhu);
			CheckBox danma = (CheckBox) layout.findViewById(R.id.danma);
			View danma_layout = (View) layout.findViewById(R.id.danma_layout);

			changci.setText((++position) + "");
			if(bean.getFinalScore().equals("null")){
				duizhang.setText(bean.getMasterName() +"vs" + bean.getGuestName());
			}else{
			duizhang.setText(bean.getMasterName() + bean.getFinalScore() + bean.getGuestName());
			}
			caiguo.setText(bean.getResult());
			compareResult(context, touzhu, bean.getResult(), bean.getNumber());

			if (isShowDan) {
				danma_layout.setVisibility(View.VISIBLE);
				if (bean.getDan().equals("1")) {
					danma.setVisibility(View.VISIBLE);
					danma.setChecked(true);
				} else {
					danma.setVisibility(View.GONE);
				}
			} else {
				danma_layout.setVisibility(View.GONE);
			}
			// LinearLayout.LayoutParams layout_pa = new
			// LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			// layout_pa.topMargin = 0;
			return layout;
		} catch (Exception e) {
			if (Settings.DEBUG)
				Log.d(Settings.TAG, "-error_msg---LotteryResultUtils---getViewByCZBean(获取北单视图失败)---Exception");
			return layout;

		}
	}

	public static void compareResult(Context context, LinearLayout layout, String result, String scheme) {
		
		String[] scheme_array = new String[scheme.length()];
		
		for (int i = 0; i < scheme.length(); i++) {
			scheme_array[i]=scheme.substring(i, i+1);
			if (i >= 1) {
				TextView split = ViewUtils.Bla_scheme_For_JC(context, ",", "split", "blue");
				layout.addView(split);
			}
			if (scheme_array[i].equals(result)) {
				TextView txt1 = ViewUtils.Bla_scheme_For_JC(context, (scheme_array[i]), "", "red");
				layout.addView(txt1);
			} else {
				TextView txt2 = ViewUtils.Bla_scheme_For_JC(context, (scheme_array[i]), "", "blue");
				layout.addView(txt2);
			}
		}
	}

	public static boolean checkIsShowDan(String lid) {
		if (LotteryId.SFC.equals(lid)) {
			return false;
		} else if (LotteryId.RX9.equals(lid)) {
			return true;
		} else {
			return true;
		}
	}
	
	public static String[] changeSymbol(String balls,String split){
		String[] result = null;
		if(balls.contains("@")){
			String[] temp = balls.split("@");
			String[] first = temp[0].split(split);
			String[] second = temp[1].split(split);
			result = new String[first.length+second.length+2];
			result[0] = "(";
			result[first.length+1] = ")";
			System.arraycopy(first, 0, result, 1, first.length);
			System.arraycopy(second, 0, result, first.length+2, second.length);
			return result;
		}else{
			result = balls.split(split);
		}
		return result;
	}


}
