package com.zch.safelottery.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.ctshuzicai.ZCHBaseActivity;
import com.zch.safelottery.setttings.Settings;

public class SearchUserResultActivity extends ZCHBaseActivity {
	
	public static final boolean DEBUG = Settings.DEBUG;
    public static final String TAG = "SearchUserResultActivity";
    
    private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onCreate()");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_user_result);
		initUI();
		initData();
		
	}
	
	private void initData(){
		if (DEBUG) Log.d(Settings.TAG, TAG+"-initData()");
		title.setText("黑马王子");
	}

	private void initUI(){
		if (DEBUG) Log.d(Settings.TAG, TAG+"-initUI()");
		title = (TextView)findViewById(R.id.search_user_nickname);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();  
		if (DEBUG) Log.d(Settings.TAG, TAG+"-onDestroy()");
	}

}
