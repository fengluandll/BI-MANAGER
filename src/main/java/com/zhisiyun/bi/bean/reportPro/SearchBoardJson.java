package com.zhisiyun.bi.bean.reportPro;

import java.util.List;
import java.util.Map;

public class SearchBoardJson {
	/*
	 * m_dashboard 里 搜索框 的json
	 */

	// uuuid
	private String name;
	// 父容器 name
	private String fatherName;
	// 类型 search
	private String type;
	// 数据集 id
	private String chartId;

	private String styleConfig;

	// 关联关系 里面有每个搜索 字段 和 参数 String 为 columnid
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
