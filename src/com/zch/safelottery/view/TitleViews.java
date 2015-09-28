package com.zch.safelottery.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zch.safelottery.R;

public class TitleViews {

	private Context context;
	private FrameLayout frameLayout;
	private TextView titleNameTv;
	private TextView btnNameTv;
//	private TextView btnGap;
	private ImageView btnIconImage;
	
	private String titleName;
	
	private static TitleViews titleViews;
	
	private View view;
	
//	private boolean isName, isIcon;
	public TitleViews(Context context, String titleName){
		this.context = context;
		this.titleName = titleName;
		initView();
	}
	
//	public static TitleViews getTitleView(Context context, String titleName){
//		if(titleViews == null){
//			titleViews = new TitleViews(context, titleName);
//		}
//		return titleViews;
//	}
	
	private void initView(){
		view = LayoutInflater.from(context).inflate(R.layout.title_filtrate, null);
		frameLayout = (FrameLayout) view.findViewById(R.id.title_filtrate_listener_frame);
		titleNameTv = (TextView) view.findViewById(R.id.title_filtrate_name);
		btnNameTv = (TextView) view.findViewById(R.id.title_filtrate_btn_name);
//		btnGap = (TextView) view.findViewById(R.id.title_filtrate_btn_gap_name_to_icon);
		btnIconImage = (ImageView) view.findViewById(R.id.title_filtrate_btn_icon);
		
		setTitleName(titleName);
	}
	
	/**
	 * 设置 TitleName
	 * @param titleName
	 * @return
	 */
	public TitleViews setTitleName(String titleName){
		titleNameTv.setText(titleName);
		return titleViews;
	}
	
	/**
	 * 设置按键事件是否显示
	 * @param visibility 只能为 View.GONE and View.VISIBLE
	 */
	public void setFrameLayout(int visibility){
		frameLayout.setVisibility(visibility);
	}
	
	/**
	 * 设置按键事件名字
	 * @param btnName
	 */
	public TitleViews setBtnName(String btnName){
		if(!TextUtils.isEmpty(btnName)){
//			isName = true;
			btnNameTv.setText(btnName);
			if(!btnNameTv.isShown()) btnNameTv.setVisibility(View.VISIBLE);
		}else{
//			isName = false;
			btnNameTv.setVisibility(View.GONE);
		}
//		nameToIconGap();
		return titleViews;
	}
	
	/**
	 * 设置按键图片 Icon
	 * @param btnIcon
	 */
	public TitleViews setBtnIcon(int btnIcon){
		if(btnIcon != View.VISIBLE){
//			isIcon = true;
			btnIconImage.setImageResource(btnIcon);
			if(!btnIconImage.isShown()) btnIconImage.setVisibility(View.VISIBLE);
		}else{
//			isIcon = false;
			btnIconImage.setVisibility(View.GONE);
		}
//		nameToIconGap();
		return titleViews;
	}
	
	/**
	 * 按键事件
	 * @param onClick
	 */
	public void setOnClickListener(OnClickListener onClick){
		frameLayout.setOnClickListener(onClick);
	}
	
//	private void nameToIconGap(){
//		if(isName && isIcon){
//			btnGap.setVisibility(View.VISIBLE);
//		}else{
//			btnGap.setVisibility(View.GONE);
//		}
//	}
	/**
	 * 返回View
	 * @return
	 */
	public View getView(){
		return view;
	}
}
