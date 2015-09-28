package com.zch.safelottery.jingcai;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.ctshuzicai.D3Activity;
import com.zch.safelottery.dialogs.NormalAlertDialog;
import com.zch.safelottery.dialogs.NormalAlertDialog.OnButtonOnClickListener;
import com.zch.safelottery.lazyloadimage.ImageLoader;
import com.zch.safelottery.util.AlarmUtil;
import com.zch.safelottery.util.ImageUtil;
import com.zch.safelottery.util.LogUtil;
import com.zch.safelottery.util.StringUtil;
import com.zch.safelottery.util.TimeUtils;
import com.zch.safelottery.util.ToastUtil;

public class WorldcupAlarmListItemCLass extends JCAbstractClass {
	
	private Context mActivity;
	private LayoutInflater inflater;

	public WorldcupAlarmListItemCLass(Context mActivity, LayoutInflater inflater) {
		this.mActivity = mActivity;
		this.inflater = inflater;
	}

	@Override
	public void showItems(ImageLoader imageLoader, final JZMatchBean bean,LinearLayout parent_layout) {
		View convertView = inflater.inflate(R.layout.worldcup_listitem_class, null);
		parent_layout.addView(convertView);
		
		FrameLayout cover_layout = (FrameLayout) convertView.findViewById(R.id.cover_layout);
		TextView hometeam_name = (TextView) convertView.findViewById(R.id.tv_hometeam_name);
		TextView guestteam_name = (TextView) convertView.findViewById(R.id.tv_guestteam_name);
		TextView end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
		ImageView hometeam_icon = (ImageView) convertView.findViewById(R.id.iv_hometeam_icon);
		ImageView guestteam_icon = (ImageView) convertView.findViewById(R.id.iv_guestteam_icon);
		final CheckBox cb_custom = (CheckBox) convertView.findViewById(R.id.cb_custom);
		
		hometeam_name.setText( bean.getMainTeam() ); 
		guestteam_name.setText( bean.getGuestTeam() ); 
		end_time.setText( "投注截止："+TimeUtils.customFormatDate(bean.getEndFuShiTime(), TimeUtils.DateFormat, TimeUtils.DateMinuteFormat));
		
		String mainTeamId = StringUtil.getSnByCountryName(bean.getMainTeam());
		Bitmap mBitmapMain = ImageUtil.getImageFromAssetsFile(mActivity, "jz_cgj_nation/" + mainTeamId + ".jpg");
		String guestTeamId = StringUtil.getSnByCountryName(bean.getGuestTeam());
		Bitmap mBitmapGuest = ImageUtil.getImageFromAssetsFile(mActivity, "jz_cgj_nation/" + guestTeamId + ".jpg");
		
		hometeam_icon.setImageBitmap(mBitmapMain);
		guestteam_icon.setImageBitmap(mBitmapGuest);
		cb_custom.setChecked( bean.isHasDan() );
		
		cover_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cb_custom.isChecked()){
					cb_custom.setChecked(!cb_custom.isChecked());
					bean.setHasDan(cb_custom.isChecked());
					String action = AlarmUtil.action_start_worldcup_alarm + bean.getIssue() + bean.getSn();
					AlarmUtil.cancelAlarm(mActivity, action);
//					ToastUtil.diaplayMesShort(mActivity, "已取消");
					StatService.onEvent(mActivity, "worldcup_alarm_off", "世界杯闹铃关", 1);
				}else {
					long triggerAtTime = getTimeInMillis(bean.getEndFuShiTime());
					if(triggerAtTime > System.currentTimeMillis()){
						cb_custom.setChecked(!cb_custom.isChecked());
						bean.setHasDan(cb_custom.isChecked());
						String id = bean.getIssue() + bean.getSn();
						String vs = bean.getMainTeam() + " vs " + bean.getGuestTeam();
						AlarmUtil.setAlarm(mActivity, triggerAtTime, id, vs);
//						ToastUtil.diaplayMesShort(mActivity, "添加成功");
					}else{
						cb_custom.setChecked(false);
						bean.setHasDan(false);
						showDialog();
					}
					StatService.onEvent(mActivity, "worldcup_alarm_on", "世界杯闹铃开", 1);
				}
				
			}
		});
	}
	
	private void showDialog(){
		NormalAlertDialog dialog = new NormalAlertDialog(mActivity);
		dialog.setTitle("友情提示");
		dialog.setContent("距离这场比赛截止投注时间已经不足30分钟了，快去投注吧");
		dialog.setOk_btn_text("去下注");
		dialog.setCancle_btn_text("再看看");
		dialog.setButtonOnClickListener(new OnButtonOnClickListener() {
			@Override
			public void onOkBtnClick() {
				Intent intent3 = new Intent(mActivity, JZActivity.class);
				mActivity.startActivity(intent3);
			}
			
			@Override
			public void onCancleBtnClick() {
			}
		});
		dialog.show();
	}
	
	/**获取比赛时间毫秒值，减去三十分钟
	 * @param time
	 * @return
	 */
	public static long getTimeInMillis(String time){
		long triggerAtTime = 0;
		Calendar mCalendar = Calendar.getInstance();
        Date mData = TimeUtils.getStringTime(time);
        if(mData != null){
        	mCalendar.setTime(mData);
        	triggerAtTime = mCalendar.getTimeInMillis();
        	triggerAtTime -= (1000 * 60 * 30);
        }
        return triggerAtTime;
	}
	
	@Override
	public void showItems(final JZMatchBean bean, LinearLayout parent_layout) {
		
	}
}
