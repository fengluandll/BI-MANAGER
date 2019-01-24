package com.zhisiyun.bi.defaultDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.defaultBean.Mcharts;

public interface MchartsMapper {
	List<Mcharts> selectAll();

	Mcharts selectOne(@Param("id") String id, @Param("dashboard_id") String dashboard_id);

	Mcharts selectOneById(@Param("id") String id);
	
	Mcharts selectOneBySnId(@Param("sn_id") Integer sn_id);
	
	Mcharts selectSearchByBoardId(@Param("dashboard_id") String dashboard_id);// 根据dashboardid找到搜索框mchart

	List<Mcharts> selectById(@Param("dashboard_id") String dashboard_id);

	int updateById(Mcharts mcharts);
	
	int updateBySnId(@Param("sn_id") Integer sn_id, @Param("id") String id, @Param("is_active") String is_active);

	int newChart(@Param("dashboard_id") String dashboard_id, @Param("name") String name,
			@Param("config") String config);

	int newChartByBean(Mcharts mcharts);

	int deleteChart(@Param("id") String id, @Param("dashboard_id") String dashboard_id);
	
	int deleteChartBySnId(@Param("sn_id") Integer sn_id);

}
