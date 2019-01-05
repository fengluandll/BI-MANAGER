package com.zhisiyun.bi.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhisiyun.bi.bean.defaultBean.Mcharts;
import com.zhisiyun.bi.bean.defaultBean.Mdashboard;
import com.zhisiyun.bi.bean.defaultBean.PrivilegeEdit;
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;
import com.zhisiyun.bi.bean.defaultBean.RsReport;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.bean.defaultBean.Tdashboard;
import com.zhisiyun.bi.defaultDao.JdbcDao;
import com.zhisiyun.bi.defaultDao.MchartsMapper;
import com.zhisiyun.bi.defaultDao.MdashboardMapper;
import com.zhisiyun.bi.defaultDao.PrivilegeEditMapper;
import com.zhisiyun.bi.defaultDao.RsColumnConfMapper;
import com.zhisiyun.bi.defaultDao.RsReportMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;
import com.zhisiyun.bi.defaultDao.TdashboardMapper;
import com.zhisiyun.bi.serviceImp.LogDebugImp;
import com.zhisiyun.bi.serviceImp.ReportBoardImp;
import com.zhisiyun.bi.utils.CacheUtil;
import com.zhisiyun.bi.utils.JsonUtils;

@RestController
@RequestMapping("api/reportBoard")
public class ReportBoardApi {
	@Autowired
	private MchartsMapper mChartsMapper;

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
	private JdbcDao jdbcDao;

	@Autowired
	ReportBoardImp reportBoardImp;

	@Autowired
	RsReportMapper rsReportMapper;

	@Autowired
	CacheUtil cacheUtil;

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
		try {
			// 插入日志
			logDebugImp.add(boardId, " : fetch  进入");
			// 根据 boardId(reportId) 取出RsReport
			RsReport rsReport = rsReportMapper.selectByReportId(boardId);
			// 取出 id_emp 和 ID_GRUP
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
					mDashboard.setTemplate_id(Integer.parseInt(templateId));
					mDashboard.setGroup_id(client);
					mdashboardMapper.addByBean(mDashboard);
					Integer id = mDashboard.getId();
					// 调用刷新内存
					cacheUtil.refreshMDashboardone(id);
					// 重新取出
					mDashboard = CacheUtil.mDashboard.get(templateId + client);
				}
				rest.put("mDashboard", mDashboard);
				rest.put("tDashboard", tDashboard);
			} else {
				// 自己
				Tdashboard tDashboard = CacheUtil.tDashboard.get(rsReport.getPage_id());
				rest.put("mDashboard", tDashboard);
			}

			List<Mcharts> mCharts = CacheUtil.mCharts.get(templateId);
			rest.put("mCharts", mCharts);
			rest.put("user_type", user_type);
			rest.put("user_auth", user_auth);
			rest.put("success", "success");
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
		}
		return rest.toJSONString();
	}

	/**
	 * reportBoard 第一次查询所有的数据
	 * 
	 * @param boardId就是reportid
	 * 
	 * @param activeName
	 *            children里子报表的name
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/fetchData", method = RequestMethod.POST)
	public String fetchData(String params) {
		JSONObject object = JSON.parseObject(params);
		JSONObject rest = new JSONObject();
		try {
			// 根据 boardId(reportId) 取出RsReport
			String boardId = object.getString("boardId");
			Mdashboard dashboard = JSON.parseObject(object.getJSONObject("mDashboard").toString(), Mdashboard.class);
			RsReport rsReport = rsReportMapper.selectByReportId(boardId);
			// 图表 数据查询
			Map<String, Object> dataList = new HashMap<String, Object>();
			dataList = reportBoardImp.getAllDate(dashboard, rsReport);
			rest.put("dataList", dataList);
			rest.put("success", "success");
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
		}
		return rest.toJSONString();
	}

	@RequestMapping(value = "/fetchEdit", method = RequestMethod.POST)
	public String fetchEdit(String boardId) {
		JSONObject rest = new JSONObject();
		try {
			// 根据 boardId(reportId) 取出RsReport
			RsReport rsReport = rsReportMapper.selectByReportId(boardId);
			List<Mcharts> mCharts = CacheUtil.mCharts.get(rsReport.getPage_id());
			// 查询搜索框所拥有的子组件 的 数据
			JSONObject searchItems = new JSONObject();
			for (Mcharts mchart : mCharts) {
				String config = mchart.getConfig();
				JSONObject object = JSON.parseObject(config);
				String type = object.getString("type");
				if (type.equals("11")) {
					String[] searchItem = object.getString("searchItem").split(",");
					for (String id : searchItem) {
						// 根据 id 查rs_column_config表
						RsColumnConf item = rsColumnConfMapper.selectOneById(Integer.parseInt(id));
						searchItems.put(id, item);
					}
				}
			}

			// 查询每个图表所拥有的 维度 度量图例 和 搜索框的 子组件 所在 字段 的对应的表数据
			JSONObject idColumns = new JSONObject();
			for (Mcharts mchart : mCharts) {
				String config = mchart.getConfig();
				JSONObject object = JSON.parseObject(config);
				String type = object.getString("type");
				if (type.equals("0") || type.equals("1")) { // 折线图 柱状图
					String[] idColumn = (object.getString("dimension") + "," + object.getString("measure") + ","
							+ object.getString("color")).split(",");
					for (String id : idColumn) {
						// 根据 id 查rs_column_config表
						RsColumnConf item = rsColumnConfMapper.selectOneById(Integer.parseInt(id));
						idColumns.put(id, item);
					}
				} else if (type.equals("2")) { // 饼图
					String[] idColumn = (object.getString("dimension") + "," + object.getString("color")).split(",");
					for (String id : idColumn) {
						// 根据 id 查rs_column_config表
						RsColumnConf item = rsColumnConfMapper.selectOneById(Integer.parseInt(id));
						idColumns.put(id, item);
					}
				} else if (type.equals("3")) { // 交叉表
					String[] idColumn = object.getString("column").split(",");
					for (String id : idColumn) {
						// 根据 id 查rs_column_config表
						RsColumnConf item = rsColumnConfMapper.selectOneById(Integer.parseInt(id));
						idColumns.put(id, item);
					}
				} else if (type.equals("11")) { // 搜索框
					String[] idColumn = object.getString("searchItem").split(",");
					for (String id : idColumn) {
						// 根据 id 查rs_column_config表
						RsColumnConf item = rsColumnConfMapper.selectOneById(Integer.parseInt(id));
						idColumns.put(id, item);
					}
				}
			}
			// 查询每个图表 所拥有的 数据集 的 所有字段 的表数据
			JSONObject tableIdColumns = new JSONObject();
			String sameDataSetName = "";
			for (Mcharts mchart : mCharts) {
				String config = mchart.getConfig();
				JSONObject object = JSON.parseObject(config);
				String dataSetName = object.getString("dataSetName");
				if (!sameDataSetName.contains(dataSetName)) {
					RsTableConf rsTableConf = rsTableConfMapper.selectByName(dataSetName).get(0);
					List<RsColumnConf> tableIdColumn = rsColumnConfMapper.selectByTableId(rsTableConf.getId());
					tableIdColumns.put(dataSetName, tableIdColumn);
				}
				sameDataSetName = sameDataSetName + dataSetName;
			}

			rest.put("searchItems", searchItems);
			rest.put("idColumns", idColumns);
			rest.put("tableIdColumns", tableIdColumns);
			rest.put("success", "success");
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
		}
		return rest.toJSONString();
	}

	/**
	 * @图表关联请求数据
	 * @点击搜索框请求数据
	 * 
	 * @props fatherId
	 * @props chartName 关联图表uuuid
	 * @props columnId 关联的字段id
	 * @props value 参数值
	 * 
	 * @serialData 20180927
	 * @author wangliu
	 * 
	 */
	@RequestMapping(value = "/searchDate", method = RequestMethod.POST)
	public String searchDate(String params) {
		JSONObject object = JSON.parseObject(params);
		JSONObject rest = new JSONObject();
		// 图表数据集合
		Map<String, Object> dataList = new HashMap<String, Object>();
		try {
			Mdashboard dashboard = JSON.parseObject(object.getJSONObject("mDashboard").toString(), Mdashboard.class);
			String boardId = object.getString("boardId");
			// 根据 boardId(reportId) 取出RsReport
			RsReport rsReport = rsReportMapper.selectByReportId(boardId);
			JSONArray valueArray = object.getJSONArray("value");
			String[] value = new String[3];
			if (null != valueArray && valueArray.size() > 1) {
				value[0] = valueArray.getString(0);// 图表维度的字段 id
				value[1] = valueArray.getString(1);// 值
				value[2] = valueArray.getString(2);// 图表的名称(mchart表id)
			}
			// 请求search数据
			dataList = reportBoardImp.getSearchDate(dashboard, value, rsReport);
			rest.put("dataList", dataList);
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
		}
		return rest.toJSONString();
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
				mdashboardMapper.updateById(Integer.parseInt(id), style_config);
				// 跟新完刷新缓存
				cacheUtil.refreshMDashboardone(Integer.parseInt(id));
			} else if (dashboard_type.equals("self")) {// 自己
				tdashboardMapper.updateById(Integer.parseInt(id), style_config);
				// 跟新完刷新缓存
				cacheUtil.refreshTDashboardone(Integer.parseInt(id));
			}
			rest.put("success", "success");
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
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
			dataList = reportBoardImp.searchItemData(id, rsReport);
			map.put(id, dataList);
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
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
			Mdashboard mDashboard = mdashboardMapper.selectById(Integer.parseInt(id));
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
			mdashboardMapper.updateById(Integer.parseInt(id), style_config_save);// 数据库更新
			// 跟新完刷新缓存
			cacheUtil.refreshMDashboardone(Integer.parseInt(id));
			rest.put("success", "true");
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
		}
		return rest.toJSONString();
	}

}
