package com.zhisiyun.bi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zhisiyun.bi.bean.defaultBean.Mcharts;
import com.zhisiyun.bi.bean.defaultBean.Mdashboard;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.defaultDao.MchartsMapper;
import com.zhisiyun.bi.defaultDao.MdashboardMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;

@Controller
@RequestMapping("dashboard")
public class DashBoardController {

	@Autowired
	private MdashboardMapper mDashboardMapper;

	@Autowired
	private RsTableConfMapper rsTableConfMapper;

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
	public ModelAndView editDataSet(Model model, String id) throws Exception {
		ModelAndView view = new ModelAndView();
		model.addAttribute("id", id);
		view.setViewName("dashboard/dashboard_dataSet");
		return view;
	}

	/**
	 * @see 弹窗页面编辑数据集
	 * @author wangliu
	 * @serialData 20181113
	 **/
	@RequestMapping("/addDataSet")
	public ModelAndView addDataSet(Model model, String id) throws Exception {
		ModelAndView view = new ModelAndView();
		model.addAttribute("id", id);
		try {
			// 找mDashboard中的 dataSet和dataSetRelation
			Mdashboard mDashboard = mDashboardMapper.selectById(Integer.parseInt(id));
			String style_config = mDashboard.getStyle_config();
			// 找出所有的数据集
			List<RsTableConf> tablelist = rsTableConfMapper.selectAll();
			model.addAttribute("style_config", style_config);
			model.addAttribute("tablelist", tablelist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		view.setViewName("dashboard/dashboard_addDataSet");
		return view;
	}

	/**
	 * @see 弹窗页面编辑数据集关联
	 * @author wangliu
	 * @serialData 20181113
	 **/
	@RequestMapping("/addDataSetRelation")
	public ModelAndView addDataSetRelation(Model model, String id) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/dashboard_addDataRelation");
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
