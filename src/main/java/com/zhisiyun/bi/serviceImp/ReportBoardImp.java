package com.zhisiyun.bi.serviceImp;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhisiyun.bi.api.ReportBoardApi;
import com.zhisiyun.bi.bean.defaultBean.Mcharts;
import com.zhisiyun.bi.bean.defaultBean.Mdashboard;
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;
import com.zhisiyun.bi.bean.defaultBean.RsReport;
import com.zhisiyun.bi.bean.defaultBean.RsTableConf;
import com.zhisiyun.bi.bean.reportPro.MchartsPro;
import com.zhisiyun.bi.bean.reportPro.PropsBean;
import com.zhisiyun.bi.bean.reportPro.SearchBoardJson;
import com.zhisiyun.bi.bean.reportPro.SearchRelation;
import com.zhisiyun.bi.defaultDao.JdbcDao;
import com.zhisiyun.bi.defaultDao.MchartsMapper;
import com.zhisiyun.bi.defaultDao.RsColumnConfMapper;
import com.zhisiyun.bi.defaultDao.RsTableConfMapper;
import com.zhisiyun.bi.utils.JsonUtils;

@Service
public class ReportBoardImp {

	@Autowired
	private MchartsMapper mChartsMapper;

	@Autowired
	private RsColumnConfMapper rsColumnConfMapper;

	@Autowired
	private JdbcDao jdbcDao;

	@Autowired
	private RsTableConfMapper rsTableConfMapper;

	static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private static Logger log = LoggerFactory.getLogger(ReportBoardApi.class);

	/**
	 * 获取所有的图表的数据
	 * 
	 **/
	// style_config
	// {name:name,type:root,children:[{name:uuuid,type:search,chartId:chartId,styleConfig:"",relation:{
	// id:{ label:,relationFields:,props:,
	// }},},{name:,type:tab,children:[{name:,title:,type:tab_panel,children:[{},{}]},{}]}]}
	public Map<String, Object> getAllDate(Mdashboard mDashboard, JSONObject idColumns, RsReport rsReport) {
		// 图表数据集合
		Map<String, Object> mapList = new HashMap<String, Object>();
		try {
			JSONObject style_config = JSON.parseObject(mDashboard.getStyle_config());
			JSONArray childrenArray = style_config.getJSONArray("children");
			JsonUtils jsonUtils = new JsonUtils();

			// 遍历 m_dashboard 获取 所有的 chart
			List<MchartsPro> mchartsProList = jsonUtils.getChartsJson(childrenArray);

			// 遍历所有的 搜索框 找出 fatherName 和 字段 props
			List<SearchBoardJson> searchBoardJsonList = jsonUtils.getSearches(childrenArray);

			Map<String, SearchRelation> relation = searchBoardJsonList.get(0).getRelation();

			// m_charts id
			String chartId = searchBoardJsonList.get(0).getChartId();
			Mcharts mchart = mChartsMapper.selectOneById(Integer.parseInt(chartId));
			JSONObject searchJson = JSONObject
					.parseObject(JSON.parseObject(mchart.getConfig()).getString("searchJson"));

			// 循环图表 拼接 参数进行 查询
			for (MchartsPro chart : mchartsProList) {
				List<PropsBean> propsBeanList = new ArrayList<PropsBean>();

				// 循环search中的Item 拼接参数
				for (Map.Entry<String, SearchRelation> entry : relation.entrySet()) {
					Map<String, String[]> relationFields = entry.getValue().getRelationFields();
					String searchItemId = entry.getKey();// relation:{ id:{
															// label:,{"cjmncjsdhfkjsdjfk":[4490,4492,4495,4498,4501]},props:[],
															// }}, id
					for (Map.Entry<String, String[]> subEntry : relationFields.entrySet()) {
						String key = subEntry.getKey();
						String chartItemId = subEntry.getValue()[0];// relation:{ id:{
																	// label:,{"cjmncjsdhfkjsdjfk":[4490,4492,4495,4498,4501]},props:[],
																	// }}, cjmncjsdhfkjsdjfk

						// 判断 搜索item是否与图表有关联
						if (chart.getName().equals(key)) {
							// 根据searchItem 找 props
							String[] propsList = entry.getValue().getProps();

							// 根据 searchItem id 找
							JSONObject itemStyle = searchJson.getJSONObject(searchItemId);
							PropsBean propsBean = new PropsBean();
							propsBean.setSearchItemId(searchItemId);
							propsBean.setChartItemId(chartItemId);
							propsBean.setPropsList(propsList);
							propsBean.setItemStyle(itemStyle);
							propsBeanList.add(propsBean);
							break; // 每个searchItem 找到了 对应的图表就应该去 循环下一个item
						}
					}

				}

				// 下面拼接 sql 进行查询 到 service层
				if (chart.getType().equals("line") || chart.getType().equals("bar") || chart.getType().equals("pie")) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					list = this.getChartDate(chart, propsBeanList, new String[0], rsReport);
					// Map中放入 chart 的uuuuid 和 数据List
					mapList.put(chart.getName(), list);
				} else if (chart.getType().equals("table")) {
					Map<String, Object> map = new HashMap<>();
					map = this.getTableDate(chart, propsBeanList, new String[0], rsReport);
					mapList.put(chart.getName(), map);
				}
			}
			// 执行sql

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapList;
	}

	/**
	 * 请求获取 search的数据
	 * 
	 **/
	public Map<String, Object> getSearchDate(JSONArray array, String[] prop, RsReport rsReport) {
		// 图表数据集合
		Map<String, Object> mapList = new HashMap<String, Object>();
		try {
			JsonUtils jsonUtils = new JsonUtils();
			// 遍历 m_dashboard 获取 所有的 chart
			List<MchartsPro> mchartsProList = jsonUtils.getChartsJson(array);

			// 遍历所有的 搜索框 找出 fatherName 和 字段 props
			List<SearchBoardJson> searchBoardJsonList = jsonUtils.getSearches(array);

			// 获取 search 里面的参数啦
			Map<String, SearchRelation> relation = searchBoardJsonList.get(0).getRelation();

			// m_charts id
			String chartId = searchBoardJsonList.get(0).getChartId();
			Mcharts mchart = mChartsMapper.selectOneById(Integer.parseInt(chartId));
			JSONObject searchJson = JSONObject
					.parseObject(JSON.parseObject(mchart.getConfig()).getString("searchJson"));

			// 如果是点击图表plot查询知道到图表的ralation,并找出关联查询的其他图表的id
			List<String[]> plotCharts = jsonUtils.getPlotClickCharts(array, prop[2], prop[0]);

			// 循环图表 拼接 参数进行 查询
			for (MchartsPro chart : mchartsProList) {
				List<PropsBean> propsBeanList = new ArrayList<PropsBean>();

				// 循环search中的Item 拼接参数
				for (Map.Entry<String, SearchRelation> entry : relation.entrySet()) {
					Map<String, String[]> relationFields = entry.getValue().getRelationFields();
					String searchItemId = entry.getKey();// relation:{
															// id:{label:,{"cjmncjsdhfkjsdjfk":[4490,4492,4495,4498,4501]},props:[],}},
															// id
					for (Map.Entry<String, String[]> subEntry : relationFields.entrySet()) {
						String key = subEntry.getKey();
						String chartItemId = subEntry.getValue()[0];// relation:{
																	// id:{label:,{"cjmncjsdhfkjsdjfk":[4490,4492,4495,4498,4501]},props:[],
																	// }}, cjmncjsdhfkjsdjfk
						// 判断 搜索item是否与图表有关联
						if (chart.getName().equals(key)) {
							// 根据searchItem 找 props
							String[] propsList = entry.getValue().getProps();

							// 根据 searchItem id 找
							JSONObject itemStyle = searchJson.getJSONObject(searchItemId);
							PropsBean propsBean = new PropsBean();
							propsBean.setSearchItemId(searchItemId);
							propsBean.setChartItemId(chartItemId);
							propsBean.setPropsList(propsList);
							propsBean.setItemStyle(itemStyle);
							propsBeanList.add(propsBean);
							break; // 每个searchItem 找到了 对应的图表就应该去 循环下一个item
						}
					}

				}
				// 查看是否是被plot查询关联的图表
				String[] propValue = new String[3];
				if (null != prop && prop.length == 3 && StringUtils.isNotEmpty(prop[0])
						&& StringUtils.isNotEmpty(prop[1]) && StringUtils.isNotEmpty(prop[2])) {
					for (String plotChart[] : plotCharts) {
						if (chart.getName().equals(plotChart[0])) {
							propValue[0] = plotChart[1]; // 图表维度的字段 id,现在要变成被关联的图表的字段id
							propValue[1] = prop[1];// 值
							propValue[2] = prop[2];// 图表的名称(mchart表id)
						}
					}
				}

				// 下面拼接 sql 进行查询 到 service层
				if (chart.getType().equals("line") || chart.getType().equals("bar") || chart.getType().equals("pie")) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					list = this.getChartDate(chart, propsBeanList, propValue, rsReport);
					// Map中放入 chart 的uuuuid 和 数据List
					mapList.put(chart.getName(), list);
				} else if (chart.getType().equals("table")) {
					Map<String, Object> map = new HashMap<>();
					map = this.getTableDate(chart, propsBeanList, propValue, rsReport);
					mapList.put(chart.getName(), map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapList;
	}

	/**
	 * @查询 chart的数据
	 * @author wangliu
	 * @time 20180928
	 * 
	 */
	public List<Map<String, Object>> getChartDate(MchartsPro chart, List<PropsBean> propsBeanList, String[] propValue,
			RsReport rsReport) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 根据 chart的数据集 拼接sql
		String chartId = chart.getChartId();
		Mcharts mcharts = mChartsMapper.selectOneById(Integer.parseInt(chartId));
		JSONObject obj = JSON.parseObject(mcharts.getConfig());
		String type = obj.getString("type");

		String dimension = obj.getString("dimension");
		String measure = obj.getString("measure");
		String color = obj.getString("color");
		String dataSetName = obj.getString("dataSetName");

		RsColumnConf dimensionBean = rsColumnConfMapper.selectOneById(Integer.parseInt(dimension));
		RsColumnConf measureBean = rsColumnConfMapper.selectOneById(Integer.parseInt(measure));
		RsColumnConf colorBean = null;
		if (null != color && !"".equals(color)) {
			colorBean = rsColumnConfMapper.selectOneById(Integer.parseInt(color));
		}
		StringBuilder builder = new StringBuilder("select ");
		if (colorBean != null) {
			builder.append(colorBean.getRsc_name());
			builder.append(" color, ");
		}
		if (!dimensionBean.getIs_calc().equals("Y") && dimensionBean.getRsc_type() == 3) {
			builder.append(" to_char(" + dimensionBean.getRsc_name() + ",'YYYY-MM-DD') ");
			builder.append(" x, ");
		} else {
			builder.append(dimensionBean.getRsc_name());
			builder.append(" x, ");
		}
		if (measureBean.getIs_calc().equals("Y")) {
			builder.append(measureBean.getRsc_name());
		} else {
			builder.append("count(1)");
		}
		builder.append(" y from ");
		builder.append(dataSetName);
		builder.append(" where 1=1 ");

		// 拼接where 参数
		this.getSqlWhere(builder, propsBeanList);

		// 拼接 点击图表的参数
		if (null != propValue && propValue.length == 3 && StringUtils.isNotEmpty(propValue[0])
				&& StringUtils.isNotEmpty(propValue[1])) {
			RsColumnConf clickItemIdBean = rsColumnConfMapper.selectOneById(Integer.parseInt(propValue[0]));
			String rsc_name = clickItemIdBean.getRsc_name();
			builder.append(" and " + rsc_name + " = '" + propValue[1] + "' ");
		}
		// HR系统param
		if (rsReport != null) {
			Map<String, Object> reportParams = mergeReportParams(rsReport);
			for (String key : reportParams.keySet()) {
				String jsonParam = null;
				JSONArray array = (JSONArray) reportParams.get(key);
				for (int i = 0; i < array.size(); i++) {
					String value = (String) array.get(i);
					jsonParam = value + ",";
				}
				jsonParam = jsonParam.substring(0, jsonParam.length() - 1);
				builder.append(" and " + key + " in ('" + jsonParam + "') ");
			}
		}

		// 按图例-维度分组
		builder.append(" group by ");
		if (colorBean != null) {
			builder.append(colorBean.getRsc_name() + ", ");
		}
		builder.append(dimensionBean.getRsc_name() + " ");

		String sql = builder.toString();
		log.info("图表查询sql: "+sql);

		// 开始进行数据库查询
		Map map = new HashMap();
		list = jdbcDao.query(sql, map);

		return list;
	}

	/**
	 * @查询 chart的数据
	 * @author wangliu
	 * @time 20180928
	 * 
	 */
	public Map<String, Object> getTableDate(MchartsPro chart, List<PropsBean> propsBeanList, String[] propValue,
			RsReport rsReport) {
		Map<String, Object> data = new HashMap<>();
		List<String> headers = new ArrayList();
		List body = new ArrayList();
		data.put("header", headers);
		data.put("body", body);
		try {
			// 根据 chart的数据集 拼接sql
			String chartId = chart.getChartId();
			Mcharts mcharts = mChartsMapper.selectOneById(Integer.parseInt(chartId));
			JSONObject obj = JSON.parseObject(mcharts.getConfig());
			String type = obj.getString("type");

			String column = obj.getString("column");
			String dataSetName = obj.getString("dataSetName");

			// column id
			List<Integer> ids = new ArrayList<Integer>();
			String[] array = column.split(",");
			for (String id : array) {
				if ("" != id) {
					ids.add(Integer.parseInt(id));
				}
			}
			List<RsColumnConf> rsColumnConfList = rsColumnConfMapper.selectByIds(ids);

			// 拼接sql
			StringBuilder builder = new StringBuilder("select ");
			List<String> cols = new ArrayList<>();
			for (RsColumnConf rs : rsColumnConfList) {
				// is_calc rsc_category 1维度 2度量
				if (rs.getIs_calc().equals("N") && rs.getRsc_category().equals(2)) {
					cols.add(" sum(" + rs.getRsc_name() + ") as " + rs.getRsc_display());
				} else {
					cols.add(rs.getRsc_name() + " as " + rs.getRsc_display());
				}
				headers.add(rs.getRsc_display());
			}
			builder.append(StringUtils.join(cols.toArray(), ","));
			builder.append(" from ");
			builder.append(dataSetName);
			builder.append(" where 1=1 ");

			// 拼接where 参数
			this.getSqlWhere(builder, propsBeanList);

			// 拼接 点击图表的参数
			if (null != propValue && propValue.length == 3 && StringUtils.isNotEmpty(propValue[0])
					&& StringUtils.isNotEmpty(propValue[1])) {
				RsColumnConf clickItemIdBean = rsColumnConfMapper.selectOneById(Integer.parseInt(propValue[0]));
				String rsc_name = clickItemIdBean.getRsc_name();
				builder.append(" and " + rsc_name + " = '" + propValue[1] + "' ");
			}
			// HR系统param
			if (rsReport != null) {
				Map<String, Object> reportParams = mergeReportParams(rsReport);
				for (String key : reportParams.keySet()) {
					String jsonParam = null;
					JSONArray arrayParam = (JSONArray) reportParams.get(key);
					for (int i = 0; i < arrayParam.size(); i++) {
						String value = (String) arrayParam.get(i);
						jsonParam = value + ",";
					}
					jsonParam = jsonParam.substring(0, jsonParam.length() - 1);
					builder.append(" and " + key + " in ('" + jsonParam + "') ");
				}
			}

			// group by
			if (rsColumnConfList != null && rsColumnConfList.size() > 0) {
				List<String> groupBy = new ArrayList<>();
				for (RsColumnConf col : rsColumnConfList) {
					if (col.getRsc_category().equals(1)) {// 只有维度可以group by 20181122
						groupBy.add(col.getRsc_name());
					}
				}
				if (groupBy.size() > 0) {
					builder.append(" group by ");
				}
				builder.append(StringUtils.join(groupBy, " , "));
			}

			String sql = builder.toString();
			log.info("交叉表查询sql: "+sql);

			// 开始进行数据库查询
			Map map = new HashMap();
			List<Map<String, Object>> list = jdbcDao.query(sql, map);

			// 添加body
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

		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	public void getSqlWhere(StringBuilder builder, List<PropsBean> propsBeanList) {
		for (PropsBean bean : propsBeanList) {
			JSONObject itemStyle = bean.getItemStyle();
			String itemType = itemStyle.getString("type");
			String chartItemId = bean.getChartItemId();
			RsColumnConf itemBean = rsColumnConfMapper.selectOneById(Integer.parseInt(chartItemId));

			// 参数
			String[] props = bean.getPropsList();
			switch (itemType) {
			// 数字
			case "1":
				String cal = itemStyle.getString("cal");
				String num_cal = "";
				switch (cal) {
				case "0":
					num_cal = ">";
					break;
				case "1":
					num_cal = ">=";
					break;
				case "2":
					num_cal = "<";
					break;
				case "3":
					num_cal = "<=";
					break;
				case "4":
					num_cal = "=";
					break;
				case "5":
					num_cal = "<>";
					break;
				}
				if (props != null && props.length > 0) {
					String prop = props[0];
					builder.append(" and " + itemBean.getRsc_name() + " " + num_cal + " " + prop);
				}
				break;
			// 字符串
			case "2":
				if (props != null && props.length > 0) {
					// 字符串参数 数组形式转换成 一个字符串
					String propTmp = "";
					for (int i = 0; i < props.length; i++) {
						propTmp = propTmp + "'" + props[i] + "',";
					}
					propTmp = propTmp.substring(0, propTmp.length() - 1);
					if (propTmp != null && !propTmp.equals("")) {
						builder.append(" and " + itemBean.getRsc_name() + " in ( " + propTmp + " ) ");
					}
				}
				break;
			// 日期
			case "3":
				String date_type = itemStyle.getString("date_type"); // 日周月年
				String time_type = itemStyle.getString("time_type"); // 绝对时间,相对时间
				String from_type = itemStyle.getString("from_type"); // 日期区间,日期

				Date date = new Date();
				String start = "";
				String end = "";
				if (null != props && props.length > 0) {
					// 有第一个时间的时候取第一个时间     服务器和本地差8小时可能要参考老代码进行修改 wangliu   20181123
					start = props[0].toString().substring(0, 10);
				} else {
					// 没有就取当前时间
					start = fmt.format(date);
				}
				if (null != props && props.length > 1) {
					end = props[1].substring(0, 10);
				} else {
					end = fmt.format(date);
				}
				String dateFmtType = "";
				if (date_type.equals("0")) {
					dateFmtType = "day";
				} else if (date_type.equals("1")) {
					dateFmtType = "week";
				} else if (date_type.equals("2")) {
					dateFmtType = "month";
				} else if (date_type.equals("3")) {
					dateFmtType = "year";
				}
				if (from_type.equals("0")) {
					// 日期区间
					builder.append(" and ");
					builder.append(" date_trunc( '" + dateFmtType + "' , " + itemBean.getRsc_name() + ") ");
					builder.append(" between date_trunc('" + dateFmtType + "', timestamp '" + start + "') ");
					builder.append(" and date_trunc('" + dateFmtType + "', timestamp '" + end + "') ");
				} else if (from_type.equals("1")) {
					builder.append(" and ");
					builder.append(" date_trunc('" + dateFmtType + "', " + itemBean.getRsc_name() + ") ");
					builder.append(" = date_trunc('" + dateFmtType + "', timestamp '" + start + "') ");
				}
				break;
			}
		}
	}

	private Map mergeReportParams(RsReport report) {
		JSONObject json = JSONObject.parseObject(report.getParams());
		Map<String, Object> map = new HashMap<>();
		for (String key : json.keySet()) {
			String paramKey = null;
			if (StringUtils.equals("ID_GRUP", key)) {
				paramKey = "id_grup";
			}
			if (StringUtils.equals("ID_COMP", key)) {
				paramKey = "id_comp";
			}
			if (StringUtils.equals("ID_ORG", key)) {
				paramKey = "id_org";
			}
			if (StringUtils.equals("ID_E_G_P", key)) {
				paramKey = "id_e_g_p";
			}
			if (StringUtils.equals("ID_E_G_S", key)) {
				paramKey = "id_e_g_s";
			}
			if (StringUtils.equals("ID_P_R_P", key)) {
				paramKey = "id_p_r_p";
			}
			if (StringUtils.equals("ID_P_R_S", key)) {
				paramKey = "id_p_r_s";
			}
			if (paramKey != null) {
				map.put(paramKey, json.getJSONArray(key));
			}
		}
		return map;
	}

	/***
	 * 
	 * 搜索框 子项str下拉列表数据
	 * 
	 **/

	public List searchItemData(String id, RsReport rsReport) {
		List restList = new ArrayList();
		try {
			// 根据id 查 rs_column_conf
			RsColumnConf rsColumnConf = rsColumnConfMapper.selectOneById(Integer.parseInt(id));
			// 根据rs_t_id 查 rs_table_conf
			RsTableConf rsTableConf = rsTableConfMapper.selectById(rsColumnConf.getRs_t_id());

			// 拼接sql
			StringBuilder builder = new StringBuilder("select ");
			builder.append(rsColumnConf.getRsc_name());
			builder.append(" from ");
			builder.append(rsTableConf.getDs_name());
			builder.append(" where 1=1 ");
			// HR系统param
			if (rsReport != null) {
				Map<String, Object> reportParams = mergeReportParams(rsReport);
				for (String key : reportParams.keySet()) {
					String jsonParam = null;
					JSONArray arrayParam = (JSONArray) reportParams.get(key);
					for (int i = 0; i < arrayParam.size(); i++) {
						String value = (String) arrayParam.get(i);
						jsonParam = value + ",";
					}
					jsonParam = jsonParam.substring(0, jsonParam.length() - 1);
					builder.append(" and " + key + " in ('" + jsonParam + "') ");
				}
			}
			builder.append(" group by ");
			builder.append(rsColumnConf.getRsc_name());

			String sql = builder.toString();
			log.info("搜索框下拉框查询 sql: "+sql);

			// 开始进行数据库查询
			Map map = new HashMap();
			List<Map<String, Object>> list = jdbcDao.query(sql, map);
			for (Map m : list) {
				for (Object val : m.values()) {
					restList.add(val);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restList;
	}

}
