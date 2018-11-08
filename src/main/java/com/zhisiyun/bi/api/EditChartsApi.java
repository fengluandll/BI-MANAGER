package com.zhisiyun.bi.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhisiyun.bi.bean.db.DbParameters;
import com.zhisiyun.bi.bean.db.JTableParam;
import com.zhisiyun.bi.bean.db.JTableResult;
import com.zhisiyun.bi.bean.defaultBean.Mcharts;
import com.zhisiyun.bi.bean.defaultBean.Mdashboard;
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.defaultDao.JdbcDao;
import com.zhisiyun.bi.defaultDao.MchartsMapper;
import com.zhisiyun.bi.defaultDao.MdashboardMapper;
import com.zhisiyun.bi.defaultDao.RsColumnConfMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;
import com.zhisiyun.bi.utils.SqlUtils;

@RestController
@RequestMapping("api/edit")
public class EditChartsApi {

	@Autowired
	private MchartsMapper mChartsMapper;

	@Autowired
	private RsColumnConfMapper rsColumnConfMapper;

	@Autowired
	private RsTableConfMapper rsTableConfMapper;

	@Autowired
	private MdashboardMapper mdashboardMapper;

	@Autowired
	private JdbcDao jdbcDao;

	/******************** chartList 列表 **************************/

	/**
	 * 查出所有的mcharts 列表
	 * 
	 * @param boardid:dashboard的id
	 * 
	 * @date 20181019
	 * @return
	 */
	@RequestMapping(value = "/getChartList", method = RequestMethod.POST)
	public JTableResult getChartList(String boardId, JTableParam jTableParam) {
		DbParameters<String> dbParameters = new DbParameters<String>();
		JTableResult jTableResult = new JTableResult();
		dbParameters.addParam("boardId", boardId);
		dbParameters.setPageSize(jTableParam.getLength());
		dbParameters.setOffset(jTableParam.getStart());
		dbParameters.setPaging(true);
		try {
			List<Mcharts> chartList = null;
			if (null != boardId && !"".equals(boardId)) {
				chartList = mChartsMapper.selectById(Integer.parseInt(boardId));
			} else {
				chartList = mChartsMapper.selectAll();
			}
			jTableResult.setDraw(jTableParam.getDraw());
			jTableResult.setRecordsTotal(dbParameters.getTotalRecord());
			jTableResult.setRecordsFiltered(dbParameters.getTotalRecord());
			jTableResult.setData(chartList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jTableResult;
	}

	/************************************************************************************/
	/**
	 * 图表编辑保存
	 * 
	 * @param id
	 * @param config
	 * 
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map<String, Object> update(String id, String config) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 获取去chart表的 name
			JSONObject object = JSON.parseObject(config);
			String name = object.getString("name");
			if ("".equals(name)) {
				name = id;
			}
			mChartsMapper.updateById(Integer.parseInt(id), name, config);
			map.put("success", "success");
		} catch (Exception e) {
			map.put("success", "false");
		}
		return map;
	}

	/**
	 * 图表编辑新建
	 * 
	 * @param id
	 * @param config
	 * 
	 * @return
	 */
	@RequestMapping(value = "/newChart", method = RequestMethod.POST)
	public Map<String, Object> newChart(String id, String config) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 获取去chart表的 name
			JSONObject object = JSON.parseObject(config);
			String name = object.getString("name");
			String type = object.getString("type");
			if ("".equals(name)) {
				if (null != type) {
					if (type.equals("0")) {
						name = "新建" + id + "Chart" + type;
					} else if (type.equals("3")) {
						name = "新建" + id + "Table" + type;
					} else if (type.equals("11")) {
						name = "新建" + id + "Search" + type;
					}
				} else {
					name = "新建" + id + "组件";
				}
			}
			mChartsMapper.newChart(Integer.parseInt(id), name, config);
			map.put("success", "success");
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 图表编辑删除
	 * 
	 * @param id
	 * @param config
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteChart", method = RequestMethod.POST)
	public Map<String, Object> deleteChart(String id, String dashboard_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (null != id && null != dashboard_id) {
				mChartsMapper.deleteChart(Integer.parseInt(id), Integer.parseInt(dashboard_id));
			}
			map.put("success", "success");
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 根据table name 请求 数据集 的 字段
	 * 
	 * @param ds_name
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getColumns", method = RequestMethod.POST)
	public Map<String, Object> getColumns(String ds_name) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<RsColumnConf> rsColumnConfList = null;
		try {
			// 根据 ds_name 查询 id
			RsTableConf rsTableConf = rsTableConfMapper.selectByName(ds_name).get(0);
			// 查询数据集 的 字段
			rsColumnConfList = rsColumnConfMapper.selectByTableId(rsTableConf.getId());
			map.put("list", rsColumnConfList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 根据 id list 请求 数据集 的 字段
	 * 
	 * @param id
	 * @date 20180921
	 * @return
	 */
	@RequestMapping(value = "/getColumnsByIdList", method = RequestMethod.POST)
	public Map<String, Object> getColumnsByIdList(String idList) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<RsColumnConf> rsColumnConfList = null;
		try {
			List<Integer> idStr = new ArrayList<Integer>();
			String[] array = idList.split(",");
			for (String id : array) {
				if ("" != id) {
					idStr.add(Integer.parseInt(id));
				}
			}
			// 查询数据集 的 字段
			rsColumnConfList = rsColumnConfMapper.selectByIds(idStr);
			map.put("list", rsColumnConfList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 根据id 获取 字符集表
	 * 
	 * @param id
	 * @date 20180921
	 * @return
	 */
	@RequestMapping(value = "/getColumnsById", method = RequestMethod.POST)
	public Map<String, Object> getColumnsById(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		RsColumnConf rsColumnConf = null;
		try {
			rsColumnConf = rsColumnConfMapper.selectOneById(Integer.parseInt(id));
			map.put("rsColumnConf", rsColumnConf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/******************* 报表列表 *****************************/
	/**
	 * 查出所有的m_dashboard 列表
	 * 
	 * @param boardName:dashboard名称
	 * 
	 * @date 20181019
	 * @return
	 */
	@RequestMapping(value = "/getMDashboardList", method = RequestMethod.POST)
	public JTableResult getMDashboardList(String boardName, JTableParam jTableParam) {
		DbParameters<String> dbParameters = new DbParameters<String>();
		JTableResult jTableResult = new JTableResult();
		dbParameters.addParam("roleName", boardName);
		dbParameters.setPageSize(jTableParam.getLength());
		dbParameters.setOffset(jTableParam.getStart());
		dbParameters.setPaging(true);
		try {
			List<Mdashboard> mdashboardList = null;
			if (null != boardName && !"".equals(boardName)) {
				mdashboardList = mdashboardMapper.selectByName(boardName);
			} else {
				mdashboardList = mdashboardMapper.selectAll();
			}
			jTableResult.setDraw(jTableParam.getDraw());
			jTableResult.setRecordsTotal(dbParameters.getTotalRecord());
			jTableResult.setRecordsFiltered(dbParameters.getTotalRecord());
			jTableResult.setData(mdashboardList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jTableResult;
	}

	/**
	 * @see dashboard保存
	 * @author wangliu
	 * @serialData 20181019
	 **/
	@RequestMapping(value = "/saveDashboard", method = RequestMethod.POST)
	public void saveDashboard(String name) throws Exception {
		try {
			Mdashboard mdashboard = new Mdashboard();
			mdashboard.setName(name);
			// 创建一个mdashboard
			mdashboardMapper.addByBean(mdashboard);
			// 根据boardid 创建一个 chart
			Mcharts mcharts = new Mcharts();
			Integer boardId = mdashboard.getId();
			String chartName = "搜索框" + boardId;
			String config = "{\"name\":\"\",\"type\":\"11\",\"dataSetName\":\"\",\"dataSearch\":\"\",\"searchItem\":\"\",\"padding\":\"\",\"searchJson\":{}}";
			mcharts.setDashboard_id(boardId);
			mcharts.setName(chartName);
			mcharts.setConfig(config);
			mChartsMapper.newChartByBean(mcharts);
			// 跟新mdashboard中的style_config
			Integer chartId = mcharts.getId();
			String style_config = "{\"name\":\"\",\"type\":\"root\",\"children\":[{\"name\":\"" + chartId
					+ "\",\"type\":\"search\",\"chartId\":\"" + chartId
					+ "\",\"fatherName\":\"root\",\"styleConfig\":\"\",\"relation\":{}}],\"dragactStyle\":[{\"GridX\":0,\"GridY\":0,\"w\":39,\"h\":2,\"key\":\""
					+ chartId + "\",\"type\":\"search\"}]}";
			mdashboardMapper.updateById(boardId, style_config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/************ react 请求 ************/

	/**
	 * 请求 图表的 数据
	 * 
	 * @param chartId
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/findType", method = RequestMethod.POST)
	public String findType(String chartId) {
		JSONObject rest = new JSONObject();
		try {
			// 根据 chartId 查询出表 m_charts表
			Mcharts mCharts = mChartsMapper.selectOneById(Integer.parseInt(chartId));
			// 解析出param
			String config = mCharts.getConfig();
			JSONObject object = JSON.parseObject(config);
			String type = object.getString("type");
			rest.put("type", type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rest.toJSONString();
	}

	/**
	 * 请求 图表的 数据
	 * 
	 * @param chartId
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/findChartDate", method = RequestMethod.POST)
	public String findChartDate(String chartId, String json) {
		JSONObject rest = new JSONObject();
		List list = new ArrayList();
		try {
			// 根据 chartId 查询出表 m_charts表
			Mcharts mCharts = mChartsMapper.selectOneById(Integer.parseInt(chartId));
			// 解析出param
			String config = mCharts.getConfig();
			JSONObject object = JSON.parseObject(config);
			String dataSetName = object.getString("dataSetName");
			String dimension = object.getString("dimension").split(",")[0];
			String measure = object.getString("measure").split(",")[0];
			String legend = object.getString("color").split(",")[0];

			RsColumnConf dimensionObj = rsColumnConfMapper.selectOneById(Integer.parseInt(dimension));
			RsColumnConf measureObj = rsColumnConfMapper.selectOneById(Integer.parseInt(measure));
			RsColumnConf legendObj = null;
			if (null != legend && !"".equals(legend)) {
				legendObj = rsColumnConfMapper.selectOneById(Integer.parseInt(legend));
			}

			// where 条件
			JSONObject jsonObj = JSON.parseObject(json);

			// 拼接查询的sql
			SqlUtils sqlUtils = new SqlUtils();
			Map map = new HashMap();
			String sql = sqlUtils.assemble(dataSetName, dimensionObj, measureObj, legendObj, jsonObj, map);
			System.out.println("编辑chart sql: "+sql);
			list = jdbcDao.query(sql, map);
			if (list.size() > 10) {
				list = list.subList(0, 10);
			}
			// 调用查询过程
			rest.put("list", list);
			rest.put("mCharts", mCharts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rest.toJSONString();
	}

	/**
	 * 请求 table明细的 数据
	 * 
	 * @param chartId
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/findTableDate", method = RequestMethod.POST)
	public String findTableDate(String chartId, String json) {
		JSONObject rest = new JSONObject();
		List<Map<String, Object>> list = new ArrayList();
		Map<String, Object> data = new HashMap<>();
		List<String> headers = new ArrayList();
		List body = new ArrayList();
		data.put("header", headers);
		data.put("body", body);
		try {
			// 根据 chartId 查询出表 m_charts表
			Mcharts mCharts = mChartsMapper.selectOneById(Integer.parseInt(chartId));
			// 解析出param
			String config = mCharts.getConfig();
			JSONObject object = JSON.parseObject(config);
			String column = object.getString("column");
			String dataSetName = object.getString("dataSetName");
			List<Integer> ids = new ArrayList<Integer>();
			String[] array = column.split(",");
			for (String id : array) {
				if ("" != id) {
					ids.add(Integer.parseInt(id));
				}
			}
			List<RsColumnConf> rsColumnConfList = rsColumnConfMapper.selectByIds(ids);
			// 拼接查询的sql
			SqlUtils sqlUtils = new SqlUtils();
			Map map = new HashMap();
			String sql = sqlUtils.assemble(dataSetName, rsColumnConfList, map, headers);
			System.out.println("编辑table sql: "+sql);
			list = jdbcDao.query(sql, map);

			List row;
			if (list.size() > 0) {
				Map<String, RsColumnConf> meaMap = new HashMap<>();
				for (RsColumnConf rsColumnConf : rsColumnConfList) {
					meaMap.put(rsColumnConf.getRsc_display(), rsColumnConf);
				}
				RsColumnConf rsColumnConf;
				Object val;
				for (Map<String, Object> m : list) {
					row = new ArrayList();
					for (String header : headers) {
						// 判断是否是度量 and 数值类型
						rsColumnConf = meaMap.get(header);
						val = m.get(header);
						if (val != null && rsColumnConf != null && rsColumnConf.getRsc_type() == 1) {
							// 是否存在单位换算值
							if (rsColumnConf.getRsc_conversion() != null) {
								val = new BigDecimal(String.valueOf(val))
										.multiply(new BigDecimal(rsColumnConf.getRsc_conversion()));
							}
							// 是否存在数值格式化公式
							if (StringUtils.isNotEmpty(rsColumnConf.getRsc_formatter())) {
								val = new DecimalFormat(rsColumnConf.getRsc_formatter()).format(val);
							}
						}
						row.add(val);
					}
					body.add(row);
				}
			}
			rest.put("mCharts", mCharts);
			rest.put("list", data);
		} catch (Exception e) {
			rest.put("result", "error");
			rest.put("list", data);
			e.printStackTrace();
		}

		return rest.toJSONString();
	}

}
