package com.zch.safelottery.dialogs;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.bean.JZMatchBean;
import com.zch.safelottery.util.JCBFUtils;
import com.zch.safelottery.util.LotteryId;

/**
 * 自定义退出dialog
 * 
 * @author Messi
 * 
 */
public class JCGradeShowDialog extends Dialog {

	private View contentView;

	private Dialog dialog;
	private LinearLayout contentLayout;
	private String title;
	private Context context;
	private String ok_btn_text, cancle_btn_text;
	private int type;// 默认两个按钮，1为一个按钮
	private BFAdapter bfAdapter;
	private ArrayList<String> selectedPvList;
	private ArrayList<String> selectedList;
	private ArrayList<Itembean> bList;

	private String lid;
	private String wanfa;
	private JZMatchBean bean;
	private LayoutInflater inflater;
	private OnShowOnClickListener buttonOnClickListener;
	private Drawable a, b, c, d;
	private String numstr;
	private int heiht;
	private GridView grid;
	private String oldnum;

	public JCGradeShowDialog(Context context) {
		super(context);
		this.context = context;
	}

	public JCGradeShowDialog(Context context, String lid, String wanfa, JZMatchBean bean) {
		super(context);
		this.context = context;
		this.lid = lid;
		this.wanfa = wanfa;
		this.bean = bean;
	}

	public void show() {
		oldnum=bean.getNumstr();
		selectedList = bean.getSelectedList();
		selectedPvList = bean.getSelectedPvList();
		if (selectedList.size() == 0) {
			selectedList = new ArrayList<String>();
		}
		bList = new ArrayList<JCGradeShowDialog.Itembean>();
		String fenshuSP[] = bean.getSp().split("#");
		for (int i = 0; i < fenshuSP.length; i++) {
			Itembean itembean = new Itembean();
			itembean.setSp(fenshuSP[i]);
			itembean.setIsselect(0);
			itembean.setNum(i);
			bList.add(itembean);
		}
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.bf_choice_dialog, null);
		dialog = new Dialog(context, R.style.dialog);

		Button ok_btn = (Button) view.findViewById(R.id.money_not_allowed_btn_sure);
		Button cancle_btn = (Button) view.findViewById(R.id.money_not_allowed_btn_cancel);
		TextView title = (TextView) view.findViewById(R.id.money_not_allowed_title);
		grid = (GridView) view.findViewById(R.id.bf_choice_grid);
		if (lid.equals(LotteryId.JCLQ)) {
			grid.setNumColumns(3);
			grid.setColumnWidth(100);
		}
		if (heiht > 0) {
			LayoutParams lParams = grid.getLayoutParams();
			lParams.height = heiht;
		}
		bfAdapter = new BFAdapter(bList);
		grid.setAdapter(bfAdapter);
		title.setText(bean.getMainTeam() + " vs " + bean.getGuestTeam());

		if (type == 1) {
			cancle_btn.setVisibility(View.GONE);
		}
		if (contentView != null) {
			contentLayout.addView(contentView);
		}

		if (!TextUtils.isEmpty(ok_btn_text)) {
			ok_btn.setText(ok_btn_text);
		}

		if (!TextUtils.isEmpty(cancle_btn_text)) {
			cancle_btn.setText(cancle_btn_text);
		}

		ok_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (buttonOnClickListener != null) {
					selectedPvList.clear();
					selectedList.clear();
					StringBuffer numbuffer = new StringBuffer();
					for (int i = 0; i < bList.size(); i++) {
						if (bList.get(i).getIsselect() == 1) {
							selectedPvList.add(bList.get(i).getSp());
							if (lid.equals(LotteryId.JCZQ)) {
								if (wanfa.equals("04")) {
									numbuffer.append(JCBFUtils.getJZQCBFName(bList.get(i).getNum()) + ",");
									selectedList.add(JCBFUtils.getJZQCBFCode(i));
								}else if (wanfa.equals("03")) {
									numbuffer.append(JCBFUtils.getJZBQCName(bList.get(i).getNum()) + ",");
									selectedList.add(JCBFUtils.getJZBQCCode(i));
								}else if (wanfa.equals("02")) {
									numbuffer.append(JCBFUtils.getJZZJQSName(bList.get(i).getNum()) + ",");
									selectedList.add(i + "");
								}
							}else if (lid.equals(LotteryId.JCLQ)) {
								numbuffer.append(JCBFUtils.getJLSFCName(bList.get(i).getNum()) + ",");
								selectedList.add(JCBFUtils.getJLSFCCode(i));
							}
						}
					}
					bean.setSelectedPvList(selectedPvList);
					bean.setSelectedList(selectedList);
					String numbufferstr = numbuffer.toString();
					if (numbufferstr.length() > 0) {
						numstr = numbufferstr.substring(0, numbuffer.length() - 1);
					} else {
						numstr = "";
					}
					bean.setNumstr(numstr);
					buttonOnClickListener.onOkBtnClick();
				}
			}
		});

		cancle_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (buttonOnClickListener != null) {
					buttonOnClickListener.onCancleBtnClick();
					bean.setNumstr(oldnum);
				}
			}
		});
		dialog.setCancelable(false);
		dialog.setContentView(view);
		dialog.show();
	}

	public interface OnShowOnClickListener {
		public void onOkBtnClick();

		public void onCancleBtnClick();
	}

	public void setOnShowClickListener(OnShowOnClickListener buttonOnClickListener) {
		this.buttonOnClickListener = buttonOnClickListener;
	}

	public void setOk_btn_text(String ok_btn_text) {
		this.ok_btn_text = ok_btn_text;
	}

	public void setCancle_btn_text(String cancle_btn_text) {
		this.cancle_btn_text = cancle_btn_text;
	}

	public int getHeiht() {
		return heiht;
	}

	public void setHeiht(int heiht) {
		this.heiht = heiht;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addView(View content) {
		this.contentView = content;
	}

	public void setType(int type) {
		this.type = type;
	}

	class BFAdapter extends BaseAdapter {

		private ArrayList<Itembean> bList;

		private BFAdapter(ArrayList<Itembean> bList) {
			this.bList = bList;
		}

		public int getCount() {

			return bList.size();
		}

		public Object getItem(int position) {

			return bList.get(position);
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			final Holder holder;

			if (convertView == null) {

				holder = new Holder();
				convertView = inflater.inflate(R.layout.bf_item, null);
				holder.bfTx = (TextView) convertView.findViewById(R.id.bf_text);
				holder.bfsp = (TextView) convertView.findViewById(R.id.sp_text);

				convertView.setTag(holder);

			} else {
				holder = (Holder) convertView.getTag();
			}

			if (lid.equals(LotteryId.JCZQ)) {
				if (wanfa.equals("04")) {
					holder.bfTx.setText(JCBFUtils.getJZQCBFName(position));
					if (!bean.getNumstr().equals("")) {
						String numstring[] = bean.getNumstr().split(",");
						for (int i = 0; i < numstring.length; i++) {
							if (numstring[i].equals(JCBFUtils.getJZQCBFName(position))) {
								bList.get(position).setIsselect(1);
							}
						}
					}
					if (position > 12 && position < 18) {
						selectpingfen(convertView, holder);
					} else if (position >= 18) {
						selectlosefen(convertView, holder);
					} else {
						selectwinfen(convertView, holder);
					}

					if (position == 12 || position == 17 || position == 30) {
						holder.bfTx.setTextSize(15);
					} else {
						holder.bfTx.setTextSize(20);
					}

				} else if (wanfa.equals("03")) {
					holder.bfTx.setText(JCBFUtils.getJZBQCName(position));
					if (!bean.getNumstr().equals("")) {
						String numstring[] = bean.getNumstr().split(",");
						for (int i = 0; i < numstring.length; i++) {
							if (numstring[i].equals(JCBFUtils.getJZBQCName(position))) {
								bList.get(position).setIsselect(1);
							}
						}
					}
					holder.bfTx.setTextSize(18);
					selectwinfen(convertView, holder);
				}else if (wanfa.equals("02")) {
					holder.bfTx.setText(JCBFUtils.getJZZJQSName(position));
					if (!bean.getNumstr().equals("")) {
						String numstring[] = bean.getNumstr().split(",");
						for (int i = 0; i < numstring.length; i++) {
							if (numstring[i].equals(JCBFUtils.getJZZJQSName(position))) {
								bList.get(position).setIsselect(1);
							}
						}
					}
					selectwinfen(convertView, holder);
				}
			} else if (lid.equals(LotteryId.JCLQ)) {
				holder.bfTx.setText(JCBFUtils.getJLSFCName(position));
				holder.bfTx.setTextSize(15);
				if (!bean.getNumstr().equals("")) {
					String numstring[] = bean.getNumstr().split(",");
					for (int i = 0; i < numstring.length; i++) {
						if (numstring[i].equals(JCBFUtils.getJLSFCName(position))) {
							bList.get(position).setIsselect(1);
						}
					}
				}
				if (position < 6) {
					selectwinfen(convertView, holder);
				} else {
					selectlosefen(convertView, holder);
				}
			}
			holder.bfsp.setText(bList.get(position).getSp());

			// if (selectedPvList.size() > 0) {
			// for (int i = 0, j = selectedPvList.size(); i < j; i++) {
			// if (selectedPvList.get(i).equals(bList.get(position).getSp())) {
			// bList.get(position).setIsselect(1);
			// break;
			// }
			// }
			// }
			if (bList.get(position).getIsselect() == 1) {
				xuanzhong(convertView, holder);
			}

			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					bean.setNumstr("");
					if (lid.equals(LotteryId.JCZQ)) {
						if (wanfa.equals("04")) {
							if (bList.get(position).getIsselect() == 1) {
								if (position > 12 && position < 18) {
									selectpingfen(v, holder);
									bList.get(position).setIsselect(0);
								} else if (position >= 18) {
									selectlosefen(v, holder);
									bList.get(position).setIsselect(0);
								} else {
									selectwinfen(v, holder);
									bList.get(position).setIsselect(0);
								}
							} else {
								xuanzhong(v, holder);
								bList.get(position).setIsselect(1);
							}
						}else if (wanfa.equals("03")) {
							if (bList.get(position).getIsselect() == 1) {
									selectwinfen(v, holder);
									bList.get(position).setIsselect(0);
							} else {
								xuanzhong(v, holder);
								bList.get(position).setIsselect(1);
							}
						} else if (wanfa.equals("02")) {
							if (bList.get(position).getIsselect() == 1) {
								selectwinfen(v, holder);
								bList.get(position).setIsselect(0);
							} else {
								xuanzhong(v, holder);
								bList.get(position).setIsselect(1);

							}
						}
					} else if (lid.equals(LotteryId.JCLQ)) {
						if (bList.get(position).getIsselect() == 1) {
							if (position < 6) {
								selectwinfen(v, holder);
								bList.get(position).setIsselect(0);
							} else {
								selectlosefen(v, holder);
								bList.get(position).setIsselect(0);
							}
						} else {
							xuanzhong(v, holder);
							bList.get(position).setIsselect(1);
						}
					}

					bfAdapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}

		public void selectpingfen(View convertView, Holder holder) {
			convertView.setBackgroundResource(R.drawable.pingfen);
			holder.bfTx.setTextColor(context.getResources().getColor(R.color.text_dark_blue));
		}

		public void selectlosefen(View convertView, Holder holder) {
			convertView.setBackgroundResource(R.drawable.losefen);
			holder.bfTx.setTextColor(context.getResources().getColor(R.color.text_dark_green));
		}

		public void selectwinfen(View convertView, Holder holder) {
			convertView.setBackgroundResource(R.drawable.winfen);
			holder.bfTx.setTextColor(context.getResources().getColor(R.color.text_dark_yellow));
		}

		public void xuanzhong(View convertView, Holder holder) {
			convertView.setBackgroundResource(R.drawable.xuanzhong);
			holder.bfTx.setTextColor(context.getResources().getColor(R.color.white));
		}

	}

	static class Holder {

		LinearLayout layout;
		TextView bfTx;
		TextView bfsp;
	}

	private class Itembean {
		public String sp;
		public int isselect = 0;
		public int num = 0;

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public int getIsselect() {
			return isselect;
		}

		public void setIsselect(int isselect) {
			this.isselect = isselect;
		}

		public String getSp() {
			return sp;
		}

		public void setSp(String sp) {
			this.sp = sp;
		}

	}
}
