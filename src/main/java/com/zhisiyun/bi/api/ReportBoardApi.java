package com.zhisiyun.bi.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;
import com.zhisiyun.bi.bean.defaultBean.RsReport;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.defaultDao.JdbcDao;
import com.zhisiyun.bi.defaultDao.MchartsMapper;
import com.zhisiyun.bi.defaultDao.MdashboardMapper;
import com.zhisiyun.bi.defaultDao.RsColumnConfMapper;
import com.zhisiyun.bi.defaultDao.RsReportMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;
import com.zhisiyun.bi.serviceImp.ReportBoardImp;

@RestController
@RequestMapping("api/reportBoard")
public class ReportBoardApi {
	@Autowired
	private MchartsMapper mChartsMapper;

	@Autowired
	private MdashboardMapper mdashboardMapper;

	@Autowired
	private RsColumnConfMapper rsColumnConfMapper;

	@Autowired
	private RsTableConfMapper rsTableConfMapper;

	@Autowired
	private JdbcDao jdbcDao;

	@Autowired
	ReportBoardImp reportBoardImp;

	@Autowired
	RsReportMapper rsReportMapper;

	public static String SEPARTOR = ",";
	@Value("${report.url}")
	private String REPORT_URL;

	/**
	 * 调用的api接口
	 * 
	 * @param pageId、ID_GRUP
	 * 
	 * @return
	 */
	@RequestMapping(value = "/report", method = RequestMethod.POST)
	public String report(HttpServletRequest req) {
		try {
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
			String ID_ORG = req.getParameter("ID_GRUP");
			if (ID_ORG != null) {
				params.put("ID_GRUP", ID_ORG.split(SEPARTOR));
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
			rsReportMapper.add(reportId, pageId, params.toJSONString());
			return REPORT_URL + reportId;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * reportBoard 第一次 全部加载数据
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
		// 根据 boardId(reportId) 取出RsReport
		RsReport rsReport = rsReportMapper.selectByReportId(boardId);
		try {
			// 查询 m_dashboard 表
			Mdashboard mDashboard = mdashboardMapper.selectById(Integer.parseInt(rsReport.getPage_id()));
			// 查询 m_charts 表
			List<Mcharts> mCharts = mChartsMapper.selectById(Integer.parseInt(rsReport.getPage_id()));
			// 图表 数据查询
			Map<String, Object> dataList = new HashMap<String, Object>();
			dataList = reportBoardImp.getAllDate(mDashboard, null, rsReport);

			rest.put("mDashboard", mDashboard);
			rest.put("mCharts", mCharts);
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
			// 查询 m_dashboard 表
			Mdashboard mDashboard = mdashboardMapper.selectById(Integer.parseInt(rsReport.getPage_id()));
			// 查询 m_charts 表
			List<Mcharts> mCharts = mChartsMapper.selectById(Integer.parseInt(rsReport.getPage_id()));
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

			rest.put("mDashboard", mDashboard);
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
			JSONObject dashboard = object.getJSONObject("mdashboard");
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
			JSONObject style_config = JSON.parseObject(dashboard.getString("style_config"));
			JSONArray childrenArray = style_config.getJSONArray("children");

			// 请求search数据
			dataList = reportBoardImp.getSearchDate(childrenArray, value, rsReport);

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
	public String saveBoard(String mDashboard) {
		JSONObject rest = new JSONObject();
		try {
			JSONObject object = JSON.parseObject(mDashboard);
			JSONObject dashboard = object.getJSONObject("mDashboard_porp");
			String id = dashboard.getString("id");
			String style_config = dashboard.getString("style_config");
			mdashboardMapper.updateById(Integer.parseInt(id), style_config);
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
	public String searchItemData(@RequestParam(value = "id", required = true) String id, String boardId) {
		JSONObject rest = new JSONObject();
		// 图表数据集合
		List dataList = new ArrayList();
		try {
			// 根据 boardId(reportId) 取出RsReport
			RsReport rsReport = rsReportMapper.selectByReportId(boardId);
			dataList = reportBoardImp.searchItemData(id, rsReport);
			rest.put("list", dataList);
		} catch (Exception e) {
			rest.put("success", "false");
			e.printStackTrace();
		}
		return rest.toJSONString();
	}

}
