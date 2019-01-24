package com.zhisiyun.bi.bean.defaultBean;

import java.io.Serializable;
import java.util.Date;

public class RsColumnConf implements Serializable {

	/**
	 * 数据集 字段配置表
	 */
	private static final long serialVersionUID = 8806299147987958826L;

	private Integer sn_id; // 流水号id

	private String id; // 唯一键id;同步的拉过来,本地的rs_t_id+rsc_name

	private String rs_t_id; // table id

	private String is_calc; // 是否计算字段;自己创建的就是Y

	private String rsc_display; // 展示名称

	private String rsc_formatter; // 格式化

	private String rsc_id; // 字段的Id用来关联用的

	private String rsc_name; // 字段

	private String rsc_array; // 字段的原始字段

	/***
	 * "dim_t:时间维度 dim_o:组织维度 dim_n:普通维度 dim_b:布尔维度（只有固定值Y/N） num_n:普通度量（不可聚合）
	 * num_c:计算度量（可以聚合） str:属性（只能用来显示，不能用来分析） dim是维度，可用来筛选关联。 num是度量，可用来计算。
	 * str是属性，只能用来显示。"
	 ***/
	private Integer rsc_type; // 字段类型 1时间类型2组织类型3普通类型4布尔类型   ; 11:num_n普通度量类型12num_c计算度量类型 21:str属性只能用来显示不能用来关联查询

	private Integer rsc_sort; // 排序

	private String rsc_remark; // 备注

	private String is_active; // 自定义数据集字段 是否可用 Y可用 N删除

	private Date create_date;

	private Date modify_date;

	public Integer getSn_id() {
		return sn_id;
	}

	public void setSn_id(Integer sn_id) {
		this.sn_id = sn_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRs_t_id() {
		return rs_t_id;
	}

	public void setRs_t_id(String rs_t_id) {
		this.rs_t_id = rs_t_id;
	}

	public String getIs_calc() {
		return is_calc;
	}

	public void setIs_calc(String is_calc) {
		this.is_calc = is_calc;
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

	public String getRsc_id() {
		return rsc_id;
	}

	public void setRsc_id(String rsc_id) {
		this.rsc_id = rsc_id;
	}

	public String getRsc_name() {
		return rsc_name;
	}

	public void setRsc_name(String rsc_name) {
		this.rsc_name = rsc_name;
	}

	public String getRsc_array() {
		return rsc_array;
	}

	public void setRsc_array(String rsc_array) {
		this.rsc_array = rsc_array;
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

	public String getIs_active() {
		return is_active;
	}

	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
}
