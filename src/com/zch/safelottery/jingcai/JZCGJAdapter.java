package com.zch.safelottery.jingcai;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.util.ImageUtil;

/**
 * 竞足猜冠军adapter
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年6月6日
 */
public class JZCGJAdapter extends JCAdapter {

	private Context mContext;

	public JZCGJAdapter(Context context, ArrayList<JZMatchBean> selectedBeans, LayoutInflater inflater) {
		super(selectedBeans, inflater);
		mContext = context;
	}

	@Override
	public View getViews(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.jz_cgj_item, null);
			holder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
			holder.ivNationIco = (ImageView) convertView.findViewById(R.id.iv_nation);
			holder.tvNationName = (TextView) convertView.findViewById(R.id.tv_nation_name);
			holder.tvSp = (TextView) convertView.findViewById(R.id.tv_sp);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}
		final JZMatchBean bean = selectedBeans.get(position);
		
		holder.tvIndex.setText(String.valueOf(position+1));
		Bitmap ico = ImageUtil.getImageFromAssetsFile(mContext, "jz_cgj_nation/" + bean.getSn() + ".jpg");
		holder.ivNationIco.setImageBitmap(ico);
		holder.tvNationName.setText(bean.getName());
		holder.tvSp.setText(bean.getSp());
		
		holder.cb.setChecked(TextUtils.isEmpty(bean.getUserSelect()) ? false : true);
		holder.cb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CompoundButton buttonView = (CompoundButton) v;
				if (buttonView.isChecked()) {
					JZCGJActivity.mSelectItem.add(bean);
				}else
					JZCGJActivity.mSelectItem.remove(bean);
				bean.setUserSelect(buttonView.isChecked() ? bean.getSn() : "");
				Intent intent = new Intent(JZCGJActivity.ACTION_REFRESH_CHOOSE_NUM);
				mContext.sendBroadcast(intent);
			}
		});
		return convertView;
	}


	public void setData(ArrayList<JZMatchBean> selectedBeans){
		this.selectedBeans = selectedBeans;
	}
	static class Holder {
		TextView tvIndex;
		ImageView ivNationIco;
		TextView tvNationName;
		TextView tvSp;
		CheckBox cb;
	}

}
