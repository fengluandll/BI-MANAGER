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

	private Integer sn_id; // 流水号Id

	private String id; // t_dashboard id+config计算md5

	private String dashboard_id; // 属于的t_dashboard id

	private String name; // 名称

	private Integer mc_type; // mcahrts类型

	private String config; // 组件配置信息 Json

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

	public String getDashboard_id() {
		return dashboard_id;
	}

	public void setDashboard_id(String dashboard_id) {
		this.dashboard_id = dashboard_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMc_type() {
		return mc_type;
	}

	public void setMc_type(Integer mc_type) {
		this.mc_type = mc_type;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
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
