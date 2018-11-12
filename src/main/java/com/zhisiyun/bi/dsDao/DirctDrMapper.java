package com.zhisiyun.bi.dsDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.ds.DirctDr;

public interface DirctDrMapper {

	List<DirctDr> selectAll();

	DirctDr selectByCode(@Param("code") String code);

}
