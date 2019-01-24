package com.zhisiyun.bi.bean.defaultBean;

import java.io.Serializable;
import java.util.Date;

public class RsTableConf implements Serializable {

	/**
	 * 数据集 表
	 */
	private static final long serialVersionUID = -6272605384834358069L;

	private Integer sn_id; // 流水号id

	private String id; // 唯一键id 传过来的

	private String ds_name; // 表名

	private String ds_display; // 表中文名

	private Integer ds_type;// table 数据集类型 1 标准 2自定义

	private String column_array; // 自定义数据集刷字段的

	private String logic; // table 里的sql

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

	public String getDs_name() {
		return ds_name;
	}

	public void setDs_name(String ds_name) {
		this.ds_name = ds_name;
	}

	public String getDs_display() {
		return ds_display;
	}

	public void setDs_display(String ds_display) {
		this.ds_display = ds_display;
	}

	public Integer getDs_type() {
		return ds_type;
	}

	public void setDs_type(Integer ds_type) {
		this.ds_type = ds_type;
	}

	public String getColumn_array() {
		return column_array;
	}

	public void setColumn_array(String column_array) {
		this.column_array = column_array;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
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
