package com.zhisiyun.bi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhisiyun.bi.bean.defaultBean.Mdashboard;
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.defaultDao.MdashboardMapper;
import com.zhisiyun.bi.defaultDao.RsColumnConfMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;

@Controller
@RequestMapping("dashboard")
public class DashBoardController {

	@Autowired
	private MdashboardMapper mDashboardMapper;

	@Autowired
	private RsTableConfMapper rsTableConfMapper;

	@Autowired
	private RsColumnConfMapper rsColumnConfMapper;

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
	 * @param id:dashboardId
	 **/
	@RequestMapping("/editDataSet")
	public ModelAndView editDataSet(Model model, String id) throws Exception {
		ModelAndView view = new ModelAndView();
		model.addAttribute("id", id);
		try {
			Mdashboard mDashboard = mDashboardMapper.selectById(Integer.parseInt(id));
			String style_config = mDashboard.getStyle_config();
			JSONObject obj = JSON.parseObject(style_config);
			JSONArray dataSet = obj.getJSONArray("dataSet"); // dataSet
			JSONArray dataSetRelation = obj.getJSONArray("dataSetRelation"); // dataSetRelation
			List<String> dataSetList = JSON.toJavaObject(dataSet, List.class);
			List<List> relationList = JSON.toJavaObject(dataSetRelation, List.class);
			List<RsTableConf> tableList = new ArrayList<RsTableConf>();
			for (String dataSetId : dataSetList) {
				// 根据id 查re_column_conf
				RsTableConf table = rsTableConfMapper.selectById(Integer.parseInt(dataSetId)); // table表 为了取table得name
				tableList.add(table);
			}
			model.addAttribute("tableList", tableList);// 选中的数据集
			// 为 dataSetRelation 中的结构拼接成 数据集名 字段Id的形式
			List<String> relationStrList = new ArrayList<String>();
			for (List<String> columns : relationList) {
				String columnsContent = "";
				for (String columnId : columns) {
					if (null == columnId || "".equals(columnId)) {
						continue;
					}
					RsColumnConf rsColumnConf = rsColumnConfMapper.selectOneById(Integer.parseInt(columnId));
					Integer rs_t_id = rsColumnConf.getRs_t_id();
					String display = rsColumnConf.getRsc_display();
					RsTableConf table = rsTableConfMapper.selectById(rs_t_id);
					String ds_display = table.getDs_display();
					columnsContent = columnsContent + ds_display + " : " + display + " ; " + " - - ";
				}
				relationStrList.add(columnsContent);
			}
			model.addAttribute("relationStrList", relationStrList);// 选中的数据集的关联关系
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		model.addAttribute("id", id);
		try {
			// 找mDashboard中的 dataSet
			Mdashboard mDashboard = mDashboardMapper.selectById(Integer.parseInt(id));
			String style_config = mDashboard.getStyle_config();
			JSONObject obj = JSON.parseObject(style_config);
			JSONArray dataSet = obj.getJSONArray("dataSet"); // dataSet
			List<String> list = JSON.toJavaObject(dataSet, List.class);
			Map<String, List> map = new HashMap<String, List>();
			Map<String, String> tableName = new HashMap<String, String>();
			for (String dataSetId : list) {
				// 根据id 查re_column_conf
				List<RsColumnConf> columns = rsColumnConfMapper.selectByTableId(Integer.parseInt(dataSetId)); // dataSet对应的字段
				map.put(dataSetId, columns);
				RsTableConf table = rsTableConfMapper.selectById(Integer.parseInt(dataSetId)); // table表 为了取table得name
				tableName.put(dataSetId, table.getDs_display());
			}
			model.addAttribute("dataSetList", list);
			model.addAttribute("columnsList", map);
			model.addAttribute("tableName", tableName);
			model.addAttribute("style_config", style_config);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
