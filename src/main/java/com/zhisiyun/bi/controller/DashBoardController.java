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
	 * @see dashboard保存
	 * @author wangliu
	 * @serialData 20181019
	 **/
//	@RequestMapping("/saveDashboard")
//	public ModelAndView saveDashboard(String name) throws Exception {
//		ModelAndView view = new ModelAndView();
//		mDashboardMapper.add(name);
//		view.setViewName("dashboard/dashboardList");
//		return view;
//	}

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
