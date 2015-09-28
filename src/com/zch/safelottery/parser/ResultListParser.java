package com.zch.safelottery.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.zch.safelottery.bean.ResultListBean;

public class ResultListParser extends AbstractParser<ResultListBean>{

	@Override
	public ResultListBean parse(JSONObject json) throws JSONException {
		ResultListBean mBean = new ResultListBean();
		if(json.has("page")) mBean.setPage(json.getString("page"));
		if(json.has("pageSize")) mBean.setPageSize(json.getString("pageSize"));
		if(json.has("pageTotal")) mBean.setPageTotal(json.getString("pageTotal"));
		if(json.has("itemTotal")) mBean.setItemTotal(json.getString("itemTotal"));
		if(json.has("orderList")) mBean.setOrderList(json.getString("orderList"));
		if(json.has("programsList")) mBean.setProgramsList(json.getString("programsList"));
		return mBean;
	}

}
