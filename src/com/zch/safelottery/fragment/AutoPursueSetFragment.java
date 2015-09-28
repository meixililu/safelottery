package com.zch.safelottery.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.AutoPursueActivity;
import com.zch.safelottery.impl.AutoPursueListener;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.ConversionUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.ToastUtil;

public class AutoPursueSetFragment extends Fragment {
	
	private Button issue_minus_btn,issue_plus_btn,mutiple_minus_btn,mutiple_plus_btn,setting_submit;
	private EditText pursue_issue,pursue_mutiple,pursue_yll_input,pursue_yle_input;
	private CheckBox pursue_yll,pursue_yle,default_setting;
	private SharedPreferences mSharedPreferences;
	private int maxIssueSize,defaultIssueSize,multiple;
	private AutoPursueListener mAutoPursueListener;
	private static AutoPursueSetFragment instance;
	
	public static AutoPursueSetFragment getInstance(int mIissueSize, int dIssueSize, int defalutMutiple, AutoPursueListener listener){
		if(instance == null){
			instance = new AutoPursueSetFragment(mIissueSize,dIssueSize,defalutMutiple);
			instance.setmAutoPursueListener(listener);
		}
		return instance;
	}
	
	public static void clear(){
		instance = null;
	}
	
	public AutoPursueSetFragment(int mIissueSize, int dIssueSize, int defalutMutiple){
		this.maxIssueSize = mIissueSize;
		this.defaultIssueSize = dIssueSize;
		this.multiple = defalutMutiple;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogUtil.DefalutLog(this.getClass().getName()+"-onCreate");
		super.onCreate(savedInstanceState);
		mSharedPreferences = Settings.getSharedPreferences(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(this.getClass().getName()+"-onCreateView");
		View view = inflater.inflate(R.layout.auto_pursue_setting_fragment, container, false);
		initview(view);
		return view;
	}
	
	private void initview(View view){
		issue_minus_btn = (Button)view.findViewById(R.id.auto_pursue_issue_minus_btn);
		issue_plus_btn = (Button)view.findViewById(R.id.auto_pursue_issue_plus_btn);
		mutiple_minus_btn = (Button)view.findViewById(R.id.auto_pursue_mutiple_minus_btn);
		mutiple_plus_btn = (Button)view.findViewById(R.id.auto_pursue_mutiple_plus_btn);
		
		pursue_issue = (EditText)view.findViewById(R.id.auto_pursue_issue);
		pursue_mutiple = (EditText)view.findViewById(R.id.auto_pursue_mutiple);
		pursue_yll_input = (EditText)view.findViewById(R.id.auto_pursue_yll_input);
		pursue_yle_input = (EditText)view.findViewById(R.id.auto_pursue_yle_input);
		
		pursue_yll = (CheckBox)view.findViewById(R.id.auto_pursue_yll);
		pursue_yle = (CheckBox)view.findViewById(R.id.auto_pursue_yle);
		default_setting = (CheckBox)view.findViewById(R.id.auto_pursue_default_setting);
		
		setting_submit = (Button)view.findViewById(R.id.auto_pursue_setting_submit);
		
		pursue_yll.setOnClickListener(mClickListener);
		pursue_yle.setOnClickListener(mClickListener);
		issue_plus_btn.setOnClickListener(mClickListener);
		issue_minus_btn.setOnClickListener(mClickListener);
		mutiple_minus_btn.setOnClickListener(mClickListener);
		mutiple_plus_btn.setOnClickListener(mClickListener);
		setting_submit.setOnClickListener(mClickListener);
		
		if(maxIssueSize < defaultIssueSize){
			defaultIssueSize = maxIssueSize;
		}
		pursue_issue.setText(defaultIssueSize+"");
		pursue_mutiple.setText(multiple+"");
		pursue_yll_input.setText(mSharedPreferences.getInt("yllNum", 10)+"");
		pursue_yle_input.setText(mSharedPreferences.getInt("yleNum", 10)+"");
		String ylKey = mSharedPreferences.getString("ylKey", "");
		if(ylKey.equals("yllNum")){
			pursue_yll.setChecked(true);
			pursue_yle.setChecked(false);
		}else if(ylKey.equals("yleNum")){
			pursue_yll.setChecked(false);
			pursue_yle.setChecked(true);
		}
	}
	
	private OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.auto_pursue_yll){
				setCheckBoxState(pursue_yll,pursue_yle);
			}else if(v.getId() == R.id.auto_pursue_yle){
				setCheckBoxState(pursue_yle,pursue_yll);
			}else if(v.getId() == R.id.auto_pursue_issue_plus_btn){
				plusOrminus("plus","issue",pursue_issue);
			}else if(v.getId() == R.id.auto_pursue_issue_minus_btn){
				plusOrminus("minus","issue",pursue_issue);
			}else if(v.getId() == R.id.auto_pursue_mutiple_plus_btn){
				plusOrminus("plus","mutiple",pursue_mutiple);
			}else if(v.getId() == R.id.auto_pursue_mutiple_minus_btn){
				plusOrminus("minus","mutiple",pursue_mutiple);
			}else if(v.getId() == R.id.auto_pursue_setting_submit){
				submit();
			}
		}
	};
	
	private void submit(){
		int issueNum = ConversionUtil.StringToInt(pursue_issue.getText().toString());
		if(issueNum > 0 && issueNum <= 99){
			int mutiple = ConversionUtil.StringToInt(pursue_mutiple.getText().toString());
			if(mutiple > 0 && mutiple <= 999){
				if(pursue_yll.isChecked()){
					int yllNum = ConversionUtil.StringToInt(pursue_yll_input.getText().toString());
					if(yllNum > 0){
						int yleNum = ConversionUtil.StringToInt(pursue_yle_input.getText().toString());
						if(mAutoPursueListener != null){
							mAutoPursueListener.createScheme();
						}
						saveSetting(issueNum,mutiple,yllNum,yleNum);
					}else{
						ToastUtil.diaplayMesShort(getActivity(), "盈利率必须大于0");
					}
				}else{
					int yleNum = ConversionUtil.StringToInt(pursue_yle_input.getText().toString());
					if(yleNum > 0){
						int yllNum = ConversionUtil.StringToInt(pursue_yll_input.getText().toString());
						if(mAutoPursueListener != null){
							mAutoPursueListener.createScheme();
						}
						saveSetting(issueNum,mutiple,yllNum,yleNum);
					}else{
						ToastUtil.diaplayMesShort(getActivity(), "盈利额必须大于0");
					}
				}
			}else{
				ToastUtil.diaplayMesShort(getActivity(), "倍投最小为1倍");
			}
		}else{
			ToastUtil.diaplayMesShort(getActivity(), "至少追一期");
		}
	}
	
	private void saveSetting(int DefaultIssueNum,int DefaultMultipleNum,int yllNum,int yleNum){
		Settings.saveSharedPreferences(mSharedPreferences, "DefaultIssueNum", DefaultIssueNum);
		Settings.saveSharedPreferences(mSharedPreferences, "DefaultMultipleNum", DefaultMultipleNum);
		Settings.saveSharedPreferences(mSharedPreferences, "yllNum", yllNum);
		Settings.saveSharedPreferences(mSharedPreferences, "yleNum", yleNum);
		Settings.saveSharedPreferences(mSharedPreferences, AutoPursueActivity.isHasDefaultSettingKey, default_setting.isChecked());
		if(pursue_yll.isChecked()){
			Settings.saveSharedPreferences(mSharedPreferences, "ylKey", "yllNum");
		}else{
			Settings.saveSharedPreferences(mSharedPreferences, "ylKey", "yleNum");
		}
	}
	
	private void plusOrminus(String action, String type, EditText et){
		String tempContent = et.getText().toString();
		int num = ConversionUtil.StringToInt(tempContent);
		if(action.equals("plus")){
			if(type.equals("issue")){
				if(num < maxIssueSize){
					et.setText( (++num) + "" );
				}else{
					ToastUtil.diaplayMesShort(getActivity(), "当前最多可追期次为"+maxIssueSize+"期");
				}
			}else if(type.equals("mutiple")){
				if(num < 999){
					et.setText( (++num) + "" );
				}else{
					ToastUtil.diaplayMesShort(getActivity(), "最大倍投数为"+999+"倍");
				}
			}
		}else if(action.equals("minus")){
			if(type.equals("issue")){
				if(num > 1){
					et.setText( (--num) + "" );
				}else{
					ToastUtil.diaplayMesShort(getActivity(), "最小可追期次为"+1+"期");
				}
			}else if(type.equals("mutiple")){
				if(num > 1){
					et.setText( (--num) + "" );
				}else{
					ToastUtil.diaplayMesShort(getActivity(), "最小倍投数为"+1+"倍");
				}
			}
		}
	}
	
	private void setCheckBoxState(CheckBox cb1, CheckBox cb2){
		if(cb1.isChecked()){
			cb2.setChecked(false);
		}else{
			cb2.setChecked(true);
		}
	}

	public void setmAutoPursueListener(AutoPursueListener mAutoPursueListener) {
		this.mAutoPursueListener = mAutoPursueListener;
	}

	@Override
	public void onDestroy() {
		LogUtil.DefalutLog(this.getClass().getName()+"-onDestroy");
		super.onDestroy();
	}
	
}
