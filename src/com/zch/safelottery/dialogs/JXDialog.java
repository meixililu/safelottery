package com.zch.safelottery.dialogs;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.zch.safelottery.R;
public class JXDialog extends Dialog {
	private Context context;
	private Dialog dialog;
	private GridView gridView;
	private TextView titleview;
	private ArrayList<Integer> data;
	private GridViewAdapter adapter;
	private int start;
	private int end;
	private int color;
	private LayoutInflater inflater;

	private String title;
	private OnButtonItemClickListener onbuttonItemClickListener;

	public JXDialog(Context context) {
		super(context);
		this.context = context;
		data = new ArrayList<Integer>();
	}
	public JXDialog(Context context,ArrayList<Integer> data) {
		super(context);
		this.context = context;
		this.data = new ArrayList<Integer>();
		this.data = data;
	}

	public void show() {
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_qianqu_select, null);
		if(data.size()==0){
			setData();
		}
		titleview=(TextView)view.findViewById(R.id.title_view);
		gridView = (GridView)view.findViewById(R.id.redball_select_dialog_grid);
		titleview.setText(title);
		adapter = new GridViewAdapter(data, color);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(listener);
		dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private void setData() {
		for (int i = start; i < end; i++) {
			data.add(i);
		}
	}
	

	class GridViewAdapter extends BaseAdapter {
		
		private ArrayList<Integer> list;
		private int ballcolor;
		
		public GridViewAdapter(ArrayList<Integer> numbers, int ballColor) {
			this.list = numbers;
			this.ballcolor = ballColor;
		}

		public int getCount() {

			return list.size();
		}

		public Object getItem(int position) {

			return list.get(position);
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			Holder holder;
			if (convertView == null) {

				holder = new Holder();
				convertView = inflater.inflate(R.layout.gridview_redball_select_item, null);
				holder.tx = (TextView) convertView.findViewById(R.id.redball_count_text);
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}
			
			if(ballcolor==3){
				holder.tx.setTextSize(14);
				holder.tx.setText(list.get(position) + "%");
				holder.tx.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.blue_random_selector));
			}else if (ballcolor == 1) {
				holder.tx.setText(list.get(position) +"");
				holder.tx.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_btn_red_to_darkred_selector));
			} else {
				holder.tx.setText(list.get(position) +"");
				holder.tx.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.blue_random_selector));
			}
			return convertView;
		}

	}
	static class Holder {
		TextView tx;
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			dialog.dismiss();
			if(onbuttonItemClickListener != null){
				onbuttonItemClickListener.onItemClickListener(data.get(position));
			}
		}
	};
	
	public interface OnButtonItemClickListener {
		public void onItemClickListener( int num);
	}
	
	public void setOnItemClickListener(OnButtonItemClickListener onButtonItemClickListener) {
		this.onbuttonItemClickListener = onButtonItemClickListener;
	}
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
