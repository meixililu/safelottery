package com.zch.safelottery.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.view.ListLineFeedView.OnClickListListener;

public class XmlUtil {
	/***
	 * 数字彩
	 * @param context
	 * @param color 颜色 1.红球 0.蓝球 COLOR_BLUE红球  COLOR_RED蓝球  COLOR_RED_RECT方形
	 * @return
	 */
	public static CheckBox getBall(Context context,int color) throws Exception{
		CheckBox checkBox = null;
		LayoutInflater inflater = LayoutInflater.from(context);
		if(color == ViewUtil.COLOR_RED){
			checkBox = (CheckBox)inflater.inflate(R.layout.ballred, null);
		}else if(color == ViewUtil.COLOR_BLUE){
			checkBox = (CheckBox)inflater.inflate(R.layout.ballblue, null);
		}else if(color == ViewUtil.COLOR_RED_RECT){
			checkBox = (CheckBox)inflater.inflate(R.layout.ballred_rect, null);
		}
		return checkBox;
	}
	
	/***
	 * 数字彩
	 * @param context
	 * @param content 显示内容
	 * @param color 颜色 1.红球 0.蓝球 COLOR_BLUE红球  COLOR_RED蓝球  COLOR_RED_RECT方形
	 * @param isCheck 是否选中 不选中的情况下没有COLOR_RED_RECT
	 * @return
	 */
	public static TextView getBall(Context context, String content, int color, boolean isCheck){
		TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.buy_lottery_agent_scheme_ball, null);
		if(isCheck){
			if(color == ViewUtil.COLOR_BLUE){
				textview.setBackgroundResource(R.drawable.ball_blue);
			}else if(color == ViewUtil.COLOR_RED){
				textview.setBackgroundResource(R.drawable.ball_red);
			}else if(color == ViewUtil.COLOR_RED_RECT){
				textview.setBackgroundResource(R.drawable.jc_result_bg);
			}
			textview.setTextColor(context.getResources().getColor(R.color.white));
		}else{
			if(color == ViewUtil.COLOR_BLUE){
				textview.setTextColor(context.getResources().getColor(R.color.text_blue));
			}else if(color == ViewUtil.COLOR_RED){
				textview.setTextColor(context.getResources().getColor(R.color.red));
			}
			textview.setBackgroundResource(R.drawable.ball_white);
		}
		textview.setText(content);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		textview.setLayoutParams(params);
		return textview;
	}
	
	/***
	 * 快3
	 * @param context
	 * @return
	 */
	public static TextView getBall(Context context, String content, boolean isNeedBg, int color, int textSize){
		TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.k3_hezhi_tv, null);
		if(isNeedBg){
			textview.setBackgroundResource( getK3ResultBg(content) );
		}else {
			textview.setText(content);
			textview.setGravity(Gravity.BOTTOM);
			textview.setBackgroundResource(R.drawable.cz_resutl_text_bg);
//			textview.setTextSize(context.getResources().getDimension(textSize));
			textview.setTextColor(context.getResources().getColor(color));
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		textview.setLayoutParams(params);
		return textview;
	}
	
	/**自定义虚线
	 * @param context
	 * @return
	 */
	public static ImageView getDashed(Context context){
		ImageView iv = (ImageView)LayoutInflater.from(context).inflate(R.layout.line_dashed, null);
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		layout.topMargin = 1;
		layout.bottomMargin = 1;
		iv.setLayoutParams(layout);
		return iv;
	}

	/**自定义虚线
	 * @param context
	 * @return
	 */
	public static ImageView getLine(Context context){
		ImageView iv = (ImageView)LayoutInflater.from(context).inflate(R.layout.line, null);
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		layout.topMargin = 1;
		layout.bottomMargin = 1;
		iv.setLayoutParams(layout);
		return iv;
	}

	/**
	 * 取得List<CheckBox> 
	 * @param mContext 当前this
	 * @param onClick OnClickListListener事件
	 * @param layoutId 当前要进行操作的Layout
	 * @param ord 显示的内容 文本
	 * @param init 默认选中
	 * @return CheckBox 列表
	 */
	public static List<CheckBox> getCheckBox(Context mContext, int layoutId, final OnClickListListener onClick, String[] ord, int init){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		int size = ord.length;
		List<CheckBox> list = new ArrayList<CheckBox>(size);
		for(int i = 0; i < size; i++){
			CheckBox checkBox = (CheckBox)inflater.inflate(layoutId, null);
			checkBox.setText(ord[i]);
			checkBox.setId(i);
			checkBox.setGravity(Gravity.CENTER);
			if(i == init)  checkBox.setChecked(true); //初始化为选中
			checkBox.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, ScreenUtil.dip2px(mContext,36f),1.0f));//第三个参数就是weight 比重
			checkBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onClick != null) onClick.onClick(v);
				}
			});
			list.add(checkBox);
		}
		return list;
	}
	
	/**
	 * 取得List<CheckBox> 
	 * @param mContext 当前this
	 * @param onClick OnClickListListener事件
	 * @param layoutId 当前要进行操作的Layout
	 * @param ord 显示的内容 文本
	 * @param init 默认选中
	 * @return CheckBox 列表
	 */
	public static List<CheckBox> getCheckBoxWithMargin(Context mContext, int layoutId, final OnClickListListener onClick, String[] ord, int init,int right){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		int size = ord.length;
		List<CheckBox> list = new ArrayList<CheckBox>(size);
		for(int i = 0; i < size; i++){
			CheckBox checkBox = (CheckBox)inflater.inflate(layoutId, null);
			checkBox.setText(ord[i]);
			checkBox.setId(i);
			checkBox.setGravity(Gravity.CENTER);
			if(i == init)  checkBox.setChecked(true); //初始化为选中
			LinearLayout.LayoutParams  lp = new android.widget.LinearLayout.LayoutParams(0, ScreenUtil.dip2px(mContext,36f),1.0f);
			lp.rightMargin = ScreenUtil.dip2px(mContext, right);
			lp.topMargin= ScreenUtil.dip2px(mContext, right);
			checkBox.setLayoutParams(lp);//第三个参数就是weight 比重
			checkBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onClick != null) onClick.onClick(v);
				}
			});
			list.add(checkBox);
		}
		return list;
	}
	
	/**
	 * 取得List<View> 
	 * @param mContext 当前this
	 * @param onClick OnClickListListener事件
	 * @param layoutId 当前要进行操作的Layout
	 * @param ord 显示的内容 文本
	 * @param init 默认选中
	 * @return CheckBox 列表
	 */
	public static List<View> getSelectLottery(Context mContext, final OnClickListListener onClick, int[] ord){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		int size = ord.length;
		List<View> list = new ArrayList<View>(size);
		for(int i = 0; i < size; i++){
			View view = inflater.inflate(R.layout.buy_lottery_custom_item, null);
			LinearLayout layout = (LinearLayout) view.findViewById(R.id.buy_lottery_item_item);
			ImageView image = (ImageView) view.findViewById(R.id.buy_lottery_item_img);
			image.setBackgroundResource(ord[i]);
			layout.setId(i);
			LinearLayout.LayoutParams  lp =new android.widget.LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,1.0f);
			
			view.setLayoutParams(lp);//第三个参数就是weight 比重
			layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onClick != null) onClick.onClick(v);
				}
			});
			list.add(view);
		}
		return list;
	}
	
	/**
	 * 动态改变TextView 的颜色
	 * @param mContext
	 * @param buy
	 * @param loast
	 */
	public static View getPercent(Context mContext, int buy, int loast){
		View convertView = LayoutInflater.from(mContext).inflate(R.layout.combine_percent, null);
		TextView buyPercent = (TextView) convertView.findViewById(R.id.combine_lottery_buy_percent);
		TextView buyPercent_ = (TextView) convertView.findViewById(R.id.combine_lottery_buy_percent_);
		TextView lastPercent = (TextView) convertView.findViewById(R.id.combine_lottery_last_percent);
		buyPercent.setText(String.valueOf(buy));
		if(buy < 50){
			buyPercent.setTextColor( mContext.getResources().getColor(R.color.combine_light_orange) );
			buyPercent_.setTextColor( mContext.getResources().getColor(R.color.combine_light_orange) );
		}else{
			buyPercent.setTextColor( mContext.getResources().getColor(R.color.combine_dark_orange) );
			buyPercent_.setTextColor( mContext.getResources().getColor(R.color.combine_dark_orange) );
		}
		if(loast > 0){
			lastPercent.setText("保底" + loast + "%");
		}else{
			lastPercent.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	/**根据快3的开奖结果返回对应的色子图片
	 * @param result
	 * @return
	 */
	public static int getK3ResultBg(String result){
		int bg = 0;
		if( result.equals("1") ){
			bg = R.drawable.k3_1;
		}else if( result.equals("2") ){
			bg = R.drawable.k3_2;
		}else if( result.equals("3") ){
			bg = R.drawable.k3_3;
		}else if( result.equals("4") ){
			bg = R.drawable.k3_4;
		}else if( result.equals("5") ){
			bg = R.drawable.k3_5;
		}else if( result.equals("6") ){
			bg = R.drawable.k3_6;
		}
		return bg;
	}
}
