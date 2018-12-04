package com.zhisiyun.bi.bean.defaultBean;

import java.io.Serializable;
import java.util.Date;

public class Mdashboard implements Serializable {

	/**
	 * 用户仪表板表
	 * 
	 * @author wangliu
	 * 
	 */
	private static final long serialVersionUID = 1141953311686844636L;

	private Integer id; // 唯一Id

	private String name; // 名称

	private String style_config; // 样式 信息 Json

	private Date create_date;

	private Date modify_date;

	private Integer template_id; // 模板表id

	private String group_id; // 组织Id、公司id

	private String privilege; // 权限json

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

	public Integer getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Integer template_id) {
		this.template_id = template_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

}
