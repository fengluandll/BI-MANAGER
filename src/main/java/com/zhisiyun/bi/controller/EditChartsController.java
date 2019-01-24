package com.zhisiyun.bi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhisiyun.bi.bean.BaseBean;
import com.zhisiyun.bi.bean.defaultBean.Mcharts;
import com.zhisiyun.bi.bean.defaultBean.Mdashboard;
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.bean.defaultBean.Tdashboard;
import com.zhisiyun.bi.defaultDao.MchartsMapper;
import com.zhisiyun.bi.defaultDao.MdashboardMapper;
import com.zhisiyun.bi.defaultDao.RsColumnConfMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;
import com.zhisiyun.bi.defaultDao.TdashboardMapper;

@Controller
@RequestMapping("edit")
public class EditChartsController {

	@Autowired
	private MchartsMapper mChartsMapper;

	@Autowired
	private TdashboardMapper tDashboardMapper;

	@Autowired
	private RsTableConfMapper rsTableConfMapper;

	@Autowired
	private RsColumnConfMapper rsColumnConfMapper;

	@Value("${edit.url}")
	private String EDIT_URL;

	/**
	 * @see charts列表
	 * @author wangliu
	 * @serialData 20180913
	 **/
	@RequestMapping("/chartsList")
	public ModelAndView chartsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("mCharts/chartsList");
		return view;
	}

	/**
	 * @see 编辑图表控件页面
	 * @author wangliu
	 * @serialData 20180913
	 **/
	@RequestMapping("/chart")
	public ModelAndView index(Model model, String sn_id, String dashboardId) throws Exception {
		List<Mcharts> mChartsList = null;
		Mcharts mCharts = null;
		Tdashboard tdashboard = null;
		List<RsTableConf> rsTableConfList = null;
		try {
			// 请求 dashboard 获取一共有多少个 组件
			mChartsList = mChartsMapper.selectById(dashboardId);
			// 请求对应的 组件 数据
			mCharts = mChartsMapper.selectOneBySnId(Integer.parseInt(sn_id));
			// dashboard
			tdashboard = tDashboardMapper.selectById(dashboardId);
			// 查询 数据集(根据dashboard中配置的查询)
			String style_config_str = tdashboard.getStyle_config();
			JSONObject obj = JSON.parseObject(style_config_str);
			JSONArray dataSet = obj.getJSONArray("dataSet");
			List<String> idList = new ArrayList<String>(); // rs_table_config 的 id list
			for (int i = 0; i < dataSet.size(); i++) {
				idList.add(dataSet.getString(i).toString());
			}
			rsTableConfList = rsTableConfMapper.selectByIdList(idList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView view = new ModelAndView();
		model.addAttribute("id", mCharts.getId());
		model.addAttribute("dashboardId", dashboardId);
		model.addAttribute("mChartsList", mChartsList);
		model.addAttribute("rsTableConfList", rsTableConfList);
		model.addAttribute("mCharts", mCharts);
		model.addAttribute("mdashboard", tdashboard);
		model.addAttribute("edit_url", EDIT_URL);

		// 根据 type 来跳转 需要的 页面
		// 0 1 2 3 图表 4 table 5 search
		String config = mCharts.getConfig();
		JSONObject obj = JSON.parseObject(config);
		/***
		 * 
		 * ***/
		String type = obj.getString("type");
		if ("0".equals(type) || "1".equals(type) || "2".equals(type)) {
			view.setViewName("mCharts/editCharts");
		}
		if ("3".equals(type) || "5".equals(type) || "7".equals(type)) {
			view.setViewName("mCharts/editTable");
		}
		if ("4".equals(type)) {
			view.setViewName("mCharts/editPivottable");
		}
		if ("11".equals(type)) {
			view.setViewName("mCharts/editSearch");
		}
		if ("6".equals(type)) {
			view.setViewName("mCharts/editText");
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
		view.setViewName("mCharts/chartColumn");
		try {
			List<String> type_ids = new ArrayList<String>();// 维度度量类型
			// 获取 rs_t_id
			RsTableConf rsTableConf = rsTableConfMapper.selectByName(ds_name).get(0);
			// 维度 
			type_ids.add("1");
			type_ids.add("2");
			type_ids.add("3");
			type_ids.add("4");
			type_ids.add("21");
			List<RsColumnConf> dimension = rsColumnConfMapper.selectByTableIdAndType(rsTableConf.getId(), type_ids);
			// 度量 
			type_ids = new ArrayList<String>();
			type_ids.add("11");
			type_ids.add("12");
			List<RsColumnConf> measure = rsColumnConfMapper.selectByTableIdAndType(rsTableConf.getId(), type_ids);
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
	public ModelAndView tableColumn(Model model, String ds_name, String type) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("mCharts/tableColumn");
		try {
			// 获取 rs_t_id
			RsTableConf rsTableConf = rsTableConfMapper.selectByName(ds_name).get(0);
			List<RsColumnConf> dimension = rsColumnConfMapper.selectByTableId(rsTableConf.getId());
			model.addAttribute("dimension", dimension);
			model.addAttribute("rsTableConf", rsTableConf);
			model.addAttribute("type", type);
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
		view.setViewName("mCharts/dataSearch");
		try {
			// 获取 rs_t_id
			RsTableConf rsTableConf = rsTableConfMapper.selectByName(ds_name).get(0);
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
		view.setViewName("mCharts/lay_selectItem");
		try {
			// 获取 rs_t_id
			RsTableConf rsTableConf = rsTableConfMapper.selectByName(ds_name).get(0);
			List<RsColumnConf> dimension = rsColumnConfMapper.selectByTableId(rsTableConf.getId());
			model.addAttribute("dimension", dimension);
			model.addAttribute("rsTableConf", rsTableConf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
}
