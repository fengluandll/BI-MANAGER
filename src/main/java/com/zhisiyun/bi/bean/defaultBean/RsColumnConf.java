package com.zhisiyun.bi.bean.defaultBean;

import java.io.Serializable;

public class RsColumnConf implements Serializable {

	/**
	 * 数据集 字段配置表
	 */
	private static final long serialVersionUID = 8806299147987958826L;

	private Integer id;

	private Integer rs_t_id;

	private String is_calc;

	private Integer rsc_category;

	private String rsc_display;

	private String rsc_formatter;

	private String rsc_name;

	private Integer rsc_type;

	private Integer rsc_sort;

	private String rsc_remark;

	private Integer rsc_conversion;

	private String rsc_order;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRs_t_id() {
		return rs_t_id;
	}

	public void setRs_t_id(Integer rs_t_id) {
		this.rs_t_id = rs_t_id;
	}

	public String getIs_calc() {
		return is_calc;
	}

	public void setIs_calc(String is_calc) {
		this.is_calc = is_calc;
	}

	public Integer getRsc_category() {
		return rsc_category;
	}

	public void setRsc_category(Integer rsc_category) {
		this.rsc_category = rsc_category;
	}

	public String getRsc_display() {
		return rsc_display;
	}

	public void setRsc_display(String rsc_display) {
		this.rsc_display = rsc_display;
	}

	public String getRsc_formatter() {
		return rsc_formatter;
	}

	public void setRsc_formatter(String rsc_formatter) {
		this.rsc_formatter = rsc_formatter;
	}

	public String getRsc_name() {
		return rsc_name;
	}

	public void setRsc_name(String rsc_name) {
		this.rsc_name = rsc_name;
	}

	public Integer getRsc_type() {
		return rsc_type;
	}

	public void setRsc_type(Integer rsc_type) {
		this.rsc_type = rsc_type;
	}

	public Integer getRsc_sort() {
		return rsc_sort;
	}

	public void setRsc_sort(Integer rsc_sort) {
		this.rsc_sort = rsc_sort;
	}

	public String getRsc_remark() {
		return rsc_remark;
	}

	public void setRsc_remark(String rsc_remark) {
		this.rsc_remark = rsc_remark;
	}

	public Integer getRsc_conversion() {
		return rsc_conversion;
	}

	public void setRsc_conversion(Integer rsc_conversion) {
		this.rsc_conversion = rsc_conversion;
	}

	public String getRsc_order() {
		return rsc_order;
	}

	public void setRsc_order(String rsc_order) {
		this.rsc_order = rsc_order;
	}

}
