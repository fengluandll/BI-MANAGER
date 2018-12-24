package com.zhisiyun.bi.defaultDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.defaultBean.RsReport;

public interface RsReportMapper {

	int add(@Param("reportId") String reportId, @Param("pageId") String pageId, @Param("params") String params);

	RsReport selectByReportId(@Param("reportId") String reportId);
	
	List<RsReport> selectByPageId(@Param("pageId") String pageId);

}
