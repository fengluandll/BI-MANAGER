package com.zhisiyun.bi.defaultDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.defaultBean.Mdashboard;

public interface MdashboardMapper {

	List<Mdashboard> selectAll();

	Mdashboard selectById(@Param("id") String id);

	List<Mdashboard> selectByName(@Param("name") String name);

	int updateById(@Param("id") String id, @Param("style_config") String style_config);

	int add(@Param("name") String name);
	
	int addByBean(Mdashboard mdashboard);

}
