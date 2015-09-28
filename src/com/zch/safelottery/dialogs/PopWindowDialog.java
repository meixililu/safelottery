package com.zch.safelottery.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.zch.safelottery.R;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ScreenUtil;
import com.zch.safelottery.view.ListLineFeedView;

public class PopWindowDialog extends AlertDialog {
	
	private final String TAG = "TAG";
	private final boolean DEBUG = Settings.DEBUG;
	private OnclickFrequentListener onClick;
	private Context mContext;
	private String[] mItem;
	private int size;
	private ArrayList<CheckBox> list;
	
	private LinearLayout layoutView;
//	private MyAdapter adapter;
	
	private LayoutInflater mInflater;
	
	private int mWidth;
	private int mHeight;
	private int row; //行
	private int col = 4; //列
	private int init = -1;
	
	/**
	 * @param context 传入当前的Activity
	 */
	public PopWindowDialog(Context context) {
		super(context, R.style.popDialog);
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}
	
	/**
	 * @param context
	 * @param item 内容
	 * @param init 初始选中
	 */
	public PopWindowDialog(Context context, String[] item) {
		this(context);
		this.mContext = context;
		this.mItem = item;
	}
	/**
	 * @param context
	 * @param item 内容
	 * @param init 初始选中
	 */
	public PopWindowDialog(Context context, String[] item, int init) {
		this(context);
		this.mContext = context;
		this.mItem = item;
		this.init = init;
	}
	
	/**
	 * @param context
	 * @param item 内容
	 * @param init 初始选中
	 */
	public PopWindowDialog(Context context, List<String> item, int init) {
		this(context);
		this.mContext = context;
		this.mItem = (String[]) item.toArray(new String[item.size()]);;
		this.init = init;
	}
	
	/**
	 * @param context
	 * @param item 内容
	 * @param init 初始选中
	 * @param col 列
	 */
	public PopWindowDialog(Context context, String[] item, int init, int col) {
		this(context, item, init);
		this.col = col;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.select_popwindow_frequent);
	    size = mItem.length;
	    list =new ArrayList<CheckBox>(size);
	    
	    layoutView = (LinearLayout) findViewById(R.id.select_popwindow_list);
	    
//	    this.setCanceledOnTouchOutside(true);
	    mWidth = LayoutParams.FILL_PARENT;
		mHeight = LayoutParams.WRAP_CONTENT;
		int yushu = size % col;
	    for(int i = 0; i < size; i++){
	    	addView(i);
	    }
	    if(yushu > 0){
	    	addNullView(col - yushu);
	    }
	    ListLineFeedView listView = new ListLineFeedView(mContext, list, col);
	    layoutView.addView(listView.getView());
	}
	
	private View addView(final int index){
		View view = mInflater.inflate(R.layout.select_popwindow_frequent_item, null);
		final CheckBox itemView = (CheckBox) view.findViewById(R.id.select_popwindow_text);
		itemView.setText(mItem[index]);
		itemView.setId(index);
		itemView.setGravity(Gravity.CENTER);
		itemView.setLayoutParams(new LayoutParams(0, ScreenUtil.dip2px(mContext,36f),1.0f));//第三个参数就是weight 比重
		if(index == init)  itemView.setChecked(true); //初始化为选中
		
		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onClick != null) onClick.onClick(v);
			}
		});
		
		list.add(itemView);
		return view;
	}
	
	private void addNullView(float weight){
		View view = mInflater.inflate(R.layout.select_popwindow_frequent_item, null);
		CheckBox itemView = (CheckBox) view.findViewById(R.id.select_popwindow_text);
		itemView.setLayoutParams(new LayoutParams(0, ScreenUtil.dip2px(mContext,36f),weight));//第三个参数就是weight 比重
		itemView.setText(" ");
		itemView.setEnabled(false);
		list.add(itemView);
	}
	
	public void setOnClickListener(OnclickFrequentListener onClick) {
		this.onClick = onClick;
	}

	public void setPopViewPosition(){
		setPopViewPosition(0, ScreenUtil.dip2px(mContext, 40));
	}
	public void setPopViewPosition(int x, int y){
		Window win = this.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP|Gravity.LEFT;
		params.x = x + ScreenUtil.dip2px(mContext,6);
		params.y = y + ScreenUtil.dip2px(mContext,6);
		win.setAttributes(params);
		if(DEBUG) Log.i("TAG", "点击 setPopViewPosition" + params.width);
		this.setCanceledOnTouchOutside(true);
	}
	
	public interface OnclickFrequentListener{
		public void onClick(View v);
	}
	
	public void setWindowSize(int width, int height){
		this.getWindow().setLayout(width, height);
	}
	public void setStrItem(String[] strItem) {
		this.mItem = strItem;
	}
	public void setStrItem(ArrayList<String> strItem) {
		this.mItem = (String[]) strItem.toArray();
	}
	
	public ArrayList<CheckBox> getList(){
		return list;
	}
	
	public void show(){
		super.show();
		setWindowSize(mWidth, mHeight);
	}
	//调用的写法
//	popDialogf = new PopDialogFrequent(P5Activity.this, new String[]{"哈哈","哈哈","哈哈","哈哈","哈哈","哈哈","哈哈","哈哈","哈哈","哈哈"}, R.style.popDialog);
}
