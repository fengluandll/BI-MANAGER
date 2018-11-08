package com.zhisiyun.bi.bean.reportPro;

import java.util.List;
import java.util.Map;

public class SearchRelation {

	// relation;
	// chart 里只有relationFields;
	// search 里 有 label 和 参数;

	// 搜索子项标题
	private String label;

	// 关联关系
	// relation:{ "3591":{"label":"","relationFields":{"27":[3591]},"props":[""]}, 
	// chart uuuid,column id
	private Map<String, String[]> relationFields;

	// 参数值
	private String[] props;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<String, String[]> getRelationFields() {
		return relationFields;
	}

	public void setRelationFields(Map<String, String[]> relationFields) {
		this.relationFields = relationFields;
	}

	public String[] getProps() {
		return props;
	}

	public void setProps(String[] props) {
		this.props = props;
	}


}
