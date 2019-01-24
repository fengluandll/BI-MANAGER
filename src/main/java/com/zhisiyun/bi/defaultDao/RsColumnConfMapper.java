package com.zhisiyun.bi.defaultDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;

public interface RsColumnConfMapper {

	List<RsColumnConf> selectAll();

	List<RsColumnConf> selectByTableId(@Param("rs_t_id") String rs_t_id);
	
	List<String> selectIdsByTableId(@Param("rs_t_id") String rs_t_id);
	
	List<String> selectIdsByTableIds(@Param("rs_t_ids") List<String> rs_t_ids);

	List<RsColumnConf> selectByTableIdAndType(@Param("rs_t_id") String rs_t_id,
			@Param("type_ids") List<String> type_ids);

	RsColumnConf selectOneById(@Param("id") String id);

	List<RsColumnConf> selectByIds(@Param("ids") List<String> ids);

	int addByBean(RsColumnConf rsColumnConf);

}
