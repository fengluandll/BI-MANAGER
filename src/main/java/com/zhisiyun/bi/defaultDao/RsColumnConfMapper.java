package com.zhisiyun.bi.defaultDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.defaultBean.RsColumnConf;

public interface RsColumnConfMapper {

	List<RsColumnConf> selectAll();

	List<RsColumnConf> selectByTableId(@Param("rs_t_id") Integer rs_t_id);
	
	List<String> selectIdsByTableId(@Param("rs_t_id") Integer rs_t_id);

	List<RsColumnConf> selectByTableIdAndType(@Param("rs_t_id") Integer rs_t_id,
			@Param("rsc_category") Integer rsc_category);

	RsColumnConf selectOneById(@Param("id") Integer id);

	List<RsColumnConf> selectByIds(@Param("ids") List<Integer> ids);

	int addByBean(RsColumnConf rsColumnConf);

}
