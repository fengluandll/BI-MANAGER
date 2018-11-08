package com.zhisiyun.bi.bean.defaultBean;

import java.io.Serializable;
import java.util.Date;

public class Mdashboard implements Serializable {

	/**
	 * m 仪表板配置表
	 * 
	 * @author wangliu
	 * 
	 */
	private static final long serialVersionUID = 1141953311686844636L;

	private Integer id;

	private String name;

	// 样式 信息 Json
	private String style_config;

	private Date create_date;

	private Date modify_date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStyle_config() {
		return style_config;
	}

	public void setStyle_config(String style_config) {
		this.style_config = style_config;
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
