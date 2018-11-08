package com.zhisiyun.bi.bean.defaultBean;

import java.io.Serializable;
import java.util.Date;

public class RsTableConf implements Serializable {

	/**
	 * 数据集 表
	 */
	private static final long serialVersionUID = -6272605384834358069L;

	private Integer id;

	private Integer ds_id;

	private String ds_name;

	private String ds_display;

	private Date create_date;

	private Date modify_date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDs_id() {
		return ds_id;
	}

	public void setDs_id(Integer ds_id) {
		this.ds_id = ds_id;
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
