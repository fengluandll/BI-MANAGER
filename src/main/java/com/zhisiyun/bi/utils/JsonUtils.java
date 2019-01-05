package com.zhisiyun.bi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhisiyun.bi.bean.defaultBean.Mdashboard;
import com.zhisiyun.bi.bean.reportPro.MchartsPro;
import com.zhisiyun.bi.bean.reportPro.SearchBoardJson;
import com.zhisiyun.bi.bean.reportPro.SearchRelation;

public class JsonUtils {
	/********************************************************************************************************************/
	// 拆解m_dashboard 的 style_config 获取chart
	public List<MchartsPro> getChartsJson(JSONArray array) {
		List<MchartsPro> list = new ArrayList<MchartsPro>();
		try {
			if (array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					// 取得每个 root 下的 JSONObject
					JSONObject obj = array.getJSONObject(i);
					String type = obj.getString("type");
					if (type.equals("line") || type.equals("bar") || type.equals("pie") || type.equals("table")
							|| type.equals("pivottable") || type.equals("perspective")) {
						MchartsPro mchartsPro = new MchartsPro();
						mchartsPro.setName(obj.getString("name"));
						mchartsPro.setType(obj.getString("type"));
						mchartsPro.setChartId(obj.getString("chartId"));
						mchartsPro.setFatherName(obj.getString("fatherName"));
						list.add(mchartsPro);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/********************************************************************************************************************/

	// 遍历 获取 所有的搜索框
	public List<SearchBoardJson> getSearches(JSONArray array) {
		List<SearchBoardJson> list = new ArrayList<SearchBoardJson>();
		try {
			if (array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					// 取得每个 root 下的 JSONObject
					JSONObject obj = array.getJSONObject(i);
					String type = obj.getString("type");
					if (type.equals("search")) {
						SearchBoardJson searchBoardJson = JSONObject.toJavaObject(obj, SearchBoardJson.class);
						list.add(searchBoardJson);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/********************************************************************************************************************/

	// 根据图表的id获取它关联的其他的图表的Id
	// {"name":"66","type":"pie","chartId":"66","fatherName":"root","styleConfig":"","relation":{"1791":{"relationFields":{}},"1794":{"relationFields":{"67":["66:1790"]}}}}
	/**
	 * chartId点击表的Id columnId点击表的字段id
	 **/
	public List<String[]> getPlotClickCharts(JSONArray array, String chartId, String columnId) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			if (StringUtils.isNotEmpty(chartId) && StringUtils.isNotEmpty(columnId) && array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					// 取得每个 root 下的 JSONObject
					JSONObject obj = array.getJSONObject(i);
					String name = obj.getString("name");
					if (name.equals(chartId)) {
						// 这是点击plot图表的json
						SearchBoardJson searchBoardJson = JSONObject.toJavaObject(obj, SearchBoardJson.class);
						Map<String, SearchRelation> relation = searchBoardJson.getRelation();
						for (Map.Entry<String, SearchRelation> entry : relation.entrySet()) {// 循环每个chart的relation的json
							Map<String, String[]> relationFields = entry.getValue().getRelationFields();
							String key = entry.getKey(); // relation里的Key,也就是被点击图表的字段
							if (key.equals(columnId)) {
								for (Map.Entry<String, String[]> subEntry : relationFields.entrySet()) {// 循环每个chart里relation里的relationFields
									String ret[] = new String[2];
									String subKey = subEntry.getKey();// relationFileds里的主key,就是被关联图表的id
									String value = subEntry.getValue()[0];// relationFileds里的值,也就是被关联的图表的字段的名称
									// value是 "66:1790" 也就是 被点击表id 加被关键表的字段id
									value = value.substring(value.indexOf(":") + 1, value.length());
									ret[0] = subKey;
									ret[1] = value;
									list.add(ret);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/********************************************************************************************************************/
	/**
	 * @设置m_dashboard的style_config从children中选一个
	 * @dashboard最后要用的
	 * @mDashboard查出来的原始的
	 **/
	public void setMdashboard_style_config(Mdashboard dashboard, Mdashboard mDashboard, String activeName) {
		JSONObject style_config_obj = JSON.parseObject(mDashboard.getStyle_config());
		JSONObject children = style_config_obj.getJSONObject("children");
		// 如果 activeName=="" 就取第一个
		if (null == activeName || "".equals(activeName)) {
			// 遍历
			for (Entry<String, Object> entry : children.entrySet()) {
				JSONObject child = (JSONObject) entry.getValue();
				if (child.getInteger("order") == 1) {
					dashboard.setStyle_config(child.toString());
				}
			}
		} else {
			// 根据activeName取style_config并放入dashboard
			dashboard.setStyle_config(children.getJSONObject(activeName).toString());
		}
	}
	/********************************************************************************************************************/

}
