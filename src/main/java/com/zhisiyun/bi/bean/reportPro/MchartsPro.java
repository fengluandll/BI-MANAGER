package com.zhisiyun.bi.bean.reportPro;

import java.util.List;
import java.util.Map;

public class MchartsPro {
	/*
	 * m_dashboard 表中的 style_config 字段的 每个chart的 json
	 * 
	 */

	// uuuid
	private String name;

	private String fatherName;

	private String type;

	private String chartId;

	private String styleConfig;

	// 关联关系 chart 的 SearchRelation 里 只有relationFields
	private Map<String, SearchRelation> relation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChartId() {
		return chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public String getStyleConfig() {
		return styleConfig;
	}

	public void setStyleConfig(String styleConfig) {
		this.styleConfig = styleConfig;
	}

	public Map<String, SearchRelation> getRelation() {
		return relation;
	}

	public void setRelation(Map<String, SearchRelation> relation) {
		this.relation = relation;
	}

}
