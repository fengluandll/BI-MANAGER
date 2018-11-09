package com.zhisiyun.bi.defaultDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.defaultBean.RsTableConf;

public interface RsTableConfMapper {

	List<RsTableConf> selectAll();

	List<RsTableConf> selectByName(@Param("ds_name") String ds_name);

	RsTableConf selectById(@Param("id") Integer id);
	
	List<RsTableConf> selectByNameOrDisplay(@Param("ds_name") String ds_name);

}
