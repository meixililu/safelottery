package com.zch.safelottery.dialogs;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.YuCeBean;
import com.zch.safelottery.util.GetString;

public class BonusYuceDialog extends Dialog{

	private Context mContext;
	private LayoutInflater inflater;
	private int mCount;
	private ArrayList<YuCeBean> mListYuce;
	
	private Dialog dialog;
	

	private View view;
	private TextView qihao;
	private TextView fanshi;
	private TextView zhushu;
	private ListView listView;
	
	private String mChuan;
	private String mBet;
	private int mMultiple;
	private String mMoney;
	
	public BonusYuceDialog(Context context, ArrayList<YuCeBean> listYuce, LayoutInflater inflater) {
		super(context);
		this.mContext = context;
		this.mListYuce = listYuce;
		this.inflater = inflater;
	}
	
	/**
	 * @param chuan 串关
	 * @param count 场次
	 * @param bet 注数
	 * @param multiple 倍数
	 */
	public void setData(String chuan, int count, double bet, int multiple){
		this.mChuan = chuan;
		this.mCount = count;
		this.mBet = GetString.df_0.format(bet);
		this.mMultiple = multiple;
		this.mMoney = GetString.df.format(bet * multiple * 2);
	}
	
	@Override
	public void show() {
		view = inflater.inflate(R.layout.yuce_dialog, null);
		qihao = (TextView) view.findViewById(R.id.buy_lottery_scu_qihao);
		fanshi = (TextView) view.findViewById(R.id.yuce_dialog_fangshi);
		zhushu = (TextView) view.findViewById(R.id.yuce_dialog_zhushu);
		listView = (ListView) view.findViewById(R.id.bdrq_list);
		
		qihao.setText("比赛场次：" + mCount + "场");
		fanshi.setText(mChuan);
		zhushu.setText(mBet + "注," + mMultiple + "倍,共" + mMoney + "元");
		YuceAdapter yAdapter = new YuceAdapter(mListYuce);
		listView.setAdapter(yAdapter);

		dialog = new Dialog(mContext, R.style.dialog);
		dialog.setContentView(view);
		dialog.show();
	}
	
	class YuceAdapter extends BaseAdapter {

		private ArrayList<YuCeBean> l;

		public YuceAdapter(ArrayList<YuCeBean> a) {
			this.l = a;
		}

		public int getCount() {

			return l.size();
		}

		public Object getItem(int position) {

			return l.get(position);
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.yuce_item, null);
				holder.bet = (TextView) convertView.findViewById(R.id.no_text);
				holder.count = (TextView) convertView.findViewById(R.id.team_text);
				holder.min = (TextView) convertView.findViewById(R.id.touzhu_text);
				holder.max = (TextView) convertView.findViewById(R.id.dan_text);

				convertView.setTag(holder);
			}

			YuCeBean y = l.get(position);
			holder = (ViewHolder) convertView.getTag();
			holder.bet.setText(y.getmCount() + "场");
			holder.count.setText(GetString.df_0.format(y.getmBet()) + "注");
			holder.min.setText(GetString.df_0.format(y.getmMin()));
			holder.max.setText(GetString.df_0.format(y.getmMax()));
			return convertView;
		}
	}
	
	class ViewHolder {

		TextView bet;
		TextView count;
		TextView min;
		TextView max;
	}
}
