package com.zhisiyun.bi.defaultDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.defaultBean.Mcharts;

public interface MchartsMapper {
	List<Mcharts> selectAll();

	Mcharts selectOne(@Param("id") Integer id, @Param("dashboard_id") Integer dashboard_id);

	Mcharts selectOneById(@Param("id") Integer id);

	List<Mcharts> selectById(@Param("dashboard_id") Integer dashboard_id);

	int updateById(@Param("id") Integer id, @Param("name") String name, @Param("config") String config);

	int newChart(@Param("dashboard_id") Integer dashboard_id, @Param("name") String name,
			@Param("config") String config);

	int newChartByBean(Mcharts mcharts);

	int deleteChart(@Param("id") Integer id, @Param("dashboard_id") Integer dashboard_id);

}
