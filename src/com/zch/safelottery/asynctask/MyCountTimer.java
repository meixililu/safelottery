package com.zch.safelottery.asynctask;

import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountTimer extends CountDownTimer{
	private TextView m_s;
	private TextView m_g;
	private TextView s_s;
	private TextView s_g;
	private static final long oneDay = 24*60*60*1000;
	private static final long oneHour = 3600*1000;
	private static final long oneMinute = 60*1000;
	
	private OnCountTimer onCountTimer;
	
    /***
     * 第一次调用
     * @param context
     * @param millisInFuture
     * @param countDownInterval
     * @param m_s
     * @param m_g
     * @param s_s
     * @param s_g
     */
    public MyCountTimer(long millisInFuture, long countDownInterval,TextView m_s,TextView m_g,TextView s_s, TextView s_g){
    	super(millisInFuture, countDownInterval);
    	this.m_s = m_s;
    	this.m_g = m_g;
    	this.s_s = s_s;
    	this.s_g = s_g;
    }
    /***
     * 第二次调用因为第二调用要改变期次
     * @param context
     * @param millisInFuture
     * @param countDownInterval
     * @param tv_issue
     * @param m_s
     * @param m_g
     * @param s_s
     * @param s_g
     * @param issue
     */
    public MyCountTimer( long millisInFuture, long countDownInterval,TextView m_s,TextView m_g,TextView s_s, TextView s_g, TextView tv_issue,String issue){
    	super(millisInFuture, countDownInterval);
    	this.m_s = m_s;
    	this.m_g = m_g;
    	this.s_s = s_s;
    	this.s_g = s_g;
    	tv_issue.setText(issue);
    	
    }

    @Override    
    public void onFinish() {
		if(onCountTimer != null){
			onCountTimer.onCountTimer();
		}
    	this.cancel();
    }    

    @Override    
    public void onTick(long millisUntilFinished) {
    	m_s.setText( getMinute(millisUntilFinished,0) );
    	m_g.setText( getMinute(millisUntilFinished,1) );
    	s_s.setText( getSecond(millisUntilFinished,0) );
    	s_g.setText( getSecond(millisUntilFinished,1) );
    }    
    
    private String getMinute(long millisUntilFinished,int position){
    	long minute = (millisUntilFinished % oneDay % oneHour) / oneMinute;
    	if(position == 0){
    		return minute/10+"";
    	}else{
    		return minute%10+"";
    	}
    }
    
    private String getSecond(long millisUntilFinished,int position){
    	long second = (millisUntilFinished % oneDay % oneHour % oneMinute) / 1000;
    	if(position == 0){
    		return second/10+"";
    	}else{
    		return second%10+"";
    	}
    }
    
    public void setOnCountTimer(OnCountTimer onCountTimer){
    	this.onCountTimer = onCountTimer;
    }
    
    public interface OnCountTimer{
    	public void onCountTimer();
    }
}
