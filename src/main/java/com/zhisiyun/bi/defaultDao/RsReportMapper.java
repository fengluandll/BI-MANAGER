package com.zhisiyun.bi.defaultDao;

import org.apache.ibatis.annotations.Param;

import com.zhisiyun.bi.bean.defaultBean.RsReport;

public interface RsReportMapper {

	int add(@Param("reportId") String reportId, @Param("pageId") String pageId, @Param("params") String params);

	RsReport selectByReportId(@Param("reportId") String reportId);
	
	RsReport selectByPageId(@Param("pageId") String pageId);

}
