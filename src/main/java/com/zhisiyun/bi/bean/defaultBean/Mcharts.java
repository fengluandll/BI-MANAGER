package com.zhisiyun.bi.bean.defaultBean;

import java.io.Serializable;
import java.util.Date;

public class Mcharts implements Serializable {

	/**
	 * m 仪表板 组件表
	 * 
	 * @author wangliu
	 */
	private static final long serialVersionUID = 3927821298264635207L;

	private Integer id;

	private Integer dashboard_id;

	private String name;

	// 组件配置信息 Json
	private String config;

	private Date create_date;

	private Date modify_date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDashboard_id() {
		return dashboard_id;
	}

	public void setDashboard_id(Integer dashboard_id) {
		this.dashboard_id = dashboard_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
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
