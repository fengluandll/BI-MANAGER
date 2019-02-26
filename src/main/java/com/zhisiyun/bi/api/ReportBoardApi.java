package com.zhisiyun.bi.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhisiyun.bi.bean.defaultBean.Mcharts;
import com.zhisiyun.bi.bean.defaultBean.Mdashboard;
import com.zhisiyun.bi.bean.defaultBean.PrivilegeEdit;
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;
import com.zhisiyun.bi.bean.defaultBean.RsReport;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.bean.defaultBean.Tdashboard;
import com.zhisiyun.bi.bean.reportPro.SearchBean;
import com.zhisiyun.bi.defaultDao.MdashboardMapper;
import com.zhisiyun.bi.defaultDao.PrivilegeEditMapper;
import com.zhisiyun.bi.defaultDao.RsColumnConfMapper;
import com.zhisiyun.bi.defaultDao.RsReportMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;
import com.zhisiyun.bi.defaultDao.TdashboardMapper;
import com.zhisiyun.bi.serviceImp.LogDebugImp;
import com.zhisiyun.bi.serviceImp.ReportBoardImp;
import com.zhisiyun.bi.serviceImp.ReportBoardImpPro;
import com.zhisiyun.bi.utils.CacheUtil;
import com.zhisiyun.bi.utils.ExcelUtils;
import com.zhisiyun.bi.utils.MD5Uitls;
import com.zhisiyun.bi.utils.MchartsUtils;
import com.zhisiyun.bi.utils.ReportUtils;

@RestController
@RequestMapping("api/reportBoard")
public class ReportBoardApi {
	@Autowired
	private ReportUtils reportUtils;

	@Autowired
	private TdashboardMapper tdashboardMapper;

	@Autowired
	private MdashboardMapper mdashboardMapper;

	@Autowired
	private RsColumnConfMapper rsColumnConfMapper;

	@Autowired
	private RsTableConfMapper rsTableConfMapper;

	@Autowired
	private PrivilegeEditMapper privilegeEditMapper;

	@Autowired
	ReportBoardImp reportBoardImp;

	@Autowired
	ReportBoardImpPro reportBoardImpPro;

	@Autowired
	RsReportMapper rsReportMapper;

	@Autowired
	CacheUtil cacheUtil;

	@Autowired
	MchartsUtils mchartsUtils;

	@Autowired
	LogDebugImp logDebugImp;

	public static String SEPARTOR = ",";
	@Value("${report.url}")
	private String REPORT_URL;

	private static Logger log = LoggerFactory.getLogger(ReportBoardApi.class);

	/**
	 * 调用的api接口
	 * 
	 * @param pageId、ID_GRUP
	 * 
	 * @return
	 */
	@RequestMapping(value = "/report", method = RequestMethod.POST)
	public String report(HttpServletRequest req) {
		try {// 参数 大写的是老的 新的都是小写的,新版字段 people,client
			String pageId = req.getParameter("pageId");
			String reportId = UUID.randomUUID().toString();
			JSONObject params = new JSONObject();
			String ID_GRUP = req.getParameter("ID_GRUP");
			if (ID_GRUP != null) {
				params.put("ID_GRUP", ID_GRUP.split(SEPARTOR));
			}
			String ID_COMP = req.getParameter("ID_COMP");
			if (ID_COMP != null) {
				params.put("ID_COMP", ID_COMP.split(SEPARTOR));
			}
			String ID_ORG = req.getParameter("ID_ORG");
			if (ID_ORG != null) {
				params.put("ID_ORG", ID_ORG.split(SEPARTOR));
			}
			String ID_EMP = req.getParameter("ID_EMP");
			if (ID_EMP != null) {
				params.put("ID_EMP", ID_EMP.split(SEPARTOR));
			}
			String ID_E_G_P = req.getParameter("ID_E_G_P");
			if (ID_E_G_P != null) {
				params.put("id_emp_g", ID_E_G_P.split(SEPARTOR));
			}
			String ID_E_G_S = req.getParameter("ID_E_G_S");
			if (ID_E_G_S != null) {
				params.put("id_emp_sg", ID_E_G_S.split(SEPARTOR));
			}
			String ID_P_R_P = req.getParameter("ID_P_R_P");
			if (ID_P_R_P != null) {
				params.put("id_pa_r", ID_P_R_P.split(SEPARTOR));
			}
			String ID_P_R_S = req.getParameter("ID_P_R_S");
			if (ID_P_R_S != null) {
				params.put("id_pa_sr", ID_P_R_S.split(SEPARTOR));
			}
			String PEOPLE = req.getParameter("people");
			if (PEOPLE != null) {
				params.put("PEOPLE", PEOPLE.split(SEPARTOR));
			}
			String CLIENT = req.getParameter("client");
			if (CLIENT != null) {
				params.put("CLIENT", CLIENT.split(SEPARTOR));
			}
			rsReportMapper.add(reportId, pageId, params.toJSONString());
			return REPORT_URL + reportId;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * reportBoard 第一次 获取 m_dashboard和 m_chart数据
	 * 
	 * @param boardId就是reportid
	 * 
	 * @param config
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/fetch", method = RequestMethod.POST)
	public String fetch(String boardId) {
		JSONObject rest = new JSONObject();
		Tdashboard tdashboardAll = null; // 全局Tdashboard用来给idColumns查询用的 add by wangliu 20190111
		try {
			// 插入日志
			logDebugImp.add(boardId, " : fetch  进入");
			// 根据 boardId(reportId) 取出RsReport
			RsReport rsReport = rsReportMapper.selectByReportId(boardId);
			// 获取参数 取出 id_emp 和 ID_GRUP
			String params = rsReport.getParams();
			String people = JSON.parseObject(params).getJSONArray("PEOPLE").getString(0); // 用户的userId,判断权限用的,如果是自己T_dashboard就是"0"
			String client = JSON.parseObject(params).getJSONArray("CLIENT").getString(0); // 用户的集团id,判断权限用的
			String templateId = rsReport.getPage_id(); // report表里的page_id就是templateId
			// 根据people判断是否有编辑页面权限
			String user_auth = "0";
			if (null != people && !"".equals(people)) {
				PrivilegeEdit privilegeEdit = privilegeEditMapper.selectById(people);
				if (null != privilegeEdit && !privilegeEdit.getId_emp().equals("")) {
					user_auth = "1"; // "1"代表有权限
				}
			}
			// 判断userId是客户还是自己
			String user_type = "customer";
			if (null != people && people.equals("0")) {
				user_type = "self";
				user_auth = "1"; // 如果是自己人编辑就给编辑权限
			}
			if (user_type.equals("customer")) {
				// 客户
				Mdashboard mDashboard = CacheUtil.mDashboard.get(templateId + client);
				Tdashboard tDashboard = CacheUtil.tDashboard.get(templateId);
				if (null == mDashboard) {
					// 如果m_dashboard表里还没有数据就要从t_dashboard里copy
					mDashboard = new Mdashboard();
					mDashboard.setName(tDashboard.getName());
					mDashboard.setStyle_config(tDashboard.getStyle_config());
					mDashboard.setTemplate_id(templateId);
					mDashboard.setGroup_id(client);
					mDashboard.setId(MD5Uitls.getMD5Id(templateId + client)); // 设置唯一键 template_id+group_id
					mdashboardMapper.addByBean(mDashboard);
					// 调用刷新内存
					cacheUtil.refreshMDashboardone(MD5Uitls.getMD5Id(templateId + client));
					// 重新取出
					mDashboard = CacheUtil.mDashboard.get(templateId + client);
				}
				rest.put("mDashboard", mDashboard);
				rest.put("tDashboard", tDashboard);
				tdashboardAll = tDashboard;
			} else {
				// 自己
				Tdashboard tDashboard = CacheUtil.tDashboard.get(rsReport.getPage_id());
				rest.put("mDashboard", tDashboard);
				tdashboardAll = tDashboard;
			}
			List<Mcharts> mCharts = CacheUtil.mCharts.get(templateId);
			// 查询每个图表所拥有的 维度 度量图例 和 搜索框的 子组件 所在 字段 的对应的表数据
			Map<String, Object> idColumns = reportUtils.getColumnsByDateSet(tdashboardAll);
			rest.put("mCharts", mCharts);
			rest.put("idColumns", idColumns);
			rest.put("user_type", user_type);
			rest.put("user_auth", user_auth);
			rest.put("success", "success");
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return rest.toJSONString();
	}

	@RequestMapping(value = "/fetchEdit", method = RequestMethod.POST)
	public Map<String, Object> fetchEdit(String boardId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 根据 boardId(reportId) 取出RsReport
			RsReport rsReport = rsReportMapper.selectByReportId(boardId);
			List<Mcharts> mCharts = CacheUtil.mCharts.get(rsReport.getPage_id());
			// 查询每个图表 所拥有的 数据集 的 所有字段 的表数据
			JSONObject tableIdColumns = new JSONObject();
			List<String> dataSetNameList = new ArrayList<String>(); // 数据集名称List
			for (Mcharts mchart : mCharts) { // 循环是所有的图表找到他们的 数据集
				String config = mchart.getConfig();
				JSONObject object = JSON.parseObject(config);
				String dataSetName = object.getString("dataSetName");
				Boolean flag = true;
				for (String name : dataSetNameList) {
					if (name.equals(dataSetName)) {
						flag = false;
					}
				}
				if (flag && null != dataSetName) { // 如果名称和之前的不重复就查询
					dataSetNameList.add(dataSetName);
					RsTableConf rsTableConf = rsTableConfMapper.selectByName(dataSetName).get(0);
					List<RsColumnConf> tableIdColumn = rsColumnConfMapper.selectByTableId(rsTableConf.getId());
					tableIdColumns.put(dataSetName, tableIdColumn);
				}
			}

			map.put("tableIdColumns", tableIdColumns);
			map.put("success", "success");
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return map;
	}

	/***
	 * 通用查询方法
	 * 
	 ***/
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public Map<String, Object> search(String params) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 图表数据集合
		Map<String, Object> dataList = new HashMap<String, Object>();
		try {
			JSONObject object = JSON.parseObject(params);
			String param = object.getString("params");
			// 取出传过来的参数
			SearchBean searchBean = JSON.parseObject(param, SearchBean.class);
			// 根据 boardId(reportId) 取出RsReport
			RsReport rsReport = rsReportMapper.selectByReportId(searchBean.getReport_id());
			// 请求search数据
			dataList = reportBoardImpPro.search(rsReport, searchBean);
			map.put("dataList", dataList);
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return map;
	}

	/***
	 * 交叉表导出excel
	 * 
	 ***/
	@RequestMapping(value = "/exportTableExcel", method = RequestMethod.POST)
	public void exportTableExcel(HttpServletResponse response, String params) {
		try {
			// 图表数据集合
			Map<String, Object> dataList = new HashMap<String, Object>();
			JSONObject object = JSON.parseObject(params);
			String param = object.getString("params");
			// 取出传过来的参数
			SearchBean searchBean = JSON.parseObject(param, SearchBean.class);
			// 根据 boardId(reportId) 取出RsReport
			RsReport rsReport = rsReportMapper.selectByReportId(searchBean.getReport_id());
			// 请求search数据
			dataList = reportBoardImpPro.search(rsReport, searchBean);
			Map<String, Object> map = new HashMap<>(); // 交叉表table的数据
			for (Map.Entry<String, Object> m : dataList.entrySet()) {
				map = (Map<String, Object>) m.getValue();
			}
			// 取出dataList中的交叉表数据并导出excel
			String chart_id = searchBean.getChildren().get(0).getChartId(); // 图表id
			Mcharts mCharts = CacheUtil.mchart.get(chart_id);
			List<String> header = (List<String>) map.get("header");
			List body = (List) map.get("body");
			ExcelUtils.export(response, header, body, mCharts.getName());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * @修改样式、关联关系保存
	 * 
	 * @props fatherId
	 * 
	 * @serialData 20180929
	 * @author wangliu
	 * 
	 */
	@RequestMapping(value = "/saveBoard", method = RequestMethod.POST)
	public String saveBoard(String params) {
		JSONObject rest = new JSONObject();
		try {
			JSONObject object = JSON.parseObject(params);
			JSONObject dashboard = object.getJSONObject("mDashboard_porp"); // 前端传过来的dashboard
			String dashboard_type = object.getString("dashboard_type"); // 客户还是自己
			String id = dashboard.getString("id"); // dashboard id
			String style_config = dashboard.getJSONObject("style_config").toString();
			// 判断userId是客户还是自己
			if (dashboard_type.equals("customer")) {// 客户
				mdashboardMapper.updateById(id, style_config);
				// 跟新完刷新缓存
				cacheUtil.refreshMDashboardone(id);
			} else if (dashboard_type.equals("self")) {// 自己
				tdashboardMapper.updateById(id, style_config);
				// 跟新完刷新缓存
				cacheUtil.refreshTDashboardone(id);
			}
			rest.put("success", "success");
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return rest.toJSONString();
	}

	/**
	 * @点击搜索框子项str请求下拉列表数据
	 * 
	 * @props id
	 * @props boardId(reportId)
	 * 
	 * @serialData 20181009
	 * @author wangliu
	 * 
	 */
	@RequestMapping(value = "/searchItemData", method = RequestMethod.POST)
	public Map<String, Object> searchItemData(@RequestParam(value = "id", required = true) String id, String boardId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 图表数据集合
		List dataList = new ArrayList();
		try {
			// 根据 boardId(reportId) 取出RsReport
			RsReport rsReport = rsReportMapper.selectByReportId(boardId);
			dataList = reportBoardImpPro.searchItemData(id, rsReport);
			map.put(id, dataList);
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return map;
	}

	/**
	 * @点击搜索框子项str请求下拉列表数据
	 * 
	 * @props id
	 * @props boardId(reportId)
	 * 
	 * @serialData 20181009
	 * @author wangliu
	 * 
	 */
	@RequestMapping(value = "/pullSynchronizationTab", method = RequestMethod.POST)
	public String pullSynchronizationTab(String id) {
		JSONObject rest = new JSONObject();
		try {
			// 客户
			Mdashboard mDashboard = mdashboardMapper.selectById(id);
			// 自己
			Tdashboard tDashboard = tdashboardMapper.selectById(mDashboard.getTemplate_id());
			// 取出 tDashboard中key的值
			String style_config_t = tDashboard.getStyle_config();
			JSONObject obj_t = JSON.parseObject(style_config_t);
			JSONObject children_t = obj_t.getJSONObject("children");
			List<String> keys_t = new ArrayList<String>();// tDashboard所有的key
			for (Map.Entry<String, Object> entry : children_t.entrySet()) {
				keys_t.add(entry.getKey());
			}
			// 取出 mDashboard中key的值
			String style_config_m = mDashboard.getStyle_config();
			JSONObject obj_m = JSON.parseObject(style_config_m);
			JSONObject children_m = obj_m.getJSONObject("children");
			List<String> keys_m = new ArrayList<String>();// tDashboard所有的key
			for (Map.Entry<String, Object> entry : children_m.entrySet()) {
				keys_m.add(entry.getKey());
			}
			// 找出t中有的而m中没有的
			List<String> keys = new ArrayList<String>();
			for (int i = 0; i < keys_t.size(); i++) {
				Boolean flag = true;
				for (int j = 0; j < keys_m.size(); j++) {
					if (keys_t.get(i).equals(keys_m.get(j))) {
						flag = false;
					}
				}
				if (flag) {
					keys.add(keys_t.get(i));
				}
			}
			// 根据keys把数据更新到mDashboard
			for (int i = 0; i < keys.size(); i++) {
				children_m.put(keys.get(i), children_t.getJSONObject(keys.get(i)));
			}
			String style_config_save = obj_m.toString();
			mdashboardMapper.updateById(id, style_config_save);// 数据库更新
			// 跟新完刷新缓存
			cacheUtil.refreshMDashboardone(id);
			rest.put("success", "true");
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return rest.toJSONString();
	}

}
