package com.zhisiyun.bi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhisiyun.bi.bean.defaultBean.Mcharts;
import com.zhisiyun.bi.bean.defaultBean.Mdashboard;
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.defaultDao.MchartsMapper;
import com.zhisiyun.bi.defaultDao.MdashboardMapper;
import com.zhisiyun.bi.defaultDao.RsColumnConfMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;

@Controller
@RequestMapping("edit")
public class EditChartsController {

	@Autowired
	private MchartsMapper mChartsMapper;

	@Autowired
	private MdashboardMapper mDashboardMapper;

	@Autowired
	private RsTableConfMapper rsTableConfMapper;

	@Autowired
	private RsColumnConfMapper rsColumnConfMapper;
	
	@Value("${edit.url}")
	private String EDIT_URL;

	/**
	 * @see 编辑图表控件页面
	 * @author wangliu
	 * @serialData 20180913
	 **/
	@RequestMapping("/chart")
	public ModelAndView index(Model model, String id, String dashboardId) throws Exception {
		List<Mcharts> mChartsList = null;
		Mcharts mCharts = null;
		Mdashboard mdashboard = null;
		List<RsTableConf> rsTableConfList = null;
		try {
			// 请求 dashboard 获取一共有多少个 组件
			mChartsList = mChartsMapper.selectById(Integer.parseInt(dashboardId));
			// 请求对应的 组件 数据
			mCharts = mChartsMapper.selectOne(Integer.parseInt(id), Integer.parseInt(dashboardId));
			// dashboard
			mdashboard = mDashboardMapper.selectById(Integer.parseInt(dashboardId));
			// 查询 数据集(根据dashboard中配置的查询)
			String style_config_str = mdashboard.getStyle_config();
			JSONObject obj = JSON.parseObject(style_config_str);
			JSONArray dataSet = obj.getJSONArray("dataSet");
			List<Integer> idList = new ArrayList<Integer>(); // rs_table_config 的 id list
			for (int i = 0; i < dataSet.size(); i++) {
				idList.add(dataSet.getInteger(i));
			}
			rsTableConfList = rsTableConfMapper.selectByIdList(idList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView view = new ModelAndView();
		model.addAttribute("id", id);
		model.addAttribute("dashboardId", dashboardId);
		model.addAttribute("mChartsList", mChartsList);
		model.addAttribute("rsTableConfList", rsTableConfList);
		model.addAttribute("mCharts", mCharts);
		model.addAttribute("mdashboard", mdashboard);
		model.addAttribute("edit_url", EDIT_URL);

		// 根据 type 来跳转 需要的 页面
		// 0 1 2 3 图表 4 table 5 search
		String config = mCharts.getConfig();
		JSONObject obj = JSON.parseObject(config);
		String type = obj.getString("type");
		if ("0".equals(type) || "1".equals(type) || "2".equals(type)) {
			view.setViewName("dashboard/editCharts");
		}
		if ("3".equals(type)) {
			view.setViewName("dashboard/editTable");
		}
		if ("11".equals(type)) {
			view.setViewName("dashboard/editSearch");
		}
		return view;
	}

	/**
	 * @see 编辑图表的 列
	 * @author wangliu
	 * @serialData 20180920
	 * @param ds_name
	 *            数据集table 名称
	 **/
	@RequestMapping("/chartColumn")
	public ModelAndView chartColumn(Model model, String ds_name) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/chartColumn");
		try {
			// 获取 rs_t_id
			RsTableConf rsTableConf = rsTableConfMapper.selectByName(ds_name).get(0);
			// 维度 rsc_category = 1
			List<RsColumnConf> dimension = rsColumnConfMapper.selectByTableIdAndType(rsTableConf.getId(), 1);
			// 度量 rsc_category = 2
			List<RsColumnConf> measure = rsColumnConfMapper.selectByTableIdAndType(rsTableConf.getId(), 2);
			model.addAttribute("dimension", dimension);
			model.addAttribute("measure", measure);
			model.addAttribute("rsTableConf", rsTableConf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	/**
	 * @see 编辑table的 列
	 * @author wangliu
	 * @serialData 20180920
	 * @param ds_name
	 *            数据集table 名称
	 **/
	@RequestMapping("/tableColumn")
	public ModelAndView tableColumn(Model model, String ds_name) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/tableColumn");
		try {
			// 获取 rs_t_id
			RsTableConf rsTableConf = rsTableConfMapper.selectByName(ds_name).get(0);
			// 维度 rsc_category = 1
			List<RsColumnConf> dimension = rsColumnConfMapper.selectByTableId(rsTableConf.getId());
			model.addAttribute("dimension", dimension);
			model.addAttribute("rsTableConf", rsTableConf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	/**
	 * @see 编辑暴露字段
	 * @author wangliu
	 * @serialData 20180921
	 * @param ds_name
	 *            数据集table 名称
	 **/
	@RequestMapping("/dataSearch")
	public ModelAndView dataSearch(Model model, String ds_name) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/dataSearch");
		try {
			// 获取 rs_t_id
			RsTableConf rsTableConf = rsTableConfMapper.selectByName(ds_name).get(0);
			// 维度 rsc_category = 1
			List<RsColumnConf> dimension = rsColumnConfMapper.selectByTableId(rsTableConf.getId());
			model.addAttribute("dimension", dimension);
			model.addAttribute("rsTableConf", rsTableConf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	/**
	 * @see 增加搜索框的item
	 * @author wangliu
	 * @serialData 20180921
	 * @param ds_name
	 *            数据集table 名称
	 **/
	@RequestMapping("/addItem")
	public ModelAndView addItem(Model model, String ds_name) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("dashboard/lay_selectItem");
		try {
			// 获取 rs_t_id
			RsTableConf rsTableConf = rsTableConfMapper.selectByName(ds_name).get(0);
			// 维度 rsc_category = 1
			List<RsColumnConf> dimension = rsColumnConfMapper.selectByTableId(rsTableConf.getId());
			model.addAttribute("dimension", dimension);
			model.addAttribute("rsTableConf", rsTableConf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
}
