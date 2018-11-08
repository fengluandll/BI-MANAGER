package com.zhisiyun.bi.bean.defaultBean;

import java.io.Serializable;
import java.util.Date;

public class RsReport implements Serializable {
	/**
	 * report 表
	 */
	private static final long serialVersionUID = 376637347836384761L;
	private Integer id;
	private String report_id;
	private String page_id;
	private String params;
	private Date create_date;
	private Date modify_date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public String getPage_id() {
		return page_id;
	}

	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
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
