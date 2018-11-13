package com.zhisiyun.bi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zhisiyun.bi.defaultDao.MdashboardMapper;

@Controller
@RequestMapping("dashboard")
public class DashBoardController {

	@Autowired
	private MdashboardMapper mDashboardMapper;

	/**
	 * @see dashboard列表
	 * @author wangliu
	 * @serialData 20180913
	 **/
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/dashboardList");
		return view;
	}

	/**
	 * @see 增加一个dashboard
	 * @author wangliu
	 * @serialData 20181019
	 **/
	@RequestMapping("/addDashboard")
	public ModelAndView addDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/addDashboard");
		return view;
	}

	/**
	 * @see 跳转dashboard 编辑dateset字段关联页面
	 * @author wangliu
	 * @serialData 20181113
	 **/
	@RequestMapping("/editDataSet")
	public ModelAndView editDataSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/dashboard_dataSet");
		return view;
	}

	/******************************** 下面是chart ************************/

	/**
	 * @see charts列表
	 * @author wangliu
	 * @serialData 20180913
	 **/
	@RequestMapping("/chartsList")
	public ModelAndView chartsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/chartsList");
		return view;
	}

	/**
	 * @see 增加一个chart
	 * @author wangliu
	 * @serialData 20181019
	 **/
	@RequestMapping("/addChart")
	public ModelAndView addChart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/addChart");
		return view;
	}

}
