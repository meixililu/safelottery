package com.zch.safelottery.jingcai;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.util.ImageUtil;

/**
 * 竞足猜冠军订单adapter
 *
 * @Company 北京中彩汇网络科技有限公司
 * @author 陈振国
 * @version 1.0.0
 * @create 2014年6月7日
 */
public class JZCGJOrderAdapter extends JCAdapter {

	private Context mContext;

	public JZCGJOrderAdapter(Context context, ArrayList<JZMatchBean> selectedBeans, LayoutInflater inflater) {
		super(selectedBeans, inflater);
		mContext = context;
	}

	@Override
	public View getViews(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.jz_cgj_order_item, null);
			holder.ivNationIco = (ImageView) convertView.findViewById(R.id.iv_nation);
			holder.tvNationName = (TextView) convertView.findViewById(R.id.tv_nation_name);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}
		final JZMatchBean bean = selectedBeans.get(position);
		
		Bitmap ico = ImageUtil.getImageFromAssetsFile(mContext, "jz_cgj_nation/" + bean.getSn() + ".jpg");
		holder.ivNationIco.setImageBitmap(ico);
		holder.tvNationName.setText(bean.getName());
		
		return convertView;
	}


	public void setData(ArrayList<JZMatchBean> selectedBeans){
		this.selectedBeans = selectedBeans;
	}
	static class Holder {
		ImageView ivNationIco;
		TextView tvNationName;
	}

}
