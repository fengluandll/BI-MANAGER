package com.zhisiyun.bi.defaultDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.defaultBean.RsTableConf;

public interface RsTableConfMapper {

	List<RsTableConf> selectAll();

	List<RsTableConf> selectByName(@Param("ds_name") String ds_name);

	RsTableConf selectById(@Param("id") String id);
	
	RsTableConf selectByDsName(@Param("ds_name") String ds_name);

	List<RsTableConf> selectByNameOrDisplay(@Param("ds_name") String ds_name);

	int addByBean(RsTableConf rsTableConf);

	int addDiyByBean(RsTableConf rsTableConf);

	int updateByBean(RsTableConf rsTableConf);

	List<RsTableConf> selectByIdList(@Param("idList") List<String> idList);

	int makeTableAndColumn(@Param("sn_id") Integer sn_id);

}
