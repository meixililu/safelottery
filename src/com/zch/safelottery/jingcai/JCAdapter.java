package com.zch.safelottery.jingcai;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zch.safelottery.bean.JZMatchBean;

public abstract class JCAdapter extends BaseAdapter {
	
	public ArrayList<JZMatchBean> selectedBeans = new ArrayList<JZMatchBean>();
	public JCDingDanClickListener mJCDingDanClickListener;
	public LayoutInflater inflater; 
	
	public JCAdapter(ArrayList<JZMatchBean> selectedBeans,LayoutInflater inflater){
		this.selectedBeans = selectedBeans;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return selectedBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return selectedBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getViews(position,convertView,parent);
	}
	
	public abstract View getViews(int position, View convertView, ViewGroup parent);
	
	public interface JCDingDanClickListener{
		public void onCheck(int danCount);
	}
	
	public void setmJCDingDanClickListener(JCDingDanClickListener mJCDingDanClickListener) {
		this.mJCDingDanClickListener = mJCDingDanClickListener;
	}

}
