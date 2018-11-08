package com.zhisiyun.bi.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;

public class SqlUtils {

	static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * 获取图表数据查询SQL
	 * 
	 * @param table
	 * @param dimensions
	 * @param measures
	 * @param legends
	 * @param params
	 *            条件查询参数
	 * @param map
	 *            查询参数值s
	 * @param report
	 *            报表
	 * @return
	 */
	public String assemble(String table, RsColumnConf dimension, RsColumnConf measure, RsColumnConf legend,
			JSONObject params, Map map) {
		// 按图例-维度项分组查询维度，统计度量值时需要判断是否是计算字段，不是计算字段则统一按聚合函数count()处理

		StringBuilder builder = new StringBuilder("select ");
		if (legend != null) {
			builder.append(legend.getRsc_name());
			builder.append(" color, ");
		}
		if (!dimension.getIs_calc().equals("Y") && dimension.getRsc_type() == 3) {
			builder.append(" to_char(" + dimension.getRsc_name() + ",'YYYY-MM-DD') ");
			builder.append(" x, ");
		} else {
			builder.append(dimension.getRsc_name());
			builder.append(" x, ");
		}
		if (measure.getIs_calc().equals("Y")) {
			builder.append(measure.getRsc_name());
		} else {
			builder.append("count(1)");
		}
		builder.append(" y from ");
		builder.append(table);
		//mergeSqlParams(builder, params, map);
		// 按图例-维度分组
		builder.append(" group by ");
		if (legend != null) {
			builder.append(legend.getRsc_name());
			builder.append(" , ");
		}
		builder.append(dimension.getRsc_name());
		builder.append(" limit 10 ");
		return builder.toString();
	}

	/**
	 * 获取table明细数据查询SQL
	 * 
	 * @param table
	 * @param dimensions
	 * @param measures
	 * @param params
	 *            条件查询参数
	 * @param map
	 *            查询参数值s
	 * @param report
	 *            报表
	 * @return
	 */
	public String assemble(String table, List<RsColumnConf> list, Map map, List header) {
		// 按图例-维度项分组查询维度，统计度量值时需要判断是否是计算字段，不是计算字段则统一按聚合函数count()处理

		StringBuilder builder = new StringBuilder("select ");
		List<String> cols = new ArrayList<>();
		for (RsColumnConf rs : list) {
			if (rs.getIs_calc().equals("N") && rs.getRsc_category().equals("2")) {
				cols.add(" sum(" + rs.getRsc_name() + ") as " + rs.getRsc_display());
			} else {
				cols.add(rs.getRsc_name() + " as " + rs.getRsc_display());
			}
			header.add(rs.getRsc_display());
		}
		builder.append(StringUtils.join(cols.toArray(), ","));
		builder.append(" from ");
		builder.append(table);
		builder.append(" limit 10 ");
		return builder.toString();
	}

}
