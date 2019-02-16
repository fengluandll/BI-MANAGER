package com.zhisiyun.bi.api;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.defaultDao.JdbcDao;
import com.zhisiyun.bi.defaultDao.MchartsMapper;
import com.zhisiyun.bi.defaultDao.RsColumnConfMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;
import com.zhisiyun.bi.utils.CacheUtil;
import com.zhisiyun.bi.utils.MD5Uitls;
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
	private JdbcDao jdbcDao;

	@Autowired
	CacheUtil cacheUtil;

	private static Logger log = LoggerFactory.getLogger(ReportBoardApi.class);

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
				chartList = mChartsMapper.selectById(boardId);
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
	 * @param id:唯一键Id
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
			String type = object.getString("type");
			if ("".equals(name)) {
				name = id;
			}
			Mcharts mcharts = mChartsMapper.selectOneById(id);// 根据唯一键找一个
			String is_active = mcharts.getIs_active();
			if (is_active.equals("Y")) {
				Mcharts m = new Mcharts();
				m.setId(id);
				m.setName(name);
				m.setMc_type(Integer.parseInt(type));
				m.setConfig(config);
				mChartsMapper.updateById(m); // 如果是可用状态就直接更新config和name
				// 刷新缓存
				cacheUtil.refreshMChartsOne(id);
			} else if (is_active.equals("N")) {
				Mcharts m = new Mcharts();
				m.setId(id);
				m.setName(name);
				m.setMc_type(Integer.parseInt(type));
				m.setConfig(config);
				mChartsMapper.updateById(m);
				String md5_id = MD5Uitls.getMD5Id(mcharts.getDashboard_id() + config); // 不是可用状态就生成唯一键
				mChartsMapper.updateBySnId(mcharts.getSn_id(), md5_id, "Y"); // 根据流水号Id更新唯一键Id
				// 刷新缓存
				cacheUtil.refreshMChartsOne(md5_id);
			}
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
			/***
			 * 由于现在的逻辑是拿config算md5所以刚新建的时候大家的md5id是一样的,要等mchart编辑之后才会改变.现在的逻辑就是先拿md5id判断下如果有相同的就返回
			 ***/
			String mchart_id = MD5Uitls.getMD5Id(id + config); // 根据config生成mcharts的唯一键
			Mcharts mcharts_old = mChartsMapper.selectOneById(mchart_id); // 根据md5id查询
			if (null != mcharts_old) {
				map.put("success", "false");
				return map;
			}
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
					} else if (type.equals("4")) {
						name = "新建" + id + "pivottable" + type;
					} else if (type.equals("5")) {
						name = "新建" + id + "perspective" + type;
					} else if (type.equals("6")) {
						name = "新建" + id + "text" + type;
					} else if (type.equals("21")) {
						name = "新建" + id + "antdtable" + type;
					} else if (type.equals("11")) {
						name = "新建" + id + "Search" + type;
					}
				} else {
					name = "新建" + id + "组件";
				}
			}
			Mcharts mcharts = new Mcharts();
			mcharts.setDashboard_id(id);
			mcharts.setName(name);
			mcharts.setMc_type(Integer.parseInt(type));
			mcharts.setConfig(config);
			mcharts.setId("tmp"); // 防止不为空报错
			mcharts.setIs_active("N"); // 一开始设置为N等第一次保存的时候改为Y
			mChartsMapper.newChartByBean(mcharts);
			Integer sn_id = mcharts.getSn_id();

			mChartsMapper.updateBySnId(sn_id, mchart_id, "N"); // 插入mcharts的唯一键
			// 刷新缓存
			cacheUtil.refreshMChartsOne(mchart_id);
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
	public Map<String, Object> deleteChart(String sn_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (null != sn_id) {
				mChartsMapper.deleteChartBySnId(Integer.parseInt(sn_id));
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
			List<String> idStr = new ArrayList<String>();
			String[] array = idList.split(",");
			for (String id : array) {
				if ("" != id) {
					idStr.add(id);
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
			rsColumnConf = rsColumnConfMapper.selectOneById(id);
			map.put("rsColumnConf", rsColumnConf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/*************** mCharts方法 *******************/

	/**
	 * 获取t_dashbaord的搜索框mchart_id
	 * 
	 * @param t_dashboard_id
	 * @return m_chart_id
	 */
	@RequestMapping(value = "/getOnlySearchId", method = RequestMethod.POST)
	public Map<String, Object> getOnlySearchId(String t_dashboard_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Mcharts search = mChartsMapper.selectSearchByBoardId(t_dashboard_id);
			map.put("sn_id", search.getSn_id());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/************ react 请求 ************/

	/**
	 * 请求 图表的 数据
	 * 
	 * @param chartId
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/findMcharts", method = RequestMethod.POST)
	public String findMcharts(String chartId) {
		JSONObject rest = new JSONObject();
		try {
			// 根据 chartId 查询出表 m_charts表
			Mcharts mCharts = mChartsMapper.selectOneById(chartId);
			rest.put("mCharts", mCharts);
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
			Mcharts mCharts = mChartsMapper.selectOneById(chartId);
			// 解析出param
			String config = mCharts.getConfig();
			JSONObject object = JSON.parseObject(config);
			String dataSetName = object.getString("dataSetName");
			String dimension = object.getString("dimension").split(",")[0];
			String measure = object.getString("measure").split(",")[0];
			String legend = object.getString("color").split(",")[0];

			RsColumnConf dimensionObj = rsColumnConfMapper.selectOneById(dimension);
			RsColumnConf measureObj = rsColumnConfMapper.selectOneById(measure);
			RsColumnConf legendObj = null;
			if (null != legend && !"".equals(legend)) {
				legendObj = rsColumnConfMapper.selectOneById(legend);
			}

			// where 条件
			JSONObject jsonObj = JSON.parseObject(json);

			// 拼接查询的sql
			SqlUtils sqlUtils = new SqlUtils();
			Map map = new HashMap();
			String sql = sqlUtils.assemble(dataSetName, dimensionObj, measureObj, legendObj, jsonObj, map);
			log.info("编辑chart sql: " + sql);
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
			Mcharts mCharts = mChartsMapper.selectOneById(chartId);
			// 解析出param
			String config = mCharts.getConfig();
			JSONObject object = JSON.parseObject(config);
			String column = object.getString("column");
			String dataSetName = object.getString("dataSetName");
			List<String> ids = new ArrayList<String>();
			String[] array = column.split(",");
			for (String id : array) {
				if ("" != id) {
					ids.add(id);
				}
			}
			List<RsColumnConf> rsColumnConfList = rsColumnConfMapper.selectByIds(ids);
			// 拼接查询的sql
			SqlUtils sqlUtils = new SqlUtils();
			Map map = new HashMap();
			String sql = sqlUtils.assemble(dataSetName, rsColumnConfList, map, headers);
			log.info("编辑table sql: " + sql);
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
