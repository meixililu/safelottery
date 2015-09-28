package com.zch.safelottery.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.custom_control.AutoWrapView;

public class ViewUtils {
	
	public static AutoWrapView Bla_scheme_linearlayout(Context context){
		AutoWrapView awv = (AutoWrapView)LayoutInflater.from(context).inflate(R.layout.buy_lottery_agent_scheme_linearlayout, null);
		return awv;
	}
	
	/**
	 * 获取一个彩球,数字
	 * @param context
	 * @param text 
	 * @param color 球的背景色
	 * @param textColor 文字颜色
	 * @return
	 */
	public static TextView Bla_scheme_ball(Context context,String text,String color, String textColor){
		TextView textview = (TextView)LayoutInflater.from(context).inflate(R.layout.buy_lottery_agent_scheme_ball, null);
		if(textColor.equals("blue")){
			textview.setTextColor(context.getResources().getColor(R.color.text_blue));
		}else if(textColor.equals("red")){
			textview.setTextColor(context.getResources().getColor(R.color.red));
		}else{
			textview.setTextColor(Color.WHITE);
		}
		if(text.equals("_")){
			textview.setText("-");
		}else{
			textview.setText(text);
		}
		if(text.equals("(")){
			textview.setText("");
			textview.setBackgroundResource(R.drawable.kuohao_l);
		}else if(text.equals(")")){
			textview.setText("");
			textview.setBackgroundResource(R.drawable.kuohao_r);
		}else if(color.equals("blue")){
			textview.setBackgroundResource(R.drawable.ball_blue);
		}else if(color.equals("red")){
			textview.setBackgroundResource(R.drawable.ball_red);
		}else{
			textview.setBackgroundResource(R.drawable.ball_white);
		}
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layout.rightMargin = 0;
		textview.setLayoutParams(layout);
		return textview;
	}
	
	/**
	 * 获取一个足彩、竞彩结果
	 * @param context
	 * @param text 
	 * @param color 球的背景色
	 * @param textColor 文字颜色
	 * @return
	 */
	public static TextView Bla_scheme_For_JC(Context context,String text,String color, String textColor){
		TextView textview = (TextView)LayoutInflater.from(context).inflate(R.layout.buy_lottery_agent_scheme_ball, null);
		if(textColor.equals("black")){
			textview.setTextColor(context.getResources().getColor(R.color.text_black));
		}else if(textColor.equals("red")){
			textview.setTextColor(context.getResources().getColor(R.color.content_txt_red));
		}else if(textColor.equals("blue")){
			textview.setTextColor(context.getResources().getColor(R.color.text_blue));
		}else{
			textview.setTextColor(Color.WHITE);
		}
		textview.setText(text);
		if(text.equals("")){
			textview.setVisibility(View.GONE);
		}
		if(color.equals("split")){
			textview.setBackgroundResource(R.drawable.cz_resutl_text_split_bg);
			textview.setTextSize(18f);
		}else if(color.equals("red")){
			textview.setBackgroundResource(R.drawable.jc_result_bg);
		}else{
			textview.setBackgroundResource(R.drawable.cz_resutl_text_bg);
		}
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layout.rightMargin = 0;
		textview.setLayoutParams(layout);
		return textview;
	}
	
	//自定义虚线
	public static ImageView getDashed(Context context){
		ImageView iv = (ImageView)LayoutInflater.from(context).inflate(R.layout.line_dashed, null);
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		layout.topMargin = 1;
		layout.bottomMargin = 1;
		iv.setLayoutParams(layout);
		return iv;
	}
	
	/**
	 * 获取幸运农场图片
	 * @param context
	 * @param num 数字编号
	 * @param color 颜色，1为彩色
	 * @return
	 */
	public static TextView get_Xync_Img(Context context,String num, int color){
//		TextView img = (TextView)LayoutInflater.from(context).inflate(R.layout.buy_lottery_agent_scheme_ball_xync, null);
//		int img_resource = XyncLotteryParser.getXyncImg(num,color);
//		Drawable drawable = context.getResources().getDrawable(img_resource);
//		BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
//		Bitmap bitmap = bitmapDrawable.getBitmap();
//		Bitmap.createScaledBitmap(bitmap, 50, 50, true); 
//		Drawable drawable_result = new BitmapDrawable(bitmap);
//		img.setBackgroundDrawable(drawable_result);
////		img.setBackgroundResource(img_resource);
//		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		layout.rightMargin = 2;
//		img.setLayoutParams(layout);
//		return img;
		return null;
	}
	
	/**
	 * 获取一个飞盘
	 * @param context
	 * @param text 文字
	 * @return
	 */
	public static TextView Bla_feipan_ball(Context context,String text){
		TextView textview = (TextView)LayoutInflater.from(context).inflate(R.layout.buy_lottery_agent_scheme_ball, null);
		textview.setTextColor(context.getResources().getColor(R.color.text_blue));
		textview.setText(text);
		textview.setTextSize(16);
		TextPaint tp = textview.getPaint(); 
		tp.setFakeBoldText(true);
		textview.setBackgroundResource(R.drawable.feipan_bg);
		
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layout.rightMargin = 0;
		textview.setLayoutParams(layout);
		return textview;
	}
	
	/**
	 * 复式分隔线
	 * @param context
	 * @return
	 */
	public static TextView getBallSplit(Context context){
		TextView tv = (TextView)LayoutInflater.from(context).inflate(R.layout.buy_lottery_agent_scheme_ball_split, null);
		return tv;
	}
	
	/**
	 * 复式分隔线
	 * @param context
	 * @return
	 */
	public static TextView getCustomText(Context context){
		TextView tv = (TextView)LayoutInflater.from(context).inflate(R.layout.custom_text_view, null);
		return tv;
	}
	
	/**
	 * 分隔线
	 * @param context
	 * @param drawableId 图片ID
	 * @return
	 */
//	public static TextView getCustomText(Context context, int drawableId){
//		TextView tv = (TextView)LayoutInflater.from(context).inflate(R.layout.custom_text_view, null);
//		tv.setBackgroundResource(drawableId);
//		tv.setTextSize(0f);
//		tv.setMaxHeight(2);
//		return tv;
//	}
	
	/**
	 * 分隔线
	 * @param context
	 * @return
	 */
	public static View getRoundLine(Context context, int layoutId){
		View tv = (View)LayoutInflater.from(context).inflate(layoutId, null);
		return tv;
	}
	
	/**
	 * 取得由TextView Vertical Group 的数据
	 * @param mContext Context
	 * @param data show at data
	 * @param textSize 字体大小
	 * @return
	 */
	public static View getTextGroupVertical(Context mContext, String[] data, int textSize){
		int dataSize = data.length;
		CheckBox mCheckBox = (CheckBox) LayoutInflater.from(mContext).inflate(R.layout.checkbox_void_xml, null);
		StringBuilder str = new StringBuilder(data[0]);
		int dataEnd = data[0].length();
		for(int i = 1; i < dataSize; i++){
			str.append("\n");
			str.append(data[i]);
		}
		//对文本中的一部分进行改变
		SpannableStringBuilder style=new SpannableStringBuilder(str.toString());
		//改变 0 - dataEnd 的颜色 跟边的颜色相同
		style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.round_table_side)), 0, dataEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		//改变 0 - dataEnd 的字体，字体为 35
		style.setSpan(new AbsoluteSizeSpan(textSize),  0, dataEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		mCheckBox.setText(style);
		mCheckBox.setButtonDrawable(null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mCheckBox.setLayoutParams(params);
		return mCheckBox;
	}
	
	/**代码生成一个imageview
	 * @param mContext
	 * @return
	 */
	public static View getImageView(Context mContext){
		ImageView img = new ImageView(mContext);
		ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		img.setLayoutParams(mParams);
		return img;
	}
	
	/**viewpager 页面选中的小点
	 * @param mContext
	 * @return
	 */
	public static ImageView getDot(Context mContext){
		ImageView img = new ImageView(mContext);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(mContext, 10),ScreenUtil.dip2px(mContext, 10));
		mParams.leftMargin = ScreenUtil.dip2px(mContext, 10);
		mParams.rightMargin = ScreenUtil.dip2px(mContext, 10);
		img.setLayoutParams(mParams);
		img.setBackgroundResource(R.drawable.dot_selector);
		return img;
	}
}
