package com.zch.safelottery.combine;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.setttings.SystemInfo;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.LotteryId;

public class CombineLottery{

	/** 向上 **/
	private final int INT_STATE_RADIO_UP = 0X001;
	/** 向下 **/
	private final int INT_STATE_RADIO_DOWN = 0X002;

	/** VIEW_PAGER的长度 **/
	private final int viewPagerSum = 3;
	
	private Activity mContext;
	private LayoutInflater inflater;
	
	private ViewPager viewPager;
	private ImageView cursor;// 动画图片
	private List<CombineViewPageItem> listViewPagers;
	
	private String sort;// 排序  1 进度降序  2 进度升序  3 总金额降序 4 总金额升序
	
	private String lid;// 彩种
	private String playId;// 玩法
	
	private View combineView;
	private boolean isSortAdd;
	
	/***排序***/
	private RadioButton[] radioButtons; //排序的三个按钮
	private int[] intRadio;
	
	private int sortSate; //排序状态
	
	private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
	
	public CombineLottery(Activity context, String lid){
		this.mContext = context;
		this.lid = lid;
		
		inflater = LayoutInflater.from(context);
		
		radioButtons = new RadioButton[3];
		intRadio = new int[2];
		intRadio[0] = INT_STATE_RADIO_UP;
		
		initView();
		initData();
	}

	/**
	 * 设置彩种
	 * @param lid 彩种
	 */
	public void setLid(String lid){
		// 当选择不同彩种时才重新加载内容
		if(lid != null ){
			this.lid = lid;
		}else{
			this.lid = LotteryId.All;
			LogUtil.CustomLog("TAG", "传入的彩种为null");
		}
		for(CombineViewPageItem item: listViewPagers){
			item.setLid(lid);
		}
	}
	
	private void initView() {
		combineView = inflater.inflate(R.layout.combine_lottery, null);
		
		radioButtons[0] = (RadioButton) combineView.findViewById(R.id.combin_lottery_radio_progress);
		radioButtons[1] = (RadioButton) combineView.findViewById(R.id.combin_lottery_radio_amount);
		radioButtons[2] = (RadioButton) combineView.findViewById(R.id.combin_lottery_radio_search);
		
		viewPager = (ViewPager) combineView.findViewById(R.id.combine_lottery_view_page);
		cursor = (ImageView) combineView.findViewById(R.id.cursor);
		 
		radioButtons[0].setOnClickListener(onClickListener);
		radioButtons[1].setOnClickListener(onClickListener);
		radioButtons[2].setOnClickListener(onClickListener);
		InitImageView();
	}
	
	/**
     * 初始化动画
*/
    private void InitImageView() {
    	// 获取图片宽度
    	bmpW = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.combine_viewpager_cour).getWidth();
        offset = (SystemInfo.width / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }
	
	private void initData() {
		listViewPagers = new ArrayList<CombineViewPageItem>(viewPagerSum);
		ArrayList<View> lists = new ArrayList<View>(viewPagerSum);
		for(int i = 0; i < viewPagerSum; i++){
			CombineViewPageItem item = new CombineViewPageItem(mContext, lid);
			listViewPagers.add(item);
				if(i == 0){
					item.setSort("1");
					item.refresh();
				}else if(i == 1){
					item.setSort("3");
					item.refresh();
				}else if(i == 2){
					item.setSort("");
					item.setSearchOpen(true);
				}
			lists.add(item.getCombineView());
		}
		
		viewPager.setAdapter(new MyPagerAdapter(lists));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setCurrentItem(0);
	}
	
	/***
	 * radio的状态，判断排序的先前条件
	 * @param index 当前RakioButton 的下标
	 * @return 当前是否切换列表
	 */
	private boolean setRadioState(int index){
		// 当两个值相等时改变事件。不相等时为第一次切换
		if(sortSate == index){
			if(intRadio[index] == INT_STATE_RADIO_DOWN){
				radioButtons[index].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.combine_sort_down, 0);
				intRadio[index] = INT_STATE_RADIO_UP;
			}else{
				radioButtons[index].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.combine_sort_up, 0);
				intRadio[index] = INT_STATE_RADIO_DOWN;
			}
			return false;
		}else{
			return true;
		}
	}

	public View getCombineView(){
		LinearLayout.LayoutParams lParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		combineView.setLayoutParams(lParams);
		
		return combineView;
	}
	
	private void cutPage(int arg0){
		try {
			radioButtons[arg0].setChecked(true);
			switch(arg0){
			case 0:
				if(intRadio[0] == INT_STATE_RADIO_DOWN){
					sort = "2";//认购进度 升序
				}else{
					sort = "1";//认购进度 降序 
				}
				radioButtons[2].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search, 0);
				StatService.onEvent(mContext, "join-by processing", "合买大厅-进度排序", 1);
				//发送请求　接收到的数据放到resultList 里
//			addSortList(isSortAdd);
				break;
			case 1:
				if(intRadio[1] == INT_STATE_RADIO_DOWN){
					sort = "4";//方案总金额 升序
				}else{
					sort = "3";//方案总金额 降序
				}
				radioButtons[2].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search, 0);
				StatService.onEvent(mContext, "join-by amount", "合买大厅-总额排序", 1);
				//发送请求　接收到的数据放到resultList 里
//			addSortList(isSortAdd);
				break;
			case 2:
				StatService.onEvent(mContext, "join-search-button", "合买-搜索按钮", 1);
				
				sort = "";
				if(sortSate != 2){
					sortSate = 2;
				}
				radioButtons[2].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_red, 0);
				break;
			}
			sortSate = arg0;
			listViewPagers.get(arg0).setSort(sort);
			if(!isSortAdd){
				listViewPagers.get(arg0).refresh();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.combin_lottery_radio_progress){
				isSortAdd = setRadioState(0);//这个一定要放在前面
				if (isSortAdd) {
					viewPager.setCurrentItem(0);
				}else{
					cutPage(0);
				}
			}else if(v.getId() == R.id.combin_lottery_radio_amount){
				isSortAdd = setRadioState(1);//这个一定要放在前面
				if(isSortAdd){
					viewPager.setCurrentItem(1);
				}else{
					cutPage(1);
				}
			}else if(v.getId() == R.id.combin_lottery_radio_search){ //查找
				viewPager.setCurrentItem(2);
				cutPage(2);
			}
		}
	};
	
	public class MyPagerAdapter extends PagerAdapter{

		List<View> listViews;
		public MyPagerAdapter(List<View> listViews){
			this.listViews = listViews;
		}
		@Override
		public int getCount() {
			return listViews.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(listViews.get(position));
		}
		
		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			return view == (arg1);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(listViews.get(position), 0);
			return listViews.get(position);
		}
		
	}
	
	private class MyOnPageChangeListener implements OnPageChangeListener{
		
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int arg0) {
			isSortAdd = true;
			cutPage(arg0);
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(100);
			cursor.startAnimation(animation);
		}
	}
	private CombineViewPageItem combineViewPageItem;
}
