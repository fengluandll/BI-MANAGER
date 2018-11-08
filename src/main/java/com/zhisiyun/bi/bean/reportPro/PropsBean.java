package com.zhisiyun.bi.bean.reportPro;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class PropsBean {
	/*
	 * 搜索参数bean
	 * 
	 */

	// 搜索框子项 的 数据id
	private String searchItemId;

	// 被关联的图表的数据id
	private String chartItemId;

	// 参数的值
	private String[] propsList;

	// 搜索框子项的 配置
	private JSONObject itemStyle;

	public String getSearchItemId() {
		return searchItemId;
	}

	public void setSearchItemId(String searchItemId) {
		this.searchItemId = searchItemId;
	}

	public String getChartItemId() {
		return chartItemId;
	}

	public void setChartItemId(String chartItemId) {
		this.chartItemId = chartItemId;
	}

	public String[] getPropsList() {
		return propsList;
	}

	public void setPropsList(String[] propsList) {
		this.propsList = propsList;
	}

	public JSONObject getItemStyle() {
		return itemStyle;
	}

	public void setItemStyle(JSONObject itemStyle) {
		this.itemStyle = itemStyle;
	}


}
